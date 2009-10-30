package edu.iu.scipolicy.visualization.horizontalbargraph.layout;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.PageOrientation;
import edu.iu.scipolicy.visualization.horizontalbargraph.UnitOfTime;
import edu.iu.scipolicy.visualization.horizontalbargraph.VisualElement;
import edu.iu.scipolicy.visualization.horizontalbargraph.bar.Bar;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.Record;

public class BasicLayout {
	public static final double PAGE_WIDTH = 8.5;
	public static final double PAGE_HEIGHT = 11.0;
	public static final double PAGE_HEIGHT_TO_WIDTH_RATIO =
		PAGE_HEIGHT / PAGE_WIDTH;
	public static final double MARGIN_WIDTH_FACTOR = 0.10;
	public static final double MARGIN_HEIGHT_FACTOR = 0.10;
	
	// TODO (FIXED?): Make year width wider.
	public static final double POINTS_PER_INCH = 72.0;
	public static final double POINTS_PER_YEAR = 144.0;
	
	public static final int MAXIMUM_CHARACTER_COUNT = 30;
	public static final double POINTS_PER_EM = 20.0;
	public static final double TOTAL_TEXT_WIDTH_IN_POINTS =
		MAXIMUM_CHARACTER_COUNT * POINTS_PER_EM;
	
	public static final double MINIMUM_BAR_HEIGHT = 3.0;
	public static final double SPACE_BETWEEN_BARS = 20.0;
	
	private DateTime startDate;
	private DateTime endDate;
	private double barHeightScale;
	private UnitOfTime unitOfTime;
	private int minimumUnitOfTime;
	
	public BasicLayout(
			DateTime startDate,
			DateTime endDate,
			double minimumAmountPerUnitOfTime,
			UnitOfTime unitOfTime,
			int minimumUnitOfTime) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.barHeightScale = MINIMUM_BAR_HEIGHT / minimumAmountPerUnitOfTime;
		this.unitOfTime = unitOfTime;
		this.minimumUnitOfTime = minimumUnitOfTime;
	}

	public PageOrientation determinePageOrientation(
			Collection<? extends VisualElement> visualElements) {
		double visualizationWidth =
			calculateTotalWidthWithMargins();
		double visualizationHeight =
			calculateTotalHeightWithMargins(visualElements);
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
					(PAGE_HEIGHT * POINTS_PER_INCH) /
					visualizationHeight;
				
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
		return
			totalWidthWithoutMargins * MARGIN_WIDTH_FACTOR * 2.0;
	}
	
	public double calculateVerticalMargin(double totalHeightWithoutMargins) {
		return
			totalHeightWithoutMargins * MARGIN_HEIGHT_FACTOR * 2.0;
	}
	
	public double calculateX(DateTime date) {
		int timeBetween = this.unitOfTime.timeBetween(this.startDate, date);
		
		return timeBetween * POINTS_PER_YEAR;
	}
	
	public double calculateHeight(Record record) {
		double recordAmountPerUnitOfTime = record.getAmountPerUnitOfTime(
			this.unitOfTime, this.minimumUnitOfTime);
			/* TODO: Note, if everything's in milliseconds, the above method on
			 *  record needs no arguments at all, since we already just set the
			 *  end date to take care of the minimum length.
			 */
		
		return recordAmountPerUnitOfTime * this.barHeightScale;
	}

	/* TODO: We have a fixed BB now, anyways.
	 *  Maybe use a Rectangle instead of a custom class.
	 * (Rectangle doesn't support longs.  I'm sticking with BoundingBox.)
	 */
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
		/* TODO: Maybe a little more; doesn't the last year label go a bit
		 *  further? We're addign margins anyways, but never hurts to
		 *  be precise.
		 * (If I add space for one year past the maximum end date found,
		 *  this should be accounted for.)
		 */
		double endDateX = calculateX(this.endDate);
		
		return TOTAL_TEXT_WIDTH_IN_POINTS + endDateX;
	}
	
	public double calculateTotalWidthWithMargins() {
		double totalWidthWithoutMargins =
			calculateTotalWidthWithoutMargins();
		
		return
			totalWidthWithoutMargins +
			calculateHorizontalMargin(totalWidthWithoutMargins);
	}
	
	public double calculateTotalHeightWithoutMargins(
			Collection<? extends VisualElement> visualElements) {
		double visualElementsHeight = 0.0;
		
		for (VisualElement element : visualElements) {
			visualElementsHeight += element.getHeight();
		}
		
		double spaceBetweenVisualElements =
			visualElements.size() * SPACE_BETWEEN_BARS;
		
		return
			visualElementsHeight +
			spaceBetweenVisualElements;
	}
	
	public double calculateTotalHeightWithMargins(
			Collection<? extends VisualElement> visualElements) {
		double totalHeightWithoutMargins =
			calculateTotalHeightWithoutMargins(visualElements);
		
		return
			totalHeightWithoutMargins +
			calculateVerticalMargin(totalHeightWithoutMargins);
	}
	
	public Cursor createCursor() {
		return new BasicCursor(SPACE_BETWEEN_BARS);
	}
	
	public double positionVisualElement(
			VisualElement visualElement, Cursor cursor) {
		cursor.move(SPACE_BETWEEN_BARS);
		double position = cursor.getPosition();
		cursor.move(visualElement.getHeight());
		
		return position;
	}
	
	private double calculateVisualizationRatio(
			double visualizationWidth, double visualizationHeight) {
		return
			Math.max(visualizationWidth, visualizationHeight) /
			Math.min(visualizationWidth, visualizationHeight);
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
			record.hasStartDate(),
			record.hasEndDate(),
			startX,
			width,
			height,
			recordAmount);
	}
}