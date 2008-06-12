package edu.iu.nwb.preprocessing.extractnodesandedges.extractedges.abovebelow;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.filters.GeneralEdgeAcceptFilter;

public class EdgeNumericDecorationFilter extends GeneralEdgeAcceptFilter {

	private Object decoratorKey;
	private double threshold = 0.0;
	
	public boolean acceptEdge(Edge e) {
		if (decoratorKey != null) {
			Object value = e.getUserDatum(decoratorKey);
			if (value instanceof Number) {
				Number numericValue = (Number) value;
				double doubleValue = numericValue.doubleValue();
				return doubleValue > threshold;
			} else {
				return true;
			}
		}
		return true;
	}
	
	public void setDecorationKey(Object decoratorKey) {
		this.decoratorKey = decoratorKey;
	}
	
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getName() {
		return "Edge Numeric Decoration Filter";
	}

}
