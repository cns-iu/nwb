/*
 * Created on Dec 17, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.Display;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.Renderer;


/**
 * Subclass of Display which permits layering of renderings.
 * Useful for groups of VisualItems needing to be drawn in 
 * stages where each stage of rendering of all the items in
 * in the group must be completely finished before the next.
 *
 * @author Stephen
 */
public class MultipassDisplay extends Display {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int FONT_SIZE = 20;
    protected Paint font_color = new Color(128,32,32);
    protected int m_passes = 5;
    
    public MultipassDisplay( ) {
        super();
    }
    
    public MultipassDisplay( ItemRegistry registry, int passes ) {
        super(registry);
        m_passes = passes;
    }

    public void setPasses( int passes ) {
        m_passes = passes;
    }
    
    public void paintDisplay(Graphics2D g2D, Dimension d) {
        
        MultipassRenderer mrenderer = null;
        
        // paint background
        g2D.setColor(getBackground());
        g2D.fillRect(0, 0, d.width, d.height);
        
        // show debugging info?
        if ( m_showDebug ) {
            g2D.setFont(getFont());
            g2D.setColor(getForeground());
            g2D.drawString(getDebugString(), 5, 15);     
        }

        prepareGraphics(g2D);
        prePaint(g2D);
        
        g2D.setColor(Color.BLACK);
        synchronized (m_registry) {
            m_clip.setClip(0,0,d.width,d.height);
            m_clip.transform(m_itransform);
            for( int pass = 0; pass < m_passes; pass++ ) {
                Iterator items = m_registry.getItems();
                while (items.hasNext()) {
                    try {
                        VisualItem vi = (VisualItem) items.next();
                        Renderer renderer = vi.getRenderer();
                        if( renderer instanceof MultipassRenderer ) {
                            mrenderer = (MultipassRenderer) renderer;
                            Rectangle2D b = renderer.getBoundsRef(vi);
                            
                            if ( m_clip.intersects(b) )
                                mrenderer.render(g2D, vi, pass);
                        }
                        else {
                            if( pass == 0 ) {
                                Rectangle2D b = renderer.getBoundsRef(vi);
                                
                                if ( m_clip.intersects(b) )
                                    renderer.render(g2D, vi);
                            }
                        }
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        }

        postPaint(g2D);
    } //


}
