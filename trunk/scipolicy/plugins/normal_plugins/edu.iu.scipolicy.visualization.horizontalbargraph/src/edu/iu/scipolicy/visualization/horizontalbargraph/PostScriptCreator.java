package edu.iu.scipolicy.visualization.horizontalbargraph;

import java.awt.Color;
import java.util.Collection;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.NumberUtilities;
import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.bar.Bar;
import edu.iu.scipolicy.visualization.horizontalbargraph.layouts.Cursor;
import edu.iu.scipolicy.visualization.horizontalbargraph.layouts.Layout;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.RecordCollection;

public class PostScriptCreator {
	public static final Color YEAR_LABEL_COLOR =
		new Color(0.0039f, 0.4509f, 0.5843f);
	public static final double YEAR_TICK_LINE_LINE_WIDTH = 1.5;
	public static final String YEAR_LABEL_FONT_FAMILY = "Garamond";
	public static final int YEAR_LABEL_FONT_SIZE = 25;
	public static final int TICK_SIZE = 5;
	
	//TODO: make me a constructor
	public static String createPostScript(
			StringTemplateGroup templateGroup,
			Layout layout,
			String sourceDataName,
			double pageWidth,
			double pageHeight,
			RecordCollection recordCollection,
			Collection<Bar> bars) {
		PageOrientation pageOrientation = layout.determinePageOrientation(
			pageWidth, pageHeight, bars);
		
		String header = createHeader(
			templateGroup, layout, sourceDataName, pageWidth, pageHeight);
		String scaleAndOrientation = createTransformations(
			templateGroup,
			layout,
			pageOrientation,
			pageWidth,
			pageHeight,
			bars);
		String functions = createFunctions(templateGroup);
		String yearLabelProperties = createYearLabelProperties(
			templateGroup,
			YEAR_LABEL_COLOR,
			YEAR_TICK_LINE_LINE_WIDTH,
			YEAR_LABEL_FONT_FAMILY,
			YEAR_LABEL_FONT_SIZE);
		String yearLabelsWithVerticalTicks =
			createYearLabelsWithVerticalTicks(
				templateGroup,
				layout,
				pageHeight,
				recordCollection.getMinimumStartDate(),
				recordCollection.getMaximumEndDate(),
				bars);
		// String show = createShow(templateGroup);
		String records = createRecords(
			templateGroup,
			layout,
			pageWidth,
			pageHeight,
			bars);
		// String setRGBColor = createRGBColor(some color);
		
		return
			header +
			scaleAndOrientation +
			functions +
			yearLabelProperties +
			yearLabelsWithVerticalTicks +
			// show +
			records;
		//TODO: don't forget the newline
		//TODO: really this should just pass a bunch of stuff into one template and taht should do things, but I won't make a fuss about it
	}
	
	public static String createHeader(
			StringTemplateGroup templateGroup,
			Layout layout,
			String sourceDataName,
			double pageWidth,
			double pageHeight) {
		BoundingBox boundingBox =
			layout.calculateBoundingBox(pageWidth, pageHeight);

		StringTemplate headerTemplate = templateGroup.getInstanceOf("header");
		headerTemplate.setAttribute(
			"boundingBoxLeft", boundingBox.getLeft());
		headerTemplate.setAttribute(
			"boundingBoxBottom", boundingBox.getBottom());
		headerTemplate.setAttribute(
			"boundingBoxRight", boundingBox.getRight());
		headerTemplate.setAttribute(
			"boundingBoxTop", boundingBox.getTop());
		headerTemplate.setAttribute(
			"sourceDataName", sourceDataName);
		headerTemplate.setAttribute("pageWidth", pageWidth);
		headerTemplate.setAttribute("pageHeight", pageHeight);
		
		return headerTemplate.toString();
	}
	
