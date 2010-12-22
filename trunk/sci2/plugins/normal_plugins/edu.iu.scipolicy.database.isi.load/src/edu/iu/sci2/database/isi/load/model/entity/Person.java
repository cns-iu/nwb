package edu.iu.sci2.database.isi.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.utilities.parser.PersonParser;
import edu.iu.sci2.database.isi.load.utilities.parser.exception.PersonParsingException;

public class Person extends Entity<Person> {
	public static final Schema<Person> SCHEMA = new Schema<Person>(
		true,
		ISI.ADDITIONAL_NAME, DerbyFieldType.TEXT,
		ISI.FAMILY_NAME, DerbyFieldType.TEXT,
		ISI.FIRST_INITIAL, DerbyFieldType.TEXT,
		ISI.FULL_NAME, DerbyFieldType.TEXT,
		ISI.MIDDLE_INITIAL, DerbyFieldType.TEXT,
		ISI.PERSONAL_NAME, DerbyFieldType.TEXT,
		ISI.UNSPLIT_ABBREVIATED_NAME, DerbyFieldType.TEXT);

	private String fullName;
	private String unsplitAbbreviatedName;

	public Person(
			DatabaseTableKeyGenerator keyGenerator,
			String unsplitAbbreviatedName,
			String fullName) {
		super(
			keyGenerator,
			new Hashtable<String, Object>());
		this.unsplitAbbreviatedName = unsplitAbbreviatedName;
		this.fullName = fullName;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getUnsplitAbbreviatedName() {
		return this.unsplitAbbreviatedName;
	}

	@Override
	public Dictionary<String, Object> getAttributesForInsertion() {
		Dictionary<String, Object> attributes = DictionaryUtilities.copy(super.getAttributes());

		try {
			PersonParser personParsingResult =
				new PersonParser(this.unsplitAbbreviatedName, this.fullName);
			fillAttributes(
				attributes,
				personParsingResult.getUnsplitAbbreviatedName(),
				personParsingResult.getFullName(),
				personParsingResult.getAdditionalName(),
				personParsingResult.getFamilyName(),
				personParsingResult.getFirstInitial(),
				personParsingResult.getMiddleInitial(),
				personParsingResult.getPersonalName());
		} catch (PersonParsingException e) {}

		return attributes;
	}

	@Override
	public Object createMergeKey() {
		return getPrimaryKey();
	}

	@Override
	public void merge(Person otherPerson) {
	}

	private static void fillAttributes(
			Dictionary<String, Object> attributes,
			String unsplitAbbreviatedName,
			String fullName,
			String additionalName,
			String familyName,
			String firstInitial,
			String middleInitial,
			String personalName) {
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(
				ISI.UNSPLIT_ABBREVIATED_NAME, unsplitAbbreviatedName),
			new DictionaryEntry<String, Object>(ISI.FULL_NAME, fullName),
			new DictionaryEntry<String, Object>(ISI.ADDITIONAL_NAME, additionalName),
			new DictionaryEntry<String, Object>(ISI.FAMILY_NAME, familyName),
			new DictionaryEntry<String, Object>(ISI.FIRST_INITIAL, firstInitial),
			new DictionaryEntry<String, Object>(ISI.MIDDLE_INITIAL, middleInitial),
			new DictionaryEntry<String, Object>(ISI.PERSONAL_NAME, personalName));
	}
}