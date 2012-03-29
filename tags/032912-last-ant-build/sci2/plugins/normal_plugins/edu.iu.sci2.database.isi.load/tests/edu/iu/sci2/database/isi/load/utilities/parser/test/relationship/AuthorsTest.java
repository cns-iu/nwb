package edu.iu.sci2.database.isi.load.utilities.parser.test.relationship;


import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.cishell.utilities.StringUtilities;
import org.junit.Test;

import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.model.entity.Document;
import edu.iu.sci2.database.isi.load.model.entity.Person;
import edu.iu.sci2.database.isi.load.model.entity.relationship.Author;
import edu.iu.sci2.database.isi.load.utilities.parser.RowItemTest;
import edu.iu.sci2.database.isi.load.utilities.parser.test.entity.DocumentTest;
import edu.iu.sci2.database.isi.load.utilities.parser.test.entity.PersonTest;

public class AuthorsTest extends RowItemTest {
	// Author test data.
	public static final String ONE_AUTHOR_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "OneAuthor.isi";
	public static final String MULTIPLE_AUTHORS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleAuthors.isi";

	public static final String FIRST_DOCUMENT_TITLE =
		"Planarians Maintain a Constant Ratio of Different Cell Types During " +
		"Changes in Body Size by Using the Stem Cell System";
	public static final String SECOND_DOCUMENT_TITLE =
		"Minimum spanning trees of weighted scale-free networks";

	public static final String FIRST_AUTHOR_EMAIL_ADDRESS = "agata@mdb.biophys.kyoto-u.ac.jp";
	public static final String SECOND_AUTHOR_EMAIL_ADDRESS = "some.other@email.address";
	public static final String THIRD_AUTHOR_EMAIL_ADDRESS = "alb@nd.edu";
	public static final String FOURTH_AUTHOR_EMAIL_ADDRESS = THIRD_AUTHOR_EMAIL_ADDRESS;

	@Test
	public void testNoAuthorsWereParsed() throws Exception {
		DatabaseModel model = parseTestData(EMPTY_TEST_DATA_PATH);
		RowItemContainer<Author> authors = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.AUTHORS_TABLE_NAME);

