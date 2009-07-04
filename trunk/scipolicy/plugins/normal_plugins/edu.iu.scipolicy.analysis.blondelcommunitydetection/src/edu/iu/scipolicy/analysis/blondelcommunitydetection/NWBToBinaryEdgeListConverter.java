package edu.iu.scipolicy.analysis.blondelcommunitydetection;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NWBToBinaryEdgeListConverter extends NWBFileParserAdapter {
	private static final String NON_POSITIVE_WEIGHT_HALT_REASON =
		"Non-positive weights are not allowed.  To use this algorithm, " +
		"preprocess your network further.";
	
	private String haltParsingReason = "";
	private boolean shouldHaltParsing = false;
	
	private DataOutputStream dataOutputStream;
	
	private String weightAttribute;
	private boolean isWeighted;
	
	private Map renumberedNodesMap;
	private int renumberingIndex = 0;
	
	private ArrayList edges;
	
	public NWBToBinaryEdgeListConverter(
			FileOutputStream fileOutputStream,
			String weightAttribute,
			boolean isWeighted) {
		this.dataOutputStream = new DataOutputStream(fileOutputStream);
		this.weightAttribute = weightAttribute;
		this.isWeighted = isWeighted;
		this.renumberedNodesMap = new HashMap();
		this.edges = new ArrayList();
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
		try {
			this.writeHeader();
		}
		catch (IOException ioException) {
			throw new RuntimeException(
				"Failed to write header for Blondel network.", ioException);
		}
		
		try {
			this.writeNodes();
		}
		catch (IOException ioException) {
			throw new RuntimeException(
				"Failed to write nodes for Blondel network.", ioException);
		}
		
		try {
			this.writeEdges();
		}
		catch (IOException ioException) {
			throw new RuntimeException(
				"Failed to write edges for Blondel network.", ioException);
		}
		
		if (this.isWeighted) {
			try {
				this.writeWeights();
			}
			catch (IOException ioException) {
				throw new RuntimeException(
					"Failed to write weights for Blondel network.",
					ioException);
			}
		}
		
		try {
			this.dataOutputStream.close();
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	
	public boolean haltParsingNow() {
		return this.shouldHaltParsing;
	}
	
	private void addEdge(int sourceNode, int targetNode, Map attributes) {
		double weight;
		
		if (this.isWeighted) {
			weight = ((Number)attributes.get(this.weightAttribute)).doubleValue();
		}
		else {
			weight = 1.0;
		}
		
		if (weight < 0.0) {
			this.haltParsingReason = NON_POSITIVE_WEIGHT_HALT_REASON;
			this.shouldHaltParsing = true;
		}
		else {
			Integer sourceNodeObject = new Integer(sourceNode);
			Integer targetNodeObject = new Integer(targetNode);
			
			if (!this.renumberedNodesMap.containsKey(sourceNodeObject)) {
				Integer renumberingIndexObject =
					new Integer(this.renumberingIndex);
				// System.err.println("Node " + sourceNode + " is getting renumbered to " + renumberingIndex);
				this.renumberedNodesMap.put(sourceNodeObject, renumberingIndexObject);
				this.renumberingIndex++;
			}
			
			if (!this.renumberedNodesMap.containsKey(targetNodeObject)) {
				Integer renumberingIndexObject =
					new Integer(this.renumberingIndex);
				// System.err.println("Node " + targetNode + " is getting renumbered to " + renumberingIndex);
				this.renumberedNodesMap.put(targetNodeObject, renumberingIndexObject);
				this.renumberingIndex++;
			}
			
			this.edges.add(
				new EdgeLOL(sourceNodeObject, targetNodeObject, weight));
		}
	}
	
	private void writeHeader() throws IOException {
		int nodeCount = this.renumberedNodesMap.size();
		this.writeLittleEndianInt(nodeCount);
		System.err.println("nodeCount: " + nodeCount);
	}
	
	private void writeNodes() throws IOException {
		int nodeCount = this.renumberedNodesMap.size();
		Collection renumberedNodesCollection =
			this.renumberedNodesMap.values();
		Integer[] renumberedNodes =
			(Integer[])renumberedNodesCollection.toArray(new Integer[0]);
		
		for (int ii = 0; ii < renumberedNodes.length; ii++) {
			this.writeLittleEndianInt(renumberedNodes[ii].intValue());
		}
	}
	
	private void writeEdges() throws IOException {
		// Write the edge (or "link") count first.
		
		int edgeCount = this.edges.size();
		
		this.writeLittleEndianInt(edgeCount);
		
		// Write the edges now.
		for (int ii = 0; ii < edgeCount; ii++) {
			EdgeLOL edge = (EdgeLOL)this.edges.get(ii);
			this.writeLittleEndianInt(edge.sourceNodeOjbect.intValue());
			this.writeLittleEndianInt(edge.targetNodeObject.intValue());
		}
	}
	
	private void writeWeights() throws IOException {
		int edgeCount = this.edges.size();
		
		for (int ii = 0; ii < edgeCount; ii++) {
			EdgeLOL edge = (EdgeLOL)this.edges.get(ii);
			this.writeLittleEndianInt(edge.weight.intValue());
		}
	}
	
	private void writeLittleEndianInt(int bigEndianInt) throws IOException {
		this.dataOutputStream.writeByte((byte)(0xff & bigEndianInt));
		this.dataOutputStream.writeByte((byte)(0xff & (bigEndianInt >> 8)));
		this.dataOutputStream.writeByte((byte)(0xff & (bigEndianInt >> 16)));
		this.dataOutputStream.writeByte((byte)(0xff & (bigEndianInt >> 24)));
	}
	
	private class EdgeLOL {
		public Integer sourceNodeOjbect;
		public Integer targetNodeObject;
		public Double weight;
		
		public EdgeLOL(Integer sourceNodeObject,
					   Integer targetNodeObject,
					   double weight) {
			this.sourceNodeOjbect = sourceNodeObject;
			this.targetNodeObject = targetNodeObject;
			this.weight = new Double(weight);
		}
	}
}