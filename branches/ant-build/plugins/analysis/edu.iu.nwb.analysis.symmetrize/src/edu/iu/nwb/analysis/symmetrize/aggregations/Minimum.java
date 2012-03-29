package edu.iu.nwb.analysis.symmetrize.aggregations;

import java.util.Arrays;
import java.util.Collection;

public class Minimum implements Aggregator {

	public Object aggregate(Collection collection) {
		// TODO Auto-generated method stub
		Object[] values = collection.toArray();
		Arrays.sort(values);
		
		return values[0];
	}

	public Class getType(Class base) {
		// TODO Auto-generated method stub
		return base;
	}

}
