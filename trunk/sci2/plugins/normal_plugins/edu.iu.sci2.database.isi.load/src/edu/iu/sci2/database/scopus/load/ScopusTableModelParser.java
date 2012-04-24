package edu.iu.sci2.database.scopus.load;

import static edu.iu.sci2.database.scopus.load.EntityUtils.getNullableInteger;
import static edu.iu.sci2.database.scopus.load.EntityUtils.putField;
import static edu.iu.sci2.database.scopus.load.EntityUtils.putPK;
import static edu.iu.sci2.database.scopus.load.EntityUtils.putValue;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.util.TableIterator;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.EntityContainer;
import edu.iu.cns.database.load.framework.RelationshipContainer;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.utilities.parser.AbbreviatedNameParser;
import edu.iu.sci2.database.isi.load.utilities.parser.exception.PersonParsingException;
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

public class ScopusTableModelParser {

	private final LogService logger;

	public ScopusTableModelParser(LogService logger) {
		this.logger = logger;
	}


	public DatabaseModel parseModel(Table table) {
		List<RowItemContainer<? extends RowItem<?>>> dbTables = Lists.newArrayList();
		
		// Entities (things that mostly hold data themselves)
		RowItemContainer<Document> documents = createEntityContainer(
				ISI.DOCUMENT_TABLE_NAME, Document.SCHEMA);
		dbTables.add(documents);
		
		RowItemContainer<Person> people = createEntityContainer(
				ISI.PERSON_TABLE_NAME, Person.SCHEMA);
		dbTables.add(people);
		
		RowItemContainer<Address> addresses = createEntityContainer(
				ISI.ADDRESS_TABLE_NAME, Address.SCHEMA);
		dbTables.add(addresses);
		
		RowItemContainer<Source> sources = createEntityContainer(
				ISI.SOURCE_TABLE_NAME, Source.SCHEMA);
		dbTables.add(sources);
		
		RowItemContainer<Keyword> keywords = createEntityContainer(
				ISI.KEYWORD_TABLE_NAME, Keyword.SCHEMA);
		dbTables.add(keywords);
		
		// Not filled with data, but here to complete the table model
		dbTables.add(createEntityContainer(ISI.REFERENCE_TABLE_NAME, Reference.SCHEMA));
		dbTables.add(createEntityContainer(ISI.PATENT_TABLE_NAME, Patent.SCHEMA));
		dbTables.add(createEntityContainer(ISI.ISI_FILE_TABLE_NAME, ISIFile.SCHEMA));
		dbTables.add(createEntityContainer(ISI.PUBLISHER_TABLE_NAME, Publisher.SCHEMA));
		
		
		// Relationships (things that are mostly links between other things)
		RowItemContainer<Author> authors = createRelationshipContainer(
				ISI.AUTHORS_TABLE_NAME, Author.SCHEMA);
		dbTables.add(authors);
		
		RowItemContainer<ReprintAddress> reprintAddresses = createRelationshipContainer(
				ISI.REPRINT_ADDRESSES_TABLE_NAME, ReprintAddress.SCHEMA);
		dbTables.add(reprintAddresses);
		
		RowItemContainer<Editor> editors = createRelationshipContainer(
				ISI.EDITORS_TABLE_NAME, Editor.SCHEMA);
		dbTables.add(editors);
		
		RowItemContainer<DocumentKeyword> documentKeywords = createRelationshipContainer(
				ISI.DOCUMENT_KEYWORDS_TABLE_NAME, DocumentKeyword.SCHEMA);
		dbTables.add(documentKeywords);
		
		RowItemContainer<ResearchAddress> researchAddresses = createRelationshipContainer(
				ISI.RESEARCH_ADDRESSES_TABLE_NAME, ResearchAddress.SCHEMA);
		dbTables.add(researchAddresses);

		// Not filled with data, but here to complete the table model.
		dbTables.add(createRelationshipContainer(ISI.CITED_REFERENCES_TABLE_NAME, CitedReference.SCHEMA));
		dbTables.add(createRelationshipContainer(ISI.CITED_PATENTS_TABLE_NAME, CitedPatent.SCHEMA));
		dbTables.add(createRelationshipContainer(ISI.DOCUMENT_OCCURRENCES_TABLE_NAME, DocumentOccurrence.SCHEMA));
		dbTables.add(createRelationshipContainer(ISI.PUBLISHER_ADDRESSES_TABLE_NAME, PublisherAddress.SCHEMA));
		
		TableIterator rowNumbers = table.iterator();
		while (rowNumbers.hasNext()) {
			int rowNum = rowNumbers.nextInt();
			FileTuple<ScopusField> row = new FileTuple<ScopusField>(table.getTuple(rowNum));

			Source source = getSource(row, sources.getKeyGenerator());
			sources.add(source);
			
			
			List<Person> authorPeople = splitAndParsePersonList(row, ScopusField.AUTHORS, 
					people.getKeyGenerator());
			Person firstAuthor = null;
			if (!authorPeople.isEmpty()) firstAuthor = authorPeople.get(0);
			
			Document document = new Document(documents.getKeyGenerator(), 
					getDocumentAttribs(row, source, firstAuthor));
			documents.add(document);
			
			addAll(people, authorPeople);
			addAll(authors, Author.makeAuthors(document, authorPeople));
			
			// I have tried, and can't find *any* SCOPUS data with entries in the Editors column.
			// So this is totally untested on "real" data :-(
			// -Thomas
			List<Person> editorPeople = splitAndParsePersonList(row, ScopusField.EDITORS, 
					people.getKeyGenerator());
			addAll(people, editorPeople);
			addAll(editors, Editor.makeEditors(document, editorPeople));
			
			List<Keyword> authorKeywords = splitKeywords(row, ScopusField.AUTHOR_KEYWORDS, 
					keywords.getKeyGenerator());
			addAll(keywords, authorKeywords);
			addAll(documentKeywords, DocumentKeyword.makeDocumentKeywords(document, authorKeywords));
			
			List<Keyword> indexKeywords = splitKeywords(row, ScopusField.INDEX_KEYWORDS,
					keywords.getKeyGenerator());
			addAll(keywords,  indexKeywords);
			addAll(documentKeywords, DocumentKeyword.makeDocumentKeywords(document, indexKeywords));
			
			// TODO: make sure nothing depends on the ISI *values* for Keyword.Field.TYPE
			// ISITag.NEW_KEYWORDS_GIVEN_BY_ISI, ISITag.ORIGINAL_KEYWORDS
			
			List<Address> researchAddrAddrs = splitResearchAddresses(row, addresses.getKeyGenerator());
			addAll(addresses, researchAddrAddrs);
			addAll(researchAddresses, ResearchAddress.makeResearchAddresses(document, researchAddrAddrs));
			
			Address reprintAddrAddr = getReprintAddrAddr(row, addresses.getKeyGenerator());
			addresses.add(reprintAddrAddr);
			reprintAddresses.add(ReprintAddress.link(document, reprintAddrAddr));
		}
		
		
		return new DatabaseModel(dbTables);
	}


