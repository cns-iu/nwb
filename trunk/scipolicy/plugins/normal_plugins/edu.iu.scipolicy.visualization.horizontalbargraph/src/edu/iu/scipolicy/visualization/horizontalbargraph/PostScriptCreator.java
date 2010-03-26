package edu.iu.scipolicy.visualization.horizontalbargraph;

import java.awt.Color;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.NumberUtilities;
import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.bar.Bar;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BoundingBox;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.Cursor;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout.Arrow;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.Record;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.RecordCollection;
import edu.iu.scipolicy.visualization.horizontalbargraph.utility.Utilities;

public class PostScriptCreator {
	public static final Color YEAR_LABEL_COLOR =
		new Color(0.0f, 0.0f, 0.0f);
//		new Color(0.0039f, 0.4509f, 0.5843f);
	public static final double YEAR_TICK_LINE_LINE_WIDTH = 1.5;
	public static final String YEAR_LABEL_FONT_FAMILY = "Garamond";
	public static final int YEAR_LABEL_FONT_SIZE = 25;
	public static final int TICK_SIZE = 5;
	public static final Color RGB_COLOR = new Color(0, 0, 255);
	public static final String BAR_LABEL_FONT_FAMILY = "Garamond";
	public static final int YEAR_CHARACTER_COUNT = 4;
	public static final int DECIMAL_PLACE_COUNT = 5;

	private StringTemplateGroup templateGroup;
	private BasicLayout layout;
	private Metadata metadata;
	private RecordCollection recordCollection;
	private List<Bar> bars;
	private PageOrientation pageOrientation;

	public PostScriptCreator(
			StringTemplateGroup templateGroup,
			BasicLayout layout,
			Metadata metadata,
			RecordCollection recordCollection) {
		this.templateGroup = templateGroup;
		this.layout = layout;
		this.metadata = metadata;
		this.recordCollection = recordCollection;
		Collection<Record> records = recordCollection.getSortedRecords();
		this.bars = layout.createBars(records);
		this.pageOrientation = layout.determinePageOrientation(bars);
	}

	public String toString() {
		String header = createPostScriptHeader();
		String functions = createFunctions();
		String orientation = createOrientation();
		String visualizationHeaderAndFooter = createVisualizationHeaderAndFooter();
		String otherTransformations = createOtherTransformations();
		String yearLabelProperties = createYearLabelProperties();
		String yearLabelsWithVerticalTicks =
			createYearLabelsWithVerticalTicks();
		String barLabelProperties = createBarProperties();
		String postScriptRecords = createVisualBars();

		return
			header +
			functions +
			orientation +
			visualizationHeaderAndFooter +
			otherTransformations +
			yearLabelProperties +
			yearLabelsWithVerticalTicks +
			barLabelProperties +
			postScriptRecords +
			"\r\n";
		/* TODO: Really this should just pass a bunch of stuff into one
		 * template and that should do things, but I won't make a fuss
		 * about it.
		 */
	}

	private String createPostScriptHeader() {
		BoundingBox boundingBox = this.layout.calculateBoundingBox();

		StringTemplate headerTemplate = this.templateGroup.getInstanceOf("header");
		headerTemplate.setAttribute("boundingBoxLeft", boundingBox.getLeft());
		headerTemplate.setAttribute("boundingBoxBottom", boundingBox.getBottom());
		headerTemplate.setAttribute("boundingBoxRight", boundingBox.getRight());
		headerTemplate.setAttribute("boundingBoxTop", boundingBox.getTop());
		headerTemplate.setAttribute("sourceDataName", this.metadata.getInputDataPath());
		headerTemplate.setAttribute("pageWidth", BasicLayout.PAGE_WIDTH);
		headerTemplate.setAttribute("pageHeight", BasicLayout.PAGE_HEIGHT);

		return headerTemplate.toString();
	}

