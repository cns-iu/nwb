package edu.iu.scipolicy.visualization.horizontallinegraph.utilities;

import java.util.Date;

import org.cishell.utilities.DateUtilities;

import edu.iu.scipolicy.visualization.horizontallinegraph.Record;

public class CalculationUtilities {
	public static double horizontalCenteringOffset(
			double scale,
			Date graphStartDate,
			Date graphEndDate,
			Date firstDate,
			Date lastDate,
			double defaultBoundingBoxWidth,
			double margin,
			double unscaledBoundingBoxWidth) {
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
		double remainingWidth = (unscaledBoundingBoxWidth - graphWidth);
		
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
	
	public static long pageBoundingBoxWidth(
			double pageWidth, double scale) {
		return Math.round(pageWidth * scale);
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
}