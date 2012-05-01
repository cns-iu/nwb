package edu.iu.cns.visualization.utility.wordwrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import edu.iu.cns.visualization.utility.wordwrap.LineBreaks;

public class LineBreaksTest {
	@Test
	public void testWordsFromPhraseWithQuotesHyphensAndADecimalPoint() {
		assertTrue(Iterables.elementsEqual(
				ImmutableList.of(
						"It ",
						"says: ",
						"\"Add ",
						"three-",
						"halves ",
						"to ",
						"1.5, ",
						"spot-",
						"checking ",
						"as ",
						"you ",
						"go.\""),
				LineBreaks.in("It says: \"Add three-halves to 1.5, spot-checking as you go.\"")));
	}
	
	@Test
	public void testEmptyStringHasNoWords() {
		assertTrue(Iterables.elementsEqual(ImmutableList.of(), LineBreaks.in("")));
	}
	
	@Test
	public void testStringWithNoLineBreaksIsNotBroken() {
		String TARGET = "Unbroken.";
		
		assertTrue(Iterables.elementsEqual(ImmutableList.of(TARGET), LineBreaks.in(TARGET)));
	}
	
	@Test
	public void testConcatenationMatchesInputForNormalSentence() {
		String TARGET = "It says: \"Add three-halves to 1.5, spot-checking as you go.\"";
		
		assertEquals(TARGET, Joiner.on("").join(LineBreaks.in(TARGET)));
	}
	
	@Test
	public void testConcatenationMatchesInputForGarbage() {
		String LINE_SEPARATOR = System.getProperty("line.separator");
		String TARGET = String.format("'--! '%s., ,!!%s!\" . ,.' . !\"%s''\"'\"", LINE_SEPARATOR,
				LINE_SEPARATOR, LINE_SEPARATOR);
		
		assertEquals(TARGET, Joiner.on("").join(LineBreaks.in(TARGET)));
	}
	
	@Test
	public void testLocaleCallDoesNotModifyInstance() {
		LineBreaks words = LineBreaks.in("Ignored.").withLocale(Locale.ENGLISH);
		words.withLocale(Locale.GERMAN);
		
		assertEquals(words.getLocale(), Locale.ENGLISH);
	}
	
	@Test
	public void testLocaleChangeTakesEffectInCopy() {
		LineBreaks words = LineBreaks.in("Ignored.").withLocale(Locale.GERMAN).withLocale(Locale.ENGLISH);
		
		assertEquals(words.getLocale(), Locale.ENGLISH);
	}
}
