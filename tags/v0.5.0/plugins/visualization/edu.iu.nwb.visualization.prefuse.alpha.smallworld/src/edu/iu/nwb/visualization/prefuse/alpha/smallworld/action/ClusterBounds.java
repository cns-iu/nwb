/*
 * Created on Dec 17, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.action;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.Cluster;


/**
 * Fits a given cluster layout within a set of bounds.
 * Includes an optional border term you can specify in pixels.
 *
 * @author Stephen
 */
public class ClusterBounds extends AbstractAction {

    protected int m_border = 50;
    
    public void setBorder( int border ) {
        m_border = border;
    }
    
    @Override
    public void run( ItemRegistry registry, double frac ) {
        synchronized ( registry ) {
            Graph graph = registry.getGraph();

            // determine graph layout bounds
            
            Iterator node_iter = graph.getNodes();
            float xMin = Float.MAX_VALUE;
            float yMin = Float.MAX_VALUE;
            float xMax = Float.MIN_VALUE;
            float yMax = Float.MIN_VALUE;
            ArrayList<Cluster> al = new ArrayList<Cluster>();
            while( node_iter.hasNext() ) {
                Cluster cluster = (Cluster) node_iter.next();
                if( cluster.hasChildren() ) {
                    al.add( cluster );
//                    registry.getGraph().removeNode(cluster);
                }
                else {
                    float[] pos = new float[2];
                    cluster.setParent( null );
                    pos[0] = (float) cluster.getCenter( ).getX( );
                    pos[1] = (float) cluster.getCenter( ).getY( );
                    xMin = Math.min( xMin, pos[0]-1 );
                    yMin = Math.min( yMin, pos[1]-1 );
                    xMax = Math.max( xMax, pos[0]+1 );
                    yMax = Math.max( yMax, pos[1]+1 );
                }
            }

            for( Cluster cluster : al ) {
                registry.getGraph().removeNode(cluster);
            }
            
            // compare with the display bounds and determine the scaling coefficient
            
            float xSpan = xMax - xMin;
            float ySpan = yMax - yMin;
            float xTrueSpan = registry.getDisplay(0).getWidth()-2*m_border;
            float yTrueSpan = registry.getDisplay(0).getHeight()-2*m_border;
//            System.out.println("xTrueSpan = " + xTrueSpan);
//            System.out.println("yTrueSpan = " + yTrueSpan);
//            System.out.println("xSpan = " + xSpan);
//            System.out.println("ySpan = " + ySpan);
            float scale = 1.f;
            if( xSpan > xTrueSpan || ySpan > yTrueSpan ) {
//                scale = Math.max( (xSpan - xTrueSpan)/xSpan, (ySpan - yTrueSpan)/ySpan );
                scale = Math.min( xTrueSpan/xSpan, yTrueSpan/ySpan );
            }
            else if( xSpan < xTrueSpan && ySpan < yTrueSpan ) {
                scale = Math.min( xTrueSpan/xSpan, yTrueSpan/ySpan );            
            }
            
            // determine centering offsets
            float xCenter = (xTrueSpan-(xSpan*scale))/2.f;
            float yCenter = (yTrueSpan-(ySpan*scale))/2.f;
            
            // fit to the display bounds
            
            node_iter = graph.getNodes();
            while( node_iter.hasNext() ) {
                Cluster cluster = (Cluster) node_iter.next();
                cluster.getCenter().setLocation( scale*(cluster.getCenter().getX()-xMin)+xCenter+m_border,
                        scale*(cluster.getCenter().getY()-yMin)+yCenter+m_border);
                cluster.setBounds( new Rectangle2D.Double( cluster.getCenter().getX()-cluster.getRadius(), 
                        cluster.getCenter().getY()-cluster.getRadius(), 
                        2*cluster.getRadius(), 2*cluster.getRadius() ) );
            }
        }
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {

        // TODO Auto-generated method stub

    }

}
