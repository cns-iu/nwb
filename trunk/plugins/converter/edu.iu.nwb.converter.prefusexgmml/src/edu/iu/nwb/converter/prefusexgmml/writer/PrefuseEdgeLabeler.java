package edu.iu.nwb.converter.prefusexgmml.writer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;

/*
 * Given a Graph, find each Edge and check its "label" attribute.
 * If none is specified (that is, the value is missing) then assign one.
 * 
 * Where no label can be inferred (see createSensibleEdgeLabel),
 * the smallest positive int that is not already being used as a label
 * on some Edge is chosen.
 * 
 * Note only *newly generated* ids need be unique; it is possible that
 * there could be collisions before entering, and this class will not
 * alter that.
 */
public class PrefuseEdgeLabeler {
	
	public static final String EDGE_LABEL_JOINING_STRING = " to ";
	public static final String DEFAULT_EDGE_LABEL = "edge";
	
	public static void assignMissingEdgeLabels(Graph graph) {
		Set usedEdgeLabels = findUsedEdgeLabels(graph);
		
    	Iterator edgeIterator = graph.getEdges();
		while ( edgeIterator.hasNext() ) {
			Edge edge = (Edge) edgeIterator.next();
			
			// If no label is specified, set one and record it as used.
			if ( edge.getAttribute(XGMMLGraphWriter.LABEL) == null ) {
				String label = createSensibleEdgeLabel(edge, usedEdgeLabels);
				edge.setAttribute(XGMMLGraphWriter.LABEL, label);
				usedEdgeLabels.add(label);
			}
		}
    }
	
	private static Set findUsedEdgeLabels(Graph graph) {
        Set usedEdgeLabels = new HashSet(graph.getEdgeCount()/2);
                
        Iterator edgeIterator = graph.getEdges();
        while ( edgeIterator.hasNext() ) {
            Edge edge = (Edge) edgeIterator.next();
            
            String edgeLabel = edge.getAttribute(XGMMLGraphWriter.LABEL);
            if ( edgeLabel != null )
            	usedEdgeLabels.add(edgeLabel);
        }
        return usedEdgeLabels;
    }
    
	/*
	 * "Sensible" because this is only a matter of opinion.
	 * We could as well set label="" unconditionally, if we preferred.
	 * 
	 * First, try using other attributes of edge.
	 * Failing that, try using attributes from the Nodes that edge connects.
	 * Failing all of that, use the smallest positive integer that is
	 * not already the label for some other Edge.
	 */
    private static String createSensibleEdgeLabel(Edge edge, Set usedEdgeLabels) {
    	// Try to use edge's name attribute
    	String name = edge.getAttribute(XGMMLGraphWriter.NAME);
    	if ( name != null )
    		return name;
    	
    	// Try to use edge's id attribute
    	String id = edge.getAttribute(XGMMLGraphWriter.ID);
    	if ( id != null )
    		return id;
    	
    	// Try to use the names of the nodes joined by this edge
    	String joinedNames =
    		createEdgeLabelFromAttributeOfJoinedNodes(edge, XGMMLGraphWriter.NAME);
    	if ( joinedNames != null )
    		return joinedNames;
    	
    	// Try to use the labels of the nodes joined by this edge
    	String joinedLabels =
    		createEdgeLabelFromAttributeOfJoinedNodes(edge, XGMMLGraphWriter.LABEL);
    	if ( joinedLabels != null )
    		return joinedLabels;
    	
    	// Try to use the ids of the nodes joined by this edge
    	String joinedIDs =
    		createEdgeLabelFromAttributeOfJoinedNodes(edge, XGMMLGraphWriter.ID);
    	if ( joinedIDs != null )
    		return joinedIDs;
    	
    	// Use the smallest positive int not already in use
    	int nextUnusedEdgeLabel = 0;
    	String uniqueNewEdgeLabel;
        do {
        	uniqueNewEdgeLabel = String.valueOf(++nextUnusedEdgeLabel);
        } while ( usedEdgeLabels.contains(uniqueNewEdgeLabel) );
        
    	return uniqueNewEdgeLabel;
    }
    
    private static String createEdgeLabelFromAttributeOfJoinedNodes(Edge edge, String attributeKey) {
    	String firstNodeValue 	= edge.getFirstNode().getAttribute(attributeKey);
    	String secondNodeValue 	= edge.getSecondNode().getAttribute(attributeKey);
    	
    	if ( firstNodeValue != null && secondNodeValue != null ) {
    		return firstNodeValue + EDGE_LABEL_JOINING_STRING + secondNodeValue;
    	}
    	else {
    		return null;
    	}
    }
}