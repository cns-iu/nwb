package edu.iu.iv.attacktolerance;

import java.util.Iterator;

import edu.uci.ics.jung.algorithms.importance.DegreeDistributionRanker;
import edu.uci.ics.jung.algorithms.importance.NodeRanking;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

/**
 * Perform Attack Tolerance Test on the network graph. Randomly deletes nodes in the graph.
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class AttackTolerance {
	/**
	 * Perform attack tolerance test on the graph.
	 * @return true when test done.
	 */
	public static Graph testAttackTolerance(final Graph graph, int numberOfDeletedNodes) {
		int cnt = 0;
		Graph attackGraph = (Graph) graph.copy();
		
		DegreeDistributionRanker ranker = new DegreeDistributionRanker(attackGraph);
		ranker.evaluate();
		
		Iterator nodeRankingList = ranker.getRankings().iterator();
		
		while (cnt < numberOfDeletedNodes && nodeRankingList.hasNext()) {
			/* Select a highly-connected node. Deletes all its edges and then delete the node. */	
			Vertex v = ((NodeRanking)nodeRankingList.next()).vertex;
            
			attackGraph.removeVertex(v);
						
			/* Increment count if node deleted from the graph */
			cnt++;
		}
		
		return attackGraph;
	} // end of testAttackTolerance
	
}
