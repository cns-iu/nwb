/*
 * Created on Nov 12, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.render;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.VoroNode;


/**
 * VoroNetRenderer renders VoroNodes 
 * 
 * This is currently very buggy.  There is some confusion
 * about how to correctly interpret the results of the 
 * delaunay triangulation to properly render the voronodes.
 *
 * @author Stephen
 */
public class VoroNetRenderer implements Renderer {

    public ItemRegistry m_registry = null;
    private Stroke m_node_stroke = null; 
    private Stroke m_outline_stroke = null; 
    //private Ellipse2D m_lens = null;
    
    public VoroNetRenderer( ) {
        
        // setup default strokes
        //m_lens = new Ellipse2D.Double( 0,0, 
        //        (DOALayout.DOA_RADIUS+DOALayout.ZERO_RADIUS)*2, 
        //        (DOALayout.DOA_RADIUS+DOALayout.ZERO_RADIUS)*2);

        
    }
    
    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefuse.render.Renderer#render(java.awt.Graphics2D, edu.berkeley.guir.prefuse.VisualItem)
     */
    public void render( Graphics2D g, VisualItem item ) {

        VoroNode node = (VoroNode) item;
        Shape shape = node.getShape( );
        
	    // set up colors
        Paint itemColor = item.getColor();
        Paint fillColor = item.getFillColor();
        
        // render the shape
        Stroke s = g.getStroke();
        if ( m_node_stroke != null ) g.setStroke(m_node_stroke);
		g.setPaint(fillColor);
		g.fill(shape);
		g.setPaint(itemColor);
		g.draw(shape);
        
        // if this node contains the outline, then draw it
		if( node.getOutline( ) != null ) {		    
	        if ( m_outline_stroke != null ) g.setStroke(m_node_stroke);
			g.setPaint(itemColor);
			g.draw( node.getOutline( ) );
		}
		
		g.setStroke(s);
        
//        Cluster clust = (Cluster) m_registry.getEntity( item );
////        System.out.print("Cluster " + clust.getCenter());
////        System.out.println(" bounds = " + clust.getBounds());
//        g.setPaint( Color.BLACK );
//        while( clust != null ) { 
//            g.draw( clust.getBounds() );
//            clust = clust.getParent();
//        }

        
    }

    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefuse.render.Renderer#locatePoint(java.awt.geom.Point2D, edu.berkeley.guir.prefuse.VisualItem)
     */
    public boolean locatePoint( Point2D p, VisualItem item ) {

        VoroNode node = (VoroNode) item;
        return node.getShape( ).contains( p );

    }

    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefuse.render.Renderer#getBoundsRef(edu.berkeley.guir.prefuse.VisualItem)
     */
    public Rectangle2D getBoundsRef( VisualItem item ) {
        VoroNode node = (VoroNode) item;
		return node.getShape( ).getBounds2D( );
    }

    public static void main( String[] args ) {

    }
}
