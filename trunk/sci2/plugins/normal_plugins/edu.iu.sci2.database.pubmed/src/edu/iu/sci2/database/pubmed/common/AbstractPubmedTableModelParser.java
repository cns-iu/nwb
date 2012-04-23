package edu.iu.sci2.database.pubmed.common;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.cishell.service.database.Database;
import org.osgi.service.log.LogService;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.EntityContainer;
import edu.iu.cns.database.load.framework.RelationshipContainer;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.scholarly.model.entity.Address;
import edu.iu.sci2.database.scholarly.model.entity.Author;
import edu.iu.sci2.database.scholarly.model.entity.CitedPatent;
import edu.iu.sci2.database.scholarly.model.entity.CitedReference;
import edu.iu.sci2.database.scholarly.model.entity.Document;
import edu.iu.sci2.database.scholarly.model.entity.DocumentKeyword;
import edu.iu.sci2.database.scholarly.model.entity.DocumentOccurrence;
import edu.iu.sci2.database.scholarly.model.entity.Editor;
import edu.iu.sci2.database.scholarly.model.entity.ISIFile;
import edu.iu.sci2.database.scholarly.model.entity.Keyword;
import edu.iu.sci2.database.scholarly.model.entity.Patent;
import edu.iu.sci2.database.scholarly.model.entity.Person;
import edu.iu.sci2.database.scholarly.model.entity.Publisher;
import edu.iu.sci2.database.scholarly.model.entity.PublisherAddress;
import edu.iu.sci2.database.scholarly.model.entity.Reference;
import edu.iu.sci2.database.scholarly.model.entity.ReprintAddress;
import edu.iu.sci2.database.scholarly.model.entity.ResearchAddress;
import edu.iu.sci2.database.scholarly.model.entity.Source;
import edu.iu.sci2.medline.common.MedlineField;
import edu.iu.sci2.medline.common.MedlineUtilities;
import edu.iu.sci2.medline.common.MedlineUtilities.NameParsingException;
import edu.iu.sci2.medline.common.MedlineUtilities.ParsedName;

/**
 * An abstract class for creating a {@link DatabaseModel} for a Generic
 * Scholarly {@linkplain Database} for MEDLINE®/PubMed® data.
 */
public abstract class AbstractPubmedTableModelParser {
	protected List<RowItemContainer<? extends RowItem<?>>> dbTables;
	protected RowItemContainer<Document> documentsTable;
	protected RowItemContainer<Person> peopleTable;
	protected RowItemContainer<Address> addressesTable;
	protected RowItemContainer<Source> sourcesTable;
	protected RowItemContainer<Keyword> keywordsTable;
	protected RowItemContainer<Author> authorsTable;
	protected RowItemContainer<ReprintAddress> reprintAddressesTable;
	protected RowItemContainer<Editor> editorsTable;
	protected RowItemContainer<DocumentKeyword> documentKeywordsTable;
	protected RowItemContainer<ResearchAddress> researchAddressesTable;

	public AbstractPubmedTableModelParser() {
		makeTables();
	}

	protected void makeTables() {
		this.dbTables = Lists.newArrayList();

		// Entities (things that mostly hold data themselves)
		this.documentsTable = createEntityContainer(ISI.DOCUMENT_TABLE_NAME,
				Document.SCHEMA);
		this.dbTables.add(this.documentsTable);

		this.peopleTable = createEntityContainer(ISI.PERSON_TABLE_NAME,
				Person.SCHEMA);
		this.dbTables.add(this.peopleTable);

		this.addressesTable = createEntityContainer(ISI.ADDRESS_TABLE_NAME,
				Address.SCHEMA);
		this.dbTables.add(this.addressesTable);

		this.sourcesTable = createEntityContainer(ISI.SOURCE_TABLE_NAME,
				Source.SCHEMA);
		this.dbTables.add(this.sourcesTable);

		this.keywordsTable = createEntityContainer(ISI.KEYWORD_TABLE_NAME,
				Keyword.SCHEMA);
		this.dbTables.add(this.keywordsTable);

		// Not filled with data, but here to complete the table model
		this.dbTables.add(createEntityContainer(ISI.REFERENCE_TABLE_NAME,
				Reference.SCHEMA));
		this.dbTables.add(createEntityContainer(ISI.PATENT_TABLE_NAME,
				Patent.SCHEMA));
		this.dbTables.add(createEntityContainer(ISI.ISI_FILE_TABLE_NAME,
				ISIFile.SCHEMA));
		this.dbTables.add(createEntityContainer(ISI.PUBLISHER_TABLE_NAME,
				Publisher.SCHEMA));

		// Relationships (things that are mostly links between other things)
		this.authorsTable = createRelationshipContainer(ISI.AUTHORS_TABLE_NAME,
				Author.SCHEMA);
		this.dbTables.add(this.authorsTable);

		this.reprintAddressesTable = createRelationshipContainer(
				ISI.REPRINT_ADDRESSES_TABLE_NAME, ReprintAddress.SCHEMA);
		this.dbTables.add(this.reprintAddressesTable);

		this.editorsTable = createRelationshipContainer(ISI.EDITORS_TABLE_NAME,
				Editor.SCHEMA);
		this.dbTables.add(this.editorsTable);

		this.documentKeywordsTable = createRelationshipContainer(
				ISI.DOCUMENT_KEYWORDS_TABLE_NAME, DocumentKeyword.SCHEMA);
		this.dbTables.add(this.documentKeywordsTable);

		this.researchAddressesTable = createRelationshipContainer(
				ISI.RESEARCH_ADDRESSES_TABLE_NAME, ResearchAddress.SCHEMA);
		this.dbTables.add(this.researchAddressesTable);

		// Not filled with data, but here to complete the table model.
		this.dbTables.add(createRelationshipContainer(
				ISI.CITED_REFERENCES_TABLE_NAME, CitedReference.SCHEMA));
		this.dbTables.add(createRelationshipContainer(
				ISI.CITED_PATENTS_TABLE_NAME, CitedPatent.SCHEMA));
		this.dbTables
				.add(createRelationshipContainer(
						ISI.DOCUMENT_OCCURRENCES_TABLE_NAME,
						DocumentOccurrence.SCHEMA));
		this.dbTables.add(createRelationshipContainer(
				ISI.PUBLISHER_ADDRESSES_TABLE_NAME, PublisherAddress.SCHEMA));
	}

