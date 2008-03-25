package edu.iu.nwb.visualization.prefuse.beta.radialgraph;

import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaAlgorithmFactory;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;



/**
 * @author Weixia(Bonnie) Huang 
 */
public class RadialGraph extends PrefuseBetaAlgorithmFactory {
	protected PrefuseBetaVisualization getVisualization() {
    	return new RadialVisualization();
    }
}
