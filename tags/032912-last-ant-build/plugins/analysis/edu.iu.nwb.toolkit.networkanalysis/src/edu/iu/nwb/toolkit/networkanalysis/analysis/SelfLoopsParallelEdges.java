package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;

public class SelfLoopsParallelEdges {
	
	
	int numberOfSelfLoops = 0;
	int numberOfParallelEdges = 0;
	
	boolean directed = false;
	
	String selfLoopInfo;
	String parallelEdgeInfo;
	

	public SelfLoopsParallelEdges(final Graph g){
		HashSet selfLoops = new HashSet();
		HashSet parallelEdges = new HashSet();
		HashMap edges = new HashMap();
		boolean directed = g.isDirected();
		Edge edg;
		
		for(Iterator it = g.edges(); it.hasNext();){
			edg = (Edge)it.next();
			
			this.addEdge(edg, directed, selfLoops, parallelEdges, edges);
		}
		
		selfLoopInfo = this.calculateSelfLoops(selfLoops);
		parallelEdgeInfo = this.calculateParallelEdges(parallelEdges, edges);
		
		selfLoops = null;
		edges = null;
		parallelEdges = null;
		edg = null;

	}

	/***
	 * Here we take an Edge e, create a SimpleEdge object, which only stores
	 * the source and target ids. We create a HashSet to hold edges.
	 * We then link the SimpleEdge to the HashSet of real edges.
	 * @param e the edge to check
	 */
	
	
	public void addEdge(Edge e, boolean isDirected, HashSet selfLoops, HashSet parallelEdges, HashMap edges){
		SimpleEdge se = new SimpleEdge(e);

		if(isDirected){

			if(edges.get(se) == null){ //We haven't seen this source and target
				HashSet temp = new HashSet(); //create the HashSet to hold real edges.
				temp.add(e);
				edges.put(se, temp); //link real edges to source and target.
				
			}
			else{ //we've seen this source and target so we have a parallel edge.
				if(((HashSet)edges.get(se)).add(e)){ //add the real edge to the mapping, 
													//only if it is unique.
					
					parallelEdges.add(se); 
					this.numberOfParallelEdges++;
				}

			}	
		}
		else{
			//create a simple edge with the source and target swapped to handle undirected.
			SimpleEdge se2 = new SimpleEdge(e.getTargetNode().getRow(), e.getSourceNode().getRow());

			if((edges.get(se) == null) && (edges.get(se2) == null)){
				HashSet temp = new HashSet();
				temp.add(e);
				edges.put(se, temp);
			}
			else if((edges.get(se) != null)){
				if(((HashSet)edges.get(se)).add(e)){

					parallelEdges.add(se);
					this.numberOfParallelEdges++;
				}
			}
			else{
				if(((HashSet)edges.get(se2)).add(e)){
					this.numberOfParallelEdges++;
					parallelEdges.add(se2);
				}
			}

		}
		//if the source and target are the same, add a selfLoop.
		if(e.getSourceNode().getRow() == e.getTargetNode().getRow()){
			
			selfLoops.add(e);
			this.numberOfSelfLoops++;
			
			
		}
	}


	public int getNumSelfLoops(){
		return this.numberOfSelfLoops;
	}

	public int getNumParallelEdges(){


		return this.numberOfParallelEdges;
		
	
	}

	private String calculateParallelEdges(HashSet parallelEdges, HashMap edges){
		StringBuffer sb = new StringBuffer();
		for(Iterator it1 = parallelEdges.iterator();it1.hasNext();){
			SimpleEdge se = (SimpleEdge)it1.next();
			sb.append("There are " + ((HashSet)edges.get(se)).size() + " edges between node: \n\t");
			Iterator it2 = ((HashSet)edges.get(se)).iterator();
			Edge edg = (Edge)it2.next();
			Node nd = edg.getSourceNode();
			int columns = nd.getColumnCount();
			for(int i = 0; i < columns; i++){
				sb.append(nd.get(i) + " ");
			}
			sb.append(System.getProperty("line.separator"));
			sb.append("and node: \n\t");
			nd = edg.getTargetNode();
			columns = nd.getColumnCount();
			for(int i = 0; i < columns; i++){
				sb.append(nd.get(i) + " ");
			}
			sb.append(System.getProperty("line.separator"));
			//columns = edg.getSourceNode().getColumnCount();

			//sb.append(System.getProperty("line.separator"));


		}
		
		return sb.toString();
	}

	private String calculateSelfLoops(HashSet selfLoops){
		
		StringBuffer sb = new StringBuffer();
		for(Iterator it = selfLoops.iterator(); it.hasNext();){
			Edge edg = (Edge)it.next();
			
			Node nd = edg.getSourceNode();
			
			//sb.append((Edge)it.next());
			sb.append("The node: \n\t");
			for(int i = 0; i < nd.getColumnCount(); i++){
				sb.append(nd.get(i) + " ");
			}
			sb.append("\nhas an edge with itself.");
			sb.append(System.getProperty("line.separator"));

		}
		
		return sb.toString();
	}
	
	public String printSelfLoops(){
		return this.selfLoopInfo;
	}
	
	public String printParallelEdges(){
		return this.parallelEdgeInfo;
	}
	

}
