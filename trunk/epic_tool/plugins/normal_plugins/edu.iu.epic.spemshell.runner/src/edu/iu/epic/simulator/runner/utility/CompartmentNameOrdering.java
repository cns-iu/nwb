package edu.iu.epic.simulator.runner.utility;

import com.google.common.base.Function;
import com.google.common.collect.Ordering;

import edu.iu.epic.modeling.compartment.model.Compartment;

public class CompartmentNameOrdering extends Ordering<String> {
	public int compare(String left, String right) {
		int scoreComparison = score(left).compareTo(score(right));

		if (scoreComparison == 0) {
			// Resolve ties by direct comparison
			return left.compareTo(right);
		} else {
			return scoreComparison;
		}
	}
	
	public Ordering<Compartment> ofCompartments() {
		return this.onResultOf(new Function<Compartment, String>() {
			public String apply(Compartment compartment) {
				return compartment.getName();
			}
		});
	}
	
	private static Integer score(String rawName) {			
		String name = rawName.toUpperCase();
		
		if (name.startsWith("S")) {
			return 1;
		} else if (name.startsWith("E")) {
			return 2;
		} else if (name.startsWith("L")) {
			return 3;
		} else if (name.startsWith("I")) {
			return 4;
		} else if (name.startsWith("R")) {
			return 5;
		} else {
			return 6;
		}
	}
}