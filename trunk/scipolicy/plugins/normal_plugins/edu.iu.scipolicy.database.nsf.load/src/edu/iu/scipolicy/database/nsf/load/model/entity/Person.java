package edu.iu.scipolicy.database.nsf.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class Person extends Entity<Person> implements Comparable<Person>{
	
	public static final Schema<Person> SCHEMA = new Schema<Person>(
			true,
			NSF_Database_FieldNames.LAST_NAME, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.FIRST_NAME, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.MIDDLE_INITIAL, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.ORIGINAL_INPUT_NAME, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.FORMATTED_FULL_NAME, DerbyFieldType.TEXT
			);
	
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
		attributes.put(NSF_Database_FieldNames.LAST_NAME, lastName);
		attributes.put(NSF_Database_FieldNames.FIRST_NAME, firstName);
		attributes.put(NSF_Database_FieldNames.MIDDLE_INITIAL, middleInitial);
		attributes.put(NSF_Database_FieldNames.ORIGINAL_INPUT_NAME, originalInputName);
		attributes.put(NSF_Database_FieldNames.FORMATTED_FULL_NAME, formattedFullName);

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

	public int compareTo(Person o) {
		// TODO Auto-generated method stub
		return 0;
	}
}