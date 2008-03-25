package edu.iu.nwb.analysis.symmetrize.aggregations;

import java.util.Collection;

public class Average implements Aggregator {

	public Object aggregate(Collection collection) {
		Number sum = (Number) new Sum().aggregate(collection);
		return new Double(sum.doubleValue()/collection.size());
	}

	public Class getType(Class base) {
		// TODO Auto-generated method stub
		return Double.class;
	}

}
