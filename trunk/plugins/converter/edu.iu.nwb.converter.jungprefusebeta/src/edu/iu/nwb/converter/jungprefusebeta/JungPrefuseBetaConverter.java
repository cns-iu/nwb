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

public class JungPrefuseBetaConverter {    
	private prefuse.data.Graph prefuseGraph;
	
    public prefuse.data.Graph getPrefuseGraph( Graph jungGraph ) {
        Map jungToPrefuseVertices = new HashMap();
        prefuseGraph = new prefuse.data.Graph(PredicateUtils.enforcesDirected(jungGraph));
        
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


    private void merge(UserDataContainer jungContainer, Tuple prefuseTuple) {		
		Iterator keys = jungContainer.getUserDatumKeyIterator();
		
		while(keys.hasNext()) {
			Object realKey = keys.next();
			String key = realKey.toString();
			Object datum = jungContainer.getUserDatum(realKey);
			Class metadataClass = datum.getClass();
			if(!prefuseTuple.canSet(key, metadataClass)) {
				prefuseTuple.getTable().addColumn(key, metadataClass);
			}
			// Special cases that Prefuse refuses to set for
			if(!"target".equals(key) && !"source".equals(key)) {
				prefuseTuple.set(key, datum);
			}
		}		
	}
}
