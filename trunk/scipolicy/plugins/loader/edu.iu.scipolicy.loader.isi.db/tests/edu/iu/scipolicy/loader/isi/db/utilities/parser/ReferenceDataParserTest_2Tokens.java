package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import static org.junit.Assert.fail;

import org.cishell.utilities.StringUtilities;
import org.junit.Test;

import edu.iu.scipolicy.loader.isi.db.model.entity.Person;
import edu.iu.scipolicy.loader.isi.db.model.entity.Source;

public class ReferenceDataParserTest_2Tokens extends ReferenceDataParserTest {
	@Test
	public void test2Tokens_Invalid() throws Exception {
		ReferenceDataParser result = runTest(", ", true);
		Source resultSource = result.getSource();
		String abbreviation = resultSource.get29CharacterSourceTitleAbbreviation();
		Person resultPerson = result.getAuthor();
		int resultYear = result.getYear();

		if (!StringUtilities.isEmptyOrWhiteSpace(abbreviation) ||
				(resultPerson != null) ||
				(resultYear != IntegerParserWithDefault.DEFAULT)) {
			String failMessage =
				"Result should be invalid.  Instead, it has:" +
				"\n\tAbbreviated title: \"" +
					resultSource.get29CharacterSourceTitleAbbreviation() + "\"" +
				"\n\tPerson: \"" + resultPerson + "\"" +
				"\n\tYear: \"" + resultYear + "\"";
			fail(failMessage);
		}
	}

	/*
	 * Test for the pattern:
	 * year, source
	 */

	@Test
	public void test2Tokens_YearFirst_Year() throws Exception {
		ReferenceDataParser result = runTest(YEAR + ", " + SOURCE_STRING, true);
		int resultYear = result.getYear();

		if (resultYear != YEAR) {
			fail("Result year (" + resultYear + ") != " + YEAR);
		}
	}

	@Test
	public void test2Tokens_YearFirst_NoSourceAnnotation() throws Exception {
		testSourceWithAnnotation(
			YEAR + ", " + SOURCE_STRING, SOURCE_STRING, NO_ANNOTATION, true);
	}

	@Test
	public void test2Tokens_YearFirst_SourceIsAnnotation() throws Exception {
		testSourceAsAnnotations(YEAR_STRING + ", ", NO_POST_STRING, true);
	}

	@Test
	public void test2Tokens_YearFirst_SourceHasAnAnnotation() throws Exception {
		testSourceWithAnnotations(YEAR_STRING + ", ", NO_POST_STRING, SOURCE_STRING, true);
	}

	/*
	 * Tests for the pattern:
	 * person, source
	 * Person parsing is tested in PersonParsingTest.  No need to test it here.
	 */

	@Test
	public void test2Tokens_PersonFirst_NoSourceAnnotation() throws Exception {
		testSourceWithAnnotation(
			PERSON_STRING + ", " + SOURCE_STRING, SOURCE_STRING, NO_ANNOTATION, true);
	}

	@Test
	public void test2Tokens_PersonFirst_SourceIsAnnotation() throws Exception {
		testSourceAsAnnotations(PERSON_STRING + ", ", NO_POST_STRING, true);
	}

	@Test
	public void test2Tokens_PersonFirst_SourceHasAnAnnotation() throws Exception {
		testSourceWithAnnotations(PERSON_STRING + ", ", NO_POST_STRING, SOURCE_STRING, true);
	}
}
