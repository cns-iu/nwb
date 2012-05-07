package edu.iu.sci2.database.isi.load;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.DataFactory;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.util.collections.IntIterator;
import edu.iu.nwb.shared.isiutil.ISITableReaderHelper;
import edu.iu.nwb.shared.isiutil.exception.ISILoadingException;
import edu.iu.nwb.shared.isiutil.exception.ReadISIFileException;
import edu.iu.sci2.database.isi.load.utilities.ISITablePreprocessor;

public class ISIDatabaseLoaderAlgorithm implements Algorithm, ProgressTrackable {
	public static final boolean SHOULD_NORMALIZE_AUTHOR_NAMES = true;
	public static final boolean SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS = true;
	public static final boolean SHOULD_FILL_FILE_METADATA = true;
	public static final boolean SHOULD_CLEAN_CITED_REFERENCES = false;

	private Data inData;
	private LogService logger;
	private DatabaseService databaseProvider;
	private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;
	private CIShellContext ciShellContext;
	private AlgorithmFactory mergeIdenticalRecords;

	public ISIDatabaseLoaderAlgorithm(Data[] data,
			CIShellContext ciShellContext,
			AlgorithmFactory mergeIdenticalRecords) {
		this.inData = data[0];
		this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());
		this.databaseProvider = (DatabaseService) ciShellContext
				.getService(DatabaseService.class.getName());
		this.ciShellContext = ciShellContext;
		this.mergeIdenticalRecords = mergeIdenticalRecords;
	}

	private static Collection<Integer> getAllRows(Table table) {
		Collection<Integer> rows = new ArrayList<Integer>();
		IntIterator rowIttr = table.rows();

		while (rowIttr.hasNext()) {
			rows.add(rowIttr.nextInt());
		}

		return rows;
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			/* Convert input ISI data to an ISI table. */
			Table isiTable = convertISIToTable(this.inData, this.logger);

			/* Convert the original data to a database */
			Database originalDatabase = ISITableDatabaseLoader.createISIDatabase(
					isiTable, getAllRows(isiTable), this.progressMonitor,
					this.databaseProvider);
			Data unprocessedData = DataFactory.withClassNameAsFormat(originalDatabase, DataProperty.DATABASE_TYPE, this.inData, "ISI Data: " + this.inData.getData());
			
			List<Data> returnData = new ArrayList<Data>();
			returnData.addAll(Arrays.asList(unprocessedData));

			/*
			 * Preprocess the ISI table: generate unique IDs for rows without
			 * them.
			 */
			ISITablePreprocessor.generateMissingUniqueIDs(isiTable);

			/* Table with Unique ISI Records Merged */
			Data[] uniqueRecordsData =  getUniqueRecordsOutputData(isiTable, unprocessedData);
			returnData.addAll(Arrays.asList(uniqueRecordsData));

			return returnData.toArray(new Data[0]);
		} catch (AlgorithmCanceledException e) {
			return new Data[] {};
		} catch (ISILoadingException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	private Data[] getUniqueRecordsOutputData(Table isiTable, Data parent) {
		try {
			/** Merge using the ISI Unique Records algorithm **/
			
			/* Pack the table for the algorithm */
			Data isiTableData = DataFactory.withClassNameAsFormat(
					isiTable,
					DataProperty.TABLE_TYPE,
					this.inData,
					String.valueOf(this.inData.getMetadata().get(
							DataProperty.LABEL)));
			
			/* Run the algorithm on the packed table */
			Data[] uniqueISIRecords = this.mergeIdenticalRecords
					.createAlgorithm(new Data[] { isiTableData },
							new Hashtable<String, Object>(),
							this.ciShellContext).execute();
			
			/* Pull the Unique Records table out of the algorithm's Data[] */
			Table uniqueISITable = (Table) uniqueISIRecords[0].getData();
			
			/* Create the database from the processed table */
			Database uniqueISIDatabase = ISITableDatabaseLoader
					.createISIDatabase(uniqueISITable,
							getAllRows(uniqueISITable), this.progressMonitor,
							this.databaseProvider);

			/* Pack the data as output data using the provided parent */
			String label = (String) uniqueISIRecords[0].getMetadata().get(
					DataProperty.LABEL);
			Data data = DataFactory.withClassNameAsFormat(uniqueISIDatabase,
					DataProperty.DATABASE_TYPE, parent, label);
			return new Data[] { data };
		} catch (AlgorithmExecutionException e) {
			String message = "It was not possible to automatically merge Unique ISI records."
					+ System.getProperty("line.separator")
					+ e.getLocalizedMessage();
			this.logger.log(LogService.LOG_ERROR, message);
			return new Data[] {};
		} catch (ISILoadingException e) {
			String message = "It was not possible to automatically merge Unique ISI records."
					+ System.getProperty("line.separator")
					+ e.getLocalizedMessage();
			this.logger.log(LogService.LOG_ERROR, message);
			return new Data[] {};
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

	private static Table convertISIToTable(Data isiData, LogService logger)
			throws AlgorithmExecutionException {
		String filePath = (String) isiData.getData();
		File inISIFile = new File(filePath);

		try {
			return ISITableReaderHelper.readISIFile(inISIFile, logger,
					SHOULD_NORMALIZE_AUTHOR_NAMES,
					SHOULD_CLEAN_AUTHOR_NAME_CAPITALIZATIONS,
					SHOULD_FILL_FILE_METADATA, SHOULD_CLEAN_CITED_REFERENCES);
		} catch (ReadISIFileException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	/*
	 * SOMEDAY if you find use for this, put it in a utility. For now there is
	 * no good place. It is also used in {@link CSVDatabaseLoader}.
	 */

	/**
	 * Sets the {@code child}'s parent {@link Data} element to be {@code parent}
	 * .
	 * 
	 * @param child
	 *            The {@link Data} to have it's parent changed.
	 * @param parent
	 *            The {@link Data} to set as the parent of the {@code Child}.
	 * @return The {@code Object} as returned by {@link Dictionary}'s
	 *         {@code put} method.
	 */
	public static Object changeParent(Data child, Data parent) {
		Dictionary<String, Object> metadata = child.getMetadata();
		return metadata.put(DataProperty.PARENT, parent);
	}
}