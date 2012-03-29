/*
 * Created on Nov 13, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.types;

import edu.iu.nwb.visualization.prefuse.alpha.smallworld.geom.Pnt;

import java.awt.Shape;
import java.util.Iterator;

/**
 * Interface for nodes used by the VoroNetLayout algorithm
 *
 * This class has lots of legacy crud from trying to
 * get explicitly computed voronoi diagrams to work
 * with nodes.  Most of it is currently ignored.
 *
 * @author Stephen
 */
public interface VoroNode {

    public int size( );
    public void setSize( int size );
    public Pnt pos( );
    public void setPos( Pnt newpos );
    public void addEdge( Pnt a, Pnt b );
    public void addNeighbor( VoroNode node );
    public Shape getShape( );
    public Shape getOutline( );
    public Shape getCell( );
    public void computeShape( );
    public void computeCell( );
    public Iterator getNeighbors( );
    
}
