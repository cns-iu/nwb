package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.NSFFile;

public class AwardOccurences extends RowItem<AwardOccurences> {

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
	public void merge(AwardOccurences otherItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldMerge(AwardOccurences otherItem) {
		// TODO Auto-generated method stub
		return false;
	}
}