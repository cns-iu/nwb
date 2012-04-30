package edu.iu.cns.visualization.utility.linewrap;

import static org.junit.Assert.*;

import java.awt.Font;

import org.junit.Test;

public class LineConstraintsTest {

	@Test
	public void testShortPhraseFitsOnOneLine() {
		assertTrue(LineConstraints.byLength(10).fitsOnOneLine("Hi!"));
	}

	@Test
	public void testLongPhraseDoesNotFitOnOneLine() {
		assertFalse(LineConstraints.byLength(10).fitsOnOneLine(
				"I am the very model of a modern major-general."));
	}

	@Test
	public void testNarrowPhraseFitsOnOneLine() {
		assertTrue(LineConstraints.byWidth(30, Font.decode(null)).fitsOnOneLine("Hi!"));
	}
	
	@Test
	public void testWidePhraseDoesNotFitOnOneLine() {
		assertFalse(LineConstraints.byWidth(30, Font.decode(null)).fitsOnOneLine(
				"I am the very model of a modern major-general."));
	}

}
