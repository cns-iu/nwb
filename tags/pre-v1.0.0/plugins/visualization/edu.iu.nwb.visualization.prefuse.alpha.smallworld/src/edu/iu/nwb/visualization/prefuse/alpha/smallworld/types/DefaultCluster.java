/*
 * Created on Nov 13, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.types;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.berkeley.guir.prefuse.graph.DefaultNode;


/**
 * Instantiation of Cluster
 *
 * @author Stephen
 */
public class DefaultCluster extends DefaultNode implements Cluster {

    public static void printChildren( Cluster cluster ) {
        System.out.println( cluster.getBounds() );
        if( cluster.hasChildren() ) {
            Iterator children_iter = cluster.getChildren();
            while( children_iter.hasNext() ) {
                printChildren( (Cluster) children_iter.next() );
            }
        }
    }
    
    /**
     * Used for debugging purposes
     * @return
     */
    public static DefaultGraph generateTestSet( ) {

        DefaultGraph graph = new DefaultGraph( );
        Cluster c1 = new DefaultCluster( 200, 300, 50.f );        
        Cluster c2 = new DefaultCluster( 400, 300, 50.f );
//        graph.addNode( new DefaultCluster( 300, 300, c1.getRadius()+c2.getRadius(),c1,c2 ) );
        graph.addEdge( new DefaultEdge(c1, c2) );
        Cluster c3 = new DefaultCluster( 100, 200, 50.f );        
        Cluster c4 = new DefaultCluster( 300, 300, c1.getRadius()+c2.getRadius(),c1,c2 );
        graph.addNode( new DefaultCluster( 300, 300, c3.getRadius()+c4.getRadius(),c3,c4 ) );
        return graph;
    }
    
    
    private boolean m_root = false;
    private Rectangle2D m_bounds = null;
    private Point2D m_center = null;
    private float m_radius = 1.0f;
    private float m_distance = 0.0f;
    private ArrayList m_children = null;
    private Cluster m_parent = null;
    
    public boolean hasChildren( ) {
        return (m_children==null)?true:(m_children.size()>0);
    }
    private ArrayList getLeafNodes_helper( Cluster node, ArrayList leaves ) {
        Iterator temp = node.getChildren();
        while( temp.hasNext() ) {
            Cluster c = (Cluster) temp.next( );
            if( c.hasChildren( ) ) {
                leaves = getLeafNodes_helper( c, leaves );
            }
            else {
                leaves.add( c );
            }
        }
        return leaves;
    }
    public Iterator getLeafNodes( ) {
        
        ArrayList leaves = new ArrayList( );
        getLeafNodes_helper( this, leaves );
        return leaves.iterator();
    }
    
    public DefaultCluster( float x, float y, float radius, Cluster child_1, Cluster child_2, double distance ) {
        m_center = new Point2D.Float( x, y );
        m_children = new ArrayList( );
        m_radius = radius;
        m_children.add( child_1 );
        child_1.setParent( this );
        child_2.setParent( this );
        child_1.setRoot( false );
        child_2.setRoot( false );
        m_children.add( child_2 );
        m_distance = (float) distance;
        m_root = true;
        
        // compute cluster bounds
        
        double tempx = Math.min( x-radius, child_1.getBounds().getX() );
        double tempy = Math.min( y-radius, child_1.getBounds().getY() );
        tempx = Math.min( tempx, child_2.getBounds().getX() );
        tempy = Math.min( tempy, child_2.getBounds().getY() );
        double tempw = Math.max( (x+radius)-tempx, (child_1.getBounds().getX()+child_1.getBounds().getWidth())-tempx );
        double temph = Math.max( (y+radius)-tempy, (child_1.getBounds().getY()+child_1.getBounds().getHeight())-tempy );
        tempw = Math.max( tempw, (child_2.getBounds().getX()+child_2.getBounds().getWidth())-tempx );
        temph = Math.max( temph, (child_2.getBounds().getY()+child_2.getBounds().getHeight())-tempy );
        m_bounds = new Rectangle2D.Double( tempx, tempy, tempw, temph );
    }

