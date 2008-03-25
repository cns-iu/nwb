/*
 * Created on Dec 17, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.action;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;


/**
 * This class is dumb and shouldn't ever be used.
 * 
 * Unless you're STUPID.
 * 
 * @author Stephen
 */
public class DrawCursor extends AbstractAction {

    @Override
    public void run( ItemRegistry registry, double frac ) {

        Graphics2D g = (Graphics2D) registry.getDisplay(0).getGraphics();
        Point p = registry.getDisplay(0).getMousePosition();
        g.setPaint( Color.BLACK );
        if( p != null ) {
            System.out.println("INFINITE LOOP!");
            Shape lens = new Ellipse2D.Double( p.getX()-25, p.getY()-25, 50, 50);
            g.draw( lens );
        }
    }

    /**
     * @param args
     */
    public static void main( String[] args ) {

        // TODO Auto-generated method stub

    }

}
