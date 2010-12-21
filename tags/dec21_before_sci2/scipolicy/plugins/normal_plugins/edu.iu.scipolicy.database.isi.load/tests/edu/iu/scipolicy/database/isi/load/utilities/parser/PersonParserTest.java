package edu.iu.scipolicy.database.isi.load.utilities.parser;

import static org.junit.Assert.fail;

import java.util.Dictionary;

import org.cishell.utilities.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.load.model.entity.Person;
import edu.iu.scipolicy.database.isi.load.utilities.parser.exception.PersonParsingException;
import edu.iu.scipolicy.database.isi.load.utilities.parser.test.entity.PersonTest;

public class PersonParserTest {
	public static final String FAMILY_NAME = "Smith";
	public static final String FIRST_NAME = "John";
	public static final String MIDDLE_NAME = "George";
	public static final String EXTRA_MIDDLE_NAME = "Alexander";

	public static final String FIRST_INITIAL = "J";
	public static final String MIDDLE_INITIAL = "G";
	public static final String EXTRA_MIDDLE_INITIAL = "A";

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
		FAMILY_NAME + ", " + FIRST_INITIAL + MIDDLE_INITIAL + EXTRA_MIDDLE_INITIAL;
	public static final String ABBREVIATED_MORE_THAN_TWO_INITIALS_WITHOUT_COMMA =
		FAMILY_NAME + " " + FIRST_INITIAL + MIDDLE_INITIAL + EXTRA_MIDDLE_INITIAL;

	public static final String JUST_FAMILY_NAME_FULL_NAME = FAMILY_NAME;
	public static final String FAMILY_AND_PERSONAL_NAMES_FULL_NAME =
		FAMILY_NAME + ", " + FIRST_NAME;
	public static final String COMPLETE_NAME_FULL_NAME =
		FAMILY_NAME + ", " + FIRST_NAME + " " + MIDDLE_NAME;
	public static final String COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME =
		FAMILY_NAME + ", " + FIRST_NAME + " " + MIDDLE_NAME + " " + EXTRA_MIDDLE_NAME;

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
	 * baseTest_Separator_Starredness_WhichNamesSupplied
	 */

	@Test
	public void testParsePerson_NoNamesProvided() {
		try {
			//PersonParser.parsePerson(this.keyGenerator, "", "");
			dirtyParsePerson(this.keyGenerator, "", "");
			String failMessage =
				"An exception should have been thrown when passing in both an empty abbreviated " +
				"name and an empty full name.";
			fail(failMessage);
		} catch (Throwable e) {
			// Success.
		}
	}

