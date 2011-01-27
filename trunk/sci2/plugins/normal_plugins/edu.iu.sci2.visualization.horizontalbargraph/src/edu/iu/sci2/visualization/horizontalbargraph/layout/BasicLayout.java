package edu.iu.sci2.visualization.horizontalbargraph.layout;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

import edu.iu.sci2.visualization.horizontalbargraph.PageOrientation;
import edu.iu.sci2.visualization.horizontalbargraph.UnitOfTime;
import edu.iu.sci2.visualization.horizontalbargraph.bar.Bar;
import edu.iu.sci2.visualization.horizontalbargraph.bar.ColorLegend;
import edu.iu.sci2.visualization.horizontalbargraph.bar.ColorLegendCreator;
import edu.iu.sci2.visualization.horizontalbargraph.bar.ColorizedByRegistry;
import edu.iu.sci2.visualization.horizontalbargraph.record.Record;

public class BasicLayout {
	public static final double PAGE_WIDTH = 8.5;
	public static final double PAGE_HEIGHT = 11.0;
	public static final double PAGE_HEIGHT_TO_WIDTH_RATIO = PAGE_HEIGHT / PAGE_WIDTH;

	// In inches.
	public static final double LEFT_MARGIN = 1.0;
	public static final double RIGHT_MARGIN = 1.0;
	public static final double TOP_MARGIN = 1.1;
	public static final double BOTTOM_MARGIN = 0.5;
	public static final double MARGIN_WIDTH = LEFT_MARGIN + RIGHT_MARGIN;
	public static final double MARGIN_HEIGHT = TOP_MARGIN + BOTTOM_MARGIN;

	public static final double POINTS_PER_INCH = 72.0;
	public static final double DAYS_PER_YEAR = 365.25;
	public static final double POINTS_PER_DAY = 144.0 / DAYS_PER_YEAR;

	public static final int MAXIMUM_BAR_LABEL_CHARACTER_COUNT = 50;
	public static final double POINTS_PER_EM_SCALE = 1.1;
	public static final double HEADER_LABEL_FONT_SIZE = 14.0;

	public static final double MINIMUM_BAR_HEIGHT_LOWER_BOUND = 3.0;
	public static final double MINIMUM_BAR_HEIGHT_UPPER_BOUND = 10.0;
	public static final double SPACE_BETWEEN_BARS = 20.0;

	public static final double BAR_INDEX_PERCENTAGE_TO_USE_FOR_LABEL_FONT_SIZE = 0.4;

	private boolean scaleToFitPage;
	private DateTime startDate;
	private DateTime endDate;
	private double barHeightScale;
	private double yearLabelFontSize;
	private double barLabelFontSize;
	private ColorizedByRegistry colorizedByResgitry;

	public BasicLayout(
			boolean scaleToFitPage,
			DateTime startDate,
			DateTime endDate,
			double minimumAmountPerUnitOfTime,
			double yearLabelFontSize,
			double barLabelFontSize) {
		this.scaleToFitPage = scaleToFitPage;
		this.startDate = startDate;
		this.endDate = endDate;
		this.barHeightScale = MINIMUM_BAR_HEIGHT_LOWER_BOUND / minimumAmountPerUnitOfTime;
		this.yearLabelFontSize = yearLabelFontSize;
		this.barLabelFontSize = barLabelFontSize;
	}

	public boolean scaleToFitPage() {
		return this.scaleToFitPage;
	}

	public double getYearLabelFontSize() {
		return this.yearLabelFontSize;
	}

	public double getBarLabelFontSize() {
		return this.barLabelFontSize;
	}
	
	private ColorizedByRegistry getColorizedByResgitry(Collection<Record> records) {
		if(this.colorizedByResgitry==null){
			/* Create a color mapping to the colorizedByColumn */
			List<String> colorizedByList = new ArrayList<String>();
			for (Record record : records) {
				colorizedByList.add(record.getColorizedBy());
			}
			this.colorizedByResgitry = new ColorizedByRegistry(colorizedByList);
		}
		return this.colorizedByResgitry;
	}

