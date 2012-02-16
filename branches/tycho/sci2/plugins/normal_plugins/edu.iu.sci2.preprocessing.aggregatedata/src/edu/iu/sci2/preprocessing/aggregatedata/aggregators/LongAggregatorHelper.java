package edu.iu.sci2.preprocessing.aggregatedata.aggregators;

/**
 * This class helps Long Aggregators. Do not try to instantiate it.
 * 
 * @author dmcoe
 * 
 */
public final class LongAggregatorHelper {
	// Suppress default constructor for non-instantiability
	private LongAggregatorHelper() {
		throw new AssertionError();
	}

	/**
	 * Check to see if adding the left hand side and the right hand side of two
	 * longs would overflow a long if added.
	 * 
	 * @param lhs
	 *            A long to be added to
	 * @param rhs
	 *            A long to be added to
	 * 
	 *            Throws an ArithmeticException if the long would overflow
	 */
	private static void checkAdditionForOverFlow(long lhs, long rhs) {
		boolean wouldAdditionBeOverflow = (lhs > 0 && rhs > Long.MAX_VALUE
				- lhs);
		if (wouldAdditionBeOverflow) {
			throw new ArithmeticException("Adding " + rhs + " and " + lhs
					+ " would overflow the value for long (" + Long.MAX_VALUE
					+ ").");
		}
	}

	
	
	/**
	 * Check to see if subtracting the left hand side and the right hand side of
	 * two longs would underflow if they were added.
	 * 
	 * @param lhs
	 *            A long to be added
	 * @param rhs
	 *            A long to be added
	 * 
	 *            Throws an ArithmeticException if the long would underflow
	 */
	private static void checkAdditionForUnderflow(long lhs, long rhs) {
		boolean wouldAdditionBeUnderflow = (lhs < 0 && rhs < Long.MIN_VALUE
				- lhs);
		if (wouldAdditionBeUnderflow) {
			throw new ArithmeticException("Subtracting " + rhs + " from " + lhs
					+ " would underflow the value for long (" + Long.MIN_VALUE
					+ ").");
		}
	}

	/**
	 * Checks to see if you are in danger of underflowing or overflowing a long
	 * if you added two longs. One or both of the longs can be negative.
	 * 
	 * @param lhs
	 *            A long number to add
	 * @param rhs
	 *            A long number to add
	 * 
	 *            Throws an ArithmeticException if the long would overflow
	 */
	public static void checkAdditionForOverOrUnderFlow(long lhs, long rhs) {
		boolean canUnderflow = (lhs < 0 && rhs < 0);
		boolean canOverFlow = (lhs > 0 && rhs > 0);

		if (canUnderflow) {
			checkAdditionForUnderflow(lhs, rhs);
		} else if (canOverFlow) {
			checkAdditionForOverFlow(lhs, rhs);
		}
	}
}
