/*
 * Created on Nov 13, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.types;

import edu.berkeley.guir.prefuse.NodeItem;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.geom.Pnt;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * This class has lots of legacy crud from trying to
 * get explicitly computed voronoi diagrams to work
 * with nodes.  Most of it is currently ignored.
 *
 * @author Stephen
 */
public class DefaultVoroNode extends NodeItem implements VoroNode {

    private GeneralPath m_cell_shape = null;
    private Area m_shape = null;
    private int m_radius = 0;
    private Pnt m_pnt = null;
    private ArrayList m_neighbors = null;
    private ArrayList m_edges = null;
	private static Ellipse2D m_circle =
	    new Ellipse2D.Double(0, 0, 1, 1 );
	private Point2D vorvec1 = null; 
	private Point2D vorvec2 = null; 
    
    /**
     * 
     */
    public DefaultVoroNode( ) {
        super( );
        setColor( Color.GRAY );
        setFillColor( Color.WHITE );
        m_neighbors = new ArrayList( );
        m_edges = new ArrayList( );
        m_cell_shape = new GeneralPath( );
//        m_circle.setFrame( pos( ).coord( 0 )-size()/2, pos( ).coord( 1 )-size()/2, size( ), size( ) );
//        m_shape = new Area(m_circle);
        m_shape = new Area( );
        vorvec1 = new Point2D.Double( );
        vorvec2 = new Point2D.Double( );
    }
    
    public void clear( ) {
        super.clear();
        m_pnt = null;
        m_neighbors.clear( );
        m_edges.clear( );
        m_shape.reset( );
        m_cell_shape.reset( );
        // handle clearing the shape
    }
    
    /* (non-Javadoc)
     * @see types.VoroNode#size()
     */
    public int size( ) {

        // we're dealing with areas
//        System.out.println(""+Math.sqrt( ( (DefaultCluster) getEntity() ).getRadius( ) ));
//        return (int)Math.sqrt( ( (DefaultCluster) getEntity() ).getRadius( ) );
        return m_radius;
    }

    public void setSize( int size ) {
        
        m_radius = size;
    }
    
    /* (non-Javadoc)
     * @see types.VoroNode#pos()
     */
    public Pnt pos( ) {

        return m_pnt;
    }

    /* (non-Javadoc)
     * @see types.VoroNode#addEdge(geom.Pnt, geom.Pnt)
     */
    public void addEdge( Pnt a, Pnt b ) {

        Edge newedge = new Edge( a, b );
//        System.out.println("edges = "+m_edges.size( ));
//        System.out.println("Adding (" + newedge.a.coord(0) + ","+ newedge.a.coord(1) + "-"+ newedge.b.coord(0) +","+ newedge.b.coord(1) + ")");
        if( m_edges.size( ) == 0 ) {
            m_edges.add( newedge );
            return;
        }
        for( int i = 0; i < m_edges.size( ); i++ ) {
            Edge oldedge = (Edge) m_edges.get( i );
//            System.out.println("comparing to (" + oldedge.a.coord(0) + ","+ oldedge.a.coord(1) + "-"+ oldedge.b.coord(0) +","+ oldedge.b.coord(1) + ")");
            if( ( ( oldedge.a.coord(0) == newedge.a.coord(0) ) && 
                    ( oldedge.a.coord(1) == newedge.a.coord(1) ) &&
                    ( oldedge.b.coord(0) == newedge.b.coord(0) ) && 
                    ( oldedge.b.coord(1) == newedge.b.coord(1) ) ) ||
            ( ( oldedge.a.coord(0) == newedge.b.coord(0) ) && 
                    ( oldedge.a.coord(1) == newedge.b.coord(1) ) &&
                    ( oldedge.b.coord(0) == newedge.a.coord(0) ) && 
                    ( oldedge.b.coord(1) == newedge.a.coord(1) ) ) ){
                return;
            }
            if( ( oldedge.a.coord(0) == newedge.a.coord(0) ) && 
            ( oldedge.a.coord(1) == newedge.a.coord(1) ) ) {
                m_edges.add( i, new Edge( b, a ) );            
                return;
            }
            else if( ( oldedge.b.coord(0) == newedge.b.coord(0) ) && 
                    ( oldedge.b.coord(1) == newedge.b.coord(1) ) ) {
                m_edges.add( i+1, new Edge( b, a ) );                            
                return;
            }
            else if( ( oldedge.b.coord(0) == newedge.a.coord(0) ) && 
                    ( oldedge.b.coord(1) == newedge.a.coord(1) ) ) {
                m_edges.add( i+1, newedge );
                return;
            }
            else if( ( oldedge.a.coord(0) == newedge.b.coord(0) ) && 
                    ( oldedge.a.coord(1) == newedge.b.coord(1) ) ) {
                m_edges.add( i, newedge );
                return;
            }
            else {
//                System.out.println("unconnected add");
                m_edges.add( newedge );
                return;
            }
        }
    }

    /* (non-Javadoc)
     * @see types.VoroNode#addNeighbor(types.VoroNode)
     */
    public void addNeighbor( VoroNode node ) {
        
        if( ! m_neighbors.contains( node ) ) 
            m_neighbors.add(node);

    }

    /* (non-Javadoc)
     * @see types.VoroNode#getShape()
     */
    public Shape getShape( ) {

        return m_shape;
    }

