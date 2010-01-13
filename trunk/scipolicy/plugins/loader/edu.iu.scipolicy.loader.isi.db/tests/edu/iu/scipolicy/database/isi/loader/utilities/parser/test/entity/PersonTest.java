package edu.iu.scipolicy.database.isi.loader.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;

import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.database.loader.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.database.isi.loader.model.entity.Person;
import edu.iu.scipolicy.database.isi.loader.utilities.parser.RowItemTest;
import edu.iu.scipolicy.database.isi.loader.utilities.parser.test.relationship.AuthorsTest;

public class PersonTest extends RowItemTest {
	// Person test data.
	public static final String ONE_REFERENCE_AUTHOR_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "OneReferenceAuthor.isi";
	public static final String MULTIPLE_REFERENCE_AUTHORS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleReferenceAuthors.isi";
	public static final String MULTIPLE_AUTHORS_WITH_THE_SAME_NAMES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleAuthorsWithTheSameNames.isi";

	@Test
	public void testZeroAuthorsGetParsed() throws Exception {
		DatabaseModel model = parseTestData(EMPTY_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 0);
	}

	@Test
	public void testOneAuthorGetsParsed() throws Exception {
		DatabaseModel model = parseTestData(AuthorsTest.ONE_AUTHOR_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISIDatabase.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		List<Person> items = (List<Person>)people.getItems();
		
		checkItemCount(people, 1);
		checkFirstAuthorPerson(items);
	}

	@Test
	public void testMultipleAuthorsGetParsed() throws Exception {
		DatabaseModel model = parseTestData(AuthorsTest.MULTIPLE_AUTHORS_TEST_DATA_PATH);
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

	public static Person getPerson(
			List<Person> people,
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitAbbreviatedName) {
		for (Person person : people) {
			try {
				checkPerson(
					person,
					additionalName,
					familyName,
					firstInitial,
					fullName,
					middleInitial,
					personalName,
					unsplitAbbreviatedName);

				return person;
			} catch (Throwable e) {
			}
		}

		return null;
	}

	public static void verifyPersonExists(
			List<Person> people,
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitAbbreviatedName) throws Exception {
		if (getPerson(
				people,
				additionalName,
				familyName,
				firstInitial,
				fullName,
				middleInitial,
				personalName,
				unsplitAbbreviatedName) == null) {
			String exceptionMessage =
				"No person with the following specifications were found:" +
				"\n\tAdditional name: " + additionalName +
				"\n\tFamily name: " + familyName +
				"\n\tFirst initial: " + firstInitial +
				"\n\tFull name: " + fullName +
				"\n\tMiddle initial: " + middleInitial +
				"\n\tPersonal name: " + personalName +
				"\n\tUnsplit abbreviated name: " + unsplitAbbreviatedName;
			throw new Exception(exceptionMessage);
		}
	}

	public static void checkPeople(
			Person relationshipPerson, Person providedPerson, String displayName) {
		if (relationshipPerson != providedPerson) {
			String failMessage =
				displayName + " person and provided person are not the same." +
				"\n\t" + displayName + " person: \"" +
					relationshipPerson.getUnsplitAbbreviatedName() + "\"" +
				"\n\tProvided person: \"" +
					providedPerson.getUnsplitAbbreviatedName() + "\"";
			fail(failMessage);
		}
	}

	public static void checkPerson(
			Person person,
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitAbbreviatedName) {
		if (person == null) {
			String failMessage =
				"An exception should have been thrown " +
				"if the result Person would have ended up null.";
			fail(failMessage);
		}

		compareProperty("Additional Names", person.getAdditionalName(), additionalName);
		compareProperty("Family Names", person.getFamilyName(), familyName);
		compareProperty("First Initials", person.getFirstInitial(), firstInitial);
		compareProperty("Full Names", person.getFullName(), fullName);
		compareProperty("Middle Initials", person.getMiddleInitial(), middleInitial);
		compareProperty("Personal Names", person.getPersonalName(), personalName);
		compareProperty(
			"Unsplit Abbreviated Names",
			person.getUnsplitAbbreviatedName(),
			unsplitAbbreviatedName);
	}

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