    public DefaultCluster( float x, float y, float radius, Cluster child_1, Cluster child_2 ) {
        m_center = new Point2D.Float( x, y );
        m_children = new ArrayList( );
        m_radius = radius;
        if( child_1 != null ) {
            m_children.add( child_1 );
            child_1.setParent( this );
            if( child_2 != null ) {
                child_2.setParent( this );
                m_children.add( child_2 );
                m_distance = (float) child_1.getCenter().distance( child_2.getCenter() );

                // compute cluster bounds
                
                double tempx = Math.min( x-radius, child_1.getBounds().getX() );
                double tempy = Math.min( y-radius, child_1.getBounds().getY() );
                tempx = Math.min( tempx, child_2.getBounds().getX() );
                tempy = Math.min( tempy, child_2.getBounds().getY() );
                double tempw = Math.max( (x+radius)-tempx, (child_1.getBounds().getX()+child_1.getBounds().getWidth())-tempx );
                double temph = Math.max( (y+radius)-tempy, (child_1.getBounds().getY()+child_1.getBounds().getHeight())-tempy );
                tempw = Math.max( tempw, (child_2.getBounds().getX()+child_2.getBounds().getWidth())-tempx );
                temph = Math.max( temph, (child_2.getBounds().getY()+child_2.getBounds().getHeight())-tempy );
                m_bounds = new Rectangle2D.Double( tempx, tempy, tempw, temph );
            }
            else {
                
                // compute cluster bounds
                
                double tempx = Math.min( x-radius, child_1.getBounds().getX() );
                double tempy = Math.min( y-radius, child_1.getBounds().getY() );
                double tempw = Math.max( (x+radius)-tempx, (child_1.getBounds().getX()+child_1.getBounds().getWidth())-tempx );
                double temph = Math.max( (y+radius)-tempy, (child_1.getBounds().getY()+child_1.getBounds().getHeight())-tempy );
                m_bounds = new Rectangle2D.Double( tempx, tempy, tempw, temph );
            }
        } 
        else if( child_2 != null ) {
            child_2.setParent( this );
            m_children.add( child_2 );

            // compute cluster bounds
            
            double tempx = Math.min( x-radius, child_2.getBounds().getX() );
            double tempy = Math.min( y-radius, child_2.getBounds().getY() );
            double tempw = Math.max( (x+radius)-tempx, (child_2.getBounds().getX()+child_2.getBounds().getWidth())-tempx );
            double temph = Math.max( (y+radius)-tempy, (child_2.getBounds().getY()+child_2.getBounds().getHeight())-tempy );
            m_bounds = new Rectangle2D.Double( tempx, tempy, tempw, temph );
        }
        else {
            //m_children = null;
            m_bounds = new Rectangle2D.Double( x-radius, y-radius, 2.0*radius, 2.0*radius );
        }
    }
    
    public DefaultCluster( float x, float y ) {
        this( x, y, 10.0f, null, null );
    }
    public DefaultCluster( float x, float y, float radius ) {
        this( x, y, radius, null, null );
    }
    
    public Rectangle2D getBounds( ) {
        return m_bounds;
    }
    
    public void setBounds( Rectangle2D bounds ){
        m_bounds = bounds;
    }
    
    /* (non-Javadoc)
     * @see types.Cluster#getCenter()
     */
    public Point2D getCenter( ) {

        return m_center;
    }

    public void setRadius( float radius ) {
        m_radius = radius;
    }
    
    /* (non-Javadoc)
     * @see types.Cluster#getRadius()
     */
    public float getRadius( ) {

        return m_radius;
        
    }

    /* (non-Javadoc)
     * @see types.Cluster#getDistance()
     */
    public float getDistance( ) {

        return m_distance;
    }

    public void getDistance( float distance ) {        

        m_distance = distance;
    }

    public void addChild( Cluster child ) {
        m_children.add( child );
    }
    public void removeChild( Cluster child ) {
        m_children.remove( child );
    }    
    
    /* (non-Javadoc)
     * @see types.Cluster#getChildren()
     */
    public Iterator getChildren( ) {
        return (m_children==null)?null:m_children.iterator();
    }

    private int getChildrenCount_helper( Cluster child ) {
        int maxheight = 0;
        Iterator iter = child.getChildren();
        if( iter != null ) {
            if( !iter.hasNext( ) ) {
                return 1;
            }
            while( iter.hasNext() ) {
                //System.out.println("id="+getAttribute("id"));
                maxheight += getChildrenCount_helper( (Cluster) iter.next() );            
            }
            return maxheight;
        }
        else {
            return 1;
        }
    }
    
    public int getChildrenCount( ) {
        int maxheight = 0;
        Iterator iter = getChildren();
        if( iter != null ) {
            while( iter.hasNext() ) {
                //System.out.println("id="+getAttribute("id"));
                maxheight += getChildrenCount_helper( (Cluster) iter.next() );            
            }
            return maxheight;
        }
        else {
            return 0;
        }
    }
    
    private int getHeight_helper( Cluster child ) {

        int maxheight = 0;
        Iterator iter = child.getChildren();
        if( iter != null ) {
            while( iter.hasNext() ) {
                //System.out.println("id="+getAttribute("id"));
                maxheight = Math.max( getHeight_helper( (Cluster) iter.next() ), maxheight );            
            }
            return maxheight + 1;
        }
        else {
            return 0;
        }
    }
    
    /* (non-Javadoc)
     * @see types.Cluster#getChildren()
     */
    public int getHeight( ) {
        int maxheight = 0;
        Iterator iter = getChildren();
        if( iter != null ) {
            while( iter.hasNext() ) {
                maxheight = Math.max( getHeight_helper( (Cluster) iter.next() ), maxheight );            
            }
            return maxheight + 1;
        }
        else {
            return 0;
        }
    }

    /* (non-Javadoc)
     * @see types.Cluster#getParent()
     */
    public Cluster getParent( ) {

        return m_parent;
    }
    public void setParent( Cluster parent ) {
        m_parent = parent;
    }

    public void setCenter( Point2D center ) {
        m_center = center;
    }
    
    public boolean isRoot( ) {
        return m_root;
    }
    
    public void setRoot( boolean root ) {
        m_root = root;
    }
    
    public static void main( String[] args ) {

    }
}
