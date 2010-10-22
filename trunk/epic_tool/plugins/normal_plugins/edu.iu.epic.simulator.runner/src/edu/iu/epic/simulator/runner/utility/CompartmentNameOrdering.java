package edu.iu.epic.simulator.runner.utility;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import edu.iu.epic.modeling.compartment.model.Compartment;

public class CompartmentNameOrdering extends Ordering<String> {
	/** Compounded with the natural ordering on Strings to maintain consistency with equals(). */
	public static final Ordering<String> BY_NAME =
		Ordering.natural()
			.onResultOf(new CompartmentNameRankFunction())
			.compound(Ordering.natural());
	
	
	/** Adapts the ordering on compartment names to an ordering on compartments. */
	public static final Ordering<Compartment> BY_COMPARTMENT =
		BY_NAME.onResultOf(new Function<Compartment, String>() {
			public String apply(Compartment compartment) {
				return compartment.getName();
			}
		});

	
	public int compare(String leftName, String rightName) {
		return BY_NAME.compare(leftName, rightName);
	}
	
	
	public static class CompartmentNameRankFunction implements Function<String, Integer> {
		/** These must be upper-case. */
		public final ImmutableList<String> PREFERRED_ORDER_OF_NAME_FIRST_CHARACTERS =
			ImmutableList.of("S", "E", "L", "I", "R");
	
		public Integer apply(String compartmentName) {
			return rank(compartmentName);
		}
		
		private Integer rank(String compartmentName) {
			String firstCharacterNormal = compartmentName.substring(0, 1).toUpperCase();
			
			if (PREFERRED_ORDER_OF_NAME_FIRST_CHARACTERS.contains(firstCharacterNormal)) {
				return PREFERRED_ORDER_OF_NAME_FIRST_CHARACTERS.indexOf(firstCharacterNormal);
			} else {
				/* Unenumerated values rank after all enumerated ones. */
				return PREFERRED_ORDER_OF_NAME_FIRST_CHARACTERS.size();
			}
		}
	}
}