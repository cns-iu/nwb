package edu.iu.nwb.util.nwbfile.pipe;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

class GuardParserHandler implements NWBFileParserHandler {
	// Singleton
	private static GuardParserHandler instance = new GuardParserHandler();
	private GuardParserHandler() {
		// nuthin'
	}
	static GuardParserHandler getInstance() {
		return instance;
	}
	
	private void throwAssertionError() {
		throw new AssertionError("A ParserPipe was used without a real output stream");
	}
	
	public void setNodeCount(int numberOfNodes) {
		throwAssertionError();
	}

	public void setNodeSchema(LinkedHashMap<String, String> schema) {
		throwAssertionError();
	}

	public void addNode(int id, String label, Map<String, Object> attributes) {
		throwAssertionError();
	}

	public void setDirectedEdgeCount(int numberOfEdges) {
		throwAssertionError();
	}

	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		throwAssertionError();
	}

	public void addDirectedEdge(int sourceNode, int targetNode,
			Map<String, Object> attributes) {
		throwAssertionError();
	}

	public void setUndirectedEdgeCount(int numberOfEdges) {
		throwAssertionError();
	}

	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		throwAssertionError();
	}

	public void addUndirectedEdge(int node1, int node2,
			Map<String, Object> attributes) {
		throwAssertionError();
	}

	public void addComment(String comment) {
		throwAssertionError();
	}

	public void finishedParsing() {
		throwAssertionError();
	}

	public boolean haltParsingNow() {
		throwAssertionError();
		return false;
	}

}
