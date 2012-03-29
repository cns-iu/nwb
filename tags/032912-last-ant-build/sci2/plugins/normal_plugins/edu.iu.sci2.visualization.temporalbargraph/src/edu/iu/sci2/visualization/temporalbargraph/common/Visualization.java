package edu.iu.sci2.visualization.temporalbargraph.common;

import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.getRGB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.utilities.color.ColorRegistry;
import org.joda.time.DateTime;
import org.joda.time.Days;

import au.com.bytecode.opencsv.CSVWriter;

import com.google.common.base.Preconditions;

/**
 * This is the Visualization Area of a the postscript document for the web. It
 * includes things like the bars, labels for the bar, ticks for the dates, and
 * labels for those ticks. It also manages things like font sizes and margins
 * 
 */
public class Visualization extends AbstractVisualization {

	private List<DateTime> yearMarkers;


	private DoubleDimension size;
	
	private double vizAreaTotalDays;
	private double vizAreaDeltaY;
	
	private double usableXPoints, usableYPoints;
	private double pointsPerDay, pointsPerY;
	private double barMarginTotal;

	private List<String> visualizations;
	private List<Record> records;


	public double getPointsPerY() {
		return this.pointsPerY;
	}
	
	public double getPointsPerDay() {
		return this.pointsPerDay;
	}
	
	public Visualization(CSVWriter csvWriter, List<Record> records,
			DoubleDimension size, boolean scaleToOnePage,
			ColorRegistry<String> colorRegistry) {
		Preconditions.checkNotNull(csvWriter);
		Preconditions.checkNotNull(records);
		Preconditions.checkNotNull(size);
		Preconditions.checkNotNull(scaleToOnePage);
		Preconditions.checkNotNull(colorRegistry);
		Preconditions.checkArgument(records.size() > 0, "You must have atleast one record to visualize!");
		Preconditions
				.checkArgument(
						Days.daysBetween(
								Collections.min(records,
										Record.START_DATE_ORDERING)
										.getStartDate(),
								Collections.max(records,
										Record.END_DATE_ORDERING).getEndDate())
								.getDays() > 1,
						"You must have atleast 1 day between start and end dates to visualize!");
		
		this.records = records;
		this.yearMarkers = getYearTicks(Collections.min(records, Record.START_DATE_ORDERING).getStartDate(), Collections.max(records, Record.END_DATE_ORDERING).getEndDate(), MAX_LINEDATES);
		this.size = size;

		Collections.sort(records, Record.START_DATE_ORDERING);
		List<PostScriptBar> bars = createBars(records, csvWriter, Collections.min(this.yearMarkers), colorRegistry);
		
		int totalDays = Days.daysBetween(Collections.min(this.yearMarkers), Collections.max(this.yearMarkers)).getDays();

		this.vizAreaTotalDays = totalDays;

		/*
		 * 25 percent of the graph height will distributed as spaces between
		 * bars
		 */
		this.barMarginTotal = this.size.getHeight() * 0.25;
				
		this.usableXPoints = this.size.getWidth();
		this.usableYPoints = (this.size.getHeight() - this.barMarginTotal);
		this.pointsPerDay = this.usableXPoints / this.vizAreaTotalDays;

		if (scaleToOnePage) {
			this.visualizations = new ArrayList<String>();
			this.vizAreaDeltaY = getTotalAmountPerDay(bars);
			this.pointsPerY = this.usableYPoints / this.vizAreaDeltaY;
			String page = getVisualizationArea(bars);
			this.visualizations.add(page);
		} else {
			double topNDeltaY = getTopNDeltaYSum(bars, MAX_BARS_PER_PAGE);
			this.vizAreaDeltaY = topNDeltaY;
			this.pointsPerY = this.usableYPoints / this.vizAreaDeltaY;
			this.visualizations = new LinkedList<String>();
			
			List<List<PostScriptBar>> splitBars = splitBars(bars,
					MAX_BARS_PER_PAGE);
			
			for (List<PostScriptBar> pageBars : splitBars) {
				String page = getVisualizationArea(pageBars);
				this.visualizations.add(page);
			}
		}

	}

