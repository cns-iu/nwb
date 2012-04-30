package edu.iu.cns.visualization.utility.linewrap;

import java.text.BreakIterator;
import java.util.Iterator;
import java.util.Locale;

import com.google.common.base.Objects;
import com.google.common.collect.AbstractIterator;

/**
 * An {@link Iterable} over the "words" in a body of text. Here a "word" means the substring between
 * each consecutive pair of line breaks yielded by an instance of
 * {@link BreakIterator#getLineInstance(Locale)}.
 * 
 * <p/>For example, the words in <u>{@code It says: "Add three-halves to 1.5, spot-checking as you 
 * go."} </u> are <u>{@code It }</u>, <u>{@code says: }</u>, <u>{@code "Add }</u>,
 * <u>{@code three-}</u>, <u>{@code halves }</u>, <u>{@code to }</u>, <u>{@code 1.5, }</u>,
 * <u>{@code spot-}</u>, <u>{@code checking }</u>, <u>{@code as }</u>, <u>{@code you }</u>, and
 * <u>{@code go."}</u>.
 * 
 * <p/>The concatenation of the words produced must equal the input.
 */
public final class Words implements Iterable<String> { // TODO A less deceptive name?
	private final String text;
	private final Locale locale;

	private Words(String text, Locale locale) {
		this.text = text;
		this.locale = locale;
	}
	
	/**
	 * An {@link Iterable} over the "words" in {@code text} according to {@link Locale#getDefault()}
	 * .
	 */
	public static Words in(String text) {
		return new Words(text, Locale.getDefault());
	}
	
	/**
	 * A copy of this Words object using this explicit {@link Locale} to find line breaks. The
	 * called instance is <strong>not</strong> modified.
	 */
	public Words withLocale(Locale locale) {
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
				
				// Slide start point to the end of the window for the next round
				start = end;
				
				return word;
			}
			
			return endOfData();
		}
	}
	
	public String getText() {
		return text;
	}

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

	// TODO Or should we not provide a notion of Words equality and leave it up to the results?
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof Words)) {
			return false;
		}
		Words that = (Words) o;

		return Objects.equal(this.text, that.text) && Objects. equal(this.locale, that.locale);
	}
}