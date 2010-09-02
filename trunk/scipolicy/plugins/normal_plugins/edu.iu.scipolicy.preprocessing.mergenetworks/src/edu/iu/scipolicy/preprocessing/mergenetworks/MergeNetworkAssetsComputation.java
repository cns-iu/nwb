package edu.iu.scipolicy.preprocessing.mergenetworks;

import java.util.LinkedHashMap;
import java.util.Map;

import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.NWBAttribute;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects.Edge;
import edu.iu.scipolicy.preprocessing.mergenetworks.valueobjects.Node;


/**
 * This is used to create network asset objects i.e. node & edge to be used to generate the merged/
 * final network. It merges the network by keeping track of already existing assets & for 
 * each incoming asset it makes a decision whether or not that object exists. It uses the resolved 
 * schema to create new attributes.
 * @author cdtank
 */
public class MergeNetworkAssetsComputation  implements NWBFileParserHandler {
	
	private Map<Object, Node> nodeIDToNodeObject;
	private Map<String, Edge> edgeIdentifierToEdgeObject;

	private String uniqueNodeIdentifierColumnName; 

	private LogService logger;

	private Map<String, NWBAttribute> oldNodeAttributeNameToNewAttribute;

	private Map<String, NWBAttribute> oldEdgeAttributeNameToNewAttribute;
	
	private String EDGE_SEPARATOR = "$";

	public MergeNetworkAssetsComputation(
			String uniqueNodeIdentifierColumnName,
			Map<String, NWBAttribute> oldNodeAttributeNameToNewAttribute,
			Map<String, NWBAttribute> oldEdgeAttributeNameToNewAttribute,
			Map<Object, Node> nodeIDToNodeObject,
			Map<String, Edge> edgeIdentifierToEdgeObject,
			LogService logger) {
		
		this.uniqueNodeIdentifierColumnName = uniqueNodeIdentifierColumnName; 
		this.oldNodeAttributeNameToNewAttribute = oldNodeAttributeNameToNewAttribute;
		this.oldEdgeAttributeNameToNewAttribute = oldEdgeAttributeNameToNewAttribute;
		this.nodeIDToNodeObject = nodeIDToNodeObject;
		this.edgeIdentifierToEdgeObject = edgeIdentifierToEdgeObject;
		this.logger = logger;
	}


	/**
	 * Check if the edge with a given source & target already exists if so then merge the new 
	 * attributes with the existing edge attributes.  
	 * @param node1
	 * @param node2
	 * @param attributes
	 * @param isUnDirectedEdge 
	 */
	private void createEdgeTupleElements(int node1, 
										 int node2, 
										 Map attributes, 
										 boolean isUnDirectedEdge) {
		Edge edge;
		
		/*
		 * Unique edge identifier is constructed using the format - 
		 * "<SOURCE NODE ID>$<TARGET NODE ID>".
		 * */
		String edgeIdentifier = getEdgeIdentifier(node1, node2);
		
		if (!edgeIdentifierToEdgeObject.containsKey(edgeIdentifier)) {
			edge = new Edge(node1, node2);
			edgeIdentifierToEdgeObject.put(edgeIdentifier, edge);	
		} else {
			edge = edgeIdentifierToEdgeObject.get(edgeIdentifier);
		}
		
		for (Object nameToTypeObject : attributes.entrySet()) {
		
			Map.Entry<String, Object> nameToValue = (Map.Entry<String, Object>) nameToTypeObject;
			
			String newAttributeName = oldEdgeAttributeNameToNewAttribute
											.get(nameToValue.getKey()).getAttrName();
			
			edge.addEdgeAttribute(newAttributeName, nameToValue.getValue());
		}
	}
	
	private String getEdgeIdentifier(int node1, int node2) {
		return String.format("%s%s%s", node1, EDGE_SEPARATOR, node2);
		
		/*if (node1 > node2) {
			return String.format("%s%s%s", node2, EDGE_SEPARATOR, node1);
		} else {
			return String.format("%s%s%s", node1, EDGE_SEPARATOR, node2);
		}*/
	}


	public void addNode(int id, String label, Map attributes) {
		
		Node node;
		
		/*
		 * Since the unique node identifier can be either int or a string we are using a generic
		 * data type "object" to save the value.
		 * Based on the node attribute provided by the user for identifying unique nodes we will
		 * get the identifier value. 
		 * */
		Object uniqueIdentifierValue = getUniqueIdentifierValue(id, label, attributes);
		if (!nodeIDToNodeObject.containsKey(uniqueIdentifierValue)) {
			node = new Node(id, label);
			nodeIDToNodeObject.put(uniqueIdentifierValue, node);	
		} else {
			node = nodeIDToNodeObject.get(uniqueIdentifierValue);
		}
		
		for (Object nameToTypeObject : attributes.entrySet()) {
			Map.Entry<String, Object> nameToValue = (Map.Entry<String, Object>) nameToTypeObject;
			String newAttributeName = oldNodeAttributeNameToNewAttribute
											.get(nameToValue.getKey()).getAttrName();
			node.addNodeAttribute(newAttributeName, nameToValue.getValue());
		}
	} 

	private Object getUniqueIdentifierValue(int id, String label, Map attributes) {

		if (uniqueNodeIdentifierColumnName.equalsIgnoreCase("id")) {
			return id;
		} else if (uniqueNodeIdentifierColumnName.equalsIgnoreCase("label")) {
			return label;
		} else {
			return attributes.get(uniqueNodeIdentifierColumnName);
		}
		
	}


	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		createEdgeTupleElements(node1, node2, attributes, true);
	}

	public void finishedParsing() { }


	/*
	 * Currently there is no support for Directed Edges.
	 * */
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {}
	
	public boolean haltParsingNow() {
		return false;
	}

	public void addComment(String comment) { }
	
	public void setDirectedEdgeCount(int numberOfEdges) { }

	public void setDirectedEdgeSchema(LinkedHashMap schema) { }

	public void setNodeCount(int numberOfNodes) { }

	public void setNodeSchema(LinkedHashMap schema) { }

	public void setUndirectedEdgeCount(int numberOfEdges) { }

	public void setUndirectedEdgeSchema(LinkedHashMap schema) {	}

}
