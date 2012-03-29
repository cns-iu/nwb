package edu.iu.nwb.visualization.prefuse.beta.graphview;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Component;
import java.util.Dictionary;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.filter.GraphDistanceFilter;
import prefuse.action.layout.graph.ForceDirectedLayout;
import prefuse.activity.Activity;
import prefuse.data.tuple.TupleSet;
import prefuse.util.force.ForceSimulator;
import prefuse.util.ui.JForcePanel;
import prefuse.util.ui.JValueSlider;
import prefuse.visual.VisualItem;
import edu.iu.nwb.visualization.prefuse.beta.common.AbstractVisualization;

public class ForceDirectedVisualization extends AbstractVisualization {

	private ForceDirectedLayout layout;
	private GraphDistanceFilter filter;
	private int hops = 30;

	protected Component arrangeComponents(final Display display,
			JComponent legend) {
		
		display.pan(350, 350);
		display.setForeground(Color.GRAY);
        display.setBackground(Color.WHITE);
        
//      create a panel for editing force values
        ForceSimulator fsim = layout.getForceSimulator();
        JForcePanel fpanel = new JForcePanel(fsim);
        
        final JValueSlider slider = new JValueSlider("Distance", 0, hops, hops);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                filter.setDistance(slider.getValue().intValue());
                display.getVisualization().run(DRAW);
            }
        });
        slider.setBackground(Color.WHITE);
        slider.setPreferredSize(new Dimension(300,30));
        slider.setMaximumSize(new Dimension(300,30));
        
        Box cf = new Box(BoxLayout.Y_AXIS);
        cf.add(slider);
        cf.setBorder(BorderFactory.createTitledBorder("Connectivity Filter"));
        fpanel.add(cf);

        
        fpanel.add(Box.createVerticalGlue());
        
        // create a new JSplitPane to present the interface
        JSplitPane split = new JSplitPane();
        split.setLeftComponent(display);
        split.setRightComponent(fpanel);
        split.setOneTouchExpandable(true);
        split.setContinuousLayout(false);
        split.setDividerLocation(700);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(split, BorderLayout.CENTER);
        panel.add(legend, BorderLayout.SOUTH);
        
        return panel;     

	}

	protected Action getInitialDrawActions(String everythingGroup,
			final Visualization visualization, Dictionary parameters) {
		
		//we have to give this up due to issues with coloring the focused nodes, pending a solution
		/* TupleSet focusGroup = visualization.getGroup(Visualization.FOCUS_ITEMS); 
        focusGroup.addTupleSetListener(new TupleSetListener() {
            public void tupleSetChanged(TupleSet ts, Tuple[] add, Tuple[] rem)
            {
                for ( int i=0; i<rem.length; ++i )
                    ((VisualItem)rem[i]).setFixed(false);
                for ( int i=0; i<add.length; ++i ) {
                    ((VisualItem)add[i]).setFixed(false);
                    //((VisualItem)add[i]).setFixed(true);
                }
                if ( ts.getTupleCount() == 0 ) {
                    ts.addTuple(rem[0]);
                    ((VisualItem)rem[0]).setFixed(false);
                }
                visualization.run(DRAW);
            }
        }); */
		
		filter = new GraphDistanceFilter(everythingGroup, hops);
		TupleSet group = visualization.getGroup(everythingGroup + ".nodes");
		
		//we need these so the distance filter has something to work off of
		VisualItem tuple = (VisualItem) group.tuples().next();
		visualization.getGroup(Visualization.FOCUS_ITEMS).setTuple(tuple);
		
		return filter;
	}

	protected Action getLayoutActions(String everythingGroup,
			Visualization visualization, Dictionary parameters) {
		
		ActionList animate = new ActionList(Activity.INFINITY);
	    layout = new ForceDirectedLayout(everythingGroup);
	    animate.add(layout);
	    //animate.add(new QualityControlAnimator());
	    animate.add(new RepaintAction());
		return animate;
	}
	
	protected void setTitle (JFrame frame){
	       frame.setTitle("Force Directed with Annotation (prefuse beta)");
	}

}
