package edu.iu.sci2.database.isi.load.utilities.parser;

import java.util.Arrays;

import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.Pair;
import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.database.isi.load.model.entity.Person;
import edu.iu.sci2.database.isi.load.model.entity.Source;
import edu.iu.sci2.database.isi.load.utilities.parser.exception.PersonParsingException;
import edu.iu.sci2.database.isi.load.utilities.parser.exception.ReferenceParsingException;

public class ReferenceDataParser {
	public static final String YEAR_PATTERN = "\\d\\d\\d\\d";
	public static final String AT_LEAST_ONE_NUMBER_PATTERN = "\\d+";
	public static final String VOLUME_PREFIX_PATTERN = "[vV]";
	public static final String VOLUME_PATTERN =
		VOLUME_PREFIX_PATTERN + AT_LEAST_ONE_NUMBER_PATTERN;
	public static final String PAGE_NUMBER_PREFIX_PATTERN = "[pP]";
	public static final String PAGE_NUMBER_PATTERN =
		PAGE_NUMBER_PREFIX_PATTERN + AT_LEAST_ONE_NUMBER_PATTERN;
	public static final String OTHER_PREFIX_PATTERN = "[a-zA-Z]+";
	public static final String OTHER_NUMBER_PATTERN =
		OTHER_PREFIX_PATTERN + AT_LEAST_ONE_NUMBER_PATTERN;
	public static final String DIGITAL_OBJECT_IDENTIFIER_KEYWORD = "DOI ";
	public static final String DIGITAL_OBJECT_IDENTIFIER_PATTERN =
		DIGITAL_OBJECT_IDENTIFIER_KEYWORD + ".*/.*";

	public static final String ARTICLE_NUMBER_PREFIX = "ARTN ";
	public static final String[] OTHER_INFORMATION_PREFIXES = new String[] {
		"PII ",
		"PMID ",
		"UNSP ",
	};
	public static final String[] SOURCE_ANNOTATIONS = new String[] {
		"UNPUB",
		"IN PRESS",
		"PREPRINT",
		"UNPUBLISHED",
		"CITED INDIRECTLY",
		"PRIVATE COMMUNICATIO",
		"UNOPUB",
	};

	static {
		Arrays.sort(OTHER_INFORMATION_PREFIXES);
		Arrays.sort(SOURCE_ANNOTATIONS);
	}

	public static int MINIMUM_NUMBER_OF_TOKENS_FOR_VALID_REFERENCE = 2;
	public static int MAXIMUM_NUMBER_OF_TOKENS_FOR_VALID_REFERENCE = 6;

	private DatabaseTableKeyGenerator personKeyGenerator;
	private DatabaseTableKeyGenerator sourceKeyGenerator;
	private String annotation;
	private String articleNumber;
	private Person authorPerson;
	//private Boolean starred = false;
	private String digitalObjectIdentifier;
	private String otherInformation;
	private Integer pageNumber;
	private String rawString;
	private Source source;
	private Integer volume;
	private Integer year;
	public boolean starred = false;

	public ReferenceDataParser(
			DatabaseTableKeyGenerator personKeyGenerator,
			DatabaseTableKeyGenerator sourceKeyGenerator,
			String rawString) throws ReferenceParsingException {
		this.personKeyGenerator = personKeyGenerator;
		this.sourceKeyGenerator = sourceKeyGenerator;
		this.rawString = rawString;

		if (this.rawString.startsWith("*")) {
			this.starred = true;
			this.rawString = this.rawString.replaceFirst("\\**+", "");
		}

		String[] specialCaseCleanedTokens =
			StringUtilities.simpleCleanStrings(this.rawString.split(", "));
		String[] cleanedTokens = handleSpecialCaseTokens(specialCaseCleanedTokens);

		if ((cleanedTokens.length < MINIMUM_NUMBER_OF_TOKENS_FOR_VALID_REFERENCE) ||
				(cleanedTokens.length > MAXIMUM_NUMBER_OF_TOKENS_FOR_VALID_REFERENCE)) {
			String exceptionMessage =
				cleanedTokens.length +
				" tokens were found.  Expected at least " +
				MINIMUM_NUMBER_OF_TOKENS_FOR_VALID_REFERENCE +
				" and at most " +
				MAXIMUM_NUMBER_OF_TOKENS_FOR_VALID_REFERENCE +
				".";
			throw new ReferenceParsingException(exceptionMessage);
		}
		else if (cleanedTokens.length == 2) {
			parseTwoTokens(cleanedTokens);
		} else if (cleanedTokens.length == 3) {
			parseThreeTokens(cleanedTokens);
		} else if (cleanedTokens.length == 4) {
			parseFourTokens(cleanedTokens);
		} else if (cleanedTokens.length == 5) {
			parseFiveTokens(cleanedTokens);
		} else if((cleanedTokens.length == 6) && isDigitalObjectIdentifier(cleanedTokens[5])) {
			parseSixTokens(cleanedTokens);
		}
	}

