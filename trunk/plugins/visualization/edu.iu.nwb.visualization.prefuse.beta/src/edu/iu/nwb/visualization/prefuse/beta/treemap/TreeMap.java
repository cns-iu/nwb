package edu.iu.nwb.visualization.prefuse.beta.treemap;

import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaAlgorithmFactory;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;

public class TreeMap extends PrefuseBetaAlgorithmFactory {
	protected PrefuseBetaVisualization getVisualization() {
    	return new TreeMapVisualization();
    }
}
