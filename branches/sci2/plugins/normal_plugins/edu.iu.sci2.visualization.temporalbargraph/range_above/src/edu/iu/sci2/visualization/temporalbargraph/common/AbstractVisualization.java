package edu.iu.sci2.visualization.temporalbargraph.common;

import java.awt.Color;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.color.ColorRegistry;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

/**
 * This is the Visualization Area of a the postscript document.
 * 
 */
public abstract class AbstractVisualization {

	protected static final int MAX_LINEDATES = 15;
	protected static final int MAX_BARS_PER_PAGE = 50;

	public static final int MAX_LABEL_FONT_SIZE = 12;
	public static final int LABEL_BAR_SPACING = 15;

	public static final String STRING_TEMPLATE_FILE_PATH = "/edu/iu/sci2/visualization/temporalbargraph/common/stringtemplates/visualization.st";
	public static final StringTemplateGroup group;	
	
	static {
		group = new StringTemplateGroup(new InputStreamReader(
				AbstractTemporalBarGraphAlgorithmFactory.class
						.getResourceAsStream(STRING_TEMPLATE_FILE_PATH)));
	}
	
	protected static final Function<PostScriptBar, Double> AMOUNT_PER_DAY_GETTER =
			new Function<PostScriptBar, Double>() {
		public Double apply(PostScriptBar bar) {
			return bar.amountPerDay();
		}				
	};

	/**
	 * This calculates all the new years dates between two dates and returns them as a list
	 * @param startDate The starting date.
	 * @param endDate The ending date.
	 * @return A list of Date objects representing all the new years between the dates.
	 */
	protected static List<Date> getNewYearsDates(Date startDate, Date endDate) {
		// FIXME Switch to joda-time
		List<Date> newYearsDates = new LinkedList<Date>();
		int startYear = startDate.getYear();
		int endYear = endDate.getYear();

		if (endYear - startYear < 1){
			int year = startYear;
			int month = 0; //Jan
			int day = 1; //1st
			newYearsDates.add(new Date(year, month, day));
		}else{
			for(int i = startYear; i <= endYear; i++){
				int year = i;
				int month = 0;  //jan
				int day = 1; //1st
				newYearsDates.add(new Date(year, month, day));
			}
		}
		return newYearsDates;
	}

	/**
	 * This will return a new list of dates that is smaller or equal to the maxDates
	 * @param dates The dates you wish to prune.
	 * @param maxDates The maximum number of dates to allow.
	 * @return
	 */
	protected static List<Date> reduceDates(List<Date> dates, int maxDates){
		Collections.sort(dates);
		
		assert(maxDates >= 2);  // you need more than 2 per page
		List<Date> reducedDates = new ArrayList<Date>(maxDates);
		
		// Keep the first and last dates always
		reducedDates.add(dates.get(0));
		reducedDates.add(dates.get(dates.size() - 1));
		
		// How many datelines are left
		int yearsLeft = dates.size() - 2;
		int datelinesNeeded = maxDates - 2;
		
		// Make sure to round so the graph is nicely spaced, but never exceeds maxDates.
		int yearsBetweenTicks = (int) Math.ceil((double) yearsLeft / (double) datelinesNeeded);
		
		for(int ii = yearsBetweenTicks; ii < dates.size(); ii += yearsBetweenTicks){
			reducedDates.add(dates.get(ii));
		}
		
		Collections.sort(reducedDates);
		
		return reducedDates;
	}
	
	/**
	 * Given a list of records, it will return a date object that represents Jan 1st of the year after the last year.
	 * @param records
	 * @return A date object that represents Jan 1st of the year after the last year.
	 * @throws PostScriptCreationException
	 */
	protected static Date getFirstNewYearAfterLastEndDate(List<Record> records)
			throws PostScriptCreationException {
		if (records.size() <= 0) {
			throw new PostScriptCreationException(
					"You must provide some records for the PostScriptRecordManager to work");
		}


		Date endDate = Record.END_DATE_ORDERING.max(records).getEndDate();

		// FIXME Switch to joda-time
		int year = endDate.getYear() + 1;
		int month = 0; // Jan
		int day = 1; // 1st
		Date lastYear = new Date(year, month, day);
		return lastYear;
	}

	/**
	 * Given a list of records, it will return a date object that represents jan 1 the earliest year.
	 * @param records
	 * @return A Date object that represents jan 1 the earliest year.
	 * @throws PostScriptCreationException
	 */
	protected static Date getFirstNewYearBeforeStartDate(List<Record> records)
			throws PostScriptCreationException {
		if (records.size() <= 0) {
			throw new PostScriptCreationException(
					"You must provide some records for the PostScriptRecordManager to work");
		}

		Date startDate = Record.START_DATE_ORDERING.min(records).getStartDate();

		// FIXME Switch to joda-time
		int year = startDate.getYear();
		int month = 0; //Jan
		int day = 1; // 1st
		Date firstYear = new Date(year, month, day);
		return firstYear;
	}

