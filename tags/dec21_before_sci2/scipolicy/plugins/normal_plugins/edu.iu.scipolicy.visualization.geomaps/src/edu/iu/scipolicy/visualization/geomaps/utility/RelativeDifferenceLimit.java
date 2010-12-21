package edu.iu.scipolicy.visualization.geomaps.utility;

public class RelativeDifferenceLimit extends BinaryCondition<Double> {
	private double tolerance;

	public RelativeDifferenceLimit(double tolerance) {
		this.tolerance = tolerance;
	}
	
	// "Are x1 and x2 equal, up to the given tolerance?"
	@Override
	public boolean isSatisfiedBy(Double x1, Double x2) {
		return (relativeDifference(x1, x2) <= tolerance);
	}
	
	/* |x1 - x2| / max(|x1|, |x2|)
	 * Smaller means closer to equal.
	 * Think of as a forgiving measure of equality
	 * for floating-point numbers.
	 * Conventionally zero when x1 = x2 = 0.
	 */
	private double relativeDifference(double x1, double x2) {
		if (x1 == x2) {
			/* In particular this catches the case x1 == x2 == 0.0,
			 * where the maxOfAbsolutes would be zero and would yield an
			 * ArithmeticException below.
			 */
			return 0.0;
		} else {
			double absoluteDifference = Math.abs(x1 - x2);
			double maxOfAbsolutes = Math.max(Math.abs(x1), Math.abs(x2));
	
			return (absoluteDifference / maxOfAbsolutes);
		}
	}		
}
