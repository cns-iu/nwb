package edu.iu.sci2.visualization.geomaps.utility;

import java.util.List;

public class BinaryPredicates {
	private BinaryPredicates() {}

	/**
	 * Only checks up to the length of the shorter List.
	 * Short-circuits.
	 */
	public static <T> boolean isSatisfiedBetween(
			BinaryPredicate<T> predicate,
			List<? extends T> values1,
			List<? extends T> values2) {
		for (int ii = 0; ii < Math.min(values1.size(), values2.size()); ii++) {
			if (!(predicate.apply(Pair.of(values1.get(ii), values2.get(ii))))) {
				return false;
			}
		}

		return true;
	}
}
