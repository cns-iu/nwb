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
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.nwb.shared.isiutil.ISITableReaderHelper;
import edu.iu.nwb.shared.isiutil.database.ISI;
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
	private AlgorithmFactory mergeIdentical;
	

	public ISIDatabaseLoaderAlgorithm(Data[] data,
			CIShellContext ciShellContext, AlgorithmFactory mergeIdentical) {
		this.inData = data[0];

		this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());
		this.databaseProvider = (DatabaseService) ciShellContext
				.getService(DatabaseService.class.getName());
		this.ciShellContext = ciShellContext;
		this.mergeIdentical = mergeIdentical;
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			// Convert input ISI data to an ISI table.
			
			Table isiTable = convertISIToTable(this.inData, this.logger);

			// Preprocess the ISI table: generate unique IDs for rows without
			// them.

			ISITablePreprocessor.generateMissingUniqueIDs(isiTable);

			// Preprocess the ISI table: remove duplicate Documents (on the row
			// level).

			Collection<Integer> rows = ISITablePreprocessor
					.removeRowsWithDuplicateDocuments(isiTable);

			// Convert the ISI table to an ISI database.

			Database database = ISITableDatabaseLoader
					.createISIDatabase(isiTable, rows, this.progressMonitor,
							this.databaseProvider);

			// Annotate ISI database as output data with metadata and return it.
			Data[] annotatedOutputData = annotateOutputData(database,
					this.inData);

			List<Data> returnData = new ArrayList<Data>();
			returnData.addAll(Arrays.asList(annotatedOutputData));
			
			// Automatically merge identical documents and add them to the return data.
			Data[] mergedData = getMergedData(annotatedOutputData[0]);
			returnData.addAll(Arrays.asList(mergedData));
			
			return returnData.toArray(new Data[0]);
		} catch (AlgorithmCanceledException e) {
			return new Data[] {};
		} catch (ISILoadingException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}


	/**
	 * Given the {@code loadedDatabase}, the {@link Database} wrapped as
	 * {@link Data}, it will run the Merge Identical Documents algorithm and
	 * return it's {@link Data} array.
	 * 
	 * @param loadedDatabase
	 *            The {@link Database} wrapped as {@link Data}. It will also be
	 *            used as the parent for the returned {@link Data}.
	 * @return The {@link Data} array returned by the merging algorithm.
	 * @throws AlgorithmExecutionException
	 */
	private Data[] getMergedData(Data loadedDatabase)
			throws AlgorithmExecutionException {
		try {
			Data[] mergedData = AlgorithmUtilities.executeAlgorithm(
					this.mergeIdentical, 
					getProgressMonitor(),
					new Data[] { loadedDatabase },
					new Hashtable<String, Object>(), 
					this.ciShellContext);

			for (Data data : mergedData) {
				changeParent(data, loadedDatabase);
			}
			return mergedData;
		} catch (AlgorithmExecutionException e) {
			throw new AlgorithmExecutionException(
					"It was not possible to automatically merge the identical documents for this database."
							+ System.getProperty("line.separator")
							+ e.getMessage(), e);
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

	private static Data[] annotateOutputData(Database isiDatabase, Data parentData) {
		Data data = new BasicData(isiDatabase, ISI.ISI_DATABASE_MIME_TYPE);
		Dictionary<String, Object> metadata = data.getMetadata();
		metadata.put(
				DataProperty.LABEL,
				"ISI Database From "
						+ FileUtilities
								.extractFileNameWithExtension((String) parentData
										.getData()));
		metadata.put(DataProperty.TYPE, DataProperty.DATABASE_TYPE);
		metadata.put(DataProperty.PARENT, parentData);

		return new Data[] { data };
	}
	
	/*
	 *  SOMEDAY if you find use for this, put it in a utility.  For now there is
	 *  no good place.  It is also used in {@link CSVDatabaseLoader}.
	 */
	
	/**
	 * Sets the {@code child}'s parent {@link Data} element to be {@code parent}.
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