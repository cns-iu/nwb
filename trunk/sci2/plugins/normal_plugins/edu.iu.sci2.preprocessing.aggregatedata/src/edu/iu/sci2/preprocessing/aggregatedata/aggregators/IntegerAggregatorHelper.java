package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

/**
 * This class helps Integer Aggregators. Do not try to instantiate it.
 * 
 * @author dmcoe
 * 
 */
public final class IntegerAggregatorHelper {
	// Suppress default constructor for non-instantiability
	private IntegerAggregatorHelper() {
		throw new AssertionError();
	}

	/**
	 * Check to see if adding the left hand side and the right hand side of two
	 * ints would overflow a int if added.
	 * 
	 * @param lhs
	 *            A int to be added to
	 * @param rhs
	 *            A int to be added to
	 * 
	 *            Throws an ArithmeticException if the int would overflow
	 */
	private static void checkAdditionForOverFlow(int lhs, int rhs) {
		boolean wouldAdditionBeOverflow = (lhs > 0 && rhs > Integer.MAX_VALUE
				- lhs);
		if (wouldAdditionBeOverflow) {
			throw new ArithmeticException("Adding " + rhs + " and " + lhs
					+ " would overflow the value for int (" + Integer.MAX_VALUE
					+ ").");
		}
	}

	
	
	/**
	 * Check to see if subtracting the left hand side and the right hand side of
	 * two ints would underflow if they were added.
	 * 
	 * @param lhs
	 *            A int to be added
	 * @param rhs
	 *            A int to be added
	 * 
	 *            Throws an ArithmeticException if the int would underflow
	 */
	private static void checkAdditionForUnderflow(int lhs, int rhs) {
		boolean wouldAdditionBeUnderflow = (lhs < 0 && rhs < Integer.MIN_VALUE
				- lhs);
		if (wouldAdditionBeUnderflow) {
			throw new ArithmeticException("Subtracting " + rhs + " from " + lhs
					+ " would underflow the value for int (" + Integer.MIN_VALUE
					+ ").");
		}
	}

	/**
	 * Checks to see if you are in danger of underflowing or overflowing a int
	 * if you added two ints. One or both of the ints can be negative.
	 * 
	 * @param lhs
	 *            A int number to add
	 * @param rhs
	 *            A int number to add
	 * 
	 *            Throws an ArithmeticException if the int would overflow
	 */
	public static void checkAdditionForOverOrUnderFlow(int lhs, int rhs) {
		boolean canUnderflow = (lhs < 0 && rhs < 0);
		boolean canOverFlow = (lhs > 0 && rhs > 0);

		if (canUnderflow) {
			checkAdditionForUnderflow(lhs, rhs);
		} else if (canOverFlow) {
			checkAdditionForOverFlow(lhs, rhs);
		}
	}
}
