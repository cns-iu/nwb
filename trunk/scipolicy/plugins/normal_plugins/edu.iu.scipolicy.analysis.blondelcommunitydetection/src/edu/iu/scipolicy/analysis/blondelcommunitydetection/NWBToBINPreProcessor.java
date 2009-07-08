package edu.iu.scipolicy.analysis.blondelcommunitydetection;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NWBToBINPreProcessor extends NWBFileParserAdapter {
	private static final String NON_POSITIVE_WEIGHT_HALT_REASON =
		"Non-positive weights are not allowed.  To use this algorithm, " +
		"preprocess your network further.";
	
	private String haltParsingReason = "";
	private boolean shouldHaltParsing = false;
	
	private String weightAttribute;
	private boolean isWeighted;
	
	public NWBToBINPreProcessor() {
	}
	
	public void addDirectedEdge(int sourceNode,
								int targetNode,
								Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void addUndirectedEdge(int sourceNode,
								  int targetNode,
								  Map attributes) {
		this.addEdge(sourceNode, targetNode, attributes);
	}
	
	public void finishedParsing() {
		Node.accumulateEdgeCountsForOutput();
	}
	
	public boolean haltParsingNow() {
		return this.shouldHaltParsing;
	}
	
	private void addEdge(int sourceNodeID, int targetNodeID, Map attributes) {
		int weight;
		
		if (this.isWeighted) {
			weight = ((Number)attributes.get(this.weightAttribute)).intValue();
		}
		else {
			weight = 1;
		}
		
		if (weight < 0.0) {
			this.haltParsingReason = NON_POSITIVE_WEIGHT_HALT_REASON;
			this.shouldHaltParsing = true;
		}
		else {
			Node sourceNode = Node.getOrCreateNode(sourceNodeID);
			sourceNode.incrementEdgeCount();
			
			Node targetNode = Node.getOrCreateNode(targetNodeID);
			targetNode.incrementEdgeCount();
		}
	}
}