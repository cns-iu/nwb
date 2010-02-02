package edu.iu.scipolicy.database.isi.merge;

import java.util.Comparator;
import java.util.List;

import prefuse.data.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import edu.iu.cns.database.merge.generic.maker.LongerColumn;
import edu.iu.cns.database.merge.generic.maker.PreferrableFormComparator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class IsiPersonPriorities implements PreferrableFormComparator {	
	private List<? extends Comparator<Tuple>> columns =
			Lists.newArrayList(
					new LongerColumn(ISI.FULL_NAME),
					new LongerColumn(ISI.PERSONAL_NAME),
					new LongerColumn(ISI.UNSPLIT_ABBREVIATED_NAME),
					new LongerColumn(ISI.MIDDLE_INITIAL));
	
	
	public int compare(Tuple o1, Tuple o2) {
		return Ordering.compound(columns).compare(o1, o2);
	}

}