	public String getAnnotation() {
		return this.annotation;
	}

	public String getArticleNumber() {
		return this.articleNumber;
	}

	public Person getAuthorPerson() {
		return this.authorPerson;
	}

	/*public Boolean authorWasStarred() {
		return this.starred;
	}*/

	public String getDigitalObjectIdentifier() {
		return this.digitalObjectIdentifier;
	}

	public String getOtherInformation() {
		return this.otherInformation;
	}

	public Integer getPageNumber() {
		return this.pageNumber;
	}

	public String getRawString() {
		return this.rawString;
	}

	public Source getSource() {
		return this.source;
	}

	public Integer getVolume() {
		return this.volume;
	}

	public Integer getYear() {
		return this.year;
	}

	public boolean wasStarred() {
		return this.starred;
	}

	private String[] handleSpecialCaseTokens(String[] specialCaseTokens) {
		if (specialCaseTokens.length == 0) {
			return specialCaseTokens;
		}

		String lastToken = specialCaseTokens[specialCaseTokens.length - 1];

		if (isArticleNumber(lastToken)) {
			this.articleNumber = parseArticleNumber(lastToken);
			String[] normalCaseTokens = new String[specialCaseTokens.length - 1];
			System.arraycopy(specialCaseTokens, 0, normalCaseTokens, 0, normalCaseTokens.length);

			return normalCaseTokens;
		} else if (isOtherInformation(lastToken)) {
			this.otherInformation = parseOtherInformation(lastToken);
			String[] normalCaseTokens = new String[specialCaseTokens.length - 1];
			System.arraycopy(specialCaseTokens, 0, normalCaseTokens, 0, normalCaseTokens.length);

			return normalCaseTokens;
		} else {
			return specialCaseTokens;
		}
	}

	private void parseTwoTokens(String[] tokens) {
		String firstToken = tokens[0];
		String secondToken = tokens[1];

		/*
		 * The pattern is:
		 * year, source
		 */
		if (isYear(firstToken)) {
			this.year = IntegerParserWithDefault.parse(firstToken);
		} else {
			if (!StringUtilities.isNull_Empty_OrWhitespace(firstToken)) {
				this.authorPerson = new Person(this.personKeyGenerator, firstToken, "");
			}
			//try {
				/*Pair<Person, Boolean> parsedPerson =
					PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
				this.authorPerson = parsedPerson.getFirstObject();
				this.starred = parsedPerson.getSecondObject();*/
			//} catch (PersonParsingException e) {}
		}

		Pair<Source, String> parsedSource =
			parseSource(this.sourceKeyGenerator, secondToken);
		this.source = parsedSource.getFirstObject();
		this.annotation = parsedSource.getSecondObject();
	}

