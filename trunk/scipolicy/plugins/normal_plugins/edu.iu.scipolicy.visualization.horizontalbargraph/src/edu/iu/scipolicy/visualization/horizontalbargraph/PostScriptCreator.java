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
import edu.iu.scipolicy.visualization.horizontalbargraph.record.Record;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.RecordCollection;

public class PostScriptCreator {
	public static final Color YEAR_LABEL_COLOR =
		new Color(0.0039f, 0.4509f, 0.5843f);
	public static final double YEAR_TICK_LINE_LINE_WIDTH = 1.5;
	public static final String YEAR_LABEL_FONT_FAMILY = "Garamond";
	public static final int YEAR_LABEL_FONT_SIZE = 25;
	public static final int TICK_SIZE = 5;
	public static final Color RGB_COLOR = new Color(0, 0, 255);
	
	private StringTemplateGroup templateGroup;
	private BasicLayout layout;
	private String sourceDataName;
	private RecordCollection recordCollection;
	private Collection<Bar> bars;
	private PageOrientation pageOrientation;
	
	public PostScriptCreator(
			StringTemplateGroup templateGroup,
			BasicLayout layout,
			String sourceDataName,
			RecordCollection recordCollection) {
		this.templateGroup = templateGroup;
		this.layout = layout;
		this.sourceDataName = sourceDataName;
		this.recordCollection = recordCollection;
		Collection<Record> records = recordCollection.getSortedRecords();
		this.bars = layout.createBars(records);
		this.pageOrientation =
			layout.determinePageOrientation(bars);
	}
	
	public String toString() {
		String header = createHeader();
		String scaleAndOrientation = createTransformations();
		String functions = createFunctions();
		String yearLabelProperties = createYearLabelProperties();
		String yearLabelsWithVerticalTicks =
			createYearLabelsWithVerticalTicks();
		String postScriptRecords = createRecords();
		
		return
			header +
			scaleAndOrientation +
			functions +
			yearLabelProperties +
			yearLabelsWithVerticalTicks +
			postScriptRecords +
			"\r\n";
		/* TODO: Really this should just pass a bunch of stuff into one
		 *  template and that should do things, but I won't make a fuss
		 *  about it.
		 */
	}
	
	private String createHeader() {
		BoundingBox boundingBox = this.layout.calculateBoundingBox();

		StringTemplate headerTemplate =
			this.templateGroup.getInstanceOf("header");
		headerTemplate.setAttribute(
			"boundingBoxLeft", boundingBox.getLeft());
		headerTemplate.setAttribute(
			"boundingBoxBottom", boundingBox.getBottom());
		headerTemplate.setAttribute(
			"boundingBoxRight", boundingBox.getRight());
		headerTemplate.setAttribute(
			"boundingBoxTop", boundingBox.getTop());
		headerTemplate.setAttribute(
			"sourceDataName", this.sourceDataName);
		headerTemplate.setAttribute("pageWidth", this.layout.PAGE_WIDTH);
		headerTemplate.setAttribute("pageHeight", this.layout.PAGE_HEIGHT);
		
		return headerTemplate.toString();
	}
	
	private String createTransformations() {
		double totalWidth = this.layout.calculateTotalWidthWithoutMargins();
		double totalHeight =
			this.layout.calculateTotalHeightWithoutMargins(this.bars);
		StringTemplate transformationsTemplate =
			this.templateGroup.getInstanceOf("transformations");
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
	
	private String createFunctions() {
		StringTemplate functionsTemplate =
			this.templateGroup.getInstanceOf("functions");
		
		return functionsTemplate.toString();
	}
	
	private String createYearLabelProperties() {
		double red = (double)YEAR_LABEL_COLOR.getRed() / 255.0;
		double green = (double)YEAR_LABEL_COLOR.getGreen() / 255.0;
		double blue = (double)YEAR_LABEL_COLOR.getBlue() / 255.0;
		
		StringTemplate yearLabelPropertiesTemplate =
			this.templateGroup.getInstanceOf("yearLabelProperties");
		yearLabelPropertiesTemplate.setAttribute(
			"red", NumberUtilities.convertToDecimalNotation(red));
		yearLabelPropertiesTemplate.setAttribute(
			"green", NumberUtilities.convertToDecimalNotation(green));
		yearLabelPropertiesTemplate.setAttribute(
			"blue", NumberUtilities.convertToDecimalNotation(blue));
		yearLabelPropertiesTemplate.setAttribute(
			"lineWidth",
			NumberUtilities.convertToDecimalNotation(
				YEAR_TICK_LINE_LINE_WIDTH));
		yearLabelPropertiesTemplate.setAttribute(
			"fontFamily", YEAR_LABEL_FONT_FAMILY);
		yearLabelPropertiesTemplate.setAttribute(
			"fontSize", YEAR_LABEL_FONT_SIZE);
		
		return yearLabelPropertiesTemplate.toString();
	}
	
	private String createYearLabelsWithVerticalTicks() {
		DateTime startDate = this.recordCollection.getMinimumStartDate();
		DateTime endDate = this.recordCollection.getMaximumEndDate();
		int endYear = endDate.getYear();
		double totalHeight =
			this.layout.calculateTotalHeightWithoutMargins(this.bars);
		StringBuffer yearLabelsWithVerticalTicks = new StringBuffer();

		for (
				DateTime currentDate = startDate;
				currentDate.getYear() <= endYear;
				currentDate = currentDate.plusYears(1)) {
			yearLabelsWithVerticalTicks.append(
				createYearLabelWithVerticalTick(
					startDate,
					endDate,
					currentDate,
					totalHeight));
		}
		
		return yearLabelsWithVerticalTicks.toString();
	}
	
	private String createYearLabelWithVerticalTick(
			DateTime startDate,
			DateTime endDate,
			DateTime targetDate,
			double totalHeight) {
		double x = this.layout.calculateX(targetDate);
		
		StringTemplate yearLabelWithVerticalTickTemplate =
			this.templateGroup.getInstanceOf("yearLabelWithVerticalTick");
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
	
	private String createRecords() {
		Cursor cursor = this.layout.createCursor();
		StringBuffer records = new StringBuffer();
		
		for (Bar bar : this.bars) {
			String record = createRecord(cursor, bar);
			records.append(record);
		}
		
		return records.toString();
	}

	private String createRecord(
			Cursor cursor,
			Bar bar) {
		StringTemplate recordTemplate =
			this.templateGroup.getInstanceOf("record");
		// TODO: PostScriptUtilities.escapeString() ?
		recordTemplate.setAttribute("label", bar.getLabel());
		recordTemplate.setAttribute("x", bar.getX());
		recordTemplate.setAttribute(
			"y", this.layout.positionVisualElement(bar, cursor));
		recordTemplate.setAttribute("width", bar.getWidth());
		recordTemplate.setAttribute("height", bar.getHeight());
		recordTemplate.setAttribute("leftArrow", bar.continuesLeft());
		recordTemplate.setAttribute("rightArrow", bar.continuesRight());
		
		return recordTemplate.toString();
	}
}