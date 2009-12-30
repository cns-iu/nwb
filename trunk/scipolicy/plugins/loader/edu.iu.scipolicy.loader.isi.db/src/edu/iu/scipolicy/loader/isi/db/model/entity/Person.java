package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Person extends Entity<Person> implements Comparable<Person> {
	public static final Schema<Person> SCHEMA = new Schema<Person>(
		ISIDatabase.PERSONAL_NAME, Schema.TEXT_CLASS,
		ISIDatabase.ADDITIONAL_NAME, Schema.TEXT_CLASS,
		ISIDatabase.FAMILY_NAME, Schema.TEXT_CLASS,
		ISIDatabase.FIRST_INITIAL, Schema.TEXT_CLASS,
		ISIDatabase.MIDDLE_INITIAL, Schema.TEXT_CLASS,
		ISIDatabase.UNSPLIT_ABBREVIATED_NAME, Schema.TEXT_CLASS,
		ISIDatabase.FULL_NAME, Schema.TEXT_CLASS);

	private String personalName;
	private String additionalName;
	private String familyName;
	private String firstInitial;
	private String middleInitial;
	private String unsplitAbbreviatedName;
	private String fullName;

	public Person(
			DatabaseTableKeyGenerator keyGenerator,
			String personalName,
			String additionalName,
			String familyName,
			String firstInitial,
			String middleInitial,
			String unsplitAbbreviatedName,
			String fullName) {
		super(
			keyGenerator,
			createAttributes(
				personalName,
				additionalName,
				familyName,
				firstInitial,
				middleInitial,
				unsplitAbbreviatedName,
				fullName));
		this.personalName = personalName;
		this.additionalName = additionalName;
		this.familyName = familyName;
		this.firstInitial = firstInitial;
		this.middleInitial = middleInitial;
		this.unsplitAbbreviatedName = unsplitAbbreviatedName;
		this.fullName = fullName;
	}

	public String getPersonalName() {
		return this.personalName;
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

	public String getMiddleInitial() {
		return this.middleInitial;
	}

	public String getUnsplitAbbreviatedName() {
		return this.unsplitAbbreviatedName;
	}

	public String getFullName() {
		return this.fullName;
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
			String personalName,
			String additionalName,
			String familyName,
			String firstInitial,
			String middleInitial,
			String unsplitName,
			String fullName) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.PERSONAL_NAME, personalName);
		attributes.put(ISIDatabase.ADDITIONAL_NAME, additionalName);
		attributes.put(ISIDatabase.FAMILY_NAME, familyName);
		attributes.put(ISIDatabase.FIRST_INITIAL, firstInitial);
		attributes.put(ISIDatabase.MIDDLE_INITIAL, middleInitial);
		attributes.put(ISIDatabase.UNSPLIT_ABBREVIATED_NAME, unsplitName);
		attributes.put(ISIDatabase.FULL_NAME, fullName);

		return attributes;
	}
}