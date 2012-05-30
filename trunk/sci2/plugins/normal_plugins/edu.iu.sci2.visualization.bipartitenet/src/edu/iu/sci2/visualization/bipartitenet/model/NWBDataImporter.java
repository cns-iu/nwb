package edu.iu.sci2.visualization.bipartitenet.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Ordering;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.sci2.visualization.bipartitenet.LogStream;

/**
 * Creates a BipartiteGraphDataModel from an input file.
 *
 */

public class NWBDataImporter {
	// TODO switch to use NodeType rather than these strings.
	// TODO change this to a pure-static class, merging together .create and .constructModelFromFile
	// (that is slightly complicated because the two aren't always called one right after the other)
	public static NWBDataImporter create(String nodeTypeCol,
			String typeForLeftSide, String nodeWeightCol, String edgeWeightCol,
			Ordering<Node> leftNodeOrdering, Ordering<Node> rightNodeOrdering) {
		NodeHandler nodeHandler = new NodeHandler(nodeTypeCol, nodeWeightCol, typeForLeftSide, 
				leftNodeOrdering, rightNodeOrdering);
		EdgeHandler edgeHandler = new EdgeHandler(edgeWeightCol, (NodeProvider) nodeHandler);
		return new NWBDataImporter(nodeHandler, edgeHandler);
	}

	private final NodeHandler nodeHandler;
	private final EdgeHandler edgeHandler;
	private boolean hasBeenUsed = false;

	NWBDataImporter(NodeHandler nodeHandler,
			EdgeHandler edgeHandler) {
		this.nodeHandler = nodeHandler;
		this.edgeHandler = edgeHandler;
	}

	/**
	 * Create a BipartiteGraphDataModel from the given NWB-formatted input data.  This method
	 * may only be called once on any NWBDataImporter object.
	 */
	public BipartiteGraphDataModel constructModelFromFile(InputStream nwbData)
			throws IOException, ParsingException {
		Preconditions.checkState(! hasBeenUsed);
		ParserHandler handler = new ParserHandler();
		NWBFileParser parser = new NWBFileParser(nwbData);
		parser.parse(handler);
		hasBeenUsed = true;
		return handler.constructGraphDataModel();
	}

	private class ParserHandler extends NWBFileParserAdapter {
		/*
		 * NodeHandler delegations
		 */

		@Override
		public void setNodeSchema(LinkedHashMap<String, String> schema) {
			nodeHandler.setNodeSchema(schema);
		}
	
		@Override
		public void addNode(int id, String label, Map<String, Object> attributes) {
			nodeHandler.addNode(id, label, attributes);
		}

		/*
		 * EdgeHandler delegations
		 */
		
		@Override
		public void addDirectedEdge(int sourceNode, int targetNode,
				Map<String, Object> attributes) {
			edgeHandler.addDirectedEdge(sourceNode, targetNode, attributes);
		}
	
		@Override
		public void addUndirectedEdge(int node1, int node2,	Map<String, Object> attributes) {
			edgeHandler.addUndirectedEdge(node1, node2, attributes);
		}

		@Override
		public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
			edgeHandler.setDirectedEdgeSchema(schema);
		}
	
		@Override
		public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
			edgeHandler.setUndirectedEdgeSchema(schema);
		}

		/*
		 * Constructing the BipartiteGraphDataModel itself
		 */
		
		public BipartiteGraphDataModel constructGraphDataModel() {
			ImmutableList<Node> leftNodes = nodeHandler.getLeftNodes();
			ImmutableList<Node> rightNodes = nodeHandler.getRightNodes();
			
			// The graph being completely empty is an error, not a warning, and is noticed at a higher level.
			if (leftNodes.isEmpty()) {
				LogStream.WARNING.send("Supposedly bipartite graph has no left-hand nodes");
			}
			if (rightNodes.isEmpty()) {
				LogStream.WARNING.send("Supposedly bipartite graph has no right-hand nodes");
			}
			return new BipartiteGraphDataModel(leftNodes, rightNodes,
					edgeHandler.getEdges(), nodeHandler.getWeightColumn(), edgeHandler.getWeightColumn());
		}
	
	}
}