package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import java.util.Arrays;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.cns.shared.utilities.Pair;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;
import edu.iu.scipolicy.loader.isi.db.model.entity.Source;

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
		Arrays.sort(SOURCE_ANNOTATIONS);
	}

	private DatabaseTableKeyGenerator personKeyGenerator;
	private DatabaseTableKeyGenerator sourceKeyGenerator;
	private String rawString;
	private Person author;
	private Source source;
	private int year = ISIDatabase.NULL_YEAR;
	private int volume = ISIDatabase.NULL_VOLUME;
	private int pageNumber = ISIDatabase.NULL_PAGE_NUMBER;
	private String annotation = "";
	private boolean starred = false;

	public ReferenceDataParser(
			DatabaseTableKeyGenerator personKeyGenerator,
			DatabaseTableKeyGenerator sourceKeyGenerator,
			String rawString) throws ReferenceParsingException {
		this.personKeyGenerator = personKeyGenerator;
		this.sourceKeyGenerator = sourceKeyGenerator;
		this.rawString = rawString;

		String[] cleanedTokens = StringUtilities.simpleCleanStrings(rawString.split(","));

		if ((cleanedTokens.length < 2) || (cleanedTokens.length > 5)) {
			String exceptionMessage =
				cleanedTokens.length + " tokens were found.  Expected at least two and at most 5.";
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
		}
	}
	
	public String getRawString() {
		return this.rawString;
	}

	public Person getAuthor() {
		return this.author;
	}

	public Source getSource() {
		return this.source;
	}

	public int getYear() {
		return this.year;
	}

	public int getVolume() {
		return this.volume;
	}

	public int getPageNumber() {
		return this.pageNumber;
	}

	public String getAnnotation() {
		return this.annotation;
	}

	public boolean authorWasStarred() {
		return this.starred;
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
		/*
		 * The pattern is:
		 * person, source
		 */
		} else {
			try {
				Pair<Person, Boolean> parsedPerson =
					PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
				this.author = parsedPerson.getFirstObject();
				this.starred = parsedPerson.getSecondObject();
			} catch (PersonParsingException e) {
			}
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
			boolean isSomeOtherNumber = isSomeOtherNumber(thirdToken);

			if (isVolume || isPage || isSomeOtherNumber) {
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
			try {
				Pair<Person, Boolean> parsedPerson =
					PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
				this.author = parsedPerson.getFirstObject();
				this.starred = parsedPerson.getSecondObject();
			} catch (PersonParsingException e) {
			}

			this.year = IntegerParserWithDefault.parse(secondToken);

			Pair<Source, String> parsedSource =
				parseSource(this.sourceKeyGenerator, thirdToken);
			this.source = parsedSource.getFirstObject();
			this.annotation = parsedSource.getSecondObject();
		} else {
			// TODO: Warning?  (Invalid format.)
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
		 * person; year; source; volume, page, or chapter 
		 */
		} else if (isYear(secondToken)) {
			try {
				Pair<Person, Boolean> parsedPerson =
					PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
				this.author = parsedPerson.getFirstObject();
				this.starred = parsedPerson.getSecondObject();
			} catch (PersonParsingException e) {
			}

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

		try {
			Pair<Person, Boolean> parsedPerson =
				PersonParser.parsePerson(this.personKeyGenerator, firstToken, "");
			this.author = parsedPerson.getFirstObject();
			this.starred = parsedPerson.getSecondObject();
		} catch (PersonParsingException e) {
		}

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

	private static Pair<Source, String> parseSource(
			DatabaseTableKeyGenerator sourceKeyGenerator, String originalToken) {
		int prefixIndex = StringUtilities.prefixIndex(originalToken, SOURCE_ANNOTATIONS);
		String sourceString = originalToken;
		String prefix = "";

		if (prefixIndex != -1) {
			prefix = SOURCE_ANNOTATIONS[prefixIndex];
			sourceString = sourceString.replace(prefix, "").trim();
		}

		String fullTitle = "";
		String publicationType = "";
		String isoTitleAbbreviation = "";
		String bookSeriesTitle = "";
		String bookSeriesSubtitle = "";
		String issn = "";
		String twentyNineCharacterSourceTitleAbbreviation = sourceString;
		String conferenceTitle = "";
		String conferenceDate = "";
		String conferenceDonation = "";

		return new Pair<Source, String>(
			new Source(
				sourceKeyGenerator,
				fullTitle,
				publicationType,
				isoTitleAbbreviation,
				bookSeriesTitle,
				bookSeriesSubtitle,
				issn,
				twentyNineCharacterSourceTitleAbbreviation,
				conferenceTitle,
				conferenceDate,
				conferenceDonation),
			prefix);
	}

	private static int parseVolume(String originalToken) {
		String withoutPrefix = originalToken.replaceFirst(VOLUME_PREFIX_PATTERN, "");

		return IntegerParserWithDefault.parse(withoutPrefix);
	}

	private static int parsePageNumber(String originalToken) {
		String withoutPrefix = originalToken.replaceFirst(PAGE_NUMBER_PREFIX_PATTERN, "");

		return IntegerParserWithDefault.parse(withoutPrefix);
	}

	private static boolean isYear(String token) {
		return token.matches(YEAR_PATTERN);
	}

	private static boolean isVolume(String token) {
		return token.matches(VOLUME_PATTERN);
	}

	private static boolean isPageNumber(String token) {
		return token.matches(PAGE_NUMBER_PATTERN);
	}

	private static boolean isSomeOtherNumber(String token) {
		return token.matches(OTHER_NUMBER_PATTERN);
	}
}