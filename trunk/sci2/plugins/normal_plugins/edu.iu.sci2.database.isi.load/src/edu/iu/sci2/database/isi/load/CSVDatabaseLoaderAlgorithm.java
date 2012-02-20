/**
 * 
 */
package edu.iu.sci2.database.isi.load;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.AlgorithmNotFoundException;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.DataFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.util.collections.IntIterator;
import edu.iu.nwb.shared.isiutil.ISITag;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.nwb.shared.isiutil.exception.ISILoadingException;

/**
 * @author dmcoe
 * 
 *         This class will convert a file-ext:csv to a db:isi. It must go from
 *         the extension so it will not appear as a converter and break the
 *         converter graph since it only actually works on tables that came from
 *         an ISI file.
 * 
 *         SOMEDAY create a class of prefuse table that this could actually
 *         convert to a database.
 * 
 * 
 *         FIXME XXX TODO BUG HACK All of the evil I am about to do with
 *         specific algorithms is because of the conversion service
 *         implementation.
 * 
 *         It should be changed in the following ways: 1) It should include
 *         validators as a first step, as it does for a final step, when
 *         searching for a conversion graph. 2) It should return errors if it
 *         cannot find a conversion path. 3) If you pass it the same datatype
 *         you'd like to convert to, maybe it should throw an error. I'd be ok
 *         without changing 3, but 1+2 are musts!
 * 
 * 
 */
public class CSVDatabaseLoaderAlgorithm implements Algorithm {

	private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;
	private CIShellContext ciShellContext;
	private DatabaseService databaseProvider;
	private Data[] inData;
	private AlgorithmFactory mergeFactory;
	private AlgorithmFactory convertFactory;
	private AlgorithmFactory validateFactory;
	private LogService logger;

	public static class Factory implements AlgorithmFactory {
		public static final String MERGE_FACTORY_PID = 
				"edu.iu.sci2.database.isi.merge.document_source.MergeDocumentSourcesAlgorithm";
		public static final String VALIDATE_FACTORY_PID = 
				"edu.iu.nwb.converter.prefusecsv.validator.PrefuseCsvValidator$Factory";
		public static final String CONVERT_FACTORY_PID = 
				"edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader$Factory";
		
		private BundleContext bundleContext;

		public Algorithm createAlgorithm(Data[] data,
				Dictionary<String, Object> parameters,
				CIShellContext ciShellContext) {

			try {
				AlgorithmFactory mergeFactory = AlgorithmUtilities
						.getAlgorithmFactoryByPID(
								MERGE_FACTORY_PID,
								this.bundleContext);

				AlgorithmFactory validateFactory = AlgorithmUtilities
						.getAlgorithmFactoryByPID(
								VALIDATE_FACTORY_PID,
								this.bundleContext);

				AlgorithmFactory convertFactory = AlgorithmUtilities
						.getAlgorithmFactoryByPID(
								CONVERT_FACTORY_PID,
								this.bundleContext);
				return new CSVDatabaseLoaderAlgorithm(data, ciShellContext,
						mergeFactory, validateFactory, convertFactory);
			} catch (AlgorithmNotFoundException e) {
				throw new AlgorithmCreationFailedException(
						"One of the required algorithm factories was missing; "
								+ "the data cannot be loaded as an ISI Database."
								+ System.getProperty("line.separator"), e);
			}

		}

		protected void activate(ComponentContext componentContext) {
			this.bundleContext = componentContext.getBundleContext();
		}
	}
	
	public CSVDatabaseLoaderAlgorithm(Data[] csvFile,
			CIShellContext ciShellContext,
			AlgorithmFactory mergeFactory,
			AlgorithmFactory validateFactory,
			AlgorithmFactory convertFactory) {
		
		this.inData = csvFile;
		this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
		this.databaseProvider = (DatabaseService) ciShellContext
				.getService(DatabaseService.class.getName());
		this.ciShellContext = ciShellContext;
		this.mergeFactory = mergeFactory;
		this.validateFactory = validateFactory;
		this.convertFactory = convertFactory;

	}

	private Data[] validate(Data[] data,
			Hashtable<String, Object> emptyDictionary)
			throws AlgorithmExecutionException {
		Algorithm validator = this.validateFactory.createAlgorithm(data,
				emptyDictionary, this.ciShellContext);
		try {
			Data[] validatedData = validator.execute();
			if (validatedData.length < 1) {
				throw new AlgorithmExecutionException(
						"The validator returned an empty data[]!");
			}
			if (!(validatedData[0].getData() instanceof File)) {
				this.logger.log(LogService.LOG_WARNING,
						"The format of the validated data was not what was expected.  "
								+ "The algorithm factory with  pid='"
								+ Factory.VALIDATE_FACTORY_PID
								+ "' might have changed.");
			}
			return validatedData;
		} catch (AlgorithmExecutionException e) {
			throw new AlgorithmExecutionException(
					"Validation of the csv file failed." + e.getMessage(), e);
		}
	}

