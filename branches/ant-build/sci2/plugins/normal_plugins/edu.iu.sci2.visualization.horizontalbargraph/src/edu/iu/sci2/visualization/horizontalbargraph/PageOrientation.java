package edu.iu.sci2.visualization.horizontalbargraph;

import java.util.Collection;

import edu.iu.sci2.visualization.horizontalbargraph.bar.Bar;
import edu.iu.sci2.visualization.horizontalbargraph.layout.BasicLayout;
import edu.iu.sci2.visualization.horizontalbargraph.layout.BoundingBox;

public class PageOrientation {
	public static final double TEXT_WIDTH_FUDGE_FACTOR = 0.75;
	
	private PageOrientationType pageOrientationType;
	private double scale;
	private BasicLayout layout;
	
	public PageOrientation(
			PageOrientationType pageOrientationType, double scale, BasicLayout layout) {
		this.pageOrientationType = pageOrientationType;
		this.scale = scale;
		this.layout = layout;
	}
	
	public PageOrientationType getPageOrientationType() {
		return this.pageOrientationType;
	}
	
	public double getScale() {
		return this.scale;
	}
	
	public double getRotation() {
		return this.pageOrientationType.rotation();
	}
	
	public double getCenteringTranslateX(Collection<Bar> bars) {
		return this.pageOrientationType.centeringTranslateX(this.scale, bars, this.layout);
	}
	
	public double getCenteringTranslateY(Collection<Bar> bars) {
		return this.pageOrientationType.centeringTranslateY(this.scale, bars, this.layout);
	}

	public double getYTranslateForHeader(BoundingBox boundingBox, double distanceFromTop) {
		return this.pageOrientationType.yTranslateForHeader(boundingBox, distanceFromTop);
	}

	public double getYTranslateForFooter(BoundingBox boundingBox, double distanceFromBottom) {
		return this.pageOrientationType.yTranslateForFooter(boundingBox, distanceFromBottom);
	}

