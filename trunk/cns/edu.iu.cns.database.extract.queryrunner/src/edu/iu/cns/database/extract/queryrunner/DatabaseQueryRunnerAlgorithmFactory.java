package edu.iu.cns.database.extract.queryrunner;

import java.util.Dictionary;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

public class DatabaseQueryRunnerAlgorithmFactory implements AlgorithmFactory {
	private BundleContext bundleContext;
	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();
		this.logger = (LogService)componentContext.locateService("LOG");
	}

    @SuppressWarnings("unchecked")	// Dictionary<String, Object>
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
    	DataManagerService dataManager = (DataManagerService)this.bundleContext.getService(
    		this.bundleContext.getServiceReference(DataManagerService.class.getName()));
    	AlgorithmFactory tableQueryRunner = getTableQueryRunner(this.bundleContext);
    	AlgorithmFactory networkQueryRunner = getNetworkQueryRunner(this.bundleContext);

        return new DatabaseQueryRunnerAlgorithm(
        	data, ciShellContext, dataManager, tableQueryRunner, networkQueryRunner, this.logger);
    }

    private static AlgorithmFactory getTableQueryRunner(BundleContext bundleContext) {
    	return AlgorithmUtilities.getAlgorithmFactoryByPID(
    		"edu.iu.cns.database.extract.generic.ExtractTable", bundleContext);
    }

    private static AlgorithmFactory getNetworkQueryRunner(BundleContext bundleContext) {
    	return AlgorithmUtilities.getAlgorithmFactoryByPID(
    		"edu.iu.cns.database.extract.generic.ExtractGraph", bundleContext);
    }
}