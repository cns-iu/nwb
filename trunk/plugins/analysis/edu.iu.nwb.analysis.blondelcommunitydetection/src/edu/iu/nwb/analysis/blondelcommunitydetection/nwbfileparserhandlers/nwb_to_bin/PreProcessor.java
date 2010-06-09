package edu.iu.nwb.analysis.blondelcommunitydetection.nwbfileparserhandlers.nwb_to_bin;

import java.util.Map;

import edu.iu.nwb.analysis.blondelcommunitydetection.NetworkInfo;
import edu.iu.nwb.analysis.blondelcommunitydetection.Node;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class PreProcessor extends NWBFileParserAdapter {
	private boolean shouldHaltParsing = false;
	
	private NetworkInfo networkInfo;
	
	public PreProcessor(NetworkInfo networkInfo) {
		this.networkInfo = networkInfo;
	}
	
	@SuppressWarnings("unchecked")	// Raw Map
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	@SuppressWarnings("unchecked")	// Raw Map
	public void addUndirectedEdge(int sourceNode, int targetNode, Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void finishedParsing() {
		this.networkInfo.accumulateEdgeCountsForOutput();
	}
	
	public boolean haltParsingNow() {
		return this.shouldHaltParsing;
	}
	
	@SuppressWarnings("unchecked")	// Raw Map
	private void addEdge(int sourceNodeID, int targetNodeID, Map attributes) {
		Node sourceNode = Node.getOrCreateNode(sourceNodeID, this.networkInfo);
		sourceNode.incrementEdgeCount(this.networkInfo);
		
		Node targetNode = Node.getOrCreateNode(targetNodeID, this.networkInfo);
		targetNode.incrementEdgeCount(this.networkInfo);
	}
}