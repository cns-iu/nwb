package edu.iu.iv.search.p2p.bfs;

import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.decorators.Indexer;

/**
 * Breadth First Search Algorithm
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class BreadthFirstSearch {

	private Graph graph;
	private int networkSize;
	private int searchCost;
	private Indexer indxr;
	private LogService logger;
	private String failReason = null;
	
	/*
	 * threshold above which search has to be passed to a node. 0.0 <= threshold < 1.0
	 * Threshold of 0.0 indicates BFS flooding.  
	 */ 
	private double threshold;
	
	public BreadthFirstSearch() {
		graph = null;
		networkSize = graph.numVertices();		
		searchCost = 0;
		threshold = 0.0;
	}
		
	/**
	 * Constructor for BreadthFirstSearch.
	 * Requires a graph to be passed that represents the network on which the search is to be performed. 
	 */
	public BreadthFirstSearch(Graph graph) {
		this.graph = graph;
		networkSize = graph.numVertices();		
		searchCost = 0;
		threshold = 0.0;
		indxr = Indexer.newIndexer(graph, 0);
		
	}
	
	/**
	 * Constructor for BreadthFirstSearch.
	 * Requires a graph to be passed that represents the network on which the search is to be performed.
	 * and the threshold  
	 */
	public BreadthFirstSearch(Graph graph, double threshold) {
		this.graph = graph;
		networkSize = graph.numVertices();		
		searchCost = 0;
		this.threshold = threshold;
		indxr = Indexer.newIndexer(graph, 0);
		
	}
	
	public void setLogger(LogService logger){
		this.logger = logger;
	}
    
 	/**
   	* Perform BFS on the network
   	* @param fromId - source node from where search begins
   	* @param toId - target node to be found
   	* @param threshold - threshold to perform the BFS
   	* @return true if node found, false otherwise
   	*/
  	public boolean searchNetwork(int fromId, int toId, double threshold)
  	{
  		if (graph == null) {
			logger.log(LogService.LOG_ERROR,"Network not found !");
			failReason = "Network not found !";
  			return false;
  		}
  		if (threshold >= 1.0 || threshold < 0.0) {
			logger.log(LogService.LOG_ERROR,"Invalid threshold ! It should be between 0.0 (inclusive) and 1.0 (exclusive)");
			failReason = "Invalid threshold ! It should be between 0.0 (inclusive) and 1.0 (exclusive)";  			
  			return false;
  		}
		this.threshold = threshold;
	  	boolean isDone = searchNetwork(fromId, toId);
	  	if (isDone) {
			return true;
	  	}
	  	else {
		  	return false;
	  	}
  	}

	/**
	* Perform BFS on the network
	* @param fromId - source node from where search begins
	* @param toId - target node to be found
	* @return true if node found, false otherwise
	*/
	public boolean searchNetwork(int fromId, int toId)
	{
		if (graph == null) {
			logger.log(LogService.LOG_ERROR,"Network not found !");
			failReason = "Network not found !";
			return false;
		}
		if (threshold >= 1.0 || threshold < 0.0) {
			logger.log(LogService.LOG_ERROR,"Invalid threshold ! It should be between 0.0 (inclusive) and 1.0 (exclusive)");
			failReason = "Invalid threshold ! It should be between 0.0 (inclusive) and 1.0 (exclusive)";  
			return false;
		}
		if (fromId >= networkSize || toId >= networkSize) {
			logger.log(LogService.LOG_ERROR,"Invalid node number!");
			failReason = "Invalid node number!";
			return false;
		}
		
		Random randNum = new Random();
	   	SearchQueue q = new SearchQueue();
	   	Stack stk = new Stack();
	   	int qval, currentIndex;
	   	Integer tmpnode;
	   	double probability;
	   	byte[] visited;
		ArchetypeVertex currentNode;
		Iterator neighIter;
		searchCost = 0;
		
		if(fromId == toId)
			return true;
 
		visited = new byte[networkSize];
 
		for(int i=0; i<networkSize; i++)
		   visited[i] = 0;
 
		q.Enqueue(fromId);
		visited[fromId] = 1;

		do
		{
			if(q.isEmpty())
			{
				  //System.out.println("hit bottom, going back up one level");		       
				  if (stk.empty())
				  {
					logger.log(LogService.LOG_ERROR,"Node not found!");
					failReason = "Node not found!";					
					return false;
				  }		 
				  tmpnode = (Integer)stk.pop();
				  q.Enqueue(tmpnode.intValue());
				  visited[tmpnode.intValue()] = 1;		               
				  for(int i=0; i<networkSize; i++)
					   visited[i] = 0;
			}
	 
			qval = q.Dequeue();
			//System.out.println("node: " + qval);
			searchCost++;
	      
			if (qval != toId)
		 	{
				  if(stk.search(new Integer(qval)) == -1)
					  stk.push(new Integer(qval));
	           
				  currentNode = indxr.getVertex(qval);
				  neighIter = currentNode.getNeighbors().iterator();
				  while (neighIter.hasNext()) {	
				  		  currentIndex = indxr.getIndex((ArchetypeVertex)neighIter.next());				  
						  if(visited[currentIndex] == 0)
						  {
							  probability = randNum.nextDouble();
							  if (probability >= threshold)
							  {
								  visited[currentIndex] = 1;
								  q.Enqueue(currentIndex);
							  }
						  }
				  }
			}
	   }
	   while (qval != toId);
	   
	   searchCost--;
 	   return true;
	}

	/**
	 * This method returns the graph on which search is performed
	 * @return graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * This method returns the network size 
	 * @return network size
	 */
	public int getNetworkSize() {
		return networkSize;
	}

	/**
	 * This method returns the search cost (no. of messages for the search)
	 * @return search cost
	 */
	public int getSearchCost() {
		return searchCost;
	}

	/**
	 * This method returns the threshold used to perform BFS
	 * @return threshold
	 */
	public double getThreshold() {
		return threshold;
	}

	/**
	 * This method sets the graph on which search is to be performed 
	 * @param graph
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
		indxr = Indexer.newIndexer(graph, 0);
	}

	/**
	 * This method sets the threhold with which BFS will be performed
	 * @param threshold
	 */
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	/**
	 * Get the reason because of which modelig failed
	 * @return reason
	 */
	public String getFailReason() {
		return failReason;
	}

}
