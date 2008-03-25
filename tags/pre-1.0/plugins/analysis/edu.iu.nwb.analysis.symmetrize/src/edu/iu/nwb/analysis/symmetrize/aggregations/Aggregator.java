package edu.iu.nwb.analysis.symmetrize.aggregations;

import java.util.Collection;

public interface Aggregator {
	public Object aggregate(Collection collection);
	public Class getType(Class base);
}
