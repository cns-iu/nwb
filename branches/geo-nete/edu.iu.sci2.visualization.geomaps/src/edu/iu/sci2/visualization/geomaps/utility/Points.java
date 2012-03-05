package edu.iu.sci2.visualization.geomaps.utility;

import java.awt.geom.Point2D;

import com.google.common.base.Equivalence;

public class Points {
	private Points() {}
	
	public static Equivalence<Point2D.Double> distanceEquivalenceWithTolerance(
			final double tolerance) {
		return new Equivalence<Point2D.Double>() {
			@Override
			protected boolean doEquivalent(Point2D.Double a, Point2D.Double b) {
				return a.distance(b) <= tolerance;
			}

			@Override
			protected int doHash(Point2D.Double t) {
				return 0; // TODO Document strongly or throw UnsupportedOperationException
			}			
		};
	}
}
