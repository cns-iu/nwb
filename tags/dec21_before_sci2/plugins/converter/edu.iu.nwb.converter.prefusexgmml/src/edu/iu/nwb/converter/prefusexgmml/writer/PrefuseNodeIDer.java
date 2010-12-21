package edu.iu.nwb.converter.prefusexgmml.writer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;

/*
 * Given a Graph, find each Node and check its "id" attribute.
 * If none is specified (that is, the value is missing),
 * then use the smallest positive int that is not already in use.
 * 
 * Note only *newly generated* ids need be unique; it is possible that
 * there could be collisions before entering, and this class will not
 * alter that.
 */
public class PrefuseNodeIDer {

	public static Graph assignMissingIDs(Graph graph) {
		assignNodeIDs(graph);
		
		/* assignNodeIDs potentially renumbers Node ids.
		 * Since Edges used those IDs to record which two Nodes they connect,
		 * each Edge must now have its source and target attributes
		 * brought up-to-date.
		 */
		updateAllEdgeSourcesAndTargets(graph);
		
		return graph;
	}
	
	private static void assignNodeIDs(Graph graph) {
	    Set usedNodeIDs = findUsedNodeIDs(graph);	    
        int nextUnusedID = 0;
        
		Iterator nodeIterator = graph.getNodes();
		while ( nodeIterator.hasNext() ) {
			Node node = (Node) nodeIterator.next();
			
			if ( node.getAttribute(XGMMLGraphWriter.ID) == null ) {
				// Use the smallest positive int not already in use
				String uniqueNewNodeID;
                do {
                    uniqueNewNodeID = String.valueOf(++nextUnusedID);
                } while ( usedNodeIDs.contains(uniqueNewNodeID) );
                
				node.setAttribute(XGMMLGraphWriter.ID, uniqueNewNodeID);
			}
		}
	}
    
    private static Set findUsedNodeIDs(Graph graph) {
        Set usedNodeIDs = new HashSet(graph.getNodeCount()/2);
                
        Iterator nodeIterator = graph.getNodes();
        while ( nodeIterator.hasNext() ) {
            Node node = (Node) nodeIterator.next();
            
            String nodeID = node.getAttribute(XGMMLGraphWriter.ID);
            if ( nodeID != null )
                usedNodeIDs.add(nodeID);
        }
        return usedNodeIDs;
    }
    
    private static void updateAllEdgeSourcesAndTargets(Graph graph) {
		Iterator edgeIterator = graph.getEdges();
		while ( edgeIterator.hasNext() ) {
			Edge edge = (Edge) edgeIterator.next();

			String source = edge.getFirstNode().getAttribute(XGMMLGraphWriter.ID);
			edge.setAttribute(XGMMLGraphWriter.SOURCE, source);
			
			String target = edge.getSecondNode().getAttribute(XGMMLGraphWriter.ID);
			edge.setAttribute(XGMMLGraphWriter.TARGET, target);
		}		
	}
}
