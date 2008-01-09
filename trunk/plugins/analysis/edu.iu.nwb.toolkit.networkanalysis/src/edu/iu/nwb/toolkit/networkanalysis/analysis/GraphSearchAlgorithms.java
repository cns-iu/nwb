package edu.iu.nwb.toolkit.networkanalysis.analysis;

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
}