	private List<Person> splitAndParsePersonList(FileTuple<ScopusField> row,
			ScopusField fieldToExtract,
			DatabaseTableKeyGenerator keyGenerator) {
		List<Person> people = new ArrayList<Person>();
		Dictionary<String, Object> attribs;
		AbbreviatedNameParser nameParser;
		
		for (String name : getSplitField(row, fieldToExtract)) {
			attribs = new Hashtable<String, Object>();
			putValue(attribs, Person.Field.RAW_NAME, name);
			putValue(attribs, Person.Field.FULL_NAME, name);
			
//			try {
//				nameParser = new AbbreviatedNameParser(name);
//				putValue(attribs, Person.Field.FIRST_INITIAL, nameParser.firstInitial);
//				putValue(attribs, Person.Field.FAMILY_NAME, nameParser.familyName);
//				putValue(attribs, Person.Field.MIDDLE_INITIAL, nameParser.middleInitials);
//			} catch (PersonParsingException e) {
//				String toLog = String.format("Couldn't parse name %s", name);
//				if (this.logger != null) {
//					this.logger.log(LogService.LOG_INFO, toLog);
//				}
//			}
		
			people.add(new Person(keyGenerator, attribs));
		}
		
		return people;
	}


	private static <T extends Entity<T>> EntityContainer<T> createEntityContainer(String tableName, Schema<T> schema) {
		return new EntityContainer<T>(tableName, schema, 100);
	}
	private static <T extends RowItem<T>> RowItemContainer<T> createRelationshipContainer(String tableName, Schema<T> schema) {
		return new RelationshipContainer<T>(tableName, schema, 100);
	}
	
	private static List<Address> splitResearchAddresses(
			FileTuple<ScopusField> row, DatabaseTableKeyGenerator keyGenerator) {
		List<Address> addresses = Lists.newArrayList();
		
		for (String address : getSplitField(row, ScopusField.AFFILIATIONS)) {
			Dictionary<String, Object> attribs = EntityUtils.newDictionary();
			
			putValue(attribs, Address.Field.RAW_ADDRESS, address.trim());
			addresses.add(new Address(keyGenerator, attribs));
		}
		
		return addresses;
	}


	private static <T extends RowItem<T>> void addAll(RowItemContainer<T> container, Iterable<T> items) {
		for (T item : items) {
			container.add(item);
		}
	}
	