	public PageOrientation determinePageOrientation(Collection<Bar> bars) {
		double visualizationWidth = calculateTotalWidthWithMargins();
		double visualizationHeight = calculateTotalHeightWithMargins(bars);
		double visualizationRatio =
			calculateVisualizationRatio(visualizationHeight, visualizationWidth);
		double pageWidth = calculatePageWidth();
		double pageHeight = calculatePageHeight();
		double marginWidth = calculateHorizontalMargin();
		double marginHeight = calculateVerticalMargin();

		if (this.scaleToFitPage) {
			if (PAGE_HEIGHT_TO_WIDTH_RATIO > visualizationRatio) {
				if (visualizationHeight > visualizationWidth) {
					double scale = (pageWidth - marginWidth) / visualizationWidth;

					return new PageOrientation(
						PageOrientation.PageOrientationType.PORTRAIT, scale, this);
				} else if (visualizationHeight < visualizationWidth) {
					double scale = (pageWidth - marginWidth) / visualizationHeight;

					return new PageOrientation(
						PageOrientation.PageOrientationType.LANDSCAPE, scale, this);
				} else {
					return new PageOrientation(
						PageOrientation.PageOrientationType.PORTRAIT, 1.0, this);
				}
			} else if (PAGE_HEIGHT_TO_WIDTH_RATIO < visualizationRatio) {
				if (visualizationHeight > visualizationWidth) {
					double scale = (pageHeight - marginHeight) / visualizationHeight;

					return new PageOrientation(
						PageOrientation.PageOrientationType.PORTRAIT, scale, this);
				} else if (visualizationHeight < visualizationWidth) {
					double scale = (pageHeight - marginWidth) / visualizationWidth;

					return new PageOrientation(
						PageOrientation.PageOrientationType.LANDSCAPE, scale, this);
				} else {
					return new PageOrientation(
						PageOrientation.PageOrientationType.PORTRAIT, 1.0, this);
				}
			} else {
				return new PageOrientation(
					PageOrientation.PageOrientationType.PORTRAIT, 1.0, this);
			}
		} else {
			return new PageOrientation(PageOrientation.PageOrientationType.NO_SCALING, 1.0, this);
		}
	}

	public double calculateHorizontalMargin() {
		return (MARGIN_WIDTH * POINTS_PER_INCH);
	}

	public double calculateVerticalMargin() {
		return (MARGIN_HEIGHT * POINTS_PER_INCH) + (HEADER_LABEL_FONT_SIZE * POINTS_PER_EM_SCALE);
	}

	public double calculatePageWidth() {
		return PAGE_WIDTH * POINTS_PER_INCH;
	}

	public double calculatePageHeight() {
		return PAGE_HEIGHT * POINTS_PER_INCH;
	}

	public double calculatePointsPerDay() {
		return POINTS_PER_DAY;
	}

	public double calculateX(DateTime date) {
		int timeBetween = UnitOfTime.DAYS.timeBetween(this.startDate, date);

		return timeBetween * calculatePointsPerDay();
	} 

	public double adjustXForStartArrow(Bar bar) {
		if (bar.continuesLeft()) {
			double originalX = bar.getX();
			double arrowWidth = getBarArrowWidth(bar);

			return originalX + arrowWidth;
		} else {
			return bar.getX();
		}
	}
	
	public double getBarArrowWidth(Bar bar) {
		return bar.getHeight();
	}

	public double adjustWidthForArrows(Bar bar) {
		int arrowCount = 0;

		if (bar.continuesLeft()) {
			arrowCount++;
		}

		if (bar.continuesRight()) {
			arrowCount++;
		}

		double totalArrowsWidth =
			getBarArrowWidth(bar) * arrowCount;

		return bar.getWidth() - totalArrowsWidth;
	}

	public double calculatePointsPerEm(double fontSize) {
		return fontSize * POINTS_PER_EM_SCALE;
	}

	public double calculateTotalTextWidth(double fontSize, int characterCount) {
		return calculatePointsPerEm(fontSize) * characterCount;
	}

	public double calculateSpaceBetweenBars(double barLabelFontSize) {
		return SPACE_BETWEEN_BARS + barLabelFontSize;
	}

	public double calculateHeight(Record record) {
		return record.getAmountPerUnitOfTime() * this.barHeightScale;
	}

	public BoundingBox calculateBoundingBox(Collection<Bar> bars) {
		long boundingBoxLeft = 0;
		long boundingBoxBottom = 0;
		long boundingBoxRight;
		long boundingBoxTop;

		if (this.scaleToFitPage) {
			boundingBoxRight = Math.round(PAGE_WIDTH * POINTS_PER_INCH + 0.5);
			boundingBoxTop = Math.round(PAGE_HEIGHT * POINTS_PER_INCH + 0.5);
		} else {
			boundingBoxRight = Math.round(calculateTotalWidthWithMargins());
			boundingBoxTop = Math.round(calculateTotalHeightWithMargins(bars));
		}

		return new BoundingBox(
			boundingBoxLeft, boundingBoxBottom, boundingBoxRight, boundingBoxTop);
	}

	public double calculateTotalWidthWithoutMargins() {
		double endDateX = calculateX(this.endDate);

		return
			calculateTotalTextWidth(this.barLabelFontSize, MAXIMUM_BAR_LABEL_CHARACTER_COUNT) +
			endDateX;
	}

	public double calculateTotalWidthWithMargins() {
		double totalWidthWithoutMargins = calculateTotalWidthWithoutMargins();

		return totalWidthWithoutMargins + calculateHorizontalMargin();
	}

