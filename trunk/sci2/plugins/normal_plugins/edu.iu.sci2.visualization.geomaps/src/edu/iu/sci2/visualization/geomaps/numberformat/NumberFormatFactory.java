package edu.iu.sci2.visualization.geomaps.numberformat;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import edu.iu.sci2.visualization.geomaps.utility.BinaryCondition;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.utility.RelativeDifferenceLimit;

public class NumberFormatFactory {
	// possible future things:  NumberFormat.getPercentInstance, .getCurrencyInstance
	public static final String GENERAL_FORMAT = "General Number";
	public static final String YEAR_FORMAT = "Year";
	
	public static final SortedSet<String> ALL_FORMATS; 
	static {
		SortedSet<String> formats = new TreeSet<String>();
		formats.add(GENERAL_FORMAT);
		formats.add(YEAR_FORMAT);
		ALL_FORMATS = Collections.unmodifiableSortedSet(formats);
	}
	
	public static final String INTEGER_DIGITS_SEPARATOR = ",";
	public static final double TOLERANCE = 1.0;
	public static final int ATTEMPT_LIMIT = 8;	
	
	public static UnsignedZeroDecimalFormat getNumberFormat(String formatType, double... values) {
		NumberFormat baseFormat = new DecimalFormat(); // may be discarded later...
		
		// Optionally modify the baseFormat. 
		if (formatType.equals(YEAR_FORMAT)) {
			baseFormat.setGroupingUsed(false);
		}
		
		setFormatPrecision(baseFormat, values);
		
		UnsignedZeroDecimalFormat wrappedFormat = new UnsignedZeroDecimalFormat(baseFormat);
		
		return wrappedFormat;
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
				break;
			}
			
			maximumNumberOfFractionDigits++;
		}
		
		// We've modified the formatter that was provided to us, so we
		// don't return anything.
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


	/**
	 * Try to guess what kind of number format should be used for a data column.
	 * 
	 * @param columnName
	 * @param range 
	 * @return
	 */
	public static String guessNumberFormat(String columnName, Range<Double> range) {
		// default to something reasonable
		String numberFormat = GENERAL_FORMAT;
		
		// handle missing data: catch NPE.
		try {
			// Examine column name
			boolean nameContainsYear = columnName.toLowerCase().contains("year");
			
			// Examine number range
			boolean rangeIsInThousands = range.getMin() >= 1000 && range.getMax() < 10000;
	
			if (nameContainsYear && rangeIsInThousands) {
				numberFormat = YEAR_FORMAT;
			}
		} catch (NullPointerException e) {
			// do nothing
		}
		
		return numberFormat;
	}
}
