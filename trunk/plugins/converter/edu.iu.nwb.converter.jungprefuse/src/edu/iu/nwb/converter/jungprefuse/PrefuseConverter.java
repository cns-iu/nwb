/*
 * Created on May 9, 2004
 */
package edu.iu.nwb.converter.jungprefuse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.berkeley.guir.prefuse.graph.DefaultNode;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

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
public class PrefuseConverter {
    
    public static  class JungGraph extends DefaultGraph {

        public final Graph jungGraph;

        public JungGraph(Graph g, Collection collection, boolean b) {
            super(collection, b);
            this.jungGraph = g;            
        }

    }
    public static class JungNode extends DefaultNode {
        
        public JungNode(Vertex v) {
            this.jungVertex = v;
            
            Iterator iter = v.getUserDatumKeyIterator();
            while (iter.hasNext()) {
         	   Object key = iter.next();
         	   Object value = v.getUserDatum(key);
         	   
         	   key = key == null? null : key.toString();
         	   value = value == null? null : value.toString();
         	   
         	   setAttribute((String)key, (String)value);
            }
        }

        public final Vertex jungVertex;
    }

    public static class JungEdge extends DefaultEdge {

       public JungEdge(Edge jungEdge, Node n1, Node n2, boolean directed) {
           super(n1, n2, directed);
           this.jungEdge = jungEdge;
           
           Iterator iter = jungEdge.getUserDatumKeyIterator();
           while (iter.hasNext()) {
        	   Object key = iter.next();
        	   Object value = jungEdge.getUserDatum(key);
        	   
        	   key = key == null? null : key.toString();
        	   value = value == null? null : value.toString();
        	   
        	   setAttribute((String)key, (String)value);
           }
       }
       
       public final Edge jungEdge;
    }

    
    public static JungGraph getPrefuseGraph( Graph g ) {
        boolean isDirected = false;
        Map jungToPrefuseVertices = new HashMap();
        
        for (Iterator iter = g.getVertices().iterator(); iter.hasNext();) {
            Vertex v = (Vertex) iter.next();
            jungToPrefuseVertices.put( v, new JungNode(v));
        }
        
        for (Iterator iter = g.getEdges().iterator(); iter.hasNext();) {
            Edge e = (Edge) iter.next();
            JungNode jn1 = (JungNode) jungToPrefuseVertices.get(e.getEndpoints().getFirst());
            JungNode jn2 = (JungNode) jungToPrefuseVertices.get(e.getEndpoints().getSecond());    
            boolean directedEdge = Graph.DIRECTED_EDGE.evaluate(e);
            if (directedEdge) {
                isDirected = true;
            }
            
            JungEdge e_out = new JungEdge(e, jn1, jn2, directedEdge); 
            jn1.addEdge(e_out);
        }

        JungGraph rv = new JungGraph( g, jungToPrefuseVertices.values(), isDirected);

        return rv;
    }

}
