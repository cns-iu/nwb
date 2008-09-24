package edu.iu.nwb.visualization.prefuse.beta.common.control;

import java.awt.event.MouseEvent;

import prefuse.controls.ZoomToFitControl;

public class SmartMarginZoomToFitControl extends ZoomToFitControl {
	//private String group;

	public SmartMarginZoomToFitControl(String group, int button) {
		super(group, button);
		//this.group = group;
	}

	public void mouseClicked(MouseEvent e) {
    	//Display display = (Display)e.getComponent();
    	
    	
		//super.setMargin(display.getVisualization().getBounds(group).getBounds().width / 100);
		super.mouseClicked(e);
    }
}
