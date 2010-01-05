package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Person;

public class ProgramManager extends RowItem<ProgramManager> {

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
	public void merge(ProgramManager otherItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldMerge(ProgramManager otherItem) {
		// TODO Auto-generated method stub
		return false;
	}
}