package edu.iu.sci2.database.star.extract.common;

import java.util.Dictionary;

import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
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
	public static final int WINDOW_HEIGHT = 600;

	/* TODO: Use CIShell constants, if available, and use to build out Data also.
	 * Enumeration & change return type of extractionType
	 */
	public static final String NETWORK_EXTRACTION_TYPE = "network";
	public static final String TABLE_EXTRACTION_TYPE = "table";

	public abstract int minimumLeafTableCount();
	public abstract String extractionType();

	public static StarDatabaseMetadata getMetadata(Data data) {
    	Dictionary<String, Object> metadata = data.getMetadata();

    	return (StarDatabaseMetadata) metadata.get(StarDatabaseLoader.METADATA_KEY);
    }

	public void verifyLeafTables(StarDatabaseMetadata metadata, LogService logger) {
		int leafTableCount = 0;

		for (ColumnDescriptor columnDescriptor :
				metadata.getColumnDescriptorsByHumanReadableName().values()) {
			if (!columnDescriptor.isCoreColumn()) {
				leafTableCount++;
			}
		}

		if (leafTableCount < minimumLeafTableCount()) {
			String format =
				"No leaf tables were found.  There must be at least %d leaf table(s) in order to" +
				" run this %s extraction query on your database.";
			String exceptionMessage =
				String.format(format, minimumLeafTableCount(), extractionType());
			logger.log(LogService.LOG_ERROR, exceptionMessage);
			// TODO new exception type, catch and wrap in this instead
			throw new AlgorithmCreationFailedException(exceptionMessage);
		}
	}

	public static AlgorithmFactory getNetworkQueryRunner(BundleContext bundleContext) {
    	return AlgorithmUtilities.getAlgorithmFactoryByPID(
    		"edu.iu.cns.database.extract.generic.ExtractGraph", bundleContext);
    }

	public static AlgorithmFactory getTableQueryRunner(BundleContext bundleContext) {
    	return AlgorithmUtilities.getAlgorithmFactoryByPID(
    		"edu.iu.cns.database.extract.generic.ExtractTable", bundleContext);
    }
}