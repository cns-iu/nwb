/*
 * Created on Dec 18, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.types;

import java.util.Comparator;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.graph.Edge;


/**
 * Modified ItemComparator for Clusters
 *
 * @author Stephen
 */
public class SmallWorldComparator implements Comparator {

    protected int score(VisualItem item) {
        int score = 0;
        if ( item instanceof AggregateItem ) {
            score += (1<<5);
        } else if ( item instanceof NodeItem ) {
            score += (1<<4);
        } else if ( item instanceof EdgeItem ) {
            score += (1<<3);
        }
        if ( item.isFocus() ) {
            score += (1<<2);
        }
        if ( item.isHighlighted() ) {
            score += (1<<1);
        }
        
        return score;
    } //
    
    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(Object o1, Object o2) {
        if ( !(o1 instanceof VisualItem && o2 instanceof VisualItem) ) {
            throw new IllegalArgumentException();
        }
        
        VisualItem item1 = (VisualItem)o1;
        VisualItem item2 = (VisualItem)o2;
        int score1 = score(item1);
        int score2 = score(item2);
        
        if ( item1 instanceof EdgeItem && item2 instanceof EdgeItem ) {
            Cluster e1c1 = (Cluster)((Edge)((EdgeItem)item1).getEntity()).getFirstNode();
            Cluster e1c2 = (Cluster)((Edge)((EdgeItem)item1).getEntity()).getSecondNode();
            Cluster e2c1 = (Cluster)((Edge)((EdgeItem)item2).getEntity()).getFirstNode();
            Cluster e2c2 = (Cluster)((Edge)((EdgeItem)item2).getEntity()).getSecondNode();
            double s1 = e1c1.getCenter().distance(e1c2.getCenter());
            double s2 = e2c1.getCenter().distance(e2c2.getCenter());
            if ( s1 < s2 )
                score1 += 1;
            else if ( s2 < s1 )
                score2 += 1;
        }
        return (score1<score2 ? -1 : (score1==score2 ? 0 : 1));
    } //

}
