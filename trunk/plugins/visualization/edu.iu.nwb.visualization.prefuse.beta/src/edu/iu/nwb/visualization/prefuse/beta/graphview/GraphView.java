package edu.iu.nwb.visualization.prefuse.beta.graphview;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.MetaTypeProvider;

import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaAlgorithmFactory;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;

import prefuse.data.Graph;



/**
 * @author Weixia(Bonnie) Huang 
 */
public class GraphView extends PrefuseBetaAlgorithmFactory {
	protected PrefuseBetaVisualization getVisualization() {
    	return new ForceDirectedVisualization();
    }
}
