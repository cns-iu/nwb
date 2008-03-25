/*
 * Created on Nov 13, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.types;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.graph.Node;


/**
 * Basic data type for the small world graph 
 * visualization.  Useful for visualizing dendrograms.
 *
 * @author Stephen
 */
public interface Cluster extends Node {

    public Point2D getCenter( );
    public void setCenter( Point2D center );
    public void setBounds( Rectangle2D bounds );
    public float getRadius( );
    public float getDistance( );
    public Iterator getChildren( );
    public int getChildrenCount( );
    public int getHeight( );
    public Cluster getParent( );
    public void setParent( Cluster parent );
    public Rectangle2D getBounds( );
    public boolean hasChildren( );
    public boolean isRoot( );
    public void setRoot( boolean root );
    
}
