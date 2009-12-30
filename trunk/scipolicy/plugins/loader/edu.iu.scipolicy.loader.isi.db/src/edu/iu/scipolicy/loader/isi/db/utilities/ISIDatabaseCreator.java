package edu.iu.scipolicy.loader.isi.db.utilities;

import java.sql.Connection;
import java.sql.SQLException;

import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;

import edu.iu.scipolicy.loader.isi.db.model.ISIModel;
import edu.iu.scipolicy.loader.isi.db.utilities.exception.ModelCreationException;

public class ISIDatabaseCreator {
	// TODO: Better exception message?
	public static final String EXCEPTION_MESSAGE =
		"A problem occurred when reading ISI data.\n" +
		"Try reinstalling the application or contacting the Help Desk.";

	public static Database createFromModel(DatabaseService databaseProvider, ISIModel model)
			throws ModelCreationException {
		try {
			// Create an empty database and create a connection to it.

			Database isiDatabase = databaseProvider.createNewDatabase();
			Connection databaseConnection = isiDatabase.getConnection();

			// For table in the ISI model:
				
				// Create the database empty table that corresponds to it.

				

				/*
			 	 * Given all of the entities of the current entity type, form an SQL query that
				 *  constructs the corresponding entity table in the ISI database, and then run
				 *  that query.
			 	 */

			// Cleanup: Close the connection.

			databaseConnection.close();

			// Return the now-filled ISI database.

			return isiDatabase;
		} catch (DatabaseCreationException e) {
			throw new ModelCreationException(EXCEPTION_MESSAGE, e);
		} catch (SQLException e) {
			throw new ModelCreationException(EXCEPTION_MESSAGE, e);
		}
	}

	public static void createEmptyTable(Connection connection, ISIModel model) {
		
	}
}