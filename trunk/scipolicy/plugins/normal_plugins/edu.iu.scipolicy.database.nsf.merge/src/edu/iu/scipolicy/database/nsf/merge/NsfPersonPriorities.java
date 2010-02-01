package edu.iu.scipolicy.database.nsf.merge;

import prefuse.data.Tuple;
import edu.iu.cns.database.merge.generic.maker.LongerColumn;
import edu.iu.cns.database.merge.generic.maker.PreferrableFormComparator;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class NsfPersonPriorities implements PreferrableFormComparator {
	
	
	
	public int compare(Tuple o1, Tuple o2) {
		return new LongerColumn(NSF_Database_FieldNames.FORMATTED_FULL_NAME).compare(o1, o2);
	}

}
