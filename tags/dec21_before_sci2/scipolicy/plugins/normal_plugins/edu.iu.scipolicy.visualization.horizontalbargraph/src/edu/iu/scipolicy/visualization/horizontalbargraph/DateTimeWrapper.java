package edu.iu.scipolicy.visualization.horizontalbargraph;

import org.joda.time.DateTime;

public class DateTimeWrapper {
	private DateTime dateTime;
	private boolean isSpecified;
	private boolean isValid;

	private DateTimeWrapper(
			DateTime dateTime, boolean isSpecified, boolean isValid) {
		this.dateTime = dateTime;
		this.isSpecified = isSpecified;
		this.isValid = isValid;
	}
	
	public DateTime getDateTime() {
		return this.dateTime;
	}
	
	public boolean isSpecified() {
		return this.isSpecified;
	}
	
	public boolean isValid() {
		return this.isValid;
	}
	
	public static DateTimeWrapper createValidDateTimeWrapper(
			DateTime dateTime) {
		return new DateTimeWrapper(dateTime, true, true);
	}
	
	public static DateTimeWrapper createUnspecifiedDateTimeWrapper() {
		return new DateTimeWrapper(null, false, false);
	}
	public static DateTimeWrapper createInvalidDateTimeWrapper() {
		return new DateTimeWrapper(null, true, false);
	}
}