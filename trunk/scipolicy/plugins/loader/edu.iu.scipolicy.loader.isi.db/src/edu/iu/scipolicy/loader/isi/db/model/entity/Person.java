package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Person extends Entity<Person> implements Comparable<Person> {
	public static final Schema<Person> SCHEMA = new Schema<Person>(
		true,
		ISIDatabase.ADDITIONAL_NAME, Schema.TEXT_CLASS,
		ISIDatabase.FAMILY_NAME, Schema.TEXT_CLASS,
		ISIDatabase.FIRST_INITIAL, Schema.TEXT_CLASS,
		ISIDatabase.FULL_NAME, Schema.TEXT_CLASS,
		ISIDatabase.MIDDLE_INITIAL, Schema.TEXT_CLASS,
		ISIDatabase.PERSONAL_NAME, Schema.TEXT_CLASS,
		ISIDatabase.UNSPLIT_ABBREVIATED_NAME, Schema.TEXT_CLASS);

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
		attributes.put(ISIDatabase.ADDITIONAL_NAME, additionalName);
		attributes.put(ISIDatabase.FAMILY_NAME, familyName);
		attributes.put(ISIDatabase.FIRST_INITIAL, firstInitial);
		attributes.put(ISIDatabase.FULL_NAME, fullName);
		attributes.put(ISIDatabase.MIDDLE_INITIAL, middleInitial);
		attributes.put(ISIDatabase.PERSONAL_NAME, personalName);
		attributes.put(ISIDatabase.UNSPLIT_ABBREVIATED_NAME, unsplitName);

		return attributes;
	}
}