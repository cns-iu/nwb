package edu.iu.sci2.database.star.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseService;
import org.cishell.utility.swt.GUICanceledException;
import org.osgi.service.log.LogService;

import edu.iu.sci2.database.star.common.StarDatabaseLoader;
import edu.iu.sci2.database.star.common.StarDatabaseMetadata;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptorFactory;
import edu.iu.sci2.database.star.common.utility.CSVReaderUtilities;
import edu.iu.sci2.database.star.gui.builder.GUIBuilder;

public class StarDatabaseGUIAlgorithm implements Algorithm, ProgressTrackable {
	public static final String WINDOW_TITLE = "Generic-CSV Database Loader";
	public static final int WINDOW_WIDTH = 400;
	public static final int WINDOW_HEIGHT = 700;

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
    		ColumnsDataForLoader columnsDataForLoader = GUIBuilder.gatherUserInput(
    			WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, columnDescriptors);

    		return runDatabaseLoader(columnsDataForLoader);
    	} catch (AlgorithmCanceledException e) { 
    		this.logger.log(LogService.LOG_WARNING, e.getMessage());

    		return null;
    	} catch (GUICanceledException e) { 
    		this.logger.log(LogService.LOG_WARNING, e.getMessage());

    		return null;
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
    		throws AlgorithmCanceledException, AlgorithmExecutionException {
    	String coreEntityDisplayName = columnsDataForLoader.getCoreEntityName();
    	String coreEntityTableName =
    		StarDatabaseLoader.constructCoreEntityTableName(coreEntityDisplayName);
    	Map<String, ColumnDescriptor> columnDescriptorsByHumanReadableName =
    		ColumnDescriptorFactory.mapColumnDescriptorHumanReadableNamesToColumnDescriptors(
    			columnsDataForLoader.getColumnDescriptors());
    	Map<String, ColumnDescriptor> columnDescriptorsByDatabaseName =
    		ColumnDescriptorFactory.mapColumnDescriptorDatabaseNamesToColumnDescriptors(
    			columnsDataForLoader.getColumnDescriptors());

		Database starDatabase = StarDatabaseLoader.readCSVIntoStarDatabase(
			(File) this.parentData.getData(),
			coreEntityDisplayName,
			coreEntityTableName,
			columnDescriptorsByHumanReadableName,
			this.logger,
			this.databaseService,
			this.progressMonitor);

		return StarDatabaseLoader.annotateOutputData(
			starDatabase,
			this.parentData,
			new StarDatabaseMetadata(
				coreEntityDisplayName,
				coreEntityTableName,
				columnDescriptorsByHumanReadableName,
				columnDescriptorsByDatabaseName));
    }
}