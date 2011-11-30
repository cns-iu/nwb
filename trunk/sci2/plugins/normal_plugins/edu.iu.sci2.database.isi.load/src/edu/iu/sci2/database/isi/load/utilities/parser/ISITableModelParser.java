package edu.iu.sci2.database.isi.load.utilities.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.utilities.ArrayUtilities;
import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.ProgressMonitorUtilities;
import org.cishell.utilities.StringUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.cns.database.load.framework.EntityContainer;
import edu.iu.cns.database.load.framework.RelationshipContainer;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.ISITableReader;
import edu.iu.nwb.shared.isiutil.ISITag;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.model.entity.Address;
import edu.iu.sci2.database.isi.load.model.entity.Document;
import edu.iu.sci2.database.isi.load.model.entity.ISIFile;
import edu.iu.sci2.database.isi.load.model.entity.Keyword;
import edu.iu.sci2.database.isi.load.model.entity.Patent;
import edu.iu.sci2.database.isi.load.model.entity.Person;
import edu.iu.sci2.database.isi.load.model.entity.Publisher;
import edu.iu.sci2.database.isi.load.model.entity.Reference;
import edu.iu.sci2.database.isi.load.model.entity.Source;
import edu.iu.sci2.database.isi.load.model.entity.relationship.Author;
import edu.iu.sci2.database.isi.load.model.entity.relationship.CitedPatent;
import edu.iu.sci2.database.isi.load.model.entity.relationship.CitedReference;
import edu.iu.sci2.database.isi.load.model.entity.relationship.DocumentKeyword;
import edu.iu.sci2.database.isi.load.model.entity.relationship.DocumentOccurrence;
import edu.iu.sci2.database.isi.load.model.entity.relationship.Editor;
import edu.iu.sci2.database.isi.load.model.entity.relationship.PublisherAddress;
import edu.iu.sci2.database.isi.load.model.entity.relationship.ReprintAddress;
import edu.iu.sci2.database.isi.load.model.entity.relationship.ResearchAddress;
import edu.iu.sci2.database.isi.load.utilities.parser.exception.ReferenceParsingException;

public class ISITableModelParser {
	public static final String AUTHOR_KEYWORDS = "authorKeywords";
	public static final String KEYWORDS_PLUS = "keywordsPlus";

	public static final int BATCH_SIZE = 100;

	private ProgressMonitor progressMonitor;

	/*
	 * For each type of entity (ISI File, Publisher, Source, Reference, Address, Keyword,
	 *  Person, Patent, and Document), create a master list of entities.
	 */

	/*
	 * Surprising!
	 * 
	 * The .SCHEMA static attributes of these classes are actually mutable objects!
	 * So any extra fields (e.g. in Document) do get conveyed to these EntityContainers,
	 * through this backwards route.
	 */
	private RowItemContainer<ISIFile> isiFiles = new EntityContainer<ISIFile>(
			ISI.ISI_FILE_DISPLAY_NAME, ISI.ISI_FILE_TABLE_NAME, ISIFile.SCHEMA, BATCH_SIZE);
	private RowItemContainer<Publisher> publishers = new EntityContainer<Publisher>(
			ISI.PUBLISHER_DISPLAY_NAME, ISI.PUBLISHER_TABLE_NAME, Publisher.SCHEMA, BATCH_SIZE);
	private RowItemContainer<Source> sources = new EntityContainer<Source>(
		ISI.SOURCE_DISPLAY_NAME, ISI.SOURCE_TABLE_NAME, Source.SCHEMA, BATCH_SIZE);
	private RowItemContainer<Reference> references = new EntityContainer<Reference>(
		ISI.REFERENCE_DISPLAY_NAME, ISI.REFERENCE_TABLE_NAME, Reference.SCHEMA, BATCH_SIZE);
	private RowItemContainer<Address> addresses = new EntityContainer<Address>(
			ISI.ADDRESS_DISPLAY_NAME, ISI.ADDRESS_TABLE_NAME, Address.SCHEMA, BATCH_SIZE);
	private RowItemContainer<Keyword> keywords = new EntityContainer<Keyword>(
			ISI.KEYWORD_DISPLAY_NAME, ISI.KEYWORD_TABLE_NAME, Keyword.SCHEMA, BATCH_SIZE);
	private RowItemContainer<Person> people = new EntityContainer<Person>(
		ISI.PERSON_DISPLAY_NAME, ISI.PERSON_TABLE_NAME, Person.SCHEMA, BATCH_SIZE);
	private RowItemContainer<Patent> patents = new EntityContainer<Patent>(
			ISI.PATENT_DISPLAY_NAME, ISI.PATENT_TABLE_NAME, Patent.SCHEMA, BATCH_SIZE);
	private RowItemContainer<Document> documents = new EntityContainer<Document>(
			ISI.DOCUMENT_DISPLAY_NAME, ISI.DOCUMENT_TABLE_NAME, Document.SCHEMA, BATCH_SIZE);

