package edu.iu.sci2.database.isi.load;

import java.sql.SQLException;
import java.util.Collection;

import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;

import prefuse.data.Table;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DerbyDatabaseCreator;
import edu.iu.nwb.shared.isiutil.exception.ISILoadingException;
import edu.iu.sci2.database.isi.load.utilities.parser.ISITableModelParser;

public final class ISITableDatabaseLoader {

	private ISITableDatabaseLoader() {
	}

	// TODO rewrite this so it does not need to be given the rows.
	public static Database createISIDatabase(Table isiTable,
			Collection<Integer> rows, ProgressMonitor progressMonitor,
			DatabaseService databaseProvider)
			throws ISILoadingException {
		try {
			double totalWork = calculateTotalWork(rows);
			startProgressMonitor(totalWork, progressMonitor);

			// Create an in-memory ISI model based off of the table.

			DatabaseModel model = new ISITableModelParser(progressMonitor)
					.parseModel(isiTable, rows);

			// Use the ISI model to create an ISI database.

			Database database = DerbyDatabaseCreator.createFromModel(
					databaseProvider, model, "ISI", progressMonitor, totalWork);

			stopProgressMonitor(progressMonitor);

			return database;
		} catch (DatabaseCreationException e) {
			throw new ISILoadingException(e.getMessage(), e);
		} catch (SQLException e) {
			throw new ISILoadingException(e.getMessage(), e);
		}
	}

	private static void stopProgressMonitor(ProgressMonitor progressMonitor) {
		progressMonitor.done();

	}

	private static void startProgressMonitor(double totalWork,
			ProgressMonitor progressMonitor) {
		progressMonitor.start((ProgressMonitor.WORK_TRACKABLE
				| ProgressMonitor.CANCELLABLE | ProgressMonitor.PAUSEABLE),
				totalWork);
		progressMonitor.describeWork("Loading ISI data into a database.");

	}

	private static double calculateTotalWork(Collection<Integer> rows) {
		double totalWork = rows.size()
				/ DerbyDatabaseCreator.PERCENTAGE_OF_PROGRESS_FOR_MODEL_CREATION;

		return totalWork;
	}

}
