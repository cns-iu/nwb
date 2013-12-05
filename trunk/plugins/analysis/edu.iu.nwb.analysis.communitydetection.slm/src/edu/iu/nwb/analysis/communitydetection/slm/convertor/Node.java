package edu.iu.nwb.analysis.communitydetection.slm.convertor;

import java.util.ArrayList;

public class Node {
	private int originalID;
	// This is also the index into the nodes ArrayList.
	private int newID;
	private int edgeCountForOutput = 0;
	private int actualEdgeCount = 0;
	
	private long startingEdgeOffsetInFile = 0;
	private long workingEdgeOffsetInFile = 0;
	
	private long startingWeightOffsetInFile = 0;
	private long workingWeightOffsetInFile = 0;
	
	private ArrayList<Integer> communities = new ArrayList<Integer>();
	
	private Node(int originalID, int newID) {
		this.originalID = originalID;
		this.newID = newID;
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
	
	
	public ArrayList<Integer> getCommunities() {
		return this.communities;
	}
	
	
	public NetworkInfo incrementEdgeCount(NetworkInfo networkInfo) {
		this.actualEdgeCount++;
		networkInfo.incrementTotalEdgeCount();
		
		return networkInfo;
	}
	
	public void setEdgeCountForOutput(int edgeCountForOutput) {
		this.edgeCountForOutput = edgeCountForOutput;
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
	
	
	public NetworkInfo addCommunity(Integer communityID, NetworkInfo networkInfo) {
		this.communities.add(communityID);
		int communityCount = this.communities.size();
		
		if (communityCount > networkInfo.getMaxCommunityLevel()) {
			networkInfo.setMaxCommunityLevel(communityCount);
		}
		
		return networkInfo;
	}
	
		
	public static Node getOrCreateNode(int originalID, NetworkInfo networkInfo) {
		Node nodeThatAlreadyExists = networkInfo.findNodeByOriginalID(originalID);
		
		if (nodeThatAlreadyExists != null) {
			return nodeThatAlreadyExists;
		} else {
			Node newNode = new Node(originalID, networkInfo.getRenumberingID());
			networkInfo.addNode(newNode);
			networkInfo.incrementRenumberingID();
			
			return newNode;
		}
	}
}