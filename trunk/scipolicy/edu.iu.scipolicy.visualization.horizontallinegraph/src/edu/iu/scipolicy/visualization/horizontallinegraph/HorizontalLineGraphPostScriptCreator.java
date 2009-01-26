package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;

import prefuse.data.Table;
import edu.iu.scipolicy.utilities.DateUtilities;

/**
 * To my understanding, the drawing "canvas" is given a default size that is used
   as part of the scaling factor when forming the grant bar lines.
 * In the original implementation of this visualization, this default size was large
   enough to fit all of the elements of the visualization, so it was also used as
   the bounding box for the Encapsulated PostScript.
 * Since we are dealing with larger datasets than the original implementation of
   this visualization intended, we actually have to dynamically calculate the
   bounding box size based on the generated PostScript elements (e.g. the grant bar
   lines) so none of them are clipped.
 * Right now, the CALCULATED bounding box size is updated every time a grant bar is
   generated, which is poor design because it's a side effect.  Eventually, grant
   bars will probably be represented by objects of a class, and an array of them
   will be produced instead of the raw PostScript code for displaying them.  This
   array of grant bars could then be used to calculate the bounding box size
   "properly".
 */
public class HorizontalLineGraphPostScriptCreator {
	private float calculatedBoundingBoxWidth = 0.0f;
	private float calculatedBoundingBoxHeight = 0.0f;
	
	public HorizontalLineGraphPostScriptCreator() {
	}

	public String createPostScript(Table grantTable, int minNumberOfDaysForBar) 
		throws PostScriptCreationException
	{
		// The canvas size, in inches.
		// (NOTE: These could be constants right now, but they may eventually be
		// user-settable.)
		final float canvasWidth = 11.7f;
		final float canvasHeight = 4.75f;
		
		// DPI stands for dots per inch, so it multiplied by the canvas size gives
		// how many dots there are on the canvas.
		final int resolutionInDPI = 300;
		
		// The default (encapsulated) bounding box dimenions.
		final float boundingBoxTop = 300.0f;
		final float boundingBoxBottom = 300.0f;
		final float boundingBoxLeft = 300.0f;
		final float boundingBoxRight = 300.0f;

		final float defaultBoundingBoxWidth =
			((canvasWidth * resolutionInDPI) - boundingBoxLeft - boundingBoxRight);
		final float defaultBoundingBoxHeight =
			((canvasHeight * resolutionInDPI) - boundingBoxTop - boundingBoxBottom);
		final float grantBarMargin = 10.0f;
		// (NOTE: This could be a constant, but we may eventually want it to be
		// user-settable?)
		final int yearLabelYPosition = 5;

		Grant[] grants = readGrantsFromTable(grantTable);

		// No grants?  Cya.
		if (grants.length == 0) {
			throw new PostScriptCreationException
				("No grants to create PostScript from!");
		}
		
		Grant[] sortedGrants = sortGrants(grants);
		
		// Sum the total (monitary) amount across all grants.
		float totalGrantMoney = calculateTotalGrantAmount(sortedGrants);
		
		// Calculate the total height for all grants (all of their heights
		// together?)  This is used to scale the grants appropriately, I think.
		float totalGrantHeightSpan =
			Math.abs(defaultBoundingBoxHeight - (grantBarMargin * (sortedGrants.length + 1)));
		
		// Get the first and last dates any of these grants exist.
		Date graphStartDate = formGraphStartDateBasedOnGrants(sortedGrants);
		Date graphEndDate = formGraphEndDateBasedOnGrants(sortedGrants);
		
		// We need a Date for every New Year's Day (so, January 1st) between the
		// start and end dates to use for drawing the date dash lines.
		Date[] newYearsDatesForGraph =
			DateUtilities.generateNewYearsDatesBetweenDates(graphStartDate,
															graphEndDate);
		
		// (NOTE: This side effects the calculated canvas width and height.)
		final String postScriptGrantBars =
			formPostScriptGrantBars(sortedGrants,
									graphStartDate,
									graphEndDate,
									grantBarMargin,
									totalGrantMoney,
									totalGrantHeightSpan,
									defaultBoundingBoxWidth,
									yearLabelYPosition);
		
		final String postScriptHeader =
			formPostScriptHeader(0,
								 0,
								 boundingBoxRight,
								 defaultBoundingBoxWidth);
		
		final String postScriptYearLabels = 
			formPostScriptYearLabels(newYearsDatesForGraph,
									 graphStartDate,
									 graphEndDate,
									 defaultBoundingBoxWidth,
									 yearLabelYPosition);
		
		final String postScriptBackground = formPostScriptBackground();
		
		return postScriptHeader +
			   postScriptYearLabels +
			   postScriptGrantBars +
			   postScriptBackground;
	}
	
