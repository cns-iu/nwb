package edu.iu.nwb.preprocessing.extractnodesandedges.extractedges.abovebelow;

import edu.uci.ics.jung.graph.Edge;

public class EdgeNumericDecorationFilter extends EdgeThresholdFilter {

	@Override
	public boolean acceptEdge(Edge e) {
		if (getDecorationKey() != null) {
			Object value = e.getUserDatum(getDecorationKey());
			if (value instanceof Number) {
				Number numericValue = (Number) value;
				double doubleValue = numericValue.doubleValue();
				return doubleValue > getThreshold();
			} else {
				return true;
			}
		}
		return true;
	}

	@Override
	public String getName() {
		return "Edge Threshold Filter";
	}
}
