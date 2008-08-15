package edu.iu.nwb.preprocessing.extractnodesandedges.extractnodes.abovebelow;

import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.filters.impl.NumericDecorationFilter;

/*
* Copyright (c) 2003, the JUNG Project and the Regents of the University 
* of California
* All rights reserved.
*
* This software is open-source under the BSD license; see either
* "license.txt" or
* http://jung.sourceforge.net/license.txt for a description.
*/

//copied from jung source code, and switched the sign (now it's less than)

public class ReverseOfNumericDecorationFilter extends NumericDecorationFilter {

		public boolean acceptVertex(Vertex vertex) {
			Number n = (Number) vertex.getUserDatum(this.getDecorationKey());

			if (n.doubleValue() < this.getThreshold()) {
				return true;
			}
			return false;
		}

}
