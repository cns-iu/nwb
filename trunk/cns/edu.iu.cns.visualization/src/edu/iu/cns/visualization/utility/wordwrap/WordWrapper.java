package edu.iu.cns.visualization.utility.wordwrap;

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
		return new WordWrapper(new GreedyStrategy(new MetricAtMost(lineMetric, targetedSize)));
	}
	/*
	 * TODO Add a configurator method, perhaps breakLongWords(), that permits this object to wrap at
	 * non-line-breaks when necessary to keep each line within the targetedSize.
	 */

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
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("lineMetric", lineMetric)
					.add("targetedSize", targetedSize).toString();
		}
	}

	private static final class GreedyStrategy implements Strategy {
		private final Predicate<String> shouldFitOnOneLine;

		public GreedyStrategy(Predicate<String> shouldFitOnOneLine) {
			this.shouldFitOnOneLine = shouldFitOnOneLine;
		}

		@Override
		public Iterator<String> lineIteratorOver(Iterator<String> words) {
			return new LineIterator(words);
		}
		
		@Override
		public String toString() {
			return Objects.toStringHelper(this).add("shouldFitOnOneLine", shouldFitOnOneLine)
					.toString();
		}
		
		private final class LineIterator extends AbstractIterator<String> {
			private final PeekingIterator<String> words;
	
			public LineIterator(Iterator<String> words) {
				this.words = Iterators.peekingIterator(words);
			}

			@Override
			protected String computeNext() {
				LineBuilder line = new LineBuilder();				
				
				while (words.hasNext()) {
					// Peek at the coming word and offer it to the current line
					boolean accepted = line.offer(words.peek());
					
					if (accepted) {
						// Word was accepted, pop it off and go on
						words.next();
					} else {
						// Couldn't accept the coming word, leave it on the iterator for the next
						// line to handle
						break;
					}
				}
				
				if (line.isEmpty()) {
					// The line can only be empty here if we've run out of words
					assert (!(words.hasNext()));
					
					return endOfData();
				}
				
				return line.toString();
			}
			
			
			final class LineBuilder {
				private final StringBuilder text;
	
				LineBuilder() {
					this("");
				}
				
				LineBuilder(String text) {
					this.text = new StringBuilder(text);
				}
				
				/**
				 * Accept and append {@code word} if it fits or if the line is
				 * empty.
				 * 
				 * @param word
				 *            A word to offer for appending to this line
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
	
	/**
	 * Wraps {@code text} into lines. Breaks will be inserted only at line breaks as found by
	 * {@link LineChunks#in(String)}.
	 */
	public Iterable<String> wrap(String text) {
		return wrap(LineChunks.in(text));
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
	 * 
	 * @param words
	 *            The backing iterator. This method assumes ownership of it, so clients should cease
	 *            making direct calls to it after calling this method.
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
