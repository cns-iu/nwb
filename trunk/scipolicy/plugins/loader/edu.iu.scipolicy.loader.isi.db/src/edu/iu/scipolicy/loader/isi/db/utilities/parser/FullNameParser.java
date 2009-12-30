package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import org.cishell.utilities.StringUtilities;

public class FullNameParser {
	public String firstName = "";
	public String additionalNamesString = "";
	public String fullName = "";

	public FullNameParser(String rawFullNameString) throws PersonParsingException {
		if (!StringUtilities.isEmptyOrWhiteSpace(rawFullNameString)) {
			String fullNameWithoutComma = rawFullNameString.replaceFirst(",", "");
			String[] fullNameTokens = fullNameWithoutComma.split("\\s");
			String fullNameFamilyName = StringUtilities.simpleClean(fullNameTokens[0]);
			this.fullName = fullNameFamilyName;

			if (fullNameTokens.length >= 2) {
				this.firstName = StringUtilities.simpleClean(fullNameTokens[1]);
				this.fullName += ", " + this.firstName;

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