	private Grant[] readGrantsFromTable(Table grantTable)
	{
		// Get the number of rows from the table so we know how many elements to
		// allocate our working/resulting grant set for.
		int numGrants = grantTable.getRowCount();
		// Allocate our working/resulting grant set.
		Grant[] grantSet = new Grant [numGrants];
		
		for (int ii = 0; ii < numGrants; ii++)
			grantSet[ii] = new Grant(grantTable.getTuple(ii));
		
		return grantSet;
	}
	
	private Grant[] sortGrants(Grant[] originalGrantSet) {
		final int numGrants = originalGrantSet.length;
		Grant[] sortedGrantSet = new Grant [numGrants];
		
		for (int ii = 0; ii < numGrants; ii++) {
			sortedGrantSet[ii] = originalGrantSet[ii];
		}
		
		Arrays.sort(sortedGrantSet);
		
		return sortedGrantSet;
	}
	
	private float calculateTotalGrantAmount(Grant[] grants) {
		float calculatedTotalGrantAmount = 0.0f;
		
		for (int ii = 0; ii < grants.length; ii++)
			calculatedTotalGrantAmount += grants[ii].getAmount();
		
		return calculatedTotalGrantAmount;
	}
	
	private float calculateGrantBarHeight(float dollarAmount,
								  float totalDollarAmount,
								  float grantHeight) {
		return grantHeight * dollarAmount / totalDollarAmount;
	}
	
	private float calculateXCoordinate(Date date,
									   Date startDate,
									   Date endDate,
									   float wide)
	{
		return (DateUtilities.calculateDaysBetween(startDate, date) * wide) /
			DateUtilities.calculateDaysBetween(startDate, endDate);
	}
	
	private String formPostScriptHeader(float boundingBoxBottom,
										float boundingBogLeft,
										float boundingBoxRight,
										float defaultBoundingBoxWidth)
	{
		return
			line("%!PS-Adobe-2.0 EPSF-2.0") +
			line("%%BoundingBox:" + boundingBogLeft + " " + boundingBoxBottom +
				 " " + this.calculatedBoundingBoxWidth + " " +
				 this.calculatedBoundingBoxHeight) +
			line("%%Pages: 1") +
			line("%%Title: Horizontal Line Graph (NSF Grant Data)") +
			line("%%Creator: SciPolicy") +
			line("%%EndComments") +
			line("") +
			
			line("/tick {") +
				line(tabbed("newpath")) +
				line(tabbed("moveto")) +
				line(tabbed("0 -25 rlineto")) +
				line(tabbed("stroke")) +
			line("} def") +
			line("") +
			
			line("/verticaltick {") +
				line(tabbed("newpath")) +
				line(tabbed("moveto")) +
				line(tabbed("10 0 rlineto")) +
				line(tabbed("stroke")) +
			line("} def") +
			line("") +
			
			line("/fontheightadjust {") +
				line(tabbed("0 -3 rmoveto")) +
			line("} def") +
			line("") +
			
			line("/ticklabel {") +
				line(tabbed("gsave")) +
				line(tabbed("moveto")) +
				line(tabbed("dup stringwidth pop 2 div neg 0 rmoveto")) +
				line(tabbed("fontheightadjust")) +
				line(tabbed("show")) +
				line(tabbed("grestore")) +
			line("} def") +
			line("") +
			
			line("/personlabel {") +
				line(tabbed("moveto")) +
				line(tabbed("dup stringwidth pop neg 15 sub 0 rmoveto")) +
				line(tabbed("fontheightadjust")) +
				line(tabbed("show")) +
			line("} def") +
			line("") +
			
			line("/keyitem {") +
				line(tabbed("1 index 1 index")) +
				line(tabbed("newpath")) +
				line(tabbed("300 0 rlineto")) +
				line(tabbed("stroke")) +
				line(tabbed("moveto")) +
				line(tabbed("350 0 rmoveto")) +
				line(tabbed("fontheightadjust")) +
				line(tabbed("show")) +
			line("} def") +
			line("") +
			
			line("/period {") +
				line(tabbed("newpath")) +
				line(tabbed("4 2 roll moveto")) +
				line(tabbed("exch dup 0 rlineto 0 3 -1 roll")) +
				line(tabbed("rlineto neg 0 rlineto")) +
				line(tabbed("fill")) +
			line("} def") +
			line("") +
			
			line("/grant {") +
				line(tabbed("5 -1 roll")) +
				line(tabbed("4 index 4 index 3 index 2 div add")) +
				line(tabbed("personlabel")) +
				line(tabbed("period")) +
			line("} def") +
			line("") +
			
			line("/vertical {") +
				line(tabbed("gsave")) +
				line(tabbed("[15] 0 setdash")) +
				line(tabbed("1 setlinewidth")) +
				line(tabbed(".5 setgray")) +
				line(tabbed("2 index")) +
				line(tabbed("newpath")) +
				line(tabbed("exch")) +
				line(tabbed("moveto")) +
				line(tabbed("lineto")) +
				line(tabbed("stroke")) +
				line(tabbed("grestore")) +
			line("} def") +
			line("") +
			
			line("0 setgray") +
			line("1.5 setlinewidth") +
			line("/Helvetica findfont 12 scalefont setfont");
	}
	
