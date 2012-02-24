package edu.iu.sci2.database.scopus.load;

import static edu.iu.sci2.database.scopus.load.EntityUtils.*;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cishell.framework.algorithm.ProgressMonitor;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import prefuse.data.Table;
import prefuse.data.util.TableIterator;
import edu.iu.cns.database.load.framework.EntityContainer;
import edu.iu.cns.database.load.framework.RelationshipContainer;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.utilities.parser.AbbreviatedNameParser;
import edu.iu.sci2.database.isi.load.utilities.parser.exception.PersonParsingException;
import edu.iu.sci2.database.scholarly.model.entity.Address;
import edu.iu.sci2.database.scholarly.model.entity.Author;
import edu.iu.sci2.database.scholarly.model.entity.Document;
import edu.iu.sci2.database.scholarly.model.entity.DocumentKeyword;
import edu.iu.sci2.database.scholarly.model.entity.Editor;
import edu.iu.sci2.database.scholarly.model.entity.Keyword;
import edu.iu.sci2.database.scholarly.model.entity.Person;
import edu.iu.sci2.database.scholarly.model.entity.ReprintAddress;
import edu.iu.sci2.database.scholarly.model.entity.Source;

public class ScopusTableModelParser {

	public ScopusTableModelParser(ProgressMonitor progressMonitor) {
		// TODO Auto-generated constructor stub
	}

