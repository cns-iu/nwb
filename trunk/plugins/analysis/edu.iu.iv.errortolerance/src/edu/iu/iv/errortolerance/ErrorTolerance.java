package edu.iu.iv.errortolerance;

import java.util.Random;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.Indexer;

/**
 * Perform Error Tolerance Test on the network graph. Randomly deletes nodes in the graph.
 * @author Hardik Sheth (hsheth@indiana.edu)
*/
public class ErrorTolerance {
	private int numNodes;
	private Graph graph;
	private Indexer indxr;
	
	/**
	 * Constructor for ErrorTolerance.
	 * Requires a graph to be passed that represents the network and the number of nodes to be deleted randomly. 
	 */
	public ErrorTolerance(Graph graph, int numNodes) {
		this.graph = graph;
		this.numNodes = numNodes;
		indxr = Indexer.newIndexer(graph, 0);
	}
	
	/**
	 * Perform error tolerance test on the graph.
	 * @return true when test done.
	 */
	public boolean testErrorTolerance() {
		Random randNum = new Random();
		int cnt = 0;
		int nodeNumber;
		
		while (cnt < numNodes) {
			/* Select a random node. Deletes all its edges and then delete the node. */
            
            //ran out of nodes
            if (graph.numVertices() == 0) break;
            
			nodeNumber = randNum.nextInt(graph.numVertices());	
			Vertex v = (Vertex) indxr.getVertex(nodeNumber);
			
            //Note: graph.removeVertex does this
			//graph.removeEdges(v.getIncidentEdges());
			
            graph.removeVertex(v);
			
            indxr.updateIndex();
            
			/* Increment count if node deleted from the graph */
			cnt++;
		}
		
		return true;
	} // end of testErrorTolerance

	/**
	 * Get the graph representing the network
	 * @return graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * @return Returns the number of nodes that are randomly deleted
	 */
	public int getNumNodes() {
		return numNodes;
	}

	/**
	 * Set the graph representing the network
	 * @param graph
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	/**
	 * Set the number of nodes that are randomly deleted
	 * @param i
	 */
	public void setNumNodes(int i) {
		numNodes = i;
	}

}
