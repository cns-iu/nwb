package edu.iu.sci2.visualization.geomaps.viz;

import java.awt.Font;
import java.awt.geom.Point2D;
import java.util.EnumSet;

import com.google.common.base.Optional;

import edu.iu.sci2.visualization.geomaps.utility.Dimension;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReferenceCircles;

public enum PageLayout {
	WEB {
		@Override
		public Optional<Point2D.Double> headerLowerLeft() {
			return Optional.absent();
		}

		@Override
		public double headerHeight() {
			return 0.0;
		}

		@Override
		public Optional<Point2D.Double> howToReadLowerLeft() {
			return Optional.absent();
		}
	},
	PRINT {
		@Override
		public Optional<Point2D.Double> headerLowerLeft() {
			return Optional.of(new Point2D.Double(pageMargin(), pageHeight() - pageMargin()));
		}

		@Override
		public double headerHeight() {
			return 1.5 * POINTS_PER_INCH;
		}

		@Override
		public Optional<Point2D.Double> howToReadLowerLeft() {
			return Optional.of(new Point2D.Double(
					0.93 * LEGENDARIUM_DIMENSION.getWidth(), // TODO Fudge factor.. area legend isn't as wide as the color legends
					LEGENDARIUM_LOWER_LEFT.getY()));
		}
	};

	public static double pageWidth() {
		return 11.0 * POINTS_PER_INCH;
	}
	public static double pageHeight() {
		return 8.5 * POINTS_PER_INCH;
	}
	public static double pageMargin() {
		return 0.5 * POINTS_PER_INCH;
	}

	// TODO Fonts seem to be missing/ignored?  Getting monospace fonts right now
	public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 14);
	public static final Font CONTENT_FONT = new Font("Arial", Font.PLAIN, 10);
	
	public abstract Optional<Point2D.Double> headerLowerLeft();
	public abstract double headerHeight();
	
	public abstract Optional<Point2D.Double> howToReadLowerLeft();
	
	
	public Dimension<Double> mapPageAreaMaxDimensions() {
		return Dimension.ofSize(
				pageWidth() - 2 * pageMargin(),
				pageHeight() -
						(headerHeight()
						+ LEGENDARIUM_DIMENSION.getHeight()
						+ PAGE_FOOTER_HEIGHT_IN_POINTS));
	}

	public static final double POINTS_PER_INCH = 72.0;
	public static final double PAGE_FOOTER_HEIGHT_IN_POINTS = pageMargin() + (0.25 * POINTS_PER_INCH);
	
	public static final double MAP_CENTER_X_IN_POINTS = pageWidth() / 2.0;
	

	public static final Dimension<Double> LEGENDARIUM_DIMENSION = Dimension.ofSize(
			0.7 * pageWidth(),
			1.5 * POINTS_PER_INCH);
	public static final Point2D.Double LEGENDARIUM_LOWER_LEFT = new Point2D.Double(
			pageMargin(),
			PAGE_FOOTER_HEIGHT_IN_POINTS + (0.75 * LEGENDARIUM_DIMENSION.getHeight()));
	public static final Point2D.Double LEGEND_LOWER_LEFT = new Point2D.Double(
			LEGENDARIUM_LOWER_LEFT.x,
			LEGENDARIUM_LOWER_LEFT.y - LabeledReferenceCircles.TYPE_LABEL_FONT_SIZE - 3); // TODO
	
	public static final Dimension<Double> COLOR_GRADIENT_DIMENSION =
			Dimension.ofSize(
					0.8 * (LEGENDARIUM_DIMENSION.getWidth() / EnumSet.allOf(CircleDimension.class).size()),
					10.0);
}
