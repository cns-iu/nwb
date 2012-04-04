package edu.iu.sci2.database.isi.load.utilities.parser;

/*
 * TODO: Test for DOI cases.
 *   author, year, source, volume, page, doi
 */

import static org.junit.Assert.fail;

import org.cishell.utilities.StringUtilities;
import org.junit.After;
import org.junit.Before;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.database.isi.load.model.entity.Source;
import edu.iu.sci2.database.isi.load.utilities.parser.ReferenceDataParser;
import edu.iu.sci2.database.isi.load.utilities.parser.exception.ReferenceParsingException;

public class ReferenceDataParserTest {
	public static final String ZERO_REFERENCE_TOKENS_STRING = "";
	public static final String ONE_REFERENCE_TOKEN_STRING = "token1";
	public static final String TWO_REFERENCE_TOKEN_STRING = "token1, token2";
	public static final String THREE_REFERENCE_TOKEN_STRING = "token1, token2, token3";
	public static final String FOUR_REFERENCE_TOKEN_STRING = "token1, token2, token3, token4";
	public static final String FIVE_REFERENCE_TOKEN_STRING =
		"token1, token2, token3, token4, token5";
	public static final String SIX_REFERENCE_TOKEN_STRING =
		"token1, token2, token3, token4, token5, token6";
	public static final String SEVEN_REFERENCE_TOKENS_STRING =
		"token1, token2, token3, token4, token5, token6, token7";

	public static final String NO_ANNOTATION = "";
	public static final String NO_SOURCE = "";

	public static final int YEAR = 1984;
	public static final String YEAR_STRING = "" + YEAR;
	public static final String SOURCE_STRING = "SCI SCI CITATION IND";
	public static final String PERSON_STRING = "SMITH J";
	public static final int VOLUME_NUMBER = 100;
	public static final String VOLUME_STRING = "V" + VOLUME_NUMBER;
	public static final int PAGE_NUMBER = 200;
	public static final String PAGE_NUMBER_STRING = "P" + PAGE_NUMBER;
	public static final String DIGITAL_OBJECT_IDENTIFIER = "10.1098/rstb.2008.2260";
	public static final String DIGITAL_OBJECT_IDENTIFIER_STRING =
		"DOI " + DIGITAL_OBJECT_IDENTIFIER;

	public static final String NO_POST_STRING = "";

	private DatabaseTableKeyGenerator personKeyGenerator;
	private DatabaseTableKeyGenerator sourceKeyGenerator;

	@Before
	public void setUp() throws Exception {
		this.personKeyGenerator = new DatabaseTableKeyGenerator();
		this.sourceKeyGenerator = new DatabaseTableKeyGenerator();
	}

	@After
	public void tearDown() throws Exception {
	}

	protected void testSourceWithAnnotation(
			String rawString,
			String sourceString,
			String providedAnnotation,
			boolean exceptionFails) throws Exception {
		ReferenceDataParser result = runTest(rawString, exceptionFails, true);

		Source source = result.getSource();

		if (source == null) {
			if ((sourceString != null) || (providedAnnotation != null)) {
				String message =
					"No source was parsed." +
					"\n\tProvided source string: \"" + sourceString + "\"" +
					"\n\tProvided annotation: \"" + providedAnnotation + "\"";

				if (exceptionFails) {
					fail(message);
				} else {
					throw new Exception(message);
				}
			}
		}

		String abbreviation = source.get29CharacterSourceTitleAbbreviation();
		String annotation = result.getAnnotation();

		if (!StringUtilities.bothAreEqualOrNull(abbreviation, sourceString)) {
		//if ((abbreviation == null) || !abbreviation.equals(sourceString)) {
			String message =
				"Source was not parsed properly." +
				"\n\tParsed abbreviation: \" " + abbreviation + "\"" +
				"\n\tProvided source string: \" " + sourceString + "\"";

			if (exceptionFails) {
				fail(message);
			} else {
				throw new Exception(message);
			}
		}

		if (!StringUtilities.isNull_Empty_OrWhitespace(annotation) &&
				!annotation.equalsIgnoreCase(providedAnnotation)) {
			String message =
				"Source prefix was not parsed properly." +
				"\n\tParsed annotation: \"" + annotation + "\"" +
				"\n\tProvided annotation: \"" + providedAnnotation + "\"";

			if (exceptionFails) {
				fail(message);
			} else {
				throw new Exception(message);
			}
		}
	}

	protected void testSourceAsAnnotations(String rawPre, String rawPost, boolean exceptionFails)
			throws Exception {
		testSourceWithAnnotations(rawPre, rawPost, NO_SOURCE, exceptionFails);
	}

	protected void testSourceWithAnnotations(
			String rawPre, String rawPost, String sourceString, boolean exceptionFails)
			throws Exception {
		String processedPre = processPre(rawPre);
		String processedPost = processPost(rawPost);

		for (String annotation : ReferenceDataParser.SOURCE_ANNOTATIONS) {
			String rawString = processedPre + annotation + " " + sourceString + processedPost;
			testSourceWithAnnotation(rawString, sourceString, annotation, exceptionFails);
		}
	}

	protected ReferenceDataParser runTest(
			String rawString, boolean exceptionFails, boolean nullSourceFails) throws Exception {
		try {
			ReferenceDataParser referenceData = new ReferenceDataParser(
				this.personKeyGenerator, this.sourceKeyGenerator, rawString);

			if (nullSourceFails && (referenceData.getSource() == null)) {
				// TODO: Exception message.
				throw new Exception();
			} else {
				return referenceData;
			}
		} catch (ReferenceParsingException e) {
			if (exceptionFails) {
				fail("No exception should be thrown: " + e.getMessage());
			}

			throw e;
		}
	}

	private String processPre(String rawPre) {
		if (!StringUtilities.isNull_Empty_OrWhitespace(rawPre)) {
			return rawPre + " ";
		} else {
			return "";
		}
	}

	private String processPost(String rawPost) {
		if (!StringUtilities.isNull_Empty_OrWhitespace(rawPost)) {
			return rawPost;
		} else {
			return "";
		}
	}
}