	private String formPostScriptYearLabels(Date[] newYearsDates,
											Date graphStartDate,
											Date graphEndDate,
											float defaultBoundingBoxWidth,
											int yearLabelYPosition)
	{
		StringWriter yearLabelPostScript = new StringWriter();
		
		for (Date currentNewYearsDate : newYearsDates) {
			float xCoordinate = calculateXCoordinate(currentNewYearsDate,
													 graphStartDate,
													 graphEndDate,
													 defaultBoundingBoxWidth);
			
			yearLabelPostScript.append
				(line("(" + currentNewYearsDate.getYear() + ") " + 
						xCoordinate + " " + yearLabelYPosition + " ticklabel") +
				 line("" + xCoordinate + " " + yearLabelYPosition + " " +
					  this.calculatedBoundingBoxHeight + " vertical"));
		}
		
		return yearLabelPostScript.toString();
	}
	
	private String formPostScriptGrantBars(Grant[] grants,
										   Date graphStartDate,
										   Date graphEndDate,
										   float grantBarMargin,
										   float totalGrantAmount,
										   float grantHeight,
										   float defaultBoundingBoxWidth,
										   float startYPosition)
	{
		float cursorYCoordinate = startYPosition;
		StringWriter grantBarPostScript = new StringWriter();
		
		for (Grant currentGrant : grants) {
			String currentGrantName = currentGrant.getGrantLabel();
			Date currentGrantStartDate = currentGrant.getStartDate();
			Date currentGrantEndDate = currentGrant.getEndDate();
			float currentGrantAmount = currentGrant.getAmount();
			
			float calculatedGrantBarHeight = calculateGrantBarHeight
				(currentGrantAmount, totalGrantAmount, grantHeight);
			
			float grantBarStartXCoordinate =
				calculateXCoordinate(currentGrantStartDate,
									 graphStartDate,
									 graphEndDate,
									 defaultBoundingBoxWidth);
			
			float grantBarEndXCoordinate = calculateXCoordinate(currentGrantEndDate,
																graphStartDate,
																graphEndDate,
																defaultBoundingBoxWidth);
			
			float grantBarWidth = (grantBarEndXCoordinate - grantBarStartXCoordinate);
			
			cursorYCoordinate += grantBarMargin;
			
			grantBarPostScript.append
				(line("(" + currentGrantName + ") " + grantBarStartXCoordinate +
					  " " + cursorYCoordinate + " " + grantBarWidth + " " +
					  calculatedGrantBarHeight + " grant"));
			
			cursorYCoordinate += calculatedGrantBarHeight;
			
			updateCanvasSize(grantBarEndXCoordinate,
							 (cursorYCoordinate + grantBarMargin));
		}
		
		return grantBarPostScript.toString();
	}
	
	// TODO: Give this a more accurate name?  I'm not sure if this is actually
	// changing the background.
	private String formPostScriptBackground() {
		return line("0 0 1 setrgbcolor");
	}
	
	private String line(String str) {
		return str + "\r\n";
	}
	
	private String tabbed(String str) {
		return "\t" + str;
	}
	
	private Date formGraphStartDateBasedOnGrants(Grant[] grants) {
		// Get the first grant and its start date.
		Grant firstGrant = grants[0];
		Date firstGrantStartDate = firstGrant.getStartDate();
		// Form the GRAPH start date based on the first grant's start date.
		Date graphStartDate = new Date((firstGrantStartDate.getYear() - 1), 0, 1);
		
		return graphStartDate;
	}
	
	private Date formGraphEndDateBasedOnGrants(Grant[] grants) {
		// Form the GRAPH end date based on the determined last year of the grants.
		Date graphEndDate = new Date((determineLastYearOfGrants(grants) + 1), 0, 1);
		
		return graphEndDate;
	}
	
	private int determineLastYearOfGrants(Grant[] grants) {
		int lastYear = 0;
		
		for (int ii = 0; ii < grants.length; ii++) {
			Date grantEndDate = grants[ii].getEndDate();
			int grantEndDateYear = grantEndDate.getYear();
			
			if (grantEndDateYear > lastYear)
				lastYear = grantEndDateYear;
		}
		
		return lastYear;
	}
	
	private void updateCanvasSize(float xCoordinate, float yCoordinate) {
		if (xCoordinate > this.calculatedBoundingBoxWidth)
			this.calculatedBoundingBoxWidth = xCoordinate;
		
		if (yCoordinate > this.calculatedBoundingBoxHeight)
			this.calculatedBoundingBoxHeight = yCoordinate;
	}
	
	
}