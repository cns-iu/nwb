package edu.iu.scipolicy.loader.isi.db.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.database.loader.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;
import edu.iu.scipolicy.loader.isi.db.utilities.parser.RowItemTest;

public class PersonTest extends RowItemTest {
	public static final String ONE_REFERENCE_AUTHOR_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "OneReferenceAuthor.isi";
	public static final String MULTIPLE_REFERENCE_AUTHORS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleReferenceAuthors.isi";
	public static final String MULTIPLE_AUTHORS_WITH_THE_SAME_NAMES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleAuthorsWithTheSameNames.isi";

	@Test
	public void testZeroAuthorsGetParsed() throws Exception {
		DatabaseModel model = parseTestData(ZERO_PEOPLE_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 0);
	}

	@Test
	public void testOneAuthorGetsParsed() throws Exception {
		DatabaseModel model = parseTestData(ONE_AUTHOR_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		List<Person> items = (List<Person>)people.getItems();
		
		checkItemCount(people, 1);
		checkFirstAuthorPerson(items);
	}

	@Test
	public void testMultipleAuthorsGetParsed() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_AUTHORS_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		List<Person> items = (List<Person>)people.getItems();

		checkItemCount(people, 4);
		checkFirstAuthorPerson(items);
		checkSecondAuthorPerson(items);
		checkThirdAuthorPerson(items);
		checkFourthAuthorPerson(items);
	}

	@Test
	public void testOneAuthorGetsParsedFromOneReference() throws Exception {
		DatabaseModel model = parseTestData(ONE_REFERENCE_AUTHOR_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		List<Person> items = (List<Person>)people.getItems();

		checkItemCount(people, 1);
		checkThirdAuthorPerson(items);
	}

	@Test
	public void testMultipleAuthorsGetParsedFromOneReference() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_REFERENCE_AUTHORS_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		List<Person> items = (List<Person>)people.getItems();

		checkItemCount(people, 2);
		checkThirdAuthorPerson(items);
		checkFourthAuthorPerson(items);
	}

	@Test
	public void testAuthorsDoNotGetMerged() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_AUTHORS_WITH_THE_SAME_NAMES_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		List<Person> items = (List<Person>)people.getItems();

		checkItemCount(people, 4);
		checkFirstAuthorPerson(items);
		checkSecondAuthorPerson(items);
		checkThirdAuthorPerson(items);
	}

	// TODO: Test Person.

	private void checkFirstAuthorPerson(List<Person> people) throws Exception {
		verifyPersonExists(
			people,
			"",
			"Takeda",
			"H",
			"Takeda, Hiroyuki",
			"",
			"Hiroyuki",
			"Takeda, H");
	}

	private void checkSecondAuthorPerson(List<Person> people) throws Exception {
		verifyPersonExists(
			people,
			"",
			"Nishimura",
			"K",
			"Nishimura, Kaneyasu",
			"",
			"Kaneyasu",
			"Nishimura, K");
	}

	private void checkThirdAuthorPerson(List<Person> people) throws Exception {
		verifyPersonExists(
			people,
			"",
			"Macdonald",
			"P",
			"",
			"J",
			"",
			"Macdonald, PJ");
	}

	private void checkFourthAuthorPerson(List<Person> people) throws Exception {
		verifyPersonExists(
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
