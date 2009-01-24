package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.util.Date;

public class Grant {
	public static String GRANT_LABEL_KEY = "label";
	public static String GRANT_START_DATE_KEY = "start date";
	public static String GRANT_END_DATE_KEY = "end date";
	public static String GRANT_AMOUNT = "amount";
	
	private String label;
	private Date startDate;
	private Date endDate;
	private float amount;
	
	public Grant(String label, Date startDate, Date endDate, float amount)
		throws GrantCreationException
	{
		this.label = label;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
	}
	
	public Grant(TableRow tableRow) throws GrantCreationException {
		try {
			this.label = (String)tableRow.getValue(GRANT_LABEL_KEY);
			this.startDate = (Date)tableRow.getValue(GRANT_START_DATE_KEY);
			this.endDate = (Date)tableRow.getValue(GRANT_END_DATE_KEY);
			this.amount = ((Float)tableRow.getValue(GRANT_AMOUNT)).floatValue();
		}
		catch (NoSuchKeyValueException e) {
			throw new GrantCreationException(e);
		}
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
}