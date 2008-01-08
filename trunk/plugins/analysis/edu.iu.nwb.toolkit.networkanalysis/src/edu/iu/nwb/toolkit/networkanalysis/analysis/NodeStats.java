package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.Iterator;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;

public class NodeStats {
	int numberOfNodes;
	int numberOfAttributes;
	String[] nodeAttributes;
	
	int numberOfIsolatedNodes;
	
	public NodeStats(final Graph graph){
		this.numberOfNodes = graph.getNodeCount();
		initializeAttributes(graph);
		
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
	
	private void initializeAttributes(final Graph graph){
		Table t = graph.getNodeTable();
		this.numberOfAttributes = t.getColumnCount();
		nodeAttributes = new String[this.numberOfAttributes];
		for(int i = 0; i < this.numberOfAttributes; i++){
			nodeAttributes[i] = t.getColumnName(i);
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
	
	public int getNumberOfAttributes(){
		return this.numberOfAttributes;
	}
	
	public String[] getNodeAttributes(){
		return this.nodeAttributes;
	}

	
}
