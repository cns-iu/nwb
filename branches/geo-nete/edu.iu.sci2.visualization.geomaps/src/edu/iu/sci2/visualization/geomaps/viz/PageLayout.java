package edu.iu.sci2.visualization.geomaps.viz;

import java.awt.Font;
import java.awt.geom.Point2D;
import java.util.EnumSet;

import com.google.common.base.Optional;

import edu.iu.sci2.visualization.geomaps.utility.Dimension;

/**
 * All figures in points.  1 point = 1/72 inch.
 */
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

		@Override
		public Font titleFont() {
			return new Font("Arial", Font.BOLD, 20);
		}
		
		@Override
		public Font contentFont() {
			return new Font("Arial", Font.PLAIN, 16);
		}

		@Override
		public double pageMargin() {
			return 0.8 * POINTS_PER_INCH;
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
					0.65 * pageWidth(), // TODO Fudge factor.. area legend isn't as wide as the color legends
					legendariumLowerLeft().getY()));
		}

		@Override
		public Font titleFont() {
			return new Font("Arial", Font.BOLD, 14);
		}
		
		@Override
		public Font contentFont() {
			return new Font("Arial", Font.PLAIN, 10);
		}

		@Override
		public double pageMargin() {
			return 0.5 * POINTS_PER_INCH;
		}
	};

	public abstract double pageWidth();
	public abstract double pageHeight();
	
	public abstract double pageMargin();

	public abstract Font titleFont();	
	public abstract Font contentFont();
	
	public abstract Optional<Point2D.Double> headerLowerLeft();
	public abstract double headerHeight();
	
	public abstract Optional<Point2D.Double> howToReadLowerLeft();
	
	
	public Dimension<Double> mapPageAreaMaxDimensions() {
		return Dimension.ofSize(
				pageWidth() - 2 * pageMargin(),
				pageHeight() -
						(headerHeight()
						+ legendariumReservedDimensions().getHeight()
						+ 2 * pageMargin()));
	}

	public static final double POINTS_PER_INCH = 72.0;
	
	public double pageFooterHeight() {
		return pageMargin() + (0.25 * POINTS_PER_INCH);
	}
	
	public double mapCenterX() {
		return pageWidth() / 2.0;
	}
	

	/* TODO These dimensions do not currently dictate the actual sizing of the legendarium, but
	 * we can want to reserve some rough space for it to better fit other components around it.
	 */
	public Dimension<Double> legendariumReservedDimensions() {
		return Dimension.ofSize(
				0.6 * pageWidth(),
				1.25 * POINTS_PER_INCH);
	}
	public Point2D.Double legendariumLowerLeft() {
		return new Point2D.Double(
				pageMargin(),
				pageFooterHeight() + (0.85 * legendariumReservedDimensions().getHeight()));
	}
	public Point2D.Double legendLowerLeft() {
		return new Point2D.Double(
				legendariumLowerLeft().x,
				legendariumLowerLeft().y - 18); // TODO
	}
	
	public Dimension<Double> colorGradientDimensions() {
		return Dimension.ofSize(
				0.8 * (legendariumReservedDimensions().getWidth() / EnumSet.allOf(CircleDimension.class).size()),
				10.0);
	}
}
