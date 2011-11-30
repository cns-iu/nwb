package edu.iu.sci2.database.scopus.load;

import static edu.iu.sci2.database.scopus.load.EntityUtils.*;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cishell.framework.algorithm.ProgressMonitor;

import prefuse.data.Table;
import prefuse.data.util.TableIterator;
import edu.iu.cns.database.load.framework.EntityContainer;
import edu.iu.cns.database.load.framework.RelationshipContainer;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.utilities.parser.AbbreviatedNameParser;
import edu.iu.sci2.database.isi.load.utilities.parser.exception.PersonParsingException;
import edu.iu.sci2.database.scholarly.model.entity.Address;
import edu.iu.sci2.database.scholarly.model.entity.Author;
import edu.iu.sci2.database.scholarly.model.entity.Document;
import edu.iu.sci2.database.scholarly.model.entity.Person;
import edu.iu.sci2.database.scholarly.model.entity.ReprintAddress;

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

		RowItemContainer<Author> authors = new RelationshipContainer<Author>(
				ISI.AUTHORS_TABLE_NAME, Author.SCHEMA);
		RowItemContainer<ReprintAddress> reprintAddresses = new RelationshipContainer<ReprintAddress>(
				ISI.REPRINT_ADDRESSES_TABLE_NAME, ReprintAddress.SCHEMA);

		TableIterator rowNumbers = table.iterator();
		while (rowNumbers.hasNext()) {
			int rowNum = rowNumbers.nextInt();
			FileTuple<ScopusField> row = new FileTuple<ScopusField>(table.getTuple(rowNum));

			Document document = new Document(documents.getKeyGenerator(), 
					getDocumentAttribs(row));
			documents.add(document);
			
			List<Person> authorPeople = getAuthorPeople(row, people.getKeyGenerator());
			
			for (Person p : authorPeople) {
				people.add(p);
			}
			
			for (Author a : Author.makeAuthors(document, authorPeople)) {
				authors.add(a);
			}
			
			Address reprintAddrAddr = getReprintAddrAddr(row, addresses.getKeyGenerator());
			addresses.add(reprintAddrAddr);
			ReprintAddress reprintAddr = ReprintAddress.link(document, reprintAddrAddr);
			reprintAddresses.add(reprintAddr);
		}
		return new DatabaseModel(documents, people, authors, addresses, reprintAddresses);
	}
	
	public Address getReprintAddrAddr(FileTuple<ScopusField> row, DatabaseTableKeyGenerator keyGenerator) {
		Dictionary<String, Object> attribs = new Hashtable<String, Object>();
		
		EntityUtils.putField(attribs, Address.Field.RAW_ADDRESS_STRING, row, ScopusField.CORRESPONDENCE_ADDRESS);
		return new Address(keyGenerator, attribs);
	}

	private List<Person> getAuthorPeople(FileTuple<ScopusField> row,
			DatabaseTableKeyGenerator keyGenerator) {
		List<Person> people = new ArrayList<Person>();
		Dictionary<String, Object> attribs;
		AbbreviatedNameParser nameParser;
		
		for (String name : row.getStringField(ScopusField.AUTHORS).split("\\|")) {
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
	
	private Dictionary<String, Object> getDocumentAttribs(FileTuple<ScopusField> row) {
		Dictionary<String, Object> attribs = new Hashtable<String, Object>();
		
		putField(attribs, Document.Field.ABSTRACT_TEXT, row, ScopusField.ABSTRACT);
		putField(attribs, Document.Field.ARTICLE_NUMBER, row, ScopusField.ART_NO);
		putField(attribs, Document.Field.DIGITAL_OBJECT_IDENTIFIER, row, ScopusField.DOI);
		putField(attribs, Document.Field.TITLE, row, ScopusField.TITLE);
		putField(attribs, Document.Field.PUBLICATION_YEAR, row, ScopusField.YEAR);
		putField(attribs, Document.Field.PUBLICATION_DATE, row, ScopusField.YEAR);
		
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


