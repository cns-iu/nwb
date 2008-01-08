package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;


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
	
	public double getMeanNumberofNodes(){
		return this.getNumEdges()/this.getNumNodes();
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
	
	

	protected static LinkedHashSet uDFS(final Graph g, Integer n){

		LinkedHashSet preOrder = new LinkedHashSet();


		runUDFS(g,n, preOrder);


		return preOrder;
	}




	protected static LinkedHashSet dDFS(final Graph g, Integer n, boolean getPreOrder, boolean isReverse){
		LinkedHashSet nodeSet = new LinkedHashSet();
		

		runDDFS(g,n,nodeSet,getPreOrder,isReverse);
		

		return nodeSet;
	}

	protected static void runUDFS(final Graph g, Integer n, LinkedHashSet pre){
		Queue q = new LinkedList();
		q.add(g.getNode(n.intValue()));

		while(!q.isEmpty()){
			Node nd = (Node)q.poll();
			Integer i = new Integer(nd.getRow());
			if(!pre.contains(i)){
		
				pre.add(i);

				for(Iterator it = nd.edges(); it.hasNext();){
					Edge edg = (Edge)it.next();
					Node nd2 = edg.getTargetNode();
					q.add(nd2);
					nd2 = edg.getSourceNode();
					q.add(nd2);

				}
			}
		}

	}

	protected static void runDDFS(final Graph g, Integer n, LinkedHashSet nodeSet, boolean isPreOrder, boolean isReverse){
		boolean done = false;

		boolean[] seen = new boolean[g.getNodeCount()];
		java.util.Arrays.fill(seen, false);
		Stack nodeStack = new Stack();
		nodeStack.add(g.getNode(n.intValue()));
		while(!nodeStack.isEmpty()){
			Node nd = (Node) nodeStack.peek();
			
		
			Integer i = new Integer(nd.getRow());
			if(!seen[i.intValue()]){
				
				if(isPreOrder)
					nodeSet.add(i);
				seen[i.intValue()] = true;;
			}
				done = true;
			
			if(isReverse){
				for(Iterator it = nd.inNeighbors(); it.hasNext();){

					Node nd2 = ((Node)it.next());
					if(!seen[nd2.getRow()]){
						nodeStack.add(nd2);
						done = false;
						break;
					}
				}
			}

			else{
				for(Iterator it = nd.outNeighbors(); it.hasNext();){
					Node nd2 = ((Node)it.next());
					if(!seen[nd2.getRow()]){
					nodeStack.add(nd2);
					done = false;
					break;
				}
			}
			}
			
			if(done){
				if(!isPreOrder)
					nodeSet.add(i);
				nodeStack.pop();
			}
		}
		

	}

	

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.directedInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append(this.nodeInfo());
		
		sb.append(System.getProperty("line.separator"));
		
		sb.append(this.edgeInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append(this.densityInfo());
		
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append(this.connectedInfo());
		return sb.toString();
	}
	
	protected String nodeInfo(){
		StringBuffer sb = new StringBuffer();
		sb.append("nodes: " + this.getNumNodes());
		sb.append(System.getProperty("line.separator"));
		sb.append(this.isolatedNodeInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append("Node Attributes Present");
		sb.append(System.getProperty("line.separator"));
		int numAttributes = this.nodeStats.getNumberOfAttributes();
		for(int i = 0; i < numAttributes; i++){
			sb.append(this.nodeStats.getNodeAttributes()[i]);
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	protected String edgeInfo(){
		StringBuffer sb = new StringBuffer();
		sb.append("edges: " + this.getNumEdges());
		sb.append(System.getProperty("line.separator"));
		sb.append(this.selfLoopInfo());
		sb.append(this.parallelEdgeInfo());
		sb.append("Edge Attributes Present");
		sb.append(System.getProperty("line.separator"));
		int numAttributes = this.edgeStats.numAdditionalAttributes;
		for(int i = 0; i < numAttributes; i++){
			sb.append(this.edgeStats.getAdditionalAttributes()[i]);
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	protected String selfLoopInfo(){
		StringBuffer sb = new StringBuffer();
		if(edgeStats.getNumberOfSelfLoops() > 0){
			sb.append("There are: " + edgeStats.getNumberOfSelfLoops() + " self-loops. \n" + 
			"They are as follows:");
			sb.append(System.getProperty("line.separator"));
			sb.append(edgeStats.getSelfLoopsParallelEdges().printSelfLoops());
			sb.append(System.getProperty("line.separator"));
		}
		else{
			sb.append("No self loops were discovered.");
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	protected String parallelEdgeInfo(){
		StringBuffer sb = new StringBuffer();
		int parallelEdges = edgeStats.getNumberOfParallelEdges();
		if(parallelEdges > 0){
			sb.append("There are: " + parallelEdges + " parallel edges.\n" + 
			"They are as follows:");
			sb.append(System.getProperty("line.separator"));
			sb.append(edgeStats.getSelfLoopsParallelEdges().printParallelEdges());
			sb.append(System.getProperty("line.separator"));
		}
		else{
			sb.append("No parallel edges were discovered.");
			sb.append(System.getProperty("line.separator"));
		}

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

		sb.append("There are " + cf.getWeakComponentClusters() + " weakly connected components.\n");
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
	
	protected String isolatedNodeInfo(){
		StringBuffer sb = new StringBuffer();
		sb.append("isolated nodes: " + this.nodeStats.getNumberOfIsolatedNodes());
		return sb.toString();
	}
	
	
	protected String weightedDensityInfo(){
		StringBuffer sb = new StringBuffer();
		double weightedSum,maxObservedValue,weightedDensity;
		DecimalFormat densityFormatter = new DecimalFormat("#.#####");
		
		if(this.edgeStats.numAdditionalNumericAttributes > 0){
			sb.append(System.getProperty("line.separator"));
		sb.append("Additional Densities by Numeric Attribute");
		sb.append(System.getProperty("line.separator"));
		
		sb.append("densities (weighted against observed max)");
		sb.append(System.getProperty("line.separator"));
		for(int i = 0; i < this.edgeStats.getAdditionalNumericAttributes().length; i++){
			sb.append(this.edgeStats.getAdditionalNumericAttributes()[i]+": ");
		
		
			weightedSum = this.edgeStats.getWeightedDensitySumArray()[i];
			maxObservedValue = this.edgeStats.getMaxValueArray()[i];
			if(this.isDirectedGraph){
				weightedDensity = weightedSum/(maxObservedValue*(this.getNumNodes()*(this.getNumNodes()-1)));
			}
			else{
				double maxConnections = .5 * (this.getNumNodes()*(this.getNumNodes()-1));
				weightedDensity = weightedSum/(maxObservedValue*maxConnections);
			}
			sb.append(densityFormatter.format(weightedDensity));
			sb.append(System.getProperty("line.separator"));
		}
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
