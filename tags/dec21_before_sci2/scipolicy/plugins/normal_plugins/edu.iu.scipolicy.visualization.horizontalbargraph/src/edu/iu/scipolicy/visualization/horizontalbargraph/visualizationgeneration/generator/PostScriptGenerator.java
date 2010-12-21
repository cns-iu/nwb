package edu.iu.scipolicy.visualization.horizontalbargraph.visualizationgeneration.generator;

import java.awt.Color;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.NumberUtilities;
import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.HeaderAndFooterPositioningData;
import edu.iu.scipolicy.visualization.horizontalbargraph.Metadata;
import edu.iu.scipolicy.visualization.horizontalbargraph.PageOrientation;
import edu.iu.scipolicy.visualization.horizontalbargraph.bar.Bar;
import edu.iu.scipolicy.visualization.horizontalbargraph.bar.ColorLegend;
import edu.iu.scipolicy.visualization.horizontalbargraph.bar.ColorLegendLabel;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BoundingBox;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout.Arrow;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.Record;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.RecordCollection;
import edu.iu.scipolicy.visualization.horizontalbargraph.utility.Utilities;
import edu.iu.scipolicy.visualization.horizontalbargraph.visualizationgeneration.visualization.HorizontalBarGraphVisualization;

public class PostScriptGenerator {/*extends HorizontalBarGraphVisualizationGenerator<
		HorizontalBarGraphVisualization> {*/
	public static final Color YEAR_LABEL_COLOR = new Color(0.0f, 0.0f, 0.0f);
	public static final double YEAR_TICK_LINE_LINE_WIDTH = 1.5;
	public static final String YEAR_LABEL_FONT_FAMILY = "Garamond";
	public static final int YEAR_LABEL_FONT_SIZE = 25;
	public static final int TICK_SIZE = 5;
	public static final String BAR_LABEL_FONT_FAMILY = "Garamond";
	public static final int YEAR_CHARACTER_COUNT = 4;
	public static final int DECIMAL_PLACE_COUNT = 5;

	private StringTemplateGroup templateGroup;
	private BasicLayout layout;
	private List<Bar> bars;
	private PageOrientation pageOrientation;
	private Metadata metadata;
	private RecordCollection recordCollection;
	private ColorLegend colorLegend;

	public PostScriptGenerator(
			StringTemplateGroup templateGroup,
			BasicLayout layout,
			Metadata metadata,
			RecordCollection recordCollection) {
//		super(layout, metadata, recordCollection);
		this.templateGroup = templateGroup;
		this.layout = layout;
		this.metadata = metadata;
		this.recordCollection = recordCollection;
		Collection<Record> records = recordCollection.getSortedRecords();
		this.bars = layout.createBars(records);
		this.pageOrientation = layout.determinePageOrientation(bars);
		this.colorLegend = layout.createColorLegend(this.metadata.getColorizedByColumn(), records);
	}

	public PageOrientation getPageOrientation() {
		return this.pageOrientation;
	}

	public BasicLayout getLayout() {
		return this.layout;
	}

	public Metadata getMetadata() {
		return this.metadata;
	}

	public RecordCollection getRecordCollection() {
		return this.recordCollection;
	}

	public HorizontalBarGraphVisualization generateVisualization() {
		String postScript = generatePostScript();

		return new HorizontalBarGraphVisualization(
			getMetadata(), this.bars, this.pageOrientation, this.layout, postScript);
	}

