package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Font;
import java.awt.geom.Point2D;
import java.io.InputStreamReader;
import java.util.List;

import org.antlr.stringtemplate.StringTemplateGroup;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
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
	private final String mapKind;

	public HowToRead(Point2D.Double lowerLeft, PageLayout pageLayout, String text, String mapKind) {
		this.lowerLeft = lowerLeft;
		this.pageLayout = pageLayout;
		this.text = text;
		this.mapKind = mapKind;
	}

	
	@Override
	public String toPostScript() {
		String howToRead = "";
		
		howToRead += "% How to Read" + "\n";
		howToRead += "gsave" + "\n";
		
		howToRead += showTitle(lowerLeft, pageLayout.titleFont());
		
		howToRead += showContent(lowerLeft, pageLayout, text, mapKind);
		
		howToRead += "grestore" + "\n";
		
		return howToRead;
	}


	private static String showTitle(Point2D.Double point, Font font) {
		String title = "";
		
		title += String.format("%f %f moveto" + "\n", point.x, point.y);		
		title += PSUtility.findscalesetfont(font) + "\n";
		title += PSUtility.setgray(TITLE_FONT_GRAY) + "\n";
		title += "(How to Read this Map) show" + "\n";
		
		return title;
	}


	private static String showContent(
			Point2D.Double lowerLeft, PageLayout pageLayout, String text, String mapKind) {
		String content = "";
		
		List<String> lines = NaiveSentenceLengthSplitter.targetingPhraseLength(TARGETED_LINE_LENGTH_IN_CHARACTERS).split(text);
		List<String> restOfLines = lines.subList(1, lines.size());

		Point2D.Double firstLineStartPoint =
				new Point2D.Double(lowerLeft.x, lowerLeft.y - pageLayout.titleFont().getSize());
		Point2D.Double secondLineStartPoint =
				new Point2D.Double(
						firstLineStartPoint.x,
						firstLineStartPoint.y - pageLayout.contentFont().getSize());

		
		content += String.format("%f %f moveto", firstLineStartPoint.x, firstLineStartPoint.y) + "\n";
		
		content += PSUtility.findscalesetfont(pageLayout.contentFont()) + "\n";
		content += PSUtility.setgray(TEXT_FONT_GRAY) + "\n";
		
		
		// TODO Hack to italicize the map name in the text
		String firstLine = lines.get(0);
		Iterable<String> firstLineParts = Splitter.on(mapKind).split(firstLine);
		String beforeMapKind = Iterables.get(firstLineParts, 0);
		String afterMapKind = Iterables.get(firstLineParts, 1);
		
		// Show the bits before and after the map kind in the plain face and the map kind in italics
		content += String.format("(%s) show ", beforeMapKind);
		content += PSUtility.findscalesetfont(pageLayout.contentFont().deriveFont(Font.ITALIC)) +
				" " + String.format("(%s) show", mapKind) + " " +
				PSUtility.findscalesetfont(pageLayout.contentFont());
		content += String.format("(%s) show ", afterMapKind);
		
		
		content += PSUtility.showLines(restOfLines, secondLineStartPoint, pageLayout.contentFont().getSize());
		
		return content;
	}
	
	private static StringTemplateGroup loadTemplateGroup() {
		return new StringTemplateGroup(
				new InputStreamReader(
					GeoMapsAlgorithm.class.getResourceAsStream(STRING_TEMPLATE_FILE_PATH)));
	}
	
	/** TODO Test */
	public static class NaiveSentenceLengthSplitter {
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
