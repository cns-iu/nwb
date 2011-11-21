package edu.iu.sci2.database.nsf.merge;

import java.util.Comparator;

import prefuse.data.Tuple;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.LongerColumn;
import edu.iu.sci2.utilities.nsf.NSF_Database_FieldNames;

public class NsfPersonPriorities implements Comparator<Tuple> {	
	public int compare(Tuple o1, Tuple o2) {
		return new LongerColumn(NSF_Database_FieldNames.FORMATTED_FULL_NAME).compare(o1, o2);
	}
}
