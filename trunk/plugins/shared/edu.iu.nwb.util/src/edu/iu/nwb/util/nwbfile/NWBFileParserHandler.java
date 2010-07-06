package edu.iu.nwb.util.nwbfile;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Callback interface for receiving data as an NWB file is read. Note that
 * each method (except haltParsingNow()) has a specific order in which it is
 * executed corresponding to what is read linearly from the associated NWB file.
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public interface NWBFileParserHandler {
	/**
	 * Sets the total number of nodes in the document according to its node header.
	 * Note that if this is inaccurate, the parser will fail at the end anyway.
	 * If no total number of nodes is set in the header, then this method will
	 * not be called.
	 * 
	 * This method will be called before all other node-related methods and before
	 * any edge methods.
	 * 
	 * @param numberOfNodes The total number of nodes according to the nodes header
	 */
	public void setNodeCount(int numberOfNodes);
	
	/**
	 * Sets the node schema. The key corresponds to the attribute name and the
	 * value corresponds to its expected type. The type strings are in 
	 * {@link NWBFileProperty}. The schema is a {@link LinkedHashMap} such that
	 * when iterating through the keys, it will always be in the order in which
	 * the attributes were specified in the node header.
	 * 
	 * This method will be called after setNodeCount (if total number of nodes 
	 * is specified in the node header), before any addNode calls, and before
	 * any edge methods.
	 * 
	 * @param schema The node schema, a Map from attribute->expected type
	 */
	public void setNodeSchema(LinkedHashMap<String, String> schema);
	
	/**
	 * Adds a new node. The attributes map goes from attribute name
	 * to the value of the node's attribute. Order of key iteration is not 
	 * guaranteed here.
	 * 
	 * This method will be called after all other node methods and before any
	 * edge methods.
	 * 
	 * 
	 * @param id the ID of the node
	 * @param label the label for the node
	 * @param attributes The nodes attributes, a Map from attribute name->its value
	 */
	public void addNode(int id, String label, Map<String, Object> attributes);
	
	/**
	 * Sets the total number of directed eges in the document according to its 
	 * directed edges header. Note that if this is inaccurate, the parser will 
	 * fail at the end anyway. If no total number of directed edges is set in 
	 * the header, then this method will not be called.
	 * 
	 * This method will be called after all node-related methods and before
	 * any other directed edge methods. It can be called before or after the 
	 * group of undirected edge methods are called.
	 * 
	 * @param numberOfEdges The total number of directed edges according to the directed edges header
	 */
	public void setDirectedEdgeCount(int numberOfEdges);
	
	/**
	 * Sets the directed edge schema. The key corresponds to the attribute name 
	 * and the value corresponds to its expected type. The type strings are in 
	 * {@link NWBFileProperty}. The schema is a {@link LinkedHashMap} such that
	 * when iterating through the keys, it will always be in the order in which
	 * the attributes were specified in the node header.
	 * 
	 * This method will be called after setDirectedEdgeCount (if total number of 
	 * directed edges is specified in the directed eges header), before any 
	 * addDirectedEdge calls, after all node methods, and before or after the
	 * group of undirected edge methods are called.
	 * 
	 * @param schema The directed edge schema, a Map from attribute->expected type
	 */
	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema);
	
	/**
	 * Adds a new directed edge. The attributes map goes from attribute name
	 * to the value of the directed edge's attribute. Order of key iteration is 
	 * not guaranteed here.
	 * 
	 * This method will be called after all node methods, all other directed edge
	 * methods and before or after the group of undirected edge methods are called.
	 * 
	 * @param sourceNode the ID of the source node
	 * @param targetNode the ID of the target node
	 * @param attributes The directed edge attributes, a Map from attribute name->its value
	 */	
	public void addDirectedEdge(int sourceNode, int targetNode, Map<String, Object> attributes);

	
	/**
	 * Sets the total number of undirected eges in the document according to its 
	 * undirected edges header. Note that if this is inaccurate, the parser will 
	 * fail at the end anyway. If no total number of undirected edges is set in 
	 * the header, then this method will not be called.
	 * 
	 * This method will be called after all node-related methods and before
	 * any other undirected edge methods. It can be called before or after the 
	 * group of directed edge methods are called.
	 * 
	 * @param numberOfEdges The total number of undirected edges according to the undirected edges header
	 */
	public void setUndirectedEdgeCount(int numberOfEdges);
	
	/**
	 * Sets the undirected edge schema. The key corresponds to the attribute name 
	 * and the value corresponds to its expected type. The type strings are in 
	 * {@link NWBFileProperty}. The schema is a {@link LinkedHashMap} such that
	 * when iterating through the keys, it will always be in the order in which
	 * the attributes were specified in the node header.
	 * 
	 * This method will be called after setUndirectedEdgeCount (if total number of 
	 * undirected edges is specified in the directed eges header), before any 
	 * addUndirectedEdge calls, after all node methods, and before or after the
	 * group of directed edge methods are called.
	 * 
	 * @param schema The undirected edge schema, a Map from attribute->expected type
	 */
	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema);
	
	/**
	 * Adds a new undirected edge. The attributes map goes from attribute name
	 * to the value of the undirected edge's attribute. Order of key iteration is 
	 * not guaranteed here.
	 * 
	 * This method will be called after all node methods, all other undirected edge
	 * methods and before or after the group of directed edge methods are called.
	 * 
	 * @param node1 the ID of the first node
	 * @param node2 the ID of the second node
	 * @param attributes The undirected edge attributes, a Map from attribute name->its value
	 */	
	public void addUndirectedEdge(int node1, int node2, Map<String, Object> attributes);
	
	/**
	 * Adds a new comment. This method will be called as comment lines are found.
	 * 
	 * @param comment The comment found without its "#" prefix.
	 */
	public void addComment(String comment);
	
	/**
	 * Notifies when parsing of the NWB file has finished.
	 */
	public void finishedParsing();
	
	/**
	 * This method will be polled after each line of the NWB file has been read
	 * to see if it should stop parsing. If this returns true, then 
	 * finishedParsing() will be called after this method has been polled. 
	 * Otherwise, the parsing continues until through the whole file.
	 * 
	 * @return whether to stop parsing the NWB file or not
	 */
	public boolean haltParsingNow();
}
