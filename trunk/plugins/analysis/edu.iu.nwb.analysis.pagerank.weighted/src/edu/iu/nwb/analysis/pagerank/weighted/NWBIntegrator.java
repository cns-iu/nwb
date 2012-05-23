package edu.iu.nwb.analysis.pagerank.weighted;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.osgi.service.log.LogService;

import cern.colt.matrix.DoubleMatrix1D;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class NWBIntegrator implements NWBFileParserHandler {

	private Map<Integer, Integer> nodeLookup;
	private DoubleMatrix1D pagerank;
	private NWBFileWriter outputWriter;
	private File outputFile;
	private LogService log;

	public NWBIntegrator(Map<Integer, Integer> nodeLookup,
			DoubleMatrix1D pagerank, LogService log)
			throws AlgorithmExecutionException {
		try {
			this.outputFile = File.createTempFile("nwb-", ".nwb");
			this.outputWriter = new NWBFileWriter(this.outputFile);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Unable to write output file: " + e.toString(), e);
		}
		this.nodeLookup = nodeLookup;
		this.pagerank = pagerank;
		this.log = log;
	}
	
	public File getOutputFile() {
		return this.outputFile;
	}

	@Override
	public void addComment(String comment) {
		this.outputWriter.addComment(comment);
	}

	@Override
	public void addDirectedEdge(int sourceNode, int targetNode,
			Map<String, Object> attributes) {
		this.outputWriter.addDirectedEdge(sourceNode, targetNode, attributes);
	}

	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		Integer fakeId = this.nodeLookup.get(id);
		float pagerankValue;
		if (fakeId == null) {
			// singleton. Singletons will all be at the end of the vector, and
			// identical.
			pagerankValue = Double.valueOf(
					this.pagerank.getQuick(this.pagerank.size() - 1))
					.floatValue();
		} else {
			pagerankValue = Double.valueOf(this.pagerank.getQuick(fakeId))
					.floatValue();
		}
		attributes.put("nwbWeightedPagerank", pagerankValue);
		this.outputWriter.addNode(id, label, attributes);
	}

	@Override
	public void addUndirectedEdge(int node1, int node2,
			Map<String, Object> attributes) {
		throw new UnsupportedOperationException(
				"Undirected Edges are not supported by Page Rank.");
	}

	@Override
	public void finishedParsing() {
		this.outputWriter.finishedParsing();
	}

	@Override
	public boolean haltParsingNow() {
		return false;
	}

	@Override
	public void setDirectedEdgeCount(int numberOfEdges) {
		// don't set possibly wrong numbers
		this.log.log(LogService.LOG_DEBUG,
				"setDirectedEdgeCount is not supported by Weighted PageRank.");
	}

	@Override
	public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		this.outputWriter.setDirectedEdgeSchema(schema);
	}

	@Override
	public void setNodeCount(int numberOfNodes) {
		this.outputWriter.setNodeCount(this.pagerank.size());
	}

	@Override
	public void setNodeSchema(LinkedHashMap<String, String> schema) {
		schema.put("nwbWeightedPagerank", NWBFileProperty.TYPE_FLOAT);
		this.outputWriter.setNodeSchema(schema);
	}

	@Override
	public void setUndirectedEdgeCount(int numberOfEdges) {
		throw new UnsupportedOperationException(
				"Undirected Edges are not supported by Page Rank.");
	}

	@Override
	public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
		this.outputWriter.setUndirectedEdgeSchema(schema);
	}

}
