package edu.iu.scipolicy.preprocessing.aggregatedata.aggregators;

import java.util.Collections;
import java.util.List;

import edu.iu.scipolicy.preprocessing.aggregatedata.SingleFunctionAggregator;

public class MinAggregator implements SingleFunctionAggregator {

	public Object aggregateValue(List objectsToAggregate) {
		return (Number) Collections.min(objectsToAggregate);
	}

}
