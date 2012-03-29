package edu.iu.sci2.database.isi.merge.people;

import java.util.Comparator;

import prefuse.data.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.LongerColumn;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class IsiPersonPriorities implements Comparator<Tuple> {	
	private Ordering<Tuple> ordering =
			Ordering.compound(Lists.newArrayList(
					new LongerColumn(ISI.FULL_NAME),
					new LongerColumn(ISI.PERSONAL_NAME),
					new LongerColumn(ISI.UNSPLIT_ABBREVIATED_NAME),
					new LongerColumn(ISI.MIDDLE_INITIAL)));
	
	
	public int compare(Tuple o1, Tuple o2) {
		return ordering.compare(o1, o2);
	}
}