	private Table convert(Data[] validatedData,
			Hashtable<String, Object> emptyDictionary)
			throws AlgorithmExecutionException {
		Algorithm converter = this.convertFactory.createAlgorithm(
				validatedData, emptyDictionary, this.ciShellContext);
		try {
			Data[] convertedData = converter.execute();
			if (convertedData.length < 1) {
				throw new AlgorithmExecutionException(
						"The converted returned an empty data[]!");
			}
			Object table = convertedData[0].getData();
			if (!(table instanceof Table)) {
				throw new AlgorithmExecutionException(
						"The data converter was supposed to return type '"
								+ Table.class.getName()
								+ "' but returned type '"
								+ table.getClass().getName() + "'.");
			}
			System.out.println(convertedData[0].getData().getClass().getName());
			return (Table) convertedData[0].getData();
		} catch (AlgorithmExecutionException e) {
			throw new AlgorithmExecutionException(
					"Conversion of the csv file to a prefuse table failed."
							+ e.getMessage(), e);
		}
	}
	
	public Data[] execute() throws AlgorithmExecutionException {
		
		Hashtable<String, Object> emptyDictionary = new Hashtable<String, Object>();

		Data[] validatedData = validate(this.inData, emptyDictionary);
		Table prefuseTable = convert(validatedData, emptyDictionary);

		/*
		 * Check to see if this csv file really is an 'ISI Csv' file. If not,
		 * continue but warn the user that it doesn't look like the right
		 * format.
		 */
		
		List<ISITag> missingTags = missingISITags(prefuseTable);
		
		if (missingTags.size() > 0) {
			this.logger.log(LogService.LOG_WARNING,
					"The loaded CSV File might not be an ISI file.  There were "
							+ missingTags.size() + " missing tags.");
			int debugThreshold = 5;
			if (missingTags.size() < debugThreshold) {
				for (ISITag tag : missingTags) {
					this.logger.log(LogService.LOG_DEBUG, tag.columnName);
				}
			}
		}
		
		Table isiTable = prefuseTable;

		try {
			
			Collection<Integer> rowNumbers = new ArrayList<Integer>(
					isiTable.getRowCount());

			for (IntIterator rows = isiTable.rows(); rows.hasNext();) {
				rowNumbers.add(Integer.valueOf(rows.nextInt()));
			}

			Database database = ISITableDatabaseLoader.createISIDatabase(
					isiTable, rowNumbers, this.progressMonitor,
					this.databaseProvider);

			// Annotate ISI database as output data with metadata and return it.
			Data[] annotatedOutputData = annotateOutputData(database,
					this.inData[0]);

			List<Data> returnData = new ArrayList<Data>();
			returnData.addAll(Arrays.asList(annotatedOutputData));

			// Automatically merge identical documents and add them to the
			// return data.
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
	 * This will use {@link ISITag} to see how many columns are missing. If any
	 * tags are missing, it will return them otherwise an empty list will be
	 * returned.
	 * 
	 * @param prefuseTable
	 *            The table examine to see if it has all of the columns listed
	 *            in {@link ISITag}.
	 * @return An empty list if all {@link ISITag} columns were in this table, a
	 *         list of the missing {@link ISITag}s otherwise.
	 */
	private static List<ISITag> missingISITags(Table prefuseTable) {
		List<ISITag> missingTags = new ArrayList<ISITag>();

		ISITag[] tags = ISITag.getTagsAlphabetically();
		for (ISITag tag : tags) {
			String columnName = tag.columnName;
			Class<?> clazz = tag.type.getTableDataType();
			if (!(prefuseTable.canGet(columnName, clazz))) {
				missingTags.add(tag);
			}
		}

		return missingTags;

	}

	public void setProgressMonitor(ProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	public ProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
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
					this.mergeFactory, getProgressMonitor(),
					new Data[] { loadedDatabase },
					new Hashtable<String, Object>(), this.ciShellContext);

			for (Data data : mergedData) {
				changeParent(data, loadedDatabase);
			}
			return mergedData;
		} catch (AlgorithmExecutionException e) {
			throw new AlgorithmExecutionException(
					"It was not possible to automatically merge "
							+ "the identical documents for this database."
							+ System.getProperty("line.separator")
							+ e.getMessage(), e);
		}
	}

	/*
	 * SOMEDAY if you find use for this, put it in a utility. For now there is
	 * no good place. It is also used in {@link ISIDatabaseLoaderAlgorithm}.
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

	private static Data[] annotateOutputData(Database database, Data parent) {
		String label = "ISI Database from "
				+ FilenameUtils.getName((String) parent.getData());
		Data data = DataFactory.forObject(database, ISI.ISI_DATABASE_MIME_TYPE,
				DataProperty.DATABASE_TYPE, parent, label);
		return new Data[] { data };
	}

}
