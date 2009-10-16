package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.text.ParseException;
import java.util.Date;

import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.NumberUtilities;

import prefuse.data.Tuple;

public class Record implements Comparable<Record> {
	private String label;
	private Date startDate;
	private Date endDate;
	private double amount;
	
	public Record(String label, Date startDate, Date endDate, double amount) {
		this.label = label;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		
		fixDateYears();
	}
	
	public Record(Tuple tableRow,
				  String labelKey,
				  String startDateKey,
				  String endDateKey,
				  String sizeByKey) throws InvalidRecordException {
		this.label = (String)tableRow.get(labelKey);
		
		try {
			this.startDate = DateUtilities.interpretObjectAsDate(
				tableRow.get(startDateKey));
		} catch (ParseException unparsableDateException) {
			String exceptionMessage =
				"The record labeled \'" +
				this.label +
				"\' contains an invalid start date.  It will be ignored.";
			
			throw new InvalidRecordException(
				exceptionMessage, unparsableDateException);
		}
		
		try {
			this.endDate =
				DateUtilities.interpretObjectAsDate(tableRow.get(endDateKey));
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
	
	public int compareTo(Record otherGrant) {		
		return getStartDate().compareTo(otherGrant.getStartDate());
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
}