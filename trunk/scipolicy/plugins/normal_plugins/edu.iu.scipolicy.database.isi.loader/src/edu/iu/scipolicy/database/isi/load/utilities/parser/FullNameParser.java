package edu.iu.scipolicy.database.isi.load.utilities.parser;

import org.cishell.utilities.StringUtilities;

import edu.iu.scipolicy.database.isi.load.utilities.parser.exception.PersonParsingException;

public class FullNameParser {
	public String personalName = "";
	public String additionalNamesString = "";
	public String fullName = "";

	public FullNameParser(String rawFullNameString) throws PersonParsingException {
		if (!StringUtilities.isEmptyOrWhiteSpace(rawFullNameString)) {
			String fullNameWithoutComma = rawFullNameString.replaceFirst(",", "");
			String[] fullNameTokens = fullNameWithoutComma.split("\\s");
			String fullNameFamilyName = StringUtilities.simpleClean(fullNameTokens[0]);
			this.fullName = fullNameFamilyName;

			if (fullNameTokens.length >= 2) {
				this.personalName = StringUtilities.simpleClean(fullNameTokens[1]);
				this.fullName += ", " + this.personalName;

				if (fullNameTokens.length >= 3) {
					for (int ii = 2; ii < fullNameTokens.length; ii++) {
						this.additionalNamesString += fullNameTokens[ii];

						if ((ii + 1) < fullNameTokens.length) {
							this.additionalNamesString += " ";
						}
					}

					this.fullName += " " + this.additionalNamesString;
				}
			}
		}
	}
}