	private String generatePostScript() {
		BoundingBox boundingBox = getLayout().calculateBoundingBox(this.bars);

		String header = createPostScriptHeader(boundingBox);
		String functions = createFunctions();
		String orientation = createOrientation();
		String visualizationHeaderAndFooter = createVisualizationHeaderAndFooter(boundingBox);
		String otherTransformations = createOtherTransformations();
		String yearLabelProperties = createYearLabelProperties();
		String colorLengendRecords = createColorLegend(boundingBox);
		String yearLabelsWithVerticalTicks = createYearLabelsWithVerticalTicks();
		String barLabelProperties = createBarProperties();
		String postScriptRecords = createVisualBars();

		return
			header +
			functions +
			orientation +
			visualizationHeaderAndFooter +
			colorLengendRecords +
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

	private String createPostScriptHeader(BoundingBox boundingBox) {
		StringTemplate headerTemplate = this.templateGroup.getInstanceOf("header");
		headerTemplate.setAttribute("boundingBoxLeft", boundingBox.getLeft());
		headerTemplate.setAttribute("boundingBoxBottom", boundingBox.getBottom());
		headerTemplate.setAttribute("boundingBoxRight", boundingBox.getRight());
		headerTemplate.setAttribute("boundingBoxTop", boundingBox.getTop());
		headerTemplate.setAttribute("sourceDataName", getMetadata().getInputDataPath());
		headerTemplate.setAttribute("pageWidth", BasicLayout.PAGE_WIDTH);
		headerTemplate.setAttribute("pageHeight", BasicLayout.PAGE_HEIGHT);

		return headerTemplate.toString();
	}

	private String createVisualizationHeaderAndFooter(BoundingBox boundingBox) {
		StringTemplate showHeaderAndFooterTemplate =
			this.templateGroup.getInstanceOf("showHeaderFooter");
		showHeaderAndFooterTemplate.setAttribute(
			"analysisTime",
			Utilities.postscriptEscape(DateFormat.getDateTimeInstance(
				DateFormat.LONG, DateFormat.LONG).format(new Date())));
		showHeaderAndFooterTemplate.setAttribute(
			"inputData", Utilities.postscriptEscape(getMetadata().getInputDataPath()));

		showHeaderAndFooterTemplate.setAttribute(
			"datasetName", Utilities.postscriptEscape(getMetadata().getDatasetName()));

		showHeaderAndFooterTemplate.setAttribute(
			"label", Utilities.postscriptEscape(getMetadata().getLabelColumn()));
		showHeaderAndFooterTemplate.setAttribute(
			"startDate",
			Utilities.postscriptEscape(getMetadata().getStartDateColumn()));
		showHeaderAndFooterTemplate.setAttribute(
			"endDate",
			Utilities.postscriptEscape(getMetadata().getEndDateColumn()));
		showHeaderAndFooterTemplate.setAttribute(
			"sizeBy",
			Utilities.postscriptEscape(getMetadata().getSizeByColumn()));
		showHeaderAndFooterTemplate.setAttribute(
			"minimumAmountPerDayForBarScaling",
			getMetadata().getMinimumAmountPerDayForScaling());
		showHeaderAndFooterTemplate.setAttribute(
			"barScaling",
			Utilities.postscriptEscape(getMetadata().getScalingFunction().getDisplayName()));
		showHeaderAndFooterTemplate.setAttribute(
			"dateFormat",
			Utilities.postscriptEscape(getMetadata().getDateFormat()));
		showHeaderAndFooterTemplate.setAttribute(
			"yearLabelFontSize",
			Utilities.postscriptEscape("" + getMetadata().getYearLabelFontSize()));
		showHeaderAndFooterTemplate.setAttribute(
			"barLabelFontSize",
			Utilities.postscriptEscape("" + getMetadata().getBarLabelFontSize()));

		showHeaderAndFooterTemplate.setAttribute("x", HeaderAndFooterPositioningData.X_POSITION);
		showHeaderAndFooterTemplate.setAttribute(
			"dateTimeY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.DATE_TIME_Y));
		showHeaderAndFooterTemplate.setAttribute(
			"inputDataY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.INPUT_DATA_Y));

		showHeaderAndFooterTemplate.setAttribute(
			"datasetNameY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.DATASET_NAME_Y));

		showHeaderAndFooterTemplate.setAttribute(
			"labelY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.LABEL_COLUMN_Y));
		showHeaderAndFooterTemplate.setAttribute(
			"startDateY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.START_DATE_COLUMN_Y));
		showHeaderAndFooterTemplate.setAttribute(
			"endDateY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.END_DATE_COLUMN_Y));
		showHeaderAndFooterTemplate.setAttribute(
			"sizeByY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.SIZE_BY_COLUMN_Y));
		showHeaderAndFooterTemplate.setAttribute(
			"minimumAmountPerDayForBarScalingY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox,
				HeaderAndFooterPositioningData.MINIMUM_AMOUNT_PER_DAY_FOR_BAR_SCALING_Y));
		showHeaderAndFooterTemplate.setAttribute(
			"barScalingY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.BAR_SCALING_Y));
		showHeaderAndFooterTemplate.setAttribute(
			"dateFormatY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.DATE_FORMAT_Y));
		showHeaderAndFooterTemplate.setAttribute(
			"yearLabelFontSizeY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.YEAR_LABEL_FONT_SIZE_Y));
		showHeaderAndFooterTemplate.setAttribute(
			"barLabelFontSizeY",
			this.pageOrientation.getYTranslateForHeader(
				boundingBox, HeaderAndFooterPositioningData.BAR_LABEL_FONT_SIZE_Y));

		showHeaderAndFooterTemplate.setAttribute(
			"footerY",
			this.pageOrientation.getYTranslateForFooter(
				boundingBox, HeaderAndFooterPositioningData.FOOTER_Y));

		return showHeaderAndFooterTemplate.toString();
	}

	private String createOtherTransformations() {
		StringTemplate otherTransformationsTemplate =
			this.templateGroup.getInstanceOf("otherTransformations");
		otherTransformationsTemplate.setAttribute(
			"centerX",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getCenteringTranslateX(this.bars)));
		otherTransformationsTemplate.setAttribute(
			"centerY",
			NumberUtilities.convertToDecimalNotation(
				pageOrientation.getCenteringTranslateY(this.bars)));
		otherTransformationsTemplate.setAttribute(
			"scale", NumberUtilities.convertToDecimalNotation(pageOrientation.getScale()));

		return otherTransformationsTemplate.toString();
	}

	private String createFunctions() {
		StringTemplate functionsTemplate = this.templateGroup.getInstanceOf("functions");

		return functionsTemplate.toString();
	}

	private String createOrientation() {
		StringTemplate orientationTemplate = this.templateGroup.getInstanceOf("orientation");
		orientationTemplate.setAttribute(
			"rotation", NumberUtilities.convertToDecimalNotation(pageOrientation.getRotation()));

		return orientationTemplate.toString();
	}

	private String createYearLabelProperties() {
		double red = ((double) YEAR_LABEL_COLOR.getRed()) / 255.0;
		double green = ((double) YEAR_LABEL_COLOR.getGreen()) / 255.0;
		double blue = ((double) YEAR_LABEL_COLOR.getBlue()) / 255.0;

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
		yearLabelPropertiesTemplate.setAttribute("fontSize", getLayout().getYearLabelFontSize());

		return yearLabelPropertiesTemplate.toString();
	}
	
	/**
	 * Only create region if there is item to be colorized
	 */
	private String createColorLegend(BoundingBox boundingBox) {
		List<ColorLegendLabel> colorLegendLabelList = colorLegend.getColorLegendLabelList();
		if(colorLegendLabelList.size() == 0) {
			return "";
		}
		
		StringBuilder records = new StringBuilder();
		records.append(createColorLegendTitle(boundingBox));
		for (ColorLegendLabel label : colorLegendLabelList) {
			records.append(createColorLegendlabel(label, boundingBox));
		}

		return records.toString();
	}
	
	private String createColorLegendTitle(BoundingBox boundingBox){
		StringTemplate colorLegendTitleTemplate = this.templateGroup.getInstanceOf("colorLegendTitle");
		colorLegendTitleTemplate.setAttribute("label", colorLegend.getTitle());
		colorLegendTitleTemplate.setAttribute("labelX", colorLegend.getX());
		colorLegendTitleTemplate.setAttribute("labelY", this.pageOrientation.getYTranslateForFooter(
				boundingBox, colorLegend.getY()));
		return colorLegendTitleTemplate.toString();
	}
	
	private String createColorLegendlabel(ColorLegendLabel colorLegendLabel, BoundingBox boundingBox){
		
		StringTemplate colorLegendLabelTemplate = this.templateGroup.getInstanceOf("colorLegendLabelItem");
		colorLegendLabelTemplate.setAttribute("label", colorLegendLabel.getLabel());
		colorLegendLabelTemplate.setAttribute("labelX", colorLegendLabel.getLabelX());
		colorLegendLabelTemplate.setAttribute("labelY", this.pageOrientation.getYTranslateForFooter(
												boundingBox,colorLegendLabel.getLabelY()));
		colorLegendLabelTemplate.setAttribute("boxX", colorLegendLabel.getBoxX());
		colorLegendLabelTemplate.setAttribute("boxY", this.pageOrientation.getYTranslateForFooter(
												boundingBox, colorLegendLabel.getBoxY()));
		colorLegendLabelTemplate.setAttribute("width", colorLegendLabel.getWidth());
		colorLegendLabelTemplate.setAttribute("height", colorLegendLabel.getHeight());
		colorLegendLabelTemplate.setAttribute(
			"r", ((double) colorLegendLabel.getColor().getRed()) / 255.0);
		colorLegendLabelTemplate.setAttribute(
			"g", ((double) colorLegendLabel.getColor().getGreen()) / 255.0);
		colorLegendLabelTemplate.setAttribute(
			"b", ((double) colorLegendLabel.getColor().getBlue()) / 255.0);
		
		return colorLegendLabelTemplate.toString();
	}

	private String createYearLabelsWithVerticalTicks() {
		DateTime startDate = getRecordCollection().getMinimumDate();
		DateTime endDate = getRecordCollection().getMaximumDate();
		int endYear = endDate.getYear();
		double totalHeight = getLayout().calculateTotalHeightWithoutMargins(this.bars);
		StringBuffer yearLabelsWithVerticalTicks = new StringBuffer();

		for (DateTime currentDate = startDate;
				currentDate.getYear() <= endYear;
				currentDate = currentDate.plusYears(1)) {
			yearLabelsWithVerticalTicks.append(createYearLabelWithVerticalTick(
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
		barPropertiesTemplate.setAttribute("fontSize", getLayout().getBarLabelFontSize());

		return undoYearLabelPropertiesTemplate.toString() + barPropertiesTemplate.toString();
	}

	private String createYearLabelWithVerticalTick(
			DateTime startDate, DateTime endDate, DateTime targetDate, double totalHeight) {
		double x = getLayout().calculateX(targetDate);

		StringTemplate yearLabelWithVerticalTickTemplate =
			this.templateGroup.getInstanceOf("yearLabelWithVerticalTick");
		yearLabelWithVerticalTickTemplate.setAttribute("year", targetDate.getYear());
		yearLabelWithVerticalTickTemplate.setAttribute(
			"x", NumberUtilities.convertToDecimalNotation(x));
		yearLabelWithVerticalTickTemplate.setAttribute("tickSize", TICK_SIZE);
		yearLabelWithVerticalTickTemplate.setAttribute(
			"height", NumberUtilities.convertToDecimalNotation(totalHeight));

		return yearLabelWithVerticalTickTemplate.toString();
	}

	private String createVisualBars() {
		StringBuffer records = new StringBuffer();

		for (Bar bar : this.bars) {
			String record = createBar(bar);
			records.append(record);
		}

		return records.toString();
	}

	private String createBar(Bar bar) {
		double barX = bar.getX();
		double barY = NumberUtilities.roundToNDecimalPlaces(bar.getY(), DECIMAL_PLACE_COUNT);
		double barWidth = bar.getWidth();
		double barHeight = NumberUtilities.roundToNDecimalPlaces(
			bar.getHeight(), DECIMAL_PLACE_COUNT);
		double textX = NumberUtilities.roundToNDecimalPlaces(bar.getX(), DECIMAL_PLACE_COUNT);
		double textY = barY;
		double red = ((double) bar.getColor().getRed()) / 255.0;
		double green = ((double) bar.getColor().getGreen()) / 255.0;
		double blue = ((double) bar.getColor().getBlue()) / 255.0;

		StringTemplate barTemplate = getBarStringTemplate(bar);

		barTemplate.setAttribute("label", bar.getLabel());
		barTemplate.setAttribute("textY", textY);
		barTemplate.setAttribute("barX", barX);
		barTemplate.setAttribute("barY", barY);
		barTemplate.setAttribute("barWidth", barWidth);
		barTemplate.setAttribute("barHeight", barHeight);
		barTemplate.setAttribute("r", red);
		barTemplate.setAttribute("g", green);
		barTemplate.setAttribute("b", blue);

		String leftArrowPostScript = "";
		String rightArrowPostScript = "";
		
		if (bar.continuesLeft()) {
			Arrow leftArrow = getLayout().createLeftArrow(bar, barX, barY, barWidth);

			textX = NumberUtilities.roundToNDecimalPlaces(leftArrow.middleX, DECIMAL_PLACE_COUNT);

			StringTemplate leftArrowTemplate = getArrowStringTemplate(bar);
			leftArrowTemplate.setAttribute("startX", leftArrow.startX);
			leftArrowTemplate.setAttribute("startY", leftArrow.startY);
			leftArrowTemplate.setAttribute("middleX", leftArrow.middleX);
			leftArrowTemplate.setAttribute("middleY", leftArrow.middleY);
			leftArrowTemplate.setAttribute("endX", leftArrow.endX);
			leftArrowTemplate.setAttribute("endY", leftArrow.endY);
			leftArrowTemplate.setAttribute("r", red);
			leftArrowTemplate.setAttribute("g", green);
			leftArrowTemplate.setAttribute("b", blue);
			
			leftArrowPostScript = leftArrowTemplate.toString();
		}

		barTemplate.setAttribute("textX", textX);
		
		if (bar.continuesRight()) {
			Arrow rightArrow = getLayout().createRightArrow(bar, barX, barY, barWidth);
			
			StringTemplate rightArrowTemplate = getArrowStringTemplate(bar);;

			rightArrowTemplate.setAttribute("startX", rightArrow.startX);
			rightArrowTemplate.setAttribute("startY", rightArrow.startY);
			rightArrowTemplate.setAttribute("middleX", rightArrow.middleX);
			rightArrowTemplate.setAttribute("middleY", rightArrow.middleY);
			rightArrowTemplate.setAttribute("endX", rightArrow.endX);
			rightArrowTemplate.setAttribute("endY", rightArrow.endY);
			rightArrowTemplate.setAttribute("r", red);
			rightArrowTemplate.setAttribute("g", green);
			rightArrowTemplate.setAttribute("b", blue);
			
			rightArrowPostScript = rightArrowTemplate.toString();
		}
		
		return barTemplate.toString() + leftArrowPostScript + rightArrowPostScript;
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