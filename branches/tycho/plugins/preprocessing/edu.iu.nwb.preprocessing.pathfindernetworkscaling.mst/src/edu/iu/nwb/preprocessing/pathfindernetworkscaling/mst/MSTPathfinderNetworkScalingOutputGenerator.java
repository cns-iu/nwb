package edu.iu.nwb.preprocessing.pathfindernetworkscaling.mst;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class MSTPathfinderNetworkScalingOutputGenerator implements NWBFileParserHandler {

	private NWBFileWriter output;
	private NWBFileParser parser;
	private File edgeFile;
	private Set finalEdges;
	private int edgeCount;
	public int scaledNetworkEdgeCount;
	
	public MSTPathfinderNetworkScalingOutputGenerator(MSTPathfinderNetworkScalingComputation scaledNetworkOutput,
			File outputNWBFile) throws IOException {
		output = new NWBFileWriter(outputNWBFile);
		this.finalEdges = scaledNetworkOutput.finalEdges;
		edgeCount = 0;
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
		output.addDirectedEdge(sourceNode, targetNode, attributes);
	}
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		
		/*
		 * Output edges in the new file based on the ids. These ids are actually counters
		 * and are set when file is getting parsed. So the finalEdges consists of these ids. 
		 * */
		if(finalEdges.contains(edgeCount)) {
			output.addUndirectedEdge(node1, node2, attributes);
		}
		edgeCount++;
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
