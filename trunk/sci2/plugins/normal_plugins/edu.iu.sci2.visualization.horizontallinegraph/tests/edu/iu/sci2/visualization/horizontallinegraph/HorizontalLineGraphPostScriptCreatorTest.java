package edu.iu.sci2.visualization.horizontallinegraph;

//import static org.junit.Assert.fail;
//
//import java.util.Date;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.osgi.service.log.LogService;
//
//import prefuse.data.Table;
//import edu.iu.sci2.testutilities.TestLogService;
//import edu.iu.sci2.testutilities.TestUtilities;
//
//public class HorizontalLineGraphPostScriptCreatorTest {
//	private int MIN_NUMBER_OF_DAYS_FOR_BAR = 15;
//	
//	private LogService logger; 
//	
//	@Before
//	public void setUp() throws Exception {
//		this.logger = new TestLogService();
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	@Test
//	public void testCreatePostScriptWithEmptyTable() {
//		Table emptyTable = createEmptyTable();
//		
//		if (testCreatePostScriptGeneric(emptyTable))
//			fail();
//	}
//	
//	@Test
//	public void testCreatePostScriptWithNonEmptyTable() {
//		Table nonEmptyTable = createNonEmptyTable();
//		
//		if (!testCreatePostScriptGeneric(nonEmptyTable))
//			fail();
//	}
//	
//	private boolean testCreatePostScriptGeneric(Table table) {
//		HorizontalLineGraphPostScriptCreator postScriptCreator =
//			createHorizontalLineGraphPostScriptCreator();
//		
//		try {
//			postScriptCreator.createPostScript(table,
//											   MIN_NUMBER_OF_DAYS_FOR_BAR,
//											   logger);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			
//			return false;
//		}
//		
//		return true;
//	}
//	
//	private HorizontalLineGraphPostScriptCreator
//		createHorizontalLineGraphPostScriptCreator()
//	{
//		return new HorizontalLineGraphPostScriptCreator
//			(CommonTestData.RECORD_LABEL_KEY,
//			 CommonTestData.RECORD_START_DATE_KEY,
//			 CommonTestData.RECORD_END_DATE_KEY,
//			 CommonTestData.RECORD_AWARD_AMOUNT);
//	}
//	
//	private Table createEmptyTable() {
//		String[] columnNames = new String[]
//		{
//			CommonTestData.RECORD_LABEL_KEY,
//			CommonTestData.RECORD_START_DATE_KEY,
//			CommonTestData.RECORD_END_DATE_KEY,
//			CommonTestData.RECORD_AWARD_AMOUNT
//		};
//		
//		Class[] columnTypes =
//			new Class[] { String.class, Date.class, Date.class, Integer.class };
//		
//		return TestUtilities.createEmptyPrefuseTable(columnNames, columnTypes);
//	}
//	
//	private Table createNonEmptyTable() {
//		return TestUtilities.createPrefuseTableAndFillItWithTestRecordData
//			(CommonTestData.RECORD_LABEL_KEY,
//			 CommonTestData.RECORD_START_DATE_KEY,
//			 CommonTestData.RECORD_END_DATE_KEY,
//			 CommonTestData.RECORD_AWARD_AMOUNT);
//	}
//}
