package edu.iu.iv.search.p2p.chord;

import java.util.Iterator;

import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseVertex;

/**
 * Implements the Chord Search Algorithm on Chord p2p network
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class ChordSearch {
	private UndirectedSparseGraph g;
	private int networkSize; 
	private int searchCost;	
	private LogService logger;
	private String failReason = null;

	/**
	 * Constructor for ChordSearch algorithm. 
	 * Initialize the network by passing Graph object on which search will be performed.
	 * @param graph - Graph object representing the CHORD network on which the search is to be performed.
	 */
	public ChordSearch(Graph graph,LogService logger) {
		g = (UndirectedSparseGraph) graph;
		networkSize = g.numVertices();
		searchCost=0;
		this.logger = logger;
	}

	/**
	 * This method performs search on the graph, given the source and target nodes.	 
	 * @param fromId - Id of the source node
	 * @param toId - Id of the target node
	 * @return true if the search succeeds, false otherwise
	 */
	public boolean searchNetwork(int fromId, int toId) {		
		int max, current, nodeIndex;
		UndirectedSparseVertex tmpNode, currentNode;
		Iterator it;
		
//		Initialize the search cost
		searchCost=0;

//		System.out.println("Source Node = "+fromId+" \nTarget Node = "+toId);
		if (g == null) {
			logger.log(LogService.LOG_INFO,"Network not found !");
			failReason = "Network not found !";
			return false;
		}
		if (fromId >= networkSize || toId >= networkSize) {
			logger.log(LogService.LOG_INFO,"Invalid node number!");
			failReason = "Invalid node number!";
			return false;
		}
		
//		Repeart search until node is found
		while (fromId != toId)
		{
			max = networkSize;
			current = fromId;

// 			Iterate through all the neighbors to pass the search to a node close to the target node
			currentNode = (UndirectedSparseVertex) g.getUserDatum(new Integer(current).toString());
			it = currentNode.getNeighbors().iterator();
			while(it.hasNext())
			{
					tmpNode = (UndirectedSparseVertex) it.next();
					nodeIndex = ((Integer)tmpNode.getUserDatum("id")).intValue();
					if(nodeIndex == toId)	// found the node
					{
						current = nodeIndex;
						max = 0;
					}
					else if(nodeIndex < toId)
					{
						if ((toId - nodeIndex) < max)
						{
							current = nodeIndex;
							max = toId - nodeIndex;
						}
					}
					else // nodeIndex > toId
					{
						if ( ((networkSize - nodeIndex) + toId) < max )
						{
							current = nodeIndex;
							max = (networkSize - nodeIndex) + toId;
						} 
					}
			}
             
			fromId = current;
			searchCost++; 
		}
				
		if (searchCost >= 0) {
//			System.out.println("Node found in "+searchCost+" messages");
			return true;
		}
		else {
			logger.log(LogService.LOG_INFO,"Node not found!!");
			failReason = "Node not found!!";
			return false;
		}		
	} // end of function searchNetwork
	
	/**
	 * This methods returns the search cost (number of messages to perform search) for the network
	 * @return Search cost
	 */
	public int getSearchCost() {
		return searchCost;
	}
	
	/**
	 * Get the reason because of which modelig failed
	 * @return reason
	 */
	public String getFailReason() {
		return failReason;
	}

}	// end of class ChordSearch
