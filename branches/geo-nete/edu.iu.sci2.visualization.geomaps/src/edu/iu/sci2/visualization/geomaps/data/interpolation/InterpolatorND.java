package edu.iu.sci2.visualization.geomaps.data.interpolation;
import edu.iu.sci2.visualization.geomaps.utility.Range;


public class InterpolatorND implements Interpolator<double[]> {
	private final Range<Double> inRange;
	private final Range<double[]> outRange;
	private final Interpolator1D[] interpolators;

	private InterpolatorND(Range<Double> inRange, Range<double[]> outRange) {
		this.inRange = inRange;
		this.outRange = outRange;
		
		assert (outRange.pointA().length == outRange.pointB().length); // TODO ?
		
		int dimensionality = outRange.pointA().length;
		
		interpolators = new Interpolator1D[dimensionality];		
		for (int dd = 0; dd < dimensionality; dd++) {
			interpolators[dd] = Interpolator1D.between(
					inRange,
					Range.between(
							Double.valueOf(outRange.pointA()[dd]),
							Double.valueOf(outRange.pointB()[dd])));
		}
	}
	public static InterpolatorND between(Range<Double> inRange, Range<double[]> outRange) {
		return new InterpolatorND(inRange, outRange);
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
	public Range<Double> inRange() {
		return inRange;
	}

	@Override
	public Range<double[]> outRange() {
		return outRange;
	}
}
