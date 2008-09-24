package edu.iu.nwb.util.nwbfile;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Adapter version of NWBFileParserHandler
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public class NWBFileParserAdapter implements NWBFileParserHandler {
	public void setNodeCount(int numberOfNodes) {}
	public void setNodeSchema(LinkedHashMap schema) {}
	public void addNode(int id, String label, Map attributes) {}
	
	public void setDirectedEdgeCount(int numberOfEdges) {}
	public void setDirectedEdgeSchema(LinkedHashMap schema) {}
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {}

	public void setUndirectedEdgeCount(int numberOfEdges) {}
	public void setUndirectedEdgeSchema(LinkedHashMap schema) {}
	public void addUndirectedEdge(int node1, int node2, Map attributes) {}
	
	public void addComment(String comment) {}
	
	public void finishedParsing() {}
	public boolean haltParsingNow() { return false; }
}