	abstract public DatabaseModel getModel();

	protected static final Map<Document.Field, MedlineField> DOCUMENT_TO_MEDLINE = new HashMap<Document.Field, MedlineField>() {
		protected static final long serialVersionUID = -2605777104843557919L;
		{
			put(Document.Field.ABSTRACT, MedlineField.ABSTRACT);
			put(Document.Field.DIGITAL_OBJECT_IDENTIFIER,
					MedlineField.ARTICLE_IDENTIFIER);
			put(Document.Field.FUNDING_AGENCY_AND_GRANT_NUMBER,
					MedlineField.GRANT_NUMBER);
			put(Document.Field.ISSUE, MedlineField.ISSUE);
			put(Document.Field.LANGUAGE, MedlineField.LANGUAGE);
			put(Document.Field.PUBLICATION_DATE,
					MedlineField.DATE_OF_PUBLICATION);
			put(Document.Field.ISI_TYPE, MedlineField.PUBLICATION_TYPE);
			put(Document.Field.TITLE, MedlineField.TITLE);
			put(Document.Field.VOLUME, MedlineField.VOLUME);
		}
	};

	protected static final Map<Source.Field, MedlineField> SOURCE_TO_MEDLINE = new HashMap<Source.Field, MedlineField>() {
		protected static final long serialVersionUID = 1919394068003255939L;

		{
			put(Source.Field.ISSN, MedlineField.ISSN);
			put(Source.Field.FULL_TITLE, MedlineField.SOURCE);
			put(Source.Field.ISO_TITLE_ABBREVIATION, MedlineField.JOURNAL_TITLE);
			put(Source.Field.TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION,
					MedlineField.JOURNAL_TITLE_ABBREVIATION);
		}
	};

	protected static List<Person> getPeople(
			DatabaseTableKeyGenerator keyGenerator,
			ImmutableList<String> names, LogService logger, MedlineField field) {

		List<Person> people = new ArrayList<Person>();

		for (String name : names) {
			Dictionary<String, Object> attributes = new Hashtable<String, Object>();
			attributes.put(Person.Field.RAW_NAME.name(), name);
			try {
				ParsedName parsedName = MedlineUtilities.parseName(field, name);

				
				if (! parsedName.getForeName().isEmpty()) {
					attributes.put(Person.Field.FIRST_NAME.name(),
							parsedName.getForeName());
				}
				
				if (! parsedName.getInitials().isEmpty()) {
					String firstInitial = ""
							+ parsedName.getInitials().charAt(0);
					attributes.put(Person.Field.FIRST_INITIAL.name(),
							firstInitial);

					if (parsedName.getInitials().length() > 1) {
						String middleInitial = ""
								+ parsedName.getInitials().charAt(1);
						attributes.put(Person.Field.MIDDLE_INITIAL.name(),
								middleInitial);
					}
				}
				
				if (! parsedName.getLastName().isEmpty()) {
					attributes.put(Person.Field.FAMILY_NAME.name(),
							parsedName.getLastName());
				}
				
				String fullName = getFullName(parsedName);
				
				if (! fullName.isEmpty()) {
					attributes.put(Person.Field.FULL_NAME.name(), fullName);
				}
				
			} catch (NameParsingException e) {
				logger.log(
						LogService.LOG_ERROR,
						"'"
								+ name
								+ "' could not be parsed.  The 'People' table will be missing values.");
			}
			people.add(new Person(keyGenerator, attributes));
		}

		return people;
	}
	
