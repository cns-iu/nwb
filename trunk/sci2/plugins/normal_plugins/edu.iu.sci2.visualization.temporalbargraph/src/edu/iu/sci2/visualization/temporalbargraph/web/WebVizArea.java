package edu.iu.sci2.visualization.temporalbargraph.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.DateUtilities;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractVizArea;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptBar;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;

/**
 * This is the Visualization Area of a the postscript document for the web. It
 * includes things like the bars, labels for the bar, ticks for the dates, and
 * labels for those ticks.  It also manages things like font sizes and margins
 * 
 */
public class WebVizArea extends AbstractVizArea{
	
	private static final int MAX_LINEDATES = 15;
	private static Date vizAreaStartDate;
	private static Date vizAreaEndDate;
	
	private static double vizAreaHeight;
	private static double vizAreaWidth;
	
	private static double vizAreaLeftMargin;
	private static double vizAreaBottomMargin;
	
	private static double vizAreaTotalDays;
	private static double vizAreaDeltaY;
	
	private List<String> pages;
	private StringBuilder vizAreaDefinitions;
	
	public static StringTemplateGroup group = WebTemporalBarGraphAlgorithmFactory.loadTemplates();
	
	
	public WebVizArea (CSVWriter csvWriter, List<Record> records, double pageWidth, double pageHeight, boolean scaleToOnePage) throws PostScriptCreationException{
		
		vizAreaStartDate = getStartDate(records);
		vizAreaEndDate = getEndDate(records);	
		
		Collections.sort(records, Record.START_DATE_ORDERING);
		List<PostScriptBar> bars = createBars(records, csvWriter, vizAreaStartDate);		
		
		final double VIZ_TOP_MARGIN = pageHeight * 0.05;
		final double VIZ_BOTTOM_MARGIN = pageHeight * 0.33;
		vizAreaHeight = pageHeight - VIZ_BOTTOM_MARGIN - VIZ_TOP_MARGIN;

		final double VIZ_LEFT_MARGIN = pageWidth * 0.25;
		final double VIZ_RIGHT_MARGIN = pageWidth * .05;
		vizAreaWidth = pageWidth - VIZ_LEFT_MARGIN - VIZ_RIGHT_MARGIN;
		
		vizAreaLeftMargin = VIZ_LEFT_MARGIN;
		vizAreaBottomMargin = VIZ_BOTTOM_MARGIN;
		
		vizAreaTotalDays = getTotalDays(vizAreaStartDate, vizAreaEndDate);


		
		if (scaleToOnePage) {
			pages = new ArrayList<String>();
			vizAreaDeltaY = getTotalAmountPerDay(bars);
			String page = getVisualizationArea(bars);
			pages.add(page);
		} else {
			double topNDeltaY = getTopNDeltaYSum(bars, MAX_BARS_PER_PAGE);
			pages = new LinkedList<String>();
			List<List<PostScriptBar>> splitBars = splitBars(bars, MAX_BARS_PER_PAGE);
			for(List<PostScriptBar> pageBars : splitBars){
				vizAreaDeltaY = topNDeltaY;
				String page = getVisualizationArea(pageBars);
				pages.add(page);
			}
		}

		vizAreaDefinitions = new StringBuilder();
		vizAreaDefinitions.append(loadVisualizationDefinitions());
		
	}
	
	
	protected static String getBarsArea(List<PostScriptBar> bars){
		StringBuilder barsArea = new StringBuilder();
		
		double barMarginTotal = vizAreaHeight * 0.25;  // 25 percent of the graph height will distributed as spaces between bars
		double barSpacing = (barMarginTotal / (bars.size() + 2));  // there will be a margin at the top and bottom
		
		double usableYPoints = (vizAreaHeight - barMarginTotal);
		double usableXPoints = vizAreaWidth;
		
		
		double pointsPerDay = usableXPoints / vizAreaTotalDays;		
		double pointsPerY = usableYPoints / vizAreaDeltaY;
		
		// maybe the actual bars on the page will not require all the space.  In this case, space out the bars using bar spacing.
		double spaceUsedByBars = getTotalAmountPerDay(bars) * pointsPerY;
		
		if (spaceUsedByBars < usableYPoints){
			double extraSpace = usableYPoints - spaceUsedByBars;
			double extraSpacePerBar = extraSpace / (bars.size() + 2);  //allow for padding at top and bottom
			barSpacing += extraSpacePerBar;
		}		
		
		StringTemplate fontTemplate =
				group.getInstanceOf("visualizationLabelBarFont");
		fontTemplate.setAttribute("fontname", "Arial");
		fontTemplate.setAttribute("fontsize", Math.min(barSpacing, MAX_LABEL_FONT_SIZE));
		
		barsArea.append(fontTemplate.toString());
		
		
		double previousEndY = 0;
		for(PostScriptBar bar : bars){
			double startY = previousEndY + barSpacing;
			double changeInY = bar.amountPerDay() * pointsPerY;
			StringTemplate datelineTemplate =
					group.getInstanceOf("visualizationLabelBar");
			datelineTemplate.setAttribute("label", bar.getName());
			datelineTemplate.setAttribute("x1", bar.daysSinceEarliest() * pointsPerDay);
			datelineTemplate.setAttribute("y1", startY);
			datelineTemplate.setAttribute("deltaX", bar.lengthInDays() * pointsPerDay);
			datelineTemplate.setAttribute("deltaY", changeInY);

			barsArea.append(datelineTemplate.toString());

			previousEndY = startY + changeInY;
		}
		
		return barsArea.toString();
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
	
	protected static String getDateLinesArea(){
		StringBuilder datelineArea = new StringBuilder();
		List<String> dateLines = new LinkedList<String>();
		
		List<Date> newYearsDates = getNewYearsDates(vizAreaStartDate, vizAreaEndDate);
		
		if(newYearsDates.size() > MAX_LINEDATES){
			newYearsDates = reduceDates(newYearsDates, MAX_LINEDATES);
			
		}
		
		for(Date newYear : newYearsDates){
			
			double usableXPoints = vizAreaWidth;
			
			double pointsPerDay = usableXPoints / vizAreaTotalDays;
			
			String label = Integer.toString(newYear.getYear());
			double x = Math.abs(DateUtilities.calculateDaysBetween(vizAreaStartDate, newYear)) * pointsPerDay;
			
			StringTemplate datelineTemplate =
					group.getInstanceOf("visualizationDateLine");
			datelineTemplate.setAttribute("label", label);
			datelineTemplate.setAttribute("x1", x);
			
			dateLines.add(datelineTemplate.toString());
		}
		
		
		for(String date : dateLines){
			datelineArea.append(date);
		}
		
		return datelineArea.toString();
	}

	@Override
	protected String getVisualizationArea(List<PostScriptBar> bars) {
		
		StringBuilder page = new StringBuilder();		
		page.append(getSetup());
		page.append(getDateLinesArea());
		
		page.append(getBarsArea(bars));
		page.append(getTearDown());
		
		return page.toString();
	}

	private static String getTearDown() {
		StringTemplate tearDownTemplate =
				group.getInstanceOf("visualizationAreaTearDown");
		return tearDownTemplate.toString();
	}

	private static String getSetup() {
		StringTemplate setupTemplate =
				group.getInstanceOf("visualizationAreaSetup");
		setupTemplate.setAttribute("leftMargin", vizAreaLeftMargin);
		setupTemplate.setAttribute("bottomMargin", vizAreaBottomMargin);
		return setupTemplate.toString();
	}

	@Override
	public String getPostScriptVisualizationDefinitions() {
		return vizAreaDefinitions.toString();
	}

	@Override
	public List<String> getPages() {
		return pages;
	}
	
	
	/**
	 * This returns the postscript definitions needed for the VizArea.  This should be called after the VizArea has been set up so all the information needed is available.
	 * @return
	 */
	private static String loadVisualizationDefinitions(){
		StringTemplate definitionsTemplate =
				group.getInstanceOf("visualizationAreaDefinitions");
		definitionsTemplate.setAttribute("topVizPosition", vizAreaHeight);
		definitionsTemplate.setAttribute("leftVizMargin", vizAreaLeftMargin);
		return definitionsTemplate.toString();
	}
}
