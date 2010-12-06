package edu.iu.cns.converter.nwb_graphstream;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.iu.cns.graphstream.common.AnnotatedGraph;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NWBIntoAnnotatedGraphFileParser extends NWBFileParserAdapter {
	private AnnotatedGraph graph = new AnnotatedGraph();
	private int edgeCount = 0;

	public AnnotatedGraph getGraph() {
		return this.graph;
	}

	@Override
	public void setNodeSchema(LinkedHashMap<String, String> nodeSchema) {
		super.setNodeSchema(nodeSchema);
		this.graph.setNodeSchema(nodeSchema);
	}

	@Override
	public void setDirectedEdgeSchema(LinkedHashMap<String, String> directedEdgeSchema) {
		super.setDirectedEdgeSchema(directedEdgeSchema);
		this.graph.setDirectedEdgeSchema(directedEdgeSchema);
	}

	@Override
	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> undirectedEdgeSchema) {
		super.setUndirectedEdgeSchema(undirectedEdgeSchema);
		this.graph.setUndirectedEdgeSchema(undirectedEdgeSchema);
	}

	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		String nodeID = Integer.toString(id);
		this.graph.addNode(nodeID);
		this.graph.setNodeLabel(nodeID, label);
		this.graph.setNodeAttributes(nodeID, attributes);
	}

	@Override
	public void addDirectedEdge(int sourceNode, int targetNode, Map<String, Object> attributes) {
		String node1ID = Integer.toString(sourceNode);
		String node2ID = Integer.toString(targetNode);
		String edgeID = createEdgeID(node1ID, node2ID);
		this.graph.addEdge(edgeID, node1ID, node2ID, true);
		this.graph.setEdgeAttributes(edgeID, attributes);
	}

	@Override
	public void addUndirectedEdge(int sourceNode, int targetNode, Map<String, Object> attributes) {
		String node1ID = Integer.toString(sourceNode);
		String node2ID = Integer.toString(targetNode);
		String edgeID = createEdgeID(node1ID, node2ID);
		this.graph.addEdge(edgeID, node1ID, node2ID, false);
		this.graph.setEdgeAttributes(edgeID, attributes);
	}

	private String createEdgeID(String node1ID, String node2ID) {
		String edgeID = Integer.toString(this.edgeCount);
		this.edgeCount++;

		return edgeID;
	}
}