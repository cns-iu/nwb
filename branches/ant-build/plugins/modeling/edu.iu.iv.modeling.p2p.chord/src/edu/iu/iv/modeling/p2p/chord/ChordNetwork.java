package edu.iu.iv.modeling.p2p.chord;

import java.io.IOException;
import java.util.Iterator;
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
 * CHORD Network Modeling Algorithm for Structured P2P Networks. 
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class ChordNetwork {

	private int networkSize;
//	Each node keeps track of a certain number of nodes given by fingerTableSize
	private int fingerTableSize;
	private Graph g = null;
	private float avgDegree;
	private int minDegree, maxDegree;
	private LogService logger;
	private String failReason = null;
		
	/**
	 * Constructor for ChordNetwork
	 * @param networkSize : size of the network
	 * @param fingerTableSize : minimum number of nodes on the ring a node will connect to
	 */
	public ChordNetwork(int networkSize, int fingerTableSize, LogService logger) {
		this.networkSize = networkSize;
		this.fingerTableSize = fingerTableSize;
		g = new UndirectedSparseGraph();	
		minDegree = maxDegree = 0;
		avgDegree = 0;
		this.logger = logger;
	}
	
	/**
	 * Get the Graph object representing the CHORD network
	 * @return graph
	 */
	public Graph getGraph() {
		return g;
	}
	
	/**
	 * Build the network based on CHORD topology 
	 * @return true if the network built successfully, false otherwise
	 */
	public boolean buildChordNetwork() 
	{
		UndirectedSparseVertex  newNode;
		
		if (networkSize == 0) {
			logger.log(LogService.LOG_ERROR, "Invalid network size!");
			failReason = "Invalid network size!";
			return false;
		}
		if (fingerTableSize > networkSize) {
			logger.log(LogService.LOG_ERROR, "Invalid parameters!");
			failReason = "Invalid parameters!";
			return false;
		}
	
//		Add nodes to the graph. 
//		To each graph, add a mapping to each node from a unique id
//		To each node, add id userdatum that will be used in search algorithm
		for (int i=0; i<networkSize; i++) {
			newNode = new UndirectedSparseVertex();
			newNode.setUserDatum("id", new Integer(i), UserData.SHARED);
			g.addVertex(newNode);
			g.setUserDatum(new Integer(i).toString(), newNode, UserData.SHARED);
		}

		if (networkSize >= 2) {
			fixNeighbors();	
		}
		calculateStats();
		return true;
	}	// end of function buildChordNetwork


	/**
	 * Connect nodes to 'fingerTableSize' number of other nodes on the network ring
	 *  valid for networks of size > 1 
	 */
	private void fixNeighbors() 
	{
		int temp;
		
//		Make the connections of the first and last node seperately to form a ring
		g.addEdge(new UndirectedSparseEdge(
					(UndirectedSparseVertex)g.getUserDatum("0"), 
					(UndirectedSparseVertex)g.getUserDatum("1") ));
		g.addEdge(new UndirectedSparseEdge(
							(UndirectedSparseVertex)g.getUserDatum("0"), 
							(UndirectedSparseVertex)g.getUserDatum(new Integer(networkSize-1).toString()) ));
		g.addEdge(new UndirectedSparseEdge(
									(UndirectedSparseVertex)g.getUserDatum(new Integer(networkSize-1).toString()), 
									(UndirectedSparseVertex)g.getUserDatum(new Integer(networkSize-2).toString()) ));
		
//		Update the connections of the first and the last node
		for(int k=0; k<fingerTableSize; k++)
		{
			temp = (int)Math.pow((double)2, (double)k);			
			
			if (! ((UndirectedSparseVertex)g.getUserDatum("0")).isNeighborOf(
					(UndirectedSparseVertex)g.getUserDatum(new Integer(temp % networkSize).toString())) ) {
				g.addEdge(new UndirectedSparseEdge(
						(UndirectedSparseVertex)g.getUserDatum("0"),						 
						(UndirectedSparseVertex)g.getUserDatum(new Integer(temp % networkSize).toString()) )); 
			}
			
			if (! ((UndirectedSparseVertex)g.getUserDatum(new Integer(networkSize-1).toString())).isNeighborOf(
					(UndirectedSparseVertex)g.getUserDatum(new Integer((networkSize-1+temp) % networkSize).toString())) ) {
				g.addEdge(new UndirectedSparseEdge(
									(UndirectedSparseVertex)g.getUserDatum(new Integer(networkSize-1).toString()), 
									(UndirectedSparseVertex)g.getUserDatum(new Integer((networkSize-1+temp)%networkSize).toString()) ));
			}						
		}
									
//		Now deal with the other nodes
		for (int i=1; i<networkSize-1; i++) {
//			Connect with previous and successor node
			if (! ((UndirectedSparseVertex)g.getUserDatum(new Integer(i).toString())).isNeighborOf(
					(UndirectedSparseVertex)g.getUserDatum(new Integer(i-1).toString())) ) {
				g.addEdge(new UndirectedSparseEdge(
						(UndirectedSparseVertex)g.getUserDatum(new Integer(i).toString()),
						(UndirectedSparseVertex)g.getUserDatum(new Integer(i-1).toString()) ));
			}
			
			if (! ((UndirectedSparseVertex)g.getUserDatum(new Integer(i).toString())).isNeighborOf(
					(UndirectedSparseVertex)g.getUserDatum(new Integer(i+1).toString())) ) {
				g.addEdge(new UndirectedSparseEdge(
						(UndirectedSparseVertex)g.getUserDatum(new Integer(i).toString()), 
						(UndirectedSparseVertex)g.getUserDatum(new Integer(i+1).toString()) ));
			}
			
//			Connect with other nodes on the ring at a distance i+2^k	
			for(int k=0; k<fingerTableSize; k++)
			{
				temp = (int)Math.pow((double)2, (double)k);
				
				if (! ((UndirectedSparseVertex)g.getUserDatum(new Integer(i).toString())).isNeighborOf(
						(UndirectedSparseVertex)g.getUserDatum(new Integer((i+temp)%networkSize).toString())) ) {
					g.addEdge(new UndirectedSparseEdge(
						(UndirectedSparseVertex)g.getUserDatum(new Integer(i).toString()), 
						(UndirectedSparseVertex)g.getUserDatum(new Integer((i+temp)%networkSize).toString()) ));						
				}				
			}
		}						
	}	// end of function fixNeighbors
	
	
	/**
	* Print graph 
	*/
	public void printNetwork() {
	   UndirectedSparseVertex temp, tmpnode;
	   Iterator neigh_it;
	   float avg_deg = 0;
	   System.out.println("-----------------------------------------");
	   System.out.println("\t\tCHORD Network");
	   System.out.println("Number of Nodes = " + g.numVertices()); 
	   for(int i=0; i<g.numVertices(); i++) {
			   temp = (UndirectedSparseVertex) g.getUserDatum(new Integer(i).toString());
			   System.out.println("Node "+ temp);
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
	   avg_deg /= g.numVertices();
	   System.out.println("\nAverage Degree = "+avg_deg);
	   System.out.println("-----------------------------------------");
	} 

	
   /**
   * Create a Pajek file
   */
   public void writePajekFile(String filename) {
       PajekNetWriter pnf = new PajekNetWriter();
		try {
           pnf.save(g, filename);
       } catch (IOException e) {
           String msg = "I/O Error while writing file: " + filename ;
           logger.log(LogService.LOG_WARNING, msg, e);
       }			
   }
	
   /**
	* Write the graph to a file
	*/
   public void writeGraph(String filename) {
	   GraphMLFile gmlf = new GraphMLFile();
	   gmlf.save(g, filename);
   }
   
   /**
	* Calculate Graph Statistics (min degree, max degree, avg degree).
	*/
   private void calculateStats() {		
	  UndirectedSparseVertex temp;
	  minDegree = ((UndirectedSparseVertex) g.getUserDatum(new Integer(1).toString())).degree();
	  maxDegree = 0;
	  for(int i=0; i<g.numVertices(); i++) {
			  temp = (UndirectedSparseVertex) g.getUserDatum(new Integer(i).toString());				
			  avgDegree += temp.degree();
			  if (temp.degree() < minDegree) {
				  minDegree = temp.degree();
			  }
			  if (temp.degree() > maxDegree) {
				  maxDegree = temp.degree();
			  }
	  }	 	
	  avgDegree /= g.numVertices();	 	
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

}	// end of class ChordNetwork
