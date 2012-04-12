package edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased;

import java.util.Comparator;

import org.cishell.utilities.StringUtilities;

import prefuse.data.Tuple;

public class HasColumnValue implements Comparator<Tuple> {	
	private String column;

	public HasColumnValue(String column) {
		this.column = column;
	}
	
	public int compare(Tuple t1, Tuple t2) {
		Object o1 = t1.get(this.column);
		Object o2 = t2.get(this.column);
		
		if (hasValue(o1) && !hasValue(o2)) {
			return 1;
		} else if (hasValue(o2) && !hasValue(o1)) {
			return -1;
		}

		return 0;
	}

	private static boolean hasValue(Object value) {
		return !StringUtilities.emptyStringIfNull(value).isEmpty();
	}
}
