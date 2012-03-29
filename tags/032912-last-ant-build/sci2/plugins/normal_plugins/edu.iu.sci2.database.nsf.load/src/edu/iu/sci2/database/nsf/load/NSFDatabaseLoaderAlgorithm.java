package edu.iu.sci2.database.nsf.load;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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

import au.com.bytecode.opencsv.CSVReader;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DerbyDatabaseCreator;
import edu.iu.sci2.database.nsf.load.exception.NSFDatabaseCreationException;
import edu.iu.sci2.database.nsf.load.exception.NSFReadingException;
import edu.iu.sci2.database.nsf.load.utilities.CSVReaderUtilities;
import edu.iu.sci2.database.nsf.load.utilities.NSFMetadata;
import edu.iu.sci2.database.nsf.load.utilities.NSFTableModelParser;
import edu.iu.sci2.utilities.nsf.NsfDatabaseFieldNames;

/**
 * @author cdtank
 *
 */

public class NSFDatabaseLoaderAlgorithm implements Algorithm, ProgressTrackable {
	
	private Data[] data;
	private LogService logger;
	private DatabaseService databaseProvider;
	private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;
	
    public NSFDatabaseLoaderAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        this.data = data;
		this.logger = (LogService) context.getService(LogService.class.getName());
        this.databaseProvider =
        	(DatabaseService) context.getService(DatabaseService.class.getName());
	}

    public Data[] execute() throws AlgorithmExecutionException {
    	/*
    	 * Get the file data.
    	 */

    	String fileName = (String)data[0].getData();
		File nsfCSVFile = new File(fileName);
		
		try {
			// Setup the progress monitor.

			int rowCount = CSVReaderUtilities.rowCount(nsfCSVFile, true);
    		double totalWork = calculateTotalWork(rowCount);
    		startProgressMonitor(progressMonitor, totalWork);

			/*
			 * Create in-memory nsf model with all its entities & relationships.
			 */
			DatabaseModel inMemoryModel = createInMemoryNSFModel(nsfCSVFile, logger);
			
			/*
			 * Extract the actual database from the in-memory model. 
			 */
			Database database = convertNSFInMemoryModelToDatabase(inMemoryModel, totalWork);

			// Notify the progress monitor that we're done.

			stopProgressMonitor(progressMonitor);

			/*
			 * Provide the finished database in the data manager.
			 */
			return annotateOutputData(database, data[0]);
		} catch (AlgorithmCanceledException e) {
			return new Data[] {};
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (NSFReadingException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (NSFDatabaseCreationException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} 
    }

    public void setProgressMonitor(ProgressMonitor progressMonitor) {
    	this.progressMonitor = progressMonitor;
    }

    public ProgressMonitor getProgressMonitor() {
    	return this.progressMonitor;
    }

	private Data[] annotateOutputData(Database database, Data parentData) {
		Data output = new BasicData(database, NsfDatabaseFieldNames.NSF_DATABASE_MIME_TYPE);
		Dictionary<String, Object> metadata = output.getMetadata();
		metadata.put(
			DataProperty.LABEL,
			"NSF Database From " +
				FileUtilities.extractFileNameWithExtension((String)parentData.getData()));
		metadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);
		metadata.put(DataProperty.PARENT, data[0]);
		
		return new Data[] { output };
	}

	/**
	 * @param nsfCSVFile
	 * @param nsfCSVReader
	 * @param logger 
	 * @return
	 * @throws AlgorithmExecutionException
	 * @throws IOException
	 */
	private DatabaseModel createInMemoryNSFModel(File nsfCSVFile, LogService logger)
			throws AlgorithmCanceledException, IOException, NSFReadingException {
		
		String[] nsfFileColumnNames = getNSFFileColumnNames(nsfCSVFile);
		CSVReader nsfCSVReader = CSVReaderUtilities.createCSVReaderWithRightSeparator(nsfCSVFile);
		NSFMetadata nsfMetadata = new NSFMetadata(nsfFileColumnNames, nsfCSVFile, nsfCSVReader);

		try {
			nsfCSVReader = CSVReaderUtilities.createCSVReaderWithRightSeparator(nsfCSVFile, true);
			DatabaseModel model = new NSFTableModelParser().parseModel(
				nsfCSVReader, nsfMetadata, logger, this.progressMonitor);

			return model;
		} catch (IOException e) {
			System.out.println("in 8");
			throw new NSFReadingException(e.getMessage(), e);
		}
	}

	/**
	 * @param inMemoryModel
	 * @return
	 * @throws AlgorithmExecutionException 
	 * @throws DatabaseCreationException
	 * @throws SQLException
	 */
	private Database convertNSFInMemoryModelToDatabase(
			DatabaseModel inMemoryModel, double totalWork) throws NSFDatabaseCreationException {
		try {
			return DerbyDatabaseCreator.createFromModel(
				databaseProvider, inMemoryModel, "NSF", this.progressMonitor, totalWork);
		} catch (AlgorithmCanceledException e) {
			throw new NSFDatabaseCreationException(e.getMessage(), e);
		} catch (DatabaseCreationException e) {
			throw new NSFDatabaseCreationException(e.getMessage(), e); 
		} catch (SQLException e) {
			throw new NSFDatabaseCreationException(e.getMessage(), e); 
		}
	}

	private static double calculateTotalWork(int rowCount) {
    	double workUnitCount =
			(double) rowCount / DerbyDatabaseCreator.PERCENTAGE_OF_PROGRESS_FOR_MODEL_CREATION;

    	return workUnitCount;
    }

    private static void startProgressMonitor(ProgressMonitor progressMonitor, double totalWork) {
		progressMonitor.start(
			(ProgressMonitor.WORK_TRACKABLE |
				ProgressMonitor.CANCELLABLE |
				ProgressMonitor.PAUSEABLE),
			totalWork);
		progressMonitor.describeWork("Loading Generic-CSV data into a database.");
    }

    private static void stopProgressMonitor(ProgressMonitor progressMonitor) {
    	progressMonitor.done();
    }
	
	private static String[] getNSFFileColumnNames(File nsfCSVFile) 
			throws NSFReadingException {
		try {
			String[] columnNames = CSVReaderUtilities.getHeader(nsfCSVFile);
			
			if (columnNames == null || columnNames.length == 0) {
				throw new NSFReadingException("Cannot read in an empty nsf file");
			}
			
			return columnNames;
			
		} catch (IOException e) {
			throw new NSFReadingException(e.getMessage(), e);
		}
	}
}