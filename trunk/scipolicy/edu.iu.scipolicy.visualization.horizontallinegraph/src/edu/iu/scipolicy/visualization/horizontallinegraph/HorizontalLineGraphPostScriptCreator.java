package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.io.StringWriter;
import java.util.Date;

import edu.iu.scipolicy.utilities.DateUtilities;

public class HorizontalLineGraphPostScriptCreator {
	public HorizontalLineGraphPostScriptCreator() {
	}
	
	/* TODO: Year labels and grant bar lines are NOT getting offset correctly if the
	   start date is NOT January 1st of any year.
	 * As per Russell's original code, the year line/label for the starting YEAR of
	   all of the grants was NOT getting drawn, which is most likely a bug.  As a
	   work-around, the method that generates the New Year's dates-to-be-drawn
	   (which is DateUtilities.generateNewYearsDatesBetweenDates) adds the New
	   Year's date by default.
	 * Using that first (possibly defaultly-generated) New Year's date as the
	   starting date may just be the easiest solution.
	 * The user could still specify a full start date, which would be used to
	   exclude grants, but it should be notably different than the GRAPH start date. 
	 */
	/* TODO: The EPS bounding box should be calculated based off of what is drawn,
	   not preset?
	 */
	/* TODO: The scaling is off?
	 */
	public String createPostScript
		(Table grantTable,
		 Date grantStartDate,
		 Date grantEndDate,
		 int minNumberOfDaysForBar) throws PostScriptCreationException
	{
		// The canvas size, in inches.
		final float canvasWidth = 11.7f;
		final float canvasHeight = 4.75f;
		
		// DPI stands for dots per inch, so it multiplied by the canvas size gives
		// how many dots there are on the canvas.
		final int resolutionInDPI = 300;
		
		// The (encapsulated) bounding box dimenions.  (Note: These need to be
		// calculated based off the drawn contents?)
		final float boundingBoxTop = 300.0f;
		final float boundingBoxBottom = 300.0f;
		final float boundingBoxLeft = 300.0f;
		final float boundingBoxRight = 300.0f;

		final float scaledCanvasWidth =
			((canvasWidth * resolutionInDPI) - boundingBoxLeft - boundingBoxRight);
		final float scaledCanvasHeight =
			((canvasHeight * resolutionInDPI) - boundingBoxTop - boundingBoxBottom);
		final float grantBarMargin = 10.0f;
		final int tickHeight = -5;
		
		// Create the grants from the table.
		Grant[] grants = null;
		
		try {
			grants = readGrantsFromTable(grantTable);
		}
		catch (GrantCreationException e) {
			throw new PostScriptCreationException(e);
		}
		
		// No grants?  Cya.
		if (grants.length == 0) {
			throw new PostScriptCreationException
				("No grants to create PostScript from!");
		}
		
		// Sort the grants.
		Grant[] sortedGrants = sortGrants(grants);
		
		// Sum the total (monitary) amount across all grants.
		float totalGrantAmount = calculateTotalGrantAmount(grants);
		
		// Calculate the total height for all grants (all of their heights
		// together?)  This is used to scale the grants appropriately, I think.
		float grantHeight =
			(scaledCanvasHeight - (grantBarMargin * (sortedGrants.length + 1)));
		
		// Used to put the formed PostScript in to.
		StringWriter postScriptInProgress = new StringWriter();
		
		// Generate dates.
		Date[] allDatesBetween =
			DateUtilities.generateDaysBetweenDates(grantStartDate, grantEndDate);
		Date[] newYearsDatesForGraph =
			DateUtilities.generateNewYearsDatesBetweenDates(allDatesBetween);
		
		// Start and end dates for the graph (based off of the grant start and end
		// dates).
		Date graphStartDate = newYearsDatesForGraph[0];
		Date graphEndDate = newYearsDatesForGraph[newYearsDatesForGraph.length - 1];
		
		// Form the PostScript header.
		postScriptInProgress.append(formPostScriptHeader(boundingBoxTop,
														 boundingBoxBottom,
														 boundingBoxLeft,
														 boundingBoxRight,
														 scaledCanvasWidth,
														 scaledCanvasHeight));
		
		// Form the PostScript year labels (for the graph).
		postScriptInProgress.append(formPostScriptYearLabels(newYearsDatesForGraph,
															 graphStartDate,
															 graphEndDate,
															 scaledCanvasWidth,
															 tickHeight,
															 grantHeight));
		
		// Form the actual PostScript grant bars.
		postScriptInProgress.append(formPostScriptGrantBars(grants,
															graphStartDate,
															graphEndDate,
															grantBarMargin,
															totalGrantAmount,
															grantHeight,
															scaledCanvasWidth));
		
		// Form the PostScript background setup.
		postScriptInProgress.append(formPostScriptBackground());
		
		// Form the PostScript vertical tick marks.
		// (No, actually, don't.)
		
		return postScriptInProgress.toString();
	}
	
