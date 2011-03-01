package edu.iu.cns.persistence.session.save;

import java.util.Dictionary;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.app.service.filesaver.FileSaverService;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.service.conversion.DataConversionService;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

public class ExportSessionAlgorithmFactory implements AlgorithmFactory {
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
		DataConversionService dataConverter =
			(DataConversionService) this.bundleContext.getService(
				this.bundleContext.getServiceReference(DataConversionService.class.getName()));
		FileSaverService fileSaver =
			(FileSaverService) this.bundleContext.getService(
				this.bundleContext.getServiceReference(FileSaverService.class.getName()));

		return new ExportSessionAlgorithm(logger, dataManager, dataConverter, fileSaver);
	}
}