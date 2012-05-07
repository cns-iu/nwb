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
		
		AlgorithmFactory mergeIdenticalRecords = getMergeIdenticalRecords(this.bundleContext);
        return new ISIDatabaseLoaderAlgorithm(data, ciShellContext, mergeIdenticalRecords);
    }
    
    protected void activate(ComponentContext componentContext) {
    	this.bundleContext = componentContext.getBundleContext();
    }
    
    private static AlgorithmFactory getMergeIdenticalRecords(BundleContext bundleContext) {
    	return AlgorithmUtilities.getAlgorithmFactoryByPID("edu.iu.nwb.analysis.isidupremover.ISIDupRemoverAlgorithm", bundleContext);
    }
}