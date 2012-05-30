package edu.iu.sci2.visualization.bipartitenet.model;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class EdgeHandlerTest {
	static class FakeNodeProvider implements NodeProvider {
		private ImmutableMap<Integer, Node> nodes;

		public FakeNodeProvider(ImmutableMap<Integer, Node> nodes) {
			super();
			this.nodes = nodes;
		}
		
		public static FakeNodeProvider withNodes(Node... nodes) {
			ImmutableMap.Builder<Integer, Node> builder = ImmutableMap.builder();
			
			int id = 1;
			for (Node n : nodes) {
				builder.put(id++, n);
			}
			
			return new FakeNodeProvider(builder.build());
		}

		@Override
		public Node getNodeById(int id) {
			return nodes.get(id);
		}
	}

	@Test
	public void testCreation() {
		FakeNodeProvider np = FakeNodeProvider.withNodes();
		new EdgeHandler("hi", np);
	}

	@Test
	public void testSingleEdge() {
		Node leftNode = new Node("left", 1, NodeDestination.LEFT);
		Node rightNode = new Node("right", 1, NodeDestination.RIGHT);
		FakeNodeProvider np = FakeNodeProvider.withNodes(
				leftNode,
				rightNode);
		EdgeHandler eh = new EdgeHandler(null, np);
		LinkedHashMap<String, String> edgeSchema = Maps.newLinkedHashMap();
		eh.setDirectedEdgeSchema(edgeSchema);
		eh.addDirectedEdge(1, 2, Maps.<String,Object>newLinkedHashMap());
		
		ImmutableList<Edge> edges = eh.getEdges();
		assertEquals(1, edges.size());
		Edge e = edges.get(0);
		assertEquals(leftNode, e.getLeftNode());
		assertEquals(rightNode, e.getRightNode());
	}

}
