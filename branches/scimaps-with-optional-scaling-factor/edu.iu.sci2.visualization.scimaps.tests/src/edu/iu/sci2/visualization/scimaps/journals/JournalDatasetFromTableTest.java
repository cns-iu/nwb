package edu.iu.sci2.visualization.scimaps.journals;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import prefuse.data.Table;

public class JournalDatasetFromTableTest {
	private static final String COLUMN_NAME = "journal";
	
	private static LogService mockCountingLogger;
	private static JournalDataset dataset;

	@BeforeClass
	public static void prepareLoggerAndDataset() {
		Schema schema = new Schema(1);
		schema.addColumn(COLUMN_NAME, String.class);
		Table table = schema.instantiate();		
		table.getTuple(table.addRow()).setString(COLUMN_NAME, "Nature");
		table.getTuple(table.addRow()).setString(COLUMN_NAME, "Nature");
		table.getTuple(table.addRow()).setString(COLUMN_NAME, null);
		table.getTuple(table.addRow()).setString(COLUMN_NAME, null);
		table.getTuple(table.addRow()).setString(COLUMN_NAME, "Science");
		table.getTuple(table.addRow()).setString(COLUMN_NAME, "Science");
		table.getTuple(table.addRow()).setString(COLUMN_NAME, "Science");
		
		// Should see exactly one warning summarizing all null values set in fillTableWithJournals
		mockCountingLogger = EasyMock.createStrictMock(LogService.class);		
		mockCountingLogger.log(
				EasyMock.eq(LogService.LOG_WARNING), EasyMock.anyObject(String.class));
		EasyMock.expectLastCall().times(1);		
		EasyMock.replay(mockCountingLogger);
		
		dataset = JournalDataset.fromTable(table, COLUMN_NAME, mockCountingLogger);
	}
	
	@Test
	public void verifyLoggedWarnings() {
		EasyMock.verify(mockCountingLogger);
	}
	
	@Test
	public void testDatasetSizeIsFive() {
		assertEquals(5, dataset.size());
	}
	
	@Test
	public void testNatureCountIsTwo() {
		assertEquals(2, dataset.count(JournalDataset.Journal.forName("Nature")));
	}
	
	@Test
	public void testScienceCountIsThree() {
		assertEquals(3, dataset.count(JournalDataset.Journal.forName("Science")));
	}
}
