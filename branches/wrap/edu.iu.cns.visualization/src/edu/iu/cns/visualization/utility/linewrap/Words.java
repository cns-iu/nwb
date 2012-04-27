package edu.iu.cns.visualization.utility.linewrap;

import java.text.BreakIterator;
import java.util.Iterator;
import java.util.Locale;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;

/**
 * An {@link Iterable} over the "words" in a body of text. Here a "word" means the substring between
 * each consecutive pair of line breaks yielded by an instance of
 * {@link BreakIterator#getLineInstance(Locale)}.
 * 
 * <p/>
 * For example, the words in:
 * <pre>
 * "<em>{@code It says: "Add three-halves to 1.5, spot-checking as you go."}</em>"
 * </pre>
 * are considered to be: 
 * <pre>
 * "<em>{@code It }</em>"
 * "<em>{@code says: }</em>"
 * "<em>{@code "Add }</em>"
 * "<em>{@code three-}</em>"
 * "<em>{@code halves }</em>"
 * "<em>{@code to }</em>"
 * "<em>{@code 1.5, }</em>"
 * "<em>{@code spot-}</em>"
 * "<em>{@code checking }</em>"
 * "<em>{@code as }</em>"
 * "<em>{@code you }</em>"
 * "<em>{@code go."}</em>"
 * </pre>
 * 
 * The concatenation of the words produced must equal the input.
 */
public final class Words implements Iterable<String> {
	private final String text;
	private final Locale locale;

	private Words(String text, Locale locale) {
		this.text = text;
		this.locale = locale;
	}
	
	/**
	 * An {@link Iterable} over the "words" in {@code text} according to the default {@link Locale}.
	 */
	public static Words in(String text) {
		return Words.in(text, Locale.getDefault());
	}
	
	/**
	 * An {@link Iterable} over the "words" in {@code text} where line breaks
	 * are found according to {@code locale}.
	 */
	public static Words in(String text, Locale locale) {
		return new Words(text, locale);
	}

	
	@Override
	public Iterator<String> iterator() {
		return new WordIterator(text, locale);
	}
	
	
	private static final class WordIterator extends AbstractIterator<String> {
		private final String text;
		private final BreakIterator breakIterator;
		
		private int start;
		private int end;
		
		private WordIterator(String text, Locale locale) {
			this.text = text;
			this.breakIterator = BreakIterator.getLineInstance(locale);
			this.breakIterator.setText(text);
			
			// Set start point on the first break
			this.start = breakIterator.first();
		}
	
		@Override
		protected String computeNext() {
			// Slide end point to the next break
			this.end = breakIterator.next();
	
			if (end != BreakIterator.DONE) {
				// Snapshot of "word" in current window
				String word = text.substring(start, end);
				
				// Slide start point to the end of the window
				start = end;
				
				return word;
			}
			
			return endOfData();
		}
	}


	public static void main(String[] args) { // TODO testing
		Words result = Words.in("It says: \"Add three-halves to 1.5, spot-checking as you go.\"");
		
		System.out.println(Joiner.on("\n").join(Iterables.transform(result, new Function<String, String>() {
			@Override
			public String apply(String input) {
				return String.format(" * \"<em>{@code %s}</em>\"", input);
			}
		})));
		
		System.out.println();
		
		System.out.println(Joiner.on(",\n").join(Iterables.transform(result, new Function<String, String>() {
			@Override
			public String apply(String input) {
				return String.format("\"%s\"", input.replace("\"", "\\\""));
			}
		})));
	}
}