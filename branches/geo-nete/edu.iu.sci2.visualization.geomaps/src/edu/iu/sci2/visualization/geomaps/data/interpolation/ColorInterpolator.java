package edu.iu.sci2.visualization.geomaps.data.interpolation;
import java.awt.Color;

import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.utility.ColorTuples;
import edu.iu.sci2.visualization.geomaps.utility.Continuum;

/**
 * Wrapper for a 3-dimensional InterpolatorND that interprets its 3-tuples as RGB coordinates
 * for a Color.
 */
public class ColorInterpolator implements Interpolator<Color> {	
	private final InterpolatorND interpolator3D;

	private ColorInterpolator(Range<Double> inRange, Continuum<Color> outRange) {
		final double[] outMinTuple = ColorTuples.asTuple(outRange.getPointA());
		final double[] outMaxTuple = ColorTuples.asTuple(outRange.getPointB());
		
		this.interpolator3D = InterpolatorND.between(
				inRange, Continuum.between(outMinTuple, outMaxTuple));
	}
	public static ColorInterpolator between(Range<Double> inRange, Continuum<Color> outRange) {
		return new ColorInterpolator(inRange, outRange);
	}

	
	@Override
	public Color apply(Double value) {
		return ColorTuples.asColor(interpolator3D.apply(value));
	}

	@Override
	public Range<Double> getInRange() {
		return interpolator3D.getInRange();
	}

	@Override
	public Continuum<Color> getOutContinuum() {
		return Continuum.between(
				ColorTuples.asColor(interpolator3D.getOutContinuum().getPointA()),
				ColorTuples.asColor(interpolator3D.getOutContinuum().getPointB()));
	}
}
