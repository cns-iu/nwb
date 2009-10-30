package edu.iu.scipolicy.visualization.horizontalbargraph;

import edu.iu.scipolicy.visualization.horizontalbargraph.layouts.Layout;

public class PageOrientation {
	private PageOrientationType pageOrientationType;
	private double scale;
	
	public PageOrientation(
			PageOrientationType pageOrientationType, double scale) {
		this.pageOrientationType = pageOrientationType;
		this.scale = scale;
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
			double pageWidth,
			double pageHeight,
			double visualizationWidth,
			double visualizationHeight) {
		return this.pageOrientationType.centeringTranslateX(
			this.scale,
			pageWidth,
			pageHeight,
			visualizationWidth,
			visualizationHeight);
	}
	
	public double getCenteringTranslateY(
			double pageWidth,
			double pageHeight,
			double visualizationWidth,
			double visualizationHeight) {
		return this.pageOrientationType.centeringTranslateY(
			this.scale,
			pageWidth,
			pageHeight,
			visualizationWidth,
			visualizationHeight);
	}

	public enum PageOrientationType {
		PORTRAIT {
			public double rotation() {
				return 0.0;
			}
			
			public double centeringTranslateX(
					double scale,
					double pageWidth,
					double pageHeight,
					double visualizationWidth,
					double visualizationHeight) {
				double pageWidthInPoints = pageWidth * Layout.POINTS_PER_INCH;
				double scaledPageWidthInPoints = pageWidthInPoints / scale;
				
				return (scaledPageWidthInPoints + visualizationWidth) / 2.0;
			}
			
			public double centeringTranslateY(
					double scale,
					double pageWidth,
					double pageHeight,
					double visualizationWidth,
					double visualizationHeight) {
				double pageHeightInPoints =
					pageHeight * Layout.POINTS_PER_INCH;
				double scaledPageHeightInPoints = pageHeightInPoints / scale;
				
				return (scaledPageHeightInPoints - visualizationHeight) / 2.0;
			}
		},
		LANDSCAPE {
			public double rotation() {
				return 90.0;
			}
			
			public double centeringTranslateX(
					double scale,
					double pageWidth,
					double pageHeight,
					double visualizationWidth,
					double visualizationHeight) {
				double pageWidthInPoints = pageWidth * Layout.POINTS_PER_INCH;
				double scaledPageWidthInPoints = pageWidthInPoints / scale;
				
				return (scaledPageWidthInPoints + visualizationHeight) / 2.0;
			}
			
			public double centeringTranslateY(
					double scale,
					double pageWidth,
					double pageHeight,
					double visualizationWidth,
					double visualizationHeight) {
				double pageHeightInPoints =
					pageHeight * Layout.POINTS_PER_INCH;
				double scaledPageHeightInPoints = pageHeightInPoints / scale;
				System.err.println("pageHeightInPoints: " + pageHeightInPoints);
				System.err.println("scaledPageHeightInPoints: " + scaledPageHeightInPoints);
				System.err.println("result: " + ((scaledPageHeightInPoints - visualizationWidth) / 2.0));
				System.err.println("visualizationWidth: " + visualizationWidth);
				
				return (scaledPageHeightInPoints - visualizationWidth) / 2.0;
			}
		};
		
		public abstract double rotation();
		public abstract double centeringTranslateX(
				double scale,
				double pageWidth,
				double pageHeight,
				double visualizationWidth,
				double visualizationHeight);
		public abstract double centeringTranslateY(
				double scale,
				double pageWidth,
				double pageHeight,
				double visualizationWidth,
				double visualizationHeight);
	};
}