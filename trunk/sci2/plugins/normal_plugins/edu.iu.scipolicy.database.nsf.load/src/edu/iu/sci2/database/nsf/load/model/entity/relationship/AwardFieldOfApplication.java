package edu.iu.sci2.database.nsf.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.sci2.database.nsf.load.model.entity.Award;
import edu.iu.sci2.database.nsf.load.model.entity.FieldOfApplication;
import edu.iu.sci2.utilities.nsf.NSF_Database_FieldNames;

public class AwardFieldOfApplication extends RowItem<AwardFieldOfApplication> {
	public static final Schema<AwardFieldOfApplication> SCHEMA =
		new Schema<AwardFieldOfApplication>(
			false,
			NSF_Database_FieldNames.FIELD_OF_APPLICATIONS_FIELD_OF_APPLICATION_FOREIGN_KEY,
				DerbyFieldType.FOREIGN_KEY,
			NSF_Database_FieldNames.FIELD_OF_APPLICATIONS_AWARD_FOREIGN_KEY,
				DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
				NSF_Database_FieldNames.FIELD_OF_APPLICATIONS_FIELD_OF_APPLICATION_FOREIGN_KEY,
					NSF_Database_FieldNames.FIELD_OF_APPLICATION_TABLE_NAME,
				NSF_Database_FieldNames.FIELD_OF_APPLICATIONS_AWARD_FOREIGN_KEY,
					NSF_Database_FieldNames.AWARD_TABLE_NAME
			);
	
	private FieldOfApplication fieldOfApplication;
	private Award award;

	public AwardFieldOfApplication(FieldOfApplication fieldOfApplication, Award award) {
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

	/*@Override
	public Object createMergeKey() {
		List<Object> mergeKey = new ArrayList<Object>();
		mergeKey.add(this.fieldOfApplication.getPrimaryKey());
		mergeKey.add(this.award.getPrimaryKey());

		return mergeKey;
	}

	@Override
	public void merge(AwardFieldOfApplication otherItem) {
	}*/

	private static Dictionary<String, Object> createAttributes(
			FieldOfApplication fieldOfApplication, Award award) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(
			NSF_Database_FieldNames.FIELD_OF_APPLICATIONS_FIELD_OF_APPLICATION_FOREIGN_KEY, 
			fieldOfApplication.getPrimaryKey());		
		attributes.put(
			NSF_Database_FieldNames.FIELD_OF_APPLICATIONS_AWARD_FOREIGN_KEY,
			award.getPrimaryKey());

		return attributes;
	}
}