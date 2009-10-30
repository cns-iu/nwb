package edu.iu.scipolicy.visualization.horizontalbargraph;

import java.awt.Color;
import java.util.Collection;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.NumberUtilities;
import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.bar.Bar;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BoundingBox;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.Cursor;
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
			BasicLayout layout,
			String sourceDataName,
			RecordCollection recordCollection,
			Collection<Bar> bars) {
		PageOrientation pageOrientation =
			layout.determinePageOrientation(bars);
		
		String header = createHeader(templateGroup, layout, sourceDataName);
		String scaleAndOrientation = createTransformations(
			templateGroup,
			layout,
			pageOrientation,
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
				recordCollection.getMinimumStartDate(),
				recordCollection.getMaximumEndDate(),
				bars);
		// String show = createShow(templateGroup);
		String records = createRecords(
			templateGroup,
			layout,
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
			BasicLayout layout,
			String sourceDataName) {
		BoundingBox boundingBox = layout.calculateBoundingBox();

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
		headerTemplate.setAttribute("pageWidth", layout.PAGE_WIDTH);
		headerTemplate.setAttribute("pageHeight", layout.PAGE_HEIGHT);
		
		return headerTemplate.toString();
	}
	
	public static String createTransformations(
			StringTemplateGroup templateGroup,
			BasicLayout layout,
			PageOrientation pageOrientation,
			Collection<Bar> bars) {
		double totalWidth = layout.calculateTotalWidthWithoutMargins();
		double totalHeight = layout.calculateTotalHeightWithoutMargins(bars);
		StringTemplate transformationsTemplate =
			templateGroup.getInstanceOf("transformations");
		transformationsTemplate.setAttribute(
			"centerX",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getCenteringTranslateX(
					totalWidth, totalHeight)));
		transformationsTemplate.setAttribute(
			"centerY",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getCenteringTranslateY(
					totalWidth, totalHeight)));
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
			BasicLayout layout,
			DateTime startDate,
			DateTime endDate,
			Collection<Bar> bars) {
		int endYear = endDate.getYear();
		double totalHeight =
			layout.calculateTotalHeightWithoutMargins(bars);
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
			BasicLayout layout,
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
			BasicLayout layout,
			Collection<? extends Bar> bars) {
		Cursor cursor = layout.createCursor();
		StringBuffer records = new StringBuffer();
		
		for (Bar bar : bars) {
			String record = createRecord(
				templateGroup, cursor, layout, bar);
			records.append(record);
		}
		
		return records.toString();
	}
	
	public static String createRecord(
			StringTemplateGroup templateGroup,
			Cursor cursor,
			BasicLayout layout,
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