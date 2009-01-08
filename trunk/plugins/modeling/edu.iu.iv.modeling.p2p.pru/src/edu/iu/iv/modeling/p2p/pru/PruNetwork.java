package edu.iu.iv.modeling.p2p.pru;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.UndirectedSparseVertex;
import edu.uci.ics.jung.io.GraphMLFile;
import edu.uci.ics.jung.io.PajekNetWriter;
import edu.uci.ics.jung.utils.UserData;

/**
 * PRU Network Model for Unstructured P2P Networks
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class PruNetwork {
	private int networkSize;
	private int cacheSize;
	private int lowerBound;
	private int upperBound;
	private int[] hostCache;
	private Graph graph = null;
	private float avgDegree;
	private int minDegree, maxDegree;
	private LogService logger;
	private String failReason = null;
	
	/**
	 * Constructor for PRU Network model
	 */
	public PruNetwork() {
		networkSize = 0;
		cacheSize = 0;
		lowerBound = 0;
		upperBound = 0;
		graph = new UndirectedSparseGraph();		
		minDegree = maxDegree = 0;
		avgDegree = 0;
		
	}
	
	/**
	 * Constructor for PRU Network model.
	 * Sets the network size, node degree, minimum degree and maximum degree.
	 * @param networkSize size of the network
	 * @param cacheSize number of nodes in the cache
	 * @param lowerBound minimum degree
	 * @param upperBound maximum degree
	 */
	public PruNetwork(int networkSize, int cacheSize, int lowerBound, int upperBound, LogService logger) {
		this.networkSize = networkSize;
		this.cacheSize = cacheSize;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		graph = new UndirectedSparseGraph();
		hostCache = new int[cacheSize];
		for(int i = 0; i < cacheSize; i++)
			hostCache[i] = -1;
		minDegree = maxDegree = 0;
		avgDegree = 0;
		this.logger = logger;
	}

	/**
	 * Build PRU Network Model
	 * @return true if the network built, false otherwise
	 */
	public boolean buildPruNetwork()
	{
		UndirectedSparseVertex newNode, tempNode;
		
		if (networkSize == 0) {
			logger.log(LogService.LOG_ERROR,"Invalid network size!");
			failReason = "Invalid network size!";
			return false;
		}
		if (cacheSize < lowerBound) {
			logger.log(LogService.LOG_ERROR,"Invalid cache size!");
			failReason = "Invalid cache size!";
			return false;
		}
		
		// Add nodes to the graph
		for (int i=0; i<networkSize; i++) {
			newNode = new UndirectedSparseVertex();
			newNode.setUserDatum("inCache", new Boolean(false), UserData.SHARED);
			graph.addVertex(newNode);
			graph.setUserDatum(new Integer(i).toString(), newNode, UserData.SHARED);   
		}	
		
	   // fill cache with first lowerBound nodes
	   for(int i=0; i < lowerBound; i++)
		{
			hostCache[i] = i;
			tempNode =  (UndirectedSparseVertex) graph.getUserDatum(new Integer(i).toString());
			tempNode.setUserDatum("cacheParent", new Integer(-1), UserData.SHARED);
			tempNode.setUserDatum("inCache", new Boolean(true), UserData.SHARED);
			for(int j=0; j < lowerBound; j++) {
				if (i != j) {
					if (! tempNode.isNeighborOf((UndirectedSparseVertex)graph.getUserDatum(new Integer(j).toString())) ) {					
						graph.addEdge(new UndirectedSparseEdge(tempNode, 
							(UndirectedSparseVertex)graph.getUserDatum(new Integer(j).toString())));
					}
				}
			}
		}
 
	   // build rest of network
	   //
	   for(int i=lowerBound; i < networkSize; i++)
		{
			// check if i < cacheSize
			// if so, hook up to random(i) * lowerBound nodes and
			// add new node to cache
			if (i < cacheSize)
			 {
				 joinNet(i, i);
			 }
			else // normal network-join
			 {
				 joinNet(i, -1);
			 }
		}
		calculateStats();
		return true;
	} // end of function buildNetwork
	
	
	/**
	 * Join a new node
	 * @param node
	 * @param cacheFlag
	 */
	private void joinNet(int node, int cacheFlag)
	 {
	 	 UndirectedSparseVertex tempNode;
		 Random ranNum = new Random();
		 int cIndex;
		 int neigh;
		 tempNode =  (UndirectedSparseVertex) graph.getUserDatum(new Integer(node).toString());
		 if(cacheFlag > -1)
		  {
				hostCache[node] = node;				
				tempNode.setUserDatum("cacheParent", new Integer(-1), UserData.SHARED);
				tempNode.setUserDatum("inCache", new Boolean(true), UserData.SHARED);
				for(int i=0; i<lowerBound; i++)
				{
				   cIndex = ranNum.nextInt(node);
				   neigh = hostCache[cIndex];
				   if (! tempNode.isNeighborOf((UndirectedSparseVertex)graph.getUserDatum(new Integer(neigh).toString())) )
						connectNodes(node, neigh, cIndex);			   
				   else 
				   		i--;			   
				}
		  }
		 else
		  {
				tempNode.setUserDatum("cacheParent", new Integer(-1), UserData.SHARED);
				for(int i=0; i<lowerBound; i++)
				{
				   cIndex = ranNum.nextInt(cacheSize);
				   neigh = hostCache[cIndex];
				   if (! tempNode.isNeighborOf((UndirectedSparseVertex)graph.getUserDatum(new Integer(neigh).toString())) )
						connectNodes(node, neigh, cIndex);			   
				   else 
						i--;			   
				}
		  }
	 } // end of function joinNet
	

	/**
	 * Connects a new node to a single node in the cache and
	 * implements the cache replacement rule if the cache node reaches degree upperBound
	 * @param newNodeId
	 * @param cacheNodeId
	 * @param cIndex
	 */
	private void connectNodes(int newNodeId, int cacheNodeId, int cIndex)
	 {
		 int current, goodNeighbor, parent;
		 boolean notDone;
		 UndirectedSparseVertex newNode, cacheNode, currentNode, parentNode, tempNode, goodNeighborNode;
		 
		 newNode = (UndirectedSparseVertex)graph.getUserDatum(new Integer(newNodeId).toString());
		 cacheNode = (UndirectedSparseVertex)graph.getUserDatum(new Integer(cacheNodeId).toString());
		 
		 if (! newNode.isNeighborOf(cacheNode) ) 					
				graph.addEdge(new UndirectedSparseEdge(newNode, cacheNode));     

		 goodNeighbor = -1;
 
		 // cache replacement rule
		 if (cacheNode.numNeighbors() >= upperBound)
		 {
			  current = cacheNodeId;			  
			  notDone = true;
			  while(notDone)
			   {
					currentNode = (UndirectedSparseVertex)graph.getUserDatum(new Integer(current).toString());
			   		parent = ((Integer)currentNode.getUserDatum("cacheParent")).intValue();
			   		parentNode = (UndirectedSparseVertex)graph.getUserDatum(new Integer(parent).toString());
					if (parent == -1)
					{
						goodNeighbor = newNodeId;
						notDone = false;
					}
					else if( (((Boolean)parentNode.getUserDatum("inCache")).booleanValue()) 
												|| (parentNode.numNeighbors() > lowerBound))
					{
						goodNeighbor = -1;
						for(int i=0; i< networkSize; i++) {
							tempNode = (UndirectedSparseVertex)graph.getUserDatum(new Integer(i).toString());
							if( (parentNode.isNeighborOf(tempNode)) &&
								 (! ((Boolean)tempNode.getUserDatum("inCache")).booleanValue()) &&
								 (tempNode.numNeighbors() == lowerBound) ) {
									goodNeighbor = i;		 	 
							}
							if(goodNeighbor > -1)
								notDone = false;
							else
								current = parent;								
						}
					}
					else // found good swap
				 	{
						goodNeighbor = parent;
					 	notDone = false;
				 	}
			   }	
			   
			   goodNeighborNode = (UndirectedSparseVertex)graph.getUserDatum(new Integer(goodNeighbor).toString());
			   if (! cacheNode.isNeighborOf(goodNeighborNode) ) 					
						   graph.addEdge(new UndirectedSparseEdge(cacheNode, goodNeighborNode));     
			   cacheNode.setUserDatum("inCache", new Boolean(false), UserData.SHARED);	
			   goodNeighborNode.setUserDatum("cacheParent", new Integer(cacheNodeId), UserData.SHARED);
			   goodNeighborNode.setUserDatum("inCache", new Boolean(true), UserData.SHARED);	
			   hostCache[cIndex] = goodNeighbor;			  
		 }
	 } // end of function connectNodes


	/**
	 * Returns the number of nodes in the cache
	 * @return number of nodes in the cache
	 */
	public int getCacheSize() {
		return cacheSize;
	}

	/**
	 * Returns the network in the form of a graph
	 * @return graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Returns the host cache of nodes
	 * @return hostCache
	 */
	public int[] getHostCache() {
		return hostCache;
	}

	/**
	 * Returns the minimum degree of nodes in the network
	 * @return minimum degree
	 */
	public int getLowerBound() {
		return lowerBound;
	}

	/**
	 * Returns the size of the network
	 * @return network size
	 */
	public int getNetworkSize() {
		return networkSize;
	}

	/**
	 * Returns the maximum degree of nodes in the network
	 * @return maximum degree
	 */
	public int getUpperBound() {
		return upperBound;
	}

	/**
	 * Sets the number of nodes in the cache
	 * @param cacheSize
	 */
	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
		hostCache = new int[cacheSize];
		for(int i = 0; i < cacheSize; i++)
			hostCache[i] = -1;
	}

	/**
	 * Sets the minimum degree of nodes in the network
	 * @param minDegree
	 */
	public void setLowerBound(int minDegree) {
		lowerBound = minDegree;
	}

	/**
	 * Sets the size of the network
	 * @param networkSize
	 */
	public void setNetworkSize(int networkSize) {
		this.networkSize = networkSize;
	}

	/**
	 * Sets the maximum degree of nodes in the network
	 * @param maxDegree
	 */
	public void setUpperBound(int maxDegree) {
		upperBound = maxDegree;
	}

	/**
	* Print the network graph 
	*/
	public void printNetwork() {
	   UndirectedSparseVertex temp, tmpnode;
	   Iterator neigh_it;
	   float avg_deg = 0;
	   System.out.println("\t\tPRU Network Model for Unstructured P2P Networks");
	   System.out.println("Number of Nodes = " + graph.numVertices()); 
	   for(int i=0; i<graph.numVertices(); i++) {
			   temp = (UndirectedSparseVertex) graph.getUserDatum(new Integer(i).toString());
//			   print node information
			   System.out.println("Node "+temp);
			   System.out.println("\tcacheParent="+temp.getUserDatum("cacheParent")+", inCache="+temp.getUserDatum("inCache"));
			   System.out.println("\tDegree="+temp.degree());
			   avg_deg += temp.degree();
			   System.out.print("\tNeighbors: ");
			   Set node_neighbors = temp.getNeighbors();
			   neigh_it = node_neighbors.iterator();
			   while( neigh_it.hasNext() ) {
				   tmpnode = (UndirectedSparseVertex) neigh_it.next();
				   System.out.print(tmpnode+"\t");
			   }
			   System.out.println();
	   }	 	
	   avg_deg /= graph.numVertices();
	   System.out.println("\nAverage Degree = "+avg_deg);
	} //end of function printNetwork

	
   /**
   * Create a Pajek file
   */
   public void writePajekFile(String filename) {
	   PajekNetWriter pnw = new PajekNetWriter() ;
	   try {
        pnw.save(this.graph, filename) ;
	   } catch (IOException e) {
       String msg = "I/O exception occurred while saving file: " + filename ;
       logger.log(LogService.LOG_ERROR, msg, e);
    }
   }
	
   /**
	* Write the graph to a file
	*/
   public void writeGraph(String filename) {
	   GraphMLFile gmlf = new GraphMLFile();
	   gmlf.save(graph, filename);
   }

   /**
	* Calculate Graph Statistics (min degree, max degree, avg degree).
	*/
   private void calculateStats() {		
	  UndirectedSparseVertex temp;
	  minDegree = ((UndirectedSparseVertex) graph.getUserDatum(new Integer(1).toString())).degree();
	  maxDegree = 0;
	  for(int i=0; i<graph.numVertices(); i++) {
			  temp = (UndirectedSparseVertex) graph.getUserDatum(new Integer(i).toString());				
			  avgDegree += temp.degree();
			  if (temp.degree() < minDegree) {
				  minDegree = temp.degree();
			  }
			  if (temp.degree() > maxDegree) {
				  maxDegree = temp.degree();
			  }
	  }	 	
	  avgDegree /= graph.numVertices();	 	
   }
   
   /**
	* Get average degree of the network
	* @return average degree
	*/
   public float getAvgDegree() {
	   return avgDegree;
   }

   /**
	* Get max degree of the network
	* @return max degree
	*/
   public int getMaxDegree() {
	   return maxDegree;
   }

   /**
	* Get min degree of the network
	* @return min degree
	*/
   public int getMinDegree() {
	   return minDegree;
   } 
   
   /**
	* Get the reason because of which modelig failed
	* @return reason
	*/
   public String getFailReason() {
	   return failReason;
   }
}
