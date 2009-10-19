package edu.iu.scipolicy.visualization.horizontallinegraph;

import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.cishell.utilities.DateUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.scipolicy.visualization.horizontallinegraph.utilities.CalculationUtilities;
import edu.iu.scipolicy.visualization.horizontallinegraph.utilities.PostScriptFormationUtilities;

/**
 * To my understanding, the drawing "canvas" is given a default size that is
 *  used as part of the scaling factor when forming the record bar lines.
 * In the original implementation of this visualization, this default size was
 *  large enough to fit all of the elements of the visualization, so it was
 *  also used as the bounding box for the Encapsulated PostScript.
 * Since we are dealing with larger datasets than the original implementation
 *  of this visualization intended, we actually have to dynamically calculate
 *  the bounding box size based on the generated PostScript elements
 *  (e.g. the record bar lines) so none of them are clipped.
 * Right now, the CALCULATED bounding box size is updated every time a record
 *  bar is generated, which is poor design because it's a side effect.
 * TODO Eventually, record bars will probably be represented by objects of a class,
 *  and an array of them will be produced instead of the raw PostScript code
 *  for displaying them.  This array of record bars could then be used to
 *  calculate the bounding box size "properly".
 */
public class HorizontalLineGraphPostScriptCreator {
	public static final int FAKE_BOUNDING_BOX_WIDTH_HACK = 600;
	public static final double MARGIN_WIDTH_FACTOR = 0.10;
	public static final double MARGIN_HEIGHT_FACTOR = 0.10;
	public static final double SPACING_ABOVE_X_AXIS_FACTOR = 0.05;
	
	private String labelKey;
	private String startDateKey;
	private String endDateKey;
	private String sizeByKey;
	private String startDateFormat;
	private String endDateFormat;
	private double pageWidth;
    private double pageHeight;
    private boolean shouldScaleOutput;
	
	private double calculatedBoundingBoxWidth = 0.0;
	private double calculatedBoundingBoxHeight = 0.0;
	
	public HorizontalLineGraphPostScriptCreator(
			String labelKey,
			String startDateKey,
			String endDateKey,
			String sizeByKey,
			String startDateFormat,
			String endDateFormat,
			double pageWidth,
			double pageHeight,
			boolean shouldScaleOutput)
	{
		this.labelKey = labelKey;
		this.startDateKey = startDateKey;
		this.endDateKey = endDateKey;
		this.sizeByKey = sizeByKey;
		this.startDateFormat = startDateFormat;
		this.endDateFormat = endDateFormat;
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;
		this.shouldScaleOutput = shouldScaleOutput;
	}

