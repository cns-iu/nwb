package edu.iu.cns.visualization.utility.linewrap; // TODO Move this entire package to some more general String utilities area?

import java.text.BreakIterator;
import java.util.Iterator;
import java.util.Locale;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

/**
 * Partitions a body of text or sequence of words into a sequence of lines. Breaks may occur only at
 * line breaks as determined by {@link BreakIterator#getLineInstance(java.util.Locale)}.
 * 
 * <p/>The default {@link Locale} is {@link Locale#getDefault()}.
 */
public final class LineWrapper {
	private final Strategy strategy;
	private final Locale locale;
	
	private LineWrapper(Strategy strategy) {
		this(strategy, Locale.getDefault());
	}

	private LineWrapper(Strategy strategy, Locale locale) {
		this.strategy = strategy;
		this.locale = locale;		
	}

	/**
	 * A LineWrapper that places as many words on each line as possible and violates its
	 * LineConstraint only when necessary (as when a single word alone is in violation).
	 */
	public static LineWrapper greedy(LineConstraint lineConstraint) {
		return new LineWrapper(new Greedy(lineConstraint));
	}

	private static final class Greedy implements Strategy {
		private final LineConstraint lineConstraint;
	
		Greedy(LineConstraint lineConstraint) {
			this.lineConstraint = lineConstraint;
		}
			
		@Override
		public Iterator<String> lineIteratorOver(Iterator<String> words) {
			return new LineIterator(lineConstraint, words);
		}
		
		private static final class LineIterator extends AbstractIterator<String> {
			private final LineConstraint lineConstraint;
			private final PeekingIterator<String> words;
	
			LineIterator(LineConstraint lineConstraint, Iterator<String> words) {
				this.lineConstraint = lineConstraint;
				this.words = Iterators.peekingIterator(words);
			}
	
			@Override
			protected String computeNext() {
				Line line = new Line();				
				
				while (words.hasNext()) {
					if (!(line.canFit(words.peek()))) {
						if (line.isEmpty()) {
							line.append(words.next());
						}
						
						return line.toString();
					}
					
					line.append(words.next());
				}
				
				if (!(line.isEmpty())) {
					return line.toString();
				}
				
				return endOfData();
			}
			
			
			class Line { // TODO code review carefully
				private final StringBuilder text;
	
				Line() {
					this("");
				}
				
				Line(String text) {
					this.text = new StringBuilder(text);
				}
				
				boolean canFit(String moreText) {
					return lineConstraint.fitsOnOneLine(text.toString() + moreText);
				}
				
				boolean isEmpty() {
					return text.toString().isEmpty();
				}
				
				void append(String moreText) {
					text.append(moreText);
				}
				
				@Override
				public String toString() {
					return text.toString();
				}
			}
		}		
	}

	/**
	 * Returns a LineWrapper equivalent to this one but with the specified {@code locale} used to
	 * determine line breaks.
	 */
	public LineWrapper locale(Locale locale) { // TODO emphasize that this is a copy
		Splitter.on("").omitEmptyStrings();
		return new LineWrapper(strategy, locale);
	}
	
	
	public Iterable<String> wrap(String text) { // TODO Note that this uses locale?
		return wrap(Words.in(text, locale).iterator());
	}
	
	public Iterable<String> wrap(final Iterator<String> words) { // TODO Note that this doesn't use locale?
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return strategy.lineIteratorOver(words);
			}
		};
	}
	
	private interface Strategy { // TODO Could expose a factory taking this
		Iterator<String> lineIteratorOver(Iterator<String> words);
	}

	public static void main(String[] args) { // TODO remove
		String text = "Four score and seven years ago our fathers brought forth on this " +
				"continent a new nation; conceived in liberty; and dedicated to the proposition " +
				"that all men are created equal. Now we are engaged in a great civil war; " +
				"testing whether that nation; or any nation; so conceived and so dedicated; " +
				"can long endure. We are met on a great battle-field of that war. We have come " +
				"to dedicate a portion of that field; as a final resting place for those who " +
				"here gave their lives that that nation might live. It is altogether fitting " +
				"and proper that we should do this.";
		
		System.out.println(Joiner.on("\n").join(
				toStringInQuotes(LineWrapper.greedy(LineConstraints.length(20)).wrap(text))));
		
//		System.out.println();
//		
//		System.out.println(Joiner.on("\n").join(
//				toStringInQuotes(LineWrapper.greedy(
//						LineConstraints.targetWidth(-100.0, Font.decode("Arial 12"))).wrap(text))));
	}
	
	private static <E> Iterable<String> toStringInQuotes(Iterable<? extends E> elements) { // TODO Remove
		return Iterables.transform(elements, new Function<E, String>() {
			@Override
			public String apply(E input) {
				return String.format("\"%s\"", input);
			}
		});
	}
}
