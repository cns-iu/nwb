/*
 * Created on Dec 20, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.types;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
 * Reads really basic stuff from a constrained set of
 * GML.
 *
 * @author Stephen
 */
public class GMLGraphReader implements GraphReader {

    public Graph loadGraph( String filename ) throws FileNotFoundException,
            IOException {

        return loadGraph( new File(filename) );
    }

    public Graph loadGraph( URL url ) throws IOException {

        return loadGraph( url.openConnection().getInputStream() );
    }

    public Graph loadGraph( File f ) throws FileNotFoundException, IOException {

        return loadGraph( new FileInputStream( f ) );        

    }

    public Graph loadGraph( InputStream is ) throws IOException {

        Graph graph = new DefaultGraph( );
        BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
        
        String line = "";
        
//        int prev_id = -1;
        boolean in_node = false;
        boolean in_edge = false;
        Cluster cur_node = null;
        String temp = "";
        int source = 0;
        int target = 0;
        
        Map<Integer,Node> node_map = new HashMap<Integer,Node>();
        while( (line = br.readLine() ) != null ) {
            
            // split line into id tokens
            String[] items = line.split("\\s+");
            
            if( items.length > 1 ) {
                if( items[0].equalsIgnoreCase( "node" ) ) {
                    cur_node = new DefaultCluster( 0, 0 );
                    graph.addNode( cur_node );
                    in_node = true;
                    in_edge = false;
                }
                else if( items[0].equalsIgnoreCase( "edge" ) ) {
                    in_node = false;
                    in_edge = true;
                }
                else if( items[0].equalsIgnoreCase( "id" ) ) {
                    if( in_node ) {
                        cur_node.setAttribute( "id", items[1]);
//                        if( Integer.parseInt(items[1])-prev_id > 1 ) {
//                            System.out.println("Error in GML at " + items[1]);
//                        }
//                        prev_id = Integer.parseInt(items[1]);
                        node_map.put( Integer.parseInt(items[1]), cur_node );
                    }
                }
                else if( items[0].equalsIgnoreCase( "label" ) ) {
                    if( in_node ) {
                        temp = "";
                        for( int i = 1; i < items.length; i++ ) {
                            temp += (items[i] + " ");
                        }
                        cur_node.setAttribute( "label", temp );
                    }
                }
                else if( items[0].equalsIgnoreCase( "source" ) ) {
                    if( in_edge ) {
                        source = Integer.parseInt( items[1] );
                    }
                }
                else if( items[0].equalsIgnoreCase( "target" ) ) {
                    if( in_edge ) {
                        target = Integer.parseInt( items[1] );
                        Edge edge = new DefaultEdge( node_map.get(source), node_map.get(target) );
                        graph.addEdge( edge );
                    }
                }
            }
            

        }
        
        // reset ids 
        
        int ids = 0;
        Iterator node_iter = graph.getNodes();
        while( node_iter.hasNext() ) {
            Node node = (Node)node_iter.next();
            node.setAttribute("id", ""+ids++ );
        }
        
        return graph;
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {

        if( args.length > 0 ) {
            GMLGraphReader bgr = new GMLGraphReader( );
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
