package edu.iu.sci2.visualization.geomaps.utility.numberformat;

import junit.framework.TestCase;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.geomaps.utility.NaiveSentenceLengthSplitter;

public class NaiveSentenceLengthSplitterTest extends TestCase {
	private static final int TARGETED_PHRASE_LENGTH = 40; // Needs to match SPLIT_BASE_STRING below
	private static final ImmutableList<String> SPLIT_BASE_STRING =
			ImmutableList.of(
					//  5  5  5  3  5 12    = 32 and 10 more would make 42
					"Lorem ipsum dolor sit amet; consectetuer" + " ",
					// 10  5  3  4  7  4  7 = 40 and  9 more would make 49
					"adipiscing elit; sed diam nonummy nibh euismod" + " ",
					//  9  2  7  6  5  7  4 = 40 and  9 more would make 49
					"tincidunt ut laoreet dolore magna aliquam erat" + " ",
					//  9                   =  9 and we're done
					"volutpat.");
	
	private static final String TEST_STRING = Joiner.on("").join(SPLIT_BASE_STRING);
	
	private static final Function<String, String> TRIM_FUNCTION =
			new Function<String, String>() {
				public String apply(String s) {
					return s.trim();
				}
			};
	private static final ImmutableList<String> EXPECTED_SPLIT =
			ImmutableList.copyOf(Lists.transform(SPLIT_BASE_STRING, TRIM_FUNCTION));
	
	public static void testSplit() {
		assertEquals(
				EXPECTED_SPLIT,
				NaiveSentenceLengthSplitter
						.targetingPhraseLength(TARGETED_PHRASE_LENGTH)
						.split(TEST_STRING));
	}
}
