package edu.iu.nwb.analysis.extractnetfromtable.aggregate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ModeFunctionFactory implements AggregateFunctionFactory {
	private static final String type = AggregateFunctionNames.MODE;

	public AggregateFunction getFunction(Class c) {
		if (int.class.equals(c) || Integer.class.equals(c)) {
			return new IntegerMode();
		} else if (boolean.class.equals(c) || Boolean.class.equals(c)) {
			return new BooleanMode();
		} else {			
			return new StringMode();
		}
	}

	public String getType() {
		return ModeFunctionFactory.type;
	}
}

abstract class ModeFunction extends AggregateFunction {
	private Map objectToOccurrences;
	
	public ModeFunction() {
		this.objectToOccurrences = new HashMap();
	}
	
	public Object getResult() {		
		return findKeyWithMaxValue(objectToOccurrences);
	}

	public void operate(Object o) {
		if (!objectToOccurrences.containsKey(o)) {
			objectToOccurrences.put(o, new Integer(0));
		}
		
		int oldNumberOfOccurrences = ((Integer) objectToOccurrences.get(o)).intValue();
		
		objectToOccurrences.put(o, new Integer(oldNumberOfOccurrences + 1));
	}
	
	/**
	 * @param numericMap	A map from Objects to Numbers.
	 * @return				A key in numericMap whose corresponding value is maximal.
	 * 						null when numericMap is empty.
	 */
	private static Object findKeyWithMaxValue(Map/*<Object, Number>*/ numericMap) {
		Object keyToMaxValue = null;
		double maxValue = Double.NEGATIVE_INFINITY;		
		
		for (Iterator numericEntries = numericMap.entrySet().iterator();
				numericEntries.hasNext();) {
			Entry numericEntry = (Entry) numericEntries.next();
			double value = ((Number) numericEntry.getValue()).doubleValue();
			
			if (value >= maxValue) {
				keyToMaxValue = numericEntry.getKey();
				maxValue = value;
			}
		}
		
		return keyToMaxValue;
	}
}

class IntegerMode extends ModeFunction {
	public Class getType() {
		return Integer.class;
	}
}

class StringMode extends ModeFunction {
	public Class getType() {
		return String.class;
	}
}

class BooleanMode extends ModeFunction {
	public Class getType() {
		return Boolean.class;
	}
}