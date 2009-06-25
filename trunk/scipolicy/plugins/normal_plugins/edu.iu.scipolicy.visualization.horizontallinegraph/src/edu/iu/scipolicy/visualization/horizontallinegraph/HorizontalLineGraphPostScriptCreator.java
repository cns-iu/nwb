package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.scipolicy.utilities.DateUtilities;

/**
 * To my understanding, the drawing "canvas" is given a default size that is used
   as part of the scaling factor when forming the record bar lines.
 * In the original implementation of this visualization, this default size was large
   enough to fit all of the elements of the visualization, so it was also used as
   the bounding box for the Encapsulated PostScript.
 * Since we are dealing with larger datasets than the original implementation of
   this visualization intended, we actually have to dynamically calculate the
   bounding box size based on the generated PostScript elements (e.g. the record bar
   lines) so none of them are clipped.
 * Right now, the CALCULATED bounding box size is updated every time a record bar is
   generated, which is poor design because it's a side effect.  Eventually, record
   bars will probably be represented by objects of a class, and an array of them
   will be produced instead of the raw PostScript code for displaying them.  This
   array of record bars could then be used to calculate the bounding box size
   "properly".
 */
public class HorizontalLineGraphPostScriptCreator {
	String labelKey;
	String startDateKey;
	String endDateKey;
	String sizeByKey;
	
	private double calculatedBoundingBoxWidth = 0.0;
	private double calculatedBoundingBoxHeight = 0.0;
	
	public HorizontalLineGraphPostScriptCreator(String labelKey,
												String startDateKey,
												String endDateKey,
												String sizeByKey)
	{
		this.labelKey = labelKey;
		this.startDateKey = startDateKey;
		this.endDateKey = endDateKey;
		this.sizeByKey = sizeByKey;
	}

	public String createPostScript(Table table,
								   int minNumberOfDaysForBar,
								   LogService logger)
			throws PostScriptCreationException {
		// The canvas size, in inches.
		// (NOTE: These could be constants right now, but they may eventually be
		// user-settable.)
		final double canvasWidth = 11.7;
		final double canvasHeight = 4.75;
		
		// DPI stands for dots per inch, so it multiplied by the canvas size gives
		// how many dots there are on the canvas.
		final int resolutionInDPI = 300;
		
		// The default (encapsulated) bounding box dimenions.
		final double boundingBoxTop = 300.0;
		final double boundingBoxBottom = 300.0;
		final double boundingBoxLeft = 300.0;
		final double boundingBoxRight = 300.0;

		final double defaultBoundingBoxWidth =
			((canvasWidth * resolutionInDPI) - boundingBoxLeft - boundingBoxRight);
		final double defaultBoundingBoxHeight =
			((canvasHeight * resolutionInDPI) - boundingBoxTop - boundingBoxBottom);
		final double barMargin = 24.0;
		// (NOTE: This could be a constant, but we may eventually want it to be
		// user-settable?)
		final int startYPosition = 5;

		Record[] records;
		
		try {
			records = readRecordsFromTable(table, logger);
		}
		catch (ParseException parseException) {
			throw new PostScriptCreationException(parseException);
		}

		// No records?  Cya.
		if (records.length == 0) {
			throw new PostScriptCreationException
				("No records to create PostScript from!");
		}
		
		Record[] sortedRecords = sortRecords(records);
		
		// Sum the total (monitary) amount across all records.
		double totalAmount = calculateTotalAmount(sortedRecords);
		
		// Calculate the total height for all records (all of their heights
		// together?)  This is used to scale the records appropriately, I think.
		double totalHeightSpan =
			Math.abs(defaultBoundingBoxHeight -
					 (barMargin * (sortedRecords.length + 1)));
		
		// Get the first and last dates any of these records exist.
		Date graphStartDate = formGraphStartDateBasedOnRecords(sortedRecords);
		Date graphEndDate = formGraphEndDateBasedOnRecords(sortedRecords);
		
		// We need a Date for every New Year's Day (so, January 1st) between the
		// start and end dates to use for drawing the date dash lines.
		Date[] newYearsDatesForGraph =
			DateUtilities.generateNewYearsDatesBetweenDates(graphStartDate,
															graphEndDate);
		
		// (NOTE: This side effects the calculated canvas width and height.)
		final String postScriptRecordBars =
			formPostScriptRecordBars(sortedRecords,
									graphStartDate,
									graphEndDate,
									barMargin,
									totalAmount,
									totalHeightSpan,
									defaultBoundingBoxWidth,
									startYPosition);
		
		final String postScriptHeader =
			formPostScriptHeader(0,
								 barMargin,
								 defaultBoundingBoxWidth,
								 barMargin);
		
		final String postScriptYearLabels = 
			formPostScriptYearLabels(newYearsDatesForGraph,
									 graphStartDate,
									 graphEndDate,
									 defaultBoundingBoxWidth,
									 startYPosition,
									 barMargin);
		
		final String postScriptBackground = formPostScriptBackground();
		
		return postScriptHeader +
			   postScriptYearLabels +
			   postScriptRecordBars +
			   postScriptBackground;
	}
	
