package edu.iu.sci2.visualization.temporalbargraph;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.cishell.utilities.DateUtilities;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities;

public class VizAreaPSCreator {
	private static final int MAX_BARS_PER_PAGE = 50;
	
	public static final int MAX_LABEL_FONT_SIZE = 16;
	public static final int LABEL_BAR_SPACING = 15;

	private static final Function<PostScriptBar, Double> DELTA_Y_GETTER =
			new Function<PostScriptBar, Double>() {
				public Double apply(PostScriptBar bar) {
					return bar.amountPerDay();
				}				
			};
	
	private List<String> pages;

	/**
	 * This is a class that is responsible for the postscript of the visualization area of a page.  It includes the bars, bar labels, date lines and date labels
	 * @param csvWriter The writer for writing the name, width, height, and area for each bar.
	 * @param records The records that should become bars.
	 * @param height The height of the visualization area.
	 * @param width The width of the visualization area.
	 * @param scaleToOnePage Should the visualization be on one page, or MAX_BARS_PER_PAGE should be on each page
	 * @throws PostScriptCreationException
	 */
	public VizAreaPSCreator(CSVWriter csvWriter, List<Record> records, double height, double width, boolean scaleToOnePage) throws PostScriptCreationException{
		
		Date startDate = getStartDate(records);
		Date endDate = getEndDate(records);	
		
		List<PostScriptBar> bars = createBars(records, csvWriter, startDate);
		
		double totalDeltaX = getTotalDeltaX(startDate, endDate);
				
		if (scaleToOnePage) {
			pages = new ArrayList<String>();
			double totalDeltaY = getTotalDeltaY(bars);
			String page = getVisualizationArea(bars, totalDeltaX, totalDeltaY, height, width, startDate, endDate);
			pages.add(page);
		} else {
			double topNDeltaY = getTopNDeltaYSum(bars, MAX_BARS_PER_PAGE);
			pages = new LinkedList<String>();
			List<List<PostScriptBar>> splitBars = splitBars(bars, MAX_BARS_PER_PAGE);
			for(List<PostScriptBar> pageBars : splitBars){
				double totalDeltaY = topNDeltaY;
				String page = getVisualizationArea(pageBars, totalDeltaX, totalDeltaY, height, width, startDate, endDate);
				pages.add(page);
			}
		}
		
	}
	
	/**
	 * Get the postscript date lines for the graph.
	 * @param startDate The starting date of the graph.
	 * @param endDate The ending date of the graph.
	 * @param width The width of the graph.
	 * @param totalDeltaX The total deltaX.  This should be the total number of days.
	 * @return A list of postscript lines that represent the dateline.
	 */
	@SuppressWarnings("deprecation")
	private static List<String> getDateLines(Date startDate, Date endDate, double width, double totalDeltaX) {
		List<String> psDates = new LinkedList<String>();
		List<Date> newYearsDates = getNewYearsDates(startDate, endDate);
		for(Date newYear : newYearsDates){
			double usableXPoints = width;
			double pointsPerDay = usableXPoints / totalDeltaX;
			
			String label = Integer.toString(newYear.getYear());
			double x = Math.abs(DateUtilities.calculateDaysBetween(startDate, newYear)) * pointsPerDay;
			
			psDates.add(PostScriptFormationUtilities.line(String.format("(%s) %f dateline", label, x)));
		}
		
		return psDates;
	}

