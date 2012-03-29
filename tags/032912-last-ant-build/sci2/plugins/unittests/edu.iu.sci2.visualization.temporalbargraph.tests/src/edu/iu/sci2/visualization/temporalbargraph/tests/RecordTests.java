package edu.iu.sci2.visualization.temporalbargraph.tests;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.cishell.utilities.DateUtilities;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import prefuse.data.DataTypeException;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.tuple.TableTuple;
import prefuse.data.tuple.TupleManager;
import edu.iu.sci2.visualization.temporalbargraph.common.InvalidRecordException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;

public class RecordTests extends TestCase {

	Table table;

	public static final String labelKey = "label_column";
	public static final String startDateKey = "start_date";
	public static final String endDateKey = "end_date";
	public static final String startDateFormat = "start_format";
	public static final String endDateFormat = "end_format";
	public static final String sizeByKey = "size_by";
	public static final String categoryKey = "category";

	@Override
	@Before
	public void setUp() {
		this.table = new Table();

		this.table.addColumn(labelKey, String.class);
		this.table.addColumn(startDateKey, String.class);
		this.table.addColumn(endDateKey, String.class);
		this.table.addColumn(startDateFormat, String.class);
		this.table.addColumn(endDateFormat, String.class);
		this.table.addColumn(sizeByKey, String.class);
		this.table.addColumn(categoryKey, String.class);
	}

