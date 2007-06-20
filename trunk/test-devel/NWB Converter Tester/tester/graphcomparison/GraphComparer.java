package tester.graphcomparison;

import prefuse.data.Graph;

public interface GraphComparer {
	public boolean compare(Graph originalGraph, Graph convertedGraph, boolean IdsPreserved);
}
