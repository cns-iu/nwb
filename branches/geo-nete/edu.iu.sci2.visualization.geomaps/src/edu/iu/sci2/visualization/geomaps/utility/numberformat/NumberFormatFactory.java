package edu.iu.sci2.visualization.geomaps.utility.numberformat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.cishell.utilities.ToCaseFunction;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import com.google.common.primitives.Doubles;

import edu.iu.sci2.visualization.geomaps.utility.RelativeDifferences;
import edu.iu.sci2.visualization.geomaps.utility.StringPredicates;

public class NumberFormatFactory {
	public static enum NumericFormatType {
		YEAR,
		GENERAL;

		private static final Range<Double> TYPICAL_YEAR_RANGE = Ranges.closed(1200.0, 3000.0);

		/**
		 * Returns {@link NumericFormatType#GENERAL} if either parameter is null.
		 */
		public static NumericFormatType guessFor(String columnName, Range<Double> range) {
			// Default to something reasonable
			if ((columnName == null) || (range == null)) {
				return GENERAL;
			}
		
			if (isYearish(columnName) && isYearish(range)) {
				return YEAR;
			}
		
			return GENERAL;
		}

		private static boolean isYearish(Range<Double> range) {
			return TYPICAL_YEAR_RANGE.encloses(range);
		}

		private static boolean isYearish(final String string) {
			return Iterables.any(
					ImmutableSet.of("year", "yr", "date", "time"),
					StringPredicates.substringOf(string, ToCaseFunction.LOWER));
		}
	}
		
	public static final int ATTEMPT_LIMIT = 8;
	
	public static UnsignedZeroFormat getNumberFormat(NumericFormatType numericFormatType, double... values) {
		NumberFormat baseFormat = new DecimalFormat(); // may be discarded later...
		
		// Optionally modify the baseFormat
		if (numericFormatType.equals(NumericFormatType.YEAR)) {
			baseFormat.setGroupingUsed(false);
		}
		
		setFormatPrecision(baseFormat, values);
		
		return UnsignedZeroFormat.wrapping(baseFormat);
	}
	
	
	/* Modify a NumberFormat and add "fraction digits" (up to ATTEMPT_LIMIT)
	 * until the format it produces for each given value is:
	 * -- Equal to the original value, up to TOLERANCE
	 * 		(as measured by relative difference)
	 * -- Distinct from its successor in the array.
	 * 		(so that you don't get, for example, [0.00001, 0.00002, 1]
	 * 		and return a DecimalFormat which will produce [0, 0, 1] on those values).
	 */
	/**
	 * @see java.text.NumberFormat#setMaximumFractionDigits(int)
	 */
	private static void setFormatPrecision(NumberFormat formatter, double... values) {
		int maximumNumberOfFractionDigits = 0;
		
		// Add another fraction digit and re-check the stopping conditions
		for (int _attempt = 0; _attempt < ATTEMPT_LIMIT; _attempt++) {
			formatter.setMaximumFractionDigits(maximumNumberOfFractionDigits);
			
			double[] approximateValues = new double[values.length];
			
			/* Format each value in values and record that format's double value in
			 * approximateValues. */
			for (int ii = 0; ii < values.length; ii++) {
				try {
					approximateValues[ii] = formatter.parse(formatter.format(values[ii])).doubleValue();
				} catch (ParseException e) {
					throw new RuntimeException("TODO Failed to parse formatter result.", e);
				}
			}
			
			/* If the format produces strictly increasing values which are
			 * sufficiently close to the original values, then the formatter is
			 * good and we return it now.
			 */
			if (Ordering.natural().isStrictlyOrdered(Doubles.asList(approximateValues))
					&& RelativeDifferences.DEFAULT_EQUIVALENCE.pairwise().equivalent(
							Doubles.asList(values),
							Doubles.asList(approximateValues))) {
				break;
			}
			
			maximumNumberOfFractionDigits++;
		}
		
		// We've modified the formatter that was provided to us, so we don't return anything.
	}
}
