package edu.iu.nwb.util.nwbfile;

import java.util.Map;


public class GetMetadataAndCounts extends GetNWBFileMetadata {
	private int nodeCount = 0;
	private int directedEdgeCount = 0;
	private int undirectedEdgeCount = 0;
	public void addNode(int id, String label, Map<String, Object> attributes) {
		this.nodeCount += 1;
	}
	public void addDirectedEdge(int sourceNode, int targetNode, Map<String, Object> attributes) {
		this.directedEdgeCount  += 1;
	}
	public void addUndirectedEdge(int node1, int node2, Map<String, Object> attributes) {
		this.undirectedEdgeCount  += 1;
	}
	
	public int getNodeCount() {
		return nodeCount;
	}
	
	public boolean haltParsingNow() {
		return false;
	}
	
	
	public int getUndirectedEdgeCount() {
		return undirectedEdgeCount;
	}
	public int getDirectedEdgeCount() {
		return directedEdgeCount;
	}
}