	public String createPostScript(
			Table table, int minNumberOfDaysForBar, LogService logger)
			throws PostScriptCreationException {
		/*
		 * The canvas size, in inches.
		 * (NOTE: These could be constants right now, but they may eventually
		 *  be user-settable.)
		 */
		final double canvasWidth = 11.7;
		final double canvasHeight = 4.75;
		
		/*
		 * DPI stands for dots per inch, so it multiplied by the canvas
		 *  size gives how many dots there are on the canvas.
		 */
		final int resolutionInDPI = 300;
		
		// The default (encapsulated) bounding box dimenions.
		final double boundingBoxTop = 300.0;
		final double boundingBoxBottom = 300.0;
		final double boundingBoxLeft = 300.0;
		final double boundingBoxRight = 300.0;

		final double defaultBoundingBoxWidth =
			((canvasWidth * resolutionInDPI) -
			 boundingBoxLeft -
			 boundingBoxRight);
		final double defaultBoundingBoxHeight =
			((canvasHeight * resolutionInDPI) -
			 boundingBoxTop -
			 boundingBoxBottom);
		final double barMargin = 24.0;
		/*
		 * (NOTE: This could be a constant, but we may eventually want it to be
		 *  user-settable?)
		 */
		final int startYPosition = 5;

		Record[] records;
		
		try {
			records = readRecordsFromTable(table, logger);
		} catch (ParseException parseException) {
			throw new PostScriptCreationException(parseException);
		}

		// No records?  Cya.
		if (records.length == 0) {
			String exceptionMessage = "No records to create PostScript from!";
			
			throw new PostScriptCreationException(exceptionMessage);
		}
		
		Record[] sortedRecords = sortRecords(records);
		
		// Sum the total (monitary) amount across all records.
		double totalAmount =
			CalculationUtilities.totalAmount(sortedRecords);
		
		/*
		 * Calculate the total height for all records (all of their heights
		 *  together?)  This is used to scale the records appropriately,
		 *  I think.
		 */
		double totalHeightSpan = Math.abs(
			defaultBoundingBoxHeight -
			(barMargin * (sortedRecords.length + 1)));
		
		// Get the first and last dates any of these records exist.
		Date graphStartDate = formGraphStartDateBasedOnRecords(sortedRecords);
		Date graphEndDate = formGraphEndDateBasedOnRecords(sortedRecords);
		
		/*
		 * We need a Date for every New Year's Day (so, January 1st) between
		 *  the start and end dates to use for drawing the date dash lines.
		 */
		Date[] newYearsDatesForGraph =
			DateUtilities.generateNewYearsDatesBetweenDates(
				graphStartDate, graphEndDate);
		
		// (NOTE: This side effects the calculated canvas width and height.)
		final String postScriptRecordBars = formPostScriptRecordBars(
			sortedRecords,
			graphStartDate,
			graphEndDate,
			barMargin,
			totalAmount,
			totalHeightSpan,
			defaultBoundingBoxWidth,
			startYPosition);
		
		double originalBoundingBoxWidth = this.calculatedBoundingBoxWidth;
		// double originalBoundingBoxHeight = this.calculatedBoundingBoxHeight;
		
		updateBoundingBoxSizeForPadding();

		double scale = 1.0;
		
		if (this.shouldScaleOutput) {
			scale = CalculationUtilities.scaleToFitToPageSize(
				this.calculatedBoundingBoxWidth,
				this.calculatedBoundingBoxHeight,
				this.pageWidth,
				this.pageHeight);
		}
		
		double horizontalCenteringOffset =
			CalculationUtilities.horizontalCenteringOffset(
				scale,
				graphStartDate,
				graphEndDate,
				sortedRecords[0].getStartDate(),
				sortedRecords[sortedRecords.length - 1].getEndDate(),
				defaultBoundingBoxWidth,
				barMargin,
				originalBoundingBoxWidth);
		
		final String postScriptComments =
			PostScriptFormationUtilities.comments(
				this.calculatedBoundingBoxHeight, this.pageWidth, scale);
		final String postScriptRotation =
			PostScriptFormationUtilities.rotate(scale);
		final String postScriptTranslate =
			PostScriptFormationUtilities.translateForMargins(
				this.calculatedBoundingBoxHeight,
				MARGIN_HEIGHT_FACTOR,
				scale);
		final String postScriptScale =
			PostScriptFormationUtilities.scale(scale);
		final String postScriptHeader = PostScriptFormationUtilities.header();
		final String postScriptCenteringTranslate =
			PostScriptFormationUtilities.centeringTranslate(
				horizontalCenteringOffset);
		final String postScriptYearLabels =
			PostScriptFormationUtilities.yearLabels(
				newYearsDatesForGraph,
				graphStartDate,
				graphEndDate,
				defaultBoundingBoxWidth,
				startYPosition,
				barMargin,
				scale,
				this.calculatedBoundingBoxHeight,
				MARGIN_HEIGHT_FACTOR);
		final String postScriptPaddingAboveYearLabels =
			formPostScriptTranslateForPaddingAboveYearLabels(scale);
		final String postScriptBackground =
			PostScriptFormationUtilities.background();
		
		return postScriptComments +
			   postScriptRotation +
			   postScriptTranslate +
			   postScriptScale +
			   postScriptHeader +
			   postScriptCenteringTranslate +
			   postScriptYearLabels +
			   postScriptPaddingAboveYearLabels +
			   postScriptRecordBars +
			   postScriptBackground;
	}
	
