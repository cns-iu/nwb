package edu.iu.sci2.visualization.geomaps.utility;

import java.util.List;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

/** TODO Test */
public class NaiveSentenceLengthSplitter {
	private final int targetedPhraseLength;

	private NaiveSentenceLengthSplitter(int targetedPhraseLength) {
		this.targetedPhraseLength = targetedPhraseLength;
	}
	/**
	 * @param targetedPhraseLength	Try to make the total {@link String#length()} of each
	 * 								phrase no greater than this.
	 */
	public static NaiveSentenceLengthSplitter targetingPhraseLength(int targetedPhraseLength) {
		return new NaiveSentenceLengthSplitter(targetedPhraseLength);
	}
	
	/** TODO javadoc */
	/**
	 * 
	 * 
	 * @see Splitter#on(CharMatcher)
	 * @see CharMatcher#WHITESPACE
	 */
	public List<String> split(CharSequence sentence) {
		List<List<CharSequence>> wordLists = splitToWordLists(sentence);
		List<String> phrases = Lists.newArrayList();
		for (List<CharSequence> wordList : wordLists) {
			phrases.add(Joiner.on(" ").join(wordList));
		}
		
		return phrases;
	}
	
	
	private List<List<CharSequence>> splitToWordLists(CharSequence sentence) {
		return splitToWordLists(Splitter.on(CharMatcher.WHITESPACE).trimResults().split(sentence));
	}
	
	private List<List<CharSequence>> splitToWordLists(Iterable<? extends CharSequence> words) {
		List<List<CharSequence>> phrases = Lists.newArrayList();
		List<CharSequence> currentPhrase = Lists.newArrayList();
		
		for (CharSequence word : words) {
			if (totalLength(currentPhrase) + word.length() <= targetedPhraseLength) {
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