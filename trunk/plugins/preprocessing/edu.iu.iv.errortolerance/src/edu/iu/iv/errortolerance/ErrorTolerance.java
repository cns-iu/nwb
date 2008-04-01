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
	
	/**
	 * Perform error tolerance test on the graph.
	 * @return true when test done.
	 */
	public static Graph testErrorTolerance(final Graph graph, int numberOfDeletedNodes) {
		Random randNum = new Random(); //Probably want to be able to take a seed for repeatability.
		Graph errorGraph = (Graph) graph.copy();
		Indexer indxr = Indexer.newIndexer(errorGraph, 0);
		int count = 0;
		int nodeNumber;
		
		while (count < numberOfDeletedNodes) {
			/* Select a random node. Deletes all its edges and then delete the node. */
            
            //ran out of nodes
            if (errorGraph.numVertices() == 0) break;
            
			nodeNumber = randNum.nextInt(errorGraph.numVertices());	
			Vertex v = (Vertex) indxr.getVertex(nodeNumber);
			
            //Note: graph.removeVertex does this
			//graph.removeEdges(v.getIncidentEdges());
			
            errorGraph.removeVertex(v);
			
            indxr.updateIndex();
            
			/* Increment count if node deleted from the graph */
			count++;
		}
		
		return errorGraph;
	} // end of testErrorTolerance


}