    /* (non-Javadoc)
     * @see types.VoroNode#getShape()
     */
    public Shape getCell( ) {

        return m_cell_shape;
    }

    /* (non-Javadoc)
     * @see types.VoroNode#getOutline()
     */
    public Shape getOutline( ) {

        return null;
    }

    /* (non-Javadoc)
     * @see types.VoroNode#computeShape()
     */
    public void computeShape( ) {
        m_circle.setFrame( pos( ).coord( 0 )-size()/2, pos( ).coord( 1 )-size()/2, size( ), size( ) );
        m_shape.add( new Area( m_circle ) );        
//        m_shape.add( new Area( getCell( ) ) );
//        for( int i = 0; i < m_neighbors.size( ); i++ ) {
//            VoroNode neighb = (VoroNode) m_neighbors.get( i );
//            m_shape.subtract( new Area( neighb.getCell( ) ) );
//        }        
    }

    /* (non-Javadoc)
     * @see types.VoroNode#computeShape()
     */
    public void computeCell( ) {

        //System.out.println( "edges = " + m_edges.size() );
        if( m_edges.size() > 2 ) {
	        m_cell_shape.moveTo( (float) ( (Edge) m_edges.get( 0 ) ).a.coord( 0 ), 
	                (float) ( (Edge) m_edges.get( 0 ) ).a.coord( 1 ) );
	        for( int i = 0; i < m_edges.size( ); i++ ) {
	            m_cell_shape.lineTo( (float) ( (Edge) m_edges.get( i ) ).b.coord( 0 ), 
	                    (float) ( (Edge) m_edges.get( i ) ).b.coord( 1 ) );            
	        }
        }
        else if ( m_edges.size( ) == 2 ) {
            vorvec1.setLocation( ( (Edge) m_edges.get( 0 ) ).a.coord( 0 ) - ( (Edge) m_edges.get( 0 ) ).b.coord( 0 ),  
                    ( (Edge) m_edges.get( 0 ) ).a.coord( 1 ) - ( (Edge) m_edges.get( 0 ) ).b.coord( 1 ) );
            vorvec2.setLocation( ( (Edge) m_edges.get( 1 ) ).b.coord( 0 ) - ( (Edge) m_edges.get( 1 ) ).a.coord( 0 ),  
                    ( (Edge) m_edges.get( 1 ) ).b.coord( 1 ) - ( (Edge) m_edges.get( 1 ) ).a.coord( 1 ) );
	        m_cell_shape.moveTo( (float)vorvec1.getX( )*1000.0f, (float)  vorvec1.getY( )*1000.0f ); 
            m_cell_shape.lineTo( (float) ( (Edge) m_edges.get( 0 ) ).b.coord( 0 ), 
                    (float) ( (Edge) m_edges.get( 0 ) ).b.coord( 1 ) );            
	        m_cell_shape.lineTo( (float)vorvec2.getX( )*1000.0f, (float)  vorvec2.getY( )*1000.0f ); 
	        m_cell_shape.lineTo( (float)vorvec1.getX( )*1000.0f, (float)  vorvec1.getY( )*1000.0f ); 
        }
        else if( m_edges.size( ) == 1 ) {
            vorvec2.setLocation( pos( ).coord(0)-( (Edge) m_edges.get( 0 ) ).a.coord( 0 ),  
                    pos( ).coord(1)-( (Edge) m_edges.get( 0 ) ).a.coord( 1 ) );
            vorvec1.setLocation( pos( ).coord(0)-( (Edge) m_edges.get( 0 ) ).b.coord( 0 ),  
                    pos( ).coord(1)-( (Edge) m_edges.get( 0 ) ).b.coord( 1 ) );
	        m_cell_shape.moveTo( (float)vorvec1.getX( )*1000.0f + (float)pos().coord(0), 
	                (float)vorvec1.getY( )*1000.0f + (float)pos().coord(1) ); 
            m_cell_shape.lineTo( (float) ( (Edge) m_edges.get( 0 ) ).a.coord( 0 ), 
                    (float) ( (Edge) m_edges.get( 0 ) ).a.coord( 1 ) );            
            m_cell_shape.lineTo( (float) ( (Edge) m_edges.get( 0 ) ).b.coord( 0 ), 
                    (float) ( (Edge) m_edges.get( 0 ) ).b.coord( 1 ) );            
	        m_cell_shape.lineTo( (float)vorvec2.getX( )*1000.0f + (float)pos().coord(0), 
	                (float)vorvec2.getY( )*1000.0f + (float)pos().coord(1) ); 
	        m_cell_shape.lineTo( (float)vorvec1.getX( )*1000.0f + (float)pos().coord(0), 
	                (float)vorvec1.getY( )*1000.0f + (float)pos().coord(1) ); 
        }
    }

    /* (non-Javadoc)
     * @see types.VoroNode#getNeighbors()
     */
    public Iterator getNeighbors( ) {

        return m_neighbors.iterator();
    }

    public void setPos( Pnt newpoint ) {
        m_pnt = newpoint;
    }

    public static void main( String[] args ) {

    }
    
    class Edge {
        public Pnt a;
        public Pnt b;
        Edge( Pnt a, Pnt b) {
            this.a = a;
            this.b = b;
        }
        
    }
}
