package edu.iu.scipolicy.database.nsf.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.scipolicy.database.nsf.load.model.entity.Award;
import edu.iu.scipolicy.database.nsf.load.model.entity.NSFFile;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class AwardOccurence extends RowItem<AwardOccurence> {

	public static final Schema<AwardOccurence> SCHEMA = new Schema<AwardOccurence>(
			false,
			NSF_Database_FieldNames.AWARD_OCCURRENCES_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSF_Database_FieldNames.AWARD_OCCURRENCES_NSF_FILE_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
			FOREIGN_KEYS(
					NSF_Database_FieldNames.AWARD_OCCURRENCES_AWARD_FOREIGN_KEY,
						NSF_Database_FieldNames.AWARD_TABLE_NAME,
					NSF_Database_FieldNames.AWARD_OCCURRENCES_NSF_FILE_FOREIGN_KEY,
						NSF_Database_FieldNames.NSF_FILE_TABLE_NAME);
	
	private Award award;
	private NSFFile nsfFile;

	public AwardOccurence(Award award, NSFFile nsfFile) {
		super(createAttributes(award, nsfFile));
		this.award = award;
		this.nsfFile = nsfFile; 
	}

	public Award getAward() {
		return this.award;
	}
	
	public NSFFile getNSFFile() {
		return this.nsfFile;
	}

	private static Dictionary<String, Comparable<?>> createAttributes(Award award, NSFFile nsfFile) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		
		attributes.put(NSF_Database_FieldNames.AWARD_OCCURRENCES_AWARD_FOREIGN_KEY, 
			   	   	   award.getPrimaryKey());
		
		attributes.put(NSF_Database_FieldNames.AWARD_OCCURRENCES_NSF_FILE_FOREIGN_KEY, 
					   nsfFile.getPrimaryKey());
		
		return attributes;
	}

	@Override
	public void merge(AwardOccurence otherItem) { }

	@Override
	public boolean shouldMerge(AwardOccurence otherItem) {
		return false;
	}
}