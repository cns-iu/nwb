package edu.iu.iv.modeling.p2p.randomnetwork;

import java.util.Iterator;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.Indexer;
import edu.uci.ics.jung.graph.impl.AbstractSparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.random.generators.ErdosRenyiGenerator;

/**
 * Generates Erdos-Renyi Random Graph
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class RandomNetwork {
	private int networkSize;
	private double wiringProbability;
	private Graph g = null;
	private float avgDegree;
	private int minDegree, maxDegree;
	
	public RandomNetwork(int size, double probability) {
		networkSize = size;
		wiringProbability = probability;
		g = new UndirectedSparseGraph();
	}
	
	public void buildRandomNetwork() {
		ErdosRenyiGenerator erg = new ErdosRenyiGenerator(networkSize, wiringProbability);
		g = (UndirectedSparseGraph) erg.generateGraph();	
		calculateStats();	
	}
	
	/**
	 * Calculate Graph Statistics (min degree, max degree, avg degree).
	 */
	private void calculateStats() {		
	   Vertex temp;
	   Iterator iter = g.getVertices().iterator();	   
	   minDegree = networkSize;
	   maxDegree = 0;
	   while(iter.hasNext()) {
			   temp = (AbstractSparseVertex) iter.next();				
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

	public Graph getGraph() {
		return g;
	}
		
	/**
	 * @return connection probability
	 */
	public double getWiringProbability() {
		return wiringProbability;
	}
	
//	 Print graph - for testing
	public void printNetwork() {
	   Vertex temp, tmpnode;
	   Iterator vertices_iter, neigh_it;
	   
//	   Indexer to index all the nodes in the graph
	   Indexer indxr = Indexer.newIndexer(g, 0);
	   Set vertices = g.getVertices();
	   vertices_iter = vertices.iterator();
	   
	   System.out.println("Number of Nodes = " + g.numVertices()); 
//	   Iterate through all the nodes and print its statistics
	   while (vertices_iter.hasNext()) {
	   		   temp = (Vertex) vertices_iter.next();	   		   
//			   print node information
			   System.out.println("Node "+indxr.getIndex(temp));			   
			   System.out.println("\tDegree="+temp.degree());
			   System.out.print("\tNeighbors: ");
			   Set node_neighbors = temp.getNeighbors();
			   neigh_it = node_neighbors.iterator();
			   while( neigh_it.hasNext() ) {
				   tmpnode = (Vertex) neigh_it.next();
				   System.out.print(indxr.getIndex(tmpnode)+"\t");
			   }
			   System.out.println();
	   }	 	
	} //end of function printNetwork

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

} // end of class RandomNetwork
