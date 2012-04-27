package edu.iu.cns.visualization.utility.linewrap;

import java.awt.Font;

import com.google.common.base.Joiner;

public final class LineWrappers {
	private LineWrappers() {}
	
	/**
	 * Stuffs as much text on to each line as it can, violating its LineConstraint only when
	 * necessary (as when a single word alone is in violation).
	 */
	public static LineWrapper greedy(LineConstraint lineConstraint) {
		return new GreedyLineWrapper(lineConstraint);
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
				LineWrappers.greedy(LineConstraints.length(20))
					.wrap(text)));
		System.out.println();
		System.out.println(Joiner.on("\n").join(
				LineWrappers.greedy(LineConstraints.width(100.0, Font.decode("Arial 12")))
					.wrap(text)));
	}
}
