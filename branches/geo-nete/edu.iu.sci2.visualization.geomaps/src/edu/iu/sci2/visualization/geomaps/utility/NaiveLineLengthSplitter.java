package edu.iu.sci2.visualization.geomaps.utility;

import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/**
 * Wraps text into a list of lines.  Breaks only on whitespace and makes each line as long as
 * possible without over-running the requested total line length in characters except as necessary.
 * Whitespace is clobbered.
 */
public class NaiveLineLengthSplitter {
	private final int targetedLineLength;

	private NaiveLineLengthSplitter(int targetedLineLength) {
		this.targetedLineLength = targetedLineLength;
	}
	/**
	 * @param targetedLineLength	Try to make the total {@link String#length()} of each
	 * 								phrase no greater than this.
	 */
	public static NaiveLineLengthSplitter targetingLineLength(int targetedLineLength) {
		return new NaiveLineLengthSplitter(targetedLineLength);
	}
	
	/**
	 * Splits text on whitespace, then groups the resulting "words" into lines whose total
	 * "word" character count are as close to the requested count as possible, over-running it
	 * only when necessary (as it is when the character count of a single "word" is too large).
	 * 
	 * <p>"Words" are never broken.  Splits happen only on whitespace.
 	 * 
 	 * <p>Your whitespace will be clobbered.
	 * 
	 * @see Splitter#on(CharMatcher)
	 * @see CharMatcher#WHITESPACE
	 */
	public List<String> split(CharSequence text) {
		List<List<CharSequence>> wordLists = splitToWordLists(text);
		List<String> lines = Lists.newArrayList();
		for (List<CharSequence> wordList : wordLists) {
			lines.add(Joiner.on(" ").join(wordList));
		}
		
		return lines;
	}
	
	
	private List<List<CharSequence>> splitToWordLists(CharSequence text) {
		return splitToWordLists(Splitter.on(CharMatcher.WHITESPACE).trimResults().split(text));
	}
	
	private List<List<CharSequence>> splitToWordLists(Iterable<? extends CharSequence> words) {
		List<List<CharSequence>> phrases = Lists.newArrayList();
		List<CharSequence> currentPhrase = Lists.newArrayList();
		
		for (CharSequence word : words) {
			if (totalLength(currentPhrase) + word.length() <= targetedLineLength) {
				currentPhrase.add(word);
			} else {
				if (!currentPhrase.isEmpty()) { phrases.add(currentPhrase);	}
				
				currentPhrase = Lists.newArrayList(word);					
			}
		}
		
		if (!currentPhrase.isEmpty()) { phrases.add(currentPhrase);	}
		
		return phrases;
	}
	
	private static int totalLength(Iterable<? extends CharSequence> strings) {
		int total = 0;
		for (CharSequence s : strings) {
			total += s.length();
		}
		
		return total;
	}	
}