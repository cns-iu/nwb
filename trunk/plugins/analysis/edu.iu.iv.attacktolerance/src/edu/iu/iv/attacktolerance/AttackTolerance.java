package edu.iu.iv.attacktolerance;

import java.util.Iterator;

import edu.uci.ics.jung.algorithms.importance.DegreeDistributionRanker;
import edu.uci.ics.jung.algorithms.importance.NodeRanking;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.Indexer;

/**
 * Perform Attack Tolerance Test on the network graph. Randomly deletes nodes in the graph.
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class AttackTolerance {
	private int numNodes;
	private Graph graph;
	
	/**
	 * Constructor for AttackTolerance.
	 * Requires a graph to be passed that represents the network and the number of highly connected nodes to be deleted. 
	 */
	public AttackTolerance(Graph graph, int numNodes) {
		this.graph = graph;
		this.numNodes = numNodes;
		Indexer.newIndexer(graph, 0);
	}
	
	/**
	 * Perform attack tolerance test on the graph.
	 * @return true when test done.
	 */
	public boolean testAttackTolerance() {
		int cnt = 0;
		
		DegreeDistributionRanker ranker = new DegreeDistributionRanker(graph);
		ranker.evaluate();
		
		Iterator nodeRankingList = ranker.getRankings().iterator();
		
		while (cnt < numNodes) {
			/* Select a highly-connected node. Deletes all its edges and then delete the node. */	
			Vertex v = ((NodeRanking)nodeRankingList.next()).vertex;
			graph.removeEdges(v.getIncidentEdges());
			graph.removeVertex(v);
						
			/* Increment count if node deleted from the graph */
			cnt++;
		}
		
		return true;
	} // end of testAttackTolerance
	
	/**
	 * Get the graph representing the network
	 * @return graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * @return The number of highly-connected nodes that are deleted
	 */
	public int getNumNodes() {
		return numNodes;
	}

	/**
	 * Set the graph representing the network
	 * @param graph
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Set the number of highly-connected nodes that are deleted
	 * @param i
	 */
	public void setNumNodes(int i) {
		numNodes = i;
	}

}
