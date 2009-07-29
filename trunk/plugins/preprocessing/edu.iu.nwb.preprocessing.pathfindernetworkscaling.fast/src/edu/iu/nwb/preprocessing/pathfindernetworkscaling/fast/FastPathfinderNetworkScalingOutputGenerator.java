package edu.iu.nwb.preprocessing.pathfindernetworkscaling.fast;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class FastPathfinderNetworkScalingOutputGenerator implements NWBFileParserHandler {

	private NWBFileWriter output;
	private int scaledNetworkEdgeCount;
	/**
	 * @return the scaledNetworkEdgeCount
	 */
	public int getScaledNetworkEdgeCount() {
		return scaledNetworkEdgeCount; 
	}

	private FastPathfinderNetworkScalingComputation scaledNetworkOutput;
	
	public FastPathfinderNetworkScalingOutputGenerator(
			FastPathfinderNetworkScalingComputation scaledNetworkOutput,
			File outputNWBFile) throws IOException {
		output = new NWBFileWriter(outputNWBFile);
		this.scaledNetworkEdgeCount = 0;
		this.scaledNetworkOutput = scaledNetworkOutput;
	}

	public void setNodeCount(int numberOfNodes) {
		output.setNodeCount(numberOfNodes);
	}
	
	public void setNodeSchema(LinkedHashMap schema) {
		output.setNodeSchema(schema);
	}
	
	public void addNode(int id, String label, Map attributes) {
		output.addNode(id, label, attributes);
	}
	
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		setEdgeRows(sourceNode, targetNode, attributes, false);
	}
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		setEdgeRows(node1, node2, attributes, true);
	}

	/**
	 * @param node1
	 * @param node2
	 * @param attributes
	 */
	private void setEdgeRows(int node1, int node2, Map attributes, boolean isUndirected) {
		
		/*
		 * Output only edges whose distance matrix value is equal to the weight 
		 * matrix value.
		 * */
		if (scaledNetworkOutput.getDistanceMatrixElement(node1, node2) 
				== scaledNetworkOutput.getWeightMatrixElement(node1, node2)) {
			if (isUndirected) {
				output.addUndirectedEdge(node1, node2, attributes);
			} else {
				output.addDirectedEdge(node1, node2, attributes);
			}
			scaledNetworkEdgeCount++;
		}
	}
	public void setDirectedEdgeCount(int numberOfEdges) {
		output.setDirectedEdgeCount(numberOfEdges);
	}
	public void setDirectedEdgeSchema(LinkedHashMap schema) {
		output.setDirectedEdgeSchema(schema);
	}
	public void setUndirectedEdgeCount(int numberOfEdges) {
		output.setUndirectedEdgeCount(numberOfEdges);
	}
	public void setUndirectedEdgeSchema(LinkedHashMap schema) {
		output.setUndirectedEdgeSchema(schema);
	}

	public void addComment(String comment) {
			output.addComment(comment);
	}
	
	public void finishedParsing() {
		output.finishedParsing();
	}

	public boolean haltParsingNow() {
		return false;
	}
}
