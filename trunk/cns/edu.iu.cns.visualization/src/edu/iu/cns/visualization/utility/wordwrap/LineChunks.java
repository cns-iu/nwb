package edu.iu.cns.visualization.utility.wordwrap;

import java.text.BreakIterator;
import java.util.Iterator;
import java.util.Locale;

import com.google.common.base.Objects;
import com.google.common.collect.AbstractIterator;

/**
 * An {@link Iterable} over the "words" between {@link BreakIterator#getLineInstance(Locale) line
 * breaks} in a body of text.
 * 
 * <p/>
 * For example, the words in <u>{@code It says: "Add three-halves to 1.5, spot-checking as you 
 * go."} </u> are <u>{@code It }</u>, <u>{@code says: }</u>, <u>{@code "Add }</u>, <u>{@code three-}
 * </u>, <u>{@code halves }</u>, <u>{@code to }</u>, <u>{@code 1.5, }</u>, <u>{@code spot-}</u>, <u>
 * {@code checking }</u>, <u>{@code as }</u>, <u>{@code you }</u>, and <u>{@code go."}</u>.
 * 
 * <p/>
 * The concatenation of the words will always equal the input.
 */
public final class LineChunks implements Iterable<String> {
	private final String text;
	private final Locale locale;

	private LineChunks(String text, Locale locale) {
		this.text = text;
		this.locale = locale;
	}
	
	/**
	 * An {@link Iterable} over the "words" in {@code text} according to {@link Locale#getDefault()}
	 * .
	 */
	public static LineChunks in(String text) {
		return new LineChunks(text, Locale.getDefault());
	}
	
	/**
	 * A copy of this LineChunks object using this explicit {@link Locale} to find line breaks. The
	 * called instance is <strong>not</strong> modified.
	 */
	public LineChunks withLocale(Locale locale) {
		return new LineChunks(text, locale);
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
				
				// Slide start point to the end of the window for the next round
				start = end;
				
				return word;
			}
			
			return endOfData();
		}
	}
	
	/**
	 * @return The text to scan for words between line breaks.
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return The locale used to determine line breaks.
	 */
	public Locale getLocale() {
		return locale;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("text", text).add("locale", locale).toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(text, locale);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof LineChunks)) {
			return false;
		}
		LineChunks that = (LineChunks) o;

		return Objects.equal(this.text, that.text) && Objects. equal(this.locale, that.locale);
	}
}