package edu.iu.scipolicy.visualization.geomaps.utility;

import java.util.Collection;

public abstract class BinaryCondition<T> {	
	public abstract boolean satisfiedBy(T object1, T object2);
	
	public boolean isPairwiseSatisfiedBy(Collection<T> values) {
		for (T value1 : values) {
			for (T value2 : values) {
				if (!(satisfiedBy(value1, value2))) {
					return false;
				}
			}
		}
		
		return true;
	}	
}
