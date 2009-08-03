/*
 * Created on May 9, 2004
 */
package edu.iu.nwb.converter.jungprefuse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.berkeley.guir.prefuse.graph.DefaultNode;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

/**
 * Takes in a Graph, and returns a Prefuse Graph. The Prefuse Graph will be of
 * the same topology as the JUNG graph, but will be a Prefuse JungGraph extends
 * DefaultGraph object. Its vertices and edges will all be of type JungVertex
 * extends DefaultVertex and JungEdge extends DefaultEdge. The Jung* classes
 * contain a field that points back to the original JUNG object.
 * 
 * @author danyelf
 */
public class JungPrefuseConverter {
	public static class JungGraph extends DefaultGraph {
		public final Graph jungGraph;

		public JungGraph(Graph g, Collection collection, boolean b) {
			super(collection, b);
			this.jungGraph = g;
		}
	}

	public static class JungNode extends DefaultNode {
		public final Vertex jungVertex;

		public JungNode(Vertex v) {
			this.jungVertex = v;

			Iterator it = v.getUserDatumKeyIterator();
			while (it.hasNext()) {
				Object key = it.next();
				Object value = v.getUserDatum(key);

				key = key == null ? null : key.toString();
				value = value == null ? null : value.toString();

				setAttribute((String) key, (String) value);
			}
		}
	}

	public static class JungEdge extends DefaultEdge {
		public final Edge jungEdge;

		public JungEdge(Edge jungEdge, Node n1, Node n2, boolean directed) {
			super(n1, n2, directed);
			this.jungEdge = jungEdge;

			Iterator it = jungEdge.getUserDatumKeyIterator();
			while (it.hasNext()) {
				Object key = it.next();
				Object value = jungEdge.getUserDatum(key);

				key = key == null ? null : key.toString();
				value = value == null ? null : value.toString();

				setAttribute((String) key, (String) value);
			}
		}
	}

	public static JungGraph getPrefuseGraph(Graph g) {
		boolean isDirected = false;
		Map jungToPrefuseVertices = new HashMap();

		for (Iterator it = g.getVertices().iterator(); it.hasNext();) {
			Vertex v = (Vertex) it.next();
			jungToPrefuseVertices.put(v, new JungNode(v));
		}

		for (Iterator iter = g.getEdges().iterator(); iter.hasNext();) {
			Edge e = (Edge) iter.next();
			JungNode node1 =
				(JungNode) jungToPrefuseVertices.get(
						e.getEndpoints().getFirst());
			JungNode node2 =
				(JungNode) jungToPrefuseVertices.get(
						e.getEndpoints().getSecond());
			boolean directedEdge = Graph.DIRECTED_EDGE.evaluate(e);
			if (directedEdge) {
				isDirected = true;
			}

			JungEdge edgeOut = new JungEdge(e, node1, node2, directedEdge);
			node1.addEdge(edgeOut);
		}

		JungGraph rv =
			new JungGraph(g, jungToPrefuseVertices.values(), isDirected);

		return rv;
	}
}
