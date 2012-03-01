package edu.iu.sci2.visualization.geomaps.viz.legend.numberformat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import org.cishell.utilities.ToCaseFunction;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Doubles;

import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.utility.RelativeDifferences;
import edu.iu.sci2.visualization.geomaps.utility.StringPredicates;

public class NumberFormatFactory {
	public static enum NumericFormatType {
		YEAR,
		GENERAL;

		public static NumericFormatType guessFor(
				String columnName, Range<? extends Number> range) {
			// Default to something reasonable
			if ((columnName == null) || (range == null)) {
				return GENERAL;
			}
		
			if (mentionsSomethingLikeYear(columnName) && rangeIsInThousands(range)) {
				return YEAR;
			}
		
			return GENERAL;
		}

		private static boolean rangeIsInThousands(Range<? extends Number> range) {
			return (range.getPointA().doubleValue() >= 1000) &&
				   (range.getPointB().doubleValue() < 10000);
		}

		private static boolean mentionsSomethingLikeYear(final String string) {
			return Iterables.any(
					ImmutableSet.of("year", "yr", "date", "time"),
					StringPredicates.isContainedBy(string, ToCaseFunction.LOWER));
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
	private static void setFormatPrecision(NumberFormat formatter, double[] values) {
		int maximumNumberOfFractionDigits = 0;
		
		// Add another fraction digit and re-check the stopping conditions
		for (int _attempt = 0; _attempt < ATTEMPT_LIMIT; _attempt++) {
			formatter.setMaximumFractionDigits(maximumNumberOfFractionDigits);
			
			double[] approximateValues = new double[values.length];
			
			/* Format each value in values and record that format's double value in
			 * approximateValues. */
			for (int jj = 0; jj < values.length; jj++) {
				try {
					approximateValues[jj] = formatter.parse(formatter.format(values[jj])).doubleValue();
				} catch (ParseException e) {
					// TODO !!!!!!!!!
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
