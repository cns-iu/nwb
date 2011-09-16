package edu.iu.sci2.visualization.geomaps.numberformat;

import java.text.Format;


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
}