	public double calculateTotalHeightWithoutMargins(Collection<Bar> bars) {
		double barsHeight = 0.0;

		for (Bar bar : bars) {
			barsHeight += bar.getHeight();
		}

		double spaceBetweenVisualElements =
			bars.size() * (calculateSpaceBetweenBars(this.barLabelFontSize) + 1);

		return barsHeight + spaceBetweenVisualElements;
	}

	public double calculateTotalHeightWithMargins(Collection<Bar> bars) {
		double totalHeightWithoutMargins = calculateTotalHeightWithoutMargins(bars);

		return totalHeightWithoutMargins + calculateVerticalMargin();
	}

	public Cursor createCursor(double barLabelFontSize) {
		return new BasicCursor(0.0);
	}

	public double positionBar(double barHeight, Cursor cursor) {
		double halfBarHeight = (barHeight / 2.0);
		cursor.move(calculateSpaceBetweenBars(this.barLabelFontSize));
		cursor.move(halfBarHeight);
		double position = cursor.getPosition();
		cursor.move(halfBarHeight);

		return position;
	}

	public List<Bar> createBars(Collection<Record> records) {
		Cursor cursor = createCursor(getBarLabelFontSize());
		List<Bar> bars = new ArrayList<Bar>();
	
		for (Record record : records) {
			bars.add(createBar(record, cursor, this.getColorizedByResgitry(records)));
		}

		return bars;
	}

	public Bar createBar(Record record, Cursor cursor, ColorizedByRegistry colorizedByResgitry) {
		// TODO (for Patrick): Document why ths label is trimmed here instead of earlier.
		String trimmedLabel = record.getLabel().trim();
		String label = trimmedLabel;

		Color color = colorizedByResgitry.getColorOf(record.getColorizedBy());
			
		if (trimmedLabel.length() > MAXIMUM_BAR_LABEL_CHARACTER_COUNT) {
			label =
				trimmedLabel.substring(0, MAXIMUM_BAR_LABEL_CHARACTER_COUNT - 3) + "...";
		}
		
		DateTime recordStartDate = record.getStartDate();
		DateTime recordEndDate = record.getEndDate();
		double recordAmount = record.getAmount();
		boolean hasInvalidAmount = record.hasInvalidAmount();

		double startX = calculateX(recordStartDate);
		double endX = calculateX(recordEndDate);
		double width = endX - startX;
		double height = calculateHeight(record);

		return new Bar(
			label,
			record.getLabel(),
			color,
			!record.hasStartDate(),
			!record.hasEndDate(),
			startX,
			positionBar(height, cursor),
			width,
			height,
			recordAmount,
			hasInvalidAmount);
	}

	// The seemingly-redundant doubles are passed in because they're needed by the caller as well.
	public Arrow createLeftArrow(Bar bar, double barX, double barY, double barWidth) {
		double halfBarHeight = (bar.getHeight() / 2.0);

		// Bottom point.
		double startX = barX;
		double startY = (barY - halfBarHeight);

		// Left/middle point.
		double middleX = barX - getBarArrowWidth(bar);
		double middleY = barY;

		// Top point.
		double endX = barX;
		double endY = barY + halfBarHeight;
		
		return new Arrow(startX, startY, middleX, middleY, endX, endY);
	}
	
	public Arrow createRightArrow(Bar bar, double barX, double barY, double barWidth) {
		double halfBarHeight = (bar.getHeight() / 2.0);

		// Top point.
		double startX = barX + barWidth;
		double startY = (barY + halfBarHeight);

		// Right/middle point.
		double middleX = barX + barWidth + getBarArrowWidth(bar);
		double middleY = barY;

		// Bottom point.
		double endX = barX + barWidth;
		double endY = (barY - halfBarHeight);
		
		return new Arrow(startX, startY, middleX, middleY, endX, endY);
	}

	private double calculateVisualizationRatio(
			double visualizationWidth, double visualizationHeight) {
		return
			Math.max(visualizationWidth, visualizationHeight) /
			Math.min(visualizationWidth, visualizationHeight);
	}

	public class Arrow {
		public double startX;
		public double startY;
		public double middleX;
		public double middleY;
		public double endX;
		public double endY;
		
		public Arrow(
				double startX,
				double startY,
				double middleX,
				double middleY,
				double endX,
				double endY) {
			this.startX = startX;
			this.startY = startY;
			this.middleX = middleX;
			this.middleY = middleY;
			this.endX = endX;
			this.endY = endY;
		}
	}

	private class BasicCursor implements Cursor {
		private double y;

		public BasicCursor(double y) {
			this.y = y;
		}

		public double getPosition() {
			return this.y;
		}

		public void move(double yAmount) {
			this.y += yAmount;
		}
	}
	
	public ColorLegend createColorLegend(String columnTitle, Collection<Record> records){
		return new ColorLegendCreator(columnTitle, this.getColorizedByResgitry(records)).create();
	}
}