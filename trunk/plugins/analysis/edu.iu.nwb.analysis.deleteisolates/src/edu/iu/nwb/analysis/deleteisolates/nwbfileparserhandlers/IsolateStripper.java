package edu.iu.nwb.analysis.deleteisolates.nwbfileparserhandlers;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class IsolateStripper extends NWBFileWriter {
	private int isolateNodeCount = 0;
	private Map nonIsolateNodeIDs;
	
	public IsolateStripper(Map nonIsolateNodeIDs, File outputNWBFile)
			throws IOException {
		super(outputNWBFile);
		this.nonIsolateNodeIDs = nonIsolateNodeIDs;
	}
	
	public int getIsolateNodeCount() {
		return this.isolateNodeCount;
	}
	
	public void addNode(int oldNodeID, String nodeLabel, Map attributes) {
		Integer boxedOldNodeID = new Integer(oldNodeID);
		
		if (this.nonIsolateNodeIDs.containsKey(boxedOldNodeID)) {
			Integer newNodeID =
				(Integer)this.nonIsolateNodeIDs.get(boxedOldNodeID);
			super.addNode(newNodeID.intValue(), nodeLabel, attributes);
		} else {
			this.isolateNodeCount++;
		}
	}
	
	public void addDirectedEdge(
			int oldSourceNodeID, int oldTargetNodeID, Map attributes) {
		super.addDirectedEdge(findNewNodeID(oldSourceNodeID),
							  findNewNodeID(oldTargetNodeID),
							  attributes);
	}
	
	public void addUndirectedEdge(
			int oldSourceNodeID, int oldTargetNodeID, Map attributes) {
		super.addUndirectedEdge(findNewNodeID(oldSourceNodeID),
							  	findNewNodeID(oldTargetNodeID),
								attributes);
	}
	
	private int findNewNodeID(int oldNodeID) {
		return ((Integer)this.nonIsolateNodeIDs.get(new Integer(oldNodeID))).
			intValue();
	}
}