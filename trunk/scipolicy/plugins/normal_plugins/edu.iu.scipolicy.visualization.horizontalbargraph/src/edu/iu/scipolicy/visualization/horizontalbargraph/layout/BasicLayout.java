package edu.iu.scipolicy.visualization.horizontalbargraph.layout;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.PageOrientation;
import edu.iu.scipolicy.visualization.horizontalbargraph.UnitOfTime;
import edu.iu.scipolicy.visualization.horizontalbargraph.bar.Bar;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.Record;

public class BasicLayout {
	public static final double PAGE_WIDTH = 8.5;
	public static final double PAGE_HEIGHT = 11.0;
	public static final double PAGE_HEIGHT_TO_WIDTH_RATIO =
		PAGE_HEIGHT / PAGE_WIDTH;
	public static final double MARGIN_WIDTH_FACTOR = 0.10;
	public static final double MARGIN_HEIGHT_FACTOR = 0.10;

	public static final double POINTS_PER_INCH = 72.0;
	public static final double POINTS_PER_DAY = 144.0 / 365.25;

	public static final int MAXIMUM_CHARACTER_COUNT = 30;
	public static final double POINTS_PER_EM = 20.0;
	public static final double TOTAL_TEXT_WIDTH_IN_POINTS =
		MAXIMUM_CHARACTER_COUNT * POINTS_PER_EM;

	public static final double MINIMUM_BAR_HEIGHT = 3.0;
	public static final double BAR_ARROW_HEIGHT = 6.0;
	public static final double BAR_ARROW_WIDTH_FACTOR = 0.005;
	public static final double SPACE_BETWEEN_BARS = 20.0;

	private DateTime startDate;
	private DateTime endDate;
	private double barHeightScale;

