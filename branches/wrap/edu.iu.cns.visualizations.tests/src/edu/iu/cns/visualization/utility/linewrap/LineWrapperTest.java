package edu.iu.cns.visualization.utility.linewrap;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

public class LineWrapperTest {
	private static final LineWrapper LINE_WRAPPER = LineWrapper.greedy(LineConstraints.byLength(40));
	private static final ImmutableList<String> TARGET_LINES = ImmutableList.of(
		/////0000000001111111111222222222233333333334
		/////1234567890123456789012345678901234567890
			"Four score and seven years ago our ",
			"fathers brought forth on this continent ",
			"a new nation; conceived in liberty; and ",
			"dedicated to the proposition that all ",
			"men are created equal.");
	private static final String TEXT = Joiner.on("").join(TARGET_LINES);

	@Test
	public void testWrapString() {
		assertEquals(TARGET_LINES,
				ImmutableList.copyOf(LINE_WRAPPER.wrap(TEXT)));
	}

	@Test
	public void testWrapIterableOfString() {
		assertEquals(TARGET_LINES,
				ImmutableList.copyOf(LINE_WRAPPER.wrap(Words.in(TEXT).withLocale(Locale.US))));
	}

	@Test
	public void testWrapIteratorOfString() {
		assertEquals(TARGET_LINES, ImmutableList.copyOf(LINE_WRAPPER.wrap(Words.in(TEXT)
				.withLocale(Locale.US).iterator())));
	}

}
