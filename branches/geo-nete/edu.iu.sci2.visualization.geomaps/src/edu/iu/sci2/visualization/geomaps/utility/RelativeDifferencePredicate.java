package edu.iu.sci2.visualization.geomaps.utility;

public class RelativeDifferencePredicate implements BinaryPredicate<Double> {
	private final double tolerance;

	public RelativeDifferencePredicate(double tolerance) {
		this.tolerance = tolerance;
	}
	
	/** Is the relative difference of x1 and x2 no greater than the tolerance? */
	@Override
	public boolean apply(Pair<Double> pair) {
		return (relativeDifference(pair.getLeft(), pair.getRight()) <= tolerance);
	}
	
	/* |x1 - x2| / max(|x1|, |x2|)
	 * Smaller means closer to equal.
	 * Think of as a forgiving measure of equality
	 * for floating-point numbers.
	 * Conventionally zero when x1 = x2 = 0.
	 */
	private static double relativeDifference(double x1, double x2) {
		if (x1 == x2) {
			// This catches the case x1 == x2 == 0.0, where the maxOfAbsolutes below would be zero.
			return 0.0;
		} else {
			double absoluteDifference = Math.abs(x1 - x2);
			double maxOfAbsolutes = Math.max(Math.abs(x1), Math.abs(x2));
	
			return (absoluteDifference / maxOfAbsolutes);
		}
	}
}
