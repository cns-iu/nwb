package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.util.Date;

import prefuse.data.Tuple;

public class Grant implements Comparable {
	// TODO: Make these point to NSFConstants plugin eventually (when it exists).
	public static String GRANT_AWARD_LABEL_KEY = "TITLE";
	public static String GRANT_AWARD_START_DATE_KEY = "START_DATE";
	public static String GRANT_AWARD_END_DATE_KEY = "EXPIRATION_DATE";
	public static String GRANT_AWARD_AMOUNT = "AWARDED_AMOUNT_TO_DATE";
	public static String GRANT_AWARD_NUMBER = "AWARD_NUMBER";
	
	private String label;
	private Date startDate;
	private Date endDate;
	private double amount;
	
	public Grant(String label, Date startDate, Date endDate, double amount)
	{
		this.label = label;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		
		fixDateYears();
	}
	
	public Grant(Tuple tableRow,
				 String labelKey,
				 String startDateKey,
				 String endDateKey,
				 String sizeByKey)
	{
		this.label = (String)tableRow.get(labelKey);
		this.startDate = (Date)tableRow.get(startDateKey);
		this.endDate = (Date)tableRow.get(endDateKey);
		this.amount = ((Integer)tableRow.get(sizeByKey)).floatValue();
		
		fixDateYears();
	}
	
	public String getGrantLabel() {
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
	
	public int compareTo(Object otherObject) {
		Grant otherGrant = (Grant)otherObject;
		
		return getStartDate().compareTo(otherGrant.getStartDate());
	}
	
	private void fixDateYears() {
		if (this.startDate.getYear() < 1900)
			this.startDate.setYear(this.startDate.getYear() + 1900);
		
		if (this.endDate.getYear() < 1900)
			this.endDate.setYear(this.endDate.getYear() + 1900);
	}
}