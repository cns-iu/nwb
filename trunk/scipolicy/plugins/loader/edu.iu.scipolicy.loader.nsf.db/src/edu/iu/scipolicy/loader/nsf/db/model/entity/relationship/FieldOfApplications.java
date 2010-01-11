package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.FieldOfApplication;

public class FieldOfApplications extends RowItem<FieldOfApplications> {

	public static final Schema<FieldOfApplications> SCHEMA = new Schema<FieldOfApplications>(
			false,
			NSFDatabase.FIELD_OF_APPLICATIONS_FIELD_OF_APPLICATION_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSFDatabase.FIELD_OF_APPLICATIONS_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
					NSFDatabase.FIELD_OF_APPLICATIONS_FIELD_OF_APPLICATION_FOREIGN_KEY,
						NSFDatabase.FIELD_OF_APPLICATION_TABLE_NAME,
					NSFDatabase.FIELD_OF_APPLICATIONS_AWARD_FOREIGN_KEY,
						NSFDatabase.AWARD_TABLE_NAME
					);
	
	private FieldOfApplication fieldOfApplication;
	private Award award;

	public FieldOfApplications(FieldOfApplication fieldOfApplication, Award award) {
		super(createAttributes(fieldOfApplication, award));
		this.fieldOfApplication = fieldOfApplication;
		this.award = award; 
	}

	public FieldOfApplication getFieldOfApplication() {
		return this.fieldOfApplication;
	}
	
	public Award getAward() {
		return this.award;
	}

	private static Dictionary<String, Comparable<?>> createAttributes(
		FieldOfApplication fieldOfApplication, 
		Award award) {
		
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(NSFDatabase.FIELD_OF_APPLICATIONS_FIELD_OF_APPLICATION_FOREIGN_KEY, 
					   fieldOfApplication.getPrimaryKey());
		
		attributes.put(NSFDatabase.FIELD_OF_APPLICATIONS_AWARD_FOREIGN_KEY, 
				   	   award.getPrimaryKey());
		return attributes;
	}

	@Override
	public void merge(FieldOfApplications otherItem) { }

	@Override
	public boolean shouldMerge(FieldOfApplications otherItem) {
		return false;
	}
}