package edu.iu.sci2.database.isi.extract.tests;

import java.io.File;
import java.sql.Connection;

import org.cishell.service.database.Database;
import org.cishell.utilities.FileUtilities;
import org.junit.Ignore;
import org.junit.Test;


/**
 * To test a particular query that's failing, try putting it in query.sql and un-@Ignore-ing this
 * test.  You get nice syntax highlighting in the .sql file in Eclipse, and a simple stack trace
 * :-)
 * @author thgsmith
 *
 */
@Ignore
public class SingleDatabaseQueryTest {
	@Test
	public void testSingleQuery() throws Exception {
		Database db = FivePapersHelper.createDatabase();
		Connection c = db.getConnection();
		File sqlFile =
				FileUtilities.safeLoadFileFromClasspath(DatabaseExtractTest.class, 
						"/edu/iu/sci2/database/isi/extract/tests/query.sql");
		String breakingQuery = FileUtilities.readEntireTextFile(sqlFile);
		c.createStatement().execute(breakingQuery);
		c.close();
	}

}
