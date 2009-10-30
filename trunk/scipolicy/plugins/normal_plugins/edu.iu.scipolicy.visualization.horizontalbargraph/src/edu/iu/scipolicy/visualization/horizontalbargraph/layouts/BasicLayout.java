package edu.iu.scipolicy.visualization.horizontalbargraph.layouts;

import java.util.Collection;

import org.joda.time.DateTime;

import edu.iu.scipolicy.visualization.horizontalbargraph.BoundingBox;
import edu.iu.scipolicy.visualization.horizontalbargraph.PageOrientation;
import edu.iu.scipolicy.visualization.horizontalbargraph.UnitOfTime;
import edu.iu.scipolicy.visualization.horizontalbargraph.VisualElement;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.Record;

public class BasicLayout implements Layout {
	public static final double SPACE_BETWEEN_BARS = 20.0;
	public static final double POINTS_PER_EM = 20.0;
	
	private double pointsPerUnitOfTime;
	private double minimumBarHeight;
	private DateTime startDate;
	private DateTime endDate;
	private double minimumAmountPerUnitOfTime;
	private UnitOfTime unitOfTime;
	private int minimumUnitOfTime;
	private double marginWidthFactor;
	private double marginHeightFactor;
	private int maximumLabelCharacterCount;
	
	public BasicLayout(
			double pointsPerUnitOfTime,
			double minimumBarHeight,
			DateTime startDate,
			DateTime endDate,
			double minimumAmountPerUnitOfTime,
			UnitOfTime unitOfTime,
			int minimumUnitOfTime,
			double marginWidthFactor,
			double marginHeightFactor,
			int maximumLabelCharacterCount) {
		this.pointsPerUnitOfTime = pointsPerUnitOfTime;
		this.minimumBarHeight = minimumBarHeight;
		this.startDate = startDate;
		this.endDate = endDate;
		this.minimumAmountPerUnitOfTime = minimumAmountPerUnitOfTime;
		this.unitOfTime = unitOfTime;
		this.minimumUnitOfTime = minimumUnitOfTime;
		this.marginWidthFactor = marginWidthFactor;
		this.marginHeightFactor = marginHeightFactor;
		this.maximumLabelCharacterCount = maximumLabelCharacterCount;
	}
	
	//TODO: No need for a method, we have a constant
	//TODO: Why is there an interface? remove
	public double getSpaceBetweenBars() {
		return SPACE_BETWEEN_BARS;
	}
	
