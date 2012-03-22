package edu.iu.sci2.visualization.geomaps.utility.numberformat;

import java.text.Format;

import com.google.common.base.Objects;


/**
 * Decorates a {@link Format} by producing "0" when the delegate would produce "-0".  All other
 * results pass through unchanged.
 */
public class UnsignedZeroFormat {
	public static final String UNSIGNED_ZERO = "0";
	public static final String NEGATIVE_ZERO = "-0";

	private final Format format;
	
	private UnsignedZeroFormat(Format formatter) {
		this.format = formatter;
	}
	public static UnsignedZeroFormat wrapping(Format formatter) {
		return new UnsignedZeroFormat(formatter);
	}
	
	
	public String format(double value) {
		String result = format.format(value);
		
		return NEGATIVE_ZERO.equals(result) ? UNSIGNED_ZERO : result;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("format", format).toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(format);
	}
	
	@Override
	public boolean equals(Object thatObject) {
		if (this == thatObject) {
			return true;
		}
		if (thatObject == null) {
			return false;
		}
		if (!(thatObject instanceof UnsignedZeroFormat)) {
			return false;
		}
		UnsignedZeroFormat that = (UnsignedZeroFormat) thatObject;

		return Objects.equal(this.format, that.format);
	}	
}