	/**
	 * This calculates all the new years dates between two dates and returns them as a list
	 * @param startDate The starting date.
	 * @param endDate The ending date.
	 * @return A list of Date objects representing all the new years between the dates.
	 */
	@SuppressWarnings("deprecation")
	private static List<Date> getNewYearsDates(Date startDate, Date endDate) {
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
	 * Given a list of records, it will return a date object that represents Jan 1st of the year after the last year.
	 * @param records
	 * @return A date object that represents Jan 1st of the year after the last year.
	 * @throws PostScriptCreationException
	 */
	@SuppressWarnings("deprecation")
	private static Date getEndDate(List<Record> records)
			throws PostScriptCreationException {
		if (records.size() <= 0) {
			throw new PostScriptCreationException(
					"You must provide some records for the PostScriptRecordManager to work");
		}
		
		Date endDate = Ordering.natural().min(records).getEndDate();
		
		// FIXME Switch to joda-time
		int year = endDate.getYear() + 1;
		int month = 0; // Jan
		int day = 11; // 1st
		Date lastYear = new Date(year, month, day);
		return lastYear;
	}

	/**
	 * Given a list of records, it will return a date object that represents jan 1 the earliest year.
	 * @param records
	 * @return A Date object that represents jan 1 the earliest year.
	 * @throws PostScriptCreationException
	 */
	@SuppressWarnings("deprecation")
	private static Date getStartDate(List<Record> records)
			throws PostScriptCreationException {
		if (records.size() <= 0) {
			throw new PostScriptCreationException(
					"You must provide some records for the PostScriptRecordManager to work");
		}
		
		Date startDate = Ordering.natural().min(records).getStartDate();
		
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
	private static List<List<PostScriptBar>> splitBars(List<PostScriptBar> bars,
			int maxBarsPerPage) {
		
		assert(maxBarsPerPage > 0);

		// The rounding errors here can add up and you might get ugly pages at the end.
		// SOMEDAY use a better algorithm here
		int pagesRequired = (int) Math.ceil((float) bars.size() / (float) maxBarsPerPage);
		int actualBarsPerPage = (int) Math.ceil((float) bars.size() / (float) pagesRequired);
		
		return Lists.partition(bars, actualBarsPerPage);
	}

	/**
	 * This will construct the postscript for a visualization area.
	 * @param bars
	 * @param totalDeltaX
	 * @param totalGraphDeltaY
	 * @param height
	 * @param width
	 * @param startDate
	 * @param endDate
	 * @return The postscript representing the page.
	 */
	private static String getVisualizationArea(List<PostScriptBar> bars,
			double totalDeltaX, double totalGraphDeltaY, double height, double width, Date startDate, Date endDate) {
		StringBuilder page = new StringBuilder();

		double barMarginTotal = height * 0.25;  // 25 percent of the graph height will distributed as spaces between bars
		double barSpacing = (barMarginTotal / (bars.size() + 2));  // there will be a margin at the top and bottom
		
		double usableYPoints = (height - barMarginTotal);
		double usableXPoints = width;
		
		double pointsPerDay = usableXPoints / totalDeltaX;
		
		double pointsPerY = usableYPoints / totalGraphDeltaY;
		
		// maybe the actual bars on the page will not require all the space.  In this case, space out the bars using bar spacing.
		double spaceUsedByBars = getTotalDeltaY(bars) * pointsPerY;
		
		if (spaceUsedByBars < usableYPoints){
			double extraSpace = usableYPoints - spaceUsedByBars;
			double extraSpacePerBar = extraSpace / (bars.size() + 2);  //allow for padding at top and bottom
			barSpacing += extraSpacePerBar;
		}		
		
			
		double fontSize = Math.min(barSpacing, MAX_LABEL_FONT_SIZE);

		page.append(PostScriptFormationUtilities.definition("vizboxtop", Double.toString(height)));
		page.append(PostScriptFormationUtilities.font("Arial", fontSize));

		List<String> dateLines = getDateLines(startDate, endDate, width, totalDeltaX);
		for(String date : dateLines){
			page.append(date);
		}

		double previousEndY = 0;
		for(PostScriptBar bar : bars){
			double startY = previousEndY + barSpacing;
			
			page.append(PostScriptFormationUtilities.line(String.format(
					"(%s) %f %f %f %f record", 
					bar.getName(), 
					bar.daysSinceEarliest() * pointsPerDay, 
					startY,
					bar.lengthInDays() * pointsPerDay, 
					bar.amountPerDay() * pointsPerY)));
			previousEndY = startY + (bar.amountPerDay() * pointsPerY);
		}
		
		
		return page.toString();
	}

	private static double getTotalDeltaY(List<PostScriptBar> bars){
		double totalDeltaY = 0;
		// If speed becomes a factor, put this calculation inside of the
		// loop where deltaY is calculated
		for (PostScriptBar bar : bars) {
			totalDeltaY += bar.amountPerDay();
		}
		return totalDeltaY;
	}

	private static double getTotalDeltaX(Date startDate, Date endDate) throws PostScriptCreationException {
		
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
	private static List<PostScriptBar> createBars(List<Record> records, CSVWriter csvWriter, Date startDate) {
		List<PostScriptBar> bars = new ArrayList<PostScriptBar>(records.size());
		for(Record record : records){
			double daysSinceEarliest = Math.abs(DateUtilities.calculateDaysBetween(startDate, record.getStartDate()));
			
			int daysBetweenStartAndStop = Math.abs(DateUtilities.calculateDaysBetween(record.getStartDate(), record.getEndDate()));
			if (daysBetweenStartAndStop == 0){
				continue;
			}
			
			double area = record.getAmount();
			
			double amountPerDay = area / daysBetweenStartAndStop;
			
			PostScriptBar psBar = new PostScriptBar(daysSinceEarliest, daysBetweenStartAndStop, amountPerDay, record);

			String[] bar = new String [] { psBar.getName(), Double.toString(psBar.lengthInDays()), Double.toString(psBar.amountPerDay()), Double.toString(psBar.getArea())};
			csvWriter.writeNext(bar);
		
			bars.add(psBar);
		}
		return bars;
	}

	private static double getTopNDeltaYSum(List<PostScriptBar> bars, int barsPerPage) {
		// TODO Use a heap instead?
		List<Double> deltaYs = ImmutableList.copyOf(Collections2.transform(bars, DELTA_Y_GETTER));		
		List<Double> greatestDeltaYs = Ordering.natural().greatestOf(deltaYs, barsPerPage);
		return sum(greatestDeltaYs);
	}
	
	public static double sum(Iterable<? extends Number> numbers) {
		double total = 0.0;
		
		for (Number number : numbers) {
			total += number.doubleValue();
		}
		
		return total;
	}

	/**
	 * This will return "header" definitions that represent all the definitions needed by a visualization area 
	 * @return
	 */
	public static String getPostScriptVisualizationDefinitions(){
		StringBuilder postScriptHeader = new StringBuilder();
		
		// General Definitions

		
		// The postscript definition of a bar
		postScriptHeader.append(PostScriptFormationUtilities.line("/bar { % x1 y1 deltaX deltaY => -") +
									PostScriptFormationUtilities.line("	newpath") +  
									PostScriptFormationUtilities.line("	4 2 roll moveto") +
									PostScriptFormationUtilities.line("	exch dup 0 rlineto 0 3 -1 roll") +
									PostScriptFormationUtilities.line("	rlineto neg 0 rlineto") +
									PostScriptFormationUtilities.line("	fill") +
								PostScriptFormationUtilities.line("} def"));
		
		// The postscript definition of a record
		postScriptHeader
				.append(PostScriptFormationUtilities.line("/record { % (label) x1 y1 deltaX deltaY => -")
						+ PostScriptFormationUtilities.line("	5 -1 roll % stack: x1 y1 deltaX deltaY label")
						+ PostScriptFormationUtilities.line("	4 index 4 index 3 index 2 div add % stack: x1 y1 deltaX deltaY label x1 (y1 + deltaY/2)")
						+ PostScriptFormationUtilities.line("	recordlabel % draw the label at x1 (y1 + deltaY/2)")
						+ PostScriptFormationUtilities.line("	bar")
						+ PostScriptFormationUtilities.line("} def"));
		
		// The postscript definition for label
		// TODO this should be related to the font size
		double labelBarSpacing = 14;
		postScriptHeader.append(PostScriptFormationUtilities.definition("labelbarspacing", Double.toString(labelBarSpacing)));
		postScriptHeader.append(
				PostScriptFormationUtilities.line("/recordlabel { % (label) => -")
				+ PostScriptFormationUtilities.line("	moveto")
				+ PostScriptFormationUtilities.line("	righttrim")
				+ PostScriptFormationUtilities.line("	dup stringwidth pop neg labelbarspacing sub 0 rmoveto")
				+ PostScriptFormationUtilities.line("	show")
				+ PostScriptFormationUtilities.line("} def"));
		
		// The postscript definition that concatinates strings.
		postScriptHeader.append(
				PostScriptFormationUtilities.line("/concatstrings { % (a) (b) => (ab)") +
				PostScriptFormationUtilities.line("	exch dup length") +
				PostScriptFormationUtilities.line("	2 index length add string") +
				PostScriptFormationUtilities.line("	dup dup 4 2 roll copy length") +
				PostScriptFormationUtilities.line("	4 -1 roll putinterval") +
				PostScriptFormationUtilities.line("} bind def"));
		

		
		// The postscript definition that helps trim the label
		postScriptHeader.append(
				PostScriptFormationUtilities.line("/righttrim { % (abcde) -> (a...)")
				+ PostScriptFormationUtilities.line("	dup stringwidth pop leftvizmargin labelbarspacing sub gt {")
				+ PostScriptFormationUtilities.line("	dup length 1 sub 3 sub 0 exch getinterval (...) concatstrings righttrim")
				+ PostScriptFormationUtilities.line("	} if ")
				+ PostScriptFormationUtilities.line("} def"));
		
		// The definition for a dateline
		postScriptHeader.append(
				PostScriptFormationUtilities.line("/dateline { % (label) x => -") +
				PostScriptFormationUtilities.line("	gsave") +
				PostScriptFormationUtilities.line("	dup") +
				PostScriptFormationUtilities.line("	0 moveto") +
				PostScriptFormationUtilities.line("	.75 .75 .75 setrgbcolor") +
				PostScriptFormationUtilities.line("	[15] 0 setdash") +
				PostScriptFormationUtilities.line("	1 setlinewidth") +
				PostScriptFormationUtilities.line("	0 vizboxtop rlineto") +
				PostScriptFormationUtilities.line("	stroke") +
				PostScriptFormationUtilities.line("	datelinelabel") +
				PostScriptFormationUtilities.line("	grestore") +
				PostScriptFormationUtilities.line("} def"));
								
		// The definition for a datelinelabel
		// TODO this needs to be a product of the spacing between the lines
		double dateLineLabelFontSize = 6;
		postScriptHeader.append(PostScriptFormationUtilities.definition("datelinelabelfontsize", "" + Double.toString(dateLineLabelFontSize)));
		
		postScriptHeader.append(
				PostScriptFormationUtilities.line("/datelinelabel { % (label) x => -") +
				PostScriptFormationUtilities.line("	/Helvetica datelinelabelfontsize selectfont") +
				PostScriptFormationUtilities.line("	datelinelabelfontsize neg center") +
				PostScriptFormationUtilities.line("	show") +
				PostScriptFormationUtilities.line("} def"));
		
		return postScriptHeader.toString();
	}
	
	public List<String> getPostScriptPages() {
		return pages;
	}
	
}
