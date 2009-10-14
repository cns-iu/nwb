package edu.iu.nwb.toolkit.networkanalysis.analysis;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Stack;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;

/**
 * @see edu.iu.nwb.preprocessing.duplicatenodedetector.util.GraphSearchAlgorithms
 */
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

	private static void runUDFS(
			final Graph graph,
			Integer startingNodeIndex,
			LinkedHashSet alreadyCheckedNodes) {
		LinkedList queue = new LinkedList();

		queue.add(startingNodeIndex);

		while (!queue.isEmpty()) {
			Integer nodeRow = (Integer)queue.removeFirst();

			if (!alreadyCheckedNodes.contains(nodeRow)) {
				Node node = graph.getNode(nodeRow.intValue());
				alreadyCheckedNodes.add(nodeRow);

				for (Iterator edgeIt = node.edges(); edgeIt.hasNext();) {
					Edge edge = (Edge) edgeIt.next();

					Node target = edge.getTargetNode();
					Integer targetRow = new Integer(target.getRow());
					if (!alreadyCheckedNodes.contains(targetRow)) {
						queue.add(targetRow);
					}

					Node source = edge.getSourceNode();
					Integer sourceRow = new Integer(source.getRow());
					if (!alreadyCheckedNodes.contains(sourceRow)) {
						queue.add(sourceRow);
					}
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
