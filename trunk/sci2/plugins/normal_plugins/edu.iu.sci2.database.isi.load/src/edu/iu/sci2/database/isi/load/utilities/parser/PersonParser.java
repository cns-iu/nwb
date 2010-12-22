package edu.iu.sci2.database.isi.load.utilities.parser;

import edu.iu.sci2.database.isi.load.utilities.parser.exception.PersonParsingException;

public class PersonParser {
	private AbbreviatedNameParser parsedAbbreviatedName;
	private FullNameParser parsedFullName;

	public PersonParser(String rawAbbreviatedNameString, String rawFullNameString)
			throws PersonParsingException {
		this.parsedAbbreviatedName =
			new AbbreviatedNameParser(rawAbbreviatedNameString);
		this.parsedFullName = new FullNameParser(rawFullNameString);
	}

	public String getUnsplitAbbreviatedName() {
		return this.parsedAbbreviatedName.unsplitAbbreviatedName;
	}

	public String getFullName() {
		return this.parsedFullName.fullName;
	}

	public String getAdditionalName() {
		return this.parsedFullName.additionalNamesString;
	}

	public String getFamilyName() {
		return this.parsedAbbreviatedName.familyName;
	}

	public String getFirstInitial() {
		return this.parsedAbbreviatedName.firstInitial;
	}

	public String getMiddleInitial() {
		return this.parsedAbbreviatedName.middleInitials;
	}

	public String getPersonalName() {
		return this.parsedFullName.personalName;
	}

	/*public static Pair<Person, Boolean> parsePerson(
			DatabaseTableKeyGenerator personKeyGenerator,
			String rawAbbreviatedNameString,
			String rawFullNameString) throws PersonParsingException {
		AbbreviatedNameParser parsedAbbreviatedName =
			new AbbreviatedNameParser(rawAbbreviatedNameString);
		FullNameParser parsedFullName = new FullNameParser(rawFullNameString);
		Pair<Person, Boolean> pair = new Pair<Person, Boolean>(
			new Person(
				personKeyGenerator,
				parsedFullName.additionalNamesString,
				parsedAbbreviatedName.familyName,
				parsedAbbreviatedName.firstInitial,
				parsedFullName.fullName,
				parsedAbbreviatedName.middleInitials,
				parsedFullName.personalName,
				parsedAbbreviatedName.unsplitAbbreviatedName),
			parsedAbbreviatedName.starred);

		return pair;
	}*/
}