/*
 * Created on Nov 13, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.layout;

import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.geom.DelaunayTriangulation;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.geom.Pnt;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.geom.Simplex;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.VoroNode;

/**
 * Constructs voronoi cell shapes and delaunay neighbors for 
 * each voronode in the registry
 * 
 * This code isn't used but could be if you feel like making life
 * hard.
 *
 * @author Stephen
 */
public class VoroNetLayout extends Layout {

    protected String m_itemClass = ItemRegistry.DEFAULT_NODE_CLASS; 
//    protected String m_itemClass = "VoroNode"; 
    private DelaunayTriangulation dt;     // The Delaunay triangulation
    public Simplex initialTriangle;      // The large initial triangle
    private int initialSize = 10000;      // Controls size of initial triangle
    
    
    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run( ItemRegistry registry, double frac ) {
        
        // create the initial simplex in which to insert
        // voronodes
        initialTriangle = new Simplex(new Pnt[] {
                new Pnt(-initialSize, -initialSize),
                new Pnt( initialSize, -initialSize),
                new Pnt(           0,  initialSize)});
            dt = new DelaunayTriangulation(initialTriangle);

        // insert the positions of the voronodes
//            Iterator iter = registry.getItems( m_itemClass, true );
            Iterator iter = registry.getNodeItems( true );
        while( iter.hasNext( ) ) {
            try {
                VoroNode node = (VoroNode) iter.next( );
                node.pos( ).owner = node;
                dt.delaunayPlace( node.pos( ) );
            } catch ( ClassCastException e ) { /* do nothing */ };
        }
        
        // construct the shape of each voronoi cell
//        System.out.println("---");
        // Loop through all the edges of the DT (each is done twice)
        for (Iterator it = dt.iterator(); it.hasNext();) {
            Simplex triangle = (Simplex) it.next();

            for (Iterator otherIt = dt.neighbors(triangle).iterator(); otherIt.hasNext();) {
                Simplex other = (Simplex) otherIt.next();

                VoroNode a = null;
                VoroNode b = null;

                // find the two vertices the simplices share 
                // (a total of 9 iterations max)
                Iterator yetAnotherIt = triangle.iterator();
                while( yetAnotherIt.hasNext( ) ) {
                    VoroNode test = ( ( Pnt ) yetAnotherIt.next() ).owner;
                    if( test == null )
                        continue;
                    Iterator yetAnotherItStill = other.iterator();
                    while( yetAnotherItStill.hasNext( ) ) {
                        if( test == ( ( Pnt ) yetAnotherItStill.next() ).owner ) {
                            if( a == null ) {
                                a = test;
                                break;
                            }
                            else {
                                b = test;
                            }
                        }
                    }
                    if( b != null )
                        break;
                }
                
                Pnt p = Pnt.circumcenter((Pnt[]) triangle.toArray(new Pnt[0]));
                Pnt q = Pnt.circumcenter((Pnt[]) other.toArray(new Pnt[0]));
                
                // add edge to both voronodes
                if( b != null ) {
                    a.addEdge( p, q );
                    b.addEdge( p, q );
                    a.addNeighbor( b );
                    b.addNeighbor( a );
                }                
            }
        }   
        
        // build cell shapes from nodes
        iter = registry.getItems( m_itemClass, true );
        while( iter.hasNext( ) ) {
            try {
                VoroNode node = (VoroNode) iter.next( );
                node.computeCell( );
            } catch ( ClassCastException e ) { /* do nothing */ };
        }

        // build shapes from nodes
        iter = registry.getItems( m_itemClass, true );
        while( iter.hasNext( ) ) {
            try {
                VoroNode node = (VoroNode) iter.next( );
                node.computeShape( );
            } catch ( ClassCastException e ) { /* do nothing */ };
        }
    }

    public static void main( String[] args ) {

    }
    
    public String getItemClass( ) {

        return m_itemClass;
    }
    
    public void setItemClass( String class1 ) {

        m_itemClass = class1;
    }
}
