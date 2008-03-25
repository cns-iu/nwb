package edu.iu.iv.search.p2p.randomwalk;

import java.util.Iterator;
import java.util.Random;

import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.decorators.Indexer;

/**
 * Random-Walk Search Algorithm
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class RandomWalk {

	private Graph graph;
	private int networkSize;
	private int searchCost;
	private int numWalkers;
	private Indexer indxr;	
	private LogService logger;
	private String failReason = null;
	
	/**
	 * Constructor for RandomWalk Search Algorithm.
	 * It requires that a graph object be passed that represents the network on which the search is to be performed. 	 
	 * @param graph
	 */	
	public RandomWalk(Graph graph) {
		this.graph = graph;
		networkSize = graph.numVertices();
		numWalkers = 1;
		searchCost = 0;
		indxr = Indexer.newIndexer(graph, 0);
		
	}
	
	/**
	 * Constructor for RandomWalk Search Algorithm.
	 * It requires that a graph object be passed that represents the network on which the search is to be performed and the number of walkers to perform the random walk 
	 * @param graph
	 * @param numWalkers
	 */	
		public RandomWalk(Graph graph, int numWalkers) {
			this.graph = graph;
			networkSize = graph.numVertices();
			this.numWalkers = numWalkers;
			searchCost = 0;
			indxr = Indexer.newIndexer(graph, 0);
			
		}
		
		public void setLogger(LogService logger){
			this.logger = logger;
			
		}

	/**
	 * Search random network
	 * @param fromId - source node from where search begins
	 * @param toId - target node to be found
	 * @param numWalkers - number of walkers to perform the search
	 * @return true if node found, false otherwise
	 */
	public boolean searchNetwork(int fromId, int toId, int numWalkers)
	{
		if (graph == null) {
			logger.log(LogService.LOG_ERROR,"Network not found !");
			failReason = "Network not found !";
			return false;
		}
		if (numWalkers <= 0) {
			logger.log(LogService.LOG_ERROR,"Invalid number of walkers !");
			failReason = "Invalid number of walkers !";
			return false;
		}
		this.numWalkers = numWalkers;
		boolean isDone = searchNetwork(fromId, toId);
		if (isDone) {
			return true;
		}
		else {
			return false;
		}
	}
	
 	/**
 	 * Search random network
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
		if (numWalkers <= 0) {
			logger.log(LogService.LOG_ERROR,"Invalid number of walkers !");
			failReason = "Invalid number of walkers !";
			return false;
		}
		if (fromId >= networkSize || toId >= networkSize) {
			System.err.println("Invalid node number!");
			logger.log(LogService.LOG_ERROR,"Invalid node number!");
			failReason = "Invalid node number!";
			return false;
		}
		
		// keeps track of the last node visited by each walker
		int lastNode[] = new int[numWalkers];
		
		// keep track of the current position of each walker
		int currentPosition[] = new int[numWalkers];
		
		// Global knowledge to all the walkers if still searching or not.
		boolean looking = true;
		
		boolean roundDone;		
		int past, next, currentIndex; 
		ArchetypeVertex currentNode;
		Iterator neighIter;
		searchCost = 0;
		Random rand = new Random();
 
		//System.out.println("f: " + fromID + " t: " + toID + " numWalkers: " + numWalkers );
 
		// Start from the source node.
		for(int i = 0; i< numWalkers; i++) {
			lastNode[i] = fromId;
			currentPosition[i] = fromId;
		}
  
  		// No need to search if already at the target node.
		for(int i = 0; i< numWalkers; i++) {
			if (currentPosition[i] == toId)
				looking = false;
		}
         
      // All walkers keep searching until looking=false                                                        
	  while (looking)
	  {
		  //System.out.println("walkers at step " + searchCost/numWalkers);
		  //for(int i = 0; i< numWalkers; i++)
		  //    System.out.print(i + ":" + walker[i] + " ");
		  //System.out.println(" ");
 
		// Process each walker
		for(int i = 0; i< numWalkers; i++)
		{
			roundDone = false;
            currentNode = indxr.getVertex(currentPosition[i]);
			// Process only if a node has more than one neighbor            
			if ( currentNode.numNeighbors() > 1 )
			{
				// chose random number between 0 and neighborCount - 1
				// and skip previous node.
				next = rand.nextInt( currentNode.numNeighbors() - 1 );	 
				//System.out.println("n: " + next + " searchCost: "
				//      + table[walker[i]].neighborCount + " hist " + history[i]); 
				neighIter = currentNode.getNeighbors().iterator();
				while (neighIter.hasNext()) {				
					//System.out.println("searchCost next: " + next);
					currentIndex = indxr.getIndex((ArchetypeVertex)neighIter.next());
					if( (next == 0) && (currentIndex != lastNode[i]) && !roundDone ) 
					{
						roundDone = true;
						past = currentPosition[i];
						currentPosition[i] = currentIndex;
						lastNode[i] = past;
						next--;
						//System.out.println(" to " + currentIndex);
					}
					else if ( (currentIndex != lastNode[i]) && !roundDone )
					{ 
						next--;
						//System.out.println(" decr on " + currentIndex);
					}
				}
			}
			// else a node has only 1 neighbor
			else if (currentNode.numNeighbors() == 1)
			{
				// just return to previous node
				neighIter = currentNode.getNeighbors().iterator();
				while (neighIter.hasNext()) {	
					currentIndex = indxr.getIndex((ArchetypeVertex)neighIter.next());
					if( (currentIndex != currentPosition[i]) && !roundDone)
					{
						//System.out.println("w:" + i
						//                   + " at " + walker[i]
						//                   + " return to " + j);
						past = currentPosition[i];
						currentPosition[i] = currentIndex;
						lastNode[i] = past;
						roundDone = true;
					}
					// connected just to itself. Isolated in space !
					else if (currentIndex == currentPosition[i])
					{						
						logger.log(LogService.LOG_ERROR,"isolated node in search!!");
						failReason = "isolated node in search!!";
						return false;
					}
				}
			}
			// Node has no neighbors.. Isolated in space !
			else
			{
				logger.log(LogService.LOG_ERROR,"isolated node in search!!");
				failReason = "isolated node in search!!";
				return false;
			}
		}
 
		// check for success and update counter
		for(int i = 0; i< numWalkers; i++)
		{
			if (currentPosition[i] == toId)
				looking = false;
 
			searchCost++;
		}
     
	  } // end of while (looking)
 
	  //System.out.println("walkers at step " + searchCost/numWalkers);
	  //for(int i = 0; i< numWalkers; i++)
	  //  System.out.print(i + ":" + walker[i] + " ");
	  //System.out.println(" "); 
	  return true;
	} // end of function searchNetwork
     
	/**
	 * Return the cost (number of the messages) of the search performed
	 * @return searchCost
	 */
	public int getSearchCost() {
		return searchCost;
	}

	/**
	 * Get the number of walkers used for search
	 * @return numWalkers
	 */
	public int getNumWalkers() {
		return numWalkers;
	}

	/**
	 * Get the reason because of which modelig failed
	 * @return reason
	 */
	public String getFailReason() {
		return failReason;
	}

} // end of class
