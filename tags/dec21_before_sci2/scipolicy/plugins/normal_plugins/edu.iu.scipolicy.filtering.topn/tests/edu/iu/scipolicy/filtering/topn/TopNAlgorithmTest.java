package edu.iu.scipolicy.filtering.topn;

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

public class TopNAlgorithmTest {
	public static final int TEST_TOP_N = 25;
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
			TopNTestUtilities.formUserParametersForTopN
				(TEST_TOP_N, TEST_COLUMN_NAME, isDescending);
		
		Data[] testInputData = TopNTestUtilities.formTestData(TEST_COLUMN_NAME);
		Data[] testOutputData = null;
		
		boolean testFailed = false;
		
		try {
			TopNAlgorithm topNAlgorithm =
				new TopNAlgorithm(testInputData, testUserParameters, null);
			
			testOutputData = topNAlgorithm.execute();
			
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
