package edu.iu.nwb.converter.jungprefuse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.utils.UserDataContainer;

public class PrefuseJungConverter {
	public static Graph getJungGraph(edu.berkeley.guir.prefuse.graph.Graph prefuseGraph) {		
		boolean directed = prefuseGraph.isDirected();
		
		Graph jungGraph;
		if(directed) {
			jungGraph = new DirectedSparseGraph();
		} else {
			jungGraph = new UndirectedSparseGraph();
		}
		
		HashMap nodeVertexMap = new HashMap();
		
		Iterator nodes = prefuseGraph.getNodes();
		while(nodes.hasNext()) {
			Node node = (Node) nodes.next();
			/* Can handle directed & undirected & parallel
			 * Switch to SimpleSparseVertex if we don't care about parallel
			 */
			Vertex vertex = new SparseVertex(); 			
			merge(node, vertex);
			
			jungGraph.addVertex(vertex);
			
			nodeVertexMap.put(node, vertex);
		}
		
		Iterator prefuseEdges = prefuseGraph.getEdges();
		while(prefuseEdges.hasNext()) {
			Edge prefuseEdge = (Edge) prefuseEdges.next();
			
			Vertex first = (Vertex) nodeVertexMap.get(prefuseEdge.getFirstNode());
			Vertex second = (Vertex) nodeVertexMap.get(prefuseEdge.getSecondNode());
			
			edu.uci.ics.jung.graph.Edge jungEdge;
			if(directed) {
				jungEdge = new DirectedSparseEdge(first, second);
			} else {
				jungEdge = new UndirectedSparseEdge(first, second);
			}
			
			merge(prefuseEdge, jungEdge);
			
			jungGraph.addEdge(jungEdge);
			
		}
		
		
		return jungGraph;
	}

	private static void merge(Entity prefuseEntity, UserDataContainer jungContainer) {		
		Map attributes = prefuseEntity.getAttributes();
		
		Iterator keys = attributes.keySet().iterator();
		while(keys.hasNext()) {
			Object key = keys.next();
			jungContainer.addUserDatum(key, attributes.get(key), UserData.SHARED);
		}		
	}	
}
