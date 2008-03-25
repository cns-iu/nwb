/*
 * Created on Dec 18, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.action;

import java.util.ArrayList;
import java.util.Iterator;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.action.AbstractAction;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.Cluster;


/**
 * Flattens a dendrogram by slaughtering the parents
 * of all leaf nodes.
 *
 * @author Stephen
 */
public class KillYourParents extends AbstractAction {

    @Override
    public void run( ItemRegistry registry, double frac ) {

        synchronized ( registry ) {
            Graph graph = registry.getGraph();

            // determine graph layout bounds
            
            Iterator node_iter = graph.getNodes();
            ArrayList<Cluster> al = new ArrayList<Cluster>();
            while( node_iter.hasNext() ) {
                Cluster cluster = (Cluster) node_iter.next();
                if( cluster.hasChildren() ) {
                    al.add( cluster );
                }
            }

            for( Cluster cluster : al ) {
                registry.getGraph().removeNode(cluster);
            }
        }

    }
}
