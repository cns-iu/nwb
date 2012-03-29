package edu.iu.nwb.util.nwbfile;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Objects;


public class ForwardingNWBHandler implements NWBFileParserHandler {
	private final NWBFileParserHandler delegate;
	
	public static ForwardingNWBHandler create(NWBFileParserHandler delegate) {
		return new ForwardingNWBHandler(delegate);
	}
	
	protected ForwardingNWBHandler(NWBFileParserHandler delegate) {
		this.delegate = delegate;
	}

	public void setNodeCount(int numberOfNodes) {
		delegate.setNodeCount(numberOfNodes);
	}

	public void setNodeSchema(LinkedHashMap<String, String> schema) {
		delegate.setNodeSchema(schema);
	}

	public void addNode(int id, String label, Map<String, Object> attributes) {
		delegate.addNode(id, label, attributes);
	}

	public void setDirectedEdgeCount(int numberOfEdges) {
		delegate.setDirectedEdgeCount(numberOfEdges);
	}

	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		delegate.setDirectedEdgeSchema(schema);
	}

	public void addDirectedEdge(int sourceNode, int targetNode,
			Map<String, Object> attributes) {
		delegate.addDirectedEdge(sourceNode, targetNode, attributes);
	}

	public void setUndirectedEdgeCount(int numberOfEdges) {
		delegate.setUndirectedEdgeCount(numberOfEdges);
	}

	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		delegate.setUndirectedEdgeSchema(schema);
	}

	public void addUndirectedEdge(int node1, int node2,
			Map<String, Object> attributes) {
		delegate.addUndirectedEdge(node1, node2, attributes);
	}

	public void addComment(String comment) {
		delegate.addComment(comment);
	}

	public void finishedParsing() {
		delegate.finishedParsing();
	}

	public boolean haltParsingNow() {
		return delegate.haltParsingNow();
	}

	public String toString() {
		return Objects.toStringHelper(this)
			.add("next", delegate)
			.toString();
	}
}
