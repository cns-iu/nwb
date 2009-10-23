package edu.iu.scipolicy.preprocessing.aggregatedata;

import java.util.HashSet;
import java.util.Set;

public class Prototype {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int i = 5;
		Set<String> uniqueAggregatedValues = new HashSet<String>();
		
		uniqueAggregatedValues.add("Chintan".toLowerCase());
		
		if (uniqueAggregatedValues.contains("Chinta".toLowerCase())) {
			System.out.println("yyy");
		}
		
		System.out.println(Integer.toString(i).toLowerCase());
	}

}
