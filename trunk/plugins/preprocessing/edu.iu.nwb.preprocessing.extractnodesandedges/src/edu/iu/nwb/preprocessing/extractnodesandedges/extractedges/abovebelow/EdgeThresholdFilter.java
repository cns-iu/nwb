package edu.iu.nwb.preprocessing.extractnodesandedges.extractedges.abovebelow;

import edu.uci.ics.jung.graph.filters.GeneralEdgeAcceptFilter;

public abstract class EdgeThresholdFilter extends GeneralEdgeAcceptFilter {

	private Object decoratorKey;
	private double threshold = 0.0;
	
	public Object getDecorationKey() {
		return this.decoratorKey;
	}
	
	public void setDecorationKey(Object decoratorKey) {
		this.decoratorKey = decoratorKey;
	}
	
	public double getThreshold() {
		return this.threshold;
	}
	
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public String getName() {
		return "General Edge Threshold Filter";
	}

}
