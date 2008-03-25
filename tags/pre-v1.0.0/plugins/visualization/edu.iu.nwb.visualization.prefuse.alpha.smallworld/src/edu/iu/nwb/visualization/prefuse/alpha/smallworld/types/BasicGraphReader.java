/*
 * Created on Dec 16, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.graph.io.GraphReader;


/**
 * Reads graphs of the format where each line =
 * "node1 node2"
 * indicates an edge between node1 and node2.
 * No nodes of degree zero allowed.
 *
 * @author Stephen
 */
public class BasicGraphReader implements GraphReader {

    public Graph loadGraph( String filename ) throws FileNotFoundException,
            IOException {
        
        return loadGraph( new File(filename) );
    }

    public Graph loadGraph( URL url ) throws IOException {

        // TODO Auto-generated method stub
        return null;
    }
    
    public Graph convertGraph(Graph graph) {
		Graph clusterGraph = new DefaultGraph();
		Map<String,Node> node_map = new HashMap<String,Node>();
		
		Node node1 = null;
        Node node2 = null;
        int nodeCounter = 0;
        Iterator iter = graph.getEdges();
        while(iter.hasNext()) {
        	Edge edge = (Edge) iter.next();
        	Node first = edge.getFirstNode();
			String firstLabel = first.getAttribute("label");
			if( ( node1 = node_map.get( firstLabel ) ) == null ) {
        		node1 = new DefaultCluster( 0,0 );
        		copyAttributes(first, node1);
        		node1.setAttribute( "id", ""+nodeCounter );
        		nodeCounter++;
        		clusterGraph.addNode( node1 );
        		node_map.put( firstLabel, node1 );
        	}
        	Node second = edge.getSecondNode();
			String secondLabel = second.getAttribute("label");
			if( ( node2 = node_map.get( secondLabel ) ) == null ) {
        		node2 = new DefaultCluster( 0,0 );
        		copyAttributes(second, node2);
        		node2.setAttribute( "id", ""+nodeCounter );
        		nodeCounter++;
        		clusterGraph.addNode( node2 );
        		node_map.put( secondLabel, node2 );
        	}
        
        	Edge clusterEdge = new DefaultEdge( node1, node2 );
        	clusterGraph.addEdge( clusterEdge );
        }
		return clusterGraph;
    }

    private void copyAttributes(Node first, Node node1) {
		node1.setAttributes(first.getAttributes());
		
	}

	public Graph loadGraph( File f ) throws FileNotFoundException, IOException {

        Graph graph = new DefaultGraph( );
        BufferedReader br = new BufferedReader( new FileReader( f ) );
        
        String line = "";
        
        int node_counter = 0; //used for creating numerical ids
        int foo = 0;
        Map<String,Node> node_map = new HashMap<String,Node>();
        while( (line = br.readLine() ) != null ) {
            
            // split line into id tokens
            String[] items = line.split("\\s+");
            
            
            if( items.length >= 2 ) {
                foo++;
//                System.out.println("item0 = \"" + items[0] + "\"");
//                System.out.println("item1 = \"" + items[1] + "\"");
                
                Node node1 = null;
                Node node2 = null;
                
                // create cluster nodes if not already in graph
                
                if( ( node1 = node_map.get( items[0] ) ) == null ) {
                    node1 = new DefaultCluster( 0,0 );
//                    System.out.println( "Creating Node label=" + items[0] + " id = " + node_counter );
                    node1.setAttribute( "label", items[0] );
                    node1.setAttribute( "id", ""+node_counter );
                    node_counter++;
                    graph.addNode( node1 );
                    node_map.put( items[ 0 ], node1 );
                }
                if( ( node2 = node_map.get( items[1] ) ) == null ) {
                    node2 = new DefaultCluster( 0,0 );
//                    System.out.println( "Creating Node label=" + items[1] + " id = " + node_counter );
                    node2.setAttribute( "label", items[1] );
                    node2.setAttribute( "id", ""+node_counter );
                    node_counter++;
                    graph.addNode( node2 );
                    node_map.put( items[ 1 ], node2 );
                }
                Edge edge = new DefaultEdge( node1, node2 );
                graph.addEdge( edge );
                
            }            

        }
        System.out.println("edges = "+foo);
        return graph;
    }

    public Graph loadGraph( InputStream is ) throws IOException {

        Graph graph = new DefaultGraph( );
        BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
        
        String line = "";
        
        int node_counter = 0; //used for creating numerical ids
        int foo = 0;
        Map<String,Node> node_map = new HashMap<String,Node>();
        while( (line = br.readLine() ) != null ) {
            
            // split line into id tokens
            String[] items = line.split("\\s+");
            
            
            if( items.length >= 2 ) {
                foo++;
//                System.out.println("item0 = \"" + items[0] + "\"");
//                System.out.println("item1 = \"" + items[1] + "\"");
                
                Node node1 = null;
                Node node2 = null;
                
                // create cluster nodes if not already in graph
                
                if( ( node1 = node_map.get( items[0] ) ) == null ) {
                    node1 = new DefaultCluster( 0,0 );
//                    System.out.println( "Creating Node label=" + items[0] + " id = " + node_counter );
                    node1.setAttribute( "label", items[0] );
                    node1.setAttribute( "id", ""+node_counter );
                    node_counter++;
                    graph.addNode( node1 );
                    node_map.put( items[ 0 ], node1 );
                }
                if( ( node2 = node_map.get( items[1] ) ) == null ) {
                    node2 = new DefaultCluster( 0,0 );
//                    System.out.println( "Creating Node label=" + items[1] + " id = " + node_counter );
                    node2.setAttribute( "label", items[1] );
                    node2.setAttribute( "id", ""+node_counter );
                    node_counter++;
                    graph.addNode( node2 );
                    node_map.put( items[ 1 ], node2 );
                }
                Edge edge = new DefaultEdge( node1, node2 );
                graph.addEdge( edge );
                
            }            

        }
        System.out.println("edges = "+foo);
        return graph;
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {

        if( args.length > 0 ) {
            BasicGraphReader bgr = new BasicGraphReader( );
            try {
                Graph graph = bgr.loadGraph( args[ 0 ] );
                System.out.println("Graph Successfully Loaded with " + graph.getNodeCount() + " nodes and " + graph.getEdgeCount() + " edges.");
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

}
