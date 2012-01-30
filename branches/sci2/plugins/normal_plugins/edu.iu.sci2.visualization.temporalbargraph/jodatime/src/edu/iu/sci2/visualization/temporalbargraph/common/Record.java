package edu.iu.sci2.visualization.temporalbargraph.common;

import java.util.Comparator;
import java.util.Date;

import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.NumberUtilities;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;

import prefuse.data.Tuple;

import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities;

/**
 * A record stores information that might eventually lead to a visualization.
 * Information such as the data's category, label, start date, amount, etc are
 * kept here.
 * 
 */
public class Record {
	public static enum Category {
		DEFAULT
	};

	public static final Ordering<Record> START_DATE_ORDERING = Ordering
			.from(new Comparator<Record>() {
				@Override
				public int compare(Record r1, Record r2) {
					return DateTimeComparator.getInstance().compare(
							r1.getStartDate(), r2.getStartDate());
				}
			});
	public static final Ordering<Record> END_DATE_ORDERING = Ordering
			.from(new Comparator<Record>() {
				@Override
				public int compare(Record r1, Record r2) {
					return DateTimeComparator.getInstance().compare(
							r1.getEndDate(), r2.getEndDate());
				}
			});

	private static final DateTimeParser[] EUROPEAN_FORMATS = {
			DateTimeFormat.fullDate().getParser(),
			DateTimeFormat.longDate().getParser(),
			DateTimeFormat.mediumDate().getParser(),
			DateTimeFormat.shortDate().getParser(),
			DateTimeFormat.forPattern("d-MM-yy").getParser(),
			DateTimeFormat.forPattern("d-MM-yyyy").getParser(),
			DateTimeFormat.forPattern("dd-MM-yy").getParser(),
			DateTimeFormat.forPattern("dd-MM-yyyy").getParser(),
			DateTimeFormat.forPattern("d/MM/yy").getParser(),
			DateTimeFormat.forPattern("dd/MM/yy").getParser(),
			DateTimeFormat.forPattern("d/MM/yyyy").getParser(),
			DateTimeFormat.forPattern("dd/MMM/yyyy").getParser(),
			DateTimeFormat.forPattern("d-MMM-yy").getParser(),
			DateTimeFormat.forPattern("d-MMM-yyyy").getParser(),
			DateTimeFormat.forPattern("dd-MMM-yy").getParser(),
			DateTimeFormat.forPattern("dd-MMM-yyyy").getParser(),
			DateTimeFormat.forPattern("d/MMM/yy").getParser(),
			DateTimeFormat.forPattern("dd/MMM/yy").getParser(),
			DateTimeFormat.forPattern("d/MMM/yyyy").getParser(),
			DateTimeFormat.forPattern("dd/MMM/yyyy").getParser(),
			DateTimeFormat.forPattern("yyyy").getParser(), };

	private static final DateTimeParser[] US_FORMATS = {
			DateTimeFormat.fullDate().getParser(),
			DateTimeFormat.fullDate().getParser(),
			DateTimeFormat.longDate().getParser(),
			DateTimeFormat.mediumDate().getParser(),
			DateTimeFormat.shortDate().getParser(),
			DateTimeFormat.forPattern("MM-d-yy").getParser(),
			DateTimeFormat.forPattern("MM-d-yyyy").getParser(),
			DateTimeFormat.forPattern("MM-dd-yy").getParser(),
			DateTimeFormat.forPattern("MM-dd-yyyy").getParser(),
			DateTimeFormat.forPattern("MM/d/yy").getParser(),
			DateTimeFormat.forPattern("MM/dd/yy").getParser(),
			DateTimeFormat.forPattern("MM/d/yyyy").getParser(),
			DateTimeFormat.forPattern("MMM/dd/yyyy").getParser(),
			DateTimeFormat.forPattern("MMM-d-yy").getParser(),
			DateTimeFormat.forPattern("MMM-d-yyyy").getParser(),
			DateTimeFormat.forPattern("MMM-dd-yy").getParser(),
			DateTimeFormat.forPattern("MMM-dd-yyyy").getParser(),
			DateTimeFormat.forPattern("MMM/d/yy").getParser(),
			DateTimeFormat.forPattern("MMM/dd/yy").getParser(),
			DateTimeFormat.forPattern("MMM/d/yyyy").getParser(),
			DateTimeFormat.forPattern("MMM/dd/yyyy").getParser(),
			DateTimeFormat.forPattern("yyyy").getParser(), };