	protected static String getFullName(ParsedName parsedName) {
		StringBuilder fullName = new StringBuilder();
		if (!parsedName.getForeName().isEmpty()) {
			fullName.append(parsedName.getForeName());
			if (!parsedName.getInitials().isEmpty()) {
				if (parsedName.getInitials().length() > 1) {
					fullName.append(parsedName.getInitials().charAt(1));
				}
			}
		} else {
			if (!parsedName.getInitials().isEmpty()) {
				fullName.append(parsedName.getInitials());
			}
		}

		if (!parsedName.getLastName().isEmpty()) {
			fullName.append(parsedName.getLastName());
		}
		return fullName.toString();
	}

	protected static Document makeDocument(
			DatabaseTableKeyGenerator keyGenerator,
			Map<MedlineField, Object> medlineValues, LogService logger) {
		// SOMEDAY try to combine with sources
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		for (Document.Field documentField : DOCUMENT_TO_MEDLINE.keySet()) {

			MedlineField medlineField = DOCUMENT_TO_MEDLINE.get(documentField);

			if (! DerbyFieldType.typesAreCompatible(medlineField.getFieldType(), documentField.type())) {
					logger.log(
							LogService.LOG_WARNING,
							"The medline field "
									+ medlineField
									+ " did not match the type of the document field "
									+ documentField
									+ ".\nThe created database table might have errors as a result of skipping this field.");
					continue;
			}
			Object medlineValue = medlineValues.get(medlineField);
			if (medlineValue != null) {
				attributes.put(documentField.name(), medlineValue);
			} else {
				logger.log(LogService.LOG_DEBUG, "No value was found for "
						+ documentField);
			}

		}
		return new Document(keyGenerator, attributes);
	}

	protected static Source makeSource(DatabaseTableKeyGenerator keyGenerator,
			Map<MedlineField, Object> medlineValues, LogService logger) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		for (Source.Field sourceField : SOURCE_TO_MEDLINE.keySet()) {

			MedlineField medlineField = SOURCE_TO_MEDLINE.get(sourceField);

			if (medlineField.getFieldType() == String.class) {
				// TODO better type checking
				if (sourceField.type() != DerbyFieldType.TEXT) {
					logger.log(
							LogService.LOG_WARNING,
							"The medline field "
									+ medlineField
									+ " did not match the type of the source field "
									+ sourceField
									+ ".\nThe created database table might have errors as a result of skipping this field.");
					continue;
				}
			} else {
				logger.log(LogService.LOG_ERROR,
						"Currently, only String medline fields are supported.");
			}

			Object medlineValue = medlineValues.get(medlineField);
			if (medlineValue != null) {
				attributes.put(sourceField.name(), medlineValue);
			} else {
				logger.log(LogService.LOG_DEBUG, "No value was found for "
						+ sourceField);
			}

		}
		return new Source(keyGenerator, attributes);
	}

	public static ImmutableList<Keyword> makeKeywords(
			ImmutableList<String> keywordNames, String keywordType,
			DatabaseTableKeyGenerator keyGenerator) {
		List<Keyword> keywords = new ArrayList<Keyword>();
		for (String keyword : keywordNames) {
			Dictionary<String, Object> attributes = new Hashtable<String, Object>();
			attributes.put(Keyword.Field.NAME.name(), keyword);
			attributes.put(Keyword.Field.TYPE.name(), keywordType);
			keywords.add(new Keyword(keyGenerator, attributes));
		}
		return ImmutableList.copyOf(keywords);
	}

	public static ImmutableList<Address> makeAddresses(
			DatabaseTableKeyGenerator keyGenerator, List<String> addressValues) {
		List<Address> addresses = new ArrayList<Address>();

		for (String address : addressValues) {
			Dictionary<String, Object> attributes = new Hashtable<String, Object>();
			attributes.put(Address.Field.RAW_ADDRESS.name(), address);
			addresses.add(new Address(keyGenerator, attributes));
		}

		return ImmutableList.copyOf(addresses);
	}

	protected static <T extends Entity<T>> EntityContainer<T> createEntityContainer(
			String tableName, Schema<T> schema) {
		return new EntityContainer<T>(tableName, schema);
	}

	protected static <T extends RowItem<T>> RowItemContainer<T> createRelationshipContainer(
			String tableName, Schema<T> schema) {
		return new RelationshipContainer<T>(tableName, schema);
	}

}
