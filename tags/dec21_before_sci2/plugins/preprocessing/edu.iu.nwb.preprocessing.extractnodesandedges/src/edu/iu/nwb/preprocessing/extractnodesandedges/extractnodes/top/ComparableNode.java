package edu.iu.nwb.preprocessing.extractnodesandedges.extractnodes.top;

import edu.uci.ics.jung.graph.Vertex;

public class ComparableNode implements Comparable {
	
	private Vertex v;
	private Double rank;
	
	public ComparableNode(Vertex v, Object rankKey) {
		this.v = v;
		Object datum = v.getUserDatum(rankKey);
		if (datum instanceof Number) {
			rank = new Double(((Number) datum).doubleValue());
		} else {
			throw new IllegalArgumentException("rankKey must index a Number");
		}
	}
	
	public Vertex getNode() {
		return v;
	}

	public int compareTo(Object arg0) {
		if (arg0 instanceof ComparableNode) {
			ComparableNode otherComparableNode = (ComparableNode) arg0;
			double result =  rank.doubleValue() - otherComparableNode.rank.doubleValue();
			if (result > 0) return 1;
			if (result < 0) return -1;
			return 0;
		} else {
			throw new IllegalArgumentException("ComparableNode can only be compared to other ComparableNode");
		}
	}
	
	
}
