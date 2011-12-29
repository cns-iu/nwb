/*
 * Created on Jan 22, 2006
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.render;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.layout.DOALayout;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.Cluster;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.VoroNode;


/**
 * Draws the Degree of Abstraction Lens based on 
 * parameters in DOALayout
 *
 * @author Stephen
 */
public class LensDisplay extends MultipassDisplay {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Ellipse2D m_lens = null;

    public LensDisplay( ItemRegistry registry, int passes ) {
        super(registry, passes);
        // setup default strokes
        m_lens = new Ellipse2D.Double( 0,0, 
                (DOALayout.DOA_RADIUS+DOALayout.ZERO_RADIUS)*2, 
                (DOALayout.DOA_RADIUS+DOALayout.ZERO_RADIUS)*2);
    }
    
    /**
     * @param args
     */
    public static void main( String[] args ) {

    }

    /**
     * Paint routine called <i>after</i> items are drawn. Subclasses should
     * override this method to perform custom drawing.
     * @param g the Graphics context to draw into
     */
    protected void postPaint(Graphics2D g) {

        Point p = m_registry.getDisplay(0).getMousePosition();
        g.setPaint( Color.BLACK );
        if( p != null ) {            
            m_lens.setFrameFromCenter( p.getX(), p.getY(), p.getX()-(DOALayout.DOA_RADIUS+DOALayout.ZERO_RADIUS), 
                    p.getY()-(DOALayout.DOA_RADIUS+DOALayout.ZERO_RADIUS) );
            g.draw( m_lens );
            m_lens.setFrameFromCenter( p.getX(), p.getY(), p.getX()-DOALayout.ZERO_RADIUS, 
                    p.getY()-DOALayout.ZERO_RADIUS );
            g.draw( m_lens );

            synchronized ( m_registry ) {
                Iterator iter = m_registry.getNodeItems();
                while( iter.hasNext() ) {
                    VoroNode node = (VoroNode) iter.next();
                    Cluster cluster = (Cluster) m_registry.getEntity((VisualItem)node);
                    if( p.distance( node.pos().coord(0), node.pos().coord(1) ) < cluster.getRadius( )/2 
                            && cluster.getAttribute("label") != null ) {
                        g.setFont(g.getFont().deriveFont((float)FONT_SIZE));
                        g.setPaint(font_color);
                        int positionX = (int)cluster.getCenter().getX();
                        int positionY = (int)cluster.getCenter().getY();                
                        g.drawString(cluster.getAttribute("label"), 
                            Math.min(positionX, getWidth() - g.getFontMetrics().stringWidth(cluster.getAttribute("label"))),  
                            Math.max(positionY, FONT_SIZE));
                        break;
                    }
                }
            }
        }
        
        
    } //
}
