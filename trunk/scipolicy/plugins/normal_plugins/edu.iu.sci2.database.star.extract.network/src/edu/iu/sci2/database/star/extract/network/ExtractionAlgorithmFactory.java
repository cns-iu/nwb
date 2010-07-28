package edu.iu.sci2.database.star.extract.network;

import java.util.Dictionary;

import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

import edu.iu.sci2.database.star.common.StarDatabaseLoader;
import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;

public abstract class ExtractionAlgorithmFactory implements AlgorithmFactory {
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 800;

	@SuppressWarnings("unchecked")
	public static StarDatabaseMetadata getMetadata(Data data) {
    	Dictionary<String, Object> metadata = data.getMetadata();

    	return (StarDatabaseMetadata) metadata.get(StarDatabaseLoader.METADATA_KEY);
    }

	public static void verifyLeafTables(StarDatabaseMetadata metadata, LogService logger) {
		int leafTableCount = 0;

		for (ColumnDescriptor columnDescriptor : metadata.getColumnDescriptorsByHumanReadableName().values()) {
			if (!columnDescriptor.isCoreColumn()) {
				leafTableCount++;
			}
		}

		if (leafTableCount == 0) {
			String exceptionMessage =
				"No leaf tables were found.  There must be at least one leaf table in order to " +
				"run these network extraction queries on your database.";
			logger.log(LogService.LOG_ERROR, exceptionMessage);
			// TODO: Make a custom exception for this?
			throw new RuntimeException(exceptionMessage);
		}
	}

	public static AlgorithmFactory getNetworkQueryRunner(BundleContext bundleContext) {
    	return AlgorithmUtilities.getAlgorithmFactoryByPID(
    		"edu.iu.cns.database.extract.generic.ExtractGraph", bundleContext);
    }
}