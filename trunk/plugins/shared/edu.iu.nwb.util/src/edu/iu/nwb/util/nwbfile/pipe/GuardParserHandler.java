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
	
	private static void throwAssertionError() {
		throw new AssertionError("A ParserPipe was used without a real output stream");
	}
	
	@Override
	public void setNodeCount(int numberOfNodes) {
		throwAssertionError();
	}

	@Override
	public void setNodeSchema(LinkedHashMap<String, String> schema) {
		throwAssertionError();
	}

	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		throwAssertionError();
	}

	@Override
	public void setDirectedEdgeCount(int numberOfEdges) {
		throwAssertionError();
	}

	@Override
	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		throwAssertionError();
	}

	@Override
	public void addDirectedEdge(int sourceNode, int targetNode,
			Map<String, Object> attributes) {
		throwAssertionError();
	}

	@Override
	public void setUndirectedEdgeCount(int numberOfEdges) {
		throwAssertionError();
	}

	@Override
	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		throwAssertionError();
	}

	@Override
	public void addUndirectedEdge(int node1, int node2,
			Map<String, Object> attributes) {
		throwAssertionError();
	}

	@Override
	public void addComment(String comment) {
		throwAssertionError();
	}

	@Override
	public void finishedParsing() {
		throwAssertionError();
	}

	@Override
	public boolean haltParsingNow() {
		throwAssertionError();
		return false;
	}

}
