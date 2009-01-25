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
	private float amount;
	
	public Grant(String label, Date startDate, Date endDate, float amount)
	{
		this.label = label;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
	}
	
	public Grant(Tuple tableRow) {
		this.label = (String)tableRow.get(GRANT_AWARD_LABEL_KEY);
		this.startDate = (Date)tableRow.get(GRANT_AWARD_START_DATE_KEY);
		this.endDate = (Date)tableRow.get(GRANT_AWARD_END_DATE_KEY);
		this.amount = ((Integer)tableRow.get(GRANT_AWARD_AMOUNT)).floatValue();
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
	
	public float getAmount() {
		return this.amount;
	}
	
	public int compareTo(Object otherObject) {
		Grant otherGrant = (Grant)otherObject;
		
		return getStartDate().compareTo(otherGrant.getStartDate());
	}
}