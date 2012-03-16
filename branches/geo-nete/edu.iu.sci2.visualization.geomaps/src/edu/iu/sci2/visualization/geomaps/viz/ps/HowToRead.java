package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.geom.Point2D;
import java.io.InputStreamReader;
import java.util.List;

import org.antlr.stringtemplate.StringTemplateGroup;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;


public class HowToRead implements PostScriptable {
	public static final String STRING_TEMPLATE_FILE_PATH =
			"/edu/iu/sci2/visualization/geomaps/viz/stringtemplates/howToRead.stg";
	public static StringTemplateGroup TEMPLATE_GROUP = loadTemplateGroup();
	
	public static final int TARGETED_LINE_LENGTH_IN_CHARACTERS = 60;
	
	public static final double TITLE_FONT_GRAY = 0.0;
	public static final double TEXT_FONT_GRAY = 0.15;
	
	private final Point2D.Double lowerLeft;
	private final PageLayout pageLayout;
	private final String text;

	public HowToRead(Point2D.Double lowerLeft, PageLayout pageLayout, String text) {
		this.lowerLeft = lowerLeft;
		this.pageLayout = pageLayout;
		this.text = text;
	}

	
	@Override
	public String toPostScript() {
		String howToRead = "";
		
		howToRead += "% How to Read" + "\n";
		howToRead += "gsave" + "\n";
		
		howToRead += String.format("%f %f moveto" + "\n", lowerLeft.x, lowerLeft.y);
		
		howToRead += PSUtility.findscalesetfont(pageLayout.titleFont()) + "\n";
		howToRead += PSUtility.setgray(TITLE_FONT_GRAY) + "\n";
		howToRead += "(How to Read this Map) show" + "\n";
		
		howToRead += String.format("%f %f moveto", lowerLeft.x, lowerLeft.y - pageLayout.titleFont().getSize()) + "\n";
		
		howToRead += PSUtility.findscalesetfont(pageLayout.contentFont()) + "\n";
		howToRead += PSUtility.setgray(TEXT_FONT_GRAY) + "\n";
		
		for (String textLine : SentenceSplitter.targetingPhraseLength(TARGETED_LINE_LENGTH_IN_CHARACTERS).split(text)) {
			howToRead += PSUtility.showAndNewLine(textLine,	pageLayout.contentFont().getSize());
		}
		
		howToRead += "grestore" + "\n";
		
		return howToRead;
	}
	
	private static StringTemplateGroup loadTemplateGroup() {
		return new StringTemplateGroup(
				new InputStreamReader(
					GeoMapsAlgorithm.class.getResourceAsStream(STRING_TEMPLATE_FILE_PATH)));
	}
	
	/** TODO Test */
	public static class SentenceSplitter {
		private final int targetedPhraseLength;

		private SentenceSplitter(int targetedPhraseLength) {
			this.targetedPhraseLength = targetedPhraseLength;
		}
		/**
		 * @param targetedPhraseLength	Try to make the total {@link String#length()} of each
		 * 								phrase no greater than this.
		 */
		public static SentenceSplitter targetingPhraseLength(int targetedPhraseLength) {
			return new SentenceSplitter(targetedPhraseLength);
		}
		

		public List<String> split(CharSequence sentence) {
			List<List<CharSequence>> wordLists = splitToWordLists(sentence);
			List<String> phrases = Lists.newArrayList();
			for (List<CharSequence> wordList : wordLists) {
				phrases.add(Joiner.on(" ").join(wordList));
			}
			
			return phrases;
		}
		
		/**
		 * @see Splitter#on(CharMatcher)
		 * @see CharMatcher#WHITESPACE
		 * @see #split(Iterable)
		 */
		public List<List<CharSequence>> splitToWordLists(CharSequence sentence) {
			return splitToWordLists(Splitter.on(CharMatcher.WHITESPACE).trimResults().split(sentence));
		}
		
		public List<List<CharSequence>> splitToWordLists(Iterable<? extends CharSequence> words) {
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
}
