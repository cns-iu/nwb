package edu.iu.cns.graphstream.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;

public class AnnotatedGraph extends DefaultGraph {
	private LinkedHashMap<String, String> nodeSchema;
	private LinkedHashMap<String, String> directedEdgeSchema;
	private LinkedHashMap<String, String> undirectedEdgeSchema;
	private Map<String, String> nodeLabelsByNodeID = new HashMap<String, String>();

	@SuppressWarnings("deprecation")
	public AnnotatedGraph() {
		super();
	}

	@SuppressWarnings("deprecation")
	public AnnotatedGraph(boolean strictChecking, boolean autoCreate) {
		super(strictChecking, autoCreate);
	}

	public AnnotatedGraph(String id) {
		super(id);
	}

	public AnnotatedGraph(String id, boolean strictChecking, boolean autoCreate) {
		super(id, strictChecking, autoCreate);
	}

	public LinkedHashMap<String, String> getNodeSchema() {
		return this.nodeSchema;
	}

	public LinkedHashMap<String, String> getDirectedEdgeSchema() {
		return this.directedEdgeSchema;
	}

	public LinkedHashMap<String, String> getUndirectedEdgeSchema() {
		return this.undirectedEdgeSchema;
	}

	public String getNodeLabel(String nodeID) {
		return this.nodeLabelsByNodeID.get(nodeID);
	}

	public void setNodeSchema(LinkedHashMap<String, String> nodeSchema) {
		this.nodeSchema = nodeSchema;
	}

	public void setDirectedEdgeSchema(LinkedHashMap<String, String> directedEdgeSchema) {
		this.directedEdgeSchema = directedEdgeSchema;
	}

	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> undirectedEdgeSchema) {
		this.undirectedEdgeSchema = undirectedEdgeSchema;
	}

	public void setNodeLabel(String nodeID, String nodeLabel) {
		this.nodeLabelsByNodeID.put(nodeID, nodeLabel);
	}

	public void setNodeAttributes(String nodeID, Map<String, Object> attributes) {
		Node node = getNode(nodeID);
		node.addAttributes(attributes);
	}

	public void setEdgeAttributes(String edgeID, Map<String, Object> attributes) {
		Edge edge = getEdge(edgeID);
		edge.addAttributes(attributes);
	}
}