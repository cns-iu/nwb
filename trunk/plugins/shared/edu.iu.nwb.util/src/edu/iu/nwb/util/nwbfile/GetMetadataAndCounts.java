package edu.iu.nwb.util.nwbfile;

import java.util.Map;


public class GetMetadataAndCounts extends GetNWBFileMetadata {
	private int nodeCount = 0;
	private int directedEdgeCount = 0;
	private int undirectedEdgeCount = 0;
	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		this.nodeCount += 1;
	}
	@Override
	public void addDirectedEdge(int sourceNode, int targetNode, Map<String, Object> attributes) {
		this.directedEdgeCount  += 1;
	}
	@Override
	public void addUndirectedEdge(int node1, int node2, Map<String, Object> attributes) {
		this.undirectedEdgeCount  += 1;
	}
	
	@Override
	public int getNodeCount() {
		return nodeCount;
	}
	
	@Override
	public boolean haltParsingNow() {
		return false;
	}
	
	
	@Override
	public int getUndirectedEdgeCount() {
		return undirectedEdgeCount;
	}
	@Override
	public int getDirectedEdgeCount() {
		return directedEdgeCount;
	}
}
