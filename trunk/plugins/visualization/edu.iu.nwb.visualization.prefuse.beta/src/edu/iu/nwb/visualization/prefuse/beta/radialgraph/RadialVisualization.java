package edu.iu.nwb.visualization.prefuse.beta.radialgraph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Dictionary;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.GroupAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.PolarLocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.RadialTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.ControlAdapter;
import prefuse.controls.FocusControl;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Tuple;
import prefuse.data.event.TupleSetListener;
import prefuse.data.tuple.DefaultTupleSet;
import prefuse.data.tuple.TupleSet;
import prefuse.util.ui.JFastLabel;
import prefuse.visual.VisualItem;
import edu.iu.nwb.visualization.prefuse.beta.common.AbstractVisualization;
import edu.iu.nwb.visualization.prefuse.beta.common.Constants;

public class RadialVisualization extends AbstractVisualization {

	private String linear = "linear";


	protected Component arrangeComponents(Display display,
			JComponent legend) {
		
		display.getVisualization().alwaysRunAfter("retree", LAYOUT);
		display.addControlListener(new FocusControl(1, "retree"));
		
		final JFastLabel title = new JFastLabel("                 ");
        title.setPreferredSize(new Dimension(350, 20));
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        title.setBorder(BorderFactory.createEmptyBorder(3,0,0,0));
        title.setFont(Constants.TITLE_FONT);
        
        display.addControlListener(new ControlAdapter() {
            public void itemEntered(VisualItem item, MouseEvent e) {
                if ( item.canGetString(Constants.label) )
                    title.setText(item.getString(Constants.label));
            }
            public void itemExited(VisualItem item, MouseEvent e) {
                title.setText(null);
            }
        });
        
        Box box = new Box(BoxLayout.X_AXIS);
        box.add(Box.createHorizontalStrut(10));
        box.add(title);
        box.add(Box.createHorizontalGlue());
        box.add(Box.createHorizontalStrut(3));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(display, BorderLayout.CENTER);
        panel.add(box, BorderLayout.SOUTH);
        
        JPanel overall = new JPanel(new BorderLayout());
        overall.add(panel, BorderLayout.CENTER);
        overall.add(legend, BorderLayout.SOUTH);
        
        return overall; 
	}

	protected void setTitle (JFrame frame){
		frame.setTitle("Radial Tree/Graph with Annotation (prefuse beta)");
	}
	
	protected Action getInitialDrawActions(String everythingGroup,
			Visualization visualization, Dictionary parameters) {
		
		ActionList retree = new ActionList();
		retree.add(new TreeRootAction(everythingGroup));
		retree.add(new RadialTreeLayout(everythingGroup));
		retree.add(new CollapsedSubtreeLayout(everythingGroup));
		
		
		return retree;
	}

	protected Action getLayoutActions(String everythingGroup,
			final Visualization visualization, Dictionary parameters) {
		
		ActionList retree = new ActionList();
		retree.add(new TreeRootAction(everythingGroup));
		retree.add(new RadialTreeLayout(everythingGroup));
		retree.add(new CollapsedSubtreeLayout(everythingGroup));
		
		visualization.putAction("retree", retree);
		
		ActionList actions = new ActionList(1250);
		actions.setPacingFunction(new SlowInSlowOutPacer());
		
		actions.add(new QualityControlAnimator());
		actions.add(new VisibilityAnimator(everythingGroup));
		actions.add(new PolarLocationAnimator(everythingGroup + ".nodes", linear));
		actions.add(new RepaintAction());
		
		
		visualization.addFocusGroup(linear, new DefaultTupleSet());
        visualization.getGroup(Visualization.FOCUS_ITEMS).addTupleSetListener(
            new TupleSetListener() {
                public void tupleSetChanged(TupleSet t, Tuple[] add, Tuple[] rem) {
                    TupleSet linearInterp = visualization.getGroup(linear);
                    if ( add.length < 1 ) return; linearInterp.clear();
                    for ( Node n = (Node)add[0]; n!=null; n=n.getParent() )
                        linearInterp.addTuple(n);
                }
            }
        );
		
		
		
		return actions;
	}
	
	
	/**
     * Switch the root of the tree by requesting a new spanning tree
     * at the desired root
     */
    public static class TreeRootAction extends GroupAction {
        public TreeRootAction(String graphGroup) {
            super(graphGroup);
        }
        public void run(double frac) {
            TupleSet focus = m_vis.getGroup(Visualization.FOCUS_ITEMS);
            if ( focus==null || focus.getTupleCount() == 0 ) return;
            
            Graph g = (Graph)m_vis.getGroup(m_group);
            Node f = null;
            while ( !g.containsTuple(f=(Node)focus.tuples().next()) ) {
                f = null;
            }
            if ( f == null ) return;
            g.getSpanningTree(f);
        }
    }
}
