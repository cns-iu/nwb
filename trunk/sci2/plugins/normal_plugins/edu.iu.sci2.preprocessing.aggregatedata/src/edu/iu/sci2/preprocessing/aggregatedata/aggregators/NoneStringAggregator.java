package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

import java.util.List;

import edu.iu.sci2.preprocessing.aggregatedata.SingleFunctionAggregator;

public class NoneStringAggregator implements SingleFunctionAggregator<StringBuffer> {
	@Override
	public StringBuffer aggregateValue(List<StringBuffer> objectsToAggregate) {
		return null;
	}
}
