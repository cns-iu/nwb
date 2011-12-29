package edu.id.iv.modeling.p2p.hypergrid;

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
 * HyperGrid Network Model for Unstructured P2P Networks
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class HypergridNetwork {
	private int networkSize;
	private int degree;
	private Graph g = null;
	private float avgDegree;
	private int minDegree, maxDegree;
	private LogService logger;
	private String failReason = null;
	
	/**
	 * Constructor for HypergridNetwork. Initializes an UndirectedSparseGraph and sets the network size
	 * @param networkSize
	 */
	public HypergridNetwork(int networkSize, int degree, LogService logger) {
		this.networkSize = networkSize;
		this.degree = degree;
		g = new UndirectedSparseGraph();
		minDegree = maxDegree = 0;
		avgDegree = 0;
		this.logger = logger;
	}
	
	/**
	 * This method returns the Graph object built on HyperGrid network
	 * @return graph 
	 */
	public Graph getGraph() {
		return g;
	}
	
	
	/**
	* This method builds the unstructured P2P network based on Hypergrid topology
	* @return true if the network built, false otherwise
	*/
	public boolean buildHyperGridNetwork() {
		if (networkSize == 0) {
			logger.log(LogService.LOG_ERROR,"Invalid network size!");
			failReason = "Invalid network size!";
			return false;
		}
		if (degree > networkSize) {
			logger.log(LogService.LOG_ERROR,"Invalid parameters!");
			failReason = "Invalid parameters!";
			return false;
		}
		int current = 1;
		int level = 1;
		int parent, offset, randomNumber, first, tailUp;
		Random randomNums = new Random();
		UndirectedSparseVertex newNode, temp1Node, temp2Node;
 
		// Add the first node to the graph.. This is level 0.
		UndirectedSparseVertex initialNode = new UndirectedSparseVertex();
		g.addVertex(initialNode);
		g.setUserDatum("0", initialNode, UserData.SHARED);
//		System.out.println("Added Node 0");
		
		// build regular tree first.
		// keep track of current level, and build one level at a time.
		//
		while(current < networkSize)
		{
			newNode = new UndirectedSparseVertex();
			g.addVertex(newNode);
			g.setUserDatum(new Integer(current).toString(), newNode, UserData.SHARED);
//			System.out.println("Added Node "+ current);
			
			// hook up to parent
			parent = nodeParent(current, level);
			 
			//System.out.println("C " + current + " P " + parent + " L " + level +
			//      " last " + lastID(level));
					
					if (! newNode.isNeighborOf(
							(UndirectedSparseVertex)g.getUserDatum(new Integer(parent).toString())) ) {					
						g.addEdge(new UndirectedSparseEdge(newNode, 
							(UndirectedSparseVertex)g.getUserDatum(new Integer(parent).toString())));
					}
 
					// increment level if node is last on this level
			//
			if (current == lastID(level))
				level++;
     
			current++;
		}
		
		// fix current id and level number
	   current--;
	   if (current == lastID(level - 1))
		   level--;
 
	   // then do outer layer random wiring
	   offset = current - lastID(level -1);
	   first = current - offset + 1;
 
	   //System.out.println("C " + current + " off " + offset + " first " + first + " tail " + lastID(level-1));
 
	   parent = nodeParent(current, level);
	   tailUp = lastID(level-1);

	   for (int i = tailUp - 1; i >= parent; i--)
		{     
		  for (int j=0; j < degree; j++)
		  {
			  temp1Node = (UndirectedSparseVertex)(g.getUserDatum(new Integer(i).toString()));
			  if ( temp1Node.numNeighbors() < degree )
			  {
					randomNumber = randomNums.nextInt(tailUp - i);
					randomNumber++;
					temp2Node = (UndirectedSparseVertex)(g.getUserDatum(new Integer(i+randomNumber).toString()));
					if (temp2Node.numNeighbors() < degree)
					{
						if (! temp1Node.isNeighborOf(temp2Node) ) 													
							g.addEdge(new UndirectedSparseEdge(temp1Node, temp2Node) );												
				  	}
			  }
		  }
	  	}

		for(int i = 0; i < offset; i++)
		{
			for (int j=0; j < degree; j++)
			{
				temp1Node = (UndirectedSparseVertex)(g.getUserDatum(new Integer(current).toString()));
				if (temp1Node.numNeighbors() < degree)				
				{
					if (offset > 1 && (first+offset-1 - current > 0))
					{
					  // choose random leaf node
					  randomNumber = randomNums.nextInt(first+offset - 1 - current);
					  randomNumber++;
					  temp2Node = (UndirectedSparseVertex)(g.getUserDatum(new Integer(current+randomNumber).toString()));
					  if (temp2Node.numNeighbors() < degree)					  
					  {
						if (! temp1Node.isNeighborOf(temp2Node) ) 													
								g.addEdge(new UndirectedSparseEdge(temp1Node, temp2Node) );						
					  }
					}
				}
			}
			current--;
		}
		calculateStats();
		return true;
	} // end of function buildHyperGridNetwork
	
	
	/**
	 * Get id of the parent of a node in the tree
	 * @param current Current node, whose parent has to be found
	 * @param level Level on which the current node resides
	 * @return Id of the parent node
	 */
	private int nodeParent(int current, int level)
	{
		int offset;
 
		if (level == 1)
			return 0;
 
		offset = current - lastID(level -1);
 
		return lastID(level - 2) + (int)Math.ceil(((double)offset/(double)(degree-1)));
	}
 
 
	/**
	 * Get the id of the last node on a particular level of a tree
	 * @param level Level number 
	 * @return Id of the last node
	 */
	private int lastID(int level)
	{
		int k = degree - 1;
 
		if (level == 0)
			return 0;
		else if (degree == 2)
			return  (int)Math.pow((double)degree, (double)level);
		else
			return ((((int)Math.pow((double)k, (double)level)*degree) - 2)/(k-1)) - 1; 
	}
	
	/**
	* Print graph 
	*/
	public void printNetwork() {
	   UndirectedSparseVertex temp, tmpnode;
	   Iterator neigh_it;
	   float avg_deg = 0;
	   System.out.println("-----------------------------------------");
	   System.out.println("\t\tHypergrid Network");
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
       PajekNetWriter pnw = new PajekNetWriter() ;
	   try {
        pnw.save(g, filename);
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
} // end of class HypergridNetwork
