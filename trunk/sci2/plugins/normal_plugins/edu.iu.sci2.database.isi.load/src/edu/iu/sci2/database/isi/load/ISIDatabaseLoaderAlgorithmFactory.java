package edu.iu.sci2.database.isi.load;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

public class ISIDatabaseLoaderAlgorithmFactory implements AlgorithmFactory {
    private BundleContext bundleContext;

	public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		
		AlgorithmFactory mergeIdentical = getMergeIdentical(this.bundleContext);
        return new ISIDatabaseLoaderAlgorithm(data, ciShellContext, mergeIdentical);
    }
    
    protected void activate(ComponentContext componentContext) {
    	this.bundleContext = componentContext.getBundleContext();
    }
    
    private static AlgorithmFactory getMergeIdentical(BundleContext bundleContext) {
    	return AlgorithmUtilities.getAlgorithmFactoryByPID(
        		"edu.iu.sci2.medline.validator.Validator", bundleContext);
    }
}