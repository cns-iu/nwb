package edu.iu.cns.graphstream.common;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class AnnotatedGraph extends MultiGraph {
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

	@SuppressWarnings("deprecation")
	public AnnotatedGraph(AnnotatedGraph copyFrom) {
		super();
		setupFrom(copyFrom);
	}

	@SuppressWarnings("deprecation")
	public AnnotatedGraph(AnnotatedGraph copyFrom, boolean strictChecking, boolean autoCreate) {
		super(strictChecking, autoCreate);
		setupFrom(copyFrom);
	}

	public AnnotatedGraph(AnnotatedGraph copyFrom, String id) {
		super(id);
		setupFrom(copyFrom);
	}

	public AnnotatedGraph(
			AnnotatedGraph copyFrom, String id, boolean strictChecking, boolean autoCreate) {
		super(id, strictChecking, autoCreate);
		setupFrom(copyFrom);
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

		if (attributes != null) {
			node.addAttributes(attributes);
		}
	}

	public void setEdgeAttributes(String edgeID, Map<String, Object> attributes) {
		Edge edge = getEdge(edgeID);

		if (attributes != null) {
			edge.addAttributes(attributes);
		}
	}

	private void setupFrom(AnnotatedGraph copyFrom) {
		setupNodeSchemaFrom(copyFrom);
		setupDirectedEdgeSchemaFrom(copyFrom);
		setupUndirectedEdgeSchemaFrom(copyFrom);

		for (Node node : copyFrom.getEachNode()) {
			String id = node.getId();
			String label = copyFrom.getNodeLabel(id);
			Map<String, Object> attributes = Utilities.getElementAttributes(node);

			addNode(id);
			setNodeLabel(id, label);
			setNodeAttributes(id, attributes);
		}

		for (Edge edge : copyFrom.getEachEdge()) {
			String id = edge.getId();
			String node1ID = edge.getNode0().getId();
			String node2ID = edge.getNode1().getId();
			boolean isDirected = edge.isDirected();
			Map<String, Object> attributes = Utilities.getElementAttributes(edge);

			addEdge(id, node1ID, node2ID, isDirected);
			setEdgeAttributes(id, attributes);
		}
	}

	private void setupNodeSchemaFrom(AnnotatedGraph copyFrom) {
		LinkedHashMap<String, String> nodeSchema = copyFrom.getNodeSchema();

		if (nodeSchema != null) {
			setNodeSchema(new LinkedHashMap<String, String>(nodeSchema));
		}
	}

	private void setupDirectedEdgeSchemaFrom(AnnotatedGraph copyFrom) {
		LinkedHashMap<String, String> directedEdgeSchema = copyFrom.getDirectedEdgeSchema();

		if (directedEdgeSchema != null) {
			setDirectedEdgeSchema(new LinkedHashMap<String, String>(directedEdgeSchema));
		}
	}

	private void setupUndirectedEdgeSchemaFrom(AnnotatedGraph copyFrom) {
		LinkedHashMap<String, String> undirectedEdgeSchema = copyFrom.getUndirectedEdgeSchema();

		if (undirectedEdgeSchema != null) {
			setUndirectedEdgeSchema(new LinkedHashMap<String, String>(undirectedEdgeSchema));
		}
	}
}