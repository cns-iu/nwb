package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.cns.shared.utilities.Pair;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;

public class PersonParser {
	public static Pair<Person, Boolean> parsePerson(
			DatabaseTableKeyGenerator personKeyGenerator,
			String rawAbbreviatedNameString,
			String rawFullNameString) throws PersonParsingException {
		AbbreviatedNameParser parsedAbbreviatedName =
			new AbbreviatedNameParser(rawAbbreviatedNameString);
		FullNameParser parsedFullName = new FullNameParser(rawFullNameString);

		return new Pair<Person, Boolean>(
			new Person(
				personKeyGenerator,
				parsedFullName.firstName,
				parsedFullName.additionalNamesString,
				parsedAbbreviatedName.familyName,
				parsedAbbreviatedName.firstInitial,
				parsedAbbreviatedName.middleInitials,
				parsedAbbreviatedName.unsplitAbbreviatedName,
				parsedFullName.fullName),
			parsedAbbreviatedName.starred);
	}
}