	private static Iterable<String> getSplitField(FileTuple<ScopusField> row, 
			ScopusField fieldToSplit) {
		Splitter splitter = ScopusField.getSplitterFor(fieldToSplit);
		Preconditions.checkNotNull(splitter, "No splitter available for field %s", fieldToSplit);
		return splitter.split(row.getStringField(fieldToSplit));
	}
	
	private static List<Keyword> splitKeywords(FileTuple<ScopusField> row,
			ScopusField fieldToSplit, DatabaseTableKeyGenerator keyGenerator) {
		List<Keyword> keywords = Lists.newArrayList();
		
		for (String keyword : getSplitField(row, fieldToSplit)) {
			Dictionary<String, Object> attribs = new Hashtable<String, Object>();
			putValue(attribs, Keyword.Field.NAME, keyword.trim());
			putValue(attribs, Keyword.Field.TYPE, fieldToSplit.toString());
			keywords.add(new Keyword(keyGenerator, attribs));
		}
		
		return keywords;
	}

	private static Source getSource(FileTuple<ScopusField> row,
			DatabaseTableKeyGenerator keyGenerator) {
		Dictionary<String, Object> attribs = new Hashtable<String, Object>();
		
		putField(attribs, Source.Field.PUBLICATION_TYPE, row, ScopusField.DOCUMENT_TYPE);
		
		putField(attribs, Source.Field.ISSN, row, ScopusField.ISSN);
		
		putField(attribs, Source.Field.FULL_TITLE, row, ScopusField.SOURCE_TITLE);
		
		putField(attribs, Source.Field.CONFERENCE_TITLE, row, ScopusField.CONFERENCE_NAME);
		putField(attribs, Source.Field.CONFERENCE_LOCATION, row, ScopusField.CONFERENCE_LOCATION);
		putField(attribs, Source.Field.CONFERENCE_DATES, row, ScopusField.CONFERENCE_DATE);
		
		// You might think that ScopusField.ABBREVIATED_SOURCE_TITLE would be shorter than
		// ScopusField.SOURCE_TITLE, but you'd be wrong.  :-/
		// (at least in my sample data)
		
		// ScopusField.SOURCE is always "Scopus" AFAIK.  Not related to the "source" database table.
		
		return new Source(keyGenerator, attribs);
	}

	private static Address getReprintAddrAddr(FileTuple<ScopusField> row, DatabaseTableKeyGenerator keyGenerator) {
		Dictionary<String, Object> attribs = new Hashtable<String, Object>();
		
		EntityUtils.putField(attribs, Address.Field.RAW_ADDRESS, row, ScopusField.CORRESPONDENCE_ADDRESS);
		return new Address(keyGenerator, attribs);
	}

	private static Dictionary<String, Object> getDocumentAttribs(FileTuple<ScopusField> row, Source source, Person firstAuthor) {
		Dictionary<String, Object> attribs = new Hashtable<String, Object>();
		
		putPK(attribs, Document.Field.SOURCE_ID, source);
		putPK(attribs, Document.Field.FIRST_AUTHOR_ID, firstAuthor);
		
		putField(attribs, Document.Field.ABSTRACT, row, ScopusField.ABSTRACT);
		putField(attribs, Document.Field.DOCUMENT_NUMBER, row, ScopusField.ART_NO);
		putField(attribs, Document.Field.DIGITAL_OBJECT_IDENTIFIER, row, ScopusField.DOI);
		putField(attribs, Document.Field.TITLE, row, ScopusField.TITLE);
		putField(attribs, Document.Field.PUBLICATION_YEAR, row, ScopusField.YEAR);
		putField(attribs, Document.Field.PUBLICATION_DATE, row, ScopusField.YEAR);
		putField(attribs, Document.Field.TIMES_CITED, row, ScopusField.CITED_BY);
		putField(attribs, Document.Field.ISBN, row, ScopusField.ISBN);
		putField(attribs, Document.Field.ISSUE, row, ScopusField.ISSUE);
		putField(attribs, Document.Field.LANGUAGE, row, ScopusField.LANGUAGE_OF_ORIGINAL_DOCUMENT);
		putField(attribs, Document.Field.VOLUME, row, ScopusField.VOLUME);
		
		
		// Make our own Page Count, since it's often missing from the file.
		Integer beginningPage = getNullableInteger(row, ScopusField.PAGE_START),
				endingPage = getNullableInteger(row, ScopusField.PAGE_START),
				pageCount = getNullableInteger(row, ScopusField.PAGE_COUNT);
		if (pageCount == null && beginningPage != null && endingPage != null) {
			pageCount = endingPage - beginningPage;
		}
				
		putValue(attribs, Document.Field.BEGINNING_PAGE, beginningPage);
		putValue(attribs, Document.Field.ENDING_PAGE, endingPage);
		putValue(attribs, Document.Field.PAGE_COUNT, pageCount);
		
				
		return attribs;
	}
	

}