	public enum PageOrientationType {
		PORTRAIT {
			public double rotation() {
				return 0.0;
			}
			
			public double centeringTranslateX(
					double scale, Collection<Bar> bars, BasicLayout layout) {
				double visualizationWidth = layout.calculateTotalWidthWithoutMargins();
				double pageWidthInPoints = BasicLayout.PAGE_WIDTH * BasicLayout.POINTS_PER_INCH;
				double scaledVisualizationWidth = visualizationWidth * scale;
				double totalTextWidth = layout.calculateTotalTextWidth(
					layout.getBarLabelFontSize(), BasicLayout.MAXIMUM_BAR_LABEL_CHARACTER_COUNT);
				double fudgeForTextWidth = ((totalTextWidth * scale) / 2.0);

				return
					((pageWidthInPoints - scaledVisualizationWidth) / 2.0) +
					(BasicLayout.LEFT_MARGIN * BasicLayout.POINTS_PER_INCH * scale / 1.0) +
					fudgeForTextWidth;
			}
			
			public double centeringTranslateY(
					double scale, Collection<Bar> bars, BasicLayout layout) {
				double visualizationHeight = layout.calculateTotalHeightWithoutMargins(bars);
				double pageHeightInPoints = BasicLayout.PAGE_HEIGHT * BasicLayout.POINTS_PER_INCH;
				double scaledVisualizationHeight = visualizationHeight * scale;
				
				return
					((pageHeightInPoints - scaledVisualizationHeight) / 2.0) -
					(BasicLayout.HEADER_LABEL_FONT_SIZE * BasicLayout.POINTS_PER_EM_SCALE);
			}

			public double yTranslateForHeader(BoundingBox boundingBox, double distanceFromTop) {
				return (boundingBox.getTop() - (distanceFromTop * BasicLayout.POINTS_PER_INCH));
			}

			public double yTranslateForFooter(BoundingBox boundingBox, double distanceFromBottom) {
				return
					(boundingBox.getBottom() + (distanceFromBottom * BasicLayout.POINTS_PER_INCH));
			}
		},
		LANDSCAPE {
			public double rotation() {
				return 90.0;
			}
			
			public double centeringTranslateX(
					double scale, Collection<Bar> bars, BasicLayout layout) {
				double visualizationWidth = layout.calculateTotalWidthWithoutMargins();
				double scaledVisualizationWidth = visualizationWidth * scale;
				double pageHeightInPoints = BasicLayout.PAGE_HEIGHT * BasicLayout.POINTS_PER_INCH;
				double totalTextWidth = layout.calculateTotalTextWidth(
					layout.getBarLabelFontSize(), BasicLayout.MAXIMUM_BAR_LABEL_CHARACTER_COUNT);
				double fudgeForTextWidth = ((totalTextWidth * scale) / 2.0);

				return
					((pageHeightInPoints - scaledVisualizationWidth) / 2.0) +
					(BasicLayout.BOTTOM_MARGIN * BasicLayout.POINTS_PER_INCH * scale / 1.0) +
					fudgeForTextWidth;
			}
			
			public double centeringTranslateY(
					double scale, Collection<Bar> bars, BasicLayout layout) {
				double visualizationHeight = layout.calculateTotalHeightWithoutMargins(bars);
				double pageWidthInPoints = BasicLayout.PAGE_WIDTH * BasicLayout.POINTS_PER_INCH;
				double scaledVisualizationHeight = visualizationHeight * scale;

				return
					-((pageWidthInPoints + scaledVisualizationHeight) / 2.0) -
					(BasicLayout.HEADER_LABEL_FONT_SIZE * BasicLayout.POINTS_PER_EM_SCALE);
			}

			public double yTranslateForHeader(BoundingBox boundingBox, double distanceFromTop) {
				return -(distanceFromTop * BasicLayout.POINTS_PER_INCH);
			}

			public double yTranslateForFooter(BoundingBox boundingBox, double distanceFromBottom) {
				return
					(-boundingBox.getRight() + (distanceFromBottom * BasicLayout.POINTS_PER_INCH));
			}
		},
		NO_SCALING {
			public double rotation() {
				return 0.0;
			}

			public double centeringTranslateX(
					double scale, Collection<Bar> bars, BasicLayout layout) {
				double visualizationWidth = layout.calculateTotalWidthWithoutMargins();
				BoundingBox boundingBox = layout.calculateBoundingBox(bars);
				double totalTextWidth = layout.calculateTotalTextWidth(
					layout.getBarLabelFontSize(), BasicLayout.MAXIMUM_BAR_LABEL_CHARACTER_COUNT);
				double fudgeForTextWidth = ((totalTextWidth * scale) / 1.0);
				
				return
					((boundingBox.getRight() - visualizationWidth) / 2.0) +
					(BasicLayout.LEFT_MARGIN * BasicLayout.POINTS_PER_INCH * scale / 1.0) +
					fudgeForTextWidth;
			}

			public double centeringTranslateY(
					double scale, Collection<Bar> bars, BasicLayout layout) {
				double visualizationHeight = layout.calculateTotalHeightWithoutMargins(bars);
				BoundingBox boundingBox = layout.calculateBoundingBox(bars);
				
				return
					((boundingBox.getTop() - visualizationHeight) / 2.0) -
					(BasicLayout.HEADER_LABEL_FONT_SIZE * BasicLayout.POINTS_PER_EM_SCALE);
			}

			public double yTranslateForHeader(BoundingBox boundingBox, double distanceFromTop) {
				return (boundingBox.getTop() - (distanceFromTop * BasicLayout.POINTS_PER_INCH));
			}

			public double yTranslateForFooter(BoundingBox boundingBox, double distanceFromBottom) {
				return
					(boundingBox.getBottom() + (distanceFromBottom * BasicLayout.POINTS_PER_INCH));
			}
		};
		
		public abstract double rotation();
		public abstract double centeringTranslateX(
				double scale, Collection<Bar> bars, BasicLayout layout);
		public abstract double centeringTranslateY(
				double scale, Collection<Bar> bars, BasicLayout layout);
		public abstract double yTranslateForHeader(
				BoundingBox boundingBox, double distanceFromTop);
		public abstract double yTranslateForFooter(
				BoundingBox boundingBox, double distanceFromBottom);
	};
}