package edu.iu.cns.visualization.utility.wordwrap;

import static org.junit.Assert.assertEquals;

import java.awt.Font;

import org.junit.Assume;
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
		try {
			assertEquals(0, LineMetrics.widthInFont(Font.decode("Dialog 12")).sizeOf(""));
		} catch (InternalError e) {
			if (e.getMessage() != null && e.getMessage().contains("X11 window server")) {
				Assume.assumeNoException(e);
			}
			throw e;
		}
	}
	
	@Test
	public void testWidthOf3WsInDialog12Is33() {
		try {
			assertEquals(33, LineMetrics.widthInFont(Font.decode("Dialog 12"))
					.sizeOf("WWW"));
		} catch (InternalError e) {
			if (e.getMessage() != null
					&& e.getMessage().contains("X11 window server")) {
				Assume.assumeNoException(e);
			}
			throw e;
		}
	}
}
