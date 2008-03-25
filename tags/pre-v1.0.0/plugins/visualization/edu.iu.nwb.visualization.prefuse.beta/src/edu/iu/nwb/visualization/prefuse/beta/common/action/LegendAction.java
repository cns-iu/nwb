package edu.iu.nwb.visualization.prefuse.beta.common.action;

import javax.swing.JComponent;

//unifies legend creation
public interface LegendAction {
	public JComponent getLegend();
	public int getLegendSize();
}
