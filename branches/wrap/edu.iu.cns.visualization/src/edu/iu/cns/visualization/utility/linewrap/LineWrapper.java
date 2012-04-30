package edu.iu.cns.visualization.utility.linewrap; // TODO Move this entire package to some more general String utilities area?

import java.util.Iterator;

import com.google.common.base.Objects;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

/**
 * Partitions a body of text or sequence of words into a sequence of lines.
 */
public final class LineWrapper {
	private final Strategy strategy;
	
	private LineWrapper(Strategy strategy) {
		this.strategy = strategy;	
	}

	/**
	 * A LineWrapper that places as many words on each line as possible and violates its
	 * LineConstraint only when necessary (as when a single word alone is in violation). This
	 * strategy minimizes line count, not raggedness.
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
			protected String computeNext() { // TODO add comments
				LineBuilder line = new LineBuilder();				
				
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
			
			
			class LineBuilder { // TODO code review carefully, mutable, non-static
				private final StringBuilder text;
	
				LineBuilder() {
					this("");
				}
				
				LineBuilder(String text) {
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
	 * Wrap {@code text} into lines.  Line breaks are discovered using {@link Words#in(String)}.
	 */
	public Iterable<String> wrap(String text) {
		return wrap(Words.in(text));
	}

	/**
	 * Wrap {@code words} into lines. Breaks occur only between elements of {@code words}, never
	 * within.
	 * 
	 * @see Words#in(String)
	 */
	public Iterable<String> wrap(Iterable<String> words) {
		return wrap(words.iterator());
	}
	
	/**
	 * Wrap {@code words} into lines. Breaks occur only between elements of {@code words}, never
	 * within.
	 * 
	 * @see Words#in(String)
	 */
	public Iterable<String> wrap(final Iterator<String> words) {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return strategy.lineIteratorOver(words);
			}
		};
	}
	
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("strategy", strategy).toString();
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(strategy);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null) { return false; }
		if (!(o instanceof LineWrapper)) { return false; }
		LineWrapper that = (LineWrapper) o;

		return Objects.equal(this.strategy, that.strategy);
	}

	private interface Strategy {
		Iterator<String> lineIteratorOver(Iterator<String> words);
	}
}
