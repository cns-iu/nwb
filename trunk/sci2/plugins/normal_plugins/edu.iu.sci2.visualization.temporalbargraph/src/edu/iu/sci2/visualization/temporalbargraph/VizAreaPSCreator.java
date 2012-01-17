package edu.iu.sci2.visualization.temporalbargraph;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.cishell.utilities.DateUtilities;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractVizArea;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptBar;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;
import edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities;

public class VizAreaPSCreator  extends AbstractVizArea {
	private static final int MAX_BARS_PER_PAGE = 50;
	
	public static final int MAX_LABEL_FONT_SIZE = 16;
	public static final int LABEL_BAR_SPACING = 15;
	
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
	 * This will return "header" definitions that represent all the definitions needed by a visualization area 
	 * @return
	 */
	public String getPostScriptVisualizationDefinitions(){
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

	@Override
	protected String getVisualizationArea(List<PostScriptBar> bars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getPages() {
		// TODO Auto-generated method stub
		return pages;
	}
	
}
