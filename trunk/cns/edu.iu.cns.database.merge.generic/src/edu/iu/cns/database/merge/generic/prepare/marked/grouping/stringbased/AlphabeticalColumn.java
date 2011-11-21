package edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased;

import java.util.Comparator;

import org.cishell.utilities.StringUtilities;

import prefuse.data.Tuple;

public class AlphabeticalColumn implements Comparator<Tuple> {	
	private String column;

	public AlphabeticalColumn(String column) {
		this.column = column;
	}
	
	public int compare(Tuple o1, Tuple o2) {
		String v1 = StringUtilities.emptyStringIfNull(o1.get(column));
		String v2 = StringUtilities.emptyStringIfNull(o2.get(column));
		
		return String.CASE_INSENSITIVE_ORDER.compare(v1, v2);
	}
}
