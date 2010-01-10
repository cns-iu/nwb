package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Person;

public class ProgramManager extends RowItem<ProgramManager> {

	public static final Schema<ProgramManager> SCHEMA = new Schema<ProgramManager>(
			false,
			NSFDatabase.PROGRAM_MANAGER_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSFDatabase.PROGRAM_MANAGER_PERSON_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
					NSFDatabase.PROGRAM_MANAGER_AWARD_FOREIGN_KEY,
						NSFDatabase.AWARD_TABLE_NAME,
					NSFDatabase.PROGRAM_MANAGER_PERSON_FOREIGN_KEY,
						NSFDatabase.PERSON_TABLE_NAME);
	
	private Person person;
	private Award award;

	public ProgramManager(Person person, Award award) {
		super(createAttributes());
		this.person = person;
		this.award = award;
	}

	public Person getPerson() {
		return this.person;
	}
	
	public Award getAward() {
		return this.award;
	}

	private static Dictionary<String, Comparable<?>> createAttributes() {
		return new Hashtable<String, Comparable<?>>();
	}

	@Override
	public void merge(ProgramManager otherItem) { }

	@Override
	public boolean shouldMerge(ProgramManager otherItem) {
		return false;
	}
}