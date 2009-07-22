package edu.iu.nwb.preprocessing.pathfindernetworkscaling.mst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.service.log.LogService;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;


/**
 * After getting the input file following algorithm is implemented. 
 * 1. When reading through the nodes, initiate clusters (one for each node). 
 * 2. Next, when reading through the edges create tuples containing unique edge id, 
 * node 1, node 2 & edge weight. Depending upon users choice of how edge weight should
 * be represented, normalize the edge weight i.e. If they chose similarity, normalize edge 
 * weights to be dissimilarity based by inverting the edge weight.
 * 3. Sort edge tuples based on their weight.
 * 4. Iterate over the edge tuple i.e. for each edge e(u, v) do,
 * 		a. For all the edges e(u', v') remaining in the edge list having edge weight equal to the 
 * 		current edge e(u, v) do,
 * 			i. Remove e(u', v') from the edge list
 * 			ii.If nodes u' & v' are not in the same cluster then,
 * 				(a) Add e(u', v') to the final edges list, which holds the edges which will belong 
 * 				to the final scaled network.
 * 				(b) Merge the clusters belonging to u' & v' such that smaller cluster is merged with
 * 				the bigger cluster & smaller cluster is deleted.
 * 
 * @author cdtank
 */
public class MSTPathfinderNetworkScalingComputation  implements NWBFileParserHandler {
	
	private static final double DEFAULT_EDGE_WEIGHT = 1.0; 
	
	private Map nodeIDToClusterID = new HashMap();
	private Map clusterIDToNodes = new HashMap();
	private List edges = new ArrayList();
	private List alternativeEdges = new ArrayList();
	public Set finalEdges = new HashSet();
	
	private int edgeCount;
	private String edgeWeightColumnName; 
	private String edgeWeightType;

	private LogService logger;

	public MSTPathfinderNetworkScalingComputation(String edgeWeightColumnName, 
			String edgeWeightType, LogService logger) {
		
		edgeCount = 0;
		this.edgeWeightType = edgeWeightType;
		this.edgeWeightColumnName = edgeWeightColumnName; 
		this.logger = logger;
	}


	/**
	 * Create edge tuple elements. Check for irregularities in input & 
	 * assume default values for edge weight.
	 * @param node1
	 * @param node2
	 * @param attributes
	 * @param isUnDirectedEdge In order to also set matrix element corresponding to [node2, node1] 
	 */
	private void createEdgeTupleElements(int node1, int node2, Map attributes, boolean isUnDirectedEdge) {

		double edgeWeight;
		
		/*
		 * To make sure that null edge weights represented by "*" are set to default edge 
		 * weight of 1.0 
		 * */
		try {
			edgeWeight = Double.parseDouble(attributes.get(edgeWeightColumnName).toString());
		}
		catch (Exception e) {
			
			/*
			 * If the weight is null, use the default weight
			 * */
			edgeWeight = DEFAULT_EDGE_WEIGHT;
		}
		
		/*
		 * Edge weight cannot be less than or equal to 0. Else assume default edge weight value & 
		 * print a warning.
		 * */
		if(edgeWeight <= 0) {
			edgeWeight = DEFAULT_EDGE_WEIGHT;
			logger.log(LogService.LOG_WARNING, "Edge weight for " + node1 + " - " + node2 + 
					" should be more than 0. Default " +
					"value of \"Edge Weight\" (" + DEFAULT_EDGE_WEIGHT + ") used.");
		}
		
		if(edgeWeightType.equalsIgnoreCase(MSTPathfinderNetworkScalingAlgorithm.WEIGHT_SIMILARITY)) {
			edgeWeight = 1 / edgeWeight;
		}
		
		edges.add(new EdgeTuple(edgeCount, node1, node2, edgeWeight));
		
		/*
		 * Since Optimal PFNET is mathematically union of all the MSTs we need to check for all
		 * possible minimum paths. These will be stored in alternativeEdges. 
		 * */
		alternativeEdges.add(new EdgeTuple(edgeCount, node1, node2, edgeWeight));
		
		edgeCount++;
		
	}
	
	public void addNode(int id, String label, Map attributes) {
		
		nodeIDToClusterID.put(id, id);
		
		/*
		 * In order to maintain a list of nodes for a cluster this is used.
		 * */
		List nodes = new ArrayList();
		nodes.add(id);
		clusterIDToNodes.put(id, nodes);
		
	} 

	public void addUndirectedEdge(int node1, int node2, Map attributes) {
		createEdgeTupleElements(node1, node2, attributes, true);
	}

	public void finishedParsing() {
		Collections.sort(edges);
		Collections.sort(alternativeEdges);
		
		/*
		 * Side affects the final edge list & clusterIDToNodes.
		 * */
		pruneEdgeList();
	}

