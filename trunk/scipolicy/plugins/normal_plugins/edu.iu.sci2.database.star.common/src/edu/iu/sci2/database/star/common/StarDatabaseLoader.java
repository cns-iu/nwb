package edu.iu.sci2.database.star.common;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import au.com.bytecode.opencsv.CSVReader;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DerbyDatabaseCreator;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.common.utility.CSVModelParser;
import edu.iu.sci2.database.star.common.utility.CSVReaderUtilities;

public class StarDatabaseLoader {
	public static final String STAR_DATABASE_MIME_TYPE = "db:star";
	public static final String METADATA_KEY = "star_database_metadata";

	public static String constructCoreEntityTableName(String coreEntityDisplayName) {
		return StarDatabaseCSVDataValidationRules.normalizeName(coreEntityDisplayName);
	}

	public static Database readCSVIntoStarDatabase(
    		File file,
    		String coreEntityDisplayName,
    		String coreEntityTableName,
    		Map<String, ColumnDescriptor> columnDescriptors,
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

    		return database;
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

	@SuppressWarnings("unchecked")
    public static Data[] annotateOutputData(
    		Database isiDatabase, Data parentData, StarDatabaseMetadata starDatabaseMetadata) {
    	Data data = new BasicData(isiDatabase, STAR_DATABASE_MIME_TYPE);
    	Dictionary<String, Object> metadata = (Dictionary<String, Object>)data.getMetadata();
    	metadata.put(DataProperty.LABEL, formOutDataLabel(parentData));
    	metadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);
    	metadata.put(DataProperty.PARENT, parentData);
    	metadata.put(METADATA_KEY, starDatabaseMetadata);

    	return new Data[] { data };
    }

    private static String formOutDataLabel(Data parentData) {
    	String absoluteFilePath = ((File)parentData.getData()).getAbsolutePath();

    	return
    		"Star Database From " + FileUtilities.extractFileNameWithExtension(absoluteFilePath);
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
}