	private String createVisualizationHeaderAndFooter() {
		StringTemplate showHeaderAndFooterTemplate =
			this.templateGroup.getInstanceOf("showHeaderFooter");
		showHeaderAndFooterTemplate.setAttribute(
			"analysisTime",
			Utilities.postscriptEscape(DateFormat.getDateTimeInstance(
				DateFormat.LONG, DateFormat.LONG).format(new Date())));
		showHeaderAndFooterTemplate.setAttribute(
			"inputData", Utilities.postscriptEscape(this.metadata.getInputDataPath()));

		showHeaderAndFooterTemplate.setAttribute(
			"datasetName", Utilities.postscriptEscape(this.metadata.getDatasetName()));

		showHeaderAndFooterTemplate.setAttribute(
			"label", Utilities.postscriptEscape(this.metadata.getLabelColumn()));
		showHeaderAndFooterTemplate.setAttribute(
			"startDate",
			Utilities.postscriptEscape(this.metadata.getStartDateColumn()));
		showHeaderAndFooterTemplate.setAttribute(
			"endDate",
			Utilities.postscriptEscape(this.metadata.getEndDateColumn()));
		showHeaderAndFooterTemplate.setAttribute(
			"sizeBy",
			Utilities.postscriptEscape(this.metadata.getSizeByColumn()));
		showHeaderAndFooterTemplate.setAttribute(
			"dateFormat",
			Utilities.postscriptEscape(this.metadata.getDateFormat()));
		showHeaderAndFooterTemplate.setAttribute(
			"yearLabelFontSize",
			Utilities.postscriptEscape("" + this.metadata.getYearLabelFontSize()));
		showHeaderAndFooterTemplate.setAttribute(
			"barLabelFontSize",
			Utilities.postscriptEscape("" + this.metadata.getBarLabelFontSize()));

		showHeaderAndFooterTemplate.setAttribute("x", HeaderAndFooterPositioningData.X_POSITION);
		showHeaderAndFooterTemplate.setAttribute(
			"dateTimeY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.DATE_TIME_Y_PERCENTAGE));
		showHeaderAndFooterTemplate.setAttribute(
			"inputDataY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.INPUT_DATA_Y_PERCENTAGE));

		showHeaderAndFooterTemplate.setAttribute(
			"datasetNameY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.DATASET_NAME_Y_PERCENTAGE));

		showHeaderAndFooterTemplate.setAttribute(
			"labelY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.LABEL_COLUMN_Y_PERCENTAGE));
		showHeaderAndFooterTemplate.setAttribute(
			"startDateY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.START_DATE_COLUMN_Y_PERCENTAGE));
		showHeaderAndFooterTemplate.setAttribute(
			"endDateY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.END_DATE_COLUMN_Y_PERCENTAGE));
		showHeaderAndFooterTemplate.setAttribute(
			"sizeByY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.SIZE_BY_COLUMN_Y_PERCENTAGE));
		showHeaderAndFooterTemplate.setAttribute(
			"dateFormatY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.DATE_FORMAT_Y_PERCENTAGE));
		showHeaderAndFooterTemplate.setAttribute(
			"yearLabelFontSizeY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.YEAR_LABEL_FONT_SIZE_Y_PERCENTAGE));
		showHeaderAndFooterTemplate.setAttribute(
			"barLabelFontSizeY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.BAR_LABEL_FONT_SIZE_Y_PERCENTAGE));

		showHeaderAndFooterTemplate.setAttribute(
			"footerY",
			this.pageOrientation.getYTranslateForHeaderAndFooter(
				HeaderAndFooterPositioningData.FOOTER_Y));

		return showHeaderAndFooterTemplate.toString();
	}

	private String createOtherTransformations() {
		double totalWidth = this.layout.calculateTotalWidthWithoutMargins();
		double totalHeight =
			this.layout.calculateTotalHeightWithoutMargins(this.bars);

		StringTemplate otherTransformationsTemplate =
			this.templateGroup.getInstanceOf("otherTransformations");
		otherTransformationsTemplate.setAttribute(
			"centerX",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getCenteringTranslateX(
					totalWidth, totalHeight)));
		otherTransformationsTemplate.setAttribute(
			"centerY",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getCenteringTranslateY(
					totalWidth, totalHeight)));
		otherTransformationsTemplate.setAttribute(
			"scale",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getScale()));

		return otherTransformationsTemplate.toString();
	}

	private String createFunctions() {
		StringTemplate functionsTemplate =
			this.templateGroup.getInstanceOf("functions");

		return functionsTemplate.toString();
	}

	private String createOrientation() {
		StringTemplate orientationTemplate =
			this.templateGroup.getInstanceOf("orientation");
		orientationTemplate.setAttribute(
			"rotation",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getRotation()));

		return orientationTemplate.toString();
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
			"lineWidth", NumberUtilities.convertToDecimalNotation(YEAR_TICK_LINE_LINE_WIDTH));
		yearLabelPropertiesTemplate.setAttribute("fontFamily", YEAR_LABEL_FONT_FAMILY);
		yearLabelPropertiesTemplate.setAttribute("fontSize", this.layout.getYearLabelFontSize());

		return yearLabelPropertiesTemplate.toString();
	}

	private String createYearLabelsWithVerticalTicks() {
		DateTime startDate = this.recordCollection.getMinimumDate();
		DateTime endDate = this.recordCollection.getMaximumDate();
		int endYear = endDate.getYear();
		double totalHeight = this.layout.calculateTotalHeightWithoutMargins(this.bars);
		StringBuffer yearLabelsWithVerticalTicks = new StringBuffer();

		for (
				DateTime currentDate = startDate;
				currentDate.getYear() <= endYear;
				currentDate = currentDate.plusYears(1)) {
			yearLabelsWithVerticalTicks.append(
				createYearLabelWithVerticalTick(
					startDate, endDate, currentDate, totalHeight));
		}

		return yearLabelsWithVerticalTicks.toString();
	}

	private String createBarProperties() {
		StringTemplate undoYearLabelPropertiesTemplate =
			this.templateGroup.getInstanceOf("undoYearLabelProperties");
		undoYearLabelPropertiesTemplate.setAttribute("fontFamily", BAR_LABEL_FONT_FAMILY);
		undoYearLabelPropertiesTemplate.setAttribute(
			"inverseFontSize", 1.0 / YEAR_LABEL_FONT_SIZE);

		StringTemplate barPropertiesTemplate = this.templateGroup.getInstanceOf("barProperties");
		barPropertiesTemplate.setAttribute("fontFamily", BAR_LABEL_FONT_FAMILY);
		barPropertiesTemplate.setAttribute("fontSize", this.layout.getBarLabelFontSize());

		return undoYearLabelPropertiesTemplate.toString() + barPropertiesTemplate.toString();
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

	private String createVisualBars() {
		Cursor cursor = this.layout.createCursor(this.layout.getBarLabelFontSize());
		StringBuffer records = new StringBuffer();

		for (Bar bar : this.bars) {
			String record = createBar(cursor, bar);
			records.append(record);
		}

		return records.toString();
	}

	private String createBar(Cursor cursor, Bar bar) {
		double barX = NumberUtilities.roundToNDecimalPlaces(
			this.layout.adjustXForStartArrow(bar), DECIMAL_PLACE_COUNT);
		double barY = NumberUtilities.roundToNDecimalPlaces(
			this.layout.positionBar(bar, cursor), DECIMAL_PLACE_COUNT);
		double barWidth = NumberUtilities.roundToNDecimalPlaces(
			this.layout.adjustWidthForArrows(bar), DECIMAL_PLACE_COUNT);
		double barHeight = NumberUtilities.roundToNDecimalPlaces(
			bar.getHeight(), DECIMAL_PLACE_COUNT);
		double textX = NumberUtilities.roundToNDecimalPlaces(
			bar.getX(), DECIMAL_PLACE_COUNT);
		double textY = NumberUtilities.roundToNDecimalPlaces(
			(bar.getHeight() / 2.0) + barY, DECIMAL_PLACE_COUNT);

		StringTemplate barTemplate = getBarStringTemplate(bar);

		barTemplate.setAttribute("label", bar.getLabel());
		barTemplate.setAttribute("textX", textX);
		barTemplate.setAttribute("textY", textY);
		barTemplate.setAttribute("barX", barX);
		barTemplate.setAttribute("barY", barY);
		barTemplate.setAttribute("barWidth", barWidth);
		barTemplate.setAttribute("barHeight", barHeight);
		
		String barPostScript = barTemplate.toString();
		String leftArrowPostScript = "";
		String rightArrowPostScript = "";
		
		if (bar.continuesLeft()) {
			Arrow leftArrow = this.layout.createLeftArrow(bar, barX, barY, barWidth);

			StringTemplate leftArrowTemplate = getArrowStringTemplate(bar);
			leftArrowTemplate.setAttribute("startX", leftArrow.startX);
			leftArrowTemplate.setAttribute("startY", leftArrow.startY);
			leftArrowTemplate.setAttribute("middleX", leftArrow.middleX);
			leftArrowTemplate.setAttribute("middleY", leftArrow.middleY);
			leftArrowTemplate.setAttribute("endX", leftArrow.endX);
			leftArrowTemplate.setAttribute("endY", leftArrow.endY);
			
			leftArrowPostScript = leftArrowTemplate.toString();
		}
		
		if (bar.continuesRight()) {
			Arrow rightArrow = this.layout.createRightArrow(bar, barX, barY, barWidth);
			
			StringTemplate rightArrowTemplate = getArrowStringTemplate(bar);;

			rightArrowTemplate.setAttribute("startX", rightArrow.startX);
			rightArrowTemplate.setAttribute("startY", rightArrow.startY);
			rightArrowTemplate.setAttribute("middleX", rightArrow.middleX);
			rightArrowTemplate.setAttribute("middleY", rightArrow.middleY);
			rightArrowTemplate.setAttribute("endX", rightArrow.endX);
			rightArrowTemplate.setAttribute("endY", rightArrow.endY);
			
			rightArrowPostScript = rightArrowTemplate.toString();
		}
		
		return barPostScript + leftArrowPostScript + rightArrowPostScript;
	}

	private StringTemplate getBarStringTemplate(Bar bar) {
		if (!bar.hasInfiniteAmount()) {
			return this.templateGroup.getInstanceOf("bar");
		} else {
			return this.templateGroup.getInstanceOf("infiniteBar");
		}
	}

	private StringTemplate getArrowStringTemplate(Bar bar) {
		if (!bar.hasInfiniteAmount()) {
			return this.templateGroup.getInstanceOf("arrow");
		} else {
			return this.templateGroup.getInstanceOf("infiniteArrow");
		}
	}
}