package edu.iu.sci2.visualization.temporalbargraph.common;

import java.awt.Color;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.color.ColorRegistry;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.Years;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
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
	protected static List<DateTime> getNewYearsDates(DateTime startDate, DateTime endDate) {
		List<DateTime> newYearsDates = new LinkedList<DateTime>();
		int startYear = startDate.toLocalDate().getYear();
		int endYear = endDate.toLocalDate().getYear();

		if (endYear - startYear < 1){
			newYearsDates.add(new LocalDate(startYear, DateTimeConstants.JANUARY, 1).toDateTimeAtStartOfDay());
		}else{
			for(int i = startYear; i <= endYear; i++){
				newYearsDates.add(new LocalDate(i, DateTimeConstants.JANUARY, 1).toDateTimeAtStartOfDay());
			}
		}
		
		return newYearsDates;
	}

	/**
	 * Retain {@code maxNumberToRetain} (mostly) equally spaced elements from
	 * {@code collection} after sorting using {@code comparator}.
	 * @param maxNumberToRetain Must be >= 2 since we always retain the first and last elements from {@code collection}.
	 */
	public static <E> List<E> decimate(
			Collection<? extends E> collection,
			Comparator<? super E> comparator,
			int maxNumberToRetain){
		Preconditions.checkArgument(maxNumberToRetain >= 2, "maxNumberToRetain must be >= 2, it was %d", maxNumberToRetain);
		Preconditions.checkArgument(collection.size() >= maxNumberToRetain, "collection must be >= maxNumberToRetain.  collection.size() was %d and maxNumberToRetain was %d", collection.size(), maxNumberToRetain);

		List<? extends E> sortedList = Ordering.from(comparator).sortedCopy(collection);		
		
		/* Always keep the two extreme elements from the original collection. */
		List<E> decimated = new ArrayList<E>(maxNumberToRetain);		
		decimated.add(sortedList.get(0));
		decimated.add(sortedList.get(sortedList.size() - 1));

		if (maxNumberToRetain == 2) {
			return Lists.newArrayList(decimated);
		}
		
		/* How many of the interior elements do we have,
		 * and how many do we want to retain?
		 * Then figure the corresponding step size for the original collection. */
		int numberOfInteriorElements = sortedList.size() - 2;
		int numberOfPreservedInteriorElements = maxNumberToRetain - 2;
		// Make sure to round the distance between retained interior elements, but never exceeds maxNumberToPreserve.
		int stepSize = (int) Math.ceil((double) numberOfInteriorElements / (double) numberOfPreservedInteriorElements);
		
		assert(stepSize > 0);
		
		for (int ii = stepSize; ii < sortedList.size() - 1; ii += stepSize) {
			decimated.add(sortedList.get(ii));
		}
		
		Collections.sort(decimated, comparator);
		return decimated;
	}
	
	/**
	 * Given a list of records, it will return a date object that represents Jan 1st of the year after the last year.
	 * @param records
	 * @return A date object that represents Jan 1st of the year after the last year.
	 * @throws PostScriptCreationException
	 */
	protected static DateTime getFirstNewYearAfterLastEndDate(List<Record> records)
			throws PostScriptCreationException {
		if (records.size() <= 0) {
			throw new PostScriptCreationException(
					"You must provide some records for the PostScriptRecordManager to work");
		}


		DateTime endDate = Record.END_DATE_ORDERING.max(records).getEndDate();

		DateTime lastYear = new LocalDate(endDate.toLocalDate().getYear() + 1, DateTimeConstants.JANUARY, 1).toDateTimeAtStartOfDay();
		return lastYear;
	}

	/**
	 * Given a list of records, it will return a date object that represents jan 1 the earliest year.
	 * @param records
	 * @return A Date object that represents jan 1 the earliest year.
	 * @throws PostScriptCreationException
	 */
	protected static DateTime getFirstNewYearBeforeStartDate(List<Record> records)
			throws PostScriptCreationException {
		if (records.size() <= 0) {
			throw new PostScriptCreationException(
					"You must provide some records for the PostScriptRecordManager to work");
		}

		DateTime startDate = Record.START_DATE_ORDERING.min(records).getStartDate();

		DateTime firstYear = new LocalDate(startDate.toLocalDate().getYear(), DateTimeConstants.JANUARY, 1).toDateTimeAtStartOfDay();
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

	/**
	 * Given a list of records, this will create postscriptbars
	 * @param records The records to be made into postscriptbars
	 * @param csvWriter The writer to write the name, width, height and area for each record
	 * @param startDate The starting date of the graph
	 * @return A list of all the postscriptbars
	 */
	protected static List<PostScriptBar> createBars(List<Record> records, CSVWriter csvWriter, DateTime startDate, ColorRegistry<String> colorRegistry) {
		List<PostScriptBar> bars = new ArrayList<PostScriptBar>(records.size());
		
		for(Record record : records){
			int daysBetweenStartAndStop = Days.daysBetween(record.getStartDate(), record.getEndDate()).getDays();
				
			if (daysBetweenStartAndStop == 0){
				daysBetweenStartAndStop = new Period(Years.ONE).getDays();
			}

			double area = record.getAmount();

			double amountPerDay = area / daysBetweenStartAndStop;
			
			Color barColor;
			if(record.getCategory().equals(Record.Category.DEFAULT.toString())){
				barColor = colorRegistry.getDefaultColor();
			}else {
				barColor = colorRegistry.getColorOf(record.getCategory());
			}
			 
			double daysSinceEarliest = Days.daysBetween(startDate, record.getStartDate()).getDays();
			
			PostScriptBar psBar = new PostScriptBar(daysSinceEarliest, daysBetweenStartAndStop, amountPerDay, record, barColor);
			
			String[] bar = new String [] { psBar.getName(), Double.toString(psBar.lengthInDays()), Double.toString(psBar.amountPerDay()), Double.toString(psBar.getArea())};
			csvWriter.writeNext(bar);

			bars.add(psBar);
		}
		return bars;
	}

	protected static double getTopNDeltaYSum(List<PostScriptBar> bars, int barsPerPage) {
		List<Double> deltaYs = ImmutableList.copyOf(Collections2.transform(bars, AMOUNT_PER_DAY_GETTER));		
		List<Double> greatestDeltaYs = Ordering.natural().greatestOf(deltaYs, barsPerPage);
		return sum(greatestDeltaYs);
	}

	private static double sum(Iterable<? extends Double> doubles) {
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
