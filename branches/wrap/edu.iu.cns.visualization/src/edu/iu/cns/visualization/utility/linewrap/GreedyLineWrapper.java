package edu.iu.cns.visualization.utility.linewrap; // TODO Move all of this to some more general String utilities area?

import java.util.List;

import com.google.common.base.Functions;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @see LineWrappers#greedy(LineConstraint)
 */
final class GreedyLineWrapper implements LineWrapper { // TODO where to put unit tests?
	private final LineConstraint lineConstraint;

	GreedyLineWrapper(LineConstraint lineConstraint) {
		this.lineConstraint = lineConstraint;
	}
		
	/**
	 * @see edu.iu.cns.visualization.utility.linewrap.LineWrapper#wrap(java.lang.String)
	 */
	@Override
	public List<String> wrap(String text) {
		List<Line> lines = Lists.newArrayList();
		Line currentLine = new Line();
		
		for (String word : Words.in(text)) {
			if (!currentLine.canAppend(word)) {
				// Reset
				lines.add(currentLine);
				currentLine = new Line();
			}
			
			currentLine.append(word);
		}
		
		// Finish last line
		lines.add(currentLine);
		
		return Lists.transform(lines, Functions.toStringFunction());
	}

	
	private class Line {
		private final StringBuilder text;

		private Line() {
			this("");
		}
		
		private Line(String text) {
			this.text = new StringBuilder(text);
		}
		
		boolean canAppend(String moreText) {
			return lineConstraint.fitsOnOneLine(text.toString() + moreText);
		}
		
		void append(String moreText) {
			Preconditions.checkArgument(canAppend(moreText)); // TODO message
			
			text.append(moreText);
		}
		
		@Override
		public String toString() {
			return text.toString();
		}
	}
}
