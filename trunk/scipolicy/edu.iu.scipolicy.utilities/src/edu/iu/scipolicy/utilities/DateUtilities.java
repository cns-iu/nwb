package edu.iu.scipolicy.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

// TODO: Fix this class.
public class DateUtilities {
	// TODO: Is this actually necessary?
	public static Date[] generateDaysBetweenDates(Date startDate, Date endDate) {
		GregorianCalendar startDateCalendar =
			new GregorianCalendar(startDate.getYear() + 1900,
								  startDate.getMonth(),
								  startDate.getDate());
		
		GregorianCalendar endDateCalendar =
			new GregorianCalendar(endDate.getYear() + 1900,
								  endDate.getMonth(),
								  endDate.getDate());
		
		// Return an empty set of days (Dates) if the start date is actually AFTER
		// the end date.
		if (startDateCalendar.getTimeInMillis() > endDateCalendar.getTimeInMillis())
			return new Date [0];
		
		// There is at least one day between the provided start and end dates (dates
		// themselves included).
		
		ArrayList workingDaysBetweenDates = new ArrayList();
		GregorianCalendar currentCalendarForDateThatWeAreCalculating =
			(GregorianCalendar)startDateCalendar.clone();
		final Date actualEndDateAccordingToCalendar = endDateCalendar.getTime();
		boolean shouldKeepGeneratingDaysBetweenDates = true;
		int b = 0;
		
		// This is the meat of the Date generation.
		while (shouldKeepGeneratingDaysBetweenDates) {
			// Get the current calculated date.
			Date currentCalculatedDate =
				currentCalendarForDateThatWeAreCalculating.getTime();
			
			// Add the current date that we are calculating.
			workingDaysBetweenDates.add(currentCalculatedDate);
			
			// Move the current calendar for the date that we are calculating
			// forward in time a day.
			currentCalendarForDateThatWeAreCalculating.add(Calendar.DATE, 1);
			
			// Should we stop now?
			if ((currentCalculatedDate.getYear() ==
					actualEndDateAccordingToCalendar.getYear()) &&
				(currentCalculatedDate.getMonth() ==
					actualEndDateAccordingToCalendar.getMonth()) &&
				(currentCalculatedDate.getDate() ==
					actualEndDateAccordingToCalendar.getDate()))
			{
				shouldKeepGeneratingDaysBetweenDates = false;
			}
		}
		
		Date[] finalDaysBetweenDates = new Date [workingDaysBetweenDates.size()];
		
		return (Date[])workingDaysBetweenDates.toArray(finalDaysBetweenDates);
	}
	
	public static int calculateDaysBetween(Date[] dateSet) {
		return dateSet.length;
	}
	
	// Holy crap this blows!
	public static int calculateDaysBetween(Date startDate, Date endDate) {
		return calculateDaysBetween(generateDaysBetweenDates(startDate, endDate));
	}
	
	// TODO: This could REALLY be improved.
	// Assumes dateSet is sorted from earliest to latest.
	public static Date[] generateNewYearsDatesBetweenDates(Date[] dateSet) {
		ArrayList workingNewYearsDates = new ArrayList();
		
		// Return an empty set if there are no dates.
		if (dateSet.length == 0)
			return new Date[] {};
		
		// If the first date is not a new year's date, add a new year's date for
		// that date's year.
		if ((dateSet[0].getMonth() != 0) || (dateSet[0].getDate() != 1))
			workingNewYearsDates.add(new Date(dateSet[0].getYear(), 0, 1));
		
		// Find each date that has the month and day of 1-1 (well, 0-1 because Date
		// is stupid).
		for (int ii = 0; ii < dateSet.length; ii++) {
			if ((dateSet[ii].getMonth() == 0) && (dateSet[ii].getDate() == 1))
				workingNewYearsDates.add(dateSet[ii]);
		}
		
		// Make sure the year after the last date gets added as well.
		workingNewYearsDates.add
			(new Date((dateSet[dateSet.length - 1].getYear() + 1), 0, 1));
		
		Date[] finalNewYearsDates = new Date [workingNewYearsDates.size()];
		
		return (Date[])workingNewYearsDates.toArray(finalNewYearsDates);
	}
	
	// Holy crap this blows again!
	public static Date[] generateNewYearsDatesBetweenDates(Date startDate,
														   Date endDate)
	{
		Date[] allDaysBetweenDates = generateDaysBetweenDates(startDate, endDate);

		return generateNewYearsDatesBetweenDates(allDaysBetweenDates);
	}
	
	// TODO: This could also REALLY be improved.
	public static Date[] generateFirstOfTheMonthDatesBetweenDates(Date[] dateSet) {
		ArrayList workingFirstOfTheMonthDates = new ArrayList();
		
		// Find each date that has the day of 1.
		for (int ii = 0; ii < dateSet.length; ii++) {
			if (dateSet[ii].getDate() == 1)
				workingFirstOfTheMonthDates.add(dateSet[ii]);
		}
		
		Date[] finalFirstOfTheMonthDates =
			new Date [workingFirstOfTheMonthDates.size()];
		
		return (Date[])workingFirstOfTheMonthDates.toArray
			(finalFirstOfTheMonthDates);
	}
	
	public static Date[] generateFirstOfTheMonthDatesBetweenDates(Date startDate,
																  Date endDate)
	{
		Date[] allDaysBetweenDates = generateDaysBetweenDates(startDate, endDate);
		
		return generateFirstOfTheMonthDatesBetweenDates(allDaysBetweenDates);
	}
	
	public static void main(String[] args) {
		Date[] datesBetween = generateDaysBetweenDates(new Date(1984, 0, 1), new Date(2003, 11, 31));
		
		Date[] newYearsDates = generateNewYearsDatesBetweenDates(datesBetween);
		Date[] firstOfTheMonthDates = generateFirstOfTheMonthDatesBetweenDates(datesBetween);
		
		Date[] outputDates = newYearsDates;
		
		System.err.println("Dates (" + outputDates.length + "):");
		
		for (int ii = 0; ii < outputDates.length; ii++) {
			System.err.println("\tDate: (" + outputDates[ii].getMonth() + "/" + outputDates[ii].getDate() + "/" + outputDates[ii].getYear() + ")");
		}
	}
}