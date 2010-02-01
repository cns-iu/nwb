package edu.iu.scipolicy.database.nsf.merge;

import prefuse.data.Tuple;
import edu.iu.cns.database.merge.generic.maker.MergeCheck;

public class AlwaysMerge implements MergeCheck {
	
	
	//we could speed things up if there was a flag to avoid the shouldMerge check entirely when we always merge things with a key
	public boolean shouldMerge(Tuple first, Tuple second) {
		return true;
	}

}
