package edu.iu.scipolicy.database.nsf.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.database.nsf.load.model.entity.Award;
import edu.iu.scipolicy.database.nsf.load.model.entity.Person;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class Investigator extends Entity<Investigator> implements Comparable<Investigator>{
	
	public static final Schema<Investigator> SCHEMA = new Schema<Investigator>(
			true,
			NSF_Database_FieldNames.INVESTIGATOR_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSF_Database_FieldNames.INVESTIGATOR_PERSON_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSF_Database_FieldNames.IS_MAIN_PI, DerbyFieldType.BOOLEAN,
			NSF_Database_FieldNames.EMAIL_ADDRESS, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.STATE, DerbyFieldType.TEXT
			).
			FOREIGN_KEYS(
					NSF_Database_FieldNames.INVESTIGATOR_AWARD_FOREIGN_KEY,
						NSF_Database_FieldNames.AWARD_TABLE_NAME,
					NSF_Database_FieldNames.INVESTIGATOR_PERSON_FOREIGN_KEY,
						NSF_Database_FieldNames.PERSON_TABLE_NAME);
	
	private boolean isMainPI;
	private String emailAddress;
	private String state;
	private Award award;
	private Person person;
	
	public Investigator(DatabaseTableKeyGenerator keyGenerator,
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

	private static Dictionary<String, Comparable<?>> createAttributes(
			Award award, 
			Person person, 
			boolean isMainPI,
			String emailAddress,
			String state) {
		
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(NSF_Database_FieldNames.INVESTIGATOR_AWARD_FOREIGN_KEY, award.getPrimaryKey());
		attributes.put(NSF_Database_FieldNames.INVESTIGATOR_PERSON_FOREIGN_KEY, person.getPrimaryKey());
		attributes.put(NSF_Database_FieldNames.EMAIL_ADDRESS, emailAddress);
		attributes.put(NSF_Database_FieldNames.STATE, state);
		attributes.put(NSF_Database_FieldNames.IS_MAIN_PI, isMainPI);

		return attributes;
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

	@Override
	public void merge(Investigator otherItem) {	}

	@Override
	public boolean shouldMerge(Investigator otherItem) {
		return false;
	}

	public int compareTo(Investigator o) {
		// TODO Auto-generated method stub
		return 0;
	}
}