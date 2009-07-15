package edu.iu.nwb.analysis.pagerank.weighted;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

public abstract class Graph {
	int numberOfNodes;

	public int getNumberOfNodes() {
		return numberOfNodes;
	}
	
	public abstract void performEdgePass(EdgeHandler handler) throws AlgorithmExecutionException;
}
