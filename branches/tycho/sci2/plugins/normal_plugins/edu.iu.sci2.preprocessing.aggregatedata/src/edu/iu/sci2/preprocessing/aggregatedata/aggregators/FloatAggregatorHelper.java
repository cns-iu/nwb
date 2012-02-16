package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

/**
 * This class helps Float Aggregators. Do not try to instantiate it.
 * 
 * @author dmcoe
 * 
 */
public final class FloatAggregatorHelper {
	// Suppress default constructor for non-instantiability
	private FloatAggregatorHelper() {
		throw new AssertionError();
	}

	/**
	 * Check to see if adding the left hand side and the right hand side of two
	 * floats would overflow a float if added.
	 * 
	 * @param lhs
	 *            A float to be added to
	 * @param rhs
	 *            A float to be added to
	 * 
	 *            Throws an ArithmeticException if the float would overflow
	 */
	private static void checkAdditionForOverFlow(float lhs, float rhs) {
		boolean wouldAdditionBeOverflow = (lhs > 0 && rhs > Float.MAX_VALUE
				- lhs);
		if (wouldAdditionBeOverflow) {
			throw new ArithmeticException("Adding " + rhs + " and " + lhs
					+ " would overflow the value for float (" + Float.MAX_VALUE
					+ ").");
		}
	}

	
	
	/**
	 * Check to see if subtracting the left hand side and the right hand side of
	 * two floats would underflow if they were added.
	 * 
	 * @param lhs
	 *            A float to be added
	 * @param rhs
	 *            A float to be added
	 * 
	 *            Throws an ArithmeticException if the float would underflow
	 */
	private static void checkAdditionForUnderflow(float lhs, float rhs) {
		boolean wouldAdditionBeUnderflow = (lhs < 0 && rhs < Float.MIN_VALUE
				- lhs);
		if (wouldAdditionBeUnderflow) {
			throw new ArithmeticException("Subtracting " + rhs + " from " + lhs
					+ " would underflow the value for float (" + Float.MIN_VALUE
					+ ").");
		}
	}

	/**
	 * Checks to see if you are in danger of underflowing or overflowing a float
	 * if you added two floats. One or both of the floats can be negative.
	 * 
	 * @param lhs
	 *            A float number to add
	 * @param rhs
	 *            A float number to add
	 * 
	 *            Throws an ArithmeticException if the float would overflow
	 */
	public static void checkAdditionForOverOrUnderFlow(float lhs, float rhs) {
		boolean canUnderflow = (lhs < 0 && rhs < 0);
		boolean canOverFlow = (lhs > 0 && rhs > 0);

		if (canUnderflow) {
			checkAdditionForUnderflow(lhs, rhs);
		} else if (canOverFlow) {
			checkAdditionForOverFlow(lhs, rhs);
		}
	}
}
