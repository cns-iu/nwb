package edu.iu.sci2.preprocessing.mergenetworks;

import java.util.LinkedHashMap;
import java.util.Map;

import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.nwb.common.NWBAttribute;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.sci2.preprocessing.mergenetworks.valueobjects.Edge;
import edu.iu.sci2.preprocessing.mergenetworks.valueobjects.Node;


/**
 * This is used to create network asset objects i.e. node & edge to be used to generate the merged/
 * final network. It merges the network by keeping track of already existing assets & for 
 * each incoming asset it makes a decision whether or not that object exists. It uses the resolved 
 * schema to create new attributes.
 * @author cdtank
 */
public class MergeNetworkAssetsComputation  implements NWBFileParserHandler {
	// TODO: Don't pass these in (as empty maps), but rather ask for them back out when the caller is done with this thing.
	private Map<Object, Node> nodeIDToNode;
	private Map<String, Edge> edgeIDToEdge;

	private String uniqueNodeIdentifierColumnName; 

	private LogService logger;

	private Map<String, NWBAttribute> oldNodeAttributeNameToNewAttribute;

	private Map<String, NWBAttribute> oldEdgeAttributeNameToNewAttribute;
	
	private String thisNetworkName;
	private String otherNetworkName;
	
	private String EDGE_SEPARATOR = "$";
	
	public static final String YES = "T";
	public static final String NO = "F";

	public MergeNetworkAssetsComputation(
			String uniqueNodeIdentifierColumnName,
			Map<String, NWBAttribute> oldNodeAttributeNameToNewAttribute,
			Map<String, NWBAttribute> oldEdgeAttributeNameToNewAttribute,
			Map<Object, Node> nodeIDToNodeObject,
			Map<String, Edge> edgeIdentifierToEdgeObject,
			String thisNetworkName,
			String otherNetworkName,
			LogService logger) {
		
		this.uniqueNodeIdentifierColumnName = uniqueNodeIdentifierColumnName; 
		this.oldNodeAttributeNameToNewAttribute = oldNodeAttributeNameToNewAttribute;
		this.oldEdgeAttributeNameToNewAttribute = oldEdgeAttributeNameToNewAttribute;
		this.nodeIDToNode = nodeIDToNodeObject;
		this.edgeIDToEdge = edgeIdentifierToEdgeObject;
		this.thisNetworkName = thisNetworkName;
		this.otherNetworkName = otherNetworkName;
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
		String edgeIdentifier = getEdgeID(node1, node2);
		
		if (!edgeIDToEdge.containsKey(edgeIdentifier)) {
			edge = new Edge(node1, node2);
			edgeIDToEdge.put(edgeIdentifier, edge);	
		} else {
			edge = edgeIDToEdge.get(edgeIdentifier);
		}
		
		for (Object nameToTypeObject : attributes.entrySet()) {
		
			Map.Entry<String, Object> nameToValue = (Map.Entry<String, Object>) nameToTypeObject;
			
			String newAttributeName = oldEdgeAttributeNameToNewAttribute
											.get(nameToValue.getKey()).getAttrName();
			
			edge.addAttribute(newAttributeName, nameToValue.getValue());
		}
		
		/*
		 * the code below, when performed on the edges from each network we are merging (as it will be),
		 *  should result in two columns,
		 *    one saying YES or NO to whether that edge was present in the first input network, 
		 *    and the other similarly saying YES or NO to whether that edge was present in the second input network.
		 *   
		 *    Note that it is very possible for an edge to be present in both networks.
		 */
		
		//for this edge...
		//consider the mark for whether this edge is present in my network
		String edgeIsInThisNetwork_AttributeName = MergeNetworks.IS_PRESENT_IN_NETWORK_PREFIX 
			+ this.thisNetworkName;
		Object edgeIsInThisNetwork = edge.getAttribute(edgeIsInThisNetwork_AttributeName);
		//if there is no mark yet, make the mark YES
		if (edgeIsInThisNetwork == null) { 
			edge.addAttribute(edgeIsInThisNetwork_AttributeName, YES);
		}
		//if the mark says YES, leave it as YES
		else if (YES.equals(edgeIsInThisNetwork)) {}
		//if the mark says NO, change it to YES
		else if (NO.equals(edgeIsInThisNetwork)) {
			edge.addAttribute(edgeIsInThisNetwork_AttributeName, YES);
		}
		//now consider the mark for whether this edge is present in the other network
		String edgeIsInOtherNetwork_AttributeName = MergeNetworks.IS_PRESENT_IN_NETWORK_PREFIX 
			+ this.otherNetworkName;
		Object edgeIsInOtherNetwork = edge.getAttribute(edgeIsInOtherNetwork_AttributeName);
		//if there is no mark yet, make the mark NO
		if (edgeIsInOtherNetwork == null) {
			edge.addAttribute(edgeIsInOtherNetwork_AttributeName, NO);
		}
		//if the mark says YES, leave it as YES
		if (YES.equals(edgeIsInOtherNetwork)) {}
		//if the mark says NO, leave it as NO
		if (NO.equals(edgeIsInOtherNetwork)) {}
		
		/*
		 * (the consideration of edges in the 'other' network is necessary
		 * in order to have edges that aren't from a particular network have "NO" in
		 * the appropriate column, rather than just having a null value.)
		 */
	}
	
	private String getEdgeID(int node1, int node2) {
		if (node1 > node2) {
			return String.format("%s%s%s", node2, EDGE_SEPARATOR, node1);
		} else {
			return String.format("%s%s%s", node1, EDGE_SEPARATOR, node2);
		}
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
		if (!nodeIDToNode.containsKey(uniqueIdentifierValue)) {
			node = new Node(id, label);
			nodeIDToNode.put(uniqueIdentifierValue, node);	
		} else {
			node = nodeIDToNode.get(uniqueIdentifierValue);
		}
		
		for (Object nameToTypeObject : attributes.entrySet()) {
			Map.Entry<String, Object> nameToValue = (Map.Entry<String, Object>) nameToTypeObject;
			String newAttributeName = oldNodeAttributeNameToNewAttribute
											.get(nameToValue.getKey()).getAttrName();
			node.addAttribute(newAttributeName, nameToValue.getValue());
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
