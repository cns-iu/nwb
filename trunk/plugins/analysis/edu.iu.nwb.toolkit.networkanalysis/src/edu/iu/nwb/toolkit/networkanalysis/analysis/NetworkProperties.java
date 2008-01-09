package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.text.DecimalFormat;


public class NetworkProperties {

	ComponentForest cf;
	EdgeStats edgeStats;
	NodeStats nodeStats;
	

	boolean isDirectedGraph;
	private double density = -1;
	


	public NetworkProperties(final prefuse.data.Graph graph) {
		this.isDirectedGraph = graph.isDirected();
		
		nodeStats = new NodeStats(graph);
		edgeStats = new EdgeStats(graph);
		
		cf = new ComponentForest(graph);
		
		
		
		if(!(this.hasParallelEdges() || this.hasSelfLoops()))
			calculateDensity();


	}




	public int getNumNodes() {
		return this.nodeStats.getNumerOfNodes();
	}

	public boolean isConnected() {
		return this.cf.isWeaklyConnected();
	}

	public boolean isDirected() {
		
		return this.isDirectedGraph;
	}

	public boolean hasSelfLoops() {
		
		return (edgeStats.getNumberOfSelfLoops() > 0);
	}

	public boolean hasParallelEdges() {
	
		return edgeStats.getNumberOfParallelEdges() > 0;
	}

	public int getNumEdges() {
		return this.edgeStats.getNumberOfEdges();
	}	

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.directedInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append(this.nodeStats.nodeInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append(this.edgeStats.edgeInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append(this.densityInfo());
		
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append(this.connectedInfo());
		return sb.toString();
	}
	
	



	protected String directedInfo(){
		StringBuffer sb = new StringBuffer();
		if(this.isDirected()){
			sb.append("This graph is directed.");
		}
		else{
			sb.append("This graph claims to be undirected.");
		}
		return sb.toString();
	}

	protected String connectedInfo(){
		StringBuffer sb = new StringBuffer();
		if(cf.isWeaklyConnected()){
			sb.append("This graph is weakly connected.\n");
		}
		else{
			sb.append("This graph is not weakly connected.\n");
		}

		sb.append("There are " + cf.getWeakComponentClusters() + " weakly connected components. (" + this.nodeStats.getNumberOfIsolatedNodes() +
				" isolates)\n");
		sb.append("The largest connected component consists of " + cf.getMaximumWeakConnectedNodes()+ " nodes.\n");
		
		if(this.isDirected()){
			sb.append(System.getProperty("line.separator"));
			if(cf.isStronglyConnected()){
				sb.append("This graph is strongly connected\n");
			}
			else{
				sb.append("This graph is not strongly connected.\n");
			}
			
			sb.append("There are " + cf.getStrongComponentClusters() + " strongly connected components.\n");
			sb.append("The largest strongly connected component consists of " + cf.getMaximumStrongConnectedNodes() + " nodes.\n");
		}
		else{
			sb.append("Did not calculate strong connectedness because this graph was not directed.");
		}
		
		return sb.toString();
	}

	public String densityInfo(){
		StringBuffer sb = new StringBuffer();
		if(density > -1){
			DecimalFormat densityFormatter = new DecimalFormat("#.#####");
			String densityString = densityFormatter.format(this.density);
			
			sb.append("density (disregarding weights): " + densityString);
			sb.append(System.getProperty("line.separator"));
			sb.append(this.weightedDensityInfo());
			sb.append(System.getProperty("line.separator"));
			sb.append(System.getProperty("line.separator"));
			
			
		}

		

		return sb.toString();
	}
	
	
	
	
	protected String weightedDensityInfo(){
		StringBuffer sb = new StringBuffer();
		
		
		if(this.edgeStats.numAdditionalNumericAttributes > 0){
			sb.append(System.getProperty("line.separator"));
		sb.append("Additional Densities by Numeric Attribute");
		sb.append(System.getProperty("line.separator"));
		sb.append(printWeightedDensities(false));
		sb.append(printWeightedDensities(true));
		
		
		
		}
		return sb.toString();
	}
	
	private String printWeightedDensities(boolean useObservedMax){
		double weightedSum,maxObservedValue,weightedDensity, maxConnections;
		DecimalFormat densityFormatter = new DecimalFormat("#.#####");
		StringBuffer sb = new StringBuffer();
		maxConnections = (this.getNumNodes()*(this.getNumNodes()-1));
		if(useObservedMax){
			sb.append("densities (weighted against observed max)");
		}
		else{
			sb.append("densities (weighted against standard max)");
		}
		sb.append(System.getProperty("line.separator"));
		for(int i = 0; i < this.edgeStats.getAdditionalNumericAttributes().length; i++){
			sb.append(this.edgeStats.getAdditionalNumericAttributes()[i]+": ");
		
		
			weightedSum = this.edgeStats.getWeightedDensitySumArray()[i];
			maxObservedValue = this.edgeStats.getMaxValueArray()[i];
			if(this.isDirectedGraph){
				weightedDensity = weightedSum/maxConnections;
			}
			else{
				
				weightedDensity = weightedSum/(.5*maxConnections);
			}
			if(useObservedMax)
				weightedDensity = weightedDensity/maxObservedValue;
			
			sb.append(densityFormatter.format(weightedDensity));
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	protected void calculateDensity(){
		long maxEdges = this.getNumNodes()* (this.getNumNodes()-1);
		if(this.isDirected()){
			density = (double)this.getNumEdges()/(maxEdges);
		}
		else{
			density = (double)this.getNumEdges()/(maxEdges/2);
		}
	}

	
	public String testPrint(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.edgeStats.printEdgeAttributes());
		sb.append(this.nodeStats.printNumberOfIsolatedNodes());
		return sb.toString();
	}
	
	

}
