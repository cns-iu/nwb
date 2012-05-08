package edu.iu.sci2.database.medline.converter.medline_table_to_isi_db;

import java.sql.SQLException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.DataFactory;
import org.osgi.service.log.LogService;

import com.google.common.base.Preconditions;

import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DerbyDatabaseCreator;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.medline.common.MedlineTable;

/**
 * A converter for converting from a {@linkplain MedlineTable} to an
 * {@code isi:db}.
 * 
 */
public class Converter implements Algorithm, ProgressTrackable {
	public static class Factory implements AlgorithmFactory {

		@Override
		public Algorithm createAlgorithm(Data[] data,
				Dictionary<String, Object> parameters,
				CIShellContext ciShellContext) {
			try {
				MedlineTable medlineTable = (MedlineTable) data[0].getData();
				return new Converter(medlineTable, data[0], ciShellContext);
			} catch (ClassCastException e) {
				throw new AlgorithmCreationFailedException(
						"The data is in the wrong format.", e);
			}
		}

	}

	MedlineTable table;
	private Data parent;
	private DatabaseService databaseProvider;
	private LogService logger;

	private ProgressMonitor progressMonitor = ProgressMonitor.NULL_MONITOR;

	public Converter(MedlineTable table, Data parent,
			CIShellContext ciShellContext) {
		Preconditions.checkNotNull(table);
		Preconditions.checkNotNull(ciShellContext);

		this.table = table;
		this.parent = parent;
		this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());
		this.databaseProvider = (DatabaseService) ciShellContext
				.getService(DatabaseService.class.getName());
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		if (this.databaseProvider == null) {
			throw new AlgorithmExecutionException(
					"A database provider could not be located!");
		}

		try {
			Database database = getDatabase(this.table, this.databaseProvider,
					this.logger, this.getProgressMonitor());

			return createOutputData(database, this.parent);

		} catch (AlgorithmCanceledException e) {
			throw e;
		} catch (DatabaseCreationException e) {
			throw new AlgorithmExecutionException(
					"The medline database couldn't be created.", e);
		} catch (SQLException e) {
			throw new AlgorithmExecutionException(
					"The medline database couldn't be created.", e);
		}
	}

	private static Data[] createOutputData(Database database, Data parent) {
		return new Data[] { DataFactory.forObject(database,
				ISI.ISI_DATABASE_MIME_TYPE, DataProperty.DATABASE_TYPE, parent,
				"Medline Database for " + parent.getData().toString()) };
	}

	private static Database getDatabase(MedlineTable medlineTable,
			DatabaseService databaseProvider, LogService logger,
			ProgressMonitor progressMonitor) throws AlgorithmCanceledException,
			DatabaseCreationException, SQLException {
		progressMonitor.start(ProgressMonitor.WORK_TRACKABLE, 2);
		progressMonitor.describeWork("Making the table model.");
		DatabaseModel model = new MedlineTableTableModelParser(medlineTable,
				logger).getModel();
		progressMonitor.worked(1);

		progressMonitor.start(ProgressMonitor.WORK_TRACKABLE, model
				.getRowItemLists().size() + 1);

		progressMonitor.describeWork("Creating the database from the model.");
		Database database = DerbyDatabaseCreator.createFromModel(
				databaseProvider, model, "medline", progressMonitor, model
						.getRowItemLists().size());
		progressMonitor.worked(1);

		progressMonitor.done();
		return database;
	}

	@Override
	public void setProgressMonitor(ProgressMonitor monitor) {
		Preconditions.checkNotNull(monitor);
		this.progressMonitor = monitor;
	}

	@Override
	public ProgressMonitor getProgressMonitor() {
		return this.progressMonitor;
	}
}