	public static String createTransformations(
			StringTemplateGroup templateGroup,
			Layout layout,
			PageOrientation pageOrientation,
			double pageWidth,
			double pageHeight,
			Collection<Bar> bars) {
		double totalWidth =
			layout.calculateTotalWidthWithoutMargins(pageWidth);
		double totalHeight =
			layout.calculateTotalHeightWithoutMargins(bars, pageHeight);
		StringTemplate transformationsTemplate =
			templateGroup.getInstanceOf("transformations");
		transformationsTemplate.setAttribute(
			"centerX",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getCenteringTranslateX(
					pageWidth, pageHeight, totalWidth, totalHeight)));
		transformationsTemplate.setAttribute(
			"centerY",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getCenteringTranslateY(
					pageWidth, pageHeight, totalWidth, totalHeight)));
		transformationsTemplate.setAttribute(
			"scale",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getScale()));
		transformationsTemplate.setAttribute(
			"rotation",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getRotation()));
		
		return transformationsTemplate.toString();
	}
	
	public static String createFunctions(StringTemplateGroup templateGroup) {
		StringTemplate functionsTemplate =
			templateGroup.getInstanceOf("functions");
		
		return functionsTemplate.toString();
	}
	
	public static String createYearLabelProperties(
			StringTemplateGroup templateGroup,
			Color color,
			double lineWidth,
			String fontFamily,
			int fontSize) {
		double red = (double)color.getRed() / 255.0;
		double green = (double)color.getGreen() / 255.0;
		double blue = (double)color.getBlue() / 255.0;
		
		StringTemplate yearLabelPropertiesTemplate =
			templateGroup.getInstanceOf("yearLabelProperties");
		yearLabelPropertiesTemplate.setAttribute(
			"red", NumberUtilities.convertToDecimalNotation(red));
		yearLabelPropertiesTemplate.setAttribute(
			"green", NumberUtilities.convertToDecimalNotation(green));
		yearLabelPropertiesTemplate.setAttribute(
			"blue", NumberUtilities.convertToDecimalNotation(blue));
		yearLabelPropertiesTemplate.setAttribute(
			"lineWidth", NumberUtilities.convertToDecimalNotation(lineWidth));
		yearLabelPropertiesTemplate.setAttribute("fontFamily", fontFamily);
		yearLabelPropertiesTemplate.setAttribute("fontSize", fontSize);
		
		return yearLabelPropertiesTemplate.toString();
	}
	
	public static String createYearLabelsWithVerticalTicks(
			StringTemplateGroup templateGroup,
			Layout layout,
			double pageHeight,
			DateTime startDate,
			DateTime endDate,
			Collection<Bar> bars) {
		int endYear = endDate.getYear();
		double totalHeight =
			layout.calculateTotalHeightWithoutMargins(bars, pageHeight);
		StringBuffer yearLabelsWithVerticalTicks = new StringBuffer();
		
		for (// TODO: Experimental for loop style here.
				DateTime currentDate = startDate;
				currentDate.getYear() <= endYear;
				currentDate = currentDate.plusYears(1)) {
			yearLabelsWithVerticalTicks.append(
				createYearLabelWithVerticalTick(
					templateGroup,
					layout,
					startDate,
					endDate,
					currentDate,
					totalHeight));
		}
		
		return yearLabelsWithVerticalTicks.toString();
	}
	
	public static String createYearLabelWithVerticalTick(
			StringTemplateGroup templateGroup,
			Layout layout,
			DateTime startDate,
			DateTime endDate,
			DateTime targetDate,
			double totalHeight) {
		double x = layout.calculateX(targetDate);
		
		StringTemplate yearLabelWithVerticalTickTemplate =
			templateGroup.getInstanceOf("yearLabelWithVerticalTick");
		yearLabelWithVerticalTickTemplate.setAttribute(
			"year", targetDate.getYear());
		yearLabelWithVerticalTickTemplate.setAttribute(
			"x", NumberUtilities.convertToDecimalNotation(x));
		yearLabelWithVerticalTickTemplate.setAttribute(
			"tickSize", TICK_SIZE);
		yearLabelWithVerticalTickTemplate.setAttribute(
			"height", NumberUtilities.convertToDecimalNotation(totalHeight));
		
		return yearLabelWithVerticalTickTemplate.toString();
	}
	
	/*public static String createShow(StringTemplateGroup templateGroup) {
		StringTemplate showTemplate = templateGroup.getInstanceOf("show");
		
		return showTemplate.toString();
	}*/
	
	public static String createRecords(
			StringTemplateGroup templateGroup,
			Layout layout,
			double pageWidth,
			double pageHeight,
			Collection<? extends Bar> bars) {
		Cursor cursor = layout.createCursor();
		StringBuffer records = new StringBuffer();
		
		for (Bar bar : bars) {
			String record = createRecord(
				templateGroup, cursor, layout, pageWidth, pageHeight, bar);
			//System.err.println(record);
			records.append(record);
		}
		
		return records.toString();
	}
	
	public static String createRecord(
			StringTemplateGroup templateGroup,
			Cursor cursor,
			Layout layout,
			double pageWidth,
			double pageHeight,
			Bar bar) {
		StringTemplate recordTemplate = templateGroup.getInstanceOf("record");
		// TODO: PostScriptUtilities.escapeString() ?
		recordTemplate.setAttribute("label", bar.getLabel());
		recordTemplate.setAttribute("x", bar.getX());
		recordTemplate.setAttribute(
			"y", layout.positionVisualElement(bar, cursor));
		recordTemplate.setAttribute("width", bar.getWidth());
		recordTemplate.setAttribute("height", bar.getHeight());
		recordTemplate.setAttribute("leftArrow", bar.continuesLeft());
		recordTemplate.setAttribute("rightArrow", bar.continuesRight());
		
		return recordTemplate.toString();
	}
}