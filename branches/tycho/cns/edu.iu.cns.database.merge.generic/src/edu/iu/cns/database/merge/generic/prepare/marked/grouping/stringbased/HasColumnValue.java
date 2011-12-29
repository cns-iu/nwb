package edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased;

import java.util.Comparator;

import org.cishell.utilities.StringUtilities;

import prefuse.data.Tuple;

public class HasColumnValue implements Comparator<Tuple> {	
	private String column;

	public HasColumnValue(String column) {
		this.column = column;
	}
	
	public int compare(Tuple o1, Tuple o2) {
		Object v1 = o1.get(column);
		Object v2 = o2.get(column);
		
		if (valuePresent(v1) && !valuePresent(v2)) {
			return 1;
		} else if (valuePresent(v2) && !valuePresent(v1)) {
			return -1;
		}
		
		return 0;
	}

	private static boolean valuePresent(Object value) {
		return !StringUtilities.emptyStringIfNull(value).isEmpty();
	}
}
