/*
 * Created on Nov 12, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.geom.Pnt;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.render.TubeRenderer;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.Cluster;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.VoroNode;


/**
 * Combination filter/layout fliters top clusters according to
 * a 2-D DOA function and determins their size and position
 *
 * Unfortunately there isn't a clear-cut distinction between
 * filtering and layout in the case of Degree of Abstraction
 * so I have made the grotesque decision to simply copy code
 * from Filter into Layout. 
 * 
 * @author Stephen
 */
public class DOALayout extends Layout {

    private double CONST_DOA = 0.25;
    public static double DOA_RADIUS = 50;
    public static double ZERO_RADIUS = 50;
    protected TubeRenderer m_tube = null;

    private Set m_classes;
    private boolean  m_gc;
    private boolean  m_edges = false;

    public void setTube( TubeRenderer tube ) {
        m_tube = tube; 
    }
    
    public void setEdges( boolean edges ) {
        m_edges = edges;
    }
    
    public void setConstantDOA( double DOA ) {
        CONST_DOA = DOA;
    }
    public double getConstantDOA( ) {
        return CONST_DOA;
    }
    
    /**
     * Creates a new filter associated with the given item class that
     * optionally performs garbage collection.
     * @param itemClass the item class associated with this filter
     * @param gc indicates whether garbage collection should be performed
     */
    public DOALayout(String itemClass, boolean gc) {
        m_classes = new HashSet(3);
        if ( itemClass != null )
            m_classes.add(itemClass);
        m_gc = gc;
    } //
        
    /**
     * Indicates whether or not this filter performs garbage collection.
     * @return true if garbage collection enabled, false otherwise
     */
    public boolean isGarbageCollectEnabled() {
        return m_gc;
    } //
    
    /**
     * Sets a flag determining if garbage collection is performed
     * @param s the flag indicating if garbage collected is performed
     */
    public void setGarbageCollect(boolean s) {
        m_gc = s;
    } //
    
    /**
     * Gets the item classes associated with this Filter. Item classes are
     * repreesnted as String instances corresponding to entries in an 
     * ItemRegistry.
     * @return the item classes associated with this Filter
     */
    public String[] getItemClasses() {
        return (String[])m_classes.toArray(new String[m_classes.size()]);
    } //
    
    /**
     * Associate an item class with this filter
     * @param itemClass the itemClass to add
     */
    public void addItemClass(String itemClass) {
        m_classes.add(itemClass);
    } //
    
    /**
     * Dissociate an item class with this filter
     * @param itemClass the itemClass to remove
     */
    public void removeItemClass(String itemClass) {
        m_classes.remove(itemClass);
    } //
    
    protected String m_itemClass = ItemRegistry.DEFAULT_NODE_CLASS; 
//    protected String m_itemClass = "VoroNode"; 
    protected Point2D m_mousePos = null;
    protected Cluster m_dendrogram_root = null;
    private ItemRegistry m_registry = null;


    public DOALayout( ) {
        this( ItemRegistry.DEFAULT_NODE_CLASS, true );
//        this( "VoroNode", true);
    }
    
    public DOALayout( String itemClass ) {
        this( itemClass, true);
        m_itemClass = itemClass;
    }
    
    /**
     * Computes the distance between a point and a rectangle
     * 
     * @param point
     * @param rect
     * @return
     */
    protected double distPointRect( Point2D point, Rectangle2D rect ) {
        
        if( rect.contains(point))
            return 0.;
        
        double dx = Math.abs(point.getX()-rect.getCenterX());
        double dy = Math.abs(point.getY()-rect.getCenterY());
        double sqrDist = 0.;
        double sqdx = dx - rect.getWidth()/2.;        
        double sqdy = dy - rect.getHeight()/2.;        
        if( sqdx > 0) {
            sqrDist += sqdx*sqdx;
        }
        if( sqdy > 0) {
            sqrDist += sqdy*sqdy;
        }        
        
        return Math.sqrt(sqrDist);
    }
    
    protected float doaFunc( Rectangle2D bounds ) {

        if( m_anchor != null ) {
	        double dist = distPointRect( m_anchor, bounds );
	        
	        if( dist < DOA_RADIUS + ZERO_RADIUS ) {

	            if( dist < ZERO_RADIUS )
	                return 0.0f;
	            
	            // linear DOA
	            return (float) ( ( (dist-ZERO_RADIUS) / DOA_RADIUS ) * CONST_DOA );
	        }        
        }        
    	        
        return (float)CONST_DOA;
    }
    
    protected float doaFunc( Point2D point ) {
       
        if( m_anchor != null ) {
        
	        double dist = point.distance( m_anchor );
	        
	        if( dist < DOA_RADIUS + ZERO_RADIUS ) {

	            if( dist < ZERO_RADIUS )
	                return 0.0f;
	            
	            // linear DOA
	            return (float) ( ( (dist-ZERO_RADIUS) / DOA_RADIUS ) * CONST_DOA );
	        }        
        }        
	        
        return (float)CONST_DOA;
    }
    
