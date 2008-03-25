/*
 * Created on Nov 16, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.render;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.VoroNode;


/**
 * Renders edges as tubes.
 * 
 * Currently is too inefficient for practical use.
 * 
 * A curiosity.
 *
 * @author Stephen
 */
public class TubeRenderer implements Renderer {

    public static Color[] color1 = null; //Color.WHITE;
    public static Color[] color2 = null; //Color.BLACK;
    public static int width = 3;
    private int paint_bins = 10;
    protected boolean m_tubes = true;
    protected Line2D m_line = new Line2D.Float();
    private float maxdistance = 0.f; 
    private float mindistance = 0.f;
    private float distance = 0.f;

    public void setMaxDistance( float maxdist ) {
        maxdistance = maxdist;
    }
    public void setMinDistance( float mindist ) {
        mindistance = mindist;
    }
    
    public TubeRenderer( ) {
        
        super();
           
        color1 = new Color[ paint_bins ];
        color2 = new Color[ paint_bins ];
        for( int i = 0; i < paint_bins; i++ ) {
            color1[i] = new Color(255,255,255,(int)(255.f * (float)(paint_bins-i)/(float)paint_bins));
            color2[i] = new Color(0,0,0,(int)(128.f * (float)(paint_bins-i)/(float)paint_bins));
        }
        
    }
    
    public void setTubes( boolean tubes ) {
        m_tubes = tubes;
    }
    
    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefuse.render.Renderer#render(java.awt.Graphics2D, edu.berkeley.guir.prefuse.VisualItem)
     */
    public void render( Graphics2D g, VisualItem item ) {

        
        EdgeItem edge = (EdgeItem) item;
                
		VoroNode item1 = (VoroNode)edge.getFirstNode();
		VoroNode item2 = (VoroNode)edge.getSecondNode();

        if( m_tubes ) {
            
    		GeneralPath poly = new GeneralPath();
    		GradientPaint gpaint = null;
    		
    		double dx = item1.pos().coord(0) - item2.pos().coord(0);
    		double dy = item1.pos().coord(1) - item2.pos().coord(1);
    		double d = Math.sqrt( dx*dx + dy*dy );
    		if(d == 0.)
    		    return;
    		double temp = dx;
    		dx = dy/d;
    		dy = -temp/d;
    		dx *= width;
    		dy *= width;
    		gpaint = new GradientPaint(  (float)item1.pos().coord(0)-(float)dx,
    		        (float)item1.pos().coord(1)-(float)dy,
    		        color1[0],
    		        (float)item1.pos().coord(0)+(float)dx,
    		        (float)item1.pos().coord(1)+(float)dy,
    		        color2[0]);
    //		System.out.println("dx = " + dx + " dy = " + dy);
    		poly.moveTo( (float)item1.pos().coord(0)-(float)dx,
    		        (float)item1.pos().coord(1)-(float)dy);
    		poly.lineTo( (float)item1.pos().coord(0)+(float)dx,
    		        (float)item1.pos().coord(1)+(float)dy);
    		poly.lineTo( (float)item2.pos().coord(0)+(float)dx,
    		        (float)item2.pos().coord(1)+(float)dy);
    		poly.lineTo( (float)item2.pos().coord(0)-(float)dx,
    		        (float)item2.pos().coord(1)-(float)dy);
    		poly.lineTo( (float)item1.pos().coord(0)-(float)dx,
    		        (float)item1.pos().coord(1)-(float)dy);
    		
    		g.setPaint(gpaint);
    		//g.setPaint(Color.BLACK);
    		g.fill( poly );
    		g.draw( poly );
        }
        else {
            m_line.setLine( item1.pos().coord(0), item1.pos().coord(1), 
                    item2.pos().coord(0), item2.pos().coord(1) );
            //determine the opacity of the line
            distance = (float)Math.sqrt( Math.pow(item1.pos().coord(0)-item2.pos().coord(0), 2)
                    + Math.pow(item1.pos().coord(1)-item2.pos().coord(1), 2) );

            //System.out.println("distance = " + distance + " vs " + mindistance + " and " + maxdistance );
            g.setPaint(color2[Math.round((float)(paint_bins-1)*(float)Math.sqrt((distance-mindistance)/(maxdistance-mindistance)))]);
            //g.setPaint(color2[0]);
            g.draw(m_line);
        }
    }

    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefuse.render.Renderer#locatePoint(java.awt.geom.Point2D, edu.berkeley.guir.prefuse.VisualItem)
     */
    public boolean locatePoint( Point2D p, VisualItem item ) {

        // TODO Auto-generated method stub
        return true;
    }

    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefuse.render.Renderer#getBoundsRef(edu.berkeley.guir.prefuse.VisualItem)
     */
    public Rectangle2D getBoundsRef( VisualItem item ) {

//        EdgeItem edge = (EdgeItem) item;
//        
//		VoroNode item1 = (VoroNode)edge.getFirstNode();
//		VoroNode item2 = (VoroNode)edge.getSecondNode();
//		
//		double dx = item1.pos().coord(0) - item2.pos().coord(0);
//		double dy = item1.pos().coord(1) - item2.pos().coord(1);
//		double d = Math.sqrt( dx*dx + dy*dy );
//		if(d == 0.)
//		    return null;
//		dx /= d;
//		dy /= d;
//		dx *= width;
//		dy *= width;
//		poly.moveTo( (float)item1.pos().coord(0)-(float)dx,
//		        (float)item1.pos().coord(1)-(float)dy);
//		poly.lineTo( (float)item1.pos().coord(0)+(float)dx,
//		        (float)item1.pos().coord(1)+(float)dy);
//		poly.lineTo( (float)item2.pos().coord(0)+(float)dx,
//		        (float)item2.pos().coord(1)+(float)dy);
//		poly.lineTo( (float)item2.pos().coord(0)-(float)dx,
//		        (float)item2.pos().coord(1)-(float)dy);
//		poly.lineTo( (float)item1.pos().coord(0)-(float)dx,
//		        (float)item1.pos().coord(1)-(float)dy);
//		
//		g.setPaint(gpaint);
//		g.fill( poly );
        // TODO Auto-generated method stub
        Rectangle2D fart = new Rectangle2D.Double(10,10,20,20);
        return fart;
    }

    public static void main( String[] args ) {

    }
}
