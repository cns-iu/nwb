package edu.iu.nwb.visualization.prefuse.beta.specified;

import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaAlgorithmFactory;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;



/**
 * @author Weixia(Bonnie) Huang 
 */
public class Specified extends PrefuseBetaAlgorithmFactory {
	protected PrefuseBetaVisualization getVisualization() {
    	return new SpecifiedVisualization();
    }
}
