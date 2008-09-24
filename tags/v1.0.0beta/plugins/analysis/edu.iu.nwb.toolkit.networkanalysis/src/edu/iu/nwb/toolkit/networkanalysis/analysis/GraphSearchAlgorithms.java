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

		LinkedHashSet nodeSet = new LinkedHashSet();
		Integer nodeNumber;
		if(n == null){  //If no node number is presented, search the whole graph.
			for(Iterator it = g.nodes(); it.hasNext();){
				nodeNumber = new Integer(((Node)it.next()).getRow());
				runUDFS(g,nodeNumber,nodeSet);
			}
		}
		else{  //Otherwise, just search the specific node.
			nodeNumber = new Integer(n.intValue());
			runUDFS(g,nodeNumber, nodeSet);
		}

		return nodeSet;
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
		
		Node nd;
		Integer nodeRow;
		Integer nodeNumber;
		Edge edg;
		Node nd2;
		q.add(new Integer(n.intValue()));
		while(!q.isEmpty()){
			nodeRow = new Integer(((Integer)q.poll()).intValue());
			if(!pre.contains(nodeRow)){
				nd = g.getNode(nodeRow.intValue());
				pre.add(nodeRow);

				for(Iterator it = nd.edges(); it.hasNext();){
					edg = (Edge)it.next();
					nd2 = edg.getTargetNode();
					nodeNumber = new Integer(nd2.getRow());
					if(!pre.contains(nodeNumber))
						q.add(nodeNumber);
					nd2 = edg.getSourceNode();
					nodeNumber = new Integer(nd2.getRow());
					if(!pre.contains(nodeNumber))
						q.add(nodeNumber);

				}
			}
		}
		nodeRow = null;
		nodeNumber = null;
		q = null;
		edg = null;
		nd2 = null;

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

		Node nd;
		Node nd2;
		Integer nodeRow;
		Integer nodeNumber;

		Stack nodeStack = new Stack();

		nodeStack.add(new Integer(n.intValue()));
		while(!nodeStack.isEmpty()){
			nodeRow = (Integer)nodeStack.peek();
			nd = g.getNode(nodeRow.intValue());
			if(!nodeSet.contains(nodeRow)){
				if(isPreOrder)
					nodeSet.add(nodeRow);
				nodeSet.add(nodeRow);
			}
			done = true;
			for(Iterator it = nd.outNeighbors(); it.hasNext();){
				nd2 = ((Node)it.next());
				nodeNumber = new Integer(nd2.getRow());
				if(!nodeSet.contains(nodeNumber)){
					nodeStack.add(nodeNumber);
					done = false;
					break;
				}
			}
			if(done){
				if(!isPreOrder)
					nodeSet.add(nodeRow);
				nodeStack.pop();
			}
		}
		nodeStack = null;
		nd = null;
		nd2 = null;
		nodeRow = null;
		nodeNumber = null;
	}
}
