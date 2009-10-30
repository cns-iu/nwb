package edu.iu.scipolicy.visualization.horizontalbargraph.record;

import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.UnitOfTime;

public interface Record extends Comparable<Record> {
	public String getLabel();
	public boolean hasStartDate();
	public DateTime getStartDate();
	public boolean hasEndDate();
	public DateTime getEndDate();
	public double getAmount();
	public double calculateAmountPerUnitOfTime(
			UnitOfTime unitOfTime, int minimumUnitOfTime);
}