package edu.iu.scipolicy.database.isi.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class Person extends Entity<Person> implements Comparable<Person> {
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

	public int compareTo(Person otherPerson) {
		return -1;
	}

	public boolean shouldMerge(Person otherPerson) {
		return false;
	}

	public void merge(Person otherPerson) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			String additionalName,
			String familyName,
			String firstInitial,
			String fullName,
			String middleInitial,
			String personalName,
			String unsplitName) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISI.ADDITIONAL_NAME, additionalName);
		attributes.put(ISI.FAMILY_NAME, familyName);
		attributes.put(ISI.FIRST_INITIAL, firstInitial);
		attributes.put(ISI.FULL_NAME, fullName);
		attributes.put(ISI.MIDDLE_INITIAL, middleInitial);
		attributes.put(ISI.PERSONAL_NAME, personalName);
		attributes.put(ISI.UNSPLIT_ABBREVIATED_NAME, unsplitName);

		return attributes;
	}
}