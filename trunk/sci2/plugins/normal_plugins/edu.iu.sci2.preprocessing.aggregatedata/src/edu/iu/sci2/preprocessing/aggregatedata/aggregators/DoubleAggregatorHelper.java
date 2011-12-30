package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

/**
 * This class helps Double Aggregators. Do not try to instantiate it.
 * 
 * @author dmcoe
 * 
 */
public final class DoubleAggregatorHelper {
	
	
	// Suppress default constructor for non-instantiability
	private DoubleAggregatorHelper() {
		throw new AssertionError();
	}

	/**
	 * Check to see if adding the left hand side and the right hand side of two
	 * doubles would overflow a double if added.
	 * 
	 * @param lhs
	 *            A double to be added to
	 * @param rhs
	 *            A double to be added to
	 * 
	 *            Throws an ArithmeticException if the double would overflow
	 */
	private static void checkAdditionForOverFlow(double lhs, double rhs) {
		boolean wouldAdditionBeOverflow = (lhs > 0 && rhs > Double.MAX_VALUE
				- lhs);
		if (wouldAdditionBeOverflow) {
			throw new ArithmeticException("Adding " + rhs + " and " + lhs
					+ " would overflow the value for double (" + Double.MAX_VALUE
					+ ").");
		}
	}

	
	
	/**
	 * Check to see if subtracting the left hand side and the right hand side of
	 * two doubles would underflow if they were added.
	 * 
	 * @param lhs
	 *            A double to be added
	 * @param rhs
	 *            A double to be added
	 * 
	 *            Throws an ArithmeticException if the double would underflow
	 */
	private static void checkAdditionForUnderflow(double lhs, double rhs) {
		boolean wouldAdditionBeUnderflow = (lhs < 0 && rhs < Double.MIN_VALUE
				- lhs);
		if (wouldAdditionBeUnderflow) {
			throw new ArithmeticException("Subtracting " + rhs + " from " + lhs
					+ " would underflow the value for double (" + Double.MIN_VALUE
					+ ").");
		}
	}

	/**
	 * Checks to see if you are in danger of underflowing or overflowing a double
	 * if you added two doubles. One or both of the doubles can be negative.
	 * 
	 * @param lhs
	 *            A double number to add
	 * @param rhs
	 *            A double number to add
	 * 
	 *            Throws an ArithmeticException if the double would overflow
	 */
	public static void checkAdditionForOverOrUnderFlow(double lhs, double rhs) {
		boolean canUnderflow = (lhs < 0 && rhs < 0);
		boolean canOverFlow = (lhs > 0 && rhs > 0);

		if (canUnderflow) {
			checkAdditionForUnderflow(lhs, rhs);
		} else if (canOverFlow) {
			checkAdditionForOverFlow(lhs, rhs);
		}
	}
}
