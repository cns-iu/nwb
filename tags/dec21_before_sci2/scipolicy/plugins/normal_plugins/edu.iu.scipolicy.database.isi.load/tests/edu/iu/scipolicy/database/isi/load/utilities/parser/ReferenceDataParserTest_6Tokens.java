package edu.iu.scipolicy.database.isi.load.utilities.parser;

import static org.junit.Assert.fail;

import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.StringUtilities;
import org.junit.Test;

import edu.iu.scipolicy.database.isi.load.model.entity.Source;

public class ReferenceDataParserTest_6Tokens extends ReferenceDataParserTest {
	@Test
	public void test6Tokens_Invalid() throws Exception {
		ReferenceDataParser result = runTest(",  ,  ,  ,  ,  ", true, false);
		Integer resultYear = result.getYear();
		Source resultSource = result.getSource();
		Integer resultVolume = result.getVolume();
		Integer resultPageNumber = result.getPageNumber();
		String resultDigitalObjectIdentifier = result.getDigitalObjectIdentifier();

		if (resultSource != null) {
			if ((resultYear != IntegerParserWithDefault.DEFAULT) ||
					!StringUtilities.isNull_Empty_OrWhitespace(
						resultSource.get29CharacterSourceTitleAbbreviation()) ||
					(resultVolume != IntegerParserWithDefault.DEFAULT) ||
					(resultPageNumber != IntegerParserWithDefault.DEFAULT) ||
					!"".equals(resultDigitalObjectIdentifier)) {
				String failMessage =
					"Result should be invalid.  Instead, it has:" +
					"\n\tYear: \"" + resultYear + "\"" +
					"\n\tSource: \"" + resultSource + "\"" +
					"\n\tVolume: \"" + resultVolume + "\"" +
					"\n\tPage number: \"" + resultPageNumber + "\"";
				fail(failMessage);
			}
		} else {
			if ((resultYear != IntegerParserWithDefault.DEFAULT) ||
					(resultVolume != IntegerParserWithDefault.DEFAULT) ||
					(resultPageNumber != IntegerParserWithDefault.DEFAULT) ||
					!StringUtilities.isNull_Empty_OrWhitespace(resultDigitalObjectIdentifier)) {
				String failMessage =
					"Result should be invalid.  Instead, it has:" +
					"\n\tYear: \"" + resultYear + "\"" +
					"\n\tSource: \"" + resultSource + "\"" +
					"\n\tVolume: \"" + resultVolume + "\"" +
					"\n\tPage number: \"" + resultPageNumber + "\"";
				fail(failMessage);
			}
		}
	}

	/*
	 * Tests for the pattern:
	 * person, year, source, volume, page number, doi
	 * (This is most likely the only acceptable pattern.)
	 * Person parsing is tested in PersonTest.  No need to test it here.
	 */

	@Test
	public void test6Tokens_PersonFirst_Year() throws Exception {
		ReferenceDataParser result = runTest(
			PERSON_STRING + ", " +
				YEAR + ", " +
				SOURCE_STRING + ", " +
				VOLUME_STRING + ", " +
				PAGE_NUMBER_STRING + ", " +
				DIGITAL_OBJECT_IDENTIFIER_STRING,
			true,
			true);
		int resultYear = result.getYear();

		if (resultYear != YEAR) {
			fail("Result year (" + resultYear + ") != " + YEAR);
		}
	}

	@Test
	public void test6Tokens_PersonFirst_NoSourceAnnotation() throws Exception {
		testSourceWithAnnotation(
			PERSON_STRING + ", " +
				YEAR + ", " +
				SOURCE_STRING + ", " +
				VOLUME_STRING + ", " +
				PAGE_NUMBER_STRING + ", " +
				DIGITAL_OBJECT_IDENTIFIER_STRING,
			SOURCE_STRING,
			NO_ANNOTATION,
			true);
	}

	@Test
	public void test6Tokens_PersonFirst_SourceIsAnAnnotation() throws Exception {
		testSourceAsAnnotations(
			PERSON_STRING + ", " + YEAR + ", ",
			", " + VOLUME_STRING + ", " + PAGE_NUMBER_STRING + ", " + DIGITAL_OBJECT_IDENTIFIER_STRING,
			true);
	}

	@Test
	public void test6Tokens_PersonFirst_SourceHasAnAnnotation() throws Exception {
		testSourceWithAnnotations(
			PERSON_STRING + ", " + YEAR + ", ",
			", " + VOLUME_STRING + ", " + PAGE_NUMBER_STRING + ", " + DIGITAL_OBJECT_IDENTIFIER_STRING,
			SOURCE_STRING,
			true);
	}

	@Test
	public void test6Tokens_PersonFirst_Volume() throws Exception {
		ReferenceDataParser result = runTest(
			PERSON_STRING + ", " +
				YEAR + ", " +
				SOURCE_STRING + ", " +
				VOLUME_STRING + ", " +
				PAGE_NUMBER_STRING + ", " +
				DIGITAL_OBJECT_IDENTIFIER_STRING,
			true,
			true);
		int resultVolume = result.getVolume();

		if (resultVolume != VOLUME_NUMBER) {
			fail("Result volume (" + resultVolume + ") != " + VOLUME_NUMBER);
		}
	}

	@Test
	public void test6Tokens_PersonFirst_PageNumber() throws Exception {
		ReferenceDataParser result = runTest(
			PERSON_STRING + ", " +
				YEAR + ", " +
				SOURCE_STRING + ", " +
				VOLUME_STRING + ", " +
				PAGE_NUMBER_STRING + ", " +
				DIGITAL_OBJECT_IDENTIFIER_STRING,
			true,
			true);
		int resultPageNumber = result.getPageNumber();

		if (resultPageNumber != PAGE_NUMBER) {
			fail("Result page number (" + resultPageNumber + ") != " + PAGE_NUMBER);
		}
	}

	/*
	 * Actually test that this DOI turns into a unique document/gets merged/etc. in the ISI
	 *  parser tests.
	 */
	@Test
	public void test6Tokens_PersonFirst_DigitalObjectIdentifier() throws Exception {
		ReferenceDataParser result = runTest(
			PERSON_STRING + ", " +
				YEAR + ", " +
				SOURCE_STRING + ", " +
				VOLUME_STRING + ", " +
				PAGE_NUMBER_STRING + ", " +
				DIGITAL_OBJECT_IDENTIFIER_STRING,
			true,
			true);
		String resultDigitalObjectIdentifier = result.getDigitalObjectIdentifier();

		if (!resultDigitalObjectIdentifier.equals(DIGITAL_OBJECT_IDENTIFIER)) {
			String failMessage =
				"Result digital object identifier (" +
				resultDigitalObjectIdentifier +
				") != " +
				DIGITAL_OBJECT_IDENTIFIER;

			fail(failMessage);
		}
	}
}
