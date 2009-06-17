package edu.iu.scipolicy.visualization.horizontallinegraph;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import prefuse.data.Table;
import edu.iu.scipolicy.testutilities.TestUtilities;

public class HorizontalLineGraphPostScriptCreatorTest {
	private int MIN_NUMBER_OF_DAYS_FOR_BAR = 15;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreatePostScriptWithEmptyTable() {
		Table emptyTable = createEmptyTable();
		
		if (testCreatePostScriptGeneric(emptyTable))
			fail();
	}
	
	@Test
	public void testCreatePostScriptWithNonEmptyTable() {
		Table nonEmptyTable = createNonEmptyTable();
		
		if (!testCreatePostScriptGeneric(nonEmptyTable))
			fail();
	}
	
	private boolean testCreatePostScriptGeneric(Table table) {
		HorizontalLineGraphPostScriptCreator postScriptCreator =
			createHorizontalLineGraphPostScriptCreator();
		
		try {
			postScriptCreator.createPostScript(table, MIN_NUMBER_OF_DAYS_FOR_BAR);
		}
		catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
		
		return true;
	}
	
	private HorizontalLineGraphPostScriptCreator
		createHorizontalLineGraphPostScriptCreator()
	{
		return new HorizontalLineGraphPostScriptCreator
			(Grant.GRANT_AWARD_LABEL_KEY,
			 Grant.GRANT_AWARD_START_DATE_KEY,
			 Grant.GRANT_AWARD_END_DATE_KEY,
			 Grant.GRANT_AWARD_AMOUNT);
	}
	
	private Table createEmptyTable() {
		String[] columnNames = new String[]
		{
			Grant.GRANT_AWARD_LABEL_KEY,
			Grant.GRANT_AWARD_START_DATE_KEY,
			Grant.GRANT_AWARD_END_DATE_KEY,
			Grant.GRANT_AWARD_AMOUNT
		};
		
		Class[] columnTypes =
			new Class[] { String.class, Date.class, Date.class, Integer.class };
		
		return TestUtilities.createEmptyPrefuseTable(columnNames, columnTypes);
	}
	
	private Table createNonEmptyTable() {
		return TestUtilities.createPrefuseTableAndFillItWithTestGrantData
			(Grant.GRANT_AWARD_LABEL_KEY,
			 Grant.GRANT_AWARD_START_DATE_KEY,
			 Grant.GRANT_AWARD_END_DATE_KEY,
			 Grant.GRANT_AWARD_AMOUNT);
	}
}