	/*
	 * Create all of the entity joining tables (Publisher Addresses, Reprint Addresses,
	 *  Research Addresses, Document Keywords, Authors, Editors, Cited Patents, Document
	 *  Occurrences, and Cited References).
	 */

	private RowItemContainer<PublisherAddress> publisherAddresses =
		new RelationshipContainer<PublisherAddress>(
			ISI.PUBLISHER_ADDRESSES_DISPLAY_NAME,
			ISI.PUBLISHER_ADDRESSES_TABLE_NAME,
			PublisherAddress.SCHEMA,
			BATCH_SIZE);

	private RowItemContainer<ReprintAddress> reprintAddresses =
		new RelationshipContainer<ReprintAddress>(
			ISI.REPRINT_ADDRESSES_DISPLAY_NAME,
			ISI.REPRINT_ADDRESSES_TABLE_NAME,
			ReprintAddress.SCHEMA,
			BATCH_SIZE);

	private RowItemContainer<ResearchAddress> researchAddresses =
		new RelationshipContainer<ResearchAddress>(
			ISI.RESEARCH_ADDRESSES_DISPLAY_NAME,
			ISI.RESEARCH_ADDRESSES_TABLE_NAME,
			ResearchAddress.SCHEMA,
			BATCH_SIZE);

	private RowItemContainer<DocumentKeyword> documentKeywords =
		new RelationshipContainer<DocumentKeyword>(
			ISI.DOCUMENT_KEYWORDS_DISPLAY_NAME,
			ISI.DOCUMENT_KEYWORDS_TABLE_NAME,
			DocumentKeyword.SCHEMA,
			BATCH_SIZE);

	private RowItemContainer<Author> authors =
		new RelationshipContainer<Author>(
			ISI.AUTHORS_DISPLAY_NAME,
			ISI.AUTHORS_TABLE_NAME,
			Author.SCHEMA,
			BATCH_SIZE);

	private RowItemContainer<Editor> editors =
		new RelationshipContainer<Editor>(
			ISI.EDITORS_DISPLAY_NAME,
			ISI.EDITORS_TABLE_NAME,
			Editor.SCHEMA,
			BATCH_SIZE);

	private RowItemContainer<CitedPatent> citedPatents =
		new RelationshipContainer<CitedPatent>(
			ISI.CITED_PATENTS_DISPLAY_NAME,
			ISI.CITED_PATENTS_TABLE_NAME,
			CitedPatent.SCHEMA,
			BATCH_SIZE);

	private RowItemContainer<DocumentOccurrence> documentOccurrences =
		new RelationshipContainer<DocumentOccurrence>(
			ISI.DOCUMENT_OCCURRENCES_DISPLAY_NAME,
			ISI.DOCUMENT_OCCURRENCES_TABLE_NAME,
			DocumentOccurrence.SCHEMA,
			BATCH_SIZE);

	private RowItemContainer<CitedReference> citedReferences =
		new RelationshipContainer<CitedReference>(
			ISI.CITED_REFERENCES_DISPLAY_NAME,
			ISI.CITED_REFERENCES_TABLE_NAME,
			CitedReference.SCHEMA,
			BATCH_SIZE);

