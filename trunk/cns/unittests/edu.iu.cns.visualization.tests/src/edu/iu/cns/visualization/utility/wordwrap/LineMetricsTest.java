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
			assertEquals(0, LineMetrics.widthInFont(Font.decode("Dialog 12"))
					.sizeOf(""));
		} catch (InternalError e) {
			// Skip this test if we're running headless on a Linux box.
			if (e.getMessage() != null && e.getMessage().contains("X11")) {
				Assume.assumeNoException(e);
			}
			throw e;
		}
	}

	@Test
	public void testWidthOf3WsInDialog12Is33() {
		/*
		 * Different OS have different DPI values. Windows default to 96 while
		 * Linux and Mac are 72. This cause the font width differ from different
		 * platform. There are three alternative solutions:
		 * 1) We can set the DPI but we might not want to set DPI on our test 
		 *    server. Also it could be too much for a test. 
		 * 2) We can change the expected width based on test platform.It could be 
		 *    differ if the user change their system setting. 
		 * 3) Remove this test since it is system dependence.
		 * 
		 * I decided to use the 2) solution for now. If this problem appear
		 * again. You might need to consider the 1) or 3) solution.
		 */
		int expected_width = 30;
		if (System.getProperty("os.name").startsWith("Windows")) {
			expected_width = 33;
		}

		try {
			assertEquals(
				expected_width,
				LineMetrics.widthInFont(Font.decode("Dialog 12")).sizeOf("WWW"));
		} catch (NoClassDefFoundError e) {
			// Skip this test if we're running headless on a Linux box.
			if (e.getMessage() != null && e.getMessage().contains("X11")) {
				Assume.assumeNoException(e);
			}
			throw e;
		}
	}
}
