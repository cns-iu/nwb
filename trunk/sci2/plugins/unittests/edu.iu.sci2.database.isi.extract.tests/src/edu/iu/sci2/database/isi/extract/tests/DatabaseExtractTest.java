package edu.iu.sci2.database.isi.extract.tests;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.LocalCIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.FileUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

@RunWith(Parameterized.class)
public class DatabaseExtractTest {
	private static Data databaseData;
	private static BundleContext bundleContext;
	private static CIShellContext ciContext;
	
	public static void makeDB() throws Exception {
		bundleContext = Activator.getContext();
		ciContext = new LocalCIShellContext(bundleContext);
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
			throw new Exception("Exception while running " + currentStatement, e);
		} finally {
			if (s != null) s.close();
			c.close();
		}
		
		databaseData = new BasicData(database, "db:isi");
	}
	
	// Annoying that the test name doesn't include the PID of the algorithm under test.
	// The first release of JUnit after April 9, 2012 will support this:
	// @Parameters(name="DatabaseExtractTest({0})")
	// See https://github.com/KentBeck/junit/commit/3a5c9f2731462e36dd1c173ea8840d7b9b34b0ab
	// and http://stackoverflow.com/a/10143872/88198
	@Parameters
	public static Collection<Object[]> data() throws Exception {
		makeDB();
		ServiceReference<?>[] refs = bundleContext.getServiceReferences(AlgorithmFactory.class.getName(),
				"(service.pid=*isi.extract.*)");
		ArrayList<Object[]> listOfObjArrays = Lists.newArrayList();
		for (ServiceReference<?> r : refs) {
			String thisPid = (String) r.getProperty("service.pid");
			// this is the only one that takes parameters, so skip it for now
			if (thisPid.contains("weighted_document")) continue;
			listOfObjArrays.add(new Object[] { thisPid });
		}
		return listOfObjArrays;
	}
	
	
	private String pid;
	public DatabaseExtractTest(String pid) {
		this.pid = pid;
	}
	
	@Test
	public void test() throws Exception {
		System.out.println(this.toString());
		AlgorithmFactory fact = AlgorithmUtilities.getAlgorithmFactoryByPID(
				pid, bundleContext);
		
		Algorithm algo = fact.createAlgorithm(new Data[] {databaseData}, new Hashtable<String, Object>(), ciContext);
		try {
			Data[] results = algo.execute();
			assertTrue(pid + " didn't generate any Data results", results.length > 0);
		} catch (AlgorithmExecutionException e) {
			throw new AssertionError("Error when running " + pid, e);
		}
	}

	public String toString() {
		return String.format("Test of %s", pid);
	}
}
