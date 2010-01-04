package edu.iu.scipolicy.loader.isi.db.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.ISIModel;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;
import edu.iu.scipolicy.loader.isi.db.utilities.parser.BaseRowItemParsingTest;

public class PersonTest extends BaseRowItemParsingTest {
	public static final String ZERO_PEOPLE_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "ZeroPeople.isi";
	public static final String ONE_AUTHOR_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "OneAuthor.isi";
	public static final String MULTIPLE_AUTHORS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleAuthors.isi";
	public static final String ONE_REFERENCE_AUTHOR_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "OneReferenceAuthor.isi";
	public static final String MULTIPLE_REFERENCE_AUTHORS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleReferenceAuthors.isi";
	public static final String MULTIPLE_AUTHORS_WITH_THE_SAME_NAMES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleAuthorsWithTheSameNames.isi";

	@Test
	public void testZeroAuthorsGetParsed() throws Exception {
		ISIModel model = parseTestData(ZERO_PEOPLE_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.ISI_FILE_TABLE_NAME);

		if (people == null) {
			fail("people == null.  It shouldn't.  Ever.");
		}

		int numItems = people.getItems().size();

		if (numItems != 0) {
			fail("Found " + numItems + " item(s); expected 0.");
		}
	}

	@Test
	public void testOneAuthorGetsParsed() throws Exception {
		ISIModel model = parseTestData(ONE_AUTHOR_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		if (people == null) {
			fail("people == null.  It shouldn't.  Ever.");
		}

		List<Person> items = (List<Person>)people.getItems();
		int numItems = items.size();

		if (numItems != 1) {
			fail("Found " + numItems + " item(s); expected 1.");
		}

		checkFirstAuthor(items);
	}

	@Test
	public void testMultipleAuthorsGetParsed() throws Exception {
		ISIModel model = parseTestData(MULTIPLE_AUTHORS_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		if (people == null) {
			fail("people == null.  It shouldn't.  Ever.");
		}

		List<Person> items = (List<Person>)people.getItems();
		int numItems = items.size();

		if (numItems != 4) {
			fail("Found " + numItems + " item(s); expected 4.");
		}

		checkFirstAuthor(items);
		checkSecondAuthor(items);
		checkThirdAuthor(items);
		checkFourthAuthor(items);
	}

	@Test
	public void testOneAuthorGetsParsedFromOneReference() throws Exception {
		ISIModel model = parseTestData(ONE_REFERENCE_AUTHOR_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		if (people == null) {
			fail("people == null.  It shouldn't.  Ever.");
		}

		List<Person> items = (List<Person>)people.getItems();
		int numItems = items.size();

		if (numItems != 1) {
			fail("Found " + numItems + " item(s); expected 1.");
		}

		checkThirdAuthor(items);
	}

	@Test
	public void testMultipleAuthorsGetParsedFromOneReference() throws Exception {
		ISIModel model = parseTestData(MULTIPLE_REFERENCE_AUTHORS_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		if (people == null) {
			fail("people == null.  It shouldn't.  Ever.");
		}

		List<Person> items = (List<Person>)people.getItems();
		Person p = items.get(0);
		System.err.println("abbreviated name: " + p.getUnsplitAbbreviatedName());
		int numItems = items.size();

		if (numItems != 2) {
			fail("Found " + numItems + " item(s); expected 2.");
		}

		checkThirdAuthor(items);
		checkFourthAuthor(items);
	}

	@Test
	public void testAuthorsDoNotGetMerged() throws Exception {
		ISIModel model = parseTestData(MULTIPLE_AUTHORS_WITH_THE_SAME_NAMES_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		if (people == null) {
			fail("people == null.  It shouldn't.  Ever.");
		}

		List<Person> items = (List<Person>)people.getItems();
		int numItems = items.size();

		if (numItems != 4) {
			fail("Found " + numItems + " item(s); expected 4.");
		}

		checkFirstAuthor(items);
		checkSecondAuthor(items);
		checkThirdAuthor(items);
	}

	// TODO: Test Person.

	private void checkFirstAuthor(List<Person> people) throws Exception {
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

	private void checkSecondAuthor(List<Person> people) throws Exception {
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

	private void checkThirdAuthor(List<Person> people) throws Exception {
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

	private void checkFourthAuthor(List<Person> people) throws Exception {
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
