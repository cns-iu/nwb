package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.Iterator;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;

public class NodeStats extends Thread{
	int numberOfNodes;
	int numberOfAttributes;
	String[] nodeAttributes;
	
	double averageDegree = 0;
	double averageInDegree = 0;
	double averageOutDegree = 0;
	
	int numberOfIsolatedNodes = 0;
	private Graph nodeGraph;
	
	private NodeStats(final Graph graph){
		this.nodeGraph = graph;	
	}
	
	public static NodeStats constructNodeStats(final Graph graph){
		return new NodeStats(graph);
	}
	
	private void findIsolatedNodes(final Graph graph){
		for(Iterator it = graph.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			int degree = n.getDegree();
			if(degree == 0){
				this.numberOfIsolatedNodes++;
			}
			this.averageDegree+= (double) degree/ (double) this.numberOfNodes;
			if(graph.isDirected()){
				this.averageInDegree += (double)n.getInDegree()/(double)this.numberOfNodes;
				this.averageOutDegree += (double)n.getOutDegree()/(double)this.numberOfNodes;				
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

	public void run(){
		this.numberOfNodes = this.nodeGraph.getNodeCount();
		initializeAttributes(this.nodeGraph);
		
		this.numberOfIsolatedNodes = 0;
		this.findIsolatedNodes(this.nodeGraph);
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
	
	/**
	 * @return the averageDegree
	 */
	public double getRoundedAverageDegree() {
		return Double.parseDouble(NetworkProperties.roundedStatisticsFormatter.format(averageDegree));
	}
	
	/**
	 * @return the averageInDegree
	 */
	public double getRoundedAverageInDegree() {
		return Double.parseDouble(NetworkProperties.roundedStatisticsFormatter.format(averageInDegree));
	}

	/**
	 * @return the averageOutDegree
	 */
	public double getRoundedAverageOutDegree() {
		return Double.parseDouble(NetworkProperties.roundedStatisticsFormatter.format(averageOutDegree));
	}

	protected String nodeInfo(){
		StringBuffer sb = new StringBuffer();
		sb.append("Nodes: " + this.numberOfNodes);
		sb.append(System.getProperty("line.separator"));
		sb.append(this.isolatedNodeInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append("Node attributes present: ");
		int numAttributes = this.numberOfAttributes;
		for(int i = 0; i < numAttributes; i++){
			sb.append(this.nodeAttributes[i]);
			if(i != numAttributes-1){
				sb.append(", ");
			}
			
		}
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}
	
	protected String isolatedNodeInfo(){
		StringBuffer sb = new StringBuffer();
		sb.append("Isolated nodes: " + this.numberOfIsolatedNodes);
		return sb.toString();
	}

	
}
