package edu.iu.scipolicy.visualization.horizontalbargraph;

import org.joda.time.DateTime;

//TODO: Make start and end date stuff
public interface Record extends Comparable<Record> {
	public String getLabel();
	public DateTime getStartDate();
	public DateTime getEndDate();
	public double getAmount();
}