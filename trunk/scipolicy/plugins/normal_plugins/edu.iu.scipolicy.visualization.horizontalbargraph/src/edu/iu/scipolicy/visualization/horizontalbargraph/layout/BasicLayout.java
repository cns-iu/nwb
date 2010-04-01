package edu.iu.scipolicy.visualization.horizontalbargraph.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.PageOrientation;
import edu.iu.scipolicy.visualization.horizontalbargraph.UnitOfTime;
import edu.iu.scipolicy.visualization.horizontalbargraph.bar.Bar;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.Record;

public class BasicLayout {
	public static final double PAGE_WIDTH = 8.5;
	public static final double PAGE_HEIGHT = 11.0;
	public static final double PAGE_HEIGHT_TO_WIDTH_RATIO = PAGE_HEIGHT / PAGE_WIDTH;
	public static final double MARGIN_WIDTH_FACTOR = 0.10;
	public static final double MARGIN_HEIGHT_FACTOR = 0.10;

	public static final double POINTS_PER_INCH = 72.0;
	public static final double DAYS_PER_YEAR = 365.25;
	public static final double POINTS_PER_DAY = 144.0 / DAYS_PER_YEAR;

	public static final int MAXIMUM_BAR_LABEL_CHARACTER_COUNT = 50;
	public static final double POINTS_PER_EM_SCALE = 1.1;

	public static final double MINIMUM_BAR_HEIGHT_LOWER_BOUND = 3.0;
	public static final double MINIMUM_BAR_HEIGHT_UPPER_BOUND = 10.0;
	public static final double SPACE_BETWEEN_BARS = 20.0;

	public static final double BAR_INDEX_PERCENTAGE_TO_USE_FOR_LABEL_FONT_SIZE = 0.4;

	boolean scaleToFitPage;
	private DateTime startDate;
	private DateTime endDate;
	private double barHeightScale;
	private double yearLabelFontSize;
	private double barLabelFontSize;

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

//		if (barLabelFontSize < MINIMUM_BAR_HEIGHT_LOWER_BOUND) {
//			this.barHeightScale = MINIMUM_BAR_HEIGHT_LOWER_BOUND / minimumAmountPerUnitOfTime;
//		} else if (barLabelFontSize > MINIMUM_BAR_HEIGHT_UPPER_BOUND) {
//			this.barHeightScale = MINIMUM_BAR_HEIGHT_UPPER_BOUND / minimumAmountPerUnitOfTime;
//		} else {
//			this.barHeightScale = barLabelFontSize / minimumAmountPerUnitOfTime;
//		}
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

	public PageOrientation determinePageOrientation(Collection<Bar> bars) {
		double visualizationWidth = calculateTotalWidthWithMargins();
		double visualizationHeight = calculateTotalHeightWithMargins(bars);
		double visualizationRatio = calculateVisualizationRatio(
			visualizationHeight, visualizationWidth);

		if (this.scaleToFitPage) {
			if (PAGE_HEIGHT_TO_WIDTH_RATIO > visualizationRatio) {
				if (visualizationHeight > visualizationWidth) {
					double scale = (PAGE_WIDTH * POINTS_PER_INCH) / visualizationWidth;

					return new PageOrientation(
						PageOrientation.PageOrientationType.PORTRAIT, scale, this);
				} else if (visualizationHeight < visualizationWidth) {
					double scale = (PAGE_WIDTH * POINTS_PER_INCH) / visualizationHeight;

					return new PageOrientation(
						PageOrientation.PageOrientationType.LANDSCAPE, scale, this);
				} else {
					return new PageOrientation(
						PageOrientation.PageOrientationType.PORTRAIT, 1.0, this);
				}
			} else if (PAGE_HEIGHT_TO_WIDTH_RATIO < visualizationRatio) {
				if (visualizationHeight > visualizationWidth) {
					double scale = (PAGE_HEIGHT * POINTS_PER_INCH) / visualizationHeight;

					return new PageOrientation(
						PageOrientation.PageOrientationType.PORTRAIT, scale, this);
				} else if (visualizationHeight < visualizationWidth) {
					double scale = (PAGE_HEIGHT * POINTS_PER_INCH) / visualizationWidth;

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

	public double calculateHorizontalMargin(double totalWidthWithoutMargins) {
		return totalWidthWithoutMargins * MARGIN_WIDTH_FACTOR * 2.0;
	}

	public double calculateVerticalMargin(double totalHeightWithoutMargins) {
		return totalHeightWithoutMargins * MARGIN_HEIGHT_FACTOR * 2.0;
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

		return
			totalWidthWithoutMargins +
			calculateHorizontalMargin(totalWidthWithoutMargins);
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

		return
			totalHeightWithoutMargins +
			calculateVerticalMargin(totalHeightWithoutMargins);
	}

	public Cursor createCursor(double barLabelFontSize) {
		return new BasicCursor(calculateSpaceBetweenBars(barLabelFontSize));
	}

	public double positionBar(Bar bar, Cursor cursor) {
		cursor.move(calculateSpaceBetweenBars(this.barLabelFontSize));
		double position = cursor.getPosition();
		cursor.move(bar.getHeight());

		return position;
	}

	public List<Bar> createBars(Collection<Record> records) {
		List<Bar> bars = new ArrayList<Bar>();

		for (Record record : records) {
			bars.add(createBar(record));
		}

		return bars;
	}

	public Bar createBar(Record record) {
		String trimmedLabel = record.getLabel().trim();
		String label = trimmedLabel;
		
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
			!record.hasStartDate(),
			!record.hasEndDate(),
			startX,
			width,
			height,
			recordAmount,
			hasInvalidAmount);
	}

	// The seemingly-redundant doubles are passed in because they're needed by the caller as well.
	public Arrow createLeftArrow(Bar bar, double barX, double barY, double barWidth) {
		// Bottom point.
		double startX = barX;
		double startY = barY;

		// Left/middle point.
		double middleX = barX - getBarArrowWidth(bar);
		double middleY = barY + (bar.getHeight() / 2.0);

		// Top point.
		double endX = barX;
		double endY = barY + bar.getHeight();
		
		return new Arrow(startX, startY, middleX, middleY, endX, endY);
	}
	
	public Arrow createRightArrow(Bar bar, double barX, double barY, double barWidth) {
		// Top point.
		double startX = barX + barWidth;
		double startY = barY + bar.getHeight();

		// Right/middle point.
		double middleX = barX + barWidth + getBarArrowWidth(bar);
		double middleY = barY + (bar.getHeight() / 2.0);

		// Bottom point.
		double endX = barX + barWidth;
		double endY = barY;
		
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
}