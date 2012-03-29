package edu.iu.nwb.util.nwbfile.model;

import java.util.Map;

public class AttributePredicates {
	/*
	 * new Function<Map<String, Object>, ? extends Number>(attributeName)
	 * 
	 * new Predicate<Double>(limit)
	 * 
	 * Functions.compose(function, predicate)
	 * function.compose(predicate) 
	 */
	
	public static AttributePredicate keepAbove(final String attribute, final double limit) {
		return new AttributePredicate() {
			public boolean apply(Map<String, Object> input) {
				return getAttributeAsDouble(input, attribute) > limit;
			}
			@Override public String toString() {
				return String.format("AttributePredicate(%s > %f)", attribute, limit);
			}
		};
	}
	
	public static AttributePredicate keepBelow(final String attribute, final double limit) {
		return new AttributePredicate() {
			public boolean apply(Map<String, Object> input) {
				return getAttributeAsDouble(input, attribute) < limit;
			}
			@Override public String toString() {
				return String.format("AttributePredicate(%s < %f)", attribute, limit);
			}
		};
	}
	
	// default visibility to avoid "synthetic accessor" warnings
	static double getAttributeAsDouble(Map<String, Object> input,
			final String attribute) {
		return ((Number) input.get(attribute)).doubleValue();
	}

//	private static Function<Map<String, Object>, Double> numberGetter(final String attribute) {
//		return new Function<Map<String, Object>, Double>() {
//			public Double apply(Map<String, Object> input) {
//				Object value = input.get(attribute);
//				if (value == null) return null;
//				if (value instanceof Double) return (Double) value; // skip useless unbox/rebox
//				return ((Number) value).doubleValue();
//			}
//		};
//	}
//	
//	private static Predicate<Double> isAbove(final double limit) {
//		return new Predicate<Double>() {
//			public boolean apply(Double input) {
//				return input > limit; // unboxing: can't handle null.
//			}
//		};
//	}
//	
//	public static Predicate<Map<String, Object>> attributeIsAbove(final String attribute, final double limit) {
//		Predicate<Double> aboveAndNotNull = Predicates.and(
//				Predicates.notNull(),
//				isAbove(limit));
//		return Predicates.compose(aboveAndNotNull, numberGetter(attribute));
//	}
//	
}
