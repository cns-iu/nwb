package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import static org.junit.Assert.fail;

import org.cishell.utilities.StringUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.cns.shared.utilities.Pair;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;

public class PersonParserTest {
	public static final String FAMILY_NAME = "Smith";
	public static final String FIRST_NAME = "John";
	public static final String FIRST_INITIAL = "J";
	public static final String MIDDLE_INITIAL = "G";
	public static final String EXTRA_INITIAL = "A";
	public static final String ABBREVIATED_NO_INITIALS = FAMILY_NAME;
	public static final String ABBREVIATED_FIRST_INITIAL_WITH_COMMA =
		FAMILY_NAME + ", " + FIRST_INITIAL;
	public static final String ABBREVIATED_FIRST_INITIAL_WITHOUT_COMMA =
		FAMILY_NAME + " " + FIRST_INITIAL;
	public static final String ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA =
		FAMILY_NAME + ", " + FIRST_INITIAL + MIDDLE_INITIAL;
	public static final String ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITHOUT_COMMA =
		FAMILY_NAME + " " + FIRST_INITIAL + MIDDLE_INITIAL;
	public static final String ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA =
		FAMILY_NAME + ", " + FIRST_INITIAL + MIDDLE_INITIAL + EXTRA_INITIAL;
	public static final String ABBREVIATED_MORE_THAN_TWO_INITIALS_WITHOUT_COMMA =
		FAMILY_NAME + " " + FIRST_INITIAL + MIDDLE_INITIAL + EXTRA_INITIAL;
	public static final String FULL_NAME = FAMILY_NAME + ", " + FIRST_NAME;
	public static final String NO_PERSONAL_NAME = "";
	public static final String NO_ADDITIONAL_NAME = "";
	public static final String NO_FAMILY_NAME = "";
	public static final String NO_FIRST_INITIAL = "";
	public static final String NO_MIDDLE_INITIAL = "";
	public static final String NO_FULL_NAME = "";

	private DatabaseTableKeyGenerator keyGenerator;

