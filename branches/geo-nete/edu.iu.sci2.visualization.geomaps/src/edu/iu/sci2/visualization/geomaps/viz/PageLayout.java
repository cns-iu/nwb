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
		public double pageWidth() {
			return 17.78 * POINTS_PER_INCH;
		}
		
		@Override
		public double pageHeight() {
			return 13.33 * POINTS_PER_INCH;
		}
		
		@Override
		public Optional<Point2D.Double> headerLowerLeft() {
			return Optional.absent();
		}

		@Override
		public double headerHeight() {
			return pageMargin();
		}

		@Override
		public Optional<Point2D.Double> howToReadLowerLeft() {
			return Optional.absent();
		}
	},
	PRINT {
		@Override
		public double pageWidth() {
			return 11.0 * POINTS_PER_INCH;
		}
		@Override
		public double pageHeight() {
			return 8.5 * POINTS_PER_INCH;
		}
		
		@Override
		public Optional<Point2D.Double> headerLowerLeft() {
			return Optional.of(new Point2D.Double(pageMargin(), pageHeight() - pageMargin()));
		}

		@Override
		public double headerHeight() {
			return 0.18 * pageHeight();
		}

		@Override
		public Optional<Point2D.Double> howToReadLowerLeft() {
			return Optional.of(new Point2D.Double(
					0.93 * legendariumDimensions().getWidth(), // TODO Fudge factor.. area legend isn't as wide as the color legends
					legendariumLowerLeft().getY()));
		}
	};

	public abstract double pageWidth();
	public abstract double pageHeight();
	
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
						+ legendariumDimensions().getHeight()
						+ PAGE_FOOTER_HEIGHT_IN_POINTS));
	}

	public static final double POINTS_PER_INCH = 72.0;
	public static final double PAGE_FOOTER_HEIGHT_IN_POINTS = pageMargin() + (0.25 * POINTS_PER_INCH);
	
	public double mapCenterX() {
		return pageWidth() / 2.0;
	}
	

	public Dimension<Double> legendariumDimensions() {
		return Dimension.ofSize(
				0.7 * pageWidth(),
				1.75 * POINTS_PER_INCH);
	}
	public Point2D.Double legendariumLowerLeft() {
		return new Point2D.Double(
				pageMargin(),
				PAGE_FOOTER_HEIGHT_IN_POINTS + (0.75 * legendariumDimensions().getHeight()));
	}
	public Point2D.Double legendLowerLeft() {
		return new Point2D.Double(
				legendariumLowerLeft().x,
				legendariumLowerLeft().y - LabeledReferenceCircles.TYPE_LABEL_FONT_SIZE - 3); // TODO
	}
	
	public Dimension<Double> colorGradientDimensions() {
		return Dimension.ofSize(
				0.8 * (legendariumDimensions().getWidth() / EnumSet.allOf(CircleDimension.class).size()),
				10.0);
	}
}
