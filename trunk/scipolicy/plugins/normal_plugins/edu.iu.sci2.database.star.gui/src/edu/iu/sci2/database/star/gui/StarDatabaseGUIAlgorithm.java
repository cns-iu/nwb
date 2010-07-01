package edu.iu.sci2.database.star.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.service.database.DatabaseService;
import org.osgi.service.log.LogService;

import edu.iu.cns.database.load.framework.exception.InvalidDerbyFieldTypeException;
import edu.iu.sci2.database.star.gui.builder.LoadStarDatabaseGUIBuilder;
import edu.iu.sci2.database.star.load.StarDatabaseLoaderAlgorithm;
import edu.iu.sci2.database.star.load.parameter.ParameterDescriptors;
import edu.iu.sci2.database.star.load.utility.CSVReaderUtilities;
import edu.iu.sci2.database.star.load.utility.csv.validator.exception.CSVHeaderValidationException;

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
    	for (String columnName : header) {
    		ColumnDescriptor columnDescriptor = new ColumnDescriptor(columnName);
    		columnDescriptors.add(columnDescriptor);
    	}

    	return columnDescriptors;
    }

    private Data[] runDatabaseLoader(ColumnsDataForLoader columnsDataForLoader)
    		throws AlgorithmExecutionException {
    	String coreEntityDisplayName = columnsDataForLoader.getCoreEntityName();
    	Dictionary<String, Object> parameters = createParameters(columnsDataForLoader);

    	try {
    		StarDatabaseLoaderAlgorithm starDatabaseLoader = new StarDatabaseLoaderAlgorithm(
    			this.parentData,
    			coreEntityDisplayName,
    			this.logger,
    			this.databaseService,
    			parameters);
    		starDatabaseLoader.setProgressMonitor(this.progressMonitor);

    		return starDatabaseLoader.execute();
    	} catch (CSVHeaderValidationException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} catch (InvalidDerbyFieldTypeException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} catch (IOException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }

    private static Dictionary<String, Object> createParameters(
    		ColumnsDataForLoader columnsDataForLoader) {
    	Dictionary<String, Object> parameters = new Hashtable<String, Object>();

    	for (ColumnDescriptor columnDescriptor : columnsDataForLoader.getColumnDescriptors()) {
    		String columnName = columnDescriptor.getName();
    		parameters.put(ParameterDescriptors.Type.id(columnName), columnDescriptor.getType());
    		parameters.put(
    			ParameterDescriptors.SeparateEntity.id(columnName),
    			!columnDescriptor.isCoreColumn());
    		parameters.put(
    			ParameterDescriptors.MergeIdentical.id(columnName),
    			columnDescriptor.mergeIdenticalValues());
    		parameters.put(
    			ParameterDescriptors.Separator.id(columnName), columnDescriptor.getSeparator());
    	}

    	return parameters;
    }
}