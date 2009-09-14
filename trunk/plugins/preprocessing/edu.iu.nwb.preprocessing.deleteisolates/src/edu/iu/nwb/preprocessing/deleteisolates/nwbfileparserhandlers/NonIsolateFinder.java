package edu.iu.nwb.preprocessing.deleteisolates.nwbfileparserhandlers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NonIsolateFinder extends NWBFileParserAdapter {
	private Set nonIsolateNodeIDs = new HashSet();
	
	public NonIsolateFinder() {}
	
	public Set getNonIsolateNodeIDs() {
		return nonIsolateNodeIDs;
	}
	
	public void addDirectedEdge(
			int sourceNodeID, int targetNodeID, Map attributes) {
		nonIsolateNodeIDs.add(new Integer(sourceNodeID));
		nonIsolateNodeIDs.add(new Integer(targetNodeID));
	}
	
	public void addUndirectedEdge(
			int sourceNodeID, int targetNodeID, Map attributes) {
		nonIsolateNodeIDs.add(new Integer(sourceNodeID));
		nonIsolateNodeIDs.add(new Integer(targetNodeID));
	}
}