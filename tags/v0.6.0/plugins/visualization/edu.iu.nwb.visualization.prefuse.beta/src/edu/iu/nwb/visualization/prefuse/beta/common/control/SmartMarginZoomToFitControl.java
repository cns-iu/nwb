package edu.iu.nwb.visualization.prefuse.beta.common.control;

import java.awt.Insets;
import java.awt.event.MouseEvent;

import prefuse.Display;
import prefuse.Visualization;
import prefuse.controls.ZoomToFitControl;
import prefuse.util.GraphicsLib;
import prefuse.util.ui.UILib;

public class SmartMarginZoomToFitControl extends ZoomToFitControl {
	private String group;

	public SmartMarginZoomToFitControl(String group, int button) {
		super(group, button);
		this.group = group;
	}

	public void mouseClicked(MouseEvent e) {
    	Display display = (Display)e.getComponent();
    	
    	
		//super.setMargin(display.getVisualization().getBounds(group).getBounds().width / 100);
		super.mouseClicked(e);
    }
}