	@SuppressWarnings("unchecked") // Raw Iterator from the tuples method.
	private Record[] readRecordsFromTable(Table recordTable, LogService logger)
			throws ParseException {
		List<Record> workingRecordSet = new ArrayList<Record>();
		
		for(Iterator<Tuple> rows = recordTable.tuples(); rows.hasNext();) {
			Tuple row = rows.next();
			
			try {
				Record newRecord = new Record(
					row,
					this.labelKey,
					this.startDateKey,
					this.endDateKey,
					this.sizeByKey,
					this.startDateFormat,
					this.endDateFormat);
				
				workingRecordSet.add(newRecord);
			} catch (InvalidRecordException invalidRecordException) {
				logger.log(LogService.LOG_WARNING,
						   invalidRecordException.getMessage(),
						   invalidRecordException);
			}
		}

		return (Record[]) workingRecordSet.toArray(new Record[0]);
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
	
	private String formPostScriptTranslateForPaddingAboveYearLabels(
			double scale) {
		double xTranslate = 0.0;
		double yTranslate = CalculationUtilities.paddingAboveXAxis(
			this.calculatedBoundingBoxHeight,
			SPACING_ABOVE_X_AXIS_FACTOR,
			scale);
		
		return PostScriptFormationUtilities.line(
			xTranslate + " " + yTranslate + " translate");
	}
	
	private String formPostScriptRecordBars(Record[] records,
										   Date graphStartDate,
										   Date graphEndDate,
										   double recordBarMargin,
										   double totalRecordAmount,
										   double recordHeight,
										   double defaultBoundingBoxWidth,
										   double startYPosition) {
		double cursorYCoordinate = startYPosition;
		StringWriter recordBarPostScript = new StringWriter();
		
		for (Record currentRecord : records) {
			String currentRecordName = currentRecord.getLabel();
			Date currentRecordStartDate = currentRecord.getStartDate();
			Date currentRecordEndDate = currentRecord.getEndDate();
			double currentRecordAmount = currentRecord.getAmount();
			
			double recordBarStartXCoordinate =
				CalculationUtilities.xCoordinate(
					currentRecordStartDate,
					graphStartDate,
					graphEndDate,
					defaultBoundingBoxWidth,
					recordBarMargin);
			
			double recordBarEndXCoordinate =
				CalculationUtilities.xCoordinate(
					currentRecordEndDate,
					graphStartDate,
					graphEndDate,
					defaultBoundingBoxWidth,
					recordBarMargin);
			
			double recordBarWidth =
				recordBarEndXCoordinate - recordBarStartXCoordinate;
			
			double totalMonthSpan = DateUtilities.calculateMonthsBetween(
				currentRecordStartDate, currentRecordEndDate);
			double scale = 12.0 / totalMonthSpan;
			
			double calculatedRecordBarHeight =
				CalculationUtilities.recordBarHeight(
					currentRecordAmount,
					totalRecordAmount,
					recordHeight,
					scale);
			
			cursorYCoordinate += recordBarMargin;
			
			String recordString =
				PostScriptFormationUtilities.line(
					"(" + currentRecordName + ") " +
					recordBarStartXCoordinate + " " +
					cursorYCoordinate + " " +
					recordBarWidth + " " +
					calculatedRecordBarHeight +
					" record");
			
			recordBarPostScript.append(recordString);
			
			cursorYCoordinate += calculatedRecordBarHeight;
			
			double newCursorYCoordinate = cursorYCoordinate + recordBarMargin;
			updateCanvasSize(recordBarEndXCoordinate, newCursorYCoordinate);
		}
		
		return recordBarPostScript.toString();
	}
	
	private Date formGraphStartDateBasedOnRecords(Record[] records) {
		// Get the first record and its start date.
		Record firstRecord = records[0];
		Date firstRecordStartDate = firstRecord.getStartDate();
		// Form the GRAPH start date based on the first record's start date.
		Date graphStartDate =
			new Date((firstRecordStartDate.getYear() - 1), 0, 1);
		
		return graphStartDate;
	}
	
	private Date formGraphEndDateBasedOnRecords(Record[] records) {
		/*
		 * Form the GRAPH end date based on the determined last year of
		 *  the records.
		 */
		Date graphEndDate =
			new Date((determineLastYearOfRecords(records) + 1), 0, 1);
		
		return graphEndDate;
	}
	
	private int determineLastYearOfRecords(Record[] records) {
		int lastYear = 0;
		
		for (int ii = 0; ii < records.length; ii++) {
			Date recordEndDate = records[ii].getEndDate();
			int recordEndDateYear = recordEndDate.getYear();
			
			if (recordEndDateYear > lastYear) {
				lastYear = recordEndDateYear;
			}
		}
		
		return lastYear;
	}
	
	private void updateCanvasSize(double xCoordinate, double yCoordinate) {
		if (xCoordinate > this.calculatedBoundingBoxWidth) {
			this.calculatedBoundingBoxWidth = xCoordinate;
		}
		
		if (yCoordinate > this.calculatedBoundingBoxHeight) {
			this.calculatedBoundingBoxHeight = yCoordinate;
		}
	}
	
	private void updateBoundingBoxSizeForPadding() {
		double marginWidth = CalculationUtilities.marginWidth(
			this.calculatedBoundingBoxWidth, MARGIN_WIDTH_FACTOR, 1.0);
		this.calculatedBoundingBoxWidth += marginWidth;
		
		double marginHeight = CalculationUtilities.marginHeight(
			this.calculatedBoundingBoxHeight,
			MARGIN_HEIGHT_FACTOR,
			1.0);
		double paddingAboveXAxis = CalculationUtilities.paddingAboveXAxis(
			this.calculatedBoundingBoxHeight,
			SPACING_ABOVE_X_AXIS_FACTOR,
			1.0);
		this.calculatedBoundingBoxHeight += (marginHeight + paddingAboveXAxis);
	}
}