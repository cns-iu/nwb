package edu.iu.cns.visualization.utility.linewrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class WordsTest {
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
				Words.in("It says: \"Add three-halves to 1.5, spot-checking as you go.\"")));
	}
	
	@Test
	public void testConcatenationMatchesInputForNormalSentence() {
		String TARGET = "It says: \"Add three-halves to 1.5, spot-checking as you go.\"";
		
		assertEquals(TARGET, Joiner.on("").join(Words.in(TARGET)));
	}
	
	@Test
	public void testConcatenationMatchesInputForGarbage() {
		String LINE_SEPARATOR = System.getProperty("line.separator");
		String TARGET = String.format("'--! '%s., ,!!%s!\" . ,.' . !\"%s''\"'\"", LINE_SEPARATOR,
				LINE_SEPARATOR, LINE_SEPARATOR);
		
		assertEquals(TARGET, Joiner.on("").join(Words.in(TARGET)));
	}
}
