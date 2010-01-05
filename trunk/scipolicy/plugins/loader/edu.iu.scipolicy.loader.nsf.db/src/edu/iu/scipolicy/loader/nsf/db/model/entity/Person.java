package edu.iu.scipolicy.loader.nsf.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;

public class Person extends Entity<Person> {
	private String lastName;
	private String firstName;
	private String middleInitial;
	private String originalInputName;
	private String formattedFullName;

	public Person(DatabaseTableKeyGenerator keyGenerator,
				  String originalInputName) {
		this(keyGenerator,
			 "",
			 "",
			 "",
			 "",
			 originalInputName);
	}
					
	public Person(
			DatabaseTableKeyGenerator keyGenerator,
			String lastName,
			String firstName,
			String middleInitial,
			String formattedFullName,
			String originalInputName) {
		super(keyGenerator, createAttributes(lastName, 
											 firstName, 
											 middleInitial, 
											 formattedFullName, 
											 originalInputName));
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.formattedFullName = formattedFullName;
		this.originalInputName = originalInputName;
	}

	private static Dictionary<String, Comparable<?>> createAttributes(String lastName,
													   String firstName,
													   String middleInitial,
													   String formattedFullName,
													   String originalInputName) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(NSFDatabase.LAST_NAME, lastName);
		attributes.put(NSFDatabase.FIRST_NAME, firstName);
		attributes.put(NSFDatabase.MIDDLE_INITIAL, middleInitial);
		attributes.put(NSFDatabase.ORIGINAL_INPUT_NAME, originalInputName);
		attributes.put(NSFDatabase.FORMATTED_FULL_NAME, formattedFullName);

		return attributes;
	}

	public final boolean equals(Person otherEntity) {
		return false;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getMiddleInitial() {
		return middleInitial;
	}

	public String getOriginalInputName() {
		return originalInputName;
	}

	public String getFormattedFullName() {
		return formattedFullName;
	}

	@Override
	public void merge(Person otherItem) { }

	@Override
	public boolean shouldMerge(Person otherItem) {
		return false;
	}
}