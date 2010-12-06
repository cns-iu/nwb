package edu.iu.cns.converter.nwb_graphstream;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Node;

import edu.iu.cns.graphstream.common.AnnotatedGraph;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class GraphStreamToNWBAlgorithm implements Algorithm {
	public static final String OUTPUT_FILE_BASE_NAME = "graphstream_to_nwb-";
	public static final String NWB_MIME_TYPE = "file:text/nwb";
	public static final String OUT_LABEL = "Converted to NWB";

	private Data inputData;
	private AnnotatedGraph inputGraph;

	public GraphStreamToNWBAlgorithm(Data inputData, AnnotatedGraph inputGraph) {
		this.inputData = inputData;
		this.inputGraph = inputGraph;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			File outputFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
				OUTPUT_FILE_BASE_NAME, "nwb");
			NWBFileWriter nwbWriter = new NWBFileWriter(outputFile);

			writeNodes(nwbWriter);
			writeDirectedEdges(nwbWriter);
			writeUndirectedEdges(nwbWriter);

			return wrapGraphAsOutputData(outputFile);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	private void writeNodes(NWBFileWriter nwbWriter) {
		nwbWriter.setNodeSchema(this.inputGraph.getNodeSchema());

		for (Iterator<Node> nodes = this.inputGraph.getNodeIterator(); nodes.hasNext();) {
			Node node = nodes.next();
			String nodeID = node.getId();
			int id = Integer.parseInt(nodeID);
			nwbWriter.addNode(
				id,
				this.inputGraph.getNodeLabel(nodeID),
				getAttributes(node));
		}
	}

	private void writeDirectedEdges(NWBFileWriter nwbWriter) {
		setDirectedEdgeSchema(nwbWriter);

		for (Iterator<Edge> edges = this.inputGraph.getEdgeIterator(); edges.hasNext();) {
			Edge edge = edges.next();
			int node1ID = Integer.parseInt(edge.getNode0().getId());
			int node2ID = Integer.parseInt(edge.getNode1().getId());
			Map<String, Object> attributes = getAttributes(edge);

			if (edge.isDirected()) {
				nwbWriter.addDirectedEdge(node1ID, node2ID, attributes);
			}
		}
	}

	private void writeUndirectedEdges(NWBFileWriter nwbWriter) {
		setUndirectedEdgeSchema(nwbWriter);

		for (Iterator<Edge> edges = this.inputGraph.getEdgeIterator(); edges.hasNext();) {
			Edge edge = edges.next();
			int node1ID = Integer.parseInt(edge.getNode0().getId());
			int node2ID = Integer.parseInt(edge.getNode1().getId());
			Map<String, Object> attributes = getAttributes(edge);

			if (!edge.isDirected()) {
				nwbWriter.addUndirectedEdge(node1ID, node2ID, attributes);
			}
		}
	}

	private void setDirectedEdgeSchema(NWBFileWriter nwbWriter) {
		LinkedHashMap<String, String> directedEdgeSchema = this.inputGraph.getDirectedEdgeSchema();

		if (directedEdgeSchema != null) {
			nwbWriter.setDirectedEdgeSchema(directedEdgeSchema);
		}
	}

	private void setUndirectedEdgeSchema(NWBFileWriter nwbWriter) {
		LinkedHashMap<String, String> undirectedEdgeSchema =
			this.inputGraph.getUndirectedEdgeSchema();

		if (undirectedEdgeSchema != null) {
			nwbWriter.setUndirectedEdgeSchema(undirectedEdgeSchema);
		}
	}

	private Data[] wrapGraphAsOutputData(File outputFile) {
    	Data outputNWBFileData = new BasicData(outputFile, NWB_MIME_TYPE);
    	Dictionary<String, Object> outputNWBFileMetadata = outputNWBFileData.getMetadata();
    	outputNWBFileMetadata.put(DataProperty.LABEL, OUT_LABEL);
    	outputNWBFileMetadata.put(DataProperty.PARENT, inputData);
    	outputNWBFileMetadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    	
    	return new Data[] { outputNWBFileData };
    }

	private Map<String, Object> getAttributes(Element element) {
		Map<String, Object> attributes = new HashMap<String, Object>();

		for (String attributeName : element.getAttributeKeySet()) {
			attributes.put(attributeName, element.getAttribute(attributeName));
		}

		return attributes;
	}
}