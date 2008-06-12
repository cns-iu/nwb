package edu.iu.nwb.preprocessing.extractnodesandedges.extractnodes.abovebelow;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.filters.impl.NumericDecorationFilter;

public class InverseNumericDecorationFilter extends NumericDecorationFilter {

	public boolean acceptVertex(Vertex vertex) {
		return ! super.acceptVertex(vertex);
	}
}
