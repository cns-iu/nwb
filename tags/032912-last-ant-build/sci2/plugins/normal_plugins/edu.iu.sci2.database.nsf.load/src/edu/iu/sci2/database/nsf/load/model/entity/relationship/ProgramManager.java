package edu.iu.sci2.database.nsf.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.sci2.database.nsf.load.model.entity.Award;
import edu.iu.sci2.database.nsf.load.model.entity.Person;
import edu.iu.sci2.utilities.nsf.NsfDatabaseFieldNames;

public class ProgramManager extends RowItem<ProgramManager> {

	public static final Schema<ProgramManager> SCHEMA = new Schema<ProgramManager>(
			false,
			NsfDatabaseFieldNames.PROGRAM_MANAGER_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NsfDatabaseFieldNames.PROGRAM_MANAGER_PERSON_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
				NsfDatabaseFieldNames.PROGRAM_MANAGER_AWARD_FOREIGN_KEY,
					NsfDatabaseFieldNames.AWARD_TABLE_NAME,
				NsfDatabaseFieldNames.PROGRAM_MANAGER_PERSON_FOREIGN_KEY,
					NsfDatabaseFieldNames.PERSON_TABLE_NAME
			);
	
	private Person person;
	private Award award;

	public ProgramManager(Person person, Award award) {
		super(createAttributes(person, award));
		this.person = person;
		this.award = award;
	}

	public Person getPerson() {
		return this.person;
	}
	
	public Award getAward() {
		return this.award;
	}

	/*@Override
	public Object createMergeKey() {
		List<Object> mergeKey = new ArrayList<Object>();
		mergeKey.add(this.person.getPrimaryKey());
		mergeKey.add(this.award.getPrimaryKey());

		return mergeKey;
	}

	@Override
	public void merge(ProgramManager otherItem) {
	}*/

	private static Dictionary<String, Object> createAttributes(Person person, Award award) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(
			NsfDatabaseFieldNames.PROGRAM_MANAGER_AWARD_FOREIGN_KEY, award.getPrimaryKey());
		attributes.put(
			NsfDatabaseFieldNames.PROGRAM_MANAGER_PERSON_FOREIGN_KEY, person.getPrimaryKey());

		return attributes;
	}
}