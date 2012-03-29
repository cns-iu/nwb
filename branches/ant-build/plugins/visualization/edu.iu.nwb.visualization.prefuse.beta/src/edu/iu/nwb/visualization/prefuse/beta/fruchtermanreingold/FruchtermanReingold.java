package edu.iu.nwb.visualization.prefuse.beta.fruchtermanreingold;

import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaAlgorithmFactory;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;



/**
 * @author Weixia(Bonnie) Huang 
 */
public class FruchtermanReingold extends PrefuseBetaAlgorithmFactory {
	protected PrefuseBetaVisualization getVisualization() {
    	return new FruchtermanReingoldVisualization();
    }
}
