package edu.iu.scipolicy.database.isi.load.utilities.parser;

import org.cishell.utilities.StringUtilities;

import edu.iu.scipolicy.database.isi.load.utilities.parser.exception.PersonParsingException;

public class AbbreviatedNameParser {
	public String unsplitAbbreviatedName;
	public String familyName;
	public String firstInitial;
	public String middleInitials;

	public AbbreviatedNameParser(String rawAbbreviatedNameString) throws PersonParsingException {
		if (StringUtilities.isNull_Empty_OrWhitespace(rawAbbreviatedNameString)) {
			String exceptionMessage =
				"An abbreviated name must be supplied.  Cannot continue parsing this \"person\".";
			throw new PersonParsingException(exceptionMessage);
		}

		String abbreviatedSeparatorExpression;

		if (rawAbbreviatedNameString.contains(",")) {
			abbreviatedSeparatorExpression = ",\\s";
		} else {
			abbreviatedSeparatorExpression = "\\s";
		}

		String[] initialTokens = rawAbbreviatedNameString.split(abbreviatedSeparatorExpression);
		this.familyName = StringUtilities.toSentenceCase(initialTokens[0]);
		this.unsplitAbbreviatedName =
			rawAbbreviatedNameString.replaceFirst(abbreviatedSeparatorExpression, ", ");

		// If there is a first and/or middle initial supplied...
		if (initialTokens.length > 1) {
			String initials = initialTokens[1];
			int nameTokenLength = initials.length();
			boolean nameTokenLengthIsAtLeast1 = (nameTokenLength >= 1);
			boolean nameTokenLengthIsAtLeast2 = (nameTokenLength >= 2);

			if (nameTokenLengthIsAtLeast1 || nameTokenLengthIsAtLeast2) {
				this.firstInitial = Character.toString(Character.toUpperCase(initials.charAt(0)));

				if (nameTokenLengthIsAtLeast2) {
					// If there is also a middle initial...
					this.middleInitials = initials.substring(1).toUpperCase();
				}
			}
		}
	}
}