	@Before
	public void setUp() throws Exception {
		this.keyGenerator = new DatabaseTableKeyGenerator();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Format for tests:
	 * baseTest_Separator_Starredness_Initials
	 * Each case should include both cases for the author full names.
	 */

	@Test
	public void testParsePerson_NoNamesProvided() {
		try {
			PersonParser.parsePerson(this.keyGenerator, "", "");
			String failMessage =
				"An exception should have been thrown when passing in both an empty abbreviated " +
				"name and an empty full name.";
			fail(failMessage);
		} catch (PersonParsingException e) {
			// Success.
		}
	}

	@Test
	public void testParsePerson_CommaSeparator_NotStarred_NoInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult =
				PersonParser.parsePerson(this.keyGenerator, ABBREVIATED_NO_INITIALS, NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				FAMILY_NAME,
				NO_FULL_NAME,
				false);

			/*Pair<Person, Boolean> fullNameResult =
				PersonParser.parsePerson(this.keyGenerator, ABBREVIATED_NO_INITIALS, FULL_NAME);
			checkResult(
				fullNameResult,
				FIRST_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				FULL_NAME,
				FULL_NAME,
				false);*/
		} catch (PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_CommaSeparator_NotStarred_JustFirstInitial() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator, ABBREVIATED_FIRST_INITIAL_WITH_COMMA, NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				FAMILY_NAME + ", " + FIRST_INITIAL,
				false);
		} catch (PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_CommaSeparator_NotStarred_FirstAndMiddleInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				MIDDLE_INITIAL,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				FAMILY_NAME + ", " + FIRST_INITIAL + " " + MIDDLE_INITIAL,
				false);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_CommaSeparator_NotStarred_MoreThanTwoInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				NO_FULL_NAME,
				false);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_CommaSeparator_Starred_NoInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_NO_INITIALS),
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_NO_INITIALS,
				NO_FULL_NAME,
				true);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_CommaSeparator_Starred_JustFirstInitial() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_INITIAL_WITH_COMMA),
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				FAMILY_NAME + ", " + FIRST_INITIAL,
				true);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_CommaSeparator_Starred_FirstAndMiddleInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA),
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				MIDDLE_INITIAL,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				FAMILY_NAME + ", " + FIRST_INITIAL + " " + MIDDLE_INITIAL,
				true);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_CommaSeparator_Starred_MoreThanTwoInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA),
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				NO_FULL_NAME,
				true);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_NotStarred_NoInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_NO_INITIALS,
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_NO_INITIALS,
				NO_FULL_NAME,
				false);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_NotStarred_JustFirstInitial() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_FIRST_INITIAL_WITHOUT_COMMA,
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				false);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_NotStarred_FirstAndMiddleInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITHOUT_COMMA,
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				MIDDLE_INITIAL,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				FAMILY_NAME + ", " + FIRST_INITIAL + " " + MIDDLE_INITIAL,
				false);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_NotStarred_MoreThanTwoInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITHOUT_COMMA,
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				NO_FULL_NAME,
				false);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_Starred_NoInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_NO_INITIALS),
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_NO_INITIALS,
				NO_FULL_NAME,
				true);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_Starred_JustFirstInitial() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_INITIAL_WITHOUT_COMMA),
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				true);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_Starred_FirstAndMiddleInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITHOUT_COMMA),
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				MIDDLE_INITIAL,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				FAMILY_NAME + ", " + FIRST_INITIAL + " " + MIDDLE_INITIAL,
				true);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_Starred_MoreThanTwoInitials() {
		try {
			Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_MORE_THAN_TWO_INITIALS_WITHOUT_COMMA),
				NO_FULL_NAME);
			checkResult(
				noFullNameResult,
				NO_PERSONAL_NAME,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				NO_MIDDLE_INITIAL,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				NO_FULL_NAME,
				true);
		} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}
	}

	private String starred(String target) {
		return "*" + target;
	}

	private void checkResult(
			Pair<Person, Boolean> result,
			String personalName,
			String additionalName,
			String familyName,
			String firstInitial,
			String middleInitial,
			String unsplitAbbreviatedName,
			String fullName,
			boolean starred) {
		Person person = result.getFirstObject();

		if (person == null) {
			String failMessage =
				"An exception should have been thrown " +
				"if the result Person would have ended up null.";
			fail(failMessage);
		}

		comparePersonProperty("Personal Names", person.getPersonalName(), personalName);
		comparePersonProperty("Additional Names", person.getAdditionalName(), additionalName);
		comparePersonProperty("Family Names", person.getFamilyName(), familyName);
		comparePersonProperty("First Initials", person.getFirstInitial(), firstInitial);
		comparePersonProperty("Middle Initials", person.getMiddleInitial(), middleInitial);
		comparePersonProperty(
			"Unsplit Abbreviated Names",
			person.getUnsplitAbbreviatedName(),
			unsplitAbbreviatedName);
		comparePersonProperty("Full Names", person.getFullName(), fullName);
		compareStarredness(result, starred); 
	}

	private static void comparePersonProperty(
			String propertyName, String personProperty, String compareTo) {
		if (StringUtilities.isEmptyOrWhiteSpace(personProperty)) {
			if (!StringUtilities.isEmptyOrWhiteSpace(compareTo)) {
				String failMessage =
					propertyName +
					" do not match: Result is empty (\"" + personProperty + "\")" +
					" and Comparison is not (\"" + compareTo + "\").";
				fail(failMessage);
			} else {
			}
		} else {
			// personProperty is NOT empty or white space.

			if (StringUtilities.isEmptyOrWhiteSpace(compareTo)) {
				String failMessage =
					propertyName + " do not match: Result is not empty and Comparison is.";
				fail(failMessage);
			} else if (personProperty.compareToIgnoreCase(compareTo) != 0) {
				String failMessage =
					propertyName + " do not match:" +
					"\n\tResult: \"" + personProperty + "\"" +
					"\n\tComparison: \"" + compareTo + "\"";
				fail(failMessage);
			}
		}
	}

	private static void compareStarredness(Pair<Person, Boolean> result, boolean starred) {
		if (result.getSecondObject().booleanValue() != starred) {
			String failMessage =
				"Starrednesses do not match:" +
				"\n\tResult: " + result.getSecondObject() +
				"\n\tComparison: " + starred;
			fail(failMessage);
		}
	}
}
