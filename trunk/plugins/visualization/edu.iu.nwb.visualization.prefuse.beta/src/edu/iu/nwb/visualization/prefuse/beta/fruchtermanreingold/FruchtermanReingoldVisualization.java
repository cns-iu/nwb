package edu.iu.nwb.visualization.prefuse.beta.fruchtermanreingold;

import java.awt.BorderLayout;
import java.util.Dictionary;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.layout.graph.FruchtermanReingoldLayout;
import edu.iu.nwb.visualization.prefuse.beta.common.AbstractVisualization;


public class FruchtermanReingoldVisualization extends AbstractVisualization {

	public FruchtermanReingoldVisualization() {

	}

	public Action getLayoutActions(String everythingGroup, Visualization visualization, Dictionary parameters) {
		
		
		ActionList actions = new ActionList();
		actions.add(new FruchtermanReingoldLayout(everythingGroup));
		actions.add(new RepaintAction());
		
		return actions;
	}
	
	protected Component arrangeComponents(Display display, JComponent legend) {
		
		
		JPanel panel = new JPanel(new BorderLayout());
        panel.add(display, BorderLayout.CENTER);
        panel.add(legend, BorderLayout.SOUTH);
        return panel;

	}
	
	protected void setTitle (JFrame frame){
		frame.setTitle("Fruchterman-Reingold with Annotation (prefuse beta)");
	}

	protected Action getInitialDrawActions(String everythingGroup, Visualization visualization, Dictionary parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
