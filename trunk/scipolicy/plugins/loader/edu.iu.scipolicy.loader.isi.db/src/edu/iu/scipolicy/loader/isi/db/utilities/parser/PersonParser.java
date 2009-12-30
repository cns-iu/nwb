package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.cns.shared.utilities.Pair;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;

public class PersonParser {
	public static Pair<Person, Boolean> parsePerson(
			DatabaseTableKeyGenerator personKeyGenerator,
			String rawAbbreviatedNameString,
			String rawFullNameString) throws PersonParsingException {
		if (StringUtilities.isEmptyOrWhiteSpace(rawAbbreviatedNameString)) {
			String exceptionMessage =
				"An abbreviated name must be supplied.  Cannot continue parsing this \"person\".";
			throw new PersonParsingException(exceptionMessage);
		}

		// TODO: Parse rawFullNameString as well!
		String separatorExpression;

		if (rawAbbreviatedNameString.contains(",")) {
			separatorExpression = ",\\s";
		} else {
			separatorExpression = "\\s";
		}

		boolean starred = false;

		if (rawAbbreviatedNameString.startsWith("*")) {
			starred = true;
			rawAbbreviatedNameString = rawAbbreviatedNameString.replaceFirst("\\**+", "");
		}

		String[] initialTokens = rawAbbreviatedNameString.split(separatorExpression);
		String familyName = StringUtilities.toSentenceCase(initialTokens[0]);

		/*if (familyName.startsWith("*")) {
			System.err.println("Starred");
			starred = true;
			System.err.println("Before: " + familyName);
			familyName = familyName.replaceFirst("\\**+", "");
			System.err.println("After: " + familyName);
		}*/

		String firstInitial = "";
		String middleInitial = "";
		String unsplitName = rawAbbreviatedNameString.replaceFirst(separatorExpression, ", ");
		String fullName = "";

		// If there is a first and/or middle initial supplied...
		if (initialTokens.length > 1) {
			String initials = initialTokens[1];
			int nameTokenLength = initials.length();
			boolean nameTokenLengthIs1 = (nameTokenLength == 1);
			boolean nameTokenLengthIs2 = (nameTokenLength == 2);

			if (nameTokenLengthIs1 || nameTokenLengthIs2) {
				firstInitial = Character.toString(Character.toUpperCase(initials.charAt(0)));
				fullName = familyName + ", " + firstInitial;

				if (nameTokenLengthIs2) {
					// If there is also a middle initial...
					middleInitial =
						Character.toString(Character.toUpperCase(initials.charAt(1)));
					fullName += " " + middleInitial;
				}

			} else {
				// fullName = unsplitName;
			}
		}

		String firstName = firstInitial;
		String additionalName = middleInitial;

		return new Pair<Person, Boolean>(
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
}