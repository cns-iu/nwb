package edu.iu.sci2.database.nsf.load.model.entity.relationship;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.database.nsf.load.model.entity.Award;
import edu.iu.sci2.database.nsf.load.model.entity.Person;
import edu.iu.sci2.utilities.nsf.NsfDatabaseFieldNames;

public class Investigator extends Entity<Investigator> {
	
	public static final Schema<Investigator> SCHEMA = new Schema<Investigator>(
			true,
			NsfDatabaseFieldNames.INVESTIGATOR_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NsfDatabaseFieldNames.INVESTIGATOR_PERSON_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NsfDatabaseFieldNames.IS_MAIN_PI, DerbyFieldType.BOOLEAN,
			NsfDatabaseFieldNames.EMAIL_ADDRESS, DerbyFieldType.TEXT,
			NsfDatabaseFieldNames.STATE, DerbyFieldType.TEXT
			).
			FOREIGN_KEYS(
				NsfDatabaseFieldNames.INVESTIGATOR_AWARD_FOREIGN_KEY,
					NsfDatabaseFieldNames.AWARD_TABLE_NAME,
				NsfDatabaseFieldNames.INVESTIGATOR_PERSON_FOREIGN_KEY,
					NsfDatabaseFieldNames.PERSON_TABLE_NAME
			);
	
	private boolean isMainPI;
	private String emailAddress;
	private String state;
	private Award award;
	private Person person;
	
	public Investigator(
			DatabaseTableKeyGenerator keyGenerator,
			Award award, 
			Person person,
			String emailAddress,
			String state,
			boolean isMainPI) {
		super(keyGenerator, createAttributes(award, person, isMainPI, emailAddress, state));
		this.person = person;
		this.award = award;
		this.emailAddress = emailAddress;
		this.state = state;
		this.isMainPI = isMainPI;
	}

	public Person getPerson() {
		return this.person;
	}
	
	public Award getAward() {
		return this.award;
	}

	public boolean isMainPI() {
		return isMainPI;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getState() {
		return state;
	}

	/*@Override
	public boolean shouldMerge(Investigator otherItem) {
		return false;
	}*/

	@Override
	public Object createMergeKey() {
		List<Object> mergeKey = new ArrayList<Object>();
		mergeKey.add(this.award.getPrimaryKey());
		mergeKey.add(this.person.getPrimaryKey());
		Integer primaryKey = getPrimaryKey();
		addStringOrAlternativeToMergeKey(mergeKey, this.emailAddress, primaryKey);
		addStringOrAlternativeToMergeKey(mergeKey, this.state, primaryKey);
		addStringOrAlternativeToMergeKey(mergeKey, Boolean.toString(this.isMainPI), primaryKey);

		return mergeKey;
	}

	@Override
	public void merge(Investigator otherItem) {
	}

	private static Dictionary<String, Object> createAttributes(
			Award award, 
			Person person, 
			boolean isMainPI,
			String emailAddress,
			String state) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(
			NsfDatabaseFieldNames.INVESTIGATOR_AWARD_FOREIGN_KEY, award.getPrimaryKey());
		attributes.put(
			NsfDatabaseFieldNames.INVESTIGATOR_PERSON_FOREIGN_KEY, person.getPrimaryKey());
		attributes.put(
			NsfDatabaseFieldNames.EMAIL_ADDRESS, emailAddress);
		attributes.put(
			NsfDatabaseFieldNames.STATE, state);
		attributes.put(
			NsfDatabaseFieldNames.IS_MAIN_PI, isMainPI);

		return attributes;
	}
}