	public BasicLayout(
			DateTime startDate,
			DateTime endDate,
			double minimumAmountPerUnitOfTime) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.barHeightScale = MINIMUM_BAR_HEIGHT / minimumAmountPerUnitOfTime;
	}

	public PageOrientation determinePageOrientation(Collection<Bar> bars) {
		double visualizationWidth = calculateTotalWidthWithMargins();
		double visualizationHeight = calculateTotalHeightWithMargins(bars);
		double visualizationRatio = calculateVisualizationRatio(
			visualizationHeight, visualizationWidth);

		if (PAGE_HEIGHT_TO_WIDTH_RATIO > visualizationRatio) {
			if (visualizationHeight > visualizationWidth) {
				double scale =
					(PAGE_WIDTH * POINTS_PER_INCH) / visualizationWidth;

				return new PageOrientation(
					PageOrientation.PageOrientationType.PORTRAIT, scale);
			} else if (visualizationHeight < visualizationWidth) {
				double scale =
					(PAGE_WIDTH * POINTS_PER_INCH) / visualizationHeight;

				return new PageOrientation(
					PageOrientation.PageOrientationType.LANDSCAPE, scale);
			} else {
				return new PageOrientation(
					PageOrientation.PageOrientationType.PORTRAIT, 1.0);
			}
		} else if (PAGE_HEIGHT_TO_WIDTH_RATIO < visualizationRatio) {
			if (visualizationHeight > visualizationWidth) {
				double scale =
					(PAGE_HEIGHT * POINTS_PER_INCH) / visualizationHeight;

				return new PageOrientation(
					PageOrientation.PageOrientationType.PORTRAIT, scale);
			} else if (visualizationHeight < visualizationWidth) {
				double scale =
					(PAGE_HEIGHT * POINTS_PER_INCH) / visualizationWidth;

				return new PageOrientation(
					PageOrientation.PageOrientationType.LANDSCAPE, scale);
			} else {
				return new PageOrientation(
					PageOrientation.PageOrientationType.PORTRAIT, 1.0);
			}
		} else {
			return new PageOrientation(
				PageOrientation.PageOrientationType.PORTRAIT, 1.0);
		}
	}

	public double calculateHorizontalMargin(double totalWidthWithoutMargins) {
		return totalWidthWithoutMargins * MARGIN_WIDTH_FACTOR * 2.0;
	}

	public double calculateVerticalMargin(double totalHeightWithoutMargins) {
		return totalHeightWithoutMargins * MARGIN_HEIGHT_FACTOR * 2.0;
	}

	public double calculateX(DateTime date) {
		int timeBetween = UnitOfTime.DAYS.timeBetween(this.startDate, date);

		return timeBetween * POINTS_PER_DAY;
	}

	// public double 

	public double adjustXForStartArrow(Bar bar) {
		if (bar.continuesLeft()) {
			double originalX = bar.getX();
			double arrowWidth = bar.getWidth() * BAR_ARROW_WIDTH_FACTOR;

			return originalX + arrowWidth;
		} else {
			return bar.getX();
		}
	}
	
	public double getBarArrowWidth(Bar bar) {
		return bar.getWidth() * BAR_ARROW_WIDTH_FACTOR;
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

	public double calculateHeight(Record record) {
		return record.getAmountPerUnitOfTime() * this.barHeightScale;
	}

	public BoundingBox calculateBoundingBox() {
		long boundingBoxLeft = 0;
		long boundingBoxBottom = 0;
		long boundingBoxRight =
			Math.round(PAGE_WIDTH * POINTS_PER_INCH + 0.5);
		long boundingBoxTop =
			Math.round(PAGE_HEIGHT * POINTS_PER_INCH + 0.5);

		return new BoundingBox(
			boundingBoxLeft,
			boundingBoxBottom,
			boundingBoxRight,
			boundingBoxTop);
	}

	public double calculateTotalWidthWithoutMargins() {
		double endDateX = calculateX(this.endDate);

		return TOTAL_TEXT_WIDTH_IN_POINTS + endDateX;
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

			if (bar.continuesLeft() || bar.continuesRight()) {
				barsHeight += BAR_ARROW_HEIGHT * 2.0;
			}
		}

		double spaceBetweenVisualElements =
			bars.size() * (SPACE_BETWEEN_BARS + 1);

		return barsHeight + spaceBetweenVisualElements;
	}

	public double calculateTotalHeightWithMargins(Collection<Bar> bars) {
		double totalHeightWithoutMargins =
			calculateTotalHeightWithoutMargins(bars);

		return
			totalHeightWithoutMargins +
			calculateVerticalMargin(totalHeightWithoutMargins);
	}

	public Cursor createCursor() {
		return new BasicCursor(SPACE_BETWEEN_BARS);
	}

	public double positionBar(Bar bar, Cursor cursor) {
		double arrowHeightAdjustment = 0.0;

		if (bar.continuesLeft() || bar.continuesRight()) {
			arrowHeightAdjustment = BAR_ARROW_HEIGHT;
		}

		cursor.move(SPACE_BETWEEN_BARS + arrowHeightAdjustment);
		double position = cursor.getPosition();
			cursor.move(bar.getHeight() + arrowHeightAdjustment);

		return position;
	}
	
	public Collection<Bar> createBars(Collection<Record> records) {
		Collection<Bar> bars = new ArrayList<Bar>();

		for (Record record : records) {
			bars.add(createBar(record));
		}

		return bars;
	}

	public Bar createBar(Record record) {
		DateTime recordStartDate = record.getStartDate();
		DateTime recordEndDate = record.getEndDate();
		double recordAmount = record.getAmount();

		double startX = calculateX(recordStartDate);
		double endX = calculateX(recordEndDate);
		double width = endX - startX;
		double height = calculateHeight(record);

		return new Bar(
			record.getLabel(),
			!record.hasStartDate(),
			!record.hasEndDate(),
			startX,
			width,
			height,
			recordAmount);
	}
	
	// The seemingly-redundant doubles are passed in because they're needed
	// by the caller as well.
	// TODO: ?
	public Arrow createLeftArrow(
			Bar bar, double barX, double barY, double barWidth) {
		// Bottom point.
		double startX = barX;
		double startY = barY - BAR_ARROW_HEIGHT;
		// Left/middle point.
		double middleX = barX - getBarArrowWidth(bar);
		double middleY = barY + (bar.getHeight() / 2.0);
		// Top point.
		double endX = barX;
		double endY = barY + bar.getHeight() + BAR_ARROW_HEIGHT;
		
		return new Arrow(startX, startY, middleX, middleY, endX, endY);
	}
	
	public Arrow createRightArrow(
			Bar bar, double barX, double barY, double barWidth) {
		// Top point.
		double startX = barX + barWidth;
		double startY = barY + bar.getHeight() + BAR_ARROW_HEIGHT;
		// Right/middle point.
		double middleX = barX + barWidth + getBarArrowWidth(bar);
		double middleY = barY + (bar.getHeight() / 2.0);
		// Bottom point.
		double endX = barX + barWidth;
		double endY = barY - BAR_ARROW_HEIGHT;
		
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