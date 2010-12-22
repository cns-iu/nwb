package edu.iu.sci2.database.star.load;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Map;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseService;
import org.osgi.service.log.LogService;

import edu.iu.cns.database.load.framework.exception.InvalidDerbyFieldTypeException;
import edu.iu.sci2.database.star.common.StarDatabaseLoader;
import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptorFactory;
import edu.iu.sci2.database.star.common.utility.StarDatabaseDataValidator;
import edu.iu.sci2.database.star.common.utility.csv.validator.exception.CSVHeaderValidationException;

public class StarDatabaseLoaderAlgorithm implements Algorithm, ProgressTrackable {
    private Data data;
    private File file;
    private String coreEntityDisplayName;
    private String coreEntityTableName;
    private LogService logger;
    private DatabaseService databaseProvider;
    private Map<String, ColumnDescriptor> columnDescriptorsByHumanReadableName;
    private Map<String, ColumnDescriptor> columnDescriptorsByDatabaseName;
    private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;
    
    public StarDatabaseLoaderAlgorithm(
    		Data data,
    		String coreEntityDisplayName,
    		LogService logger,
    		DatabaseService databaseProvider,
    		Dictionary<String, Object> parameters)
    		throws CSVHeaderValidationException, InvalidDerbyFieldTypeException, IOException {
        this.data = data;
        this.file = (File) this.data.getData();
        this.coreEntityDisplayName = coreEntityDisplayName;
        this.coreEntityTableName =
        	StarDatabaseLoader.constructCoreEntityTableName(coreEntityDisplayName);
        this.logger = logger;
        this.databaseProvider = databaseProvider;
        this.columnDescriptorsByHumanReadableName =
			ColumnDescriptorFactory.createColumnDescriptorsByHumanReadableName(
				this.data, parameters);
        this.columnDescriptorsByDatabaseName =
			ColumnDescriptorFactory.mapColumnDescriptorDatabaseNamesToColumnDescriptors(
				this.columnDescriptorsByHumanReadableName.values());

        StarDatabaseDataValidator.validateCoreEntityTableName(
        	coreEntityTableName, columnDescriptorsByHumanReadableName, this.logger);
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
			Database starDatabase = StarDatabaseLoader.readCSVIntoStarDatabase(
				this.file,
				this.coreEntityDisplayName,
				this.coreEntityTableName,
				this.columnDescriptorsByHumanReadableName,
				this.logger,
				this.databaseProvider,
				this.progressMonitor);

			return StarDatabaseLoader.annotateOutputData(
				starDatabase,
				this.data,
				new StarDatabaseMetadata(
					this.coreEntityDisplayName,
					this.coreEntityTableName,
					this.columnDescriptorsByHumanReadableName,
					this.columnDescriptorsByDatabaseName));
    	} catch (AlgorithmCanceledException e) {
    		return null;
    	}
    }

    public void setProgressMonitor(ProgressMonitor progressMonitor) {
    	this.progressMonitor = progressMonitor;
    }

    public ProgressMonitor getProgressMonitor() {
    	return this.progressMonitor;
    }
}