		checkItemContainerValidity(authors, "authors");
		checkItemCount(authors, 0);
	}

	@Test
	public void testOneAuthorWasParsed() throws Exception {
		DatabaseModel model = parseTestData(ONE_AUTHOR_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);
		RowItemContainer<Author> authors = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.AUTHORS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 1);

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 1);

		checkItemContainerValidity(authors, "authors");
		checkItemCount(authors, 1);

		Document firstDocument = documents.getItems().iterator().next();
		Person firstAuthorPerson = people.getItems().iterator().next();
		Author firstAuthor = (Author)authors.getItems().iterator().next();

		checkAuthor(firstAuthor, firstDocument, firstAuthorPerson);
	}

	@Test
	public void testOneAuthorWasParsed_FirstAuthor() throws Exception {
		DatabaseModel model = parseTestData(ONE_AUTHOR_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);
		RowItemContainer<Author> authors = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.AUTHORS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 1);
		Collection<Document> documentItems = documents.getItems();

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 1);
		Collection<Person> personItems = people.getItems();

		checkItemContainerValidity(authors, "authors");
		checkItemCount(authors, 1);
		Collection<Author> authorItems = authors.getItems();

		Document firstDocument = getFirstDocument(documentItems);
		Person firstAuthorPerson = getFirstAuthorPerson(personItems);
		Author firstAuthor = getAuthor(authorItems, firstDocument, firstAuthorPerson);

		checkFirstAuthor(firstAuthor, firstDocument, firstAuthorPerson);
	}

	@Test
	public void testMultipleAuthorsWereParsed() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_AUTHORS_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);
		RowItemContainer<Author> authors = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.AUTHORS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 2);
		Collection<Document> documentItems = documents.getItems();

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 4);
		Collection<Person> personItems = people.getItems();

		checkItemContainerValidity(authors, "authors");
		checkItemCount(authors, 4);
		Collection<Author> authorItems = authors.getItems();

		Document firstDocument = getFirstDocument(documentItems);
		Person firstAuthorPerson = getFirstAuthorPerson(personItems);
		Author firstAuthor = getAuthor(authorItems, firstDocument, firstAuthorPerson);
		Person secondAuthorPerson = getSecondAuthorPerson(personItems);
		Author secondAuthor = getAuthor(authorItems, firstDocument, secondAuthorPerson);

		checkAuthor(firstAuthor, firstDocument, firstAuthorPerson, 0);
		checkAuthor(secondAuthor, firstDocument, secondAuthorPerson, 1);
	}

	@Test
	public void testMultipleAuthorsWereParsed_FirstAuthor() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_AUTHORS_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);
		RowItemContainer<Author> authors = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.AUTHORS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 2);
		Collection<Document> documentItems = documents.getItems();

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 4);
		Collection<Person> personItems = people.getItems();

		checkItemContainerValidity(authors, "authors");
		checkItemCount(authors, 4);
		Collection<Author> authorItems = authors.getItems();

		Document firstDocument = getFirstDocument(documentItems);
		Person firstAuthorPerson = getFirstAuthorPerson(personItems);
		Author firstAuthor = getAuthor(authorItems, firstDocument, firstAuthorPerson);
		Person secondAuthorPerson = getSecondAuthorPerson(personItems);
		Author secondAuthor = getAuthor(authorItems, firstDocument, secondAuthorPerson);

		checkFirstAuthor(firstAuthor, firstDocument, firstAuthorPerson);
		checkAuthor(secondAuthor, firstDocument, secondAuthorPerson);
	}

	@Test
	public void testMultipleAuthorsWithOneEmailAddressSpecified() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_AUTHORS_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);
		RowItemContainer<Author> authors = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.AUTHORS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 2);
		Collection<Document> documentItems = documents.getItems();

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 4);
		Collection<Person> personItems = people.getItems();

		checkItemContainerValidity(authors, "authors");
		checkItemCount(authors, 4);
		Collection<Author> authorItems = authors.getItems();

		Document secondDocument = getSecondDocument(documentItems);
		Person thirdAuthorPerson = getThirdAuthorPerson(personItems);
		Author thirdAuthor = getAuthor(authorItems, secondDocument, thirdAuthorPerson);
		Person fourthAuthorPerson = getFourthAuthorPerson(personItems);
		Author fourthAuthor = getAuthor(authorItems, secondDocument, fourthAuthorPerson);

		checkAuthor(thirdAuthor, secondDocument, thirdAuthorPerson, THIRD_AUTHOR_EMAIL_ADDRESS);
		checkAuthor(fourthAuthor, secondDocument, fourthAuthorPerson, FOURTH_AUTHOR_EMAIL_ADDRESS);
	}

	@Test
	public void testMultipleAuthorsWithDifferentEmailAddressesSpecified() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_AUTHORS_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);
		RowItemContainer<Author> authors = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.AUTHORS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 2);
		Collection<Document> documentItems = documents.getItems();

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 4);
		Collection<Person> personItems = people.getItems();

		checkItemContainerValidity(authors, "authors");
		checkItemCount(authors, 4);
		Collection<Author> authorItems = authors.getItems();

		Document firstDocument = getFirstDocument(documentItems);
		Person firstAuthorPerson = getFirstAuthorPerson(personItems);
		Author firstAuthor = getAuthor(authorItems, firstDocument, firstAuthorPerson);
		Person secondAuthorPerson = getSecondAuthorPerson(personItems);
		Author secondAuthor = getAuthor(authorItems, firstDocument, secondAuthorPerson);

		checkAuthor(firstAuthor, firstDocument, firstAuthorPerson, FIRST_AUTHOR_EMAIL_ADDRESS);
		checkAuthor(secondAuthor, firstDocument, secondAuthorPerson, SECOND_AUTHOR_EMAIL_ADDRESS);
	}

	// TODO: Test Author.

	public static Author getAuthor(
			Collection<Author> authors, Document document, Person authorPerson) {
		for (Author author : authors) {
			if ((author.getDocument() == document) && (author.getPerson() == authorPerson)) {
				return author;
			}
		}

		return null;
	}

	public static void checkAuthor(
			Author author, Document providedDocument, Person providedAuthorPerson) {
		Document authorDocument = author.getDocument();
		Person authorPerson = author.getPerson();

		DocumentTest.checkDocuments(authorDocument, providedDocument, "Author");
		PersonTest.checkPeople(authorPerson, providedAuthorPerson, "Author");
	}

	public static void checkAuthor(
			Author author,
			Document providedDocument,
			Person providedAuthorPerson,
			int orderListed) {
		checkAuthor(author, providedDocument, providedAuthorPerson);
		checkItemOrderListed(author, orderListed);
	}

	public static void checkAuthor(
			Author author,
			Document providedDocument,
			Person providedAuthorPerson,
			String emailAddress) {
		checkAuthor(author, providedDocument, providedAuthorPerson);
		checkEmailAddress(author, emailAddress);
	}

	public static void checkFirstAuthor(
			Author author, Document providedDocument, Person providedAuthorPerson) {
		checkAuthor(author, providedDocument, providedAuthorPerson);
		checkAuthor(author, providedDocument, providedDocument.getFirstAuthorPerson());
	}

	public static void checkEmailAddress(Author author, String providedEmailAddress) {
		String authorEmailAddress = author.getEmailAddress();

		if (!StringUtilities.areValidAndEqual(authorEmailAddress, providedEmailAddress)) {
			String failMessage =
				"E-mail addresses are not the same." +
				"\n\tAuthor e-mail address: \"" + authorEmailAddress + "\"" +
				"\n\tProvided e-mail address: \"" + providedEmailAddress + "\"";
			fail(failMessage);
		}
	}

	private Document getFirstDocument(Collection<Document> documents) {
		return DocumentTest.getDocument(documents, FIRST_DOCUMENT_TITLE);
	}

	private Document getSecondDocument(Collection<Document> documents) {
		return DocumentTest.getDocument(documents, SECOND_DOCUMENT_TITLE);
	}

	private Person getFirstAuthorPerson(Collection<Person> people) {
		return PersonTest.getPerson(
			people,
			"",
			"Takeda",
			"H",
			"Takeda, Hiroyuki",
			"",
			"Hiroyuki",
			"Takeda, H");
	}

	private Person getSecondAuthorPerson(Collection<Person> people) throws Exception {
		return PersonTest.getPerson(
			people,
			"",
			"Nishimura",
			"K",
			"Nishimura, Kaneyasu",
			"",
			"Kaneyasu",
			"Nishimura, K");
	}

	private Person getThirdAuthorPerson(Collection<Person> people) throws Exception {
		return PersonTest.getPerson(
			people,
			"",
			"Macdonald",
			"P",
			"",
			"J",
			"",
			"Macdonald, PJ");
	}

	private Person getFourthAuthorPerson(Collection<Person> people) throws Exception {
		return PersonTest.getPerson(
			people,
			"",
			"Almaas",
			"E",
			"",
			"",
			"",
			"Almaas, E");
	}
}
