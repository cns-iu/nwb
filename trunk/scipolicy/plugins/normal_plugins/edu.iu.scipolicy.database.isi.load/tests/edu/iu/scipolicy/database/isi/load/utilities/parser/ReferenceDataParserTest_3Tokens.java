package edu.iu.scipolicy.database.isi.load.utilities.parser;

import static org.junit.Assert.fail;

import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.StringUtilities;
import org.junit.Test;

import edu.iu.scipolicy.database.isi.load.model.entity.Source;

public class ReferenceDataParserTest_3Tokens extends ReferenceDataParserTest {
	@Test
	public void test3Tokens_Invalid() throws Exception {
		ReferenceDataParser result = runTest(", ,  ", true, false);
		Source resultSource = result.getSource();
		Integer resultYear = result.getYear();
		Integer resultVolume = result.getVolume();
		Integer resultPageNumber = result.getPageNumber();

		if (resultSource != null) {
			if (!StringUtilities.isNull_Empty_OrWhitespace(
						resultSource.get29CharacterSourceTitleAbbreviation()) ||
					(resultYear != IntegerParserWithDefault.DEFAULT) ||
					(resultVolume != IntegerParserWithDefault.DEFAULT) ||
					(resultPageNumber != IntegerParserWithDefault.DEFAULT)) {
				String failMessage =
					"Result should be invalid.  Instead, it has:" +
					"\n\tSource: \"" + resultSource + "\"" +
					"\n\tYear: \"" + resultYear + "\"" +
					"\n\tVolume: \"" + resultVolume + "\"" +
					"\n\tPage number: \"" + resultPageNumber + "\"";
				fail(failMessage);
			}
		} else {
			if ((resultSource != null) ||
					(resultYear != IntegerParserWithDefault.DEFAULT) ||
					(resultVolume != IntegerParserWithDefault.DEFAULT) ||
					(resultPageNumber != IntegerParserWithDefault.DEFAULT)) {
				String failMessage =
					"Result should be invalid.  Instead, it has:" +
					"\n\tSource: \"" + resultSource + "\"" +
					"\n\tYear: \"" + resultYear + "\"" +
					"\n\tVolume: \"" + resultVolume + "\"" +
					"\n\tPage number: \"" + resultPageNumber + "\"";
				fail(failMessage);
			}
		}
	}

	/*
	 * Tests for the pattern:
	 * year, source, volume or page number
	 */

	@Test
	public void test3Tokens_YearFirst_InvalidThirdToken() throws Exception {
		ReferenceDataParser result = runTest(
			YEAR + ", " + SOURCE_STRING + ", ",
			true,
			true);
		Integer resultVolume = result.getVolume();
		Integer resultPageNumber = result.getPageNumber();

		if ((resultVolume != IntegerParserWithDefault.DEFAULT) ||
				(resultPageNumber != IntegerParserWithDefault.DEFAULT)) {
			String failMessage =
				"Result should not have a valid volume or page number." +
				"\n\tVolume: \"" + resultVolume + "\"" +
				"\n\tPage number: \"" + resultPageNumber + "\"";
			fail(failMessage);
		}
	}

	@Test
	public void test3Tokens_YearFirst_Year() throws Exception {
		ReferenceDataParser result = runTest(
			YEAR + ", " + SOURCE_STRING + ", " + VOLUME_STRING,
			true,
			true);
		Integer resultYear = result.getYear();

		if (resultYear != YEAR) {
			fail("Result year (" + resultYear + ") != " + YEAR);
		}
	}

	@Test
	public void test3Tokens_YearFirst_NoSourceAnnotation() throws Exception {
		testSourceWithAnnotation(
			YEAR + ", " + SOURCE_STRING + ", " + VOLUME_STRING,
			SOURCE_STRING,
			NO_ANNOTATION,
			true);
	}

	@Test
	public void test3Tokens_YearFirst_SourceIsAnAnnotation() throws Exception {
		testSourceAsAnnotations(
			YEAR + ", ",
			", " + VOLUME_STRING,
			true);
	}

	@Test
	public void test3Tokens_YearFirst_SourceHasAnAnnotation() throws Exception {
		testSourceWithAnnotations(
			YEAR + ", ",
			", " + VOLUME_STRING,
			SOURCE_STRING,
			true);
	}

	@Test
	public void test3Tokens_YearFirst_HasVolume() throws Exception {
		ReferenceDataParser result = runTest(
			YEAR + ", " + SOURCE_STRING + ", " + VOLUME_STRING,
			true,
			true);
		int resultVolume = result.getVolume();

		if (resultVolume != VOLUME_NUMBER) {
			fail("Result volume (" + resultVolume + ") != " + VOLUME_NUMBER);
		}
	}

