package edu.iu.sci2.filtering.topncommon;

import static org.junit.Assert.fail;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.sci2.filtering.topncommon.TopNUtilities;

import prefuse.data.Table;

public class TopNUtilitiesTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMutateParameters() {
		// There's not much to test here, so just make sure no exception is thrown.
		ObjectClassDefinition ocd = new
			BasicObjectClassDefinition("TopNUtilitiesTestID",
									   "Top N Utilities Test",
									   "Testing TopNUtilities",
									   null);
		
		Table testTable = new Table();
		Data testData = new BasicData(testTable, testTable.getClass().getName());
		Data[] testDataSet = new Data[] { testData };
		
		boolean testPassed = false;
		
		try {
			ObjectClassDefinition  newOCD =
				TopNUtilities.mutateParameters(testDataSet, ocd);
			
			// newOCD should NEVER be null, nor should it ever be the same object
			// as ocd.  (Give us something more to test, mutateParameters!)
			if ((newOCD != null) && (newOCD != ocd))
				testPassed = true;
		}
		catch (Exception e) {
			testPassed = false;
		}
		
		if (!testPassed)
			fail();
	}
	
	@Test
	public void testSortTableWithOnlyTopNUsingValidColumnName() {
		Table testTable = new Table();
		
		testTable.addColumn("test_column", String.class);
		
		boolean testPassed = false;
		
		try {
			Table sortedTable = TopNUtilities.
				sortTableWithOnlyTopN(testTable,
									  "test_column",
									  true,
									  -1);
			
			if (sortedTable != null)
				testPassed = true;
		}
		catch (Exception e) {
		}
		
		if (!testPassed)
			fail();
	}

	@Test
	public void testSortTableWithOnlyTopNUsingInvalidColumnName() {
		Table testTable = new Table();
		
		boolean testPassed = false;
		
		try {
			// Make sure sortTableWithOnlyTopN is handling an empty table, an
			// invalid column to sort by, and an invalid number of rows to copy
			// properly.
			Table sortedTable = TopNUtilities.
				sortTableWithOnlyTopN(testTable,
									  "test column that probably doesn't exist",
									  true,
									  -1);
		}
		catch (Exception e) {
			testPassed = true;
		}
		
		if (!testPassed)
			fail();
	}
}
