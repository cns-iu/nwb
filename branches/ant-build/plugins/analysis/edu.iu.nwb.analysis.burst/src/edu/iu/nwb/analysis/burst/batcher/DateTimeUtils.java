package edu.iu.nwb.analysis.burst.batcher;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Years;

public final class DateTimeUtils {
	
	private DateTimeUtils() {
	}
	
	/**
	 * This is a simple years different method that relied on Joda Years.yearsBetween().
	 * The result did not include the starting year.
	 * @param startDate - contains the start date information
	 * @param endDate - contains the start date information
	 * @return Positive integer that represents of years different
	 */
	public static int getYearsDifferent(DateTime startDate, DateTime endDate) {
		return Math.abs(Years.yearsBetween(startDate, endDate).getYears());
	}
	
	/**
	 * This is a simple months different method that relied on Joda Months.monthsBetween().
	 * The result did not include the starting month.
	 * @param startDate - contains the start date information
	 * @param endDate - contains the start date information
	 * @return Positive integer that represents of months different
	 */
	public static int getMonthsDifferent(DateTime startDate, DateTime endDate) {		
		return Math.abs(Months.monthsBetween(startDate, endDate).getMonths());
	}
	
	/**
	 * This is a simple days different method that relied on Joda Days.daysBetween().
	 * The result did not include the starting day.
	 * @param startDate - contains the start date information
	 * @param endDate - contains the start date information
	 * @return Positive integer that represents of days different
	 */
	public static int getDaysDifferent(DateTime startDate, DateTime endDate) {
		return Math.abs(Days.daysBetween(startDate, endDate).getDays());
	}
	
	/**
	 * This is a simple hours different method that relied on Joda Hours.hoursBetween().
	 * The result did not include the starting hour.
	 * @param startDate - contains the start date information
	 * @param endDate - contains the start date information
	 * @return Positive integer that represents of hours different
	 */
	public static int getHoursDifferent(DateTime startDate, DateTime endDate) {
		return Math.abs(Hours.hoursBetween(startDate, endDate).getHours());
	}
	
	/**
	 * This is a simple minutes different method that relied on Joda Minutes.minutesBetween().
	 * The result did not include the starting minute.
	 * @param startDate - contains the start date information
	 * @param endDate - contains the start date information
	 * @return Positive integer that represents of minutes different
	 */
	public static int getMinutesDifferent(DateTime startDate, DateTime endDate) {
		return Math.abs(Minutes.minutesBetween(startDate, endDate).getMinutes());
	}
	
	/**
	 * This is a simple seconds different method that relied on Joda Seconds.secondsBetween().
	 * The result did not include the starting year.
	 * @param startDate - contains the start date information
	 * @param endDate - contains the start date information
	 * @return Positive integer that represents of seconds different
	 */
	public static int getSecondsDifferent(DateTime startDate, DateTime endDate) {
		return Math.abs(Seconds.secondsBetween(startDate, endDate).getSeconds());
	}
	
	public static DateTime addYears(DateTime dateTime, int years) {
		return dateTime.plusYears(years);
	}
	
	public static DateTime addMonths(DateTime dateTime, int months) {
		return dateTime.plusMonths(months);
	}
	
	public static DateTime addDays(DateTime dateTime, int days) {
		return dateTime.plusDays(days);
	}
	
	public static DateTime addHours(DateTime dateTime, int hours) {
		return dateTime.plusHours(hours);
	}
	
	public static DateTime addMinutes(DateTime dateTime, int minutes) {
		return dateTime.plusMinutes(minutes);
	}
	
	public static DateTime addSeconds(DateTime dateTime, int seconds) {
		return dateTime.plusSeconds(seconds);
	}
}
