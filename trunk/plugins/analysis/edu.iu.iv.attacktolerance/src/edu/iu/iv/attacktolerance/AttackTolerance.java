package edu.iu.iv.attacktolerance;

import java.util.Iterator;

import edu.uci.ics.jung.algorithms.importance.DegreeDistributionRanker;
import edu.uci.ics.jung.algorithms.importance.NodeRanking;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

public class AttackTolerance {

	/**
	 * Perform an attack tolerance test on the graph.
	 * 
	 * @return a Graph with 'numNodesToDelete' of its highest degree nodes deleted. 
	 * Edges associated with deleted nodes will be removed as well.
	 */
	public static Graph testAttackTolerance(final Graph graph, int numNodesToDelete) {

		Graph graphToAttack = (Graph) graph.copy();

		DegreeDistributionRanker rankByDegree = new DegreeDistributionRanker(graphToAttack);
		rankByDegree.evaluate();

		Iterator nodesByDegree = rankByDegree.getRankings().iterator();
		for (int numNodesDeleted = 0;
			numNodesDeleted < numNodesToDelete && nodesByDegree.hasNext();
			numNodesDeleted++) {

			Vertex v = ((NodeRanking) nodesByDegree.next()).vertex;

			graphToAttack.removeVertex(v);
		}

		return graphToAttack;
	}
}