	private Record[] readRecordsFromTable(Table recordTable, LogService logger)
			throws ParseException {
		ArrayList workingRecordSet = new ArrayList();
		int numTableRows = recordTable.getRowCount();
		
		for (int ii = 0; ii < numTableRows; ii++) {
			try {
				Record newRecord = new Record(recordTable.getTuple(ii),
											  this.labelKey,
											  this.startDateKey,
											  this.endDateKey,
											  this.sizeByKey);
				
				workingRecordSet.add(newRecord);
			}
			catch (InvalidRecordException invalidRecordException) {
				logger.log(LogService.LOG_WARNING,
						   invalidRecordException.getMessage());
			}
		}
		
		Record[] recordSet = new Record [workingRecordSet.size()];
		recordSet = (Record[])workingRecordSet.toArray(recordSet);
		
		return recordSet;
	}
	
	private Record[] sortRecords(Record[] originalRecordSet) {
		final int numRecords = originalRecordSet.length;
		Record[] sortedRecordSet = new Record [numRecords];
		
		for (int ii = 0; ii < numRecords; ii++) {
			sortedRecordSet[ii] = originalRecordSet[ii];
		}
		
		Arrays.sort(sortedRecordSet);
		
		return sortedRecordSet;
	}
	
	private double calculateTotalAmount(Record[] records) {
		double calculatedTotalRecordAmount = 0.0;
		
		for (int ii = 0; ii < records.length; ii++)
			calculatedTotalRecordAmount += records[ii].getAmount();
		
		return calculatedTotalRecordAmount;
	}
	
	private double calculateRecordBarHeight(double dollarAmount,
								  		    double totalDollarAmount,
								  		    double recordHeight,
								  		    double scale)
	{
		double recordBarHeight =
			(recordHeight * scale * dollarAmount / totalDollarAmount);
		
		return recordBarHeight;
			
	}
	
	private double calculateXCoordinate(Date date,
									   Date startDate,
									   Date endDate,
									   double defaultBoundingBoxWidth,
									   double margin)
	{
		return ((DateUtilities.calculateDaysBetween(startDate, date) * defaultBoundingBoxWidth) /
			DateUtilities.calculateDaysBetween(startDate, endDate) + 1000);
	}
	
	private String formPostScriptHeader(double boundingBoxBottom,
										double boundingBogLeft,
										double defaultBoundingBoxWidth,
										double recordBarMargin)
	{
		String TICK_COLOR_STRING = "0.75 0.75 0.75";
		String RECORD_LABEL_COLOR_STRING = "0.4 0.4 0.4";
		String RECORD_BAR_COLOR_STRING = "0.0 0.0 0.0";
		
		return
			// TODO: The bounding box is a big fat hack. (It needs to take into
			// account text size and stuff.)
			line("%!PS-Adobe-2.0 EPSF-2.0") +
			line("%%BoundingBox:" + Math.round(boundingBogLeft) + " " +
				 Math.round(boundingBoxBottom) + " " +
				 Math.round(this.calculatedBoundingBoxWidth + 600) + " " +
				 Math.round(this.calculatedBoundingBoxHeight)) +
			// line("%%Pages: 1") +
			line("%%Title: Horizontal Line Graph") +
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
			
			line("/record {") +
				line(tabbed("5 -1 roll")) +
				line(tabbed("4 index 4 index 3 index 2 div add")) +
				line(tabbed(setrgbcolor(RECORD_LABEL_COLOR_STRING))) +
				line(tabbed("personlabel")) +
				line(tabbed(setrgbcolor(RECORD_BAR_COLOR_STRING))) +
				line(tabbed("period")) +
			line("} def") +
			line("") +
			
			line("/vertical {") +
				line(tabbed("gsave")) +
				line(tabbed("[15] 0 setdash")) +
				line(tabbed("1 setlinewidth")) +
				line(tabbed(setrgbcolor(TICK_COLOR_STRING))) +
				line(tabbed("2 index")) +
				line(tabbed("newpath")) +
				line(tabbed("exch")) +
				line(tabbed("moveto")) +
				line(tabbed("lineto")) +
				line(tabbed("stroke")) +
				line(tabbed("grestore")) +
			line("} def") +
			line("") +
			
			line("0.0039 0.4509 0.5843 setrgbcolor") +
			line("1.5 setlinewidth") +
			line("/Garamond findfont 25 scalefont setfont");
	}
	
