package edu.iu.nwb.analysis.deleteisolates.nwbfileparserhandlers;

import java.util.HashMap;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NonIsolateFinder extends NWBFileParserAdapter {
	private int nodeCount = 1;
	private Map oldNodeIDsToNewNodeIDs = new HashMap();
	
	public NonIsolateFinder() { }
	
	public Map getNodeIDsFound() {
		return this.oldNodeIDsToNewNodeIDs;
	}
	
	public void addDirectedEdge(
			int sourceNodeID, int targetNodeID, Map attributes) {
		assignNormalizedNodeID(sourceNodeID);
		assignNormalizedNodeID(targetNodeID);
	}
	
	public void addUndirectedEdge(
			int sourceNodeID, int targetNodeID, Map attributes) {
		assignNormalizedNodeID(sourceNodeID);
		assignNormalizedNodeID(targetNodeID);
	}
	
	// TODO Comment why we're doing this (normalizing node IDs).
	private void assignNormalizedNodeID(int nodeID) {
		if (!oldNodeIDsToNewNodeIDs.containsKey(nodeID)) {
			this.oldNodeIDsToNewNodeIDs.put(
					new Integer(nodeID),
					new Integer(this.nodeCount));
			this.nodeCount++;
		}
	}
}