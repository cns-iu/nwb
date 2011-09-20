package edu.iu.nwb.preprocessing.extractnodesandedges.extractedges.top;

import edu.uci.ics.jung.graph.Edge;

public class ComparableEdge implements Comparable<ComparableEdge> {	
	private Edge e;
	private Double rank;
	
	public ComparableEdge(Edge e, Object rankKey) {
		this.e = e;
		Object datum = e.getUserDatum(rankKey);
		if (datum instanceof Number) {
			rank = new Double(((Number) datum).doubleValue());
		} else {
			throw new IllegalArgumentException("rankKey must index a Number");
		}
	}
	
	public Edge getEdge() {
		return e;
	}

	public int compareTo(ComparableEdge that) {
		return this.rank.compareTo(that.rank);
	}	
}
