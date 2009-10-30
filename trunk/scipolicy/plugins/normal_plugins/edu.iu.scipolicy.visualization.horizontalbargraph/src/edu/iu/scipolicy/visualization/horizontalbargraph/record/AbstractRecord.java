package edu.iu.scipolicy.visualization.horizontalbargraph.record;

import edu.iu.scipolicy.visualization.horizontalbargraph.UnitOfTime;

public abstract class AbstractRecord implements Record {
	private String label;
	private double amount;
	
	public AbstractRecord(String label, double amount) {
		this.label = label;
		this.amount = amount;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	public double calculateAmountPerUnitOfTime(
			UnitOfTime unitOfTime, int minimumUnitOfTime) {
		/* TODO: In this type of record, just calculate this when constructed.
		 * Also, change it to getAmountPerUnitOfTime in the interface.
		 */
		/* TODO: Instead of using a .max here, each record should have its end
		 * time adjusted as necessary as they enter the collection.
		 * That makes it unnecessary to do any special treatment later at all.
		 */
		int timeBetween = Math.max(
			unitOfTime.timeBetween(getStartDate(), getEndDate()),
			minimumUnitOfTime);
		
		/* TODO: Access amount directly.
		 * Also, add note as to why casting to double.
		 */
		// TODO: Move to using milliseconds everywhere.
		return getAmount() / (double)timeBetween;
	}
	
	public int compareTo(Record otherRecord) {
		/* TODO: Make compareTo consistent with what .equals would be
		 * (then implement .equals in terms of compareTo).
		 */
		/* TODO: startDate, endDate, amount, label
		 * (and eventually category, which should come after amount, maybe?)
		 */
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