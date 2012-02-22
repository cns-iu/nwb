package edu.iu.sci2.visualization.geomaps.viz.legend.numberformat;

import java.text.Format;

import com.google.common.base.Objects;


/**
 * For pretty-printing doubles.  Used to mark up LegendComponents.
 * 
 * This decorates DecimalFormat -- when "-0" would be returned, "0" is produced
 * instead.
 */
public class UnsignedZeroFormat { // TODO Should this extend and delegate to Format?
	public static final String UNSIGNED_ZERO = "0";
	public static final String NEGATIVE_ZERO = "-0";

	private final Format formatter;
	
	private UnsignedZeroFormat(Format formatter) {
		this.formatter = formatter;
	}
	public static UnsignedZeroFormat wrapping(Format formatter) {
		return new UnsignedZeroFormat(formatter);
	}
	
	
	public String format(double value) {
		String formatted = formatter.format(value);
		
		if (Objects.equal(formatted, NEGATIVE_ZERO)) {
			return UNSIGNED_ZERO;
		}

		return formatted;
	}
}
