package edu.iu.nwb.analysis.blondelcommunitydetection;

import java.util.ArrayList;

public class NetworkInfo {
	private ArrayList nodes = new ArrayList();
	private int maxCommunityLevel = 0;
	
	private int renumberingID = 0;
	private int totalEdgeCount = 0;
	
	public NetworkInfo() {
	}
	
	public ArrayList getNodes() {
		return this.nodes;
	}
	
	public int getMaxCommunityLevel() {
		return this.maxCommunityLevel;
	}
	
	public int getRenumberingID() {
		return this.renumberingID;
	}
	
	public int getTotalEdgeCount() {
		return this.totalEdgeCount;
	}
	
	public void addNode(Node node) {
		this.nodes.add(node);
	}
	
	public void incrementRenumberingID() {
		this.renumberingID++;
	}
	
	public void incrementTotalEdgeCount() {
		this.totalEdgeCount++;
	}
	
	public void setMaxCommunityLevel(int maxCommunityLevel) {
		this.maxCommunityLevel = maxCommunityLevel;
	}
	
	public Node findNodeByOriginalID(int originalID) {
		final int nodeCount = this.nodes.size();
		
		if (originalID < 0) {
			return null;
		} else {
			//TODO: turn into a map. Absolutely must not go into production
			for (int ii = 0; ii < nodeCount; ii++) {
				Node node = (Node)this.nodes.get(ii);
				
				if (node.getOriginalID() == originalID) {
					return node;
				}
			}
			
			return null;
		}
	}
	
	public void accumulateEdgeCountsForOutput() {
		int nodeCount = this.nodes.size();
		int edgeCountForOutput = 0;

		for (int ii = 0; ii < nodeCount; ii++) {
			Node node = (Node)this.nodes.get(ii);
			edgeCountForOutput += node.getActualEdgeCount();
			node.setEdgeCountForOutput(edgeCountForOutput);
		}
	}
}