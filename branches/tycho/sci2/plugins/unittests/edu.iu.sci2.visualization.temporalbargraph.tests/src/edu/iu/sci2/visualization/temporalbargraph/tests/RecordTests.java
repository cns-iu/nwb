package edu.iu.sci2.visualization.temporalbargraph.tests;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.cishell.utilities.DateUtilities;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import prefuse.data.Table;
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

	@Before
	public void setUp() {
		table = new Table();

		table.addColumn(labelKey, String.class);
		table.addColumn(startDateKey, String.class);
		table.addColumn(endDateKey, String.class);
		table.addColumn(startDateFormat, String.class);
		table.addColumn(endDateFormat, String.class);
		table.addColumn(sizeByKey, String.class);
		table.addColumn(categoryKey, String.class);
	}

	@Test
	public void testStringTable() {

		table.addRows(2);

		String row1Label = "Data 1";
		String row1StartDateKey = "10/22/2012";
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1EndDateKey = "10/22/2012";
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		table.set(0, labelKey, row1Label);
		table.set(0, startDateKey, row1StartDateKey);
		table.set(0, endDateKey, row1EndDateKey);
		table.set(0, startDateFormat, row1StartDateFormat);
		table.set(0, endDateFormat, row1EndDateFormat);
		table.set(0, sizeByKey, row1SizeByKey);
		table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		String row2StartDateKey = "1/22/2013";
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2EndDateKey = "11/22/2014";
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		table.set(1, labelKey, row2Label);
		table.set(1, startDateKey, row2StartDateKey);
		table.set(1, endDateKey, row2EndDateKey);
		table.set(1, startDateFormat, row2StartDateFormat);
		table.set(1, endDateFormat, row2EndDateFormat);
		table.set(1, sizeByKey, row2SizeByKey);
		table.set(1, categoryKey, row2CategoryKey);

		Record r1;
		Record r2;

		try {
			r1 = new Record(table.getTuple(0), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			r2 = new Record(table.getTuple(1), labelKey, startDateKey,
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
	public void testDateTable() {
		assertEquals(2082 - 1900, new Date(2082 - 1900, 1, 1).getYear());
		assertEquals(2083, new LocalDate(new Date(2083 - 1900, 1, 1)).getYear());
		assertEquals(2084, new DateTime(new Date(2084 - 1900, 1, 1))
				.toLocalDateTime().getYear());
		table = new Table();

		table.addColumn(labelKey, String.class);
		table.addColumn(startDateKey, Date.class);
		table.addColumn(endDateKey, Date.class);
		table.addColumn(startDateFormat, String.class);
		table.addColumn(endDateFormat, String.class);
		table.addColumn(sizeByKey, String.class);
		table.addColumn(categoryKey, String.class);

		table.addRows(2);

		String row1Label = "Data 1";
		Date row1StartDateKey = new Date(2012 - 1900, 10, 22);
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		Date row1EndDateKey = new Date(2012 - 1900, 10, 22);
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		table.set(0, labelKey, row1Label);
		table.set(0, startDateKey, row1StartDateKey);
		table.set(0, endDateKey, row1EndDateKey);
		table.set(0, startDateFormat, row1StartDateFormat);
		table.set(0, endDateFormat, row1EndDateFormat);
		table.set(0, sizeByKey, row1SizeByKey);
		table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		Date row2StartDateKey = new Date(2013 - 1900, 1, 22);
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		Date row2EndDateKey = new Date(2014 - 1900, 11, 22);
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		table.set(1, labelKey, row2Label);
		table.set(1, startDateKey, row2StartDateKey);
		table.set(1, endDateKey, row2EndDateKey);
		table.set(1, startDateFormat, row2StartDateFormat);
		table.set(1, endDateFormat, row2EndDateFormat);
		table.set(1, sizeByKey, row2SizeByKey);
		table.set(1, categoryKey, row2CategoryKey);

		Record r1;
		Record r2;

		try {
			r1 = new Record(table.getTuple(0), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			r2 = new Record(table.getTuple(1), labelKey, startDateKey,
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
	public void testInvalidRecord() throws InvalidRecordException {

		table.addRows(2);

		String row1Label = "Data 1";
		String row1StartDateKey = "10/22/2012";
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1EndDateKey = "10/22/2012";
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		table.set(0, labelKey, row1Label);
		table.set(0, startDateKey, row1StartDateKey);
		table.set(0, endDateKey, row1EndDateKey);
		table.set(0, startDateFormat, row1StartDateFormat);
		table.set(0, endDateFormat, row1EndDateFormat);
		table.set(0, sizeByKey, row1SizeByKey);
		table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		String row2StartDateKey = "1/22/2013";
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2EndDateKey = "";
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		table.set(1, labelKey, row2Label);
		table.set(1, startDateKey, row2StartDateKey);
		table.set(1, endDateKey, row2EndDateKey);
		table.set(1, startDateFormat, row2StartDateFormat);
		table.set(1, endDateFormat, row2EndDateFormat);
		table.set(1, sizeByKey, row2SizeByKey);
		table.set(1, categoryKey, row2CategoryKey);

		Record r1 = new Record(table.getTuple(0), labelKey, startDateKey,
				endDateKey, sizeByKey, startDateFormat, endDateFormat,
				categoryKey);
		boolean thrown = false;
		try {
			Record r2 = new Record(table.getTuple(1), labelKey, startDateKey,
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

		table.addRows(2);

		String row1Label = "Data 1";
		String row1StartDateKey = null;
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1EndDateKey = "10/22/2012";
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		table.set(0, labelKey, row1Label);
		table.set(0, startDateKey, row1StartDateKey);
		table.set(0, endDateKey, row1EndDateKey);
		table.set(0, startDateFormat, row1StartDateFormat);
		table.set(0, endDateFormat, row1EndDateFormat);
		table.set(0, sizeByKey, row1SizeByKey);
		table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		String row2StartDateKey = "1/22/2013";
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2EndDateKey = null;
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		table.set(1, labelKey, row2Label);
		table.set(1, startDateKey, row2StartDateKey);
		table.set(1, endDateKey, row2EndDateKey);
		table.set(1, startDateFormat, row2StartDateFormat);
		table.set(1, endDateFormat, row2EndDateFormat);
		table.set(1, sizeByKey, row2SizeByKey);
		table.set(1, categoryKey, row2CategoryKey);

		boolean thrown1 = false;
		try {
			Record r1 = new Record(table.getTuple(0), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
		} catch (InvalidRecordException e) {
			thrown1 = true;
		}

		assertTrue(thrown1);

		boolean thrown2 = false;
		try {
			Record r2 = new Record(table.getTuple(1), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
		} catch (InvalidRecordException e) {
			thrown2 = true;
		}

		assertTrue(thrown2);
	}
	
	@Test
	public void testOrderingTable() {

		table.addRows(3);

		String row1Label = "Data 1";
		String row1StartDateKey = "2/22/2012";
		String row1StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1EndDateKey = "10/22/2018";
		String row1EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row1SizeByKey = "300";
		String row1CategoryKey = "Category1";
		table.set(0, labelKey, row1Label);
		table.set(0, startDateKey, row1StartDateKey);
		table.set(0, endDateKey, row1EndDateKey);
		table.set(0, startDateFormat, row1StartDateFormat);
		table.set(0, endDateFormat, row1EndDateFormat);
		table.set(0, sizeByKey, row1SizeByKey);
		table.set(0, categoryKey, row1CategoryKey);

		String row2Label = "Data 2";
		String row2StartDateKey = "1/22/2013";
		String row2StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2EndDateKey = "11/22/2015";
		String row2EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row2SizeByKey = "3000";
		String row2CategoryKey = "Category2";

		table.set(1, labelKey, row2Label);
		table.set(1, startDateKey, row2StartDateKey);
		table.set(1, endDateKey, row2EndDateKey);
		table.set(1, startDateFormat, row2StartDateFormat);
		table.set(1, endDateFormat, row2EndDateFormat);
		table.set(1, sizeByKey, row2SizeByKey);
		table.set(1, categoryKey, row2CategoryKey);
		
		String row3Label = "Data 3";
		String row3StartDateKey = "3/3/2012";
		String row3StartDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row3EndDateKey = "11/22/2016";
		String row3EndDateFormat = DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT;
		String row3SizeByKey = "2831";
		String row3CategoryKey = "Category3";

		table.set(2, labelKey, row3Label);
		table.set(2, startDateKey, row3StartDateKey);
		table.set(2, endDateKey, row3EndDateKey);
		table.set(2, startDateFormat, row3StartDateFormat);
		table.set(2, endDateFormat, row3EndDateFormat);
		table.set(2, sizeByKey, row3SizeByKey);
		table.set(2, categoryKey, row3CategoryKey);

		Record r1;
		Record r2;
		Record r3;

		try {
			r1 = new Record(table.getTuple(0), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			r2 = new Record(table.getTuple(1), labelKey, startDateKey,
					endDateKey, sizeByKey, startDateFormat, endDateFormat,
					categoryKey);
			r3 = new Record(table.getTuple(2), labelKey, startDateKey,
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
}
