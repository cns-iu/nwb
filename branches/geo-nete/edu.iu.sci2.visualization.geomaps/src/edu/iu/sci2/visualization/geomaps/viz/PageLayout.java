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
		public Dimension<Double> pageDimensions() {
			return Dimension.ofSize(17.78 * POINTS_PER_INCH, 13.33 * POINTS_PER_INCH);
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
			return 0.25 * POINTS_PER_INCH;
		}
	},
	PRINT {
		@Override
		public Dimension<Double> pageDimensions() {
			return Dimension.ofSize(11.0 * POINTS_PER_INCH, 8.5 * POINTS_PER_INCH);
		}
		
		@Override
		public Optional<Point2D.Double> headerLowerLeft() {
			return Optional.of(new Point2D.Double(
					pageMargin(),
					pageDimensions().getHeight() - pageMargin() - titleFont().getSize()));
		}

		@Override
		public double headerHeight() {
			return 0.15 * pageDimensions().getHeight();
		}

		@Override
		public Optional<Point2D.Double> howToReadLowerLeft() {
			return Optional.of(new Point2D.Double(
					0.55 * pageDimensions().getWidth(),
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
			return 0.25 * POINTS_PER_INCH;
		}
	};

	public abstract Dimension<Double> pageDimensions();
	
	public abstract double pageMargin();

	public abstract Font titleFont();	
	public abstract Font contentFont();
	
	public abstract Optional<Point2D.Double> headerLowerLeft();
	public abstract double headerHeight();
	
	public abstract Optional<Point2D.Double> howToReadLowerLeft();
	
	
	public Dimension<Double> mapPageAreaMaxDimensions() {
		return Dimension.ofSize(
				pageDimensions().getWidth() - 2 * pageMargin(),
				pageDimensions().getHeight() -
						(headerHeight()
						+ legendariumReservedDimensions().getHeight()
						+ 2 * pageMargin()));
	}

	public static final double POINTS_PER_INCH = 72.0;
	
	public double pageFooterHeight() {
		return pageMargin() + (0.25 * POINTS_PER_INCH);
	}
	
	public double mapCenterX() {
		return pageDimensions().getWidth() / 2.0;
	}
	

	/* TODO These do not currently dictate the actual dimensions, they only reserve some
	 * rough space to better fit other components around it.
	 */
	public Dimension<Double> legendariumReservedDimensions() {
		return Dimension.ofSize(
				0.6 * pageDimensions().getWidth(),
				1.4 * (titleFont().getSize() + 2 * contentFont().getSize()) +
				Math.max(
						COLOR_GRADIENT_HEIGHT + 1.4 * contentFont().getSize(),
						2 * Circle.DEFAULT_CIRCLE_RADIUS_RANGE.getPointB()));
	}
	public Point2D.Double legendariumLowerLeft() {
		return new Point2D.Double(
				pageMargin(),
				pageMargin() + legendariumReservedDimensions().getHeight());
	}
	public Point2D.Double legendLowerLeft() {
		return new Point2D.Double(
				legendariumLowerLeft().x,
				legendariumLowerLeft().y - titleFont().getSize());
	}
	
	private static final double COLOR_GRADIENT_HEIGHT = 10.0;
	
	public Dimension<Double> colorGradientDimensions() {
		return Dimension.ofSize(
				0.8 * (legendariumReservedDimensions().getWidth() / EnumSet.allOf(CircleDimension.class).size()),
				COLOR_GRADIENT_HEIGHT);
	}
}