	public DatabaseModel parseModel(Table table) {
		RowItemContainer<Document> documents = new EntityContainer<Document>(
				ISI.DOCUMENT_TABLE_NAME, Document.SCHEMA);
		RowItemContainer<Person> people = new EntityContainer<Person>(
				ISI.PERSON_TABLE_NAME, Person.SCHEMA);
		RowItemContainer<Address> addresses = new EntityContainer<Address>(
				ISI.ADDRESS_TABLE_NAME, Address.SCHEMA);
		RowItemContainer<Source> sources = new EntityContainer<Source>(
				ISI.SOURCE_TABLE_NAME, Source.SCHEMA);
		RowItemContainer<Keyword> keywords = new EntityContainer<Keyword>(
				ISI.KEYWORD_TABLE_NAME, Keyword.SCHEMA);

		RowItemContainer<Author> authors = new RelationshipContainer<Author>(
				ISI.AUTHORS_TABLE_NAME, Author.SCHEMA);
		RowItemContainer<ReprintAddress> reprintAddresses = new RelationshipContainer<ReprintAddress>(
				ISI.REPRINT_ADDRESSES_TABLE_NAME, ReprintAddress.SCHEMA);
		RowItemContainer<Editor> editors = new RelationshipContainer<Editor>(
				ISI.EDITORS_TABLE_NAME, Editor.SCHEMA);
		RowItemContainer<DocumentKeyword> documentKeywords = new RelationshipContainer<DocumentKeyword>(
				ISI.DOCUMENT_KEYWORDS_TABLE_NAME, DocumentKeyword.SCHEMA);

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
			
			
			Address reprintAddrAddr = getReprintAddrAddr(row, addresses.getKeyGenerator());
			addresses.add(reprintAddrAddr);
			ReprintAddress reprintAddr = ReprintAddress.link(document, reprintAddrAddr);
			reprintAddresses.add(reprintAddr);
		}
		return new DatabaseModel(documents, people, authors, addresses, 
				reprintAddresses, sources, editors, keywords, documentKeywords);
	}
	
	private static <T extends RowItem<T>> void addAll(RowItemContainer<T> container, Iterable<T> items) {
		for (T item : items) {
			container.add(item);
		}
	}
	
	private List<Keyword> splitKeywords(FileTuple<ScopusField> row,
			ScopusField fieldToSplit, DatabaseTableKeyGenerator keyGenerator) {
		List<Keyword> keywords = Lists.newArrayList();
		
		for (String keyword : row.getStringField(fieldToSplit).split(";")) {
			Dictionary<String, Object> attribs = new Hashtable<String, Object>();
			putValue(attribs, Keyword.Field.KEYWORD, keyword.trim());
			putValue(attribs, Keyword.Field.TYPE, fieldToSplit.toString());
			keywords.add(new Keyword(keyGenerator, attribs));
		}
		
		return keywords;
	}

	private Source getSource(FileTuple<ScopusField> row,
			DatabaseTableKeyGenerator keyGenerator) {
		Dictionary<String, Object> attribs = new Hashtable<String, Object>();
		
		putField(attribs, Source.Field.PUBLICATION_TYPE, row, ScopusField.DOCUMENT_TYPE);
		
		putField(attribs, Source.Field.ISSN, row, ScopusField.ISSN);
		
		putField(attribs, Source.Field.FULL_TITLE, row, ScopusField.SOURCE_TITLE);
		
		putField(attribs, Source.Field.CONFERENCE_TITLE, row, ScopusField.CONFERENCE_NAME);
		putField(attribs, Source.Field.CONFERENCE_LOCATION, row, ScopusField.CONFERENCE_LOCATION);
		
		// You might think that ScopusField.ABBREVIATED_SOURCE_TITLE would be shorter than
		// ScopusField.SOURCE_TITLE, but you'd be wrong.  :-/
		// (at least in my sample data)
		
		// ScopusField.SOURCE is always "Scopus" AFAIK.  Not related to the "source" database table.
		
		return new Source(keyGenerator, attribs);
	}

	public Address getReprintAddrAddr(FileTuple<ScopusField> row, DatabaseTableKeyGenerator keyGenerator) {
		Dictionary<String, Object> attribs = new Hashtable<String, Object>();
		
		EntityUtils.putField(attribs, Address.Field.RAW_ADDRESS_STRING, row, ScopusField.CORRESPONDENCE_ADDRESS);
		return new Address(keyGenerator, attribs);
	}

	private List<Person> splitAndParsePersonList(FileTuple<ScopusField> row,
			ScopusField fieldToExtract,
			DatabaseTableKeyGenerator keyGenerator) {
		List<Person> people = new ArrayList<Person>();
		Dictionary<String, Object> attribs;
		AbbreviatedNameParser nameParser;
		
		for (String name : row.getStringField(fieldToExtract).split("\\|")) {
			attribs = new Hashtable<String, Object>();
			putValue(attribs, Person.Field.UNSPLIT_NAME, name);
			
			try {
				nameParser = new AbbreviatedNameParser(name);
				putValue(attribs, Person.Field.FIRST_INITIAL, nameParser.firstInitial);
				putValue(attribs, Person.Field.FAMILY_NAME, nameParser.familyName);
				putValue(attribs, Person.Field.MIDDLE_INITIAL, nameParser.middleInitials);
			} catch (PersonParsingException e) {
				// TODO: log something
			}
		
			people.add(new Person(keyGenerator, attribs));
		}
		
		return people;
	}
	
	private Dictionary<String, Object> getDocumentAttribs(FileTuple<ScopusField> row, Source source, Person firstAuthor) {
		Dictionary<String, Object> attribs = new Hashtable<String, Object>();
		
		putPK(attribs, Document.Field.DOCUMENT_SOURCE_FK, source);
		putPK(attribs, Document.Field.FIRST_AUTHOR_FK, firstAuthor);
		
		putField(attribs, Document.Field.ABSTRACT_TEXT, row, ScopusField.ABSTRACT);
		putField(attribs, Document.Field.ARTICLE_NUMBER, row, ScopusField.ART_NO);
		putField(attribs, Document.Field.DIGITAL_OBJECT_IDENTIFIER, row, ScopusField.DOI);
		putField(attribs, Document.Field.TITLE, row, ScopusField.TITLE);
		putField(attribs, Document.Field.PUBLICATION_YEAR, row, ScopusField.YEAR);
		putField(attribs, Document.Field.PUBLICATION_DATE, row, ScopusField.YEAR);
		putField(attribs, Document.Field.CITED_YEAR, row, ScopusField.YEAR);
		putField(attribs, Document.Field.CITED_REFERENCE_COUNT, row, ScopusField.CITED_BY);
		putField(attribs, Document.Field.ISBN, row, ScopusField.ISBN);
		putField(attribs, Document.Field.ISSUE, row, ScopusField.ISSUE);
		putField(attribs, Document.Field.LANGUAGE, row, ScopusField.LANGUAGE_OF_ORIGINAL_DOCUMENT);
		putField(attribs, Document.Field.DOCUMENT_VOLUME, row, ScopusField.VOLUME);
		
		
		
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


