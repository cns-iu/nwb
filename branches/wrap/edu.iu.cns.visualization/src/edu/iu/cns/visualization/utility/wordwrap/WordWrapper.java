package edu.iu.cns.visualization.utility.wordwrap; // TODO Move this entire package to some more general String utilities area?

import java.util.Iterator;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

/**
 * Wraps a body of text (or a sequence of unbreakable words) into a sequence of lines.
 */
public final class WordWrapper {
	private final Strategy strategy;
	
	private WordWrapper(Strategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * A word wrapper that greedily places as many words on each line as possible, exceeding the
	 * {@code targetedSize} (as measured by {@code lineMetric}) only when necessary (as when a
	 * single word alone is too big). This strategy minimizes line count, not raggedness.
	 */
	public static WordWrapper fewestLines(final LineMetric lineMetric, final int targetedSize) {
		return new WordWrapper(new Greedy(new MetricAtMost(lineMetric, targetedSize)));
	}

	private static final class MetricAtMost implements Predicate<String> {
		private final LineMetric lineMetric;
		private final int targetedSize;

		private MetricAtMost(LineMetric lineMetric, int targetedSize) {
			this.lineMetric = lineMetric;
			this.targetedSize = targetedSize;
		}

		@Override
		public boolean apply(String line) {
			return lineMetric.sizeOf(line) <= targetedSize;
		}
	}

	private static final class Greedy implements Strategy {
		private final Predicate<String> shouldFitOnOneLine;

		public Greedy(Predicate<String> shouldFitOnOneLine) {
			this.shouldFitOnOneLine = shouldFitOnOneLine;
		}

		@Override
		public Iterator<String> lineIteratorOver(Iterator<String> words) {
			return new LineIterator(words);
		}
		
		private final class LineIterator extends AbstractIterator<String> { // TODO non-static?
			private final PeekingIterator<String> words;
	
			public LineIterator(Iterator<String> words) {
				this.words = Iterators.peekingIterator(words);
			}

			@Override
			protected String computeNext() {
				LineBuilder line = new LineBuilder();				
				
				while (words.hasNext()) {
					boolean accepted = line.offer(words.peek());
					
					if (accepted) {
						// The offered word was accepted, consume it and continue.
						words.next();
					} else {
						// Finish the line.  The offending word has not been consumed.
						return line.toString();
					}
				}
				
				// Grab the last line
				if (!(line.isEmpty())) { // TODO Any way to redo the above logic so that this isn't necessary?
					return line.toString();
				}
				
				return endOfData();
			}
			
			
			final class LineBuilder { // TODO code review carefully.. non-static, mutable
				private final StringBuilder text;
	
				LineBuilder() {
					this("");
				}
				
				LineBuilder(String text) {
					this.text = new StringBuilder(text);
				}
				
				/**
				 * @param A word to offer for appending to this line
				 * @return Whether the line accepts the new word.
				 */
				boolean offer(String word) {
					if (canFit(word) || this.isEmpty()) {
						append(word);
						return true;
					}
					
					return false;
				}
				
				boolean isEmpty() {
					return text.toString().isEmpty();
				}
				
				private boolean canFit(String moreText) {
					return shouldFitOnOneLine.apply(text.toString() + moreText);
				}
				
				private void append(String moreText) {
					text.append(moreText);
				}
				
				@Override
				public String toString() {
					return text.toString();
				}
			}
		}		
	}
	
//	public static WordWrapper leastRagged(LineMetric lineMetric, int targetedSize) { // TODO delete
//		return new WordWrapper(new Knuth(lineMetric, targetedSize));
//	}
	
	/**
	 * Wraps {@code text} into lines. Breaks will be inserted only at line breaks as found by
	 * {@link LineBreaks#in(String)}.
	 */
	public Iterable<String> wrap(String text) {
		return wrap(LineBreaks.in(text));
	}

	/**
	 * Wraps {@code words} into lines. Breaks will be inserted only between elements of
	 * {@code words}, never within.
	 */
	public Iterable<String> wrap(Iterable<String> words) {
		return wrap(words.iterator());
	}
	
	/**
	 * Wraps {@code words} into lines. Breaks occur only between elements of {@code words}, never
	 * within.
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
		if (!(o instanceof WordWrapper)) { return false; }
		WordWrapper that = (WordWrapper) o;

		return Objects.equal(this.strategy, that.strategy);
	}

	private interface Strategy {
		Iterator<String> lineIteratorOver(Iterator<String> words);
	}
}