	public ISITableModelParser(ProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	public DatabaseModel parseModel(Table table, Collection<Integer> rows)
			throws AlgorithmCanceledException {
		// For each record/row in the table:

		int last = 0;
		int total = 0;
		double unitsWorked = 0;

		for (Integer rowIndex : rows) {
			ProgressMonitorUtilities.handleCanceledOrPausedAlgorithm(progressMonitor);

			Tuple row = table.getTuple(rowIndex.intValue());

			// Parse (Author) People.

			List<Person> currentAuthorPeople = parseAuthorPeople(row);

			// Parse (Editor) People.

			List<Person> currentEditorPeople = parseEditorPeople(row);

			// Parse ISI File.

			ISIFile isiFile = parseISIFile(row);

			/*
			 * Parse Author Keywords, and link the Document Keywords from the Document to them
			 *  via DocumentKeywords.
			 */

			List<Keyword> authorKeywords =
				parseKeywords(row, AUTHOR_KEYWORDS, ISITag.ORIGINAL_KEYWORDS);

			/*
			 * Parse KeyWords Plus, and link the Document Keywords from the Document to them
			 *  via DocumentKeywords.
			 */

			List<Keyword> keywordsPlus =
				parseKeywords(row, KEYWORDS_PLUS, ISITag.NEW_KEYWORDS_GIVEN_BY_ISI);

			// Parse Patent, and link it to the Document via CitedPatents.

			Patent patent = parsePatent(row);

			// Parse Reprint Address (a link between Document and Address).

			Address addressForReprinting = parseAddressForReprinting(row);

			// Parse Research Addresses (links between Document and Address).

			List<Address> currentAddressesOfResearch = parseAddressesOfResearch(row);

			// Parse References, and link them to People and Sources.

			List<Reference> currentReferences = parseReferences(row);

			handlePeopleAndSourcesFromReferences(currentReferences);

			// Parse Source.

			Source source = parseSource(row);

			// Parse Publisher Address (a link between Publisher and Address).

			Address addressOfPublisher = parseAddressOfPublisher(row);

			// Parse Publisher.

			Publisher publisher = parsePublisher(row);

			// Link the Publisher to the other things parsed.

			if ((publisher != null) && (source != null)) {
				publisher.setSource(source);
			}

			if ((publisher != null) && (addressOfPublisher != null)) {
				this.publisherAddresses.add(
						new PublisherAddress(publisher, addressOfPublisher));
			}

			// Parse Document.

			Document document = parseDocument(row, currentAuthorPeople, source);

			// Link the Document to the other things parsed.

			if (document != null) {
				linkDocumentToPeople_AsAuthors(document, currentAuthorPeople, row);
				linkDocumentToPeople_AsEditors(document, currentEditorPeople, row);
			}

			if ((document != null) && (isiFile != null)) {
				this.documentOccurrences.add(new DocumentOccurrence(document, isiFile));
			}

			if (document != null) {
				linkDocumentToKeywords(document, authorKeywords);
				linkDocumentToKeywords(document, keywordsPlus);
			}

			if (patent != null) {
				this.citedPatents.add(new CitedPatent(document, patent));
			}

			if ((document != null) && (addressForReprinting != null)) {
				this.reprintAddresses.add(
						new ReprintAddress(document, addressForReprinting));
			}

			if (document != null) {
				linkDocumentToAddressesOfResearch(document, currentAddressesOfResearch);
				linkDocumentToCitedReferences(document, currentReferences);
			}

			this.progressMonitor.worked(unitsWorked);
			unitsWorked++;

			last++;
			total++;

			if (last == 100) {
				System.err.println("Completed " + total + " so far.");
				last = 0;
			}
		}

		linkReferencesToDocuments();

		// Given all of the master lists of row items, construct an DatabaseModel and return it.

		return new DatabaseModel(
			// Entities
			this.documents,
			this.addresses,
			this.isiFiles,
			this.keywords,
			this.patents,
			this.publishers,
			this.references,
			this.people,
			this.sources,
			// Relationships
			this.authors,
			this.editors,
			this.citedPatents,
			this.citedReferences,
			this.documentKeywords,
			this.documentOccurrences,
			this.publisherAddresses,
			this.reprintAddresses,
			this.researchAddresses);
	}

	private List<Person> parseAuthorPeople(Tuple row) {
		List<Person> authorPeople = new ArrayList<Person>();

		String rawAuthorsString =
			StringUtilities.simpleClean(row.getString(ISITag.AUTHORS.getColumnName()));
		String[] authorStrings = rawAuthorsString.split("\\|");
		String rawFullAuthorNamesString = StringUtilities.simpleClean(
				row.getString(ISITag.AUTHORS_FULL_NAMES.getColumnName()));
		String[] authorFullNameStrings = rawFullAuthorNamesString.split("\\|");

		for (int ii = 0; ii < authorStrings.length; ii++) {
			String authorString = authorStrings[ii];
			String cleanedAuthorString = StringUtilities.simpleClean(authorString);

			if (StringUtilities.isNull_Empty_OrWhitespace(cleanedAuthorString)) {
				continue;
			}

			Person authorPerson =
				parseAuthorPerson(authorStrings, authorFullNameStrings, cleanedAuthorString, ii);

			Person mergedAuthorPerson = this.people.add(authorPerson);
			authorPeople.add(mergedAuthorPerson);
		}

		return authorPeople;
	}

	private Person parseAuthorPerson(
			String[] authorStrings,
			String[] authorFullNameStrings,
			String cleanedAuthorString,
			int authorIndex) {
		if (authorStrings.length == authorFullNameStrings.length) {
			String authorFullNameString = authorFullNameStrings[authorIndex];
			String cleanedAuthorFullNameString =
				StringUtilities.simpleClean(authorFullNameString);
			return new Person(
				this.people.getKeyGenerator(),
				cleanedAuthorString,
				cleanedAuthorFullNameString);
		} else {
			return new Person(this.people.getKeyGenerator(), cleanedAuthorString, "");
		}
	}

	private List<Person> parseEditorPeople(Tuple row) {
		List<Person> editorPeople = new ArrayList<Person>();

		String rawEditorsString =
			StringUtilities.simpleClean(row.getString(ISITag.EDITORS.getColumnName()));
		String[] editorStrings = rawEditorsString.split("\\|");

		for (int ii = 0; ii < editorStrings.length; ii++) {
			String editorString = editorStrings[ii];
			String cleanedEditorString = StringUtilities.simpleClean(editorString);

			if (!StringUtilities.isNull_Empty_OrWhitespace(cleanedEditorString)) {
				Person editorPerson = new Person(
					this.people.getKeyGenerator(), cleanedEditorString, "");
				Person mergedEditorPerson = this.people.add(editorPerson);
				editorPeople.add(mergedEditorPerson);
			}
		}

		return editorPeople;
	}

	private ISIFile parseISIFile(Tuple row) {
		String formatVersionNumber =
			StringUtilities.trimIfNotNull(row.getString(ISITag.VERSION_NUMBER.getColumnName()));
		String fileName =
			StringUtilities.trimIfNotNull(row.getString(ISITableReader.FILE_PATH_COLUMN_NAME));
		String fileType =
			StringUtilities.trimIfNotNull(row.getString(ISITag.FILE_TYPE.getColumnName()));

		if (!StringUtilities.allAreEmptyOrWhitespace(formatVersionNumber, fileName, fileType)) {
			ISIFile isiFile = new ISIFile(
					this.isiFiles.getKeyGenerator(), formatVersionNumber, fileName, fileType);

			return this.isiFiles.add(isiFile);
		} else {
			return null;
		}
	}

	private List<Keyword> parseKeywords(
			Tuple row, String keywordType, ISITag sourceTag) {
		List<Keyword> keywords = new ArrayList<Keyword>();

		String rawKeywordsString =
			StringUtilities.simpleClean(row.getString(sourceTag.getColumnName()));
		String[] keywordStrings = rawKeywordsString.split("\\|");

		for (String keywordString : keywordStrings) {
			String cleanedKeywordString = StringUtilities.trimIfNotNull(keywordString);

			if (!StringUtilities.isNull_Empty_OrWhitespace(keywordString)) {
				Keyword keyword = this.keywords.add(new Keyword(
						this.keywords.getKeyGenerator(), cleanedKeywordString, keywordType));
				keywords.add(keyword);
			}
		}

		return keywords;
	}

	private Patent parsePatent(Tuple row) {
		String patentNumber =
			StringUtilities.trimIfNotNull(row.getString(ISITag.CITED_PATENT.getColumnName()));

		if (!StringUtilities.isNull_Empty_OrWhitespace(patentNumber)) {
			return this.patents.add(
					new Patent(this.patents.getKeyGenerator(), patentNumber));
		} else {
			return null;
		}
	}

	private Address parseAddressForReprinting(Tuple row) {
		String addressForReprintingString =
			StringUtilities.trimIfNotNull(row.getString(ISITag.REPRINT_ADDRESS.getColumnName()));

		if (!StringUtilities.isNull_Empty_OrWhitespace(addressForReprintingString)) {
			// TODO: AddressParser?
			return this.addresses.add(
					new Address(this.addresses.getKeyGenerator(), addressForReprintingString));
		} else {
			return null;
		}
	}

	private List<Address> parseAddressesOfResearch(Tuple row) {
		List<Address> addressesOfResearch = new ArrayList<Address>();

		String rawAddressesString =
			StringUtilities.simpleClean(row.getString(ISITag.RESEARCH_ADDRESSES.getColumnName()));
		String addressStrings[] = rawAddressesString.split("\\|");

		for (String addressString : addressStrings) {
			String cleanedAddressString = StringUtilities.trimIfNotNull(addressString);

			if (!StringUtilities.isNull_Empty_OrWhitespace(cleanedAddressString)) {
				// TODO: AddressParser?
				Address mergedAddressOfResearch = this.addresses.add(
						new Address(this.addresses.getKeyGenerator(), cleanedAddressString));
				addressesOfResearch.add(mergedAddressOfResearch);
			}
		}

		return addressesOfResearch;
	}

	private List<Reference> parseReferences(Tuple row) {
		List<Reference> currentReferences = new ArrayList<Reference>();
		String rawReferencesString =
			StringUtilities.simpleClean(row.getString(ISITag.CITED_REFERENCES.getColumnName()));
		String[] referenceStrings = rawReferencesString.split("\\|");

		for (int ii = 0; ii < referenceStrings.length; ii++) {
			String referenceString = referenceStrings[ii];

			if (StringUtilities.isNull_Empty_OrWhitespace(referenceString)) {
				continue;
			}

			try {
				ReferenceDataParser referenceData = new ReferenceDataParser(
						this.people.getKeyGenerator(),
						this.sources.getKeyGenerator(),
						referenceString);
				Source referenceSource = referenceData.getSource();

				if (!ArrayUtilities.allAreNull(
						referenceData.getArticleNumber(),
						referenceData.getDigitalObjectIdentifier(),
						referenceData.getRawString(),
						referenceSource)) {
					Reference reference = new Reference(
						this.references.getKeyGenerator(),
						this.people,
						this.sources.getKeyGenerator(),
						referenceSource,
						row,
						ii);
					Reference mergedReference = this.references.add(reference);
					currentReferences.add(mergedReference);
				}
			} catch (ReferenceParsingException e) {
				// TODO: Print a warning?  For now, it's just skipped.
			}
		}

		return currentReferences;
	}

	private void handlePeopleAndSourcesFromReferences(List<Reference> currentReferences) {
		for (Reference reference : currentReferences) {
			Source referenceSource = reference.getSource();

			if (referenceSource != null) {
				Source mergedSource = this.sources.add(referenceSource);
				reference.setSource(mergedSource);
			}
		}
	}

	private Document parseDocument(Tuple row, List<Person> authorPeople, Source source) {
		Person firstAuthor = null;

		if (authorPeople.size() != 0) {
			firstAuthor = authorPeople.iterator().next();
		}

		String documentAbstract =
			StringUtilities.trimIfNotNull(row.getString(ISITag.ABSTRACT.getColumnName()));
		String articleNumber =
			StringUtilities.trimIfNotNull(
				row.getString(ISITag.ARTICLE_NUMBER_OF_NEW_APS_JOURNALS.getColumnName()));

		Integer beginningPage = IntegerParserWithDefault.parse(
			StringUtilities.trimIfNotNull(row.getString(ISITag.BEGINNING_PAGE.getColumnName())));

		Integer citedReferenceCount = IntegerParserWithDefault.parse(StringUtilities.trimIfNotNull(
			row.getString(ISITag.CITED_REFERENCE_COUNT.getColumnName())));
		Integer citedYear = IntegerParserWithDefault.parse(StringUtilities.trimIfNotNull(
			row.getString(ISITag.CITED_YEAR.getColumnName())));

		String digitalObjectidentifier =
			StringUtilities.trimIfNotNull(row.getString(ISITag.DOI.getColumnName()));
		String documentType =
			StringUtilities.trimIfNotNull(row.getString(ISITag.DOCUMENT_TYPE.getColumnName()));
		Integer documentVolume = IntegerParserWithDefault.parse(StringUtilities.trimIfNotNull(
			row.getString(ISITag.VOLUME.getColumnName())));

		Integer endingPage = IntegerParserWithDefault.parse(
			StringUtilities.trimIfNotNull(row.getString(ISITag.ENDING_PAGE.getColumnName())));

		String fundingAgencyAndGrantNumber = StringUtilities.trimIfNotNull(
			row.getString(ISITag.FUNDING_AGENCY_AND_GRANT_NUMBER.getColumnName()));
		String fundingText =
			StringUtilities.trimIfNotNull(row.getString(ISITag.FUNDING_TEXT.getColumnName()));

		String isbn = StringUtilities.trimIfNotNull(row.getString(ISITag.ISBN.getColumnName()));
		String isiDocumentDeliveryNumber = StringUtilities.trimIfNotNull(
			row.getString(ISITag.ISI_DOCUMENT_DELIVERY_NUMBER.getColumnName()));
		String isiUniqueArticleIdentifier =
			StringUtilities.trimIfNotNull(row.getString(ISITag.UNIQUE_ID.getColumnName()));
		String issue = StringUtilities.trimIfNotNull(row.getString(ISITag.ISSUE.getColumnName()));

		String language =
			StringUtilities.trimIfNotNull(row.getString(ISITag.LANGUAGE.getColumnName()));

		Integer pageCount = IntegerParserWithDefault.parse(
			StringUtilities.trimIfNotNull(row.getString(ISITag.NUMBER_OF_PAGES.getColumnName())));
		String partNumber =
			StringUtilities.trimIfNotNull(row.getString(ISITag.PART_NUMBER.getColumnName()));
		String publicationDate =
			StringUtilities.trimIfNotNull(row.getString(ISITag.PUBLICATION_DATE.getColumnName()));
		Integer publicationYear = IntegerParserWithDefault.parse(
			StringUtilities.trimIfNotNull(row.getString(ISITag.PUBLICATION_YEAR.getColumnName())));

		String specialIssue =
			StringUtilities.trimIfNotNull(row.getString(ISITag.SPECIAL_ISSUE.getColumnName()));
		String subjectCategory =
			StringUtilities.trimIfNotNull(row.getString(ISITag.SUBJECT_CATEGORY.getColumnName()));
		String supplement =
			StringUtilities.trimIfNotNull(row.getString(ISITag.SUPPLEMENT.getColumnName()));

		Integer timesCited = IntegerParserWithDefault.parse(
			StringUtilities.trimIfNotNull(row.getString(ISITag.TIMES_CITED.getColumnName())));
		String title = StringUtilities.trimIfNotNull(row.getString(ISITag.TITLE.getColumnName()));

		if (!ArrayUtilities.allAreNull(
				beginningPage,
				citedReferenceCount,
				citedYear,
				documentVolume,
				endingPage,
				firstAuthor,
				pageCount,
				publicationYear,
				source,
				timesCited) &&
				!StringUtilities.allAreNull_Empty_OrWhitespace(
						documentAbstract,
						articleNumber,
						digitalObjectidentifier,
						documentType,
						fundingAgencyAndGrantNumber,
						fundingText,
						isbn,
						isiDocumentDeliveryNumber,
						isiUniqueArticleIdentifier,
						issue,
						language,
						partNumber,
						publicationDate,
						specialIssue,
						subjectCategory,
						supplement,
						title)) {
			Document document = new Document(
				this.documentKeywords.getKeyGenerator(),
				articleNumber,
				digitalObjectidentifier,
				firstAuthor,
				source,
				row);

			for (int ii = 0; ii < row.getColumnCount(); ii++) {
				String columnName = row.getColumnName(ii);

				if (ISITag.getArbitraryTag(columnName) != null) {
					String value = row.getString(ii);
					document.addArbitraryAttribute(columnName, value);
				}
			}

			return this.documents.add(document);
		} else {
			return null;
		}
	}

	private void linkDocumentToPeople_AsAuthors(
			Document document, List<Person> authorPeople, Tuple row) {
		String rawEmailAddressesString =
			StringUtilities.simpleClean(row.getString(ISITag.EMAIL_ADDRESSES.getColumnName()));
		String[] emailAddressStrings = rawEmailAddressesString.split("\\|");

		if (emailAddressStrings.length != authorPeople.size()) {
			// TODO: Warning or fail?
			// Assume just the first e-mail address for all authors.
			String emailAddress = null;

			if (emailAddressStrings.length != 0) {
				emailAddress = emailAddressStrings[0];
			}

			Iterator<Person> personIterator = authorPeople.iterator();

			for (int ii = 0; ii < authorPeople.size(); ii++) {
				Person authorPerson = personIterator.next();
				this.authors.add(new Author(document, authorPerson, emailAddress, ii));
			}
		} else {
			Iterator<Person> personIterator = authorPeople.iterator();

			for (int ii = 0; ii < authorPeople.size(); ii++) {
				Person authorPerson = personIterator.next();
				String emailAddress = emailAddressStrings[ii];
				this.authors.add(new Author(document, authorPerson, emailAddress, ii));
			}
		}
	}

	private void linkDocumentToPeople_AsEditors(
			Document document, List<Person> editorPeople, Tuple row) {
		Iterator<Person> personIterator = editorPeople.iterator();

		for (int ii = 0; ii < editorPeople.size(); ii++) {
			Person editorPerson = personIterator.next();
			this.editors.add(new Editor(document, editorPerson, ii));
		}
	}

	private void linkDocumentToKeywords(Document document, List<Keyword> keywords) {
		for (int ii = 0; ii < keywords.size(); ii++) {
			this.documentKeywords.add(
					new DocumentKeyword(document, keywords.get(ii), ii));
		}
	}

	private void linkDocumentToAddressesOfResearch(
			Document document, List<Address> addressesOfResearch) {
		for (int ii = 0; ii < addressesOfResearch.size(); ii++) {
			Address addressOfResearch = addressesOfResearch.get(ii);
			this.researchAddresses.add(
					new ResearchAddress(document, addressOfResearch, ii));
		}
	}

	private void linkDocumentToCitedReferences(Document document, List<Reference> references) {
		for (Reference reference : references) {
			this.citedReferences.add(new CitedReference(document, reference));
		}
	}

	private Source parseSource(Tuple row) {
		String bookSeriesTitle =
			StringUtilities.trimIfNotNull(row.getString(ISITag.BOOK_SERIES_TITLE.getColumnName()));
		String bookSeriesSubtitle = StringUtilities.trimIfNotNull(
				row.getString(ISITag.BOOK_SERIES_SUBTITLE.getColumnName()));

		String conferenceHost =
			StringUtilities.trimIfNotNull(row.getString(ISITag.CONFERENCE_HOST.getColumnName()));
		String conferenceLocation = StringUtilities.trimIfNotNull(
				row.getString(ISITag.CONFERENCE_LOCATION.getColumnName()));
		String conferenceSponsors = StringUtilities.trimIfNotNull(
				row.getString(ISITag.CONFERENCE_SPONSORS.getColumnName()));
		String conferenceTitle =
			StringUtilities.trimIfNotNull(row.getString(ISITag.CONFERENCE_TITLE.getColumnName()));

		String fullTitle = StringUtilities.trimIfNotNull(
				row.getString(ISITag.FULL_JOURNAL_TITLE.getColumnName()));

		String isoTitleAbbreviation = StringUtilities.trimIfNotNull(
				row.getString(ISITag.ISO_JOURNAL_TITLE_ABBREVIATION.getColumnName()));
		String issn =
			StringUtilities.trimIfNotNull(row.getString(ISITag.ISSN.getColumnName()));

		String publicationType =
			StringUtilities.trimIfNotNull(row.getString(ISITag.PUBLICATION_TYPE.getColumnName()));

		String twentyNineCharacterSourceTitleAbbreviation = StringUtilities.trimIfNotNull(
				row.getString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION.getColumnName()));

		if (!StringUtilities.allAreNull_Empty_OrWhitespace(
				bookSeriesTitle,
				bookSeriesSubtitle,
				conferenceHost,
				conferenceLocation,
				conferenceSponsors,
				conferenceTitle,
				fullTitle,
				isoTitleAbbreviation,
				issn,
				publicationType,
				twentyNineCharacterSourceTitleAbbreviation)) {
			Source source = new Source(
				this.sources.getKeyGenerator(),
				twentyNineCharacterSourceTitleAbbreviation,
				row);

			return this.sources.add(source);
		} else {
			return null;
		}
	}