	public PageOrientation determinePageOrientation(
			double pageWidth,
			double pageHeight,
			Collection<? extends VisualElement> visualElements) {
		double pageHeightToWidthRatio = pageHeight / pageWidth;
		double visualizationWidth =
			calculateTotalWidthWithMargins(pageWidth);
		double visualizationHeight =
			calculateTotalHeightWithMargins(visualElements, pageHeight);
		double visualizationRatio = calculateVisualizationRatio(
			visualizationHeight, visualizationWidth);
		
		if (pageHeightToWidthRatio > visualizationRatio) {
			if (visualizationHeight > visualizationWidth) {
				double scale =
					(pageWidth * Layout.POINTS_PER_INCH) / visualizationWidth;
				
				return new PageOrientation(
					PageOrientation.PageOrientationType.PORTRAIT, scale);
			} else if (visualizationHeight < visualizationWidth) {
				double scale =
					(pageWidth * Layout.POINTS_PER_INCH) / visualizationHeight;
				
				return new PageOrientation(
					PageOrientation.PageOrientationType.LANDSCAPE, scale);
			} else {
				return new PageOrientation(
					PageOrientation.PageOrientationType.PORTRAIT, 1.0);
			}
		} else if (pageHeightToWidthRatio < visualizationRatio) {
			if (visualizationHeight > visualizationWidth) {
				double scale =
					(pageHeight * Layout.POINTS_PER_INCH) /
					visualizationHeight;
				
				return new PageOrientation(
					PageOrientation.PageOrientationType.PORTRAIT, scale);
			} else if (visualizationHeight < visualizationWidth) {
				double scale =
					(pageHeight * Layout.POINTS_PER_INCH) / visualizationWidth;
				
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
			totalWidthWithoutMargins * this.marginWidthFactor * 2.0;
	}
	
	public double calculateVerticalMargin(double totalHeightWithoutMargins) {
		return
			totalHeightWithoutMargins * this.marginHeightFactor * 2.0;
	}
	
	public double calculateX(DateTime date) {
		int timeBetween = this.unitOfTime.timeBetween(this.startDate, date);
		
		return timeBetween * this.pointsPerUnitOfTime;
	}
	
	public double calculateHeight(Record record) {
		double recordAmountPerUnitOfTime = record.calculateAmountPerUnitOfTime(
			this.unitOfTime, this.minimumUnitOfTime);
			//TODO: note, if everything's in ms, the abvoe method on record needs no arguments at all, since we already just set the end date to take care of the minimum length
		double scaledAmountPerUnitOfTime = //TODO: this isn't an amount per unit of time, this is just a scaling factor. Also, you shouldn't arrange the calculation like this
			recordAmountPerUnitOfTime / this.minimumAmountPerUnitOfTime;
		//TODO: instead, calculate minimumBarHeight / minimumAmountPerUnitOfTime in advance (in the constructor of this, maybe), and write return recordAmountPerUnitOfTime * barHeightScale
		
		return scaledAmountPerUnitOfTime * this.minimumBarHeight;
	}
	
	//TODO: BoundingBox to layout (note: remove plural on package name)
	//TODO: we have a fixed BB now, anyways. Maybe use a Rectangle instead of a custom class
	public BoundingBox calculateBoundingBox(
			double pageWidth, double pageHeight) {
		long boundingBoxLeft = 0;
		long boundingBoxBottom = 0;
		long boundingBoxRight =
			Math.round(pageWidth * Layout.POINTS_PER_INCH + 0.5);
		long boundingBoxTop =
			Math.round(pageHeight * Layout.POINTS_PER_INCH + 0.5);
		
		return new BoundingBox(
			boundingBoxLeft,
			boundingBoxBottom,
			boundingBoxRight,
			boundingBoxTop);
	}
	
	public double calculatePageHeightToWidthRatio(//TODO: make into constant (derived from other constants)
			double pageWidth, double pageHeight) {
		return pageHeight / pageWidth;
	}
	
	public double calculateTotalWidthWithoutMargins(double pageWidth) {
		double textWidth = this.maximumLabelCharacterCount * POINTS_PER_EM;
		double endDateX = calculateX(this.endDate); //TODO: maybe a little more; doesn't the last year label go a bit further? We're addign margins anyways, but never hurts to be precise
		return textWidth + endDateX;
	}
	
	public double calculateTotalWidthWithMargins(double pageWidth) {
		double totalWidthWithoutMargins =
			calculateTotalWidthWithoutMargins(pageWidth);
		
		return
			totalWidthWithoutMargins +
			calculateHorizontalMargin(totalWidthWithoutMargins);
	}
	
	public double calculateTotalHeightWithoutMargins(
			Collection<? extends VisualElement> visualElements,
			double pageHeight) {
		double visualElementsHeight = 0.0;
		
		for (VisualElement element : visualElements) {
			visualElementsHeight += element.getHeight();
		}
		
		double spaceBetweenVisualElements =
			visualElements.size() * getSpaceBetweenBars();
		
		return
			visualElementsHeight +
			spaceBetweenVisualElements;
	}
	
	public double calculateTotalHeightWithMargins(
			Collection<? extends VisualElement> visualElements,
			double pageHeight) {
		double totalHeightWithoutMargins =
			calculateTotalHeightWithoutMargins(visualElements, pageHeight);
		
		return
			totalHeightWithoutMargins +
			calculateVerticalMargin(totalHeightWithoutMargins);
	}
	
	public Cursor createCursor() {
		return new BasicCursor(getSpaceBetweenBars());
	}
	
	public double positionVisualElement(
			VisualElement visualElement, Cursor cursor) {
		cursor.move(getSpaceBetweenBars());
		double position = cursor.getPosition();
		cursor.move(visualElement.getHeight());
		
		return position;
	}
	
	private double calculateVisualizationRatio(
			double visualizationWidth, double visualizationHeight) {
		//TODO: return Math.max / Math.min
		if (visualizationWidth > visualizationHeight) {
			return visualizationWidth / visualizationHeight;
		} else if (visualizationWidth < visualizationHeight) {
			return visualizationHeight / visualizationWidth;
		} else {
			return 1.0;
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
	
	/*public double getHorizontalScalar() {
		return this.horizontalScalar;
	}
	
	public double getVerticalScalar() {
		return this.verticalScalar;
	}
	
	public int getMaximumLabelLength() {
		return this.MAXIMUM_LABEL_LENGTH;
	}
	
	public double getVerticalSpacing() {
		return this.VERTICAL_SPACING;
	}
	
	public double calculateScale(
			double pageWidth,
			double pageHeight,
			double totalWidthWithoutMargins,
			Collection<? extends VisualElement> visualElements) {
		// TODO: The four cases Russell worked out for me.
	
		double totalWidth = calculateTotalWidth();
		double totalHeight = calculateTotalHeight(visualElements);
		double pageWidthInPoints = pageWidth * DOTS_PER_INCH;
		double pageHeightInPoints = pageHeight * DOTS_PER_INCH;
		double scale = 1.0;
		
		if (totalHeight > pageHeightInPoints) {
			scale = pageHeightInPoints / totalHeight;
		}
		
		double scaledTotalWidth = totalWidth * scale;
		
		if (scaledTotalWidth > pageWidthInPoints) {
			scale *= pageWidthInPoints / scaledTotalWidth;
		}
		
		return scale;
	}
	
	public double calculateTotalWidth() {
		// TODO:
		double textWidth = 0.0;	// maximum number of ems * some sizing for each em?
		
		return (this.periodLength + textWidth) * getHorizontalScalar();
	}
	
	public double calculateX(DateTime date) {
		return ((date.getMillis() / this.periodLength) *
				getHorizontalScalar());
	}
	
	public double calculateHeight(double amount) {
		// System.err.println("vertical scalar: " + getVerticalScalar() + "; minimumAmountPerUnitOfTime: " + this.barScale);
		return (amount * getVerticalScalar() * this.barScale);
	}*/
}