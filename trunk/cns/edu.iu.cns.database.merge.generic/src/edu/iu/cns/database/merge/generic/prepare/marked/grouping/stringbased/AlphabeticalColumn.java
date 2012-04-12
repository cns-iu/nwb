package edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased;

import java.util.Comparator;

import org.cishell.utilities.StringUtilities;

import prefuse.data.Tuple;

public class AlphabeticalColumn implements Comparator<Tuple> {	
	private String column;

	public AlphabeticalColumn(String column) {
		this.column = column;
	}
	
	public int compare(Tuple t1, Tuple t2) {
		String s1 = StringUtilities.emptyStringIfNull(t1.get(this.column));
		String s2 = StringUtilities.emptyStringIfNull(t2.get(this.column));
		
		return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
	}
}
