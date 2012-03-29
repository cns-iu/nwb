package edu.iu.nwb.composite.isiloadandclean;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;


public class ISILoadAndCleanAlgorithmFactory implements AlgorithmFactory {
    private BundleContext bundleContext;
    private LogService logger;

    protected void activate(ComponentContext componentContext) {
    	this.bundleContext = componentContext.getBundleContext();
    	this.logger = (LogService)componentContext.locateService("LOG");
    }

    @SuppressWarnings("unchecked")	// Dictionary<String, Object>
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
    	AlgorithmFactory fileLoader = getFileLoader(this.bundleContext, this.logger);
    	AlgorithmFactory isiToPrefuseConverter =
    		getISIToPrefuseConverter(this.bundleContext, this.logger);
    	AlgorithmFactory isiDuplicateRemover =
    		getISIDuplicateRemover(this.bundleContext, this.logger);
		
        return new ISILoadAndCleanAlgorithm(
        	data,
        	parameters,
        	ciShellContext,
        	fileLoader,
        	isiToPrefuseConverter,
        	isiDuplicateRemover);
    }

    private static AlgorithmFactory getFileLoader(BundleContext bundleContext, LogService logger) {
    	return AlgorithmUtilities.getAlgorithmFactoryByPID(
    		"org.cishell.reference.gui.persistence.load.FileLoadAlgorithm", bundleContext);
    }

	private static AlgorithmFactory getISIToPrefuseConverter(
			BundleContext bundleContext, LogService logger) {
		String isiToPrefuseConverterFilter =
    		"(& (in_data=file:text/isi) (out_data=prefuse.data.Table))";

		return AlgorithmUtilities.getAlgorithmFactoryByFilter(
    		isiToPrefuseConverterFilter, bundleContext);
	}

	private static AlgorithmFactory getISIDuplicateRemover(
			BundleContext bundleContext, LogService logger) {
		return AlgorithmUtilities.getAlgorithmFactoryByPID(
    		"edu.iu.nwb.analysis.isidupremover.ISIDupRemoverAlgorithm", bundleContext);
	}
}
