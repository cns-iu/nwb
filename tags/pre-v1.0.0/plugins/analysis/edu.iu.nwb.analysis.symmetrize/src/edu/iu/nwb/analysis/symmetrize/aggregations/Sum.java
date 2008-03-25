package edu.iu.nwb.analysis.symmetrize.aggregations;

import java.util.Collection;
import java.util.Iterator;

public class Sum implements Aggregator {

	public Object aggregate(Collection collection) {
		// TODO Auto-generated method stub
		double sum = 0;
		Iterator iter = collection.iterator();
		while(iter.hasNext()) {
			sum += ((Number) iter.next()).doubleValue();
		}
		return new Double(sum);
	}

	public Class getType(Class base) {
		// TODO Auto-generated method stub
		return Double.class;
	}

}
