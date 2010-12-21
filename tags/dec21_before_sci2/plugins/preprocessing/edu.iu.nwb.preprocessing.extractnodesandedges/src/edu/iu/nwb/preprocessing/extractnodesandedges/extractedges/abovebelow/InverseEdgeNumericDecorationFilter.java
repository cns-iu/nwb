package edu.iu.nwb.preprocessing.extractnodesandedges.extractedges.abovebelow;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.filters.GeneralEdgeAcceptFilter;

public class InverseEdgeNumericDecorationFilter extends EdgeThresholdFilter {
	
	public boolean acceptEdge(Edge e) {
		if (getDecorationKey() != null) {
			Object value = e.getUserDatum(getDecorationKey());
			if (value instanceof Number) {
				Number numericValue = (Number) value;
				double doubleValue = numericValue.doubleValue();
				return doubleValue < getThreshold();
			} else {
				return true;
			}
		}
		return true;
	}

	public String getName() {
		return "Below Edge Threshold Filter";
	}
}
