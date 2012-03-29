package edu.iu.nwb.analysis.symmetrize.aggregations;

import java.util.Arrays;
import java.util.Collection;

public class LastAlphabetically implements Aggregator {

	public Object aggregate(Collection collection) {
		// TODO Auto-generated method stub
		String[] values = (String[]) collection.toArray(new String[]{});
		Arrays.sort(values);
		
		return values[values.length - 1];
	}

	public Class getType(Class base) {
		// TODO Auto-generated method stub
		return String.class;
	}

}
