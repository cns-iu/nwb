package edu.iu.nwb.analysis.pagerank.weighted;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import cern.colt.matrix.DoubleMatrix1D;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class NWBIntegrator implements NWBFileParserHandler {

	private Map<Integer, Integer> nodeLookup;
	private DoubleMatrix1D pagerank;
	private NWBFileWriter output;
	private File outputFile;
	
	public NWBIntegrator(Map<Integer, Integer> nodeLookup,
			DoubleMatrix1D pagerank) throws AlgorithmExecutionException {
		try {
			outputFile = File.createTempFile("nwb-", ".nwb");
			this.output = new NWBFileWriter(outputFile);
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Unable to write output file: " + e.toString(), e);
		}
		this.nodeLookup = nodeLookup;
		this.pagerank = pagerank;
	}
	
	public File getOutputFile() {
		return outputFile;
	}

	public void addComment(String comment) {
		output.addComment(comment);
	}

	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
		output.addDirectedEdge(sourceNode, targetNode, attributes);
	}

	public void addNode(int id, String label, Map attributes) {
		Integer fakeId = nodeLookup.get(id);
		double pagerankValue;
		if(fakeId == null) {
			//singleton. Singletons will all be at the end of the vector, and identical.
			pagerankValue = pagerank.getQuick(pagerank.size() - 1);
		} else {
			pagerankValue = pagerank.getQuick(fakeId);
		}
		attributes.put("nwbWeightedPagerank", pagerankValue);
		output.addNode(id, label, attributes);
	}

	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		//not that this will ever be called . . .
		output.addUndirectedEdge(node1, node2, attributes);
	}

	public void finishedParsing() {
		output.finishedParsing();
	}

	public boolean haltParsingNow() {
		return false;
	}

	public void setDirectedEdgeCount(int numberOfEdges) {
		//don't set possibly wrong numbers
	}

	public void setDirectedEdgeSchema(LinkedHashMap schema) {
		output.setDirectedEdgeSchema(schema);
	}

	public void setNodeCount(int numberOfNodes) {
		output.setNodeCount(pagerank.size());
	}

	public void setNodeSchema(LinkedHashMap schema) {
		schema.put("nwbWeightedPagerank", NWBFileProperty.TYPE_FLOAT);
		output.setNodeSchema(schema);
	}

	public void setUndirectedEdgeCount(int numberOfEdges) {
		//don't set possibly wrong numbers
	}

	public void setUndirectedEdgeSchema(LinkedHashMap schema) {
		output.setUndirectedEdgeSchema(schema);
	}

}
