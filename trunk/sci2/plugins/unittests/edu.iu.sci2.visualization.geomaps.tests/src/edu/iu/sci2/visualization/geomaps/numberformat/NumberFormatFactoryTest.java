package edu.iu.sci2.visualization.geomaps.numberformat;

import edu.iu.sci2.visualization.geomaps.utility.Range;
import junit.framework.TestCase;




public class NumberFormatFactoryTest extends TestCase {

	public void testGeneralNumberFormat() {
		UnsignedZeroDecimalFormat format = NumberFormatFactory.getNumberFormat(
				NumberFormatFactory.GENERAL_FORMAT, 1, 5, 10);
		assertEquals("1", format.format(1));
		assertEquals("5", format.format(5));
		assertEquals("10", format.format(10));
	}

	public void testGeneralNumberFormatThousands() {
		UnsignedZeroDecimalFormat format = NumberFormatFactory.getNumberFormat(
				NumberFormatFactory.GENERAL_FORMAT, 1000, 5000, 10000);
		assertEquals("1,000", format.format(1000));
		assertEquals("5,000", format.format(5000));
		assertEquals("10,000", format.format(10000));
	}
	
	public void testYearNumberFormatThousands() {
		UnsignedZeroDecimalFormat format = NumberFormatFactory.getNumberFormat(
				NumberFormatFactory.YEAR_FORMAT, 1000, 5000, 10000);
		assertEquals("1000", format.format(1000));
		assertEquals("5000", format.format(5000));
		assertEquals("10000", format.format(10000));
	}
	
	// A question: since there's no "year 0" in the gregorian calendar
	// http://en.wikipedia.org/wiki/0_(year)
	// What do we do with negative zero in the YEAR_FORMAT case?
	// Possibly too complicated to deal with correctly?
	// Create Java date object to format?  Localized date representations?
	public void testNegativeZero() {
		UnsignedZeroDecimalFormat format = NumberFormatFactory.getNumberFormat(
				NumberFormatFactory.GENERAL_FORMAT, -1200, 0, 2000);
		assertEquals("-997", format.format(-997));
		assertEquals("0", format.format(-0.000005)); // no negative zero.
		assertEquals("0", format.format(0.000005));
		assertEquals("887", format.format(887));
	}

	public void testGeneralNumberGuess() {
		String general = NumberFormatFactory.GENERAL_FORMAT;
		assertEquals(general, NumberFormatFactory.guessNumberFormat("Something", new Range<Double>(30d, 500d)));
		
		assertEquals(general, NumberFormatFactory.guessNumberFormat("Widgets Per Year", new Range<Double>(30d, 500d)));
		assertEquals(general, NumberFormatFactory.guessNumberFormat("Widgets", new Range<Double>(2000d, 3005d)));
		
		assertEquals(general, NumberFormatFactory.guessNumberFormat(null, new Range<Double>(2000.0, 3005.0)));
		assertEquals(general, NumberFormatFactory.guessNumberFormat("Widgets", null));
		assertEquals(general, NumberFormatFactory.guessNumberFormat(null, null));
	}
	
	public void testYearGuess() {
		String year = NumberFormatFactory.YEAR_FORMAT;
		// This is what we want
		assertEquals(year, NumberFormatFactory.guessNumberFormat("Publication Year", new Range<Double>(1997d, 2005d)));
		
		// This is a bad thing about the current implementation:
		//assertEquals(year, NumberFormatFactory.guessNumberFormat("Ants Per Year", new Range<Double>(1997d, 2005d)));
		
		// case insensitive
		assertEquals(year, NumberFormatFactory.guessNumberFormat("Publication yEaR Other Stuff", new Range<Double>(1997d, 2005d)));
	}
	
	public void testIntegrationGeneral() {
		String formatName = NumberFormatFactory.guessNumberFormat("Eagles", new Range<Double>(2d, 38.4));
		UnsignedZeroDecimalFormat format = NumberFormatFactory.getNumberFormat(
				formatName, 2, 18, 38.4);
		assertEquals("18", format.format(18));
	}
	
	
}
