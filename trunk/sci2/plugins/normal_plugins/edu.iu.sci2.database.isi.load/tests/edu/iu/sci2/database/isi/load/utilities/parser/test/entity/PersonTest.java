package edu.iu.sci2.database.isi.load.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;

import org.junit.Test;

import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.model.entity.Person;
import edu.iu.sci2.database.isi.load.model.entity.Reference;
import edu.iu.sci2.database.isi.load.utilities.parser.RowItemTest;
import edu.iu.sci2.database.isi.load.utilities.parser.test.relationship.AuthorsTest;

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
			ISI.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 0);
	}

	@Test
	public void testOneAuthorGetsParsed() throws Exception {
		DatabaseModel model = parseTestData(AuthorsTest.ONE_AUTHOR_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		Collection<Person> items = people.getItems();
		
		checkItemCount(people, 1);
		checkFirstAuthorPerson(items);
	}

	@Test
	public void testMultipleAuthorsGetParsed() throws Exception {
		DatabaseModel model = parseTestData(AuthorsTest.MULTIPLE_AUTHORS_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		Collection<Person> items = people.getItems();

		checkItemCount(people, 4);
		checkFirstAuthorPerson(items);
		checkSecondAuthorPerson(items);
		checkThirdAuthorPerson(items);
		checkFourthAuthorPerson(items);
	}

	@Test
	public void testOneAuthorGetsParsedFromOneReference() throws Exception {
		DatabaseModel model = parseTestData(ONE_REFERENCE_AUTHOR_TEST_DATA_PATH);
		RowItemContainer<Reference> references = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.REFERENCE_TABLE_NAME);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);

		checkItemContainerValidity(references, "references");
		Collection<Reference> referenceItems = references.getItems();

		checkItemContainerValidity(people, "people");
		Collection<Person> personItems = people.getItems();

		checkItemCount(references, 1);

		/*
		 * The one reference person doesn't exist yet because the reference hasn't been
		 *  fully parsed.
		 */
		checkItemCount(people, 0);

		// Parsing the reference causes its person to be parsed as well.
		referenceItems.iterator().next().getAttributesForInsertion();
		checkItemCount(people, 1);
		checkThirdAuthorPerson(personItems);
	}

	@Test
	public void testMultipleAuthorsGetParsedFromOneReference() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_REFERENCE_AUTHORS_TEST_DATA_PATH);
		RowItemContainer<Reference> references = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.REFERENCE_TABLE_NAME);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);

		checkItemContainerValidity(references, "references");
		Collection<Reference> referenceItems = references.getItems();

		checkItemContainerValidity(people, "people");
		Collection<Person> items = people.getItems();

		checkItemCount(references, 2);

		/*
		 * The one reference person doesn't exist yet because the reference hasn't been
		 *  fully parsed.
		 */
		checkItemCount(people, 0);

		// Parsing the reference causes its person to be parsed as well.
		Iterator<Reference> referenceIterator = referenceItems.iterator();
		referenceIterator.next().getAttributesForInsertion();
		referenceIterator.next().getAttributesForInsertion();
		checkItemCount(people, 2);
		checkThirdAuthorPerson(items);
		checkFourthAuthorPerson(items);
	}

	@Test
	public void testAuthorsDoNotGetMerged() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_AUTHORS_WITH_THE_SAME_NAMES_TEST_DATA_PATH);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);

		checkItemContainerValidity(people, "people");
		Collection<Person> items = people.getItems();

		checkItemCount(people, 4);
		checkFirstAuthorPerson(items);
		checkSecondAuthorPerson(items);
		checkThirdAuthorPerson(items);
	}

	// TODO: Test Person.

	public static Person getPerson(
			Collection<Person> people,
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
			} catch (Throwable e) {}
		}

		return null;
	}

	public static void verifyPersonExists(
			Collection<Person> people,
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
			Dictionary<String, Object> relationshipPersonAttributes =
				relationshipPerson.getAttributes();
			Object relationshipPersonUnsplitAbbreviatedName =
				relationshipPersonAttributes.get(ISI.UNSPLIT_ABBREVIATED_NAME);

			Dictionary<String, Object> providedPersonAttributes = providedPerson.getAttributes();
			Object providedPersonUnsplitAbbreviatedName =
				providedPersonAttributes.get(ISI.UNSPLIT_ABBREVIATED_NAME);

			String failMessage =
				displayName + " person and provided person are not the same." +
				"\n\t" + displayName + " person: \"" +
					relationshipPersonUnsplitAbbreviatedName + "\"" +
				"\n\tProvided person: \"" +
					providedPersonUnsplitAbbreviatedName + "\"";
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

		Dictionary<String, Object> personAttributes = person.getAttributesForInsertion();
		String personAdditionalName = (String)personAttributes.get(ISI.ADDITIONAL_NAME);
		String personFamilyName = (String)personAttributes.get(ISI.FAMILY_NAME);
		String personFirstInitial = (String)personAttributes.get(ISI.FIRST_INITIAL);
		String personFullName = (String)personAttributes.get(ISI.FULL_NAME);
		String personMiddleInitial = (String)personAttributes.get(ISI.MIDDLE_INITIAL);
		String personPersonalName = (String)personAttributes.get(ISI.PERSONAL_NAME);
		String personUnsplitAbbreviatedName =
			(String)personAttributes.get(ISI.UNSPLIT_ABBREVIATED_NAME);

		compareProperty("Additional Names", personAdditionalName, additionalName);
		compareProperty("Family Names", personFamilyName, familyName);
		compareProperty("First Initials", personFirstInitial, firstInitial);
		compareProperty("Full Names", personFullName, fullName);
		compareProperty("Middle Initials", personMiddleInitial, middleInitial);
		compareProperty("Personal Names", personPersonalName, personalName);
		compareProperty(
			"Unsplit Abbreviated Names",
			personUnsplitAbbreviatedName,
			unsplitAbbreviatedName);
	}

	private void checkFirstAuthorPerson(Collection<Person> people) throws Exception {
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

	private void checkSecondAuthorPerson(Collection<Person> people) throws Exception {
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

	private void checkThirdAuthorPerson(Collection<Person> people) throws Exception {
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

	private void checkFourthAuthorPerson(Collection<Person> people) throws Exception {
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