	private Address parseAddressOfPublisher(Tuple row) {
		String addressOfPublisherString =
			StringUtilities.trimIfNotNull(row.getString(ISITag.PUBLISHER_ADDRESS.getColumnName()));

		if (!StringUtilities.isNull_Empty_OrWhitespace(addressOfPublisherString)) {
			return this.addresses.add(
					new Address(this.addresses.getKeyGenerator(), addressOfPublisherString));
		} else {
			return null;
		}
	}

	private Publisher parsePublisher(Tuple row) {
		String city =
			StringUtilities.trimIfNotNull(row.getString(ISITag.PUBLISHER_CITY.getColumnName()));
		String name =
			StringUtilities.trimIfNotNull(row.getString(ISITag.PUBLISHER.getColumnName()));
		String webAddress = StringUtilities.trimIfNotNull(
				row.getString(ISITag.PUBLISHER_WEB_ADDRESS.getColumnName()));

		if (!StringUtilities.allAreNull_Empty_OrWhitespace(city, name, webAddress)) {
			Publisher publisher =
				new Publisher(this.publishers.getKeyGenerator(), city, name, webAddress);

			return this.publishers.add(publisher);
		} else {
			return null;
		}
	}

	private void linkReferencesToDocuments() {
		Map<String, Document> digitalObjectIdentifiersToDocuments =
			new HashMap<String, Document>();
		Map<String, Document> articleNumbersToDocuments = new HashMap<String, Document>();

		for (Document document : this.documents.getItems()) {
			String documentDigitalObjectIdentifier = document.getDigitalObjectIdentifier();
			String documentArticleNumber = document.getArticleNumber();

			if (!StringUtilities.isNull_Empty_OrWhitespace(documentDigitalObjectIdentifier)) {
				digitalObjectIdentifiersToDocuments.put(documentDigitalObjectIdentifier, document);
			}

			if (!StringUtilities.isNull_Empty_OrWhitespace(documentArticleNumber)) {
				articleNumbersToDocuments.put(documentArticleNumber, document);
			}
		}

		for (Reference reference : this.references.getItems()) {
			String referenceDigitalObjectIdentifier = reference.getDigitalObjectIdentifier();
			String referenceArticleNumber = reference.getArticleNumber();

			if (!StringUtilities.isNull_Empty_OrWhitespace(referenceDigitalObjectIdentifier)) {
				if (digitalObjectIdentifiersToDocuments.containsKey(
						referenceDigitalObjectIdentifier)) {
					reference.setPaper(
						digitalObjectIdentifiersToDocuments.get(referenceDigitalObjectIdentifier));
				}
			} else if (!StringUtilities.isNull_Empty_OrWhitespace(referenceArticleNumber)) {
				if (articleNumbersToDocuments.containsKey(referenceArticleNumber)) {
					reference.setPaper(articleNumbersToDocuments.get(referenceArticleNumber));
				}
			}
		}
	}
}