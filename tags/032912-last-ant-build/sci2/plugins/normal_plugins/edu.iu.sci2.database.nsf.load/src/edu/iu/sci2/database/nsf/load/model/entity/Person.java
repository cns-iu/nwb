package edu.iu.sci2.database.nsf.load.model.entity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.utilities.nsf.NsfDatabaseFieldNames;

public class Person extends Entity<Person> {
	
	public static final Schema<Person> SCHEMA = new Schema<Person>(
			true,
			NsfDatabaseFieldNames.LAST_NAME, DerbyFieldType.TEXT,
			NsfDatabaseFieldNames.FIRST_NAME, DerbyFieldType.TEXT,
			NsfDatabaseFieldNames.MIDDLE_INITIAL, DerbyFieldType.TEXT,
			NsfDatabaseFieldNames.ORIGINAL_INPUT_NAME, DerbyFieldType.TEXT,
			NsfDatabaseFieldNames.FORMATTED_FULL_NAME, DerbyFieldType.TEXT
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
		attributes.put(NsfDatabaseFieldNames.LAST_NAME, lastName);
		attributes.put(NsfDatabaseFieldNames.FIRST_NAME, firstName);
		attributes.put(NsfDatabaseFieldNames.MIDDLE_INITIAL, middleInitial);
		attributes.put(NsfDatabaseFieldNames.ORIGINAL_INPUT_NAME, originalInputName);
		attributes.put(NsfDatabaseFieldNames.FORMATTED_FULL_NAME, formattedFullName);

		return attributes;
	}
}