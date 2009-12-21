package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Person extends Entity<Person> implements Comparable<Person> {
	private String personalName;
	private String additionalName;
	private String familyName;
	private String firstInitial;
	private String middleInitial;
	private String unsplitName;
	private String fullName;

	public Person(
			DatabaseTableKeyGenerator keyGenerator,
			String personalName,
			String additionalName,
			String familyName,
			String firstInitial,
			String middleInitial,
			String unsplitName,
			String fullName) {
		super(
			keyGenerator,
			createAttributes(
				personalName,
				additionalName,
				familyName,
				firstInitial,
				middleInitial,
				unsplitName,
				fullName));
		this.personalName = personalName;
		this.additionalName = additionalName;
		this.familyName = familyName;
		this.firstInitial = firstInitial;
		this.middleInitial = middleInitial;
		this.unsplitName = unsplitName;
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

	public String getUnsplitName() {
		return this.unsplitName;
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
		attributes.put(ISIDatabase.UNSPLIT_NAME, unsplitName);
		attributes.put(ISIDatabase.FULL_NAME, fullName);

		return attributes;
	}
}