package edu.iu.sci2.visualization.horizontallinegraph.utilities;

import java.util.Date;

import org.cishell.utilities.DateUtilities;

import edu.iu.sci2.visualization.horizontallinegraph.Record;

public class CalculationUtilities {
	public static double horizontalCenteringOffset(
			double scale,
			Date graphStartDate,
			Date graphEndDate,
			Date firstDate,
			Date lastDate,
			double defaultBoundingBoxWidth,
			double margin,
			double pageBoundingBoxWidth) {
		double graphStart = xCoordinate(
			firstDate,
			graphStartDate,
			graphEndDate,
			defaultBoundingBoxWidth,
			margin);
		double graphEnd = xCoordinate(
			lastDate,
			graphStartDate,
			graphEndDate,
			defaultBoundingBoxWidth,
			margin);
		double graphWidth = (graphEnd - graphStart);
		double remainingWidth = (pageBoundingBoxWidth - graphWidth);
		
		return ((graphWidth + remainingWidth) / 2.0);
	}
	
	public static double xCoordinate(
			Date dateToInterpolate,
			Date startDate,
			Date endDate,
			double defaultBoundingBoxWidth,
			double margin) {
		double interpolatedDaysBetween = Math.max(
			DateUtilities.calculateDaysBetween(startDate, dateToInterpolate),
			1);
		double totalDaysBetween = Math.max(
			DateUtilities.calculateDaysBetween(startDate, endDate),
			1);
		double width = interpolatedDaysBetween * defaultBoundingBoxWidth;
		double interpolatedWidth = width / totalDaysBetween;
		
		return interpolatedWidth + 1000;
	}
	
	public static double marginHeight(
			double boundingBoxHeight, double heightFactor, double scale) {
		return (boundingBoxHeight * heightFactor * scale);
	}
	
	public static double marginWidth(
			double boundingBoxWidth, double widthFactor, double scale) {
		return (boundingBoxWidth * widthFactor * scale);
	}
	
	public static double paddingAboveXAxis(
			double boundingBoxHeight,
			double spacingAboveXAxisFactor,
			double scale) {
		return (boundingBoxHeight * spacingAboveXAxisFactor * scale);
	}
	
	public static long pageBoundingBoxHeight(
			double boundingBoxHeight, double scale) {
		return Math.round(boundingBoxHeight * scale);
	}
	
	public static long pageBoundingBoxWidth(double pageWidth) {
		return Math.round(
			PostScriptFormationUtilities.DOTS_PER_INCH * pageWidth);
	}
	
	public static double recordBarHeight(
			double dollarAmount,
			double totalDollarAmount,
			double recordHeight,
			double scale) {
		return
			(recordHeight * scale * dollarAmount / totalDollarAmount);
	}
	
	public static double totalAmount(Record[] records) {
		double totalRecordAmount = 0.0;
		
		for (int ii = 0; ii < records.length; ii++) {
			totalRecordAmount += records[ii].getAmount();
		}
		
		return totalRecordAmount;
	}
	
	public static double scaleToFitToPageSize(
			double boundingBoxWidth,
			double boundingBoxHeight,
			double pageWidth,
			double pageHeight) {
		double pageWidthInPoints =
			(pageWidth * PostScriptFormationUtilities.DOTS_PER_INCH);
		double pageHeightInPoints =
			(pageHeight * PostScriptFormationUtilities.DOTS_PER_INCH);
		double scale = 1.0;
		
		if (boundingBoxHeight > pageHeightInPoints) {
			scale = pageHeightInPoints / boundingBoxHeight;
		}
		
		double scaledBoundingBoxWidth = (boundingBoxWidth * scale);
		
		if (scaledBoundingBoxWidth > pageWidthInPoints) {
			scale *= pageWidthInPoints / scaledBoundingBoxWidth;
		}
		
		return scale;
	}
}