	@Test
	public void testStringTable() {

		this.table.addRows(2);

		String row1Label = "Data 1";
		String row1StartDateKey = "10/22/2012";
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1EndDateKey = "10/22/2012";
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		this.table.set(0, labelKey, row1Label);
		this.table.set(0, startDateKey, row1StartDateKey);
		this.table.set(0, endDateKey, row1EndDateKey);
		this.table.set(0, startDateFormat, row1StartDateFormat);
		this.table.set(0, endDateFormat, row1EndDateFormat);
		this.table.set(0, sizeByKey, row1SizeByKey);
		this.table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		String row2StartDateKey = "1/22/2013";
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2EndDateKey = "11/22/2014";
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		this.table.set(1, labelKey, row2Label);
		this.table.set(1, startDateKey, row2StartDateKey);
		this.table.set(1, endDateKey, row2EndDateKey);
		this.table.set(1, startDateFormat, row2StartDateFormat);
		this.table.set(1, endDateFormat, row2EndDateFormat);
		this.table.set(1, sizeByKey, row2SizeByKey);
		this.table.set(1, categoryKey, row2CategoryKey);

		Record r1;
		Record r2;

		try {
			r1 = new Record(this.table.getTuple(0), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			r2 = new Record(this.table.getTuple(1), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			assertEquals(row1Label, r1.getLabel());
			assertEquals(2012, r1.getStartDate().toLocalDate().getYear());
			assertEquals(2013, r2.getStartDate().toLocalDate().getYear());

			assertEquals(row1CategoryKey, r1.getCategory());
			assertEquals(row2CategoryKey, r2.getCategory());
			assertNotSame(r1.getCategory(), r2.getCategory());
		} catch (InvalidRecordException e) {
			fail("An exception was thrown when creating the records that should not have been: "
					+ e.getMessage());
		}

	}

	@Test
	public void testIntegerTable() {
		this.table = new Table();

		this.table.addColumn(labelKey, String.class);
		this.table.addColumn(startDateKey, Integer.class);
		this.table.addColumn(endDateKey, Integer.class);
		this.table.addColumn(startDateFormat, String.class);
		this.table.addColumn(endDateFormat, String.class);
		this.table.addColumn(sizeByKey, String.class);
		this.table.addColumn(categoryKey, String.class);
		this.table.addRows(2);

		String row1Label = "Data 1";
		Integer row1StartDateKey = Integer.parseInt("2012");
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		Integer row1EndDateKey =  Integer.parseInt("2013");
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		this.table.set(0, labelKey, row1Label);
		this.table.set(0, startDateKey, row1StartDateKey);
		this.table.set(0, endDateKey, row1EndDateKey);
		this.table.set(0, startDateFormat, row1StartDateFormat);
		this.table.set(0, endDateFormat, row1EndDateFormat);
		this.table.set(0, sizeByKey, row1SizeByKey);
		this.table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		Integer row2StartDateKey = Integer.parseInt("2013");
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		Integer row2EndDateKey = Integer.parseInt("2014");
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		this.table.set(1, labelKey, row2Label);
		this.table.set(1, startDateKey, row2StartDateKey);
		this.table.set(1, endDateKey, row2EndDateKey);
		this.table.set(1, startDateFormat, row2StartDateFormat);
		this.table.set(1, endDateFormat, row2EndDateFormat);
		this.table.set(1, sizeByKey, row2SizeByKey);
		this.table.set(1, categoryKey, row2CategoryKey);

		Record r1;
		Record r2;

		try {
			r1 = new Record(this.table.getTuple(0), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			r2 = new Record(this.table.getTuple(1), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			assertEquals(row1Label, r1.getLabel());
			assertEquals(2012, r1.getStartDate().toLocalDate().getYear());
			assertEquals(2013, r2.getStartDate().toLocalDate().getYear());

			assertEquals(row1CategoryKey, r1.getCategory());
			assertEquals(row2CategoryKey, r2.getCategory());
			assertNotSame(r1.getCategory(), r2.getCategory());
		} catch (InvalidRecordException e) {
			fail("An exception was thrown when creating the records that should not have been: "
					+ e.getMessage());
		}

	}

	
	@SuppressWarnings("deprecation") // I'm testing dates so it's ok that they've been deprecated.
	@Test
	public void testDateTable() {
		assertEquals(2082 - 1900, new Date(2082 - 1900, 1, 1).getYear());
		assertEquals(2083, new LocalDate(new Date(2083 - 1900, 1, 1)).getYear());
		assertEquals(2084, new DateTime(new Date(2084 - 1900, 1, 1))
				.toLocalDateTime().getYear());
		this.table = new Table();

		this.table.addColumn(labelKey, String.class);
		this.table.addColumn(startDateKey, Date.class);
		this.table.addColumn(endDateKey, Date.class);
		this.table.addColumn(startDateFormat, String.class);
		this.table.addColumn(endDateFormat, String.class);
		this.table.addColumn(sizeByKey, String.class);
		this.table.addColumn(categoryKey, String.class);

		this.table.addRows(2);

		String row1Label = "Data 1";
		Date row1StartDateKey = new Date(2012 - 1900, 10, 22);
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		Date row1EndDateKey = new Date(2012 - 1900, 10, 22);
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		this.table.set(0, labelKey, row1Label);
		this.table.set(0, startDateKey, row1StartDateKey);
		this.table.set(0, endDateKey, row1EndDateKey);
		this.table.set(0, startDateFormat, row1StartDateFormat);
		this.table.set(0, endDateFormat, row1EndDateFormat);
		this.table.set(0, sizeByKey, row1SizeByKey);
		this.table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		Date row2StartDateKey = new Date(2013 - 1900, 1, 22);
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		Date row2EndDateKey = new Date(2014 - 1900, 11, 22);
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		this.table.set(1, labelKey, row2Label);
		this.table.set(1, startDateKey, row2StartDateKey);
		this.table.set(1, endDateKey, row2EndDateKey);
		this.table.set(1, startDateFormat, row2StartDateFormat);
		this.table.set(1, endDateFormat, row2EndDateFormat);
		this.table.set(1, sizeByKey, row2SizeByKey);
		this.table.set(1, categoryKey, row2CategoryKey);

		Record r1;
		Record r2;

		try {
			r1 = new Record(this.table.getTuple(0), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			r2 = new Record(this.table.getTuple(1), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			assertEquals(row1Label, r1.getLabel());
			assertEquals(2012, r1.getStartDate().toLocalDate().getYear());
			assertEquals(2013, r2.getStartDate().toLocalDate().getYear());

			assertEquals(row1CategoryKey, r1.getCategory());
			assertEquals(row2CategoryKey, r2.getCategory());
			assertNotSame(r1.getCategory(), r2.getCategory());
		} catch (InvalidRecordException e) {
			fail("An exception was thrown when creating the records that should not have been: "
					+ e.getMessage());
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void testInvalidRecord() throws InvalidRecordException {

		this.table.addRows(2);

		String row1Label = "Data 1";
		String row1StartDateKey = "10/22/2012";
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1EndDateKey = "10/22/2012";
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		this.table.set(0, labelKey, row1Label);
		this.table.set(0, startDateKey, row1StartDateKey);
		this.table.set(0, endDateKey, row1EndDateKey);
		this.table.set(0, startDateFormat, row1StartDateFormat);
		this.table.set(0, endDateFormat, row1EndDateFormat);
		this.table.set(0, sizeByKey, row1SizeByKey);
		this.table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		String row2StartDateKey = "1/22/2013";
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2EndDateKey = "";
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		this.table.set(1, labelKey, row2Label);
		this.table.set(1, startDateKey, row2StartDateKey);
		this.table.set(1, endDateKey, row2EndDateKey);
		this.table.set(1, startDateFormat, row2StartDateFormat);
		this.table.set(1, endDateFormat, row2EndDateFormat);
		this.table.set(1, sizeByKey, row2SizeByKey);
		this.table.set(1, categoryKey, row2CategoryKey);

		Record r1 = new Record(this.table.getTuple(0), labelKey, startDateKey,
				endDateKey, sizeByKey, startDateFormat, endDateFormat,
				categoryKey);
		boolean thrown = false;
		try {
			new Record(this.table.getTuple(1), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
		} catch (InvalidRecordException e) {
			thrown = true;
		}

		assertTrue(thrown);

		assertEquals(row1Label, r1.getLabel());
		assertEquals(2012, r1.getStartDate().toLocalDate().getYear());

		assertEquals(row1CategoryKey, r1.getCategory());

	}

	@Test
	public void testNullRecord() {

		this.table.addRows(2);

		String row1Label = "Data 1";
		String row1StartDateKey = null;
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1EndDateKey = "10/22/2012";
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		this.table.set(0, labelKey, row1Label);
		this.table.set(0, startDateKey, row1StartDateKey);
		this.table.set(0, endDateKey, row1EndDateKey);
		this.table.set(0, startDateFormat, row1StartDateFormat);
		this.table.set(0, endDateFormat, row1EndDateFormat);
		this.table.set(0, sizeByKey, row1SizeByKey);
		this.table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		String row2StartDateKey = "1/22/2013";
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2EndDateKey = null;
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		this.table.set(1, labelKey, row2Label);
		this.table.set(1, startDateKey, row2StartDateKey);
		this.table.set(1, endDateKey, row2EndDateKey);
		this.table.set(1, startDateFormat, row2StartDateFormat);
		this.table.set(1, endDateFormat, row2EndDateFormat);
		this.table.set(1, sizeByKey, row2SizeByKey);
		this.table.set(1, categoryKey, row2CategoryKey);

		boolean thrown1 = false;
		try {
			Record r1 = new Record(this.table.getTuple(0), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			assertNotNull(r1);
		} catch (InvalidRecordException e) {
			thrown1 = true;
		}

		assertTrue(thrown1);

		boolean thrown2 = false;
		try {
			Record r2 = new Record(this.table.getTuple(1), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			assertNotNull(r2);
		} catch (InvalidRecordException e) {
			thrown2 = true;
		}

		assertTrue(thrown2);
	}
	
	@Test
	public void testOrderingTable() {

		this.table.addRows(3);

		String row1Label = "Data 1";
		String row1StartDateKey = "2/22/2012";
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1EndDateKey = "10/22/2018";
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		this.table.set(0, labelKey, row1Label);
		this.table.set(0, startDateKey, row1StartDateKey);
		this.table.set(0, endDateKey, row1EndDateKey);
		this.table.set(0, startDateFormat, row1StartDateFormat);
		this.table.set(0, endDateFormat, row1EndDateFormat);
		this.table.set(0, sizeByKey, row1SizeByKey);
		this.table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		String row2StartDateKey = "1/22/2013";
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2EndDateKey = "11/22/2015";
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		this.table.set(1, labelKey, row2Label);
		this.table.set(1, startDateKey, row2StartDateKey);
		this.table.set(1, endDateKey, row2EndDateKey);
		this.table.set(1, startDateFormat, row2StartDateFormat);
		this.table.set(1, endDateFormat, row2EndDateFormat);
		this.table.set(1, sizeByKey, row2SizeByKey);
		this.table.set(1, categoryKey, row2CategoryKey);
		
		String row3Label = "Data 3";
		String row3StartDateKey = "3/3/2012";
		String row3StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row3EndDateKey = "11/22/2016";
		String row3EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row3SizeByKey = "2831";
		String row3CategoryKey = "Category3";

		this.table.set(2, labelKey, row3Label);
		this.table.set(2, startDateKey, row3StartDateKey);
		this.table.set(2, endDateKey, row3EndDateKey);
		this.table.set(2, startDateFormat, row3StartDateFormat);
		this.table.set(2, endDateFormat, row3EndDateFormat);
		this.table.set(2, sizeByKey, row3SizeByKey);
		this.table.set(2, categoryKey, row3CategoryKey);

		Record r1;
		Record r2;
		Record r3;

		try {
			r1 = new Record(this.table.getTuple(0), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			r2 = new Record(this.table.getTuple(1), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			r3 = new Record(this.table.getTuple(2), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			
			List<Record> unsortedList = new ArrayList<Record>();
			unsortedList.add(r1);
			unsortedList.add(r2);
			unsortedList.add(r3);
			
			List<Record> sortedList = Record.START_DATE_ORDERING.sortedCopy(unsortedList);
			assertEquals(sortedList.get(0), r1);
			assertEquals(sortedList.get(1), r3);
			assertEquals(sortedList.get(2), r2);
			
			sortedList = Record.END_DATE_ORDERING.sortedCopy(unsortedList);
			assertEquals(sortedList.get(0), r2);
			assertEquals(sortedList.get(1), r3);
			assertEquals(sortedList.get(2), r1);
						
		} catch (InvalidRecordException e) {
			fail("An exception was thrown when creating the records that should not have been: "
					+ e.getMessage());
		}

	}
	
	@SuppressWarnings({ "unused"})
	@Test
	public static void testNullLabel(){
		Map<String, Class<?>> columns = new HashMap<String, Class<?>>();

		columns.put(labelKey, String.class);
		columns.put(sizeByKey, Double.class);
		columns.put(startDateKey, Timestamp.class);
		columns.put(endDateKey, Timestamp.class);
		columns.put(categoryKey, String.class);

		Schema schema = new Schema(columns.keySet().toArray(new String[0]),
				columns.values().toArray(new Class<?>[0]));

		Table table = schema.instantiate();
		TupleManager tupleManager = new TupleManager(table, null,
				TableTuple.class);

		int rowId = table.addRow();
		Tuple row = tupleManager.getTuple(rowId);
		row.set(labelKey, null);
		row.set(sizeByKey, Double.valueOf(188273.0));
		row.set(startDateKey, new DateTime().toDate());
		row.set(endDateKey, new DateTime().toDate());
		row.set(categoryKey, "Category for null check");
		
		boolean exceptionCaught = false;
		
		try {
			new Record(row,
					labelKey, startDateKey, endDateKey, sizeByKey,
					DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT,
					DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT, categoryKey);
		} catch (InvalidRecordException e) {
			exceptionCaught = true;
			assertTrue("The message is not specific enough", e.getMessage().contains("label"));
		}
		
		assertTrue(exceptionCaught);
	}
	
	@Test
	public static void testNullStartDate(){
		Map<String, Class<?>> columns = new HashMap<String, Class<?>>();

		columns.put(labelKey, String.class);
		columns.put(sizeByKey, Double.class);
		columns.put(startDateKey, Timestamp.class);
		columns.put(endDateKey, Timestamp.class);
		columns.put(categoryKey, String.class);

		Schema schema = new Schema(columns.keySet().toArray(new String[0]),
				columns.values().toArray(new Class<?>[0]));

		Table table = schema.instantiate();
		TupleManager tupleManager = new TupleManager(table, null,
				TableTuple.class);

		int rowId = table.addRow();
		Tuple row = tupleManager.getTuple(rowId);
		boolean exceptionCaught = false;
		try {
			row.set(startDateKey, null);
		} catch (DataTypeException e) {
			exceptionCaught = true;
		}
		assertTrue(exceptionCaught);
	}
	
	@Test
	public static void testNullEndDate(){
		Map<String, Class<?>> columns = new HashMap<String, Class<?>>();

		columns.put(labelKey, String.class);
		columns.put(sizeByKey, Double.class);
		columns.put(startDateKey, Timestamp.class);
		columns.put(endDateKey, Timestamp.class);
		columns.put(categoryKey, String.class);

		Schema schema = new Schema(columns.keySet().toArray(new String[0]),
				columns.values().toArray(new Class<?>[0]));

		Table table = schema.instantiate();
		TupleManager tupleManager = new TupleManager(table, null,
				TableTuple.class);

		int rowId = table.addRow();
		Tuple row = tupleManager.getTuple(rowId);
		boolean exceptionCaught = false;
		try {
			row.set(endDateKey, null);
		} catch (DataTypeException e) {
			exceptionCaught = true;
		}

		assertTrue(exceptionCaught);
	}
	
	@SuppressWarnings({ "unused" })
	@Test
	public static void testNullSizeBy(){
		Map<String, Class<?>> columns = new HashMap<String, Class<?>>();

		columns.put(labelKey, String.class);
		columns.put(sizeByKey, Double.class);
		columns.put(startDateKey, Timestamp.class);
		columns.put(endDateKey, Timestamp.class);
		columns.put(categoryKey, String.class);

		Schema schema = new Schema(columns.keySet().toArray(new String[0]),
				columns.values().toArray(new Class<?>[0]));

		Table table = schema.instantiate();
		TupleManager tupleManager = new TupleManager(table, null,
				TableTuple.class);

		int rowId = table.addRow();
		Tuple row = tupleManager.getTuple(rowId);
		row.set(labelKey, "nuller");
		row.set(sizeByKey, null);
		row.set(startDateKey, new DateTime().toDate());
		row.set(endDateKey, new DateTime().toDate());
		row.set(categoryKey, "Category for null check");
		
		boolean exceptionCaught = false;
		
		try {
			new Record(row,
					labelKey, startDateKey, endDateKey, sizeByKey,
					DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT,
					DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT, categoryKey);
		} catch (InvalidRecordException e) {
			exceptionCaught = true;
			assertTrue("The message is not specific enough", e.getMessage().contains("size"));
		}
		
		assertTrue(exceptionCaught);
	}

	@SuppressWarnings({ "unused"})
	@Test
	public static void testNullCategory(){
		Map<String, Class<?>> columns = new HashMap<String, Class<?>>();

		columns.put(labelKey, String.class);
		columns.put(sizeByKey, Double.class);
		columns.put(startDateKey, Timestamp.class);
		columns.put(endDateKey, Timestamp.class);
		columns.put(categoryKey, String.class);

		Schema schema = new Schema(columns.keySet().toArray(new String[0]),
				columns.values().toArray(new Class<?>[0]));

		Table table = schema.instantiate();
		TupleManager tupleManager = new TupleManager(table, null,
				TableTuple.class);

		int rowId = table.addRow();
		Tuple row = tupleManager.getTuple(rowId);
		row.set(labelKey, "nuller");
		row.set(sizeByKey, Double.valueOf(188273.0));
		row.set(startDateKey, new DateTime().toDate());
		row.set(endDateKey, new DateTime().toDate());
		row.set(categoryKey, null);
		
		boolean exceptionCaught = false;
		
		try {
			new Record(row,
					labelKey, startDateKey, endDateKey, sizeByKey,
					DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT,
					DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT, categoryKey);
		} catch (InvalidRecordException e) {
			exceptionCaught = true;
			assertTrue("The message is not specific enough", e.getMessage().contains("category"));
		}
		
		assertTrue(exceptionCaught);
	}
	
	@SuppressWarnings({ "unused" })
	@Test
	public static void testBadTable() throws InvalidRecordException{
		Map<String, Class<?>> columns = new HashMap<String, Class<?>>();

		columns.put(labelKey, Integer.class);
		columns.put(sizeByKey, String.class);
		columns.put(startDateKey, Timestamp.class);
		columns.put(endDateKey, Timestamp.class);
		columns.put(categoryKey, String.class);

		Schema schema = new Schema(columns.keySet().toArray(new String[0]),
				columns.values().toArray(new Class<?>[0]));

		Table table = schema.instantiate();
		TupleManager tupleManager = new TupleManager(table, null,
				TableTuple.class);

		int rowId = table.addRow();
		Tuple row = tupleManager.getTuple(rowId);
		row.set(labelKey, Integer.valueOf(1));
		row.set(sizeByKey, "188273.0");
		row.set(startDateKey, new DateTime().toDate());
		row.set(endDateKey, new DateTime().toDate());
		row.set(categoryKey, "dog");

		new Record(row, labelKey, startDateKey, endDateKey, sizeByKey,
				DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT,
				DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT, categoryKey);

	}
}
