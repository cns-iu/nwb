package edu.iu.nwb.visualization.prefuse.beta.treeview;

import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaAlgorithmFactory;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;

public class TreeView extends PrefuseBetaAlgorithmFactory {
	protected PrefuseBetaVisualization getVisualization() {
    	return new TreeViewVisualization();
    }
}
