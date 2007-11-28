package edu.iu.nwb.analysis.extractmultivaluednetwork.components;

import java.util.Iterator;
import java.util.TreeSet;

public class CoValued {

	Object firstValue;
	Object secondValue;
	boolean directed = false;

	public CoValued(Object s1, Object s2, boolean d) {
		firstValue = s1;
		secondValue = s2;
		directed = d;
	}

	public boolean equals(Object o) {

		if (o.getClass().equals(getClass())) {
			final CoValued ca = (CoValued) o;
			if (!directed) {
				// If the values are strings, I don't care about case, but, if
				// we want to trust all data,
				// this can be changed.
				if (ca.firstValue instanceof String
						&& ca.secondValue instanceof String) {
					final String s1 = (String) firstValue;
					final String s2 = (String) ca.firstValue;
					final String s3 = (String) secondValue;
					final String s4 = (String) ca.secondValue;
					if ((s1.equalsIgnoreCase(s2) && s3.equalsIgnoreCase(s4))
							|| (s1.equalsIgnoreCase(s4) && s3
									.equalsIgnoreCase(s2))) {
						return true;
					}
					return false;
				} else {
					return ((firstValue.equals(ca.firstValue) && secondValue
							.equals(ca.secondValue)) || (firstValue
							.equals(ca.secondValue) && secondValue
							.equals(ca.secondValue)));
				}
			} else {
				if (ca.firstValue instanceof String
						&& ca.secondValue instanceof String) {
					final String s1 = (String) firstValue;
					final String s2 = (String) ca.firstValue;
					final String s3 = (String) secondValue;
					final String s4 = (String) ca.secondValue;
					if ((s1.equalsIgnoreCase(s2)) && (s3.equalsIgnoreCase(s4))) {
						return true;
					}
				} else {
					return firstValue.equals(ca.firstValue)
							&& secondValue.equals(ca.secondValue);
				}
			}
			return false;
		}
		return false;
	}

	public int hashCode() {
		if (!directed) {
			// I'm using TreeSet here because it creates a sorted Set, and for undirected
			// the two values, regardless of order, should result in the same hash code for this class.
			final TreeSet ts = new TreeSet();
			ts.add(firstValue);
			ts.add(secondValue);
			String s = "";
			
			
			
			for (final Iterator it = ts.iterator(); it.hasNext();) {
				s += (String) it.next();
			}

			return s.hashCode();
		} else {
			final String s = firstValue.toString() + secondValue.toString();

			return s.hashCode();
		}
	}

	public String toString() {
		return firstValue + " " + secondValue;
	}

}
