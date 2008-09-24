package edu.iu.nwb.visualization.prefuse.beta.graphview;

import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaAlgorithmFactory;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;



/**
 * @author Weixia(Bonnie) Huang 
 */
public class GraphView extends PrefuseBetaAlgorithmFactory {
	protected PrefuseBetaVisualization getVisualization() {
    	return new ForceDirectedVisualization();
    }
}
