package edu.iu.scipolicy.analysis.blondelcommunitydetection;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

import edu.iu.scipolicy.utilities.AlgorithmUtilities;

public class BlondelCommunityDetectionAlgorithmFactory implements AlgorithmFactory {
	private BundleContext bundleContext;
	
	protected void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();
	}
	
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
    	AlgorithmFactory blondelExecutableAlgorithmFactory =
    		AlgorithmUtilities.getAlgorithmFactoryByPID(
    			"edu.iu.scipolicy.analysis.blondelexecutable",
    			this.bundleContext);
    	
        return new BlondelCommunityDetectionAlgorithm(
        	blondelExecutableAlgorithmFactory, data, parameters, context);
    }
}