package edu.iu.nwb.converter.jungprefusebeta;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Tuple;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SimpleSparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.utils.PredicateUtils;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.utils.UserDataContainer;

public class PrefuseBetaJungConverter {

	private static Graph jungGraph;
	
	public static Graph getJungGraph(prefuse.data.Graph prefuseGraph) {
		Map prefuseToJungVertices = new HashMap();
        boolean directed = prefuseGraph.isDirected();
		if(directed) {
        	jungGraph = new DirectedSparseGraph();
        } else {
        	jungGraph = new UndirectedSparseGraph();
        }
        
        for (Iterator iter = prefuseGraph.nodes(); iter.hasNext();) {
            Node node = (Node) iter.next();
            Vertex vertex = new SimpleSparseVertex(); //can do directed and undirected; specialize later?
            merge(node, vertex);
            
            jungGraph.addVertex(vertex);
            prefuseToJungVertices.put( node, vertex);
        }
        
        for (Iterator iter = prefuseGraph.edges(); iter.hasNext();) {
            prefuse.data.Edge e = (prefuse.data.Edge) iter.next();
            Vertex first = (Vertex) prefuseToJungVertices.get(e.getSourceNode());
            Vertex second = (Vertex) prefuseToJungVertices.get(e.getTargetNode());
			Edge edge;
			if(directed) {
				edge = new DirectedSparseEdge(first, second);
			} else {
				edge = new UndirectedSparseEdge(first, second);
			}
            merge(e, edge);
            
            jungGraph.addEdge(edge);
            
        }


        return jungGraph;
	}
	
	private static void merge(Tuple prefuseTuple, UserDataContainer jungContainer) {
		
		Schema schema = prefuseTuple.getSchema();
		int columns = schema.getColumnCount();
		
		for(int ii = 0; ii < columns; ii++) {
			jungContainer.addUserDatum(schema.getColumnName(ii), prefuseTuple.get(ii), UserData.SHARED);
		}
		
	}

}
