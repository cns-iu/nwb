package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;

public class GraphSearchAlgorithms {
	protected static LinkedHashSet undirectedDepthFirstSearch(final Graph g, Integer n){

		LinkedHashSet preOrder = new LinkedHashSet();


		runUDFS(g,n, preOrder);


		return preOrder;
	}




	protected static LinkedHashSet directedDepthFirstSearch(final Graph g, Integer n, boolean getPreOrder, boolean isReverse){
		LinkedHashSet nodeSet = new LinkedHashSet();
		Graph g2 = g;
		if(isReverse){
			
			
			if(isReverse){
				g2 = reverseGraph(g);
			}
		}

		if(n == null){
			for(Iterator it = g2.nodes(); it.hasNext();){
				Integer nodeNumber = new Integer(((Node)it.next()).getRow());
				runDDFS(g2,nodeNumber,nodeSet,getPreOrder);
			}
		}
		else{
			runDDFS(g2,n,nodeSet,getPreOrder);
		}
		
		
		
		
		

		return nodeSet;
	}

	private static void runUDFS(final Graph g, Integer n, LinkedHashSet pre){
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
	
	public static Graph reverseGraph(final Graph g){
		Graph g2 = g;
		for(Iterator it = g.edges(); it.hasNext();){
			
			Edge e = (Edge)it.next();
			
			int edgeRow = e.getRow();
			e = g2.getEdge(edgeRow);
			int newTarget = e.getSourceNode().getRow();
			int newSource = e.getTargetNode().getRow();
			
			
			e.set(0, new Integer(newSource));
			e.set(1, new Integer(newTarget));
		
			
		}
		
		return g2;
		
	}
	

	private static void runDDFS(final Graph g, Integer n, LinkedHashSet nodeSet, boolean isPreOrder){
		boolean done = false;
		
		
		
		HashSet seen = new HashSet();
		seen.addAll(nodeSet);
		
		Stack nodeStack = new Stack();
		nodeStack.add(g.getNode(n.intValue()));
		while(!nodeStack.isEmpty()){
			Node nd = (Node) nodeStack.peek();
			
		
			Integer i = new Integer(nd.getRow());
			if(!seen.contains(i)){
				
				if(isPreOrder)
					nodeSet.add(i);
				seen.add(i);
			}
				done = true;
			
			
				for(Iterator it = nd.outNeighbors(); it.hasNext();){
					Node nd2 = ((Node)it.next());
					if(!seen.contains(new Integer(nd2.getRow()))){
					nodeStack.add(nd2);
					done = false;
					break;
				}
			}
			
			
			if(done){
				if(!isPreOrder)
					nodeSet.add(i);
				nodeStack.pop();
			}
		}
		

	}
}
