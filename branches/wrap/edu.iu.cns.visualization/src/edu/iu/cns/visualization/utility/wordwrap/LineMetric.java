package edu.iu.cns.visualization.utility.wordwrap;

/**
 * A measurer of the left-to-right extent of a line of text.
 * 
 * <p/>
 * It is not generally true that the sum of the measures of the parts of a line equals the measure
 * of the line.
 */
public interface LineMetric {
	/**
	 * @param line A line of text.
	 * @return The left-to-right extent of this line of text.
	 */
	int sizeOf(String line);
}