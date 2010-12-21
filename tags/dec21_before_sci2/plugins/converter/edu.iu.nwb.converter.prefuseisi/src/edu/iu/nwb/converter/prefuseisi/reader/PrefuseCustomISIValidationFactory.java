package edu.iu.nwb.converter.prefuseisi.reader;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

/**
 * This Factory is the replacement of PrefuseIsiValidation.java where it 
 * generate addition dataset that contains unique records of the origin.
 * To support origial PrefuseIsiValidation.java, simply add 
 * "type=validator" into the validation.properties
 * @author kongch
 */
public class PrefuseCustomISIValidationFactory implements AlgorithmFactory {
    private BundleContext bundleContext;
    private LogService logger;

    protected void activate(ComponentContext componentContext) {
    	this.bundleContext = componentContext.getBundleContext();
    	this.logger = (LogService)componentContext.locateService("LOG");
    }

	public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		AlgorithmFactory isiPrefuseValidator =
			getPrefuseIsiValidation(this.bundleContext, this.logger);
    	AlgorithmFactory isiToPrefuseConverter =
    		getISIToPrefuseConverter(this.bundleContext, this.logger);
    	AlgorithmFactory isiDuplicateRemover =
    		getISIDuplicateRemover(this.bundleContext, this.logger);
		
        return new PrefuseCustomISIValidationAlgorithm(
        	data,
        	parameters,
        	ciShellContext,
        	isiPrefuseValidator,
        	isiToPrefuseConverter,
        	isiDuplicateRemover);
    }

	/* Get the factory of PrefuseIsiValidation algorithm */
	private static AlgorithmFactory getPrefuseIsiValidation(
			BundleContext bundleContext, LogService logger) {

		return AlgorithmUtilities.getAlgorithmFactoryByPID(
	    		"edu.iu.nwb.converter.prefuseisi.reader.PrefuseIsiValidation", bundleContext);
	}
	
	/* Get the factory of PrefuseIsiReader algorithm */
	private static AlgorithmFactory getISIToPrefuseConverter(
			BundleContext bundleContext, LogService logger) {
		String isiToPrefuseConverterFilter =
    		"(& (in_data=file:text/isi) (out_data=prefuse.data.Table))";

		return AlgorithmUtilities.getAlgorithmFactoryByFilter(
    		isiToPrefuseConverterFilter, bundleContext);
	}

	/* Get the factory of ISIDupRemoverAlgorithm algorithm */
	private static AlgorithmFactory getISIDuplicateRemover(
			BundleContext bundleContext, LogService logger) {
		return AlgorithmUtilities.getAlgorithmFactoryByPID(
    		"edu.iu.nwb.analysis.isidupremover.ISIDupRemoverAlgorithm", bundleContext);
	}
}
