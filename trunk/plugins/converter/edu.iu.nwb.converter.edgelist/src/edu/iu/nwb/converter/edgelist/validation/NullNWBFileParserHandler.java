package edu.iu.nwb.converter.edgelist.validation;

import java.util.LinkedHashMap;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

/* This handler does not actually write to a file.
 * We use it to generalize the code for validation and edu.iu.nwb.converter.edgelist.conversion --
 * our validator uses the edu.iu.nwb.converter.edgelist.conversion code with this in place of an actual
 * file writer.
 */
public class NullNWBFileParserHandler implements NWBFileParserHandler {
	public void addComment(String comment) {}
	public void addDirectedEdge(int sourceNode,
								int targetNode,
								Map attributes) {}
	public void addNode(int id, String label, Map attributes) {}
	public void addUndirectedEdge(int node1, int node2, Map attributes) {}
	public void finishedParsing() {}
	public boolean haltParsingNow() {
		return true;
	}
	public void setDirectedEdgeCount(int numberOfEdges) {}
	public void setDirectedEdgeSchema(LinkedHashMap schema) {}
	public void setNodeCount(int numberOfNodes) {}
	public void setNodeSchema(LinkedHashMap schema) {}
	public void setUndirectedEdgeCount(int numberOfEdges) {}
	public void setUndirectedEdgeSchema(LinkedHashMap schema) {}
}
