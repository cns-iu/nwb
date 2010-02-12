package edu.iu.scipolicy.database.isi.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

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

	private String additionalName;
	private String familyName;
	private String firstInitial;
	private String fullName;
	private String middleInitial;
	private String personalName;
	private String unsplitAbbreviatedName;

	public Person(
			DatabaseTableKeyGenerator keyGenerator,
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitAbbreviatedName) {
		super(
			keyGenerator,
			createAttributes(
				additionalName,
				familyName,
				firstInitial,
				fullName,
				middleInitial,
				personalName,
				unsplitAbbreviatedName));
		this.additionalName = additionalName;
		this.familyName = familyName;
		this.firstInitial = firstInitial;
		this.fullName = fullName;
		this.middleInitial = middleInitial;
		this.unsplitAbbreviatedName = unsplitAbbreviatedName;
		this.personalName = personalName;
	}

	public String getAdditionalName() {
		return this.additionalName;
	}

	public String getFamilyName() {
		return this.familyName;
	}

	public String getFirstInitial() {
		return this.firstInitial;
	}

	public String getFullName() {
		return this.fullName;
	}

	public String getMiddleInitial() {
		return this.middleInitial;
	}

	public String getPersonalName() {
		return this.personalName;
	}

	public String getUnsplitAbbreviatedName() {
		return this.unsplitAbbreviatedName;
	}

	/*@Override
	public boolean shouldMerge(Person otherPerson) {
		return false;
	}*/

	@Override
	public Object createMergeKey() {
		/*List<Object> mergeKey = new ArrayList<Object>();
		Integer primaryKey = getPrimaryKey();
		mergeKey.add(primaryKey);

		return mergeKey;*/
		return getPrimaryKey();
	}

	@Override
	public void merge(Person otherPerson) {
	}

	public static Dictionary<String, Object> createAttributes(
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitName) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.ADDITIONAL_NAME, additionalName),
			new DictionaryEntry<String, Object>(ISI.FAMILY_NAME, familyName),
			new DictionaryEntry<String, Object>(ISI.FIRST_INITIAL, firstInitial),
			new DictionaryEntry<String, Object>(ISI.FULL_NAME, fullName),
			new DictionaryEntry<String, Object>(ISI.MIDDLE_INITIAL, middleInitial),
			new DictionaryEntry<String, Object>(ISI.PERSONAL_NAME, personalName),
			new DictionaryEntry<String, Object>(ISI.UNSPLIT_ABBREVIATED_NAME, unsplitName));

		return attributes;
	}
}