	private Grant[] readGrantsFromTable(Table grantTable)
			throws GrantCreationException
	{
		// Get the number of rows from the table so we know how many elements to
		// allocate our working/resulting grant set for.
		int numGrants = grantTable.getNumRowsLeft();
		// Allocate our working/resulting grant set.
		Grant[] grantSet = new Grant [numGrants];
		
		try {
			for (int ii = 0; ii < numGrants; ii++) {
				grantSet[ii] = new Grant(grantTable.getNextRow());
			}
		}
		catch (NoMoreRowsException e) {
			throw new GrantCreationException
				("This exception should NEVER be thrown.", e);
		}
		
		return grantSet;
	}
	
	private Grant[] sortGrants(Grant[] originalGrantSet) {
		// TODO: Actually sort them into a new array.
		final int numGrants = originalGrantSet.length;
		Grant[] sortedGrantSet = new Grant [numGrants];
		
		for (int ii = 0; ii < numGrants; ii++) {
			sortedGrantSet[ii] = originalGrantSet[ii];
		}
		
		return sortedGrantSet;
	}
	
	private float calculateTotalGrantAmount(Grant[] grants) {
		float calculatedTotalGrantAmount = 0.0f;
		
		for (int ii = 0; ii < grants.length; ii++)
			calculatedTotalGrantAmount += grants[ii].getAmount();
		
		return calculatedTotalGrantAmount;
	}
	
	// TODO: Eventually make this do something?
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
	
	private String formPostScriptHeader(float boundingBoxTop,
										float boundingBoxBottom,
										float boundingBogLeft,
										float boundingBoxRight,
										float scaledCanvasWidth,
										float scaledCanvasHeight)
	{
		return
			line("%!PS-Adobe-2.0 EPSF-2.0") +
			line("%%BoundingBox:-" + boundingBogLeft + " -" + boundingBoxBottom +
				 " " + (scaledCanvasWidth + boundingBoxRight) + " " +
				 (scaledCanvasHeight + boundingBoxTop)) +
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
											float scaledCanvasWidth,
											int tickHeight,
											float grantHeight)
	{
		StringWriter postScriptStringWriter = new StringWriter();
		
		for (int ii = 0; ii < newYearsDates.length; ii++) {
			Date currentNewYearsDate = newYearsDates[ii];
			
			float xCoordinate = calculateXCoordinate(currentNewYearsDate,
													 graphStartDate,
													 graphEndDate,
													 scaledCanvasWidth);
			
			postScriptStringWriter.append
				(line("(" + currentNewYearsDate.getYear() + ") " + xCoordinate +
					  " " + tickHeight + " ticklabel") +
				 line("" + xCoordinate + " " + tickHeight + " " + grantHeight +
					  " vertical"));
		}
		
		return postScriptStringWriter.toString();
	}
	
	private String formPostScriptGrantBars(Grant[] grants,
										   Date graphStartDate,
										   Date graphEndDate,
										   float grantBarMargin,
										   float totalGrantAmount,
										   float grantHeight,
										   float scaledCanvasWidth)
	{
		final int numGrants = grants.length;
		float cursorYCoordinate = 0.0f;
		StringWriter postScriptStringWriter = new StringWriter();
		
		for (int ii = 0; ii < numGrants; ii++) {
			Grant currentGrant = grants[ii];
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
									 scaledCanvasWidth);
			
			float grantBarEndXCoordinate = calculateXCoordinate(currentGrantEndDate,
																graphStartDate,
																graphEndDate,
																scaledCanvasWidth);
			
			float grantBarWidth = (grantBarEndXCoordinate - grantBarStartXCoordinate);
			
			cursorYCoordinate += grantBarMargin;
			
			postScriptStringWriter.append
				(line("(" + currentGrantName + ") " + grantBarStartXCoordinate +
					  " " + cursorYCoordinate + " " + grantBarWidth + " " +
					  calculatedGrantBarHeight + " grant"));
			
			cursorYCoordinate += calculatedGrantBarHeight;
		}
		
		return postScriptStringWriter.toString();
	}
	
	private String formPostScriptBackground() {
		return line("0 0 1 setrgbcolor");
	}
	
	private String line(String str) {
		return str + "\r\n";
	}
	
	private String tabbed(String str) {
		return "\t" + str;
	}
}