	private void parseThreeTokens(String[] tokens) {
		String firstToken = tokens[0];
		String secondToken = tokens[1];
		String thirdToken = tokens[2];

		/*
		 * Actually, I've yet to find this case, but that's not saying it won't happen.
		 * Based on the 4 token case, I assume the order for this case is:
		 * year, source, volume or page
		 */
		if (isYear(firstToken)) {
			this.year = IntegerParserWithDefault.parse(firstToken);

			Pair<Source, String> parsedSource =
				parseSource(this.sourceKeyGenerator, secondToken);
			this.source = parsedSource.getFirstObject();
			this.annotation = parsedSource.getSecondObject();

			boolean isVolume = isVolume(thirdToken);
			boolean isPage = isPageNumber(thirdToken);

			if (isVolume || isPage) {
				if (isVolume) {
					this.volume = parseVolume(thirdToken);
				} else {
					this.pageNumber = parsePageNumber(thirdToken);
				}
			} else {
				// TODO: Warning?  (Invalid format.)
			}
		/*
		 * The pattern is:
		 * person, year, source
		 */
		} else if (isYear(secondToken)) {
			if (!StringUtilities.isNull_Empty_OrWhitespace(firstToken)) {
				this.authorPerson = new Person(this.personKeyGenerator, firstToken, "");
			}
			/*try {
				Pair<Person, Boolean> parsedPerson =
					PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
				this.authorPerson = parsedPerson.getFirstObject();
				this.starred = parsedPerson.getSecondObject();
			} catch (PersonParsingException e) {}*/

			this.year = IntegerParserWithDefault.parse(secondToken);

			Pair<Source, String> parsedSource =
				parseSource(this.sourceKeyGenerator, thirdToken);
			this.source = parsedSource.getFirstObject();
			this.annotation = parsedSource.getSecondObject();
		/*
		 * Assume the pattern is:
		 * person, source, volume or page
		 */
		} else {
			if (!StringUtilities.isNull_Empty_OrWhitespace(firstToken)) {
				this.authorPerson = new Person(this.personKeyGenerator, firstToken, "");
			}
			/*try {
				Pair<Person, Boolean> parsedPerson =
					PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
				this.authorPerson = parsedPerson.getFirstObject();
				this.starred = parsedPerson.getSecondObject();
			} catch (PersonParsingException e) {}*/

			Pair<Source, String> parsedSource =
				parseSource(this.sourceKeyGenerator, secondToken);
			this.source = parsedSource.getFirstObject();
			this.annotation = parsedSource.getSecondObject();

			boolean isVolume = isVolume(thirdToken);
			boolean isPage = isPageNumber(thirdToken);

			if (isVolume || isPage) {
				if (isVolume) {
					this.volume = parseVolume(thirdToken);
				} else {
					this.pageNumber = parsePageNumber(thirdToken);
				}
			} else {
				// TODO: Warning?  (Invalid format.)
			}
		}
	}

	private void parseFourTokens(String[] tokens) {
		String firstToken = tokens[0];
		String secondToken = tokens[1];
		String thirdToken = tokens[2];
		String fourthToken = tokens[3];

		/*
		 * The pattern is:
		 * year, source, volume, page
		 */
		if (isYear(firstToken)) {
			this.year = IntegerParserWithDefault.parse(firstToken);

			Pair<Source, String> parsedSource =
				parseSource(this.sourceKeyGenerator, secondToken);
			this.source = parsedSource.getFirstObject();
			this.annotation = parsedSource.getSecondObject();

			if (isVolume(thirdToken)) {
				this.volume = parseVolume(thirdToken);
			} else {
				// TODO: Warning?  (Invalid format.)
			}

			if (isPageNumber(fourthToken)) {
				this.pageNumber = parsePageNumber(fourthToken);
			} else {
				// TODO: Warning?  (Invalid format.)
			}
		/*
		 * The pattern is:
		 * person, year, source, volume or page 
		 */
		} else if (isYear(secondToken)) {
			if (!StringUtilities.isNull_Empty_OrWhitespace(firstToken)) {
				this.authorPerson = new Person(this.personKeyGenerator, firstToken, "");
			}
			/*try {
				Pair<Person, Boolean> parsedPerson =
					PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
				this.authorPerson = parsedPerson.getFirstObject();
				this.starred = parsedPerson.getSecondObject();
			} catch (PersonParsingException e) {}*/

			this.year = IntegerParserWithDefault.parse(secondToken);

			Pair<Source, String> parsedSource =
				parseSource(this.sourceKeyGenerator, thirdToken);
			this.source = parsedSource.getFirstObject();
			this.annotation = parsedSource.getSecondObject();

			if (isVolume(fourthToken)) {
				this.volume = parseVolume(fourthToken);
			} else if (isPageNumber(fourthToken)) {
				this.pageNumber = parsePageNumber(fourthToken);
			} else {
				// TODO: Warning?  (Invalid format.)
			}
		/*
		 * The pattern is:
		 * person, source, volume, page
		 */
		} else if (isVolume(thirdToken) && isPageNumber(fourthToken)) {
			if (!StringUtilities.isNull_Empty_OrWhitespace(firstToken)) {
				this.authorPerson = new Person(this.personKeyGenerator, firstToken, "");
			}
			/*try {
				Pair<Person, Boolean> parsedPerson =
					PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
				this.authorPerson = parsedPerson.getFirstObject();
				this.starred = parsedPerson.getSecondObject();
			} catch (PersonParsingException e) {}*/

			Pair<Source, String> parsedSource =
				parseSource(this.sourceKeyGenerator, secondToken);
			this.source = parsedSource.getFirstObject();
			this.annotation = parsedSource.getSecondObject();

			this.volume = parseVolume(thirdToken);

			this.pageNumber = parsePageNumber(fourthToken);
		} else {
			// TODO: Warning?  (Invalid format.)
		}
	}

