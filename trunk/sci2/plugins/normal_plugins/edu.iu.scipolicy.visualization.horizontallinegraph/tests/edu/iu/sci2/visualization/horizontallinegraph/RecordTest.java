package edu.iu.sci2.visualization.horizontallinegraph;

//import static org.junit.Assert.fail;
//
//import java.util.Date;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//public class RecordTest {
//	private Record firstRecord;
//	private Record secondRecord;
//	private Record thirdRecord;
//	
//	@Before
//	public void setUp() throws Exception {
//		this.firstRecord =
//			new Record("record1", new Date(1984, 0, 1), new Date(1984, 0, 2), 0.0f);
//		
//		this.secondRecord =
//			new Record("record2", new Date(1984, 0, 1), new Date(1984, 0, 3), 0.1f);
//		
//		this.thirdRecord =
//			new Record("record3", new Date(1984, 0, 2), new Date(1984, 0, 3), 0.0f);
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	@Test public void testCompareToNotEqual() {
//		int compareToResultFirstToThird =
//			this.firstRecord.compareTo(this.thirdRecord);
//		
//		int compareToResultThirdToFirst =
//			this.thirdRecord.compareTo(this.firstRecord);
//		
//		if ((compareToResultFirstToThird >= 0) ||
//			(compareToResultThirdToFirst <= 0))
//		{
//			fail();
//		}
//	}
//	
//	@Test public void testCompareToEqual() {
//		int compareToResultFirstToSecond =
//			this.firstRecord.compareTo(this.secondRecord);
//		
//		int compareToResultSecondToFirst =
//			this.secondRecord.compareTo(this.firstRecord);
//		
//		if ((compareToResultFirstToSecond != 0) ||
//			(compareToResultSecondToFirst != 0))
//		{
//			fail();
//		}
//	}
//}
