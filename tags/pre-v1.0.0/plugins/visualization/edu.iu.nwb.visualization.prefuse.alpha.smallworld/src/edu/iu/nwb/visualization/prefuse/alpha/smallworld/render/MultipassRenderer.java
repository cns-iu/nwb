/*
 * Created on Dec 17, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.render;

import java.awt.Graphics2D;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.Renderer;


/**
 * Used in conjunction with the MultipassDisplay
 *
 * @author Stephen
 */
public interface MultipassRenderer extends Renderer {

    public void render( Graphics2D g, VisualItem item, int pass );
    
}