	/*
	 * I think the only acceptable pattern is:
	 * person, year, journal, volume, page number
	 */
	private void parseFiveTokens(String[] tokens) {
		String firstToken = tokens[0];
		String secondToken = tokens[1];
		String thirdToken = tokens[2];
		String fourthToken = tokens[3];
		String fifthToken = tokens[4];

		if (!StringUtilities.isNull_Empty_OrWhitespace(firstToken)) {
			this.authorPerson = new Person(this.personKeyGenerator, firstToken, "");
		}
		/*try {
			Pair<Person, Boolean> parsedPerson =
				PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
			this.authorPerson = parsedPerson.getFirstObject();
			this.starred = parsedPerson.getSecondObject();
		} catch (PersonParsingException e) {}*/

		this.year = IntegerParserWithDefault.parse(secondToken);

		Pair<Source, String> parsedSource =
			parseSource(this.sourceKeyGenerator, thirdToken);
		this.source = parsedSource.getFirstObject();
		this.annotation = parsedSource.getSecondObject();
		
		if (isVolume(fourthToken)) {
			this.volume = parseVolume(fourthToken);

			if (isPageNumber(fifthToken)) {
				this.pageNumber = parsePageNumber(fifthToken);
			} else {
				// TODO: Warning?  (Invalid format.)
			}
		} else {
			// TODO: Warning?  (Invalid format.)
		}
	}

	/*
	 * I think the only acceptable pattern is:
	 * person, year, source, volume, page number, doi
	 */
	private void parseSixTokens(String[] tokens) {
		String firstToken = tokens[0];
		String secondToken = tokens[1];
		String thirdToken = tokens[2];
		String fourthToken = tokens[3];
		String fifthToken = tokens[4];
		String sixthToken = tokens[5];

		if (!StringUtilities.isNull_Empty_OrWhitespace(firstToken)) {
			this.authorPerson = new Person(this.personKeyGenerator, firstToken, "");
		}
		/*try {
			Pair<Person, Boolean> parsedPerson =
				PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
			this.authorPerson = parsedPerson.getFirstObject();
			this.starred = parsedPerson.getSecondObject();
		} catch (PersonParsingException e) {}*/

		this.year = IntegerParserWithDefault.parse(secondToken);

		Pair<Source, String> parsedSource =
			parseSource(this.sourceKeyGenerator, thirdToken);
		this.source = parsedSource.getFirstObject();
		this.annotation = parsedSource.getSecondObject();

		if (isVolume(fourthToken)) {
			this.volume = parseVolume(fourthToken);

			if (isPageNumber(fifthToken)) {
				this.pageNumber = parsePageNumber(fifthToken);
				this.digitalObjectIdentifier = parseDigitalObjectIdentifier(sixthToken);
			} else {
				// TODO: Warning?  (Invalid format.)
			}
		} else {
			// TODO: Warning?  (Invalid format.)
		}
	}

