package edu.iu.nwb.analysis.symmetrize.aggregations;

import java.util.Arrays;
import java.util.Collection;

public class FirstAlphabetically implements Aggregator {

	public Object aggregate(Collection collection) {
		// TODO Auto-generated method stub
		
		String[] values = (String[]) collection.toArray(new String[]{});
		Arrays.sort(values);
		
		return values[0];
	}

	public Class getType(Class base) {
		// TODO Auto-generated method stub
		return String.class;
	}

}
