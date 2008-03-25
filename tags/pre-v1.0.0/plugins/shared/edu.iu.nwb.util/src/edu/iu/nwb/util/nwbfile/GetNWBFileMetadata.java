package edu.iu.nwb.util.nwbfile;

import java.util.LinkedHashMap;

/**
 * Handler class for extracting the node/edge schemas from an NWB File
 * 
 * @author Bruce Herr (bh2@bh2.net)
 */
public class GetNWBFileMetadata extends NWBFileParserAdapter {
	private LinkedHashMap nodeSchema;
	private LinkedHashMap directedEdgeSchema;
	private LinkedHashMap undirectedEdgeSchema;
	
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
	
	public boolean haltParsingNow() { 
		return nodeSchema != null && directedEdgeSchema != null && undirectedEdgeSchema != null; 
	}
}