	@Test
	public void testParsePerson_CommaSeparator_NotStarred_JustFamilyName() {
		//try {
			//Pair<Person, Boolean> noFullNameResult =
			//	PersonParser.parsePerson(
			Person person = new Person(
					this.keyGenerator,
					ABBREVIATED_NO_INITIALS,
					JUST_FAMILY_NAME_FULL_NAME);
			checkResult(
				person,
				person.getAttributesForInsertion(),
				//noFullNameResult,
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				JUST_FAMILY_NAME_FULL_NAME,
				NO_MIDDLE_INITIAL,
				NO_PERSONAL_NAME,
				FAMILY_NAME,
				false);
		/*} catch (Exception e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_CommaSeparator_NotStarred_FamilyAndFirstNames() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME,
				NO_MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				false);

		/*} catch (PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_CommaSeparator_NotStarred_CompleteName() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				COMPLETE_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				COMPLETE_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				MIDDLE_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				COMPLETE_NAME_FULL_NAME,
				MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				false);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_CommaSeparator_NotStarred_CompleteNameWithTwoAdditionalNames() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				MIDDLE_NAME + " " + EXTRA_MIDDLE_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME,
				MIDDLE_INITIAL + EXTRA_MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				false);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_CommaSeparator_Starred_JustFamilyName() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_NO_INITIALS),
				JUST_FAMILY_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				starred(ABBREVIATED_NO_INITIALS),
				JUST_FAMILY_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				JUST_FAMILY_NAME_FULL_NAME,
				NO_MIDDLE_INITIAL,
				NO_PERSONAL_NAME,
				ABBREVIATED_NO_INITIALS,
				true);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_CommaSeparator_Starred_FamilyAndFirstNames() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_INITIAL_WITH_COMMA),
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_INITIAL_WITH_COMMA),
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME,
				NO_MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				true);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_CommaSeparator_Starred_CompleteName() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA),
				COMPLETE_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA),
				COMPLETE_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				MIDDLE_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				COMPLETE_NAME_FULL_NAME,
				MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				true);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_CommaSeparator_Starred_CompleteNameWithTwoAdditionalNames() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA),
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				starred(ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA),
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				MIDDLE_NAME + " " + EXTRA_MIDDLE_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME,
				MIDDLE_INITIAL + EXTRA_MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				true);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_NotStarred_JustFamilyName() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_NO_INITIALS,
				JUST_FAMILY_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				ABBREVIATED_NO_INITIALS,
				JUST_FAMILY_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				JUST_FAMILY_NAME_FULL_NAME,
				NO_MIDDLE_INITIAL,
				NO_PERSONAL_NAME,
				ABBREVIATED_NO_INITIALS,
				false);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_NotStarred_FamilyAndFirstNames() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_FIRST_INITIAL_WITHOUT_COMMA,
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				ABBREVIATED_FIRST_INITIAL_WITHOUT_COMMA,
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME,
				NO_MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				false);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_NotStarred_CompleteName() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITHOUT_COMMA,
				COMPLETE_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITHOUT_COMMA,
				COMPLETE_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				MIDDLE_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				COMPLETE_NAME_FULL_NAME,
				MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				false);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_NotStarred_CompleteNameWithTwoAdditionalNames()
	{
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITHOUT_COMMA,
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITHOUT_COMMA,
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				MIDDLE_NAME + " " + EXTRA_MIDDLE_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME,
				MIDDLE_INITIAL + EXTRA_MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				false);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_Starred_JustFamilyName() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_NO_INITIALS),
				JUST_FAMILY_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				starred(ABBREVIATED_NO_INITIALS),
				JUST_FAMILY_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				NO_FIRST_INITIAL,
				JUST_FAMILY_NAME_FULL_NAME,
				NO_MIDDLE_INITIAL,
				NO_PERSONAL_NAME,
				ABBREVIATED_NO_INITIALS,
				true);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_Starred_FamilyAndFirstNames() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_INITIAL_WITHOUT_COMMA),
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_INITIAL_WITHOUT_COMMA),
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				NO_ADDITIONAL_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				FAMILY_AND_PERSONAL_NAMES_FULL_NAME,
				NO_MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_FIRST_INITIAL_WITH_COMMA,
				true);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_Starred_CompleteName() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITHOUT_COMMA),
				COMPLETE_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				starred(ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITHOUT_COMMA),
				COMPLETE_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				MIDDLE_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				COMPLETE_NAME_FULL_NAME,
				MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_FIRST_AND_MIDDLE_INITIALS_WITH_COMMA,
				true);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	@Test
	public void testParsePerson_WhiteSpaceSeparator_Starred_CompleteNameWithTwoAdditionalNames() {
		//try {
			/*Pair<Person, Boolean> noFullNameResult = PersonParser.parsePerson(
				this.keyGenerator,
				starred(ABBREVIATED_MORE_THAN_TWO_INITIALS_WITHOUT_COMMA),
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME);*/
			Person person = new Person(
				this.keyGenerator,
				starred(ABBREVIATED_MORE_THAN_TWO_INITIALS_WITHOUT_COMMA),
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME);
			checkResult(
				//noFullNameResult,
				person,
				person.getAttributesForInsertion(),
				MIDDLE_NAME + " " + EXTRA_MIDDLE_NAME,
				FAMILY_NAME,
				FIRST_INITIAL,
				COMPLETE_WITH_ONE_EXTRA_MIDDLE_NAME_FULL_NAME,
				MIDDLE_INITIAL + EXTRA_MIDDLE_INITIAL,
				FIRST_NAME,
				ABBREVIATED_MORE_THAN_TWO_INITIALS_WITH_COMMA,
				true);
		/*} catch(PersonParsingException e) {
			fail("No exception should've been thrown: " + e.getMessage());
		}*/
	}

	private String starred(String target) {
		return "*" + target;
	}

	private void checkResult(
			//Pair<Person, Boolean> result,
			Person person,
			Dictionary<String, Object> attributes,
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitAbbreviatedName,
			boolean starred) {
		PersonTest.checkPerson(
			person,
			additionalName,
			familyName,
			firstInitial,
			fullName,
			middleInitial,
			personalName,
			unsplitAbbreviatedName);
		//compareStarredness(result, starred); 
	}

	/*private static void compareStarredness(Pair<Person, Boolean> result, boolean starred) {
		if (result.getSecondObject().booleanValue() != starred) {
			String failMessage =
				"Starrednesses do not match:" +
				"\n\tResult: " + result.getSecondObject() +
				"\n\tComparison: " + starred;
			fail(failMessage);
		}
	}*/
	private static void compareStarredness(
			Dictionary<String, Object> attributes, boolean starred) {
		boolean personWasStarred = (Boolean)attributes.get(ISI.REFERENCE_WAS_STARRED);

		if (personWasStarred != starred) {
			String failMessage =
				"Starrednesses do not match:" +
				"\n\tResult: " + personWasStarred +
				"\n\tComparison: " + starred;
			fail(failMessage);
		}
	}

	private static void dirtyParsePerson(
			DatabaseTableKeyGenerator keyGenerator,
			String rawAbbreviatedNameString,
			String rawFullNameString) throws PersonParsingException {
		Person person = new Person(keyGenerator, rawAbbreviatedNameString, rawFullNameString);
		new PersonParser(rawAbbreviatedNameString, rawFullNameString);
	}
}
