package edu.iu.cns.persistence.session.restore;

import java.util.Dictionary;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.fileloader.FileLoaderService;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

public class ImportSessionAlgorithmFactory implements AlgorithmFactory {
	private BundleContext bundleContext;

	protected void activate(ComponentContext componentContext) {
		this.bundleContext = componentContext.getBundleContext();
	}

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	LogService logger = (LogService) this.bundleContext.getService(
    		this.bundleContext.getServiceReference(LogService.class.getName()));
		DataManagerService dataManager = (DataManagerService) this.bundleContext.getService(
    		this.bundleContext.getServiceReference(DataManagerService.class.getName()));
		FileLoaderService fileLoader = (FileLoaderService) this.bundleContext.getService(
			this.bundleContext.getServiceReference(FileLoaderService.class.getName()));

        return new ImportSessionAlgorithm(logger, dataManager, fileLoader);
    }
}