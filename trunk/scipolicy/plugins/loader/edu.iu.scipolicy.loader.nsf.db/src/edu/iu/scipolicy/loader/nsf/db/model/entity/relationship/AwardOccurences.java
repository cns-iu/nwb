package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.NSFFile;

public class AwardOccurences extends RowItem<AwardOccurences> {

	public static final Schema<AwardOccurences> SCHEMA = new Schema<AwardOccurences>(
			false,
			NSFDatabase.AWARD_OCCURRENCES_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSFDatabase.AWARD_OCCURRENCES_NSF_FILE_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY).
			FOREIGN_KEYS(
					NSFDatabase.AWARD_OCCURRENCES_AWARD_FOREIGN_KEY,
						NSFDatabase.AWARD_TABLE_NAME,
					NSFDatabase.AWARD_OCCURRENCES_NSF_FILE_FOREIGN_KEY,
						NSFDatabase.NSF_FILE_TABLE_NAME);
	
	private Award award;
	private NSFFile nSFFile;

	public AwardOccurences(Award award, NSFFile nSFFile) {
		super(createAttributes());
		this.award = award;
		this.nSFFile = nSFFile; 
	}

	public Award getAward() {
		return this.award;
	}
	
	public NSFFile getNSFFile() {
		return this.nSFFile;
	}

	private static Dictionary<String, Comparable<?>> createAttributes() {
		return new Hashtable<String, Comparable<?>>();
	}

	@Override
	public void merge(AwardOccurences otherItem) { }

	@Override
	public boolean shouldMerge(AwardOccurences otherItem) {
		return false;
	}
}