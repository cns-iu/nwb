package edu.iu.nwb.preprocessing.extractnodesandedges.extractedges.top;

import edu.uci.ics.jung.graph.Edge;

public class ComparableEdge implements Comparable {
	
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

	public int compareTo(Object arg0) {
		if (arg0 instanceof ComparableEdge) {
			ComparableEdge otherComparableEdge = (ComparableEdge) arg0;
			double result =  rank.doubleValue() - otherComparableEdge.rank.doubleValue();
			if (result > 0) return 1;
			if (result < 0) return -1;
			return 0;
		} else {
			throw new IllegalArgumentException("ComparableEdge can only be compared to other ComparableEdge");
		}
	}
	
	
}
