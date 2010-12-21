package edu.iu.scipolicy.visualization.geomaps.utility;

import java.util.Collection;
import java.util.List;

public abstract class BinaryCondition<T> {	
	public abstract boolean isSatisfiedBy(T object1, T object2);
	
	public boolean isPairwiseSatisfiedBy(Collection<T> values) {
		for (T value1 : values) {
			for (T value2 : values) {
				if (!(isSatisfiedBy(value1, value2))) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	// Note that this checks only up to the length of the shorter List.
	public boolean isSatisfiedBetween(List<T> values1, List<T> values2) {
		for (int ii = 0; ii < Math.min(values1.size(), values2.size()); ii++) {
			if (!(isSatisfiedBy(values1.get(ii), values2.get(ii)))) {
				return false;
			}
		}
		
		return true;
	}
}
