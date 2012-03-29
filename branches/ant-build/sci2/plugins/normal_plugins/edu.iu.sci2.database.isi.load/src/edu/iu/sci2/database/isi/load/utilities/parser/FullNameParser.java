package edu.iu.sci2.database.isi.load.utilities.parser;

import org.cishell.utilities.StringUtilities;

import edu.iu.sci2.database.isi.load.utilities.parser.exception.PersonParsingException;

public class FullNameParser {
	public String personalName;
	public String additionalNamesString;
	public String fullName;

	public FullNameParser(String rawFullNameString) throws PersonParsingException {
		if (!StringUtilities.isNull_Empty_OrWhitespace(rawFullNameString)) {
			StringBuilder additionalNamesBuilder = new StringBuilder();
			StringBuilder fullNameBuilder = new StringBuilder();

			String fullNameWithoutComma = rawFullNameString.replaceFirst(",", "");
			String[] fullNameTokens = fullNameWithoutComma.split("\\s");
			String fullNameFamilyName = StringUtilities.simpleClean(fullNameTokens[0]);
			fullNameBuilder.append(fullNameFamilyName);

			if (fullNameTokens.length >= 2) {
				this.personalName = StringUtilities.simpleClean(fullNameTokens[1]);
				fullNameBuilder.append(", " + this.personalName);

				if (fullNameTokens.length >= 3) {
					for (int ii = 2; ii < fullNameTokens.length; ii++) {
						additionalNamesBuilder.append(fullNameTokens[ii]);

						if ((ii + 1) < fullNameTokens.length) {
							additionalNamesBuilder.append(" ");
						}
					}

					fullNameBuilder.append(" " + additionalNamesBuilder);
				}
			}

			this.additionalNamesString = additionalNamesBuilder.toString();
			this.fullName = fullNameBuilder.toString();
		}
	}
}