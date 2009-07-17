package edu.iu.nwb.util.nwbfile;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handler class for extracting the node/edge schemas from an NWB File
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public class GetNWBFileMetadata extends NWBFileParserAdapter {
	private LinkedHashMap nodeSchema;
	private LinkedHashMap directedEdgeSchema;
	private LinkedHashMap undirectedEdgeSchema;
	
	private int nodeCount = 0;
	private int directedEdgeCount = 0;
	private int undirectedEdgeCount = 0;
	
	public LinkedHashMap getNodeSchema() {
		return nodeSchema;
	}
	public void setNodeSchema(LinkedHashMap nodeSchema) {
		this.nodeSchema = nodeSchema;
	}
	public LinkedHashMap getDirectedEdgeSchema() {
		return directedEdgeSchema;
	}
	public void setDirectedEdgeSchema(LinkedHashMap directedEdgeSchema) {
		this.directedEdgeSchema = directedEdgeSchema;
	}
	public LinkedHashMap getUndirectedEdgeSchema() {
		return undirectedEdgeSchema;
	}
	public void setUndirectedEdgeSchema(LinkedHashMap undirectedEdgeSchema) {
		this.undirectedEdgeSchema = undirectedEdgeSchema;
	}
	
	public void addNode(int id, String label, Map attributes) {
		this.nodeCount++;
	}
	
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		this.directedEdgeCount++;
	}
	
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		this.undirectedEdgeCount++;
	}
	
	public int getNodeCount() {
		return this.nodeCount;
	}
	
	public int getDirectedEdgeCount() {
		return this.directedEdgeCount;
	}
	
	public int getUndirectedEdgeCount() {
		return this.undirectedEdgeCount;
	}
	
	public int getTotalEdgeCount() {
		return this.getDirectedEdgeCount() + this.getUndirectedEdgeCount();
	}
	
	public boolean haltParsingNow() { 
		return nodeSchema != null && directedEdgeSchema != null && undirectedEdgeSchema != null; 
	}
}