    protected void selectClusters( Cluster node ) {
        if( node.getDistance()>0 && node.getDistance( ) >= m_dendrogram_root.getDistance( )*doaFunc( node.getBounds( ) ) ) {
            Iterator iter = node.getChildren( );
            while( iter.hasNext() ) {
                selectClusters( (Cluster) iter.next( ) );
            }
        }
        else {

            // create the item
            
			VoroNode vnode = (VoroNode) m_registry.getItem( m_itemClass, node, true, true );            
			
            // calculate lambda

            float lambda = ( Math.max( doaFunc( node.getParent( ).getBounds( ) ) * 
                    m_dendrogram_root.getDistance( ), node.getDistance( ) )  - node.getDistance( ) )
                    / ( node.getParent( ).getDistance( ) - node.getDistance( ) );
//            System.out.println(( Math.max( doaFunc( node.getParent( ).getBounds( ) ) * 
//                    m_dendrogram_root.getDistance( ), node.getDistance( ) )  - node.getDistance( ) ) + " " + lambda + " " + node.getDistance());
                        
            // set position and size
                        
            double x = (float) node.getCenter().getX() + lambda*(node.getParent().getCenter().getX() - node.getCenter().getX());
            double y = (float) node.getCenter().getY() + lambda*(node.getParent().getCenter().getY() - node.getCenter().getY());
            int size = (int) (node.getRadius( ) + lambda*( node.getParent( ).getRadius( ) - node.getRadius( ) ) );
            if( m_anchor != null && m_anchor.distance( x, y ) < ZERO_RADIUS ) {
                x = fisheye(x,m_anchor.getX(),2,x-ZERO_RADIUS/1,x+ZERO_RADIUS/1);
                y = fisheye(y,m_anchor.getY(),2,y-ZERO_RADIUS/1,y+ZERO_RADIUS/1);
                if( m_anchor.distance( x, y ) > ZERO_RADIUS ) {
                    double dx = m_anchor.getX()-x;
                    double dy = m_anchor.getY()-y;
                    double norm = ZERO_RADIUS/Math.sqrt(dx*dx + dy*dy);
                    dx*=norm;
                    dy*=norm;
                    x=m_anchor.getX()-dx;
                    y=m_anchor.getY()-dy;
//                    x = (float) node.getCenter().getX() + lambda*(node.getParent().getCenter().getX() - node.getCenter().getX());
//                    y = (float) node.getCenter().getY() + lambda*(node.getParent().getCenter().getY() - node.getCenter().getY());
                }
                //                x = fisheye(x,m_anchor.getX(),20,x-size/2,x+size/2);
//                y = fisheye(y,m_anchor.getY(),20,y-size/2,y+size/2);
            }
            vnode.setPos( new Pnt( x, y, vnode ) );
            vnode.setSize( size );
            vnode.computeShape();
        }
    }

    /**
     * Shamelessly stolen from FisheyeDistortion
     * @return
     */
    protected double fisheye(double x, double a, double d, double min, double max) {
        if ( d != 0 ) {
            boolean left = x<a;
            double v, m = (left ? a-min : max-a);
            if ( m == 0 ) m = max-min;
            v = Math.abs(x - a) / m;
            v = (d+1)/(d+(1/v));
            return (left?-1:1)*m*v + a;
        } else {
            return x;
        }
    } //
    
    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
     */
    public void run( ItemRegistry registry, double arg1 ) {


        // clear the registry bindings
        
        m_registry = registry;        
        m_registry.clear( );
        Iterator iter_nodes = m_registry.getGraph().getNodes();
        while( iter_nodes.hasNext() ) {
            m_dendrogram_root = (Cluster) iter_nodes.next( );
            if( m_dendrogram_root.isRoot() )
                break;
        }
        
        // traverse the dendrogram to determing visible nodes
//        System.out.println("*****");
        if( m_dendrogram_root != null)
            selectClusters( m_dendrogram_root );
        
        // attach edges between visible nodes

        double minedgelength = Double.MAX_VALUE;
        double maxedgelength = Double.MIN_VALUE;
        double dx = 0.;
        double dy = 0.;
        double edgelength = 0.;
        if( m_edges ) {
            Iterator iter_edges = m_registry.getGraph().getEdges();
            while( iter_edges.hasNext() ) {
                Edge edge = (Edge) iter_edges.next();
                Cluster clust1 = (Cluster)edge.getFirstNode();
                Cluster clust2 = (Cluster)edge.getSecondNode();
                VoroNode node1 = null;
                while( node1 == null && clust1 != null ) {
                    node1 = (VoroNode) m_registry.getItem( m_itemClass, clust1, false, false );
                    clust1 = node1!=null?clust1:clust1.getParent();
                }
                VoroNode node2 = null;
                while( node2 == null & clust2 != null ) {
                    node2 = (VoroNode) m_registry.getItem( m_itemClass, clust2, false, false );
                    clust2 = node2!=null?clust2:clust2.getParent();
                }
                if( node1 != null && node2 != null && node2 != node1 ) {
                    m_registry.getEdgeItem( new DefaultEdge( clust1, clust2 ), true );
                    if( m_tube != null ) {
                        dx = node1.pos().coord(0)-node2.pos().coord(0);
                        dy = node1.pos().coord(1)-node2.pos().coord(1);
                        edgelength = Math.sqrt(dx*dx+dy*dy);
                        minedgelength = Math.min( minedgelength, edgelength );
                        maxedgelength = Math.max( maxedgelength, edgelength );
                    }
                }
            }            
            if( m_tube != null ) {
                m_tube.setMaxDistance( (float) maxedgelength );
                m_tube.setMinDistance( (float) minedgelength );
            }
        }        
        // optional garbage collection
        if ( m_gc ) {
            Iterator iter = m_classes.iterator();
            while ( iter.hasNext() ) {
                String iclass = (String)iter.next();
                registry.garbageCollect(iclass);
            }
        }
        

    }

    public static void main( String[] args ) {

    }
}
