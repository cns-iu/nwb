package edu.iu.cns.visualization.utility.linewrap;

/**
 * Decides whether a body of text should fit on one line.
 * 
 * @see LineConstraints
 * @see LineWrapper
 */
public interface LineConstraint {
	/**
	 * @return Whether {@code text} should fit on one line
	 */
	boolean fitsOnOneLine(String text);
}