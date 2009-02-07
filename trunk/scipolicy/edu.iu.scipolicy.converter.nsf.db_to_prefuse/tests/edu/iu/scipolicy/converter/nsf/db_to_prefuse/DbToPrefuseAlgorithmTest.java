package edu.iu.scipolicy.converter.nsf.db_to_prefuse;

import static org.junit.Assert.fail;

import java.io.File;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.database.DatabaseService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.iu.scipolicy.testutilities.TestContext;
import edu.iu.scipolicy.testutilities.TestUtilities;
import edu.iu.scipolicy.utilities.nsf.NsfNames;

public class DbToPrefuseAlgorithmTest {
	private TestContext ciShellContext;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		ciShellContext = TestUtilities.createFakeCIShellContext();
		ciShellContext.start();
	}

	@After
	public void tearDown() throws Exception {
		ciShellContext.stop();
	}

	@Test
	public void testExecuteWithDatabase() {
		String[][] nsfTable = formNSFTable();
		
		try {
			genericTestExecute(nsfTable);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	private void genericTestExecute(String[][] nsfTable) throws Exception {
		DatabaseService databaseService =
			(DatabaseService)this.ciShellContext.getService
				(DatabaseService.class.getName());
		
		Data[] nsfDatabaseData =
			TestUtilities.createDatabaseFromNSFTable(nsfTable, databaseService);
		
		Algorithm dbToPrefuseAlgorithm =
			new DbToPrefuseAlgorithm(nsfDatabaseData, null, this.ciShellContext);
		
		Data[] dbToPrefuseOutData = null;
		
		try {
			dbToPrefuseOutData = dbToPrefuseAlgorithm.execute();
		}
		catch (AlgorithmExecutionException e) {
			e.printStackTrace();
			throw new Exception
				("An error occurred in the DB to Prefuse algorithm.");
		}
	}
	
	private String[][] formNSFTable() {
		String[][] nsfTable = new String[][]
		{
			new String[]
			{
				"94942", "\"Micah\"", "7/3/1985", "7/4/2009", "1000"
			},
			new String[]
			{
				"114336", "\"Elisha\"", "11/19/1985", "10/20/1994", "5000"
			},
			new String[]
			{
				"133401", "\"Patrick\"", "3/15/1984", "3/15/1985", "800"
			},
			new String[]
			{
				"266802", "\"Mark\"", "1/1/1940", "12/31/2010", "10"
			}
		};
		
		return nsfTable;
	}
}
