package edu.iu.sci2.visualization.scimap.references;

import java.text.DecimalFormat;
import java.util.Map;

public class Totals {
	
	private int found = 0;
	private int unfound = 0;
	
	public static DecimalFormat formatter = new DecimalFormat("###,###");

	public Totals(Map<String, Integer> found, Map<String, Integer> unfound) {
		this.found = count(found);
		this.unfound = count(unfound);
	}

	private int count(Map<String, Integer> found) {
		int total = 0;
		for(int number : found.values())  {
			total += number;
		}
		return total;
	}
	
	public String getFound() {
		return formatter.format(found);
	}
	
	public String getUnfound() {
		return formatter.format(unfound);
	}
	
	public String getAll() {
		return formatter.format(found + unfound);
	}
}
