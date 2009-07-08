package edu.iu.scipolicy.analysis.blondelcommunitydetection;

import java.util.ArrayList;

public class Node {
	private static ArrayList nodes = new ArrayList();
	private static int maxCommunityLevel = 0;
	
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
	
	private ArrayList communities = new ArrayList();
	
	private Node(int originalID) {
		this.originalID = originalID;
		this.newID = Node.renumberingID;
		Node.renumberingID++;
		Node.nodes.add(this);
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
	
	
	public ArrayList getCommunities() {
		return this.communities;
	}
	
	
	public void incrementEdgeCount() {
		this.actualEdgeCount++;
		Node.totalEdgeCount++;
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
	
	
	public void addCommunity(Integer communityID) {
		this.communities.add(communityID);
		int communityCount = this.communities.size();
		
		if (communityCount > Node.maxCommunityLevel) {
			Node.maxCommunityLevel = communityCount;
		}
	}
	
	
	private void setEdgeCountForOutput(int edgeCountForOutput) {
		this.edgeCountForOutput = edgeCountForOutput;
	}
	
	
	public static void reset() {
		Node.nodes = new ArrayList();
		Node.maxCommunityLevel = 0;
		
		Node.renumberingID = 0;
		Node.totalEdgeCount = 0;
	}
	
	public static ArrayList getNodes() {
		return Node.nodes;
	}
	
	public static int getMaxCommunityLevel() {
		return Node.maxCommunityLevel;
	}
	
	public static int getTotalEdgeCount() {
		return Node.totalEdgeCount;
	}
		
	public static Node getOrCreateNode(int originalID) {
		Node nodeThatAlreadyExists = Node.findNodeByOriginalID(originalID);
		
		if (nodeThatAlreadyExists != null) {
			return nodeThatAlreadyExists;
		}
		else {
			Node newNode = new Node(originalID);
			
			return newNode;
		}
	}
	
	public static Node findNodeByOriginalID(int originalID) {
		final int nodeCount = Node.nodes.size();
		
		if (originalID < 0) {
			return null;
		}
		else {
			//TODO: turn into a map. Absolutely must not go into production
			for (int ii = 0; ii < nodeCount; ii++) {
				Node node = (Node)Node.nodes.get(ii);
				
				if (node.getOriginalID() == originalID) {
					return node;
				}
			}
			
			return null;
		}
	}
	
	public static void accumulateEdgeCountsForOutput() {
		int nodeCount = Node.nodes.size();
		int edgeCountForOutput = 0;
		//TODO: iterator? java 5?
		for (int ii = 0; ii < nodeCount; ii++) {
			Node node = (Node)Node.nodes.get(ii);
			edgeCountForOutput += node.getActualEdgeCount();
			node.setEdgeCountForOutput(edgeCountForOutput);
		}
	}
}