	/*
	 * Responsible for assigning conforming edges to the final edge list. Also keeps track 
	 * of which nodes belong to which clusters. Modeled on Krushkal's MST algorithm.     
	 * */
	private void pruneEdgeList() {
		
		/*
		 * Iterate over all the edges to find minimum cost paths between two nodes.
		 * */
		for(Iterator edgeIterator = edges.iterator(); edgeIterator.hasNext(); ) {

			double currentEdgeWeight = ((EdgeTuple)edgeIterator.next()).getWeight();
			List clusterCandidatesForMerging = new ArrayList();
			
			/*
			 * Find all the edges having weights equal to the current edge. This works because 
			 * we have already sorted the edges in ascending order.
			 * */
			for(Iterator alternativeEdgesIterator = alternativeEdges.iterator(); alternativeEdgesIterator.hasNext(); ) {
				EdgeTuple currentAlternativeEdge = (EdgeTuple)alternativeEdgesIterator.next();
				double currentAlternativeEdgeWeight = currentAlternativeEdge.getWeight();
				
				if(currentAlternativeEdgeWeight == currentEdgeWeight){
					
					/*
					 * Remove the edge from further consideration. 
					 * */
					alternativeEdgesIterator.remove();
					
					/*
					 * Get nodes for current alternative edge for further processing.
					 * */
					int[] edgePoints = new int[2];
					edgePoints[0] = currentAlternativeEdge.getNode1();
					edgePoints[1] = currentAlternativeEdge.getNode2();
					int edgeID = currentAlternativeEdge.getNodeID();
					
					int cluster1ID = ((Integer) nodeIDToClusterID.get(edgePoints[0])).intValue();
					int cluster2ID = ((Integer) nodeIDToClusterID.get(edgePoints[1])).intValue();
					
					/*
					 * If the nodes connected by the current alternative edge are not in the same cluster 
					 * then add this edge id to the final edges list and merge the clusters.
					 * */
					if(cluster1ID != cluster2ID) {
						finalEdges.add(edgeID);
						mergeClusters(cluster1ID, cluster2ID);
					}
				}
				
				/*
				 * Since the edges are sorted by their weight no point in processing edges having higher 
				 * weight. Hence break the loop when first edge with higher weight is encountered.
				 * */
				else if(currentAlternativeEdgeWeight > currentEdgeWeight) {
					break;
				}
			}
		}
	}

	/**
	 * Used by {@link MSTPathfinderNetworkScalingAlgorithm} for printing the metadata.
	 * @return size of finalEdges list
	 */
	public int getScaledNetworkEdgeCount() {
		return finalEdges.size();
	}
	
	
	/**
	 * Responsible for merging 2 different clusters.
	 * @param cluster1ID
	 * @param cluster2ID
	 */
	private void mergeClusters(int cluster1ID, int cluster2ID) {


		/*
		 * Get nodes belonging to cluster ids.
		 * */
		List cluster1 = (List)clusterIDToNodes.get(cluster1ID);
		List cluster2 = (List)clusterIDToNodes.get(cluster2ID);
		
		/*
		 * In order to merge larger cluster with smaller cluster. Later delete smaller clusters.
		 * */
		if(cluster1.size() > cluster2.size()) {
			
			/*
			 * Reassign new cluster ids to nodes which are getting moved.
			 * */
			for(Iterator clusterIterator = cluster2.iterator(); clusterIterator.hasNext(); ) {
				nodeIDToClusterID.put((Integer)clusterIterator.next(), cluster1ID);
			}
			cluster1.addAll(cluster2);
			clusterIDToNodes.remove(cluster2ID);
		}
		else {
			for(Iterator clusterIterator = cluster1.iterator(); clusterIterator.hasNext(); ) {
				nodeIDToClusterID.put((Integer)clusterIterator.next(), cluster2ID);
			}
			cluster2.addAll(cluster1);
			clusterIDToNodes.remove(cluster1ID);
		}
	}

	/*
	 * Currently there is no support for Directed Edges.
	 * */
	public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {}
	
	public boolean haltParsingNow() {
		return false;
	}

	public void addComment(String comment) {}
	
	public void setDirectedEdgeCount(int numberOfEdges) {}

	public void setDirectedEdgeSchema(LinkedHashMap schema) {}

	public void setNodeCount(int numberOfNodes) {}

	public void setNodeSchema(LinkedHashMap schema) {}

	public void setUndirectedEdgeCount(int numberOfEdges) {}

	public void setUndirectedEdgeSchema(LinkedHashMap schema) {}

}
