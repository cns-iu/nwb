/*
 * Created on Nov 15, 2005
 *
 */
package edu.iu.nwb.visualization.prefuse.alpha.smallworld.control;

import java.awt.event.MouseEvent;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.action.assignment.Layout;
import edu.berkeley.guir.prefuse.activity.Activity;
import edu.berkeley.guir.prefusex.controls.AnchorUpdateControl;


/**
 * Always updates the anchor position, even if it is over
 * an item.  Constrast with AnchorUpdateControl.
 *
 * @author Stephen
 */
public class AnchorAlwaysUpdateControl extends AnchorUpdateControl {

    
    public AnchorAlwaysUpdateControl(Layout layout) {
        super(layout,null);
    } //
    
    public AnchorAlwaysUpdateControl(Layout layout, Activity update) {
        super(new Layout[] {layout}, update);
    } //
    
    public AnchorAlwaysUpdateControl(Layout[] layout, Activity update) {
        super( layout, update );
    } //

    /* (non-Javadoc)
     * @see edu.berkeley.guir.prefuse.event.ControlAdapter#itemMoved(edu.berkeley.guir.prefuse.VisualItem, java.awt.event.MouseEvent)
     */
    public void itemMoved( VisualItem gi, MouseEvent e ) {

        // TODO Auto-generated method stub
        super.moveEvent( e );
    }
    
    public static void main( String[] args ) {

    }
}
