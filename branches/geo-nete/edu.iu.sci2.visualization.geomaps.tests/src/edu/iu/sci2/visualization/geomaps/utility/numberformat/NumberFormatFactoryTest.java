package edu.iu.sci2.visualization.geomaps.utility.numberformat;

import junit.framework.TestCase;

import com.google.common.collect.Ranges;

import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.UnsignedZeroFormat;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory.NumericFormatType;




public class NumberFormatFactoryTest extends TestCase {

	public static void testGeneralNumberFormat() {
		UnsignedZeroFormat format = NumberFormatFactory.getNumberFormat(
				NumericFormatType.GENERAL, 1, 5, 10);
		assertEquals("1", format.format(1));
		assertEquals("5", format.format(5));
		assertEquals("10", format.format(10));
	}

	public static void testGeneralNumberFormatThousands() {
		UnsignedZeroFormat format = NumberFormatFactory.getNumberFormat(
				NumericFormatType.GENERAL, 1000, 5000, 10000);
		assertEquals("1,000", format.format(1000));
		assertEquals("5,000", format.format(5000));
		assertEquals("10,000", format.format(10000));
	}
	
	public static void testYearNumberFormatThousands() {
		UnsignedZeroFormat format = NumberFormatFactory.getNumberFormat(
				NumberFormatFactory.NumericFormatType.YEAR, 1000, 5000, 10000);
		assertEquals("1000", format.format(1000));
		assertEquals("5000", format.format(5000));
		assertEquals("10000", format.format(10000));
	}
	
	// A question: since there's no "year 0" in the gregorian calendar
	// http://en.wikipedia.org/wiki/0_(year)
	// What do we do with negative zero in the YEAR_FORMAT case?
	// Possibly too complicated to deal with correctly?
	// Create Java date object to format?  Localized date representations?
	public static void testNegativeZero() {
		UnsignedZeroFormat format = NumberFormatFactory.getNumberFormat(
				NumericFormatType.GENERAL, -1200, 0, 2000);
		assertEquals("-997", format.format(-997));
		assertEquals("0", format.format(-0.000005)); // no negative zero.
		assertEquals("0", format.format(0.000005));
		assertEquals("887", format.format(887));
	}

	public static void testGeneralNumberGuess() {
		NumericFormatType general = NumericFormatType.GENERAL;
		assertEquals(general, NumericFormatType.guessFor("Something", Ranges.closed(30d, 500d)));
		
		assertEquals(general, NumericFormatType.guessFor("Widgets Per Year", Ranges.closed(30d, 500d)));
		assertEquals(general, NumericFormatType.guessFor("Widgets", Ranges.closed(2000d, 3005d)));
		
		assertEquals(general, NumericFormatType.guessFor(null, Ranges.closed(2000.0, 3005.0)));
		assertEquals(general, NumericFormatType.guessFor("Widgets", null));
		assertEquals(general, NumericFormatType.guessFor(null, null));
	}
	
	public static void testYearGuess() {
		NumericFormatType year = NumberFormatFactory.NumericFormatType.YEAR;
		// This is what we want
		assertEquals(year, NumericFormatType.guessFor("Publication Year", Ranges.closed(1997d, 2005d)));
		
		// This is a bad thing about the current implementation:
		//assertEquals(year, NumberFormatFactory.guessNumberFormat("Ants Per Year", new Range<Double>(1997d, 2005d)));
		
		// case insensitive
		assertEquals(year, NumericFormatType.guessFor("Publication yEaR Other Stuff", Ranges.closed(1997d, 2005d)));
	}
	
	public static void testIntegrationGeneral() {
		NumericFormatType formatName = NumericFormatType.guessFor("Eagles", Ranges.closed(2d, 38.4));
		UnsignedZeroFormat format = NumberFormatFactory.getNumberFormat(
				formatName, 2, 18, 38.4);
		assertEquals("18", format.format(18));
	}
	
	
}
