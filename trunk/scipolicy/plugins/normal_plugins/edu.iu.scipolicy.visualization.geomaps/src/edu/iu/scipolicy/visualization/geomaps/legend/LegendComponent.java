package edu.iu.scipolicy.visualization.geomaps.legend;

import java.text.DecimalFormat;

import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public abstract class LegendComponent {
	// Reasonably, this should be between 1 and 3 or so.
	public static final int minimumNumberOfNonzeroFractionalDigits = 2;
	
	public abstract String toPostScript();
	public abstract Range<Double> getRawRange();	
	
	// TODO This is an unfortunate mess -- please improve.
	public String prettyPrintDouble(double value) {
		Range<Double> rawRange = getRawRange();
		double span = rawRange.getMax() - rawRange.getMin();
		
		double exponent;
		if (span == 0) {
			throw new RuntimeException("TODO: Zero span on " + this);
		} else {
			exponent = Math.floor(Math.log10(span));
		}
		
		// Comma-delimit into triples left of the radix.
		String formatPattern = ",###.";
		/* Allow at least the minimum number of digits (if nonzero)
		 * to the right.
		 */
		for (int ii = 0; ii < minimumNumberOfNonzeroFractionalDigits; ii++) {
			formatPattern += "#";
		}
		/* Extend the number of fractional points according to the magnitude
		 * of the span.
		 * Specifically, add a number of fractional digits
		 * (printed only when nonzero)  equal to the base-10 exponent.
		 */
		for (int ii = 0; ii < -exponent; ii++) {
			formatPattern += "#";
		}
		
		DecimalFormat decimalFormat = new DecimalFormat(formatPattern);
		String formattedValue = decimalFormat.format(value);
		
		if ("-0".equals(formattedValue)) {
			return "0";
		} else {
			return formattedValue;
		}
	}
}
