package edu.iu.nwb.util.nwbfile;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Handler class for extracting the node/edge schemas from an NWB File
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public class GetNWBFileMetadata extends NWBFileParserAdapter {
	private LinkedHashMap<String, String> nodeSchema;
	private LinkedHashMap<String, String> directedEdgeSchema;
	private LinkedHashMap<String, String> undirectedEdgeSchema;
	
	private int nodeCount = 0;
	private int directedEdgeCount = 0;
	private int undirectedEdgeCount = 0;
	
	public LinkedHashMap<String, String> getNodeSchema() {
		return this.nodeSchema;
	}
	@Override
	public void setNodeSchema(LinkedHashMap<String, String> nodeSchema) {
		this.nodeSchema = nodeSchema;
	}
	public LinkedHashMap<String, String> getDirectedEdgeSchema() {
		return this.directedEdgeSchema;
	}
	@Override
	public void setDirectedEdgeSchema(LinkedHashMap<String, String> directedEdgeSchema) {
		this.directedEdgeSchema = directedEdgeSchema;
	}
	public LinkedHashMap<String, String> getUndirectedEdgeSchema() {
		return this.undirectedEdgeSchema;
	}
	@Override
	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> undirectedEdgeSchema) {
		this.undirectedEdgeSchema = undirectedEdgeSchema;
	}
	
	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		this.nodeCount++;
	}
	
	@Override
	public void addDirectedEdge(int sourceNode, int targetNode, Map<String, Object> attributes) {
		this.directedEdgeCount++;
	}
	
	@Override
	public void addUndirectedEdge(int node1, int node2, Map<String, Object> attributes) {
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
	
	@Override
	public boolean haltParsingNow() { 
		return
			(this.nodeSchema != null) &&
			(this.directedEdgeSchema != null) &&
			(this.undirectedEdgeSchema != null); 
	}
}
