package edu.iu.sci2.visualization.temporalbargraph.common;

import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.getRGB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.color.ColorRegistry;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.Days;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * This is the Visualization Area of a the postscript document for the web. It
 * includes things like the bars, labels for the bar, ticks for the dates, and
 * labels for those ticks.  It also manages things like font sizes and margins
 * 
 */
public class Visualization extends AbstractVisualization{

	private DateTime vizAreaStartDate;
	private DateTime vizAreaEndDate;
	
	private DoubleDimension size;
		
	private double vizAreaTotalDays;
	private double vizAreaDeltaY;
	
	private List<String> visualizations;
	
	public static StringTemplateGroup group = AbstractVisualization.group;
	
	
	public Visualization (CSVWriter csvWriter, List<Record> records, DoubleDimension size, boolean scaleToOnePage, ColorRegistry<String> colorRegistry) throws PostScriptCreationException{
		
		vizAreaStartDate = getFirstNewYearBeforeStartDate(records);
		vizAreaEndDate = getFirstNewYearAfterLastEndDate(records);	
		
		this.size = size;
		
		Collections.sort(records, Record.START_DATE_ORDERING);
		List<PostScriptBar> bars = createBars(records, csvWriter, vizAreaStartDate, colorRegistry);
		
		int totalDays = Days.daysBetween(vizAreaStartDate, vizAreaEndDate).getDays();
		if (totalDays == 0){
			throw new PostScriptCreationException("You must have atleast 1 day between start and end dates to visualize.");
		}		
		
		vizAreaTotalDays = totalDays;


		
		if (scaleToOnePage) {
			visualizations = new ArrayList<String>();
			vizAreaDeltaY = getTotalAmountPerDay(bars);
			String page = getVisualizationArea(bars);
			visualizations.add(page);
		} else {
			double topNDeltaY = getTopNDeltaYSum(bars, MAX_BARS_PER_PAGE);
			visualizations = new LinkedList<String>();
			List<List<PostScriptBar>> splitBars = splitBars(bars, MAX_BARS_PER_PAGE);
			for(List<PostScriptBar> pageBars : splitBars){
				vizAreaDeltaY = topNDeltaY;
				String page = getVisualizationArea(pageBars);
				visualizations.add(page);
			}
		}

		
		
	}
	
	
	protected String getBarsArea(List<PostScriptBar> bars){
		StringBuilder barsArea = new StringBuilder();
		
		double barMarginTotal = size.getHeight() * 0.25;  // 25 percent of the graph height will distributed as spaces between bars
		double barSpacing = (barMarginTotal / (bars.size() + 2));  // there will be a margin at the top and bottom
		
		double usableYPoints = (size.getHeight() - barMarginTotal);
		double usableXPoints = size.getWidth();
		
		
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
			StringTemplate visualizationLabelBar =
					group.getInstanceOf("visualizationLabelBar");
			visualizationLabelBar.setAttribute("label", bar.getName());
			visualizationLabelBar.setAttribute("x1", bar.daysSinceEarliest() * pointsPerDay);
			visualizationLabelBar.setAttribute("y1", startY);
			visualizationLabelBar.setAttribute("deltaX", bar.lengthInDays() * pointsPerDay);
			visualizationLabelBar.setAttribute("deltaY", changeInY);
			visualizationLabelBar.setAttribute("color", getRGB(bar.getColor()));

			barsArea.append(visualizationLabelBar.toString());

			previousEndY = startY + changeInY;
		}
		
		
		return barsArea.toString();
	}
		
	@SuppressWarnings("unchecked") // Raw types from DateTimeComparator#getInstance()
	protected String getDateLinesArea(){
		StringBuilder datelineArea = new StringBuilder();
		List<String> dateLines = new LinkedList<String>();
		
		List<DateTime> newYearsDates = getNewYearsDates(vizAreaStartDate, vizAreaEndDate);
		
		if(newYearsDates.size() > MAX_LINEDATES){
			newYearsDates = decimate(newYearsDates, DateTimeComparator.getInstance(), MAX_LINEDATES);
			
		}
		
		for(DateTime newYear : newYearsDates){
			
			double usableXPoints = size.getWidth();
			
			double pointsPerDay = usableXPoints / vizAreaTotalDays;
			
			String label = Integer.toString(newYear.toLocalDate().getYear());
			double x = Days.daysBetween(vizAreaStartDate, newYear).getDays() * pointsPerDay;
			
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
		return setupTemplate.toString();
	}


	@Override
	public String renderVisualizationPostscript(int visualizationNumber) {
		return visualizations.get(visualizationNumber);
	}


	@Override
	public int numberOfVisualizations() {
		return visualizations.size();
	}


	@Override
	public String renderDefinitionsPostscript() {
		StringTemplate definitionsTemplate =
				group.getInstanceOf("visualizationAreaDefinitions");
		definitionsTemplate.setAttribute("topVizPosition", size.getHeight());
		return definitionsTemplate.toString();
	}
}
