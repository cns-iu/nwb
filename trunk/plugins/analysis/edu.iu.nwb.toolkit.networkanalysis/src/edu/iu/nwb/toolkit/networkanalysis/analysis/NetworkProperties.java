package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;

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
		if(!(this.hasParallelEdges() && this.hasSelfLoops()))
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




	protected static LinkedHashSet dDFS(final Graph g, Integer n){
		LinkedHashSet preOrder = new LinkedHashSet();
		LinkedHashSet postOrder = new LinkedHashSet();

		runDDFS(g,n,preOrder,postOrder);
		return preOrder;
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

	protected static void runDDFS(final Graph g, Integer n, LinkedHashSet pre, LinkedHashSet post){


		Queue q = new LinkedList();
		q.add(g.getNode(n.intValue()));
		while(!q.isEmpty()){
			Node nd = (Node) q.poll();
			//Node nd = g.getNode(n.intValue());
			Integer i = new Integer(nd.getRow());
			if(!pre.contains(i)){
				pre.add(i);
				//{
				//	prepre[i.intValue()] = preCount;

				//System.out.println(nd);

				for(Iterator it = nd.neighbors(); it.hasNext();){
					//Node nd = ((Edge)it.next()).getTargetNode();
					Node nd2 = ((Node)it.next());
					runDDFS(g,new Integer(nd2.getRow()),pre, post);
					//q.add(nd2);
				}




			}
			//	post[i.intValue()] = postCount;		

			post.add(i);

		}

	}

	private void calculateConnectedness(){

		cf.weakComponentCalculation(this.graph, this);		
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.nodeAndEdgeInfo());
		sb.append(this.densityInfo());
		sb.append(this.directedInfo());
		sb.append(this.selfLoopInfo());
		sb.append(this.parallelEdgeInfo());
		sb.append(this.connectedInfo());
		return sb.toString();
	}
	
	protected String nodeAndEdgeInfo(){
		StringBuffer sb = new StringBuffer();
		sb.append(System.getProperty("line.separator")+"Results:\n");
		sb.append("This graph has " + this.graph.getNodeCount() + " nodes");
		sb.append(System.getProperty("line.separator"));
		sb.append("This graph has " + this.graph.getEdgeCount() + " edges");
		sb.append(System.getProperty("line.separator"));
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
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
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

		sb.append("There are " + cf.getComponentClusters() + " weakly connected components.\n");
		sb.append("The largest connected component consists of " + cf.getMaximumConnectedNodes()+ " nodes.\n");
		return sb.toString();
	}
	
	protected String densityInfo(){
		StringBuffer sb = new StringBuffer();
		if(density > -1){
			sb.append("Graph Density (disregarding weights): " + density);
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
