package edu.iu.sci2.visualization.temporalbargraph.common;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.NumberUtilities;

import prefuse.data.Tuple;

import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities;

public class Record {
	public static enum Category { DEFAULT };
	public static final Ordering<Record> START_DATE_ORDERING =
			Ordering.from(new Comparator<Record>(){
				@Override
				public int compare(Record r1, Record r2) {
					return r1.getStartDate().compareTo(r2.getStartDate());
				}});
	public static final Ordering<Record> END_DATE_ORDERING =
			Ordering.from(new Comparator<Record>(){
				@Override
				public int compare(Record r1, Record r2) {
					return r1.getEndDate().compareTo(r2.getEndDate());
				}});

	
	private String label;
	private Date startDate;
	private Date endDate;
	private double amount;
	private String category;
	
	/**
	 * TODO
	 * @param label
	 * @param startDate
	 * @param endDate
	 * @param amount
	 */
	public Record(String label, Date startDate, Date endDate, double amount, String category) {
		this.label = label;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.category = category;
		fixDateYears();
	}
	
	/**
	 * 
	 * @param tableRow
	 * @param labelKey
	 * @param startDateKey
	 * @param endDateKey
	 * @param sizeByKey
	 * @param startDateFormat
	 * @param endDateFormat
	 * @throws InvalidRecordException
	 */
	public Record(Tuple tableRow,
				  String labelKey,
				  String startDateKey,
				  String endDateKey,
				  String sizeByKey,
				  String startDateFormat,
				  String endDateFormat,
				  String categoryKey) throws InvalidRecordException {
		
		this.label = PostScriptFormationUtilities.matchParentheses((String)tableRow.get(labelKey));
		
		if (AbstractTemporalBarGraphAlgorithmFactory.DO_NOT_PROCESS_CATEGORY_VALUE
				.equals(categoryKey)) {
			this.category = PostScriptFormationUtilities
					.matchParentheses(Category.DEFAULT.toString());
		} else {
			this.category = PostScriptFormationUtilities
					.matchParentheses((String) tableRow.get(categoryKey));
		}

		try {
			this.startDate = DateUtilities.interpretObjectAsDate(
				tableRow.get(startDateKey), startDateFormat);
		} catch (ParseException unparsableDateException) {
			String exceptionMessage =
				"The record labeled \'" +
				this.label +
				"\' contains an invalid start date.  It will be ignored.";
			
			throw new InvalidRecordException(
				exceptionMessage, unparsableDateException);
		}
		
		try {
			this.endDate = DateUtilities.interpretObjectAsDate(
				tableRow.get(endDateKey), endDateFormat);
		} catch (ParseException unparsableDateException) {
			String exceptionMessage =
				"The record labeled \'" +
				this.label +
				"\' contains an invalid end date.  It will be ignored.";
			
			throw new InvalidRecordException(
				exceptionMessage, unparsableDateException);
		}
		
		try {
			this.amount = NumberUtilities.interpretObjectAsDouble(
				tableRow.get(sizeByKey)).doubleValue();
		} catch (NumberFormatException invalidNumberFormatException) {
			String exceptionMessage =
				"The record labeled \'" + this.label + "\' " +
				"contains an invalid number in the specified size-by column " +
				"(" + sizeByKey + ").  It will be ignored.";
			
			throw new InvalidRecordException(
				exceptionMessage, invalidNumberFormatException);
		}
		
		if (Double.isInfinite(this.amount) || Double.isNaN(this.amount)) {
			String exceptionMessage =
				"The record labeled \'" + this.label + "\' " +
				"contains an invalid value in the specified size-by " +
				"column (" + sizeByKey + ").  It will be ignored.";
			
			throw new InvalidRecordException(exceptionMessage);
		}
		
		fixDateYears();
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public Date getStartDate() {
		return this.startDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	private void fixDateYears() {
		// TODO: Just use Joda time.
		if (this.startDate.getYear() < 1900) {
			this.startDate.setYear(this.startDate.getYear() + 1900);
		}
		
		if (this.endDate.getYear() < 1900) {
			this.endDate.setYear(this.endDate.getYear() + 1900);
		}
	}
	
	/**
	 * Returns the category for the record.  The default is defined by 'Category.DEFAULT'.
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

}