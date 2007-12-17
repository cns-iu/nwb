package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;


public class NetworkProperties {
	private Graph graph;
	SelfLoopsParallelEdges slpe;
	ComponentForest cf;
	private double density = -1;



	public NetworkProperties(prefuse.data.Graph graph) {
		this.graph = graph ;
		slpe = new SelfLoopsParallelEdges(graph.isDirected());
		cf = new ComponentForest();
		//long time = new Date().getTime();
		calculateConnectedness();
		
		if(!(this.hasParallelEdges() || this.hasSelfLoops()))
			calculateDensity();

		//	time = new Date().getTime() - time;
		//System.out.println("Ran in: " + time);

	}



	public Graph getGraph() {
		return this.graph;
	}

	public int getNumNodes() {
		return this.graph.getNodeCount();
	}

	public boolean isConnected() {
		return this.cf.isWeaklyConnected();
	}

	public boolean isDirected() {
		//return PredicateUtils.enforcesDirected(this.graph);
		return this.graph.isDirected();
	}

	public boolean hasSelfLoops() {
		//return GraphProperties.containsSelfLoops(this.graph);
		return (slpe.getNumSelfLoops() > 0);
	}

	public boolean hasParallelEdges() {
		//return GraphProperties.containsParallelEdges(this.graph);
		return slpe.getNumParallelEdges() > 0;
	}

	public int getNumEdges() {
		return this.graph.getEdgeCount();
	}

	public int getNumLoops() {
		int numLoops = 0;
		for (Iterator iterator = this.graph.edges(); iterator
		.hasNext();) {
			Edge e = (Edge) iterator.next();
			Node source = e.getSourceNode();
			Node target = e.getTargetNode();
			if (source.equals(target))
				++numLoops;
		}
		return numLoops;
	}

	public int getNumParallelEdges(){
		return 1;
	}

	public Edge[] getSelfLoops(){
		ArrayList edges = new ArrayList();

		for(Iterator it = this.graph.edges(); it.hasNext();){
			Edge edg = (Edge)it.next();

			if(edg.getSourceNode().getRow() == edg.getTargetNode().getRow()){
				edges.add(edg);
			}
		}


		Edge[] edgs = new Edge[edges.size()];

		return (Edge[])edges.toArray(edgs);
	}

	//private static Tree[] 

	protected LinkedHashSet uDFS(final Graph g, Integer n){

		LinkedHashSet preOrder = new LinkedHashSet();


		runUDFS(g,n, preOrder);


		return preOrder;
	}




	protected LinkedHashSet dDFS(final Graph g, Integer n,boolean isReverse, boolean getPreOrder){
		LinkedHashSet nodeSet = new LinkedHashSet();
		//LinkedHashSet postOrder = new LinkedHashSet();

		runDDFS(g,n,nodeSet,getPreOrder,isReverse);
		

		return nodeSet;
	}

	protected void runUDFS(final Graph g, Integer n, LinkedHashSet pre){
		Queue q = new LinkedList();
		q.add(g.getNode(n.intValue()));

		while(!q.isEmpty()){
			Node nd = (Node)q.poll();
			Integer i = new Integer(nd.getRow());
			if(!pre.contains(i)){
				//  System.out.println(nd);
				pre.add(i);


				for(Iterator it = nd.edges(); it.hasNext();){
					Edge edg = (Edge)it.next();
					slpe.addEdge(edg);
					Node nd2 = edg.getTargetNode();
					q.add(nd2);
					nd2 = edg.getSourceNode();
					q.add(nd2);

				}
			}
		}

	}

	protected void runDDFS(final Graph g, Integer n, LinkedHashSet nodeSet, boolean isPreOrder, boolean isReverse){
		boolean done = false;

		boolean[] seen = new boolean[g.getNodeCount()];
		java.util.Arrays.fill(seen, false);
		Stack nodeStack = new Stack();
		nodeStack.add(g.getNode(n.intValue()));
		while(!nodeStack.isEmpty()){
			Node nd = (Node) nodeStack.peek();
			
			//Node nd = g.getNode(n.intValue());
			Integer i = new Integer(nd.getRow());
			if(!seen[i.intValue()]){
				//System.out.println(nd);
				if(isPreOrder)
					nodeSet.add(i);
				seen[i.intValue()] = true;;
			}
				done = true;
			//{
			//	prepre[i.intValue()] = preCount;

			//System.out.println(nd);
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
			//System.out.println(nodeStack.size());
		}
		

	}

	private void calculateConnectedness(){

		cf.weakComponentCalculation(this.graph, this);		
		
		if(this.graph.isDirected()){
			cf.strongComponentCalculation(this.graph, this);
		}
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.directedInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append(this.nodeAndEdgeInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append(this.densityInfo());
		
		sb.append(this.selfLoopInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append(this.parallelEdgeInfo());
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append(this.connectedInfo());
		return sb.toString();
	}

	protected String nodeAndEdgeInfo(){
		StringBuffer sb = new StringBuffer();
		//sb.append(System.getProperty("line.separator")+"Results:\n");
		sb.append("nodes: " + this.graph.getNodeCount());
		sb.append(System.getProperty("line.separator"));
		sb.append("edges " + this.graph.getEdgeCount());
		return sb.toString();
	}

	protected String selfLoopInfo(){
		StringBuffer sb = new StringBuffer();
		if(slpe.getNumSelfLoops() > 0){
			sb.append("There are: " + slpe.getNumSelfLoops() + " self-loops. \n" + 
			"They are as follows:");
			sb.append(System.getProperty("line.separator"));
			sb.append(slpe.printSelfLoops());
			sb.append(System.getProperty("line.separator"));
			//sb.append(System.getProperty("line.separator"));
		}
		else{
			sb.append("No self loops were discovered.");
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	protected String parallelEdgeInfo(){
		StringBuffer sb = new StringBuffer();
		if(slpe.getNumParallelEdges() > 0){
			sb.append("There are: " + slpe.getNumParallelEdges() + " parallel edges.\n" + 
			"They are as follows:");
			sb.append(System.getProperty("line.separator"));
			sb.append(slpe.printParallelEdges());
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
		if(this.graph.isDirected()){
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
			sb.append(System.getProperty("line.separator"));
		}


		return sb.toString();
	}

	protected void calculateDensity(){
		long maxEdges = this.graph.getNodeCount()* (this.graph.getNodeCount()-1);
		//double density;
		if(this.isDirected()){
			density = (double)this.getNumEdges()/(maxEdges);
		}
		else{
			density = (double)this.getNumEdges()/(maxEdges/2);
		}
		//System.out.println(density);
	}


}
