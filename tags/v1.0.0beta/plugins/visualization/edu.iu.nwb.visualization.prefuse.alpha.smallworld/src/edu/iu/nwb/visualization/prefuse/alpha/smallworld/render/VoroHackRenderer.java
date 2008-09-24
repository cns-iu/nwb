/*
 * Created on Nov 16, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.VoroNode;


/**
 * Fudges Voronode drawing using multipass rendering techniques.
 * Nodes are drawn as a series of concentric circles where
 * each circle of each node is drawn in a single rendering pass. 
 *
 * @author Stephen
 */
public class VoroHackRenderer implements MultipassRenderer {
    
    private Color m_start_color = new Color(50,50,50);
    private Color m_end_color = Color.WHITE;
    private Color[] m_colors = null;
    private float[] m_delta_components = new float[4];
    private int m_levels = 0;
    private Stroke m_node_stroke = null; 
	private static Ellipse2D m_circle =
		new Ellipse2D.Double(0, 0, 1, 1 );

    public void setLevels( int levels ) {
        m_levels = levels;
    }
    
    public VoroHackRenderer( ) {
        this(5);
    }

    public VoroHackRenderer( int levels ) {
        m_levels = levels;
        float[] start_components = new float[4];
        float[] end_components = new float[4];
        m_delta_components = new float[4];
        m_start_color.getColorComponents( start_components );
        m_end_color.getColorComponents( end_components );
        m_delta_components[0] = (end_components[0]-start_components[0])/(float)levels;
        m_delta_components[1] = (end_components[1]-start_components[1])/(float)levels;
        m_delta_components[2] = (end_components[2]-start_components[2])/(float)levels;
        m_delta_components[3] = (end_components[3]-start_components[3])/(float)levels;
        m_colors = new Color[ levels ];
        for( int i = 0; i < levels; i++ ) {
            m_colors[ i ] = new Color(  i*m_delta_components[0]+start_components[0], 
                                        i*m_delta_components[1]+start_components[1],
                                        i*m_delta_components[2]+start_components[2],
                                        1.0f);

        }
    }
    
    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefuse.render.Renderer#render(java.awt.Graphics2D, edu.berkeley.guir.prefuse.VisualItem)
     */
    public void render( Graphics2D g, VisualItem item ) {
        
    }

    public void render( Graphics2D g, VisualItem item, int pass ) {
        
        VoroNode node = (VoroNode) item;
        Stroke s = g.getStroke();
        if ( m_node_stroke != null ) g.setStroke(m_node_stroke);
        float size = (float)node.size( ) * ((float)( m_levels - pass ))/((float)m_levels);                
        m_circle.setFrame( node.pos( ).coord( 0 )-size/2, node.pos( ).coord( 1 )-size/2, size, size );                
        g.setPaint( m_colors[pass] );                
        g.fill(m_circle);        
        g.setStroke(s);
        
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
