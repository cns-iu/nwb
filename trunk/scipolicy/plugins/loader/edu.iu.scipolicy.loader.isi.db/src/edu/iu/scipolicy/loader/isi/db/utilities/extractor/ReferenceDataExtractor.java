package edu.iu.scipolicy.loader.isi.db.utilities.extractor;

import java.util.Arrays;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.cns.shared.utilities.GenericPair;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;
import edu.iu.scipolicy.loader.isi.db.model.entity.Source;

public class ReferenceDataExtractor {
	public static final String YEAR_PATTERN = "\\d\\d\\d\\d";

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
	private int year = -1;
	private String volume = "";
	private int pageNumber = -1;
	private String annotation = "";
	private boolean starred = false;

	public ReferenceDataExtractor(
			DatabaseTableKeyGenerator personKeyGenerator,
			DatabaseTableKeyGenerator sourceKeyGenerator,
			String rawString) {
		this.personKeyGenerator = personKeyGenerator;
		this.sourceKeyGenerator = sourceKeyGenerator;
		this.rawString = rawString;

		String[] cleanedTokens = StringUtilities.simpleCleanStrings(rawString.split(","));

		if ((cleanedTokens.length < 2) || (cleanedTokens.length > 5)) {
			// TODO: Warning?
			
		}
		else if (cleanedTokens.length == 2) {
			String firstToken = cleanedTokens[0];
			String secondToken = cleanedTokens[1];

			if (isYear(firstToken)) {
				this.year = Integer.parseInt(firstToken);
			} else {
				GenericPair<Person, Boolean> personExtraction =
					extractPerson(this.personKeyGenerator, firstToken);
				this.author = personExtraction.getFirstObject();
				this.starred = personExtraction.getSecondObject();
			}

			GenericPair<Source, String> sourceExtraction =
				extractSource(this.sourceKeyGenerator, secondToken);
			this.source = sourceExtraction.getFirstObject();
			this.annotation = sourceExtraction.getSecondObject();
		} else if (cleanedTokens.length == 3) {
			String firstToken = cleanedTokens[0];
			String secondToken = cleanedTokens[1];
			String thirdToken = cleanedTokens[2];

			/*
			 * Actually, I've yet to find this case, but that's not saying it won't happen.
			 * Based on the 4 token case, I assume the order for this case is:
			 * year, journal, volume or page
			 */
			if (isYear(firstToken)) {
				
			} else {
			}
		} else if (cleanedTokens.length == 4) {
		} else if (cleanedTokens.length == 5) {
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

	public String getVolume() {
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

	private static boolean isYear(String token) {
		return token.matches(YEAR_PATTERN);
	}

	private static GenericPair<Person, Boolean> extractPerson(
			DatabaseTableKeyGenerator personKeyGenerator, String originalToken) {
		String[] nameTokens = originalToken.split("\\s");
		boolean starred = false;
		String familyName = nameTokens[0];

		if (familyName.startsWith("*")) {
			starred = true;
			familyName = familyName.replaceFirst("\\**+", "");
		}

		familyName = StringUtilities.toSentenceCase(nameTokens[0]);
		String firstInitial = "";
		String middleInitial = "";
		String unsplitName = "";
		String fullName = "";

		// If there is a first and/or middle initial supplied...
		if (nameTokens.length > 1) {
			unsplitName = nameTokens[1];
			int nameTokenLength = unsplitName.length();

			if ((nameTokenLength == 1) || (nameTokenLength == 2)) {
				firstInitial = Character.toString(Character.toUpperCase(unsplitName.charAt(0)));

				if (nameTokenLength == 2) {
					// If there is also a middle initial.
					middleInitial =
						Character.toString(Character.toUpperCase(unsplitName.charAt(1)));
				}

				fullName = familyName + ", " + firstInitial + " " + middleInitial;
			} else {
				fullName = unsplitName;
			}
		}

		String firstName = firstInitial;
		String additionalName = middleInitial;

		return new GenericPair<Person, Boolean>(
			new Person(
				personKeyGenerator,
				firstName,
				additionalName,
				familyName,
				firstInitial,
				middleInitial,
				unsplitName,
				fullName),
			starred);
	}

	private static GenericPair<Source, String> extractSource(
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

		return new GenericPair<Source, String>(
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
}