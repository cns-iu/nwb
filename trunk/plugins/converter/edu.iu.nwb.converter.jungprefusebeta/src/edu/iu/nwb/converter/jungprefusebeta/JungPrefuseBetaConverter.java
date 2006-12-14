/*
 * Created on May 9, 2004
 */
package edu.iu.nwb.converter.jungprefusebeta;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import prefuse.data.Node;
import prefuse.data.Tuple;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.PredicateUtils;
import edu.uci.ics.jung.utils.UserDataContainer;

/**
 * Takes in a Graph, and returns a Prefuse Graph. The Prefuse Graph will be
 * of the same topology as the JUNG graph, but will be a Prefuse JungGraph
 * extends DefaultGraph object. Its vertices and edges will all be of type 
 * JungVertex extends DefaultVertex and JungEdge extends DefaultEdge. 
 * The Jung* classes contain a field that points back to the original JUNG
 * object.
 * 
 * @author danyelf
 */
public class JungPrefuseBetaConverter {
    
    public static prefuse.data.Graph getPrefuseGraph( Graph jungGraph ) {
        Map jungToPrefuseVertices = new HashMap();
        prefuse.data.Graph prefuseGraph = new prefuse.data.Graph(PredicateUtils.enforcesDirected(jungGraph));
        
        for (Iterator iter = jungGraph.getVertices().iterator(); iter.hasNext();) {
            Vertex vertex = (Vertex) iter.next();
            Node node = prefuseGraph.addNode();
            merge(vertex, node);
            jungToPrefuseVertices.put( vertex, node);
        }
        
        for (Iterator iter = jungGraph.getEdges().iterator(); iter.hasNext();) {
            Edge e = (Edge) iter.next();
            Node first = (Node) jungToPrefuseVertices.get(e.getEndpoints().getFirst());
			Node second = (Node) jungToPrefuseVertices.get(e.getEndpoints().getSecond());
			prefuse.data.Edge edge = prefuseGraph.addEdge(first, second);
            merge(e, edge);
        }


        return prefuseGraph;
    }


    private static void merge(UserDataContainer jungContainer, Tuple prefuseTuple) {
		
		Iterator keys = jungContainer.getUserDatumKeyIterator();
		
		while(keys.hasNext()) {
			Object realKey = keys.next();
			String key = realKey.toString();
			Object datum = jungContainer.getUserDatum(realKey);
			Class metadataClass = datum.getClass();
			if(!prefuseTuple.canSet(key, metadataClass)) {
				prefuseTuple.getSchema().addColumn(key, metadataClass);
			}
			prefuseTuple.set(key, datum);
		}
		
	}

}
