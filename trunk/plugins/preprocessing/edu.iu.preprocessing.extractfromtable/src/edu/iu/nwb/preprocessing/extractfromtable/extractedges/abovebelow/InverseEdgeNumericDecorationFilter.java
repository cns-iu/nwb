package edu.iu.nwb.preprocessing.extractfromtable.extractedges.abovebelow;

import edu.uci.ics.jung.graph.Edge;

public class InverseEdgeNumericDecorationFilter extends EdgeNumericDecorationFilter {
		public boolean acceptEdge(Edge e) {
			return ! super.acceptEdge(e);
		}

		public String getName() {
			return "Inverse Edge Numeric Decoration Filter";
		}
}
