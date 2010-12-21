package edu.iu.scipolicy.database.nsf.load.model.entity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class Person extends Entity<Person> {
	
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

	public Person(DatabaseTableKeyGenerator keyGenerator, String originalInputName) {
		this(keyGenerator, "", "", "", "", originalInputName);
	}
					
	public Person(
			DatabaseTableKeyGenerator keyGenerator,
			String lastName,
			String firstName,
			String middleInitial,
			String formattedFullName,
			String originalInputName) {
		super(
			keyGenerator,
			createAttributes(
				lastName, firstName, middleInitial, formattedFullName, originalInputName));
		this.lastName = lastName;
		this.firstName = firstName;
		this.middleInitial = middleInitial;
		this.formattedFullName = formattedFullName;
		this.originalInputName = originalInputName;
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

	/*@Override
	public boolean shouldMerge(Person otherItem) {
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
	public void merge(Person otherItem) {
	}

	private static Dictionary<String, Object> createAttributes(
			String lastName,
			String firstName,
			String middleInitial,
			String formattedFullName,
			String originalInputName) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(NSF_Database_FieldNames.LAST_NAME, lastName);
		attributes.put(NSF_Database_FieldNames.FIRST_NAME, firstName);
		attributes.put(NSF_Database_FieldNames.MIDDLE_INITIAL, middleInitial);
		attributes.put(NSF_Database_FieldNames.ORIGINAL_INPUT_NAME, originalInputName);
		attributes.put(NSF_Database_FieldNames.FORMATTED_FULL_NAME, formattedFullName);

		return attributes;
	}
}