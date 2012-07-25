package edu.iu.sci2.visualization.temporalbargraph.common;

import java.awt.Color;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.color.ColorRegistry;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import org.cishell.utilities.MapUtilities;

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

	protected static final Function<PostScriptBar, Double> AMOUNT_PER_DAY_GETTER = new Function<PostScriptBar, Double>() {
		@Override
		public Double apply(PostScriptBar bar) {
			return bar.amountPerDay();
		}
	};

	/**
	 * This will return the years that should compose the labeled years for the
	 * visualization area.
	 */
	protected static List<DateTime> getYearTicks(DateTime startDate,
			DateTime endDate, final int maxDateLines) {
		Preconditions.checkArgument(startDate.compareTo(endDate) < 0,
				"The start date must be before the end date!");
		Preconditions.checkArgument(maxDateLines > 2,
				"MAX_DATES must be greater than 2");

		List<DateTime> yearTicks = new LinkedList<DateTime>();

		int startYear = startDate.toLocalDate().getYear();
		// Ensure that the last year always fits
		int endYear = endDate.toLocalDate().getYear() + 1;

		int difference = (endYear - startYear);
		double yearsPerStep = (double) difference / (double) maxDateLines;
		if (yearsPerStep < 1) {
			// All the dates will fit

			for (int year = startYear; year <= endYear; year++) {
				yearTicks.add(new LocalDate(year, DateTimeConstants.JANUARY, 1)
						.toDateTimeAtStartOfDay());
			}
		} else {
			// All the dates cannot fit
			int step = (int) Math.ceil(yearsPerStep);
			
			for (int i = 0; i <= maxDateLines; i++) {
				int year = i * step + startYear;
				yearTicks.add(new LocalDate(year, DateTimeConstants.JANUARY, 1)
						.toDateTimeAtStartOfDay());
				if (year >= endYear) {
					// Stop making ticks if the end date has been reached.
					break;
				}
			}
		}

		assert yearTicks.size() >= 2;
		assert yearTicks.get(yearTicks.size() - 1).getYear() >= endYear;
		assert yearTicks.get(0).getYear() == startYear;
		return yearTicks;
	}
	
	/**
	 * Split a list of bars into maxBarsPerPage. There may be fewer bars per
	 * page.
	 * 
	 * @param bars
	 * @param maxBarsPerPage
	 * @return A list of a list of bars that represent the visualization.
	 */
	protected static List<List<PostScriptBar>> splitBars(
			List<PostScriptBar> bars, int maxBarsPerPage) {

		assert (maxBarsPerPage > 0);

		// The rounding errors here can add up and you might get ugly pages at
		// the end.
		// SOMEDAY use a better algorithm here
		int pagesRequired = (int) Math.ceil((float) bars.size()
				/ (float) maxBarsPerPage);
		int actualBarsPerPage = (int) Math.ceil((float) bars.size()
				/ (float) pagesRequired);

		return Lists.partition(bars, actualBarsPerPage);
	}

	protected abstract String getVisualizationArea(List<PostScriptBar> bars);

	protected static double getTotalAmountPerDay(List<PostScriptBar> bars) {
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
	 * 
	 * @param records
	 *            The records to be made into postscriptbars
	 * @param csvWriter
	 *            The writer to write the name, width, height and area for each
	 *            record
	 * @param startDate
	 *            The starting date of the graph
	 * @return A list of all the postscriptbars
	 */
	protected static List<PostScriptBar> createBars(List<Record> records,
			CSVWriter csvWriter, DateTime startDate,
			ColorRegistry<String> colorRegistry) {
		List<PostScriptBar> bars = new ArrayList<PostScriptBar>(records.size());
		prepareColorsRankedByCategoryTotal(records, colorRegistry);
		for (Record record : records) {
			int daysBetweenStartAndStop = Days.daysBetween(
					record.getStartDate(), record.getEndDate()).getDays();

			if (daysBetweenStartAndStop == 0) {
				daysBetweenStartAndStop = Days.daysBetween(
						record.getStartDate(),
						record.getStartDate().plusYears(1)).getDays();
				assert daysBetweenStartAndStop > 0;
			}

			double area = record.getAmount();

			double amountPerDay = area / daysBetweenStartAndStop;

			Color barColor;
			if (record.getCategory().equals(Record.Category.DEFAULT.toString())) {
				barColor = colorRegistry.getDefaultColor();
			} else {
				barColor = colorRegistry.getColorOf(record.getCategory());
			}

			double daysSinceEarliest = Days.daysBetween(startDate,
					record.getStartDate()).getDays();

			PostScriptBar psBar = new PostScriptBar(daysSinceEarliest,
					daysBetweenStartAndStop, amountPerDay, record, barColor);

			String[] bar = new String[] { psBar.getName(),
					Double.toString(psBar.lengthInDays()),
					Double.toString(psBar.amountPerDay()),
					Double.toString(psBar.getArea()) };
			csvWriter.writeNext(bar);

			bars.add(psBar);
		}
		return bars;
	}

	/**
	 * This initialized the color registry to color the categories that have the
	 * highest total amount. If there are already colors registered, nothing will be
	 * done.
	 * 
	 * @param colorRegistry
	 *            The color registry to setup
	 */
	private static void prepareColorsRankedByAmount(List<Record> records,
			ColorRegistry<String> colorRegistry) {
		if (colorRegistry.getKeySet().size() > 0) {
			throw new IllegalArgumentException("The color registry has already been initialized.");
		}
		
		Map<String, Double> categoryToAmount = new HashMap<String, Double>();
		for (Record record : records) {
			String category = record.getCategory();
			Double amount = categoryToAmount.get(record.getCategory());
			if (amount == null) {
				amount = record.getAmount();
			} else {
				amount += record.getAmount();
			}
			categoryToAmount.put(category, amount);
		}

		ImmutableList<String> categoriesSortedByAmount = Ordering.natural().reverse()
				.onResultOf(Functions.forMap(categoryToAmount))
				.immutableSortedCopy(categoryToAmount.keySet());

		for (String category : categoriesSortedByAmount) {
			colorRegistry.getColorOf(category);
		}

		return;
	}
	
	/**
	 * This initialized the color registry to color the categories that have the
	 * highest total. If there are already colors registered, nothing will be
	 * done.
	 * 
	 * @param colorRegistry
	 *            The color registry to setup
	 */
	private static void prepareColorsRankedByCategoryTotal(List<Record> records,
			ColorRegistry<String> colorRegistry) {
		
		if (colorRegistry.getKeySet().size() > 0) {
			throw new IllegalArgumentException("The color registry has already been initialized.");
		}
		
		List<String> categories = new ArrayList<String>();
		
		for (Record record : records) {
			categories.add(record.getCategory());
		}
		
		Map<String, Integer> categoryCount = MapUtilities.keysToCounts(categories);
		
		ImmutableList<String> categoriesSortedByCount = Ordering.natural().reverse()
				.onResultOf(Functions.forMap(categoryCount))
				.immutableSortedCopy(categoryCount.keySet());

		for (String category : categoriesSortedByCount) {
			colorRegistry.getColorOf(category);
		}

		return;
	}

	protected static double getTopNDeltaYSum(List<PostScriptBar> bars,
			int barsPerPage) {
		List<Double> deltaYs = ImmutableList.copyOf(Collections2.transform(
				bars, AMOUNT_PER_DAY_GETTER));
		List<Double> greatestDeltaYs = Ordering.natural().greatestOf(deltaYs,
				barsPerPage);
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
	 * This will return "header" definitions that represent all the definitions
	 * needed by a visualization area
	 */
	public abstract String renderDefinitionsPostscript();

	public abstract String renderVisualizationPostscript(int visualizationNumber);

	public abstract int numberOfVisualizations();

	public abstract double minRecordValue();

	public abstract double maxRecordValue();

}
