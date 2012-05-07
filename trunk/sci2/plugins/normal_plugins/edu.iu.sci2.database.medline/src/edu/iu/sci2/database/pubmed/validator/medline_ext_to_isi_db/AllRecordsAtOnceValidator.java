package edu.iu.sci2.database.pubmed.validator.medline_ext_to_isi_db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.DataFactory;
import org.cishell.utilities.UnicodeReader;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DerbyDatabaseCreator;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.medline.common.MedlineRecord;
import edu.iu.sci2.medline.common.MedlineRecordParser;

/**
 * This 'validates' a pubmed file to an isi:db by reading the records all at
 * once. This can have serious memory consequences. It uses the
 * {@code edu.iu.sci2.medline.validator.Validator}
 * for validation.
 * 
 * <p>
 * <b> Warning!! </b>
 * </p>
 * <p>
 * It breaks the contract for a validator by actually converting the data to
 * another format.
 * </p>
 * 
 */
public class AllRecordsAtOnceValidator implements Algorithm {

	public static class Factory implements AlgorithmFactory {
		private BundleContext bundleContext;
		private static final String validatorPID = "edu.iu.sci2.medline.validator.Validator";

		@Override
		public Algorithm createAlgorithm(Data[] data,
				Dictionary<String, Object> parameters,
				CIShellContext ciShellContext) {
			Preconditions.checkNotNull(this.bundleContext,
					"This bundle was not activated correctly.");
			Preconditions.checkArgument(data != null,
					"The data must not be null.");
			Preconditions
					.checkArgument(parameters.isEmpty(),
							"The CIShell 1.0 Spec guarantees that parameters will be null for a validator.");
			return new AllRecordsAtOnceValidator(ciShellContext, getValidator(
					this.bundleContext).createAlgorithm(data, parameters,
					ciShellContext));
		}

		protected void activate(ComponentContext componentContext) {
			this.bundleContext = componentContext.getBundleContext();
		}

		private static AlgorithmFactory getValidator(BundleContext bundleContext) {
			return AlgorithmUtilities.getAlgorithmFactoryByPID(validatorPID,
					bundleContext);
		}

	}

	private Algorithm validator;
	private LogService logger;
	private DatabaseService databaseProvider;

	public AllRecordsAtOnceValidator(CIShellContext ciShellContext,
			Algorithm validator) {
		Preconditions.checkArgument(validator != null,
				"The validation algorithm must not be null.");
		Preconditions.checkArgument(ciShellContext != null,
				"The CIShell Context must not be null.");

		this.validator = validator;
		this.databaseProvider = (DatabaseService) ciShellContext
				.getService(DatabaseService.class.getName());
		this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			Data[] validMedlineFileData = this.validator.execute();
			Data rootData = getData(validMedlineFileData);
			File validMedlineFile = getFile(rootData);
			ImmutableList<MedlineRecord> records = getRecords(validMedlineFile);
			DatabaseModel model = new PubmedRecordsTableModelParser(records,
					this.logger).getModel();
			Database database = getDatabase(model);
			return createOutputData(database, rootData);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private File getFile(Data validMedlineFileData)
			throws AlgorithmExecutionException {
		Preconditions.checkArgument(validMedlineFileData != null,
				"The data must not be null.");
		try {
			return (File) validMedlineFileData.getData();
		} catch (ClassCastException e) {
			String message = "The data returned from validation was not a File as expected.";
			this.logger.log(LogService.LOG_ERROR, message);
			throw new AlgorithmExecutionException(message, e);
		}
	}

	private Data getData(Data[] validMedlineFileData)
			throws AlgorithmExecutionException {
		Preconditions.checkArgument(validMedlineFileData != null,
				"The data must not be null.");
		try {
			return validMedlineFileData[0];
		} catch (IndexOutOfBoundsException e) {
			String message = "The validation failed.";
			this.logger.log(LogService.LOG_ERROR, message);
			throw new AlgorithmExecutionException(message, e);
		}
	}

	private ImmutableList<MedlineRecord> getRecords(File validMedlineFile)
			throws AlgorithmExecutionException {
		Preconditions.checkArgument(validMedlineFile != null,
				"The file must not be null.");
		try {
			return MedlineRecordParser.getAllRecords(new BufferedReader(
					new UnicodeReader(new FileInputStream(validMedlineFile))), this.logger);

		} catch (FileNotFoundException e) {
			String message = "File could not be found.";
			this.logger.log(LogService.LOG_ERROR, message);
			throw new AlgorithmExecutionException(message, e);
		}
	}

	private Database getDatabase(DatabaseModel model)
			throws AlgorithmExecutionException {
		Preconditions.checkArgument(model != null,
				"The model must not be null to create a database.");
		try {
			Database database = DerbyDatabaseCreator.createFromModel(
					this.databaseProvider, model, "pubmed",
					ProgressMonitor.NULL_MONITOR, model.getRowItemLists()
							.size());

			if (database == null) {
				String message = "The database returned was null.";
				this.logger.log(LogService.LOG_ERROR, message);
				throw new AlgorithmExecutionException(message);
			}

			return database;
		} catch (AlgorithmCanceledException e) {
			throw e;
		} catch (DatabaseCreationException e) {
			String message = "The database could not be created.";
			this.logger.log(LogService.LOG_ERROR, message);
			throw new AlgorithmExecutionException(message, e);
		} catch (SQLException e) {
			String message = "There was a SQL problem when creating the database.";
			this.logger.log(LogService.LOG_ERROR, message);
			throw new AlgorithmExecutionException(message, e);
		}
	}

	private static Data[] createOutputData(Database database, Data parent) {
		Preconditions.checkArgument(database != null,
				"The database must not be null.");
		Preconditions.checkArgument(parent != null,
				"The parent data object must not be null.");
		return new Data[] { DataFactory.forObject(database,
				ISI.ISI_DATABASE_MIME_TYPE, DataProperty.DATABASE_TYPE, parent,
				"Pubmed Database for " + parent.getData().toString()) };
	}

}
