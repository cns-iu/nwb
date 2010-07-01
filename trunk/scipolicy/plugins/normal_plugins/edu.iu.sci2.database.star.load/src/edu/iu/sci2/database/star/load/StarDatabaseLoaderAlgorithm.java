package edu.iu.sci2.database.star.load;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import au.com.bytecode.opencsv.CSVReader;
import edu.iu.cns.database.load.framework.exception.InvalidDerbyFieldTypeException;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DerbyDatabaseCreator;
import edu.iu.sci2.database.star.load.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.load.parameter.ColumnDescriptorFactory;
import edu.iu.sci2.database.star.load.utility.CSVModelParser;
import edu.iu.sci2.database.star.load.utility.CSVReaderUtilities;
import edu.iu.sci2.database.star.load.utility.StarDatabaseDataValidator;
import edu.iu.sci2.database.star.load.utility.csv.validator.exception.CSVHeaderValidationException;

public class StarDatabaseLoaderAlgorithm implements Algorithm, ProgressTrackable {
	// TODO: Move this somewhere else.
	public static final String STAR_DATABASE_MIME_TYPE = "db:star";

    private Data data;
    private File file;
    private String coreEntityDisplayName;
    private String coreEntityTableName;
    private LogService logger;
    private DatabaseService databaseProvider;
    private Map<String, ColumnDescriptor> columnDescriptors;
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
        this.coreEntityTableName = constructCoreEntityTableName(coreEntityDisplayName);
        this.logger = logger;
        this.databaseProvider = databaseProvider;
        this.columnDescriptors =
			ColumnDescriptorFactory.createColumnDescriptors(this.data, parameters);

        StarDatabaseDataValidator.validateCoreEntityTableName(
        	coreEntityTableName, columnDescriptors, this.logger);
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
			return readCSVIntoStarDatabase(
				this.file,
				this.coreEntityDisplayName,
				this.coreEntityTableName,
				this.columnDescriptors,
				this.data,
				this.logger,
				this.databaseProvider,
				this.progressMonitor);
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

    public static Data[] readCSVIntoStarDatabase(
    		File file,
    		String coreEntityDisplayName,
    		String coreEntityTableName,
    		Map<String, ColumnDescriptor> columnDescriptors,
    		Data parentData,
    		LogService logger,
    		DatabaseService databaseService,
    		ProgressMonitor progressMonitor)
    		throws AlgorithmCanceledException, AlgorithmExecutionException {
    	try {
    		CSVReader reader = CSVReaderUtilities.createCSVReader(file, true);
			String[] header = reader.readNext();
			String[] coreColumns = determineCoreColumns(header, columnDescriptors);
			String[] nonCoreColumns = determineNonCoreColumns(header, columnDescriptors);
    		DatabaseModel model = CSVModelParser.parse(
    			coreEntityDisplayName,
    			coreEntityTableName,
    			coreColumns,
    			nonCoreColumns,
    			reader,
    			columnDescriptors,
    			progressMonitor);
    		Database database = DerbyDatabaseCreator.createFromModel(
    			databaseService, model, "Star", progressMonitor);

    		return annotateOutputData(database, parentData);
    	} catch (DatabaseCreationException e) {
    		String exceptionMessage =
    			"The following error occurred when attempting to create your database: " +
    			"\"" + e.getMessage() + "\"";
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	} catch (IOException e) {
    		String exceptionMessage =
    			"The following error occurred when attempting to read from your CSV file: " +
    			"\"" + e.getMessage() + "\"";
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	} catch (SQLException e) {
    		String exceptionMessage =
    			"The following error occurred when attempting to operate on your database: " +
    			"\"" + e.getMessage() + "\"";
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	}
    }

    private static String constructCoreEntityTableName(String coreEntityDisplayName) {
		return StarDatabaseCSVDataValidationRules.normalizeName(coreEntityDisplayName);
	}

    private static String[] determineCoreColumns(
    		String[] header, Map<String, ColumnDescriptor> columnDescriptors) {
    	List<String> coreColumns = new ArrayList<String>();

    	for (String columnName : header) {
    		if (!columnDescriptors.get(columnName).isMultiValued()) {
    			coreColumns.add(columnName);
    		}
    	}

    	return (String[]) coreColumns.toArray(new String[0]);
    }

    private static String[] determineNonCoreColumns(
    		String[] header, Map<String, ColumnDescriptor> columnDescriptors) {
    	List<String> nonCoreColumns = new ArrayList<String>();

    	for (String columnName : header) {
    		if (columnDescriptors.get(columnName).isMultiValued()) {
    			nonCoreColumns.add(columnName);
    		}
    	}

    	return (String[]) nonCoreColumns.toArray(new String[0]);
    }

    @SuppressWarnings("unchecked")
    private static Data[] annotateOutputData(Database isiDatabase, Data parentData) {
    	Data data = new BasicData(isiDatabase, STAR_DATABASE_MIME_TYPE);
    	Dictionary<String, Object> metadata = (Dictionary<String, Object>)data.getMetadata();
    	metadata.put(DataProperty.LABEL, formOutDataLabel(parentData));
    	metadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);
    	metadata.put(DataProperty.PARENT, parentData);

    	return new Data[] { data };
    }

    private static String formOutDataLabel(Data parentData) {
    	String absoluteFilePath = ((File)parentData.getData()).getAbsolutePath();

    	return
    		"Star Database From " + FileUtilities.extractFileNameWithExtension(absoluteFilePath);
    }
}