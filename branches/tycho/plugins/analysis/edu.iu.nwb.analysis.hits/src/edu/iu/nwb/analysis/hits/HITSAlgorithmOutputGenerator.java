package edu.iu.nwb.analysis.hits;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cern.colt.matrix.DoubleMatrix2D;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class HITSAlgorithmOutputGenerator implements NWBFileParserHandler {

	private NWBFileWriter output;
	private NWBFileParser parser;
	private File edgeFile;
	private DoubleMatrix2D authorityMatrix, hubMatrix;
	private HashMap nodeIDToMatrixIndexMap;
	
	public HITSAlgorithmOutputGenerator(HITSComputation hitsComputation,
			File outputNWBFile) throws IOException {
		output = new NWBFileWriter(outputNWBFile);
		this.authorityMatrix = hitsComputation.authorityMatrix;
		this.hubMatrix = hitsComputation.hubMatrix;
		this.nodeIDToMatrixIndexMap = hitsComputation.nodeIDToMatrixIndexMap;
	}


	public void setNodeCount(int numberOfNodes) {
		output.setNodeCount(numberOfNodes);
	}
	
	public void setNodeSchema(LinkedHashMap schema) {
		schema.put("authority_score", NWBFileProperty.TYPE_FLOAT);
		schema.put("hub_score", NWBFileProperty.TYPE_FLOAT);
		output.setNodeSchema(schema);
		
	}
	
	public void addNode(int id, String label, Map attributes) {
		
		int matrixIndexToBeExtracted = (Integer) ((((List) nodeIDToMatrixIndexMap
				.get(id)).toArray())[0]);
		
		float authorityScore, hubScore;
		
		try{
			authorityScore = (float)authorityMatrix.get(matrixIndexToBeExtracted, 0);
		}
		catch (Exception e) {
			authorityScore = new Float(0);
		}

		try{
			hubScore = (float)hubMatrix.get(matrixIndexToBeExtracted, 0);
		}
		catch (Exception e) {
			hubScore = new Float(0);
		}

		attributes.put("authority_score", authorityScore);
		attributes.put("hub_score", hubScore);
		output.addNode(id, label, attributes);
	}
	
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		output.addDirectedEdge(sourceNode, targetNode, attributes);
	}
	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		output.addUndirectedEdge(node1, node2, attributes);
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
