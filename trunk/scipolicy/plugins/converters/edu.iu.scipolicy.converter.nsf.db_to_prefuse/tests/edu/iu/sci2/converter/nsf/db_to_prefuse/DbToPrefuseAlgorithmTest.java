package edu.iu.sci2.converter.nsf.db_to_prefuse;

import static org.junit.Assert.fail;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.service.database.DatabaseService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.iu.sci2.converter.nsf.db_to_prefuse.DbToPrefuseAlgorithm;
import edu.iu.sci2.utilities.nsf.NsfNames;
import edu.iu.sci2.testutilities.TestContext;
import edu.iu.sci2.testutilities.TestUtilities;

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
		this.ciShellContext = TestUtilities.createFakeCIShellContext();
		this.ciShellContext.start();
	}

	@After
	public void tearDown() throws Exception {
		this.ciShellContext.stop();
	}

	@Test
	public void testExecuteWithDatabase() {
		NSFHeader nsfHeader = formNSFHeader();
		String[][] nsfTable = formNSFTable();
		
		try {
			genericTestExecute(nsfHeader, nsfTable);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	// This is generic in case we want to eventually test invalid
	// headers/table data.
	private void genericTestExecute(NSFHeader nsfHeader, String[][] nsfTable)
		throws Exception
	{
		DatabaseService databaseService =
			(DatabaseService)this.ciShellContext.getService
				(DatabaseService.class.getName());
		
		Data[] nsfDatabaseData = TestUtilities.createAndFillTestDatabase
			(NsfNames.DB.AWARD_TABLE,
			 nsfHeader.nsfHeader,
			 nsfHeader.primaryKey,
			 nsfTable,
			 databaseService);
		
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
	
	private NSFHeader formNSFHeader() {
		String[][] nsfHeader = new String[][]
		{
			{ NsfNames.DB.AWARD_NUMBER, "INTEGER" },
			{ NsfNames.DB.AWARD_TITLE, "VARCHAR(500)" },
			{ NsfNames.DB.AWARD_START_DATE, "DATE" },
			{ NsfNames.DB.AWARD_EXPIRATION_DATE, "DATE" },
			{ NsfNames.DB.AWARDED_AMOUNT_TO_DATE, "INTEGER" }
		};
		
		return new NSFHeader(nsfHeader, NsfNames.DB.AWARD_NUMBER);
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
	
	private class NSFHeader {
		public String[][] nsfHeader;
		public String primaryKey;
		
		public NSFHeader(String[][] nsfHeader, String primaryKey) {
			this.nsfHeader = nsfHeader;
			this.primaryKey = primaryKey;
		}
	}
}
