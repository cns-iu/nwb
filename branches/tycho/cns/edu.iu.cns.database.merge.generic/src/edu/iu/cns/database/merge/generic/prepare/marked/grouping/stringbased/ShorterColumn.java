package edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased;

import java.util.Comparator;

import org.cishell.utilities.StringUtilities;

import prefuse.data.Tuple;

public class ShorterColumn implements Comparator<Tuple> {	
	private String column;

	public ShorterColumn(String column) {
		this.column = column;
	}
	
	public int compare(Tuple o1, Tuple o2) {
		Integer firstValue = StringUtilities.emptyStringIfNull(o1.get(column)).length();
		Integer secondValue = StringUtilities.emptyStringIfNull(o2.get(column)).length();
		
		return secondValue.compareTo(firstValue);
	}
}