	/**
	 * Split a list of bars into maxBarsPerPage.  There may be fewer bars per page.
	 * @param bars
	 * @param maxBarsPerPage
	 * @return A list of a list of bars that represent the visualization.
	 */
	protected static List<List<PostScriptBar>> splitBars(List<PostScriptBar> bars,
			int maxBarsPerPage) {

		assert(maxBarsPerPage > 0);

		// The rounding errors here can add up and you might get ugly pages at the end.
		// SOMEDAY use a better algorithm here
		int pagesRequired = (int) Math.ceil((float) bars.size() / (float) maxBarsPerPage);
		int actualBarsPerPage = (int) Math.ceil((float) bars.size() / (float) pagesRequired);

		return Lists.partition(bars, actualBarsPerPage);
	}

	protected abstract String getVisualizationArea(List<PostScriptBar> bars);

	protected static double getTotalAmountPerDay(List<PostScriptBar> bars){
		double totalAmountPerDay = 0;
		// If speed becomes a factor, put this calculation inside of the
		// loop where deltaY is calculated
		for (PostScriptBar bar : bars) {
			totalAmountPerDay += bar.amountPerDay();
		}
		return totalAmountPerDay;
	}

	protected static double getTotalDays(Date startDate, Date endDate) throws PostScriptCreationException {

		double totalDays = DateUtilities.calculateDaysBetween(startDate, endDate); 
		if (totalDays == 0){
			throw new PostScriptCreationException("The start and end dates are the same.");
		}

		return totalDays;
	}

	/**
	 * Given a list of records, this will create postscriptbars
	 * @param records The records to be made into postscriptbars
	 * @param csvWriter The writer to write the name, width, height and area for each record
	 * @param startDate The starting date of the graph
	 * @return A list of all the postscriptbars
	 */
	protected static List<PostScriptBar> createBars(List<Record> records, CSVWriter csvWriter, Date startDate, ColorRegistry<String> colorRegistry) {
		List<PostScriptBar> bars = new ArrayList<PostScriptBar>(records.size());
		
		for(Record record : records){
			int daysBetweenStartAndStop = Math.abs(DateUtilities.calculateDaysBetween(record.getStartDate(), record.getEndDate()));
			
			if (daysBetweenStartAndStop == 0){
				
				// Assume that the user would want one year length if the start and stop days are the same.
				int year = record.getStartDate().getYear();
				int monthOfYear = DateTimeConstants.JANUARY;
				int dayOfMonth = 1;
				int hourOfDay = 12;
				int minuteOfHour = 0;
				int secondOfMinute = 0;
				int millisOfSecond = 000;
				
				DateTime recordYear = new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
				
				DateTime yearAfterRecordYear = new DateTime(year + 1, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
				
				daysBetweenStartAndStop = Days.daysBetween(recordYear, yearAfterRecordYear).getDays();
			}

			double area = record.getAmount();

			double amountPerDay = area / daysBetweenStartAndStop;
			
			Color barColor;
			if(record.getCategory().equals(Record.Category.DEFAULT.toString())){
				barColor = colorRegistry.getDefaultColor();
			}else {
				barColor = colorRegistry.getColorOf(record.getCategory());
			}
			 
			double daysSinceEarliest = Math.abs(DateUtilities.calculateDaysBetween(startDate, record.getStartDate()));
			PostScriptBar psBar = new PostScriptBar(daysSinceEarliest, daysBetweenStartAndStop, amountPerDay, record, barColor);
			
			String[] bar = new String [] { psBar.getName(), Double.toString(psBar.lengthInDays()), Double.toString(psBar.amountPerDay()), Double.toString(psBar.getArea())};
			csvWriter.writeNext(bar);

			bars.add(psBar);
		}
		return bars;
	}

	protected static double getTopNDeltaYSum(List<PostScriptBar> bars, int barsPerPage) {
		// TODO Use a heap instead?
		List<Double> deltaYs = ImmutableList.copyOf(Collections2.transform(bars, AMOUNT_PER_DAY_GETTER));		
		List<Double> greatestDeltaYs = Ordering.natural().greatestOf(deltaYs, barsPerPage);
		return sum(greatestDeltaYs);
	}

	public static double sum(Iterable<? extends Double> doubles) {
		double total = 0.0;

		for (double d : doubles) {
			total += d;
		}

		return total;
	}

	/**
	 * This will return "header" definitions that represent all the definitions needed by a visualization area 
	 * @return
	 */
	public abstract String renderDefinitionsPostscript();

	public abstract String renderVisualizationPostscript(int visualizationNumber);
	
	public abstract int numberOfVisualizations();
	
	public abstract double minRecordValue();
	
	public abstract double maxRecordValue();


}
