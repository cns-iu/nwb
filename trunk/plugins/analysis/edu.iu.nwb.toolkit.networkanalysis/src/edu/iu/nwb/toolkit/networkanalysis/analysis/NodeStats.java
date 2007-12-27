package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.Iterator;

import prefuse.data.Graph;
import prefuse.data.Node;

public class NodeStats {
	int numberOfNodes;
	
	int numberOfIsolatedNodes;
	
	public NodeStats(final Graph graph){
		this.numberOfNodes = graph.getNodeCount();
		this.numberOfIsolatedNodes = 0;
		this.findIsolatedNodes(graph);
	}
	
	private void findIsolatedNodes(final Graph graph){
		for(Iterator it = graph.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			if(n.getDegree() == 0){
				this.numberOfIsolatedNodes++;
			}
		}
	}
	
	public int getNumerOfNodes(){
		return this.numberOfNodes;
	}

	
	public int getNumberOfIsolatedNodes(){
		return this.numberOfIsolatedNodes;
	}
	
	public String printNumberOfIsolatedNodes(){
		StringBuffer sb = new StringBuffer();
			sb.append(this.numberOfIsolatedNodes);
		return sb.toString();
	}
	
	public boolean hasIsolatedNodes(){
		return this.numberOfIsolatedNodes > 0;
	}

	
}
