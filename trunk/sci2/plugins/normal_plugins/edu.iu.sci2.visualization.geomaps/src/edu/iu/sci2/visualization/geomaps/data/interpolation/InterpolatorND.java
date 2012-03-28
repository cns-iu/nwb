package edu.iu.sci2.visualization.geomaps.data.interpolation;
import com.google.common.base.Preconditions;
import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.utility.Continuum;


public class InterpolatorND implements Interpolator<double[]> {
	private final Range<Double> inRange;
	private final Continuum<double[]> outContinuum;
	private final Interpolator1D[] interpolators;

	private InterpolatorND(Range<Double> inRange, Continuum<double[]> outContinuum) {
		Preconditions.checkArgument(
				outContinuum.getPointA().length == outContinuum.getPointB().length,
				"Endpoints of the output continuum must have the same dimensionality.");
		
		this.inRange = inRange;
		this.outContinuum = outContinuum;
		
		int dimensionality = outContinuum.getPointA().length;
		
		interpolators = new Interpolator1D[dimensionality];		
		for (int dd = 0; dd < dimensionality; dd++) {
			interpolators[dd] = Interpolator1D.between(
					inRange,
					Continuum.between(
							Double.valueOf(outContinuum.getPointA()[dd]),
							Double.valueOf(outContinuum.getPointB()[dd])));
		}
	}
	public static InterpolatorND between(Range<Double> inRange, Continuum<double[]> outContinuum) {
		return new InterpolatorND(inRange, outContinuum);
	}
	

	@Override
	public double[] apply(Double value) {
		double[] tuple = new double[interpolators.length];
		
		for (int dd = 0; dd < tuple.length; dd++) {
			tuple[dd] = interpolators[dd].apply(value);
		}
		
		return tuple;
	}

	@Override
	public Range<Double> getInRange() {
		return inRange;
	}

	@Override
	public Continuum<double[]> getOutContinuum() {
		return outContinuum;
	}
}
