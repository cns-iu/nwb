package edu.iu.sci2.visualization.horizontallinegraph;

//import static org.junit.Assert.fail;
//
//import java.util.Dictionary;
//import java.util.Hashtable;
//
//import org.cishell.framework.data.BasicData;
//import org.cishell.framework.data.Data;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import prefuse.data.Table;
//import edu.iu.sci2.testutilities.TestContext;
//import edu.iu.sci2.testutilities.TestUtilities;
//
//public class HorizontalLineGraphAlgorithmTest {
//	private TestContext ciShellContext;
//	
//	@Before
//	public void setUp() throws Exception {
//		this.ciShellContext = TestUtilities.createFakeCIShellContext();
//		this.ciShellContext.start();
//	}
//
//	@After
//	public void tearDown() throws Exception {
//		this.ciShellContext.stop();
//	}
//
//	@Test public void testExecute() {
//    	Table table = TestUtilities.createPrefuseTableAndFillItWithTestRecordData
//    		(CommonTestData.RECORD_LABEL_KEY,
//    		 CommonTestData.RECORD_START_DATE_KEY,
//    		 CommonTestData.RECORD_END_DATE_KEY,
//    		 CommonTestData.RECORD_AWARD_AMOUNT);
//    	
//    	Data dataItem = new BasicData(table, "file:text/table");
//    	Data[] data = new Data[] { dataItem };
//    	Dictionary parameters = formUserParameters();
//    	
//    	HorizontalLineGraphAlgorithm algorithm =
//    		new HorizontalLineGraphAlgorithm(data, parameters, this.ciShellContext);
//    	
//    	try {
//    		algorithm.execute();
//    	}
//    	catch (Exception e) {
//    		e.printStackTrace();
//    		fail();
//    	}
//	}
//	
//	@SuppressWarnings("unchecked")
//	private Dictionary formUserParameters() {
//		Hashtable parameters = new Hashtable();
//		
//		parameters.put(HorizontalLineGraphAlgorithm.LABEL_FIELD_ID,
//					   CommonTestData.RECORD_LABEL_KEY);
//		
//    	parameters.put(HorizontalLineGraphAlgorithm.START_DATE_FIELD_ID,
//    				   CommonTestData.RECORD_START_DATE_KEY);
//    	
//    	parameters.put(HorizontalLineGraphAlgorithm.END_DATE_FIELD_ID,
//    				   CommonTestData.RECORD_END_DATE_KEY);
//    	
//    	parameters.put(HorizontalLineGraphAlgorithm.SIZE_BY_FIELD_ID,
//    				   CommonTestData.RECORD_AWARD_AMOUNT);
//		
//		return parameters;
//	}
//}
