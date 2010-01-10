package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Person;

public class Investigator extends Entity<Investigator> implements Comparable<Investigator>{
	
	public static final Schema<Investigator> SCHEMA = new Schema<Investigator>(
			true,
			NSFDatabase.INVESTIGATOR_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSFDatabase.INVESTIGATOR_PERSON_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSFDatabase.IS_MAIN_PI, DerbyFieldType.BOOLEAN,
			NSFDatabase.EMAIL_ADDRESS, DerbyFieldType.TEXT,
			NSFDatabase.STATE, DerbyFieldType.TEXT
			).
			FOREIGN_KEYS(
					NSFDatabase.INVESTIGATOR_AWARD_FOREIGN_KEY,
						NSFDatabase.AWARD_TABLE_NAME,
					NSFDatabase.INVESTIGATOR_PERSON_FOREIGN_KEY,
						NSFDatabase.PERSON_TABLE_NAME);
	
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
		super(keyGenerator, createAttributes(isMainPI, emailAddress, state));
		
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

	private static Dictionary<String, Comparable<?>> createAttributes(boolean isMainPI,
															   String emailAddress,
															   String state) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(NSFDatabase.EMAIL_ADDRESS, emailAddress);
		attributes.put(NSFDatabase.STATE, state);
		attributes.put(NSFDatabase.IS_MAIN_PI, isMainPI);

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