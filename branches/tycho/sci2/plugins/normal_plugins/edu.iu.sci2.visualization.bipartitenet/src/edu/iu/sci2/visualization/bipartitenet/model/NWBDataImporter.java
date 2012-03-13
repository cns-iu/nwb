package edu.iu.sci2.visualization.bipartitenet.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.cishell.utilities.NumberUtilities;
import org.osgi.service.log.LogService;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class NWBDataImporter {

	private final String nodeTypeCol;
	private final String nodeWeightCol;
	private final String typeForLeftSide;
	private final LogService log;
	private final String edgeWeightCol;

	public NWBDataImporter(String nodeTypeCol, String typeForLeftSide,
			String nodeSizeCol, String edgeValueCol) {
		this(nodeTypeCol, typeForLeftSide, nodeSizeCol, edgeValueCol, null);		
	}
	
	public NWBDataImporter(String nodeTypeCol, String typeForLeftSide,
			String nodeWeightCol, String edgeWeightCol, LogService log) {
		this.nodeTypeCol = nodeTypeCol;
		this.typeForLeftSide = typeForLeftSide;
		this.nodeWeightCol = nodeWeightCol;
		this.edgeWeightCol = edgeWeightCol;
		this.log = log;
	}

	private void log(int level, String message) {
		if (log == null) { 
			System.err.println(message);
		} else {
			log.log(level, message);
		}
	}

	public BipartiteGraphDataModel constructModelFromFile(InputStream nwbData)
			throws IOException, ParsingException {
		ParserHandler handler = new ParserHandler();
		NWBFileParser parser = new NWBFileParser(nwbData);
		parser.parse(handler);
		return handler.constructGraphDataModel();
	}

	private class ParserHandler implements NWBFileParserHandler {
		private Map<Integer, Node> nodeById = Maps.newHashMap();
		private List<Edge> edges = Lists.newArrayList();
		private boolean gotAnyLeftNodes = false;
		private boolean gotAnyRightNodes = false;
	
		@Override
		public void addComment(String comment) {
		}
	
		@Override
		public void addNode(int id, String label, Map<String, Object> attributes) {
			double weight;			
			if (nodeWeightCol != null) {
				weight = NumberUtilities.interpretObjectAsDouble(attributes.get(nodeWeightCol));
				if (weight < 0) {
					log(LogService.LOG_WARNING, String.format(
							"Node '%s' has negative weight (%d), which is not handled well by this algorithm's display code.", label, weight));
				}
			} else {
				weight = 1;
			}
			
			String type = (String) attributes.get(nodeTypeCol);
			NodeDestination dest;
			if (typeForLeftSide.equalsIgnoreCase(type)) {
				dest = NodeDestination.LEFT;
				gotAnyLeftNodes = true;
			} else {
				dest = NodeDestination.RIGHT;
				gotAnyRightNodes = true;
			}
			
			Node nodeObj = new Node(label, weight, dest);
			nodeById.put(id, nodeObj);
		}

		@Override
		public void addDirectedEdge(int sourceNode, int targetNode,
				Map<String, Object> attributes) {
			// Ignore original direction of directed edges so they're always left->right.
			addUndirectedEdge(sourceNode, targetNode, attributes);
		}
	
		@Override
		public void addUndirectedEdge(int node1, int node2,	Map<String, Object> attributes) {
			// Determine left and right
			Node left, right, something;
			something = nodeById.get(node1);			
			if (something.getDestination() == NodeDestination.LEFT) {
				left = something;
				right = nodeById.get(node2);
			} else {
				left = nodeById.get(node2);
				right = something;
			}
			
			if (left.getDestination() == right.getDestination()) {
				log(LogService.LOG_WARNING, String.format("Graph is not properly bipartite: %s and %s are linked but are on the same side!",
						left, right));
			}
			
			// Find the edge weight
			double value;
			if (edgeWeightCol != null) {
				value = NumberUtilities.interpretObjectAsDouble(attributes.get(edgeWeightCol));
				if (value < 0) {
					log(LogService.LOG_WARNING, String.format(
							"Edge between %s and %s has negative weight (%d), which is not handled well by this algorithm's display code.", 
							left, right, value));
				}
			} else {
				value = 1;
			}
			
			edges.add(new Edge(left, right, value));
		}

		@Override
		public void finishedParsing() {
	
		}
	
		@Override
		public boolean haltParsingNow() {
			return false;
		}
	
		@Override
		public void setDirectedEdgeCount(int numberOfEdges) {
		}
	
		@Override
		public void setUndirectedEdgeCount(int numberOfEdges) {
		}

		@Override
		public void setDirectedEdgeSchema(LinkedHashMap<String, String> schema) {
			checkEdgeSchema(schema);
		}
	
		@Override
		public void setUndirectedEdgeSchema(LinkedHashMap<String, String> schema) {
			checkEdgeSchema(schema);
		}

		private void checkEdgeSchema(LinkedHashMap<String, String> schema) {
			/* The edge weight column should come from a list of all edge columns put together in
			 * mutateParameters. */
			assert ((edgeWeightCol == null) || (schema.containsKey(edgeWeightCol)));
		}

		@Override
		public void setNodeCount(int numberOfNodes) {
		}
	
		@Override
		public void setNodeSchema(LinkedHashMap<String, String> schema) {
			/* The node weight column should come from a list of all node columns put together in
			 * mutateParameters. */
			assert ((nodeWeightCol == null) || (schema.containsKey(nodeWeightCol)));
			
			/* 
			 * The presence of this column should already have been checked during the 
			 * mutateParameters process.
			 */
			assert (schema.containsKey(nodeTypeCol));
		}
	
		public BipartiteGraphDataModel constructGraphDataModel() {
			// The graph being completely empty is an error, not a warning, and is noticed at a higher level.
			if (! gotAnyLeftNodes) {
				log(LogService.LOG_WARNING, String.format("Supposedly bipartite graph has no left-hand (%s = %s) nodes", nodeTypeCol, typeForLeftSide));
			}
			if (! gotAnyRightNodes) {
				log(LogService.LOG_WARNING, String.format("Supposedly bipartite graph has no right-hand (%s != %s) nodes", nodeTypeCol, typeForLeftSide));
			}
			
			
			return new BipartiteGraphDataModel(nodeById.values(), edges, nodeWeightCol, edgeWeightCol);
		}
	
	}
}