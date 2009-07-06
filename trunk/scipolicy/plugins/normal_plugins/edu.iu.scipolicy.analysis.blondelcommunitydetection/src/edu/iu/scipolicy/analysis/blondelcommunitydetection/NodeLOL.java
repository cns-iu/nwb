package edu.iu.scipolicy.analysis.blondelcommunitydetection;

import java.util.ArrayList;

public class NodeLOL {
	public static ArrayList nodes = new ArrayList();
	
	private static int renumberingID = 0;
	private static int totalEdgeCount = 0;
	
	private int originalID;
	// This is also the index into the nodes ArrayList.
	private int newID;
	private int edgeCountForOutput = 0;
	private int actualEdgeCount = 0;
	
	private long startingEdgeOffsetInFile = 0;
	private long workingEdgeOffsetInFile = 0;
	
	private long startingWeightOffsetInFile = 0;
	private long workingWeightOffsetInFile = 0;
	
	private NodeLOL(int originalID) {
		this.originalID = originalID;
		this.newID = NodeLOL.renumberingID;
		
		NodeLOL.renumberingID++;
		NodeLOL.nodes.add(this);
	}

	public int getOriginalID() {
		return this.originalID;
	}
	
	public int getNewID() {
		return this.newID;
	}
	
	public int getEdgeCountForOutput() {
		return this.edgeCountForOutput;
	}
	
	public int getActualEdgeCount() {
		return this.actualEdgeCount;
	}
	
	public long getStartingEdgeOffsetInFile() {
		return this.startingEdgeOffsetInFile;
	}
	
	public long getWorkingEdgeOffsetInFile() {
		return this.workingEdgeOffsetInFile;
	}
	
	public long getStartingWeightOffsetInFile() {
		return this.startingWeightOffsetInFile;
	}
	
	public long getWorkingWeightOffsetInFile() {
		return this.workingWeightOffsetInFile;
	}
	
	
	public void incrementEdgeCount() {
		this.actualEdgeCount++;
		NodeLOL.totalEdgeCount++;
	}
	
	
	public void setStartingEdgeOffsetInFile(long offsetInFile) {
		this.startingEdgeOffsetInFile = offsetInFile;
		this.workingEdgeOffsetInFile = this.startingEdgeOffsetInFile;
	}
	
	public void incrementWorkingEdgeOffsetInFile() {
		this.workingEdgeOffsetInFile += 4;
	}
	
	
	public void setStartingWeightOffsetInFile(long offsetInFile) {
		this.startingWeightOffsetInFile = offsetInFile;
		this.workingWeightOffsetInFile = this.startingWeightOffsetInFile;
	}
	
	public void incrementWorkingWeightOffsetInFile() {
		this.workingWeightOffsetInFile += 4;
	}
	
	
	private void setEdgeCountForOutput(int edgeCountForOutput) {
		this.edgeCountForOutput = edgeCountForOutput;
	}
	
	
	public static int getTotalEdgeCount() {
		return NodeLOL.totalEdgeCount;
	}
		
	public static NodeLOL getOrCreateNode(int originalID) {
		NodeLOL nodeThatAlreadyExists =
			NodeLOL.findNodeByOriginalID(originalID);
		
		if (nodeThatAlreadyExists != null) {
			return nodeThatAlreadyExists;
		}
		else {
			NodeLOL newNode = new NodeLOL(originalID);
			
			return newNode;
		}
	}
	
	public static NodeLOL findNodeByOriginalID(int originalID) {
		final int nodeCount = NodeLOL.nodes.size();
		
		if (originalID < 0) {
			return null;
		}
		else {
			for (int ii = 0; ii < nodeCount; ii++) {
				NodeLOL node = (NodeLOL)NodeLOL.nodes.get(ii);
				
				if (node.getOriginalID() == originalID) {
					return node;
				}
			}
			
			return null;
		}
	}
	
	public static void accumulateEdgeCountsForOutput() {
		int nodeCount = NodeLOL.nodes.size();
		int edgeCountForOutput = 0;
		
		for (int ii = 0; ii < nodeCount; ii++) {
			NodeLOL node = (NodeLOL)NodeLOL.nodes.get(ii);
			edgeCountForOutput += node.getActualEdgeCount();
			node.setEdgeCountForOutput(edgeCountForOutput);
		}
	}
}