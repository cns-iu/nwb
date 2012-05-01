package edu.iu.cns.visualization.utility.wordwrap;

import static org.junit.Assert.*;

import java.awt.Font;

import org.junit.Test;

public class LineMetricsTest {
	@Test
	public void testEmptyStringLengthIs0() {
		assertEquals(0, LineMetrics.length().sizeOf(""));
	}
	
	@Test
	public void testLengthIs19() {
		assertEquals(19, LineMetrics.length().sizeOf("Length should be 19"));
	}
	
	@Test
	public void testEmptyStringWidthIs0() {
		assertEquals(0, LineMetrics.widthInFont(Font.decode("Dialog 12")).sizeOf(""));
	}
	
	@Test
	public void testWidthOf3WsInDialog12Is33() {
		assertEquals(33, LineMetrics.widthInFont(Font.decode("Dialog 12")).sizeOf("WWW"));
	}
}
