package edu.iu.scipolicy.filtering.topnpercent;

import static org.junit.Assert.fail;

import java.util.Dictionary;

import org.cishell.framework.data.Data;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import prefuse.data.Table;
import edu.iu.scipolicy.filtering.topntestutilities.TopNTestUtilities;

public class TopNPercentAlgorithmTest {
	public static final float TEST_TOP_N_PERCENT = 0.25f;
	public static final String TEST_COLUMN_NAME = "meep_meep_MEEP";
	
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
	public void testExecuteIsDescending() {
		testExecuteGeneric(true);
	}
	
	@Test
	public void testExecuteIsNotDescending() {
		testExecuteGeneric(false);
	}
	
	private void testExecuteGeneric(boolean isDescending) {
		Dictionary testUserParameters =
			TopNTestUtilities.formUserParameters(TEST_TOP_N_PERCENT,
												 TEST_COLUMN_NAME,
												 isDescending);
		
		Data[] testInputData = TopNTestUtilities.formTestData(TEST_COLUMN_NAME);
		Data[] testOutputData = null;
		
		boolean testFailed = false;
		
		try {
			TopNPercentAlgorithm topNPercentAlgorithm =
				new TopNPercentAlgorithm(testInputData, testUserParameters, null);
			
			testOutputData = topNPercentAlgorithm.execute();
			
			Table testOutTable = (Table)testOutputData[0].getData();
			final int numTestOutTableRows = testOutTable.getRowCount();
			
			// Verify that the test out table is sorted in the correct order.
			
			if (isDescending) {
				int currentValue = 0;
				
				for (int ii = (numTestOutTableRows - 1); ii >= 0; ii--) {
					Integer cell = (Integer)testOutTable.get(ii, TEST_COLUMN_NAME);
					int thisRowValue = cell.intValue();
				
					if (thisRowValue < currentValue) {
						testFailed = true;
					
						break;
					}
					else
						currentValue = thisRowValue;
				}
			}
			else {
				int currentValue = 0;
				
				for (int ii = 0; ii < numTestOutTableRows; ii++) {
					Integer cell = (Integer)testOutTable.get(ii, TEST_COLUMN_NAME);
					int thisRowValue = cell.intValue();
				
					if (thisRowValue < currentValue) {
						testFailed = true;
					
						break;
					}
					else
						currentValue = thisRowValue;
				}
			}
		}
		catch (Exception e) {
			testFailed = true;
		}
		
		if (testFailed)
			fail();
	}
}
