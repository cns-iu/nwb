package edu.iu.sci2.visualization.geomaps.legend;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import edu.iu.sci2.visualization.geomaps.utility.BinaryCondition;
import edu.iu.sci2.visualization.geomaps.utility.RelativeDifferenceLimit;

/* For pretty-printing doubles.  Used to mark up LegendComponents.
 * 
 * This decorates DecimalFormat -- when "-0" would be returned, "0" is produced
 * instead.
 * 
 * Provides a static method to produce an instance of this over an array
 * of doubles.
 */
public class UnsignedZeroDecimalFormat {
	public static final String UNSIGNED_ZERO = "0";
	public static final String NEGATIVE_ZERO = "-0";
	public static final String INTEGER_DIGITS_SEPARATOR = ",";
	public static final double TOLERANCE = 1.0;
	public static final int ATTEMPT_LIMIT = 8;	
	
	private Format formatter;
	
	
	public UnsignedZeroDecimalFormat(Format formatter) {
		this.formatter = formatter;
	}
	
	
	public String format(double value) {
		String formattedValue = formatter.format(value);
		
		if (NEGATIVE_ZERO.equals(formattedValue)) {
			return UNSIGNED_ZERO;
		}

		return formattedValue;
	}
	
	/* Create a DecimalFormat and add "fraction digits" (up to ATTEMPT_LIMIT)
	 * until the format it produces for each given value is:
	 * -- Equal to the original value, up to TOLERANCE
	 * 		(as measured by relative difference)
	 * -- Distinct from its successor in the array.
	 * 		(so that you don't get, for example, [0.00001, 0.00002, 1]
	 * 		and return a DecimalFormat which will produce [0, 0, 1] on those values).
	 */
	protected static UnsignedZeroDecimalFormat createDecimalFormatOver(double... values) {
		DecimalFormat formatter = new DecimalFormat();
		
		if (!isStrictlyIncreasing(values)) {
			throw new IllegalArgumentException(
					"Error: The given values must be strictly increasing.");
		}
		
		BinaryCondition<Double> relativeDifferenceLimit =
			new RelativeDifferenceLimit(TOLERANCE);
		
		int maximumNumberOfFractionDigits = 0;
		
		// Add another fraction digit and re-check the stopping conditions
		for (int ii = 0; ii < ATTEMPT_LIMIT; ii++) {
			formatter.setMaximumFractionDigits(maximumNumberOfFractionDigits);
			
			double[] approximateValues = new double[values.length];
			
			/* Format each value in values
			 * and record that format's double value in approximateValues.
			 */
			for (int jj = 0; jj < values.length; jj++) {
				String formattedValue = formatter.format(values[jj]);
				approximateValues[jj] = Double.valueOf(formattedValue.replace(INTEGER_DIGITS_SEPARATOR, ""));
			}
			
			/* If the format produces strictly increasing values which are
			 * sufficiently close to the original values, then the formatter is
			 * good and we return it now.
			 */
			if (isStrictlyIncreasing(approximateValues)
					&& relativeDifferenceLimit.isSatisfiedBetween(
							toList(values),
							toList(approximateValues))) {
				return new UnsignedZeroDecimalFormat(formatter);
			}
			
			maximumNumberOfFractionDigits++;
		}
		
		return new UnsignedZeroDecimalFormat(formatter);
	}
	
	private static boolean isStrictlyIncreasing(double... values) {
		for (int jj = 0; jj < (values.length - 1); jj++) {			
			if (!(values[jj] < values[jj+1])) {
				return false;
			}
		}
		
		return true;
	}
	
	private static List<Double> toList(double[] values) {
		List<Double> list = new ArrayList<Double>();
		
		for (double value : values) {
			list.add(value);
		}
		
		return list;
	}
}