	protected String getBarsArea(List<PostScriptBar> bars) {
		StringBuilder barsArea = new StringBuilder();

		/*
		 * there will be a margin at the top and bottom
		 */
		double barSpacing = (this.barMarginTotal / (bars.size() + 2)); 

		// maybe the actual bars on the page will not require all the space. In
		// this case, space out the bars using bar spacing.
		double spaceUsedByBars = getTotalAmountPerDay(bars) * this.pointsPerY;

		if (spaceUsedByBars < this.usableYPoints) {
			double extraSpace = this.usableYPoints - spaceUsedByBars;
			// allow for padding at top and bottom
			double extraSpacePerBar = extraSpace / (bars.size() + 2); 
			barSpacing += extraSpacePerBar;
		}

		StringTemplate fontTemplate = group
				.getInstanceOf("visualizationLabelBarFont");
		fontTemplate.setAttribute("fontname", "ArialMT");
		fontTemplate.setAttribute("fontsize",
				Math.min(barSpacing, MAX_LABEL_FONT_SIZE));

		barsArea.append(fontTemplate.toString());

		double previousEndY = 0;
		for (PostScriptBar bar : bars) {
			double startY = previousEndY + barSpacing;
			double changeInY = bar.amountPerDay() * this.pointsPerY;
			StringTemplate visualizationLabelBar = group
					.getInstanceOf("visualizationLabelBar");
			visualizationLabelBar.setAttribute("label", bar.getName());
			visualizationLabelBar.setAttribute("x1", bar.daysSinceEarliest()
					* this.pointsPerDay);
			visualizationLabelBar.setAttribute("y1", startY);
			visualizationLabelBar.setAttribute("deltaX", bar.lengthInDays()
					* this.pointsPerDay);
			visualizationLabelBar.setAttribute("deltaY", changeInY);
			visualizationLabelBar.setAttribute("color", getRGB(bar.getColor()));

			barsArea.append(visualizationLabelBar.toString());

			previousEndY = startY + changeInY;
		}

		return barsArea.toString();
	}

	protected String getDateLinesArea(){
		StringBuilder datelineArea = new StringBuilder();
		List<String> dateLines = new LinkedList<String>();
		
		for (DateTime newYear : this.yearMarkers) {

			String label = Integer.toString(newYear.toLocalDate().getYear());
			double x = Days.daysBetween(Collections.min(this.yearMarkers), newYear).getDays() * this.pointsPerDay;
			
			StringTemplate datelineTemplate =
					group.getInstanceOf("visualizationDateLine");
			datelineTemplate.setAttribute("label", label);
			datelineTemplate.setAttribute("x1", x);

			dateLines.add(datelineTemplate.toString());
		}

		for (String date : dateLines) {
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
		StringTemplate tearDownTemplate = group
				.getInstanceOf("visualizationAreaTearDown");
		return tearDownTemplate.toString();
	}

	private static String getSetup() {
		StringTemplate setupTemplate = group
				.getInstanceOf("visualizationAreaSetup");
		return setupTemplate.toString();
	}

	@Override
	public String renderVisualizationPostscript(int visualizationNumber) {
		return this.visualizations.get(visualizationNumber);
	}

	@Override
	public int numberOfVisualizations() {
		return this.visualizations.size();
	}

	@Override
	public String renderDefinitionsPostscript() {
		StringTemplate definitionsTemplate = group
				.getInstanceOf("visualizationAreaDefinitions");
		definitionsTemplate.setAttribute("topVizPosition", this.size.getHeight());
		definitionsTemplate.setAttribute("pointsPerDay", this.pointsPerDay);
		definitionsTemplate.setAttribute("pointsPerY", this.pointsPerY);
		return definitionsTemplate.toString();
	}

	@Override
	public double minRecordValue() {
		return Record.AMOUNT_ORDERING.min(this.records).getAmount();
	}

	@Override
	public double maxRecordValue() {
		return Record.AMOUNT_ORDERING.max(this.records).getAmount();
	}
}
