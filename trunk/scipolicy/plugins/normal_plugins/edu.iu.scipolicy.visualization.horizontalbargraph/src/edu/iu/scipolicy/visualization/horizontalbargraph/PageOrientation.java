package edu.iu.scipolicy.visualization.horizontalbargraph;

import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout;

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
	
	public double getCenteringTranslateX(
			double visualizationWidth,
			double visualizationHeight) {
		return this.pageOrientationType.centeringTranslateX(
			this.scale, visualizationWidth, visualizationHeight, this.layout);
	}
	
	public double getCenteringTranslateY(
			double visualizationWidth,
			double visualizationHeight) {
		return this.pageOrientationType.centeringTranslateY(
			this.scale,
			visualizationWidth,
			visualizationHeight,
			this.layout);
	}

	public double getYTranslateForHeaderAndFooter(double percentage) {
		return this.pageOrientationType.yTranslateForHeaderAndFooter(
			BasicLayout.PAGE_WIDTH, BasicLayout.PAGE_HEIGHT, percentage);
	}

	public enum PageOrientationType {
		PORTRAIT {
			public double rotation() {
				return 0.0;
			}
			
			public double centeringTranslateX(
					double scale,
					double visualizationWidth,
					double visualizationHeight,
					BasicLayout layout) {
				double pageWidthInPoints =
					BasicLayout.PAGE_WIDTH * BasicLayout.POINTS_PER_INCH;
				double scaledVisualizationWidth = visualizationWidth * scale;
				double fudgeForTextWidth =
					layout.calculateTotalTextWidth(
						layout.getBarLabelFontSize(),
						BasicLayout.MAXIMUM_BAR_LABEL_CHARACTER_COUNT) *
					TEXT_WIDTH_FUDGE_FACTOR *
					scale;
				
				return
					((pageWidthInPoints - scaledVisualizationWidth) / 2.0) +
					fudgeForTextWidth;
			}
			
			public double centeringTranslateY(
					double scale,
					double visualizationWidth,
					double visualizationHeight,
					BasicLayout layout) {
				double pageHeightInPoints = BasicLayout.PAGE_HEIGHT * BasicLayout.POINTS_PER_INCH;
				double scaledVisualizationHeight = visualizationHeight * scale;
				
				return (pageHeightInPoints - scaledVisualizationHeight) / 2.0;
			}

			public double yTranslateForHeaderAndFooter(
					double pageWidth, double pageHeight, double percentage) {
				return (pageHeight * percentage);
			}
		},
		LANDSCAPE {
			public double rotation() {
				return 90.0;
			}
			
			public double centeringTranslateX(
					double scale,
					double visualizationWidth,
					double visualizationHeight,
					BasicLayout layout) {
				double scaledVisualizationWidth = visualizationWidth * scale;
				double pageHeightInPoints = BasicLayout.PAGE_HEIGHT * BasicLayout.POINTS_PER_INCH;
				double fudgeForTextWidth =
					layout.calculateTotalTextWidth(
						layout.getBarLabelFontSize(),
						BasicLayout.MAXIMUM_BAR_LABEL_CHARACTER_COUNT) *
					TEXT_WIDTH_FUDGE_FACTOR *
					scale;

				return
					((pageHeightInPoints - scaledVisualizationWidth) /
						2.0) +
					fudgeForTextWidth;
			}
			
			public double centeringTranslateY(
					double scale,
					double visualizationWidth,
					double visualizationHeight,
					BasicLayout layout) {
				double pageWidthInPoints = BasicLayout.PAGE_WIDTH * BasicLayout.POINTS_PER_INCH;
				double scaledVisualizationHeight = visualizationHeight * scale;
				
				return -((pageWidthInPoints + scaledVisualizationHeight) / 2.0);
			}

			public double yTranslateForHeaderAndFooter(
					double pageWidth, double pageHeight, double percentage) {
				return ((pageWidth * percentage) - pageWidth);
			}
		};
		
		public abstract double rotation();
		public abstract double centeringTranslateX(
				double scale,
				double visualizationWidth,
				double visualizationHeight,
				BasicLayout layout);
		public abstract double centeringTranslateY(
				double scale,
				double visualizationWidth,
				double visualizationHeight,
				BasicLayout layout);
		public abstract double yTranslateForHeaderAndFooter(
				double pageWidth, double pageHeight, double percentage);
	};
}