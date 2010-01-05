package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.FieldOfApplication;

public class FieldOfApplications extends RowItem<FieldOfApplications> {

	private FieldOfApplication fieldOfApplication;
	private Award award;

	public FieldOfApplications(FieldOfApplication fieldOfApplication, Award award) {
		super(createAttributes());
		this.fieldOfApplication = fieldOfApplication;
		this.award = award; 
	}

	public FieldOfApplication getFieldOfApplication() {
		return this.fieldOfApplication;
	}
	
	public Award getAward() {
		return this.award;
	}

	private static Dictionary<String, Comparable<?>> createAttributes() {
		return new Hashtable<String, Comparable<?>>();
	}

	@Override
	public void merge(FieldOfApplications otherItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldMerge(FieldOfApplications otherItem) {
		// TODO Auto-generated method stub
		return false;
	}
}