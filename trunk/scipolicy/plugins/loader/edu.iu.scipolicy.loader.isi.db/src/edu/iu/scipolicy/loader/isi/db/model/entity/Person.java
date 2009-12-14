package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;


import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;

public class Person extends Entity {
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
		super(keyGenerator);
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

	public String getfirstInitial() {
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

	public Dictionary<String, Object> createAttributes() {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISIDatabase.PERSONAL_NAME, this.personalName);
		attributes.put(ISIDatabase.ADDITIONAL_NAME, this.additionalName);
		attributes.put(ISIDatabase.FAMILY_NAME, this.familyName);
		attributes.put(ISIDatabase.FIRST_INITIAL, this.firstInitial);
		attributes.put(ISIDatabase.MIDDLE_INITIAL, this.middleInitial);
		attributes.put(ISIDatabase.UNSPLIT_NAME, this.unsplitName);
		attributes.put(ISIDatabase.FULL_NAME, this.fullName);

		return attributes;
	}

	public final boolean equals(Person otherEntity) {
		return false;
	}
}