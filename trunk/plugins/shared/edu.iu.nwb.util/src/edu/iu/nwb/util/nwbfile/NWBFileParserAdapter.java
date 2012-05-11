package edu.iu.nwb.util.nwbfile;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Adapter version of NWBFileParserHandler
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public class NWBFileParserAdapter implements NWBFileParserHandler {
	@Override
	public void setNodeCount(int numberOfNodes) {
		// do nothing.
	}

	@Override
	public void setNodeSchema(LinkedHashMap<String, String> schema) {
		// do nothing.
	}

	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		// do nothing.
	}

	@Override
	public void setDirectedEdgeCount(int numberOfEdges) {
		// do nothing.
	}

	@Override
	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		// do nothing.
	}

	@Override
	public void addDirectedEdge(int sourceNode, int targetNode, Map<String, Object> attributes) {
		// do nothing.
	}

	@Override
	public void setUndirectedEdgeCount(int numberOfEdges) {
		// do nothing.
	}

	@Override
	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		// do nothing.
	}

	@Override
	public void addUndirectedEdge(int node1, int node2, Map<String, Object> attributes) {
		// do nothing.
	}

	@Override
	public void addComment(String comment) {
		// do nothing.
	}

	@Override
	public void finishedParsing() {
		// do nothing.
	}

	@Override
	public boolean haltParsingNow() {
		return false;
	}
}

