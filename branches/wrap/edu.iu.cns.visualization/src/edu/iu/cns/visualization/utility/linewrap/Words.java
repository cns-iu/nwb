package edu.iu.cns.visualization.utility.linewrap;

import java.text.BreakIterator;
import java.util.Iterator;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;

class Words implements Iterable<String> {
	private final String text;

	private Words(String text) {
		this.text = text;
	}
	
	public static Words in(String text) {
		return new Words(text);
	}

	@Override
	public Iterator<String> iterator() {
		return new WordIterator(text);
	}

	private static class WordIterator extends AbstractIterator<String> {
		private final String text;
		private final BreakIterator breakIterator;
		private int start;
		private int end;
		
		public WordIterator(String text) {
			this.text = text;
			this.breakIterator = BreakIterator.getLineInstance(); // TODO Locale
			this.breakIterator.setText(text);		
			this.start = breakIterator.first();
		}
	
		@Override
		protected String computeNext() {
			this.end = breakIterator.next();
	
			if (end != BreakIterator.DONE) {
				final int s = start;
				final int e = end;
				
				start = end;
				
				return text.substring(s, e);
			}
			
			return endOfData();
		}
		
		@SuppressWarnings("unused")
		public static void main(String[] args) { // TODO testing
			System.out.println(Iterators.toString(new WordIterator("")));
			System.out.println(Iterators.toString(new WordIterator("Four.")));
			System.out.println(Iterators.toString(new WordIterator("Four score and seven years ago.")));
		}
	}
}