package edu.iu.nwb.converter.prefusexgmml.writer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;

/*
 * Given a Graph, find each Node and check its "label" attribute.
 * If none is specified (that is, the value is missing) then assign one.
 * 
 * Where no label can be inferred (see createSensibleNodeLabel),
 * the smallest positive int that is not already being used as a label
 * on some Node is chosen.
 * 
 * Note only *newly generated* labels need be unique; it is possible that
 * there could be collisions before entering, and this class will not
 * alter that.
 */
public class PrefuseNodeLabeler {	

	public static void assignMissingNodeLabels(Graph graph) {
		Set usedNodeLabels = findUsedNodeLabels(graph);	    
		
		Iterator nodeIterator = graph.getNodes();
		while ( nodeIterator.hasNext() ) {
			Node node = (Node) nodeIterator.next();
			
			// If no label is specified, set one and record it as used.
			if ( node.getAttribute(XGMMLGraphWriter.LABEL) == null ) {
				String label = createSensibleNodeLabel(node, usedNodeLabels);
				node.setAttribute(XGMMLGraphWriter.LABEL, label);
				usedNodeLabels.add(label);
			}
		}
	}
	
	private static Set findUsedNodeLabels(Graph graph) {
        Set usedNodeLabels = new HashSet(graph.getNodeCount()/2);
                
        Iterator nodeIterator = graph.getNodes();
        while ( nodeIterator.hasNext() ) {
            Node node = (Node) nodeIterator.next();
            
            String nodeLabel = node.getAttribute(XGMMLGraphWriter.LABEL);
            if ( nodeLabel != null )
            	usedNodeLabels.add(nodeLabel);
        }
        return usedNodeLabels;
    }
	
	/*
	 * "Sensible" because this is only a matter of opinion.
	 * We could as well set label="" unconditionally, if we preferred.
	 * 
	 * First, try using other attributes of node.
	 * Failing that, use the smallest positive integer that is
	 * not already the label for some other Node. 
	 */
	private static String createSensibleNodeLabel(Node node, Set usedNodeLabels) {
    	// Try to use the name attribute
    	String name = node.getAttribute(XGMMLGraphWriter.NAME);
    	if ( name != null ) {
    		return name;
    	}
    	
    	// Try to use the id attribute
    	String id = node.getAttribute(XGMMLGraphWriter.ID);
    	if ( id != null ) {
    		return id;
    	}
    	
    	// Use the smallest positive int not already in use
    	int nextUnusedNodeLabel = 0;
		String uniqueNewNodeLabel;
        do {
            uniqueNewNodeLabel = String.valueOf(++nextUnusedNodeLabel);
        } while ( usedNodeLabels.contains(uniqueNewNodeLabel) );
    	return uniqueNewNodeLabel;
    }
}