	private static Pair<Source, String> parseSource(
			DatabaseTableKeyGenerator sourceKeyGenerator, String originalToken) {
		int annotationIndex = StringUtilities.prefixIndex(originalToken, SOURCE_ANNOTATIONS);
		String annotation = null;
		String sourceString = originalToken;

		if (annotationIndex != -1) {
			annotation = SOURCE_ANNOTATIONS[annotationIndex];
			sourceString = sourceString.replace(annotation, "").trim();
		}

		String bookSeriesTitle = "";
		String bookSeriesSubtitle = "";
		String conferenceHost = "";
		String conferenceLocation = "";
		String conferenceSponsors = "";
		String conferenceTitle = "";
		String fullTitle = "";
		String isoTitleAbbreviation = "";
		String issn = "";
		String publicationType = "";
		String twentyNineCharacterSourceTitleAbbreviation = sourceString;

		if (!StringUtilities.allAreNull_Empty_OrWhitespace(
				annotation,
				bookSeriesTitle,
				bookSeriesSubtitle,
				conferenceHost,
				conferenceLocation,
				conferenceSponsors,
				conferenceTitle,
				fullTitle,
				isoTitleAbbreviation,
				issn,
				publicationType,
				twentyNineCharacterSourceTitleAbbreviation)) {
			return new Pair<Source, String>(
//				new Source(
//					sourceKeyGenerator,
//					/*bookSeriesTitle,
//					bookSeriesSubtitle,
//					conferenceHost,
//					conferenceLocation,
//					conferenceSponsors,
//					conferenceTitle,
//					fullTitle,
//					isoTitleAbbreviation,
//					issn,
//					publicationType,*/
//					twentyNineCharacterSourceTitleAbbreviation),
				new Source(sourceKeyGenerator, twentyNineCharacterSourceTitleAbbreviation, null),
				annotation);
		} else {
			return new Pair<Source, String>(null, annotation);
		}
	}

	private static String parseArticleNumber(String originalToken) {
		return originalToken.replaceFirst(ARTICLE_NUMBER_PREFIX, "");
	}

	private static String parseOtherInformation(String originalToken) {
		/*int otherInformationPrefixIndex =
			StringUtilities.prefixIndex(originalToken, OTHER_INFORMATION_PREFIXES);
		String prefix = OTHER_INFORMATION_PREFIXES[otherInformationPrefixIndex];

		return originalToken.replaceFirst(prefix, "").trim();*/
		return originalToken.trim();
	}

	private static Integer parseVolume(String originalToken) {
		String withoutPrefix = originalToken.replaceFirst(VOLUME_PREFIX_PATTERN, "");

		return IntegerParserWithDefault.parse(withoutPrefix);
	}

	private static Integer parsePageNumber(String originalToken) {
		String withoutPrefix = originalToken.replaceFirst(PAGE_NUMBER_PREFIX_PATTERN, "");

		return IntegerParserWithDefault.parse(withoutPrefix);
	}

	private static String parseDigitalObjectIdentifier(String originalToken) {
		return originalToken.replaceFirst(DIGITAL_OBJECT_IDENTIFIER_KEYWORD, "");
	}

	private static boolean isArticleNumber(String token) {
		return token.startsWith(ARTICLE_NUMBER_PREFIX);
	}

	private static boolean isOtherInformation(String token) {
		int otherInformationPrefixIndex =
			StringUtilities.prefixIndex(token, OTHER_INFORMATION_PREFIXES);

		return (otherInformationPrefixIndex != -1);
	}

	private static boolean isYear(String token) {
		return token.matches(YEAR_PATTERN);
	}

	private static boolean isDigitalObjectIdentifier(String token) {
		return token.matches(DIGITAL_OBJECT_IDENTIFIER_PATTERN);
	}

	private static boolean isVolume(String token) {
		return token.matches(VOLUME_PATTERN);
	}

	private static boolean isPageNumber(String token) {
		return token.matches(PAGE_NUMBER_PATTERN);
	}
}