	@Test
	public void test3Tokens_YearFirst_HasPageNumber() throws Exception {
		ReferenceDataParser result = runTest(
			YEAR + ", " + SOURCE_STRING + ", " + PAGE_NUMBER_STRING,
			true,
			false);
		int resultPageNumber = result.getPageNumber();

		if (resultPageNumber != PAGE_NUMBER) {
			fail("Result page number (" + resultPageNumber + ") != " + PAGE_NUMBER);
		}
	}

	/*
	 * Tests for the pattern:
	 * person, year, source
	 * Person parsing is tested in PersonTest.  No need to test it here.
	 */

	@Test
	public void test3Tokens_PersonFirst_YearSecond_Year() throws Exception {
		ReferenceDataParser result = runTest(
			PERSON_STRING + ", " + YEAR + ", " + SOURCE_STRING,
			true,
			true);
		int resultYear = result.getYear();

		if (resultYear != YEAR) {
			fail("Result year (" + resultYear + ") != " + YEAR);
		}
	}

	@Test
	public void test3Tokens_PersonFirst_YearSecond_NoSourceAnnotation() throws Exception {
		testSourceWithAnnotation(
			PERSON_STRING + ", " + YEAR + ", " + SOURCE_STRING,
			SOURCE_STRING,
			NO_ANNOTATION,
			true);
	}

	@Test
	public void test3Tokens_PersonFirst_YearSecond_SourceIsAnAnnotation() throws Exception {
		testSourceAsAnnotations(
			PERSON_STRING + ", " + YEAR + ", ",
			NO_POST_STRING,
			true);
	}

	@Test
	public void test3Tokens_PersonFirst_YearSecond_SourceHasAnAnnotation() throws Exception {
		testSourceWithAnnotations(
			PERSON_STRING + ", " + YEAR + ", ",
			NO_POST_STRING,
			SOURCE_STRING,
			true);
	}

	/*
	 * Tests for the pattern:
	 * person, source, volume or page number
	 * Person parsing is tested in PersonTest.  No need to test it here.
	 */

	@Test
	public void test3Tokens_PersonFirst_SourceSecond_InvalidThirdToken() throws Exception {
		ReferenceDataParser result = runTest(
			PERSON_STRING + ", " + SOURCE_STRING + ", ",
			true,
			true);
		Integer resultVolume = result.getVolume();
		Integer resultPageNumber = result.getPageNumber();

		if ((resultVolume != IntegerParserWithDefault.DEFAULT) ||
				(resultPageNumber != IntegerParserWithDefault.DEFAULT)) {
			String failMessage =
				"Result should not have a valid volume or page number." +
				"\n\tVolume: \"" + resultVolume + "\"" +
				"\n\tPage number: \"" + resultPageNumber + "\"";
			fail(failMessage);
		}
	}

	@Test
	public void test3Tokens_PersonFirst_SourceSecond_NoSourceAnnotation() throws Exception {
		testSourceWithAnnotation(
			PERSON_STRING + ", " + SOURCE_STRING + ", " + VOLUME_STRING,
			SOURCE_STRING,
			NO_ANNOTATION,
			true);
	}

	@Test
	public void test3Tokens_PersonFirst_SourceSecond_SourceIsAnAnnotation() throws Exception {
		testSourceAsAnnotations(
			PERSON_STRING + ", ",
			", " + VOLUME_STRING,
			true);
	}

	@Test
	public void test3Tokens_PersonFirst_SourceSecond_SourceHasAnAnnotation() throws Exception {
		testSourceWithAnnotations(
			PERSON_STRING + ", ",
			", " + VOLUME_STRING,
			SOURCE_STRING,
			true);
	}

	@Test
	public void test3Tokens_PersonFirst_SourceSecond_HasVolume() throws Exception {
		ReferenceDataParser result = runTest(
			PERSON_STRING + ", " + SOURCE_STRING + ", " + VOLUME_STRING,
			true,
			true);
		int resultVolume = result.getVolume();

		if (resultVolume != VOLUME_NUMBER) {
			fail("Result volume (" + resultVolume + ") != " + VOLUME_NUMBER);
		}
	}

	@Test
	public void test3Tokens_PersonFirst_SourceSecond_HasPageNumber() throws Exception {
		ReferenceDataParser result = runTest(
			PERSON_STRING + ", " + SOURCE_STRING + ", " + PAGE_NUMBER_STRING,
			true,
			true);
		int resultPageNumber = result.getPageNumber();

		if (resultPageNumber != PAGE_NUMBER) {
			fail("Result page number (" + resultPageNumber + ") != " + PAGE_NUMBER);
		}
	}
}