	private String label;
	private DateTime startDate;
	private DateTime endDate;
	private double amount;
	private String category;

	public Record(String label, Date startDate, Date endDate, double amount,
			String category) {
		this.label = label;
		this.startDate = new DateTime(startDate);
		this.endDate = new DateTime(endDate);
		this.amount = amount;
		this.category = category;
	}

	private static DateTime getDateTimeFromObject(Object date, String dateFormat)
			throws InvalidRecordException {
		if (date == null) {
			throw new IllegalArgumentException("The value for the row was null");
		} else if (Date.class == date.getClass()) {
			return new DateTime(date);
		} else if (String.class == date.getClass()) {
			DateTimeFormatter formatter;
			if (DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT.equals(dateFormat)) {
				formatter = new DateTimeFormatterBuilder().append(null,
						EUROPEAN_FORMATS).toFormatter();
			} else {
				formatter = new DateTimeFormatterBuilder().append(null,
						US_FORMATS).toFormatter();
			}

			// HACK The nsf data in the sample data all has an extra space
			// in
			// the dates other than the start date.
			return formatter.parseDateTime(date.toString()
					.replaceAll("  ", " "));

		} else {
			throw new InvalidRecordException(
					"Only date objects or string representations of the date are supported.");
		}

	}

	public Record(Tuple tableRow, String labelKey, String startDateKey,
			String endDateKey, String sizeByKey, String startDateFormat,
			String endDateFormat, String categoryKey)
			throws InvalidRecordException {

		this.label = PostScriptFormationUtilities.matchParentheses(tableRow
				.get(labelKey).toString());

		if (AbstractTemporalBarGraphAlgorithmFactory.DO_NOT_PROCESS_CATEGORY_VALUE
				.equals(categoryKey)) {
			this.category = PostScriptFormationUtilities
					.matchParentheses(Category.DEFAULT.toString());
		} else {
			this.category = PostScriptFormationUtilities
					.matchParentheses(tableRow.get(categoryKey).toString());
		}

		try {
			Object date = tableRow.get(startDateKey);
			this.startDate = getDateTimeFromObject(date, startDateFormat);
		} catch (IllegalArgumentException exception) {
			String exceptionMessage = "The record labeled \'" + this.label
					+ "\' contains an invalid start date.  It will be ignored.";

			throw new InvalidRecordException(exceptionMessage, exception);
		}

		try {
			Object date = tableRow.get(endDateKey);
			this.endDate = getDateTimeFromObject(date, endDateFormat);

		} catch (IllegalArgumentException exception) {
			String exceptionMessage = "The record labeled \'" + this.label
					+ "\' contains an invalid end date.  It will be ignored.";

			throw new InvalidRecordException(exceptionMessage, exception);
		}

		try {
			this.amount = NumberUtilities.interpretObjectAsDouble(
					tableRow.get(sizeByKey)).doubleValue();
		} catch (NumberFormatException invalidNumberFormatException) {
			String exceptionMessage = "The record labeled \'"
					+ this.label
					+ "\' "
					+ "contains an invalid number in the specified size-by column "
					+ "(" + sizeByKey + ").  It will be ignored.";

			throw new InvalidRecordException(exceptionMessage,
					invalidNumberFormatException);
		}

		if (Double.isInfinite(this.amount) || Double.isNaN(this.amount)) {
			String exceptionMessage = "The record labeled \'" + this.label
					+ "\' "
					+ "contains an invalid value in the specified size-by "
					+ "column (" + sizeByKey + ").  It will be ignored.";

			throw new InvalidRecordException(exceptionMessage);
		}

	}

	public String getLabel() {
		return this.label;
	}

	public DateTime getStartDate() {
		return this.startDate;
	}

	public DateTime getEndDate() {
		return this.endDate;
	}

	public double getAmount() {
		return this.amount;
	}

	/**
	 * Returns the category for the record. The default is defined by
	 * 'Category.DEFAULT'.
	 * 
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

}