package edu.iu.scipolicy.database.isi.load;

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
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DerbyDatabaseCreator;
import edu.iu.nwb.shared.isiutil.ISITableReaderHelper;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.nwb.shared.isiutil.exception.ISILoadingException;
import edu.iu.nwb.shared.isiutil.exception.ReadISIFileException;
import edu.iu.scipolicy.database.isi.load.utilities.ISITablePreprocessor;
import edu.iu.scipolicy.database.isi.load.utilities.parser.ISITableModelParser;

public class ISIDatabaseLoaderAlgorithm implements Algorithm, ProgressTrackable {
	public static final boolean SHOULD_NORMALIZE_AUTHOR_NAMES = true;
	public static final boolean SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS = true;
	public static final boolean SHOULD_FILL_FILE_METADATA = true;
	public static final boolean SHOULD_CLEAN_CITED_REFERENCES = false;

	public static final float PERCENTAGE_OF_PROGRESS_FOR_CREATING_MODEL = 0.1f;

    private Data inData;
    private LogService logger;
    private DatabaseService databaseProvider;
    private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;
    
    public ISIDatabaseLoaderAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext ciShellContext) {
        this.inData = data[0];

        this.logger = (LogService)ciShellContext.getService(LogService.class.getName());
        this.databaseProvider =
        	(DatabaseService)ciShellContext.getService(DatabaseService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		this.progressMonitor.describeWork("Preprocessing ISI data.");

	    	// Convert input ISI data to an ISI table.

    		Table isiTable = convertISIToTable(this.inData, this.logger);

    		// Preprocess the ISI table: generate unique IDs for rows without them.

    		ISITablePreprocessor.generateMissingUniqueIDs(isiTable);

	    	// Preprocess the ISI table: remove duplicate Documents (on the row level).

    		Collection<Integer> rows =
    			ISITablePreprocessor.removeRowsWithDuplicateDocuments(isiTable);

    		/*
    		 * We're going to say that creating the model is 10% of the total work for this
    		 *  algorithm, so multiply the number of rows by 10 to get the total number of work
    		 *  units for the progress monitor.
    		 */

    		int rowCount = rows.size();
    		int totalWorkUnitCount =
    			Math.round(rowCount / PERCENTAGE_OF_PROGRESS_FOR_CREATING_MODEL);
    		this.progressMonitor.start(
    			(ProgressMonitor.WORK_TRACKABLE |
    				ProgressMonitor.CANCELLABLE |
    				ProgressMonitor.PAUSEABLE),
    			totalWorkUnitCount);

    		// Convert the ISI table to an ISI database.

    		Database database = convertTableToDatabase(isiTable, rows, totalWorkUnitCount);
    		this.progressMonitor.done();

	    	// Annotate ISI database as output data with metadata and return it.

    	    return annotateOutputData(database, this.inData);
    	} catch (AlgorithmCanceledException e) {
    		return new Data[] {};
    	} catch (ISILoadingException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }

    public void setProgressMonitor(ProgressMonitor progressMonitor) {
    	this.progressMonitor = progressMonitor;
    }

    public ProgressMonitor getProgressMonitor() {
    	return this.progressMonitor;
    }
    
    private Table convertISIToTable(Data isiData, LogService logger)
    		throws AlgorithmExecutionException {
    	File inISIFile = (File)isiData.getData();

    	try {
    		return ISITableReaderHelper.readISIFile(
    			inISIFile,
    			logger,
    			SHOULD_NORMALIZE_AUTHOR_NAMES,
    			SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS,
    			SHOULD_FILL_FILE_METADATA,
    			SHOULD_CLEAN_CITED_REFERENCES);
    	} catch (ReadISIFileException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }

    private Database convertTableToDatabase(
    		Table table, Collection<Integer> rows, int workUnitCount)
    		throws AlgorithmCanceledException, ISILoadingException {
    	try {
	    	// Create an in-memory ISI model based off of the table.

    		DatabaseModel model =
    			new ISITableModelParser(this.progressMonitor).parseModel(table, rows);

	    	// Use the ISI model to create an ISI database.

    		return DerbyDatabaseCreator.createFromModel(
    			this.databaseProvider, model, "ISI", this.progressMonitor, workUnitCount);
    	} catch (DatabaseCreationException e) {
    		throw new ISILoadingException(e.getMessage(), e);
    	} catch (SQLException e) {
    		throw new ISILoadingException(e.getMessage(), e);
    	}
    }

    private Data[] annotateOutputData(Database isiDatabase, Data parentData) {
    	Data data = new BasicData(isiDatabase, ISI.ISI_DATABASE_MIME_TYPE);
    	Dictionary<String, Object> parentMetadata = parentData.getMetadata();
    	Dictionary<String, Object> metadata = data.getMetadata();
    	metadata.put(
    		DataProperty.LABEL, "ISI Database From " + parentMetadata.get(DataProperty.LABEL));
    	metadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);
    	metadata.put(DataProperty.PARENT, parentData);

    	//return null;
    	return new Data[] { data };
    }
}