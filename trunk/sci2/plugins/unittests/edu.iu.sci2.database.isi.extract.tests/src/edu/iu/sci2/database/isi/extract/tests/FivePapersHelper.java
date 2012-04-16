package edu.iu.sci2.database.isi.extract.tests;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseCreationException;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.DataFactory;
import org.cishell.utilities.FileUtilities;

import com.google.common.base.Splitter;


public class FivePapersHelper {
	public static final String DATA_FORMAT = "db:isi";
	
	/**
	 * Creates an ISI publication database that's equivalent to loading the Test5Papers.isi file
	 * from Sci2's sampledata directory.  The only difference is that the "ISI unique identifier"
	 * field has been replaced with hand-made identifiers because the original is binary junk
	 * (see <a href="http://jira.cns.iu.edu/browse/SCISQUARED-849">SCISQUARED-849</a>).
	 * <p>
	 * The loading of the file was done on April 12, 2012, so subsequent changes to the ISI db
	 * loading code will not be reflected in the sample data you get from this function.
	 * 
	 * @return a new Derby database, with data loaded as-if from an ISI file
	 * @throws IOException on a problem reading the input SQL file
	 * @throws SQLException on a problem running the SQL from the file
	 * @throws DatabaseCreationException if there's a problem creating a Derby database
	 */
	public static Database createDatabase() throws IOException, SQLException, DatabaseCreationException {
		CIShellContext ciContext = Activator.getCiContext();
		File sqlFile =
				FileUtilities.safeLoadFileFromClasspath(DatabaseExtractTest.class, 
						"/edu/iu/sci2/database/isi/extract/tests/FivePapersISIDb.sql");
		String dbCreationSQL = FileUtilities.readEntireTextFile(sqlFile);
		
		DatabaseService databaseProvider = (DatabaseService) ciContext.getService(DatabaseService.class.getName());
		Database database = databaseProvider.createNewDatabase();
		Connection c = database.getConnection();
		Statement s = null;
		String currentStatement = "";
		try {
			s = c.createStatement();
			// we're lucky that the data doesn't contain semicolons :-/
			for (String statement : Splitter.on(";").trimResults().omitEmptyStrings().split(dbCreationSQL)) {
				currentStatement = statement;
				s.execute(statement);
			}
		} catch (SQLException e) {
			throw new SQLException("Exception while running " + currentStatement, e);
		} finally {
			if (s != null) s.close();
			c.close();
		}
		return database;
	}
	
	public static Data wrappedAsData(Database db) {
		return DataFactory.forObject(db, FivePapersHelper.DATA_FORMAT, DataProperty.DATABASE_TYPE, null, "db");
	}
}
