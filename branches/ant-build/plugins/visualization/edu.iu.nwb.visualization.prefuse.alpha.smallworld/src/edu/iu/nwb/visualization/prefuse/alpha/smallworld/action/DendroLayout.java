/*
 * Created on Dec 18, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.action;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.Cluster;


/**
 * Primitive cluster dendrogram layout.  Recursively subdivides
 * layout boundaries.
 *
 * @author Stephen
 */
public class DendroLayout extends AbstractAction {

    protected int m_border = 50;

    @Override
    public void run( ItemRegistry registry, double frac ) {

        Cluster dendrogram_root = null;
        
        Iterator iter_nodes = registry.getGraph().getNodes();
        while( iter_nodes.hasNext() ) {
            dendrogram_root = (Cluster) iter_nodes.next( );
            if( dendrogram_root.isRoot() ) {
                break;
            }
        }
        
        // traverse the dendrogram to determing visible nodes

        if( dendrogram_root != null) {
            Rectangle2D tangle = new Rectangle2D.Double(m_border,m_border,
                    registry.getDisplay(0).getBounds().getWidth()-m_border*2,
                    registry.getDisplay(0).getBounds().getHeight()-m_border*2);
            
            dendrogramLayout( tangle, dendrogram_root );
        }
        
        

    }

    protected Point2D dendrogramLayout( Rectangle2D tangle, Cluster cluster ) {
        
        Iterator children = cluster.getChildren();       
        int numkids = cluster.getChildrenCount();
        if( numkids > 0 ) {
            
            Point2D[] points = new Point2D[2];
            boolean horiz_split = false;
            if( tangle.getWidth() < tangle.getHeight() ) {
                horiz_split = true;
            }

            boolean first_kid = true;
            while( children.hasNext() ) {
                Cluster child = (Cluster) children.next( );
                int kid_kids = child.getChildrenCount();
                kid_kids = ( kid_kids == 0 )?(kid_kids+1):(kid_kids);
                double proportion = ((double)kid_kids/(double)numkids);
                if( first_kid ) {
                    first_kid = false;
                    if( !horiz_split ) {
                        points[0] = dendrogramLayout( new Rectangle2D.Double( 
                                tangle.getMinX(), tangle.getMinY(), 
                                tangle.getWidth()*proportion, 
                                tangle.getHeight()), child );
                    }
                    else {
                        points[0] = dendrogramLayout( new Rectangle2D.Double( 
                                tangle.getMinX(), tangle.getMinY(), 
                                tangle.getWidth(), 
                                tangle.getHeight()*proportion), child );
                    }
                }
                else {
                    if( !horiz_split ) {
                        points[1] = dendrogramLayout( new Rectangle2D.Double( 
                                tangle.getMinX()+tangle.getWidth()*(1.-proportion), 
                                tangle.getMinY(), 
                                tangle.getWidth()*(proportion), 
                                tangle.getHeight()), child );
                    }
                    else {
                        points[1] = dendrogramLayout( new Rectangle2D.Double( 
                                tangle.getMinX(), 
                                tangle.getMinY()+tangle.getHeight()*(1.-proportion), 
                                tangle.getWidth(), 
                                tangle.getHeight()*proportion), child );                        
                    }
                } 
            }
            cluster.setCenter( new Point2D.Double( (points[0].getX()+points[1].getX())/2.,
                    (points[0].getY()+points[1].getY())/2. ) );
            cluster.setBounds( tangle );
        }
        else {
            cluster.setCenter( new Point2D.Double( tangle.getMinX() + Math.random()*tangle.getWidth(), 
                    tangle.getMinY() + Math.random()*tangle.getHeight() ) );
            cluster.setBounds( tangle );
        }
        
        return cluster.getCenter();
    }
    
    /**
     * @param args
     */
    public static void main( String[] args ) {

        // TODO Auto-generated method stub

    }

}
