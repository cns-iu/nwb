package edu.iu.sci2.database.star.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

import edu.iu.sci2.database.star.common.StarDatabaseLoader;
import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.common.utility.CSVReaderUtilities;
import edu.iu.sci2.database.star.gui.builder.LoadStarDatabaseGUIBuilder;

public class StarDatabaseGUIAlgorithm implements Algorithm, ProgressTrackable {
	public static final String WINDOW_TITLE = "Star Database Loader";
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 800;

	private Data parentData;
	private LogService logger;
	private DatabaseService databaseService;
	private ProgressMonitor progressMonitor;
    
    public StarDatabaseGUIAlgorithm(
    		Data parentData,
    		LogService logger,
    		DatabaseService databaseService) {
    	this.parentData = parentData;
    	this.logger = logger;
    	this.databaseService = databaseService;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		Collection<ColumnDescriptor> columnDescriptors = createColumnDescriptors();
    		ColumnsDataForLoader columnsDataForLoader = LoadStarDatabaseGUIBuilder.gatherUserInput(
    			WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, columnDescriptors);

    		return runDatabaseLoader(columnsDataForLoader);
    	} catch (IOException e) {
    		String exceptionMessage =
    			"The following error occurred when attempting to read your CSV file: \"" +
    			e.getMessage() +
    			"\".";
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	}
    }

    public ProgressMonitor getProgressMonitor() {
    	return this.progressMonitor;
    }

    public void setProgressMonitor(ProgressMonitor progressMonitor) {
    	this.progressMonitor = progressMonitor;
    }

    private Collection<ColumnDescriptor> createColumnDescriptors() throws IOException {
    	String[] header = CSVReaderUtilities.getHeader(this.parentData);
    	Collection<ColumnDescriptor> columnDescriptors = new ArrayList<ColumnDescriptor>();
    	int index = 0;

    	for (String columnName : header) {
    		ColumnDescriptor columnDescriptor = new ColumnDescriptor(index, columnName);
    		columnDescriptors.add(columnDescriptor);
    		index++;
    	}

    	return columnDescriptors;
    }

    private Data[] runDatabaseLoader(ColumnsDataForLoader columnsDataForLoader)
    		throws AlgorithmExecutionException {
    	String coreEntityDisplayName = columnsDataForLoader.getCoreEntityName();
    	String coreEntityTableName =
    		StarDatabaseLoader.constructCoreEntityTableName(coreEntityDisplayName);
    	Map<String, ColumnDescriptor> columnDescriptors =
    		mapColumnDescriptorNamesToColumnDescriptors(
    			columnsDataForLoader.getColumnDescriptors());

    	try {
    		Database starDatabase = StarDatabaseLoader.readCSVIntoStarDatabase(
				(File) this.parentData.getData(),
				coreEntityDisplayName,
				coreEntityTableName,
				columnDescriptors,
				this.logger,
				this.databaseService,
				this.progressMonitor);

    		return StarDatabaseLoader.annotateOutputData(
    			starDatabase,
    			this.parentData,
    			new StarDatabaseMetadata(
    				coreEntityDisplayName, coreEntityTableName, columnDescriptors));
    	} catch (AlgorithmCanceledException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
//    	} catch (CSVHeaderValidationException e) {
//    		throw new AlgorithmExecutionException(e.getMessage(), e);
//    	} catch (InvalidDerbyFieldTypeException e) {
//    		throw new AlgorithmExecutionException(e.getMessage(), e);
//    	} catch (IOException e) {
//    		throw new AlgorithmExecutionException(e.getMessage(), e);
//    	}
    }

    private static Map<String, ColumnDescriptor> mapColumnDescriptorNamesToColumnDescriptors(
    		Collection<ColumnDescriptor> columnDescriptors) {
    	Map<String, ColumnDescriptor> namesToDescriptors =
    		new HashMap<String, ColumnDescriptor>();

    	for (ColumnDescriptor columnDescriptor : columnDescriptors) {
    		namesToDescriptors.put(columnDescriptor.getName(), columnDescriptor);
    	}

    	return namesToDescriptors;
    }

//    private static Dictionary<String, Object> createParameters(
//    		ColumnsDataForLoader columnsDataForLoader) {
//    	Dictionary<String, Object> parameters = new Hashtable<String, Object>();
//
//    	for (ColumnDescriptor columnDescriptor : columnsDataForLoader.getColumnDescriptors()) {
//    		String columnName = columnDescriptor.getName();
//    		parameters.put(ParameterDescriptors.Type.id(columnName), columnDescriptor.getType());
//    		parameters.put(
//    			ParameterDescriptors.SeparateEntity.id(columnName),
//    			!columnDescriptor.isCoreColumn());
//    		parameters.put(
//    			ParameterDescriptors.MergeIdentical.id(columnName),
//    			columnDescriptor.mergeIdenticalValues());
//    		parameters.put(
//    			ParameterDescriptors.Separator.id(columnName), columnDescriptor.getSeparator());
//    	}
//
//    	return parameters;
//    }
}