	private String formPostScriptYearLabels(Date[] newYearsDates,
											Date graphStartDate,
											Date graphEndDate,
											double defaultBoundingBoxWidth,
											int startYPosition,
											double margin)
	{
		StringWriter yearLabelPostScript = new StringWriter();
		
		for (Date currentNewYearsDate : newYearsDates) {
			double xCoordinate = calculateXCoordinate(currentNewYearsDate,
													  graphStartDate,
													  graphEndDate,
													  defaultBoundingBoxWidth,
													  margin);
			
			yearLabelPostScript.append
				(line("0 setgray") +
				 line("(" + currentNewYearsDate.getYear() + ") " + 
						xCoordinate + " " + startYPosition + " ticklabel") +
				 line("" + xCoordinate + " " + startYPosition + " " +
					  this.calculatedBoundingBoxHeight + " vertical"));
		}
		
		return yearLabelPostScript.toString();
	}
	
	private String formPostScriptRecordBars(Record[] records,
										   Date graphStartDate,
										   Date graphEndDate,
										   double recordBarMargin,
										   double totalRecordAmount,
										   double recordHeight,
										   double defaultBoundingBoxWidth,
										   double startYPosition)
	{
		double cursorYCoordinate = startYPosition;
		StringWriter recordBarPostScript = new StringWriter();
		
		for (Record currentRecord : records) {
			String currentRecordName = currentRecord.getLabel();
			Date currentRecordStartDate = currentRecord.getStartDate();
			Date currentRecordEndDate = currentRecord.getEndDate();
			double currentRecordAmount = currentRecord.getAmount();
			
			double recordBarStartXCoordinate =
				calculateXCoordinate(currentRecordStartDate,
									 graphStartDate,
									 graphEndDate,
									 defaultBoundingBoxWidth,
									 recordBarMargin);
			
			double recordBarEndXCoordinate =
				calculateXCoordinate(currentRecordEndDate,
									 graphStartDate,
									 graphEndDate,
									 defaultBoundingBoxWidth,
									 recordBarMargin);
			
			double recordBarWidth =
				(recordBarEndXCoordinate - recordBarStartXCoordinate);
			
			double scale = (12.0 /
				DateUtilities.calculateMonthsBetween
					(currentRecordStartDate, currentRecordEndDate));
			
			double calculatedRecordBarHeight = calculateRecordBarHeight
				(currentRecordAmount, totalRecordAmount, recordHeight, scale);
			
			cursorYCoordinate += recordBarMargin;
			
			String recordString =
				line("(" + currentRecordName + ") " +
					 recordBarStartXCoordinate + " " +
					 cursorYCoordinate + " " +
					 recordBarWidth + " " +
					 calculatedRecordBarHeight +
					 " record");
			
			recordBarPostScript.append(recordString);
			
			cursorYCoordinate += calculatedRecordBarHeight;
			
			updateCanvasSize(recordBarEndXCoordinate,
							 (cursorYCoordinate + recordBarMargin));
		}
		
		return recordBarPostScript.toString();
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
	
	private String setrgbcolor(String str) {
		return str + " setrgbcolor";
	}
	
	private Date formGraphStartDateBasedOnRecords(Record[] records) {
		// Get the first record and its start date.
		Record firstRecord = records[0];
		Date firstRecordStartDate = firstRecord.getStartDate();
		// Form the GRAPH start date based on the first record's start date.
		Date graphStartDate = new Date((firstRecordStartDate.getYear() - 1), 0, 1);
		
		return graphStartDate;
	}
	
	private Date formGraphEndDateBasedOnRecords(Record[] records) {
		// Form the GRAPH end date based on the determined last year of the records.
		Date graphEndDate = new Date((determineLastYearOfRecords(records) + 1), 0, 1);
		
		return graphEndDate;
	}
	
	private int determineLastYearOfRecords(Record[] records) {
		int lastYear = 0;
		
		for (int ii = 0; ii < records.length; ii++) {
			Date recordEndDate = records[ii].getEndDate();
			int recordEndDateYear = recordEndDate.getYear();
			
			if (recordEndDateYear > lastYear)
				lastYear = recordEndDateYear;
		}
		
		return lastYear;
	}
	
	private void updateCanvasSize(double xCoordinate, double yCoordinate) {
		if (xCoordinate > this.calculatedBoundingBoxWidth)
			this.calculatedBoundingBoxWidth = xCoordinate;
		
		if (yCoordinate > this.calculatedBoundingBoxHeight)
			this.calculatedBoundingBoxHeight = yCoordinate;
	}
	
	
}