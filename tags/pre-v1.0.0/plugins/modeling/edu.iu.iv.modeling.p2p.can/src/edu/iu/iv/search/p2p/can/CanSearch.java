package edu.iu.iv.search.p2p.can;

import java.util.Iterator;
import java.util.Set;

import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseVertex;
import edu.uci.ics.jung.visualization.GraphDraw;

/**
 * Implements the CAN Search Algorithm. 
 * Requires the network in the form of a graph object on which the search is to be performed 
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class CanSearch {
	private UndirectedSparseGraph g;
	private int networkSize; 
	private int visited[];
	private int searchCost;	
	static GraphDraw gd;
	private LogService logger;
	private String failReason = null;

/**
 * Constructor for CanSearch algorithm. 
 * It requires that a graph object be passed that represents the network on which the search is to be performed.
 * Graph should have a userdatum attached to it that stores various information related to the network.
 * @param graph
 */
	public CanSearch(Graph graph , LogService logger) {
		g = (UndirectedSparseGraph) graph;		
		networkSize = g.numVertices();
//		System.out.println("networksize="+networkSize);
		visited = new int[networkSize];
		for(int i=0; i<networkSize; i++) {
			visited[i]=0;
		}
		this.logger = logger;
	}
	
	/**
	 * This method performs search on the graph, given the source and target nodes. 
	 * It calls the search method with appropriate parameters for actual search.
	 * @param fromId
	 * @param toId
	 * @return boolean
	 */
	public boolean searchNetwork(int fromId, int toId) {		
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
		UndirectedSparseVertex fromNode = (UndirectedSparseVertex) g.getUserDatum(new Integer(fromId).toString());
		UndirectedSparseVertex toNode = (UndirectedSparseVertex) g.getUserDatum(new Integer(toId).toString());
		searchCost = search(fromNode, toNode, 0, true);
//		System.out.println("Node found in "+searchCost+" messages");
		if (searchCost >= 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
		/**
		 * This method performs the actual search on the graph. 
		 * It returns the number of messages passed in the network for search.
		 * @param fromNode
		 * @param toNode
		 * @param count
		 * @param is_first
		 * @return search cost
		 */
		 private int search(UndirectedSparseVertex fromNode, UndirectedSparseVertex toNode, int count, boolean is_first)
		  {
			  int next;
			  double norm;
			  double minNorm;
			  UndirectedSparseVertex temp, nextNode;
 						
			  if(((Integer)fromNode.getUserDatum("id")).intValue() == ((Integer)toNode.getUserDatum("id")).intValue())
				  return count;
              
//			  if exceeded the network size, throw an error that node is not found
			  if(count == networkSize)
			   {
				   logger.log(LogService.LOG_INFO,"Maxed out on search!!");
				   failReason = "Maxed out on search!!";
				   return -1;
			   }
 
//			  Mark the node as visited so that you don't go back to that node
			  if (is_first) {
				for(int i=0; i<networkSize; i++) {
							visited[i]=0;
				}
				visited[((Integer)fromNode.getUserDatum("id")).intValue()] = 1;
			  }
			  else {
				visited[((Integer)fromNode.getUserDatum("id")).intValue()] = 1;	
			  }
			  
			            
			  minNorm = 1.8;  //i.e., ~sqrt(3), max length in unit cube
			  next = -1;
 
//			  Find the neighbor to whom search should be passed on in the direction of the target node
			  Set node_neighbors = fromNode.getNeighbors();
			  Iterator it = node_neighbors.iterator();
			  while (it.hasNext())
			   {
				   temp =  (UndirectedSparseVertex) it.next(); 
						norm = Math.sqrt(  
											Math.pow(((Double)temp.getUserDatum("xmid")).doubleValue() - ((Double)toNode.getUserDatum("xmid")).doubleValue(), 2.0)
										+ Math.pow(((Double)temp.getUserDatum("ymid")).doubleValue() - ((Double)toNode.getUserDatum("ymid")).doubleValue(), 2.0)
										+ Math.pow(((Double)temp.getUserDatum("zmid")).doubleValue() - ((Double)toNode.getUserDatum("zmid")).doubleValue(), 2.0)
										);
				         
						if((norm < minNorm) && (visited[((Integer)temp.getUserDatum("id")).intValue()] == 0))
						 {
							 next = ((Integer)temp.getUserDatum("id")).intValue();
							 minNorm = norm;
						 }						
			   }
			   
 
			  if (next == -1)
			   {
//				   System.out.println("Search failed!! fromNode: " + ((Integer)fromNode.getUserDatum("id")).intValue()
//				    								+ ", toNode: " + ((Integer)toNode.getUserDatum("id")).intValue() + ".");
					logger.log(LogService.LOG_INFO,"Node not found !");
				   failReason = "Node not found !";
				   return -1;
			   }
			  else 
			  {
				nextNode = (UndirectedSparseVertex) g.getUserDatum(new Integer(next).toString());				
			  	return search(nextNode, toNode, count+1, false);			  	
		  	  } 
		  }		// end of function search

	/**
	 * This methods returns the number of messages (i.e. Search Cost) passed in the network to perform search.
	 * @return search cost
	 */
	public int getSearchCost() {
		return searchCost;
	}

	/**
	 * Get the graph on which search was performed
	 * @return graph
	 */
	public UndirectedSparseGraph getGraph() {
		return g;
	}

	/**
	 * Get size of the network
	 * @return network size
	 */
	public int getNetworkSize() {
		return networkSize;
	}

	/**
	 * Set the graph to search
	 * @param graph
	 */
	public void setGraph(UndirectedSparseGraph graph) {
		g = graph;
	}

	/**
	 * Get the reason because of which modelig failed
	 * @return reason
	 */
	public String getFailReason() {
		return failReason;
	}
	
} // end of class CanSearch