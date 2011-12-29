/*
 * Created on Dec 3, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.action;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.BasicGraphReader;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.Cluster;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.ProgressUpdate;


/**
 * Basically a prefuse wrapper of Andreas Noack's 
 * linlog layout code.
 *
 * @author Stephen
 */
public class LinLogAction extends AbstractAction {

    protected boolean m_dirty = true;
    protected ProgressUpdate m_progress = null;
    
    public LinLogAction( ProgressUpdate progress ) {
        m_progress = progress;
    }
    public LinLogAction( ) {
        this(null);
    }
    
    /**
     * Only set this to true if the 
     * structure of the graph has somehow changed
     * @param dirty
     */
    public void setDirty( boolean dirty ) {
        m_dirty = dirty;
    }
    
    public boolean getDirty( ) {
        return m_dirty;
    }
    
    public void doLayout( ItemRegistry registry ) {

        if( m_dirty ) {
            
            m_dirty = false;
            
            if( registry.getGraph( ) != null ) {
                
                // convert the prefuse graph to a map of maps for linlog 
                
                Graph graph = registry.getGraph( );
                
                Map<String,Map<String,Float>> temp_graph = new HashMap<String,Map<String,Float>>();

                Iterator edge_iter = graph.getEdges();
                while( edge_iter.hasNext() ) {
                    Edge edge = (Edge) edge_iter.next();
                    String source = edge.getFirstNode().getAttribute( "id" );
                    String target = edge.getSecondNode().getAttribute( "id" );
                    if (temp_graph.get(source) == null) temp_graph.put(source, new HashMap<String,Float>());
                    temp_graph.get(source).put(target, 1.0f);                
                }
                
                // make it symmetric (it likely probably already is)
                temp_graph = LinLogLayout.makeSymmetricGraph(temp_graph);
                
                Map<String,Integer> nodeToId = LinLogLayout.makeIds(temp_graph);
                float[][] positions = LinLogLayout.makeInitialPositions(temp_graph);
                
                // see class MinimizerBarnesHut for a description of the parameters
                
                MinimizerBarnesHut minimizer = new MinimizerBarnesHut(
                        LinLogLayout.makeAttrIndexes(temp_graph, nodeToId), LinLogLayout.makeAttrWeights(temp_graph, nodeToId), 
                        LinLogLayout.makeRepuWeights(temp_graph, nodeToId),
                    1.0f, 0.0f, 0.01f, positions, m_progress);
                                
                // run the minimizer for at least 100 iterations 
                
                minimizer.minimizeEnergy(100);  // TODO: make this a variable parameter
                //minimizer.minimizeEnergy(1);  // TODO: make this a variable parameter
                Map<String,float[]> nodeToPosition = LinLogLayout.convertPositions(positions, nodeToId);

                // update the graph and fit the nodes to the display
                
                Iterator node_iter = graph.getNodes();
                while( node_iter.hasNext() ) {
                    Cluster cluster = (Cluster) node_iter.next();
                    float[] pos = nodeToPosition.get( cluster.getAttribute( "id" ) );
                    cluster.setCenter( new Point2D.Double( pos[0], pos[1] ) );
                    cluster.setBounds( new Rectangle2D.Double( pos[0]-cluster.getRadius(), 
                            pos[1]-cluster.getRadius(), 2*cluster.getRadius(), 2*cluster.getRadius() ) );
                }
                                
            }
            
        }
        
    }
    
    @Override
    public void run( ItemRegistry registry, double frac ) {

        doLayout( registry );
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {


        if( args.length > 0 ) {
         
            BasicGraphReader bgr = new BasicGraphReader( );
            try {
                Graph graph = bgr.loadGraph( args[0] );

                ItemRegistry registry = new ItemRegistry( graph );
                
                LinLogAction lla = new LinLogAction( );
                
                System.out.print("Testing Layout...");
                long begin_time = System.currentTimeMillis();
                // test the linlog layout
                lla.doLayout( registry );
                System.out.println("done in " + ((System.currentTimeMillis()-begin_time)) + "seconds" );
                
//                Iterator iter = registry.getGraph().getNodes();
//                while( iter.hasNext() ) {
//                    Cluster cluster = (Cluster) iter.next();
//                    System.out.println("Cluster " + cluster.getAttribute("id") + " at "
//                            + cluster.getCenter() );
//                }
//                
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
                        
        }        
        
        
    }

}
