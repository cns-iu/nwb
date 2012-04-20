package edu.iu.sci2.database.scopus.load;


import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
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

import prefuse.data.Table;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DerbyDatabaseCreator;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.nwb.converter.prefusescopus.util.TableCleaner;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.nwb.shared.isiutil.exception.ISILoadingException;

public class ScopusDatabaseLoaderAlgorithm implements Algorithm, ProgressTrackable {
	public static final boolean SHOULD_NORMALIZE_AUTHOR_NAMES = true;
	public static final boolean SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS = true;
	public static final boolean SHOULD_FILL_FILE_METADATA = true;
	public static final boolean SHOULD_CLEAN_CITED_REFERENCES = false;

    private Data inData;
    private LogService logger;
    private DatabaseService databaseProvider;
    private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;
    
    public ScopusDatabaseLoaderAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
        this.inData = data[0];

        this.logger = (LogService)ciShellContext.getService(LogService.class.getName());
        this.databaseProvider =
        	(DatabaseService)ciShellContext.getService(DatabaseService.class.getName());
    }

    @Override
	public Data[] execute() throws AlgorithmExecutionException {
    	System.out.println("EXECUTING");
    	try {
	    	// Convert input ISI data to an ISI table.

    		Table scopusTable = scopusToTable(this.inData);
    		System.out.println("Num rows: " + scopusTable.getRowCount());
    		
    		TableCleaner tc = new TableCleaner(this.logger);
    		scopusTable = tc.cleanTable(scopusTable);
    		
    		System.out.println("Num rows: " + scopusTable.getRowCount());
    		
    		for (int i = 0; i < scopusTable.getColumnCount(); i++) {
    			System.out.println(scopusTable.getColumnName(i) + ": " + scopusTable.getColumnType(i));
    		}
//    		// Preprocess the ISI table: generate unique IDs for rows without them.
//
//    		ISITablePreprocessor.generateMissingUniqueIDs(isiTable);
//
//	    	// Preprocess the ISI table: remove duplicate Documents (on the row level).
//
//    		Collection<Integer> rows =
//    			ISITablePreprocessor.removeRowsWithDuplicateDocuments(scopusTable);

    		// Convert the ISI table to an ISI database.

    		Database database = convertTableToDatabase(scopusTable);

	    	// Annotate ISI database as output data with metadata and return it.

    	    return annotateOutputData(database, this.inData);
    	} catch (AlgorithmCanceledException e) {
    		return new Data[] {};
    	} catch (Throwable e) {
    		e.printStackTrace();
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }

    @Override
	public void setProgressMonitor(ProgressMonitor progressMonitor) {
    	this.progressMonitor = progressMonitor;
    }

    @Override
	public ProgressMonitor getProgressMonitor() {
    	return this.progressMonitor;
    }
    
    private static Table scopusToTable(Data inputData) throws AlgorithmExecutionException {
    	String inFile = (String) inputData.getData();
    	
    	PrefuseCsvReader reader = new PrefuseCsvReader(new File(inFile));
    	
    	return (Table) reader.execute()[0].getData();
    }

    private Database convertTableToDatabase(Table table)
    		throws AlgorithmCanceledException, ISILoadingException {
    	try {
//    		double totalWork = calculateTotalWork(rows);
    		double totalWork = 10; // XXX calculate for real like above
    		startProgressMonitor(totalWork);

	    	// Create an in-memory ISI model based off of the table.

    		DatabaseModel model =
    			new ScopusTableModelParser(this.logger).parseModel(table);

	    	// Use the ISI model to create an ISI database.

    		Database database = DerbyDatabaseCreator.createFromModel(
    			this.databaseProvider, model, "Scopus", this.progressMonitor, totalWork);

    		stopProgressMonitor();

    		return database;
    	} catch (DatabaseCreationException e) {
    		throw new ISILoadingException(e.getMessage(), e);
    	} catch (SQLException e) {
    		throw new ISILoadingException(e.getMessage(), e);
    	}
    }

    private static double calculateTotalWork(Collection<Integer> rows) {
		double totalWork = rows.size()
				/ DerbyDatabaseCreator.PERCENTAGE_OF_PROGRESS_FOR_MODEL_CREATION;

    	return totalWork;
    }

    private void startProgressMonitor(double totalWork) {
		this.progressMonitor.start(
			(ProgressMonitor.WORK_TRACKABLE |
				ProgressMonitor.CANCELLABLE |
				ProgressMonitor.PAUSEABLE),
			totalWork);
		this.progressMonitor.describeWork("Loading Scopus data into a database.");
    }

    private void stopProgressMonitor() {
    	this.progressMonitor.done();
    }

    private static Data[] annotateOutputData(Database isiDatabase, Data parentData) {
    	Data data = new BasicData(isiDatabase, ISI.ISI_DATABASE_MIME_TYPE);
    	Dictionary<String, Object> metadata = data.getMetadata();
    	metadata.put(
    		DataProperty.LABEL,
    		"Publication Database From " +
    			FileUtilities.extractFileNameWithExtension(parentData.getData().toString()));
    	metadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);
    	metadata.put(DataProperty.PARENT, parentData);

    	return new Data[] { data };
    }
}