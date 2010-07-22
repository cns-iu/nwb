package edu.iu.scipolicy.visualization.horizontalbargraph.record;

import org.joda.time.Interval;
import org.joda.time.Months;

public abstract class AbstractRecord implements Record {
	private String label;
	private String colorizedBy;
	private double amount;
	private boolean hasInvalidAmount;
	
	public AbstractRecord(String label, String colorizedBy, double amount, boolean hasInvalidAmount) {
		this.label = label;
		this.colorizedBy = colorizedBy;
		this.amount = amount;
		this.hasInvalidAmount = hasInvalidAmount;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public String getColorizedBy() {
		return this.colorizedBy;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	public final double getAmountPerUnitOfTime() {
		long timeBetween = Math.max(
			new Interval(getStartDate(), getEndDate()).toDurationMillis(),
			Months.months(1).toPeriod().getMillis());

		return this.amount / timeBetween;
	}

	public final boolean hasInvalidAmount() {
		return this.hasInvalidAmount;
	}
	
	public int compareTo(Record otherRecord) {
		int startDateComparison =
			getStartDate().compareTo(otherRecord.getStartDate());
		
		if (startDateComparison != 0) {
			return startDateComparison;
		}
		
		int endDateComparison =
			getEndDate().compareTo(otherRecord.getEndDate());
		
		if (endDateComparison != 0) {
			return endDateComparison;
		}
		
		if (this.amount < otherRecord.getAmount()) {
			return -1;
		} else if (this.amount > otherRecord.getAmount()) {
			return 1;
		}
		
		return this.label.compareTo(otherRecord.getLabel());
	}
};