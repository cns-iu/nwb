package edu.iu.scipolicy.visualization.geomaps.legend;

import java.awt.Color;

import edu.iu.scipolicy.visualization.geomaps.utility.Constants;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

/* Create PostScript to draw a color gradient representing the extrema of the
 * interpolated range and label each end with the respective value in the raw
 * range (which is intended to be the respective value before scaling and
 * before interpolation).
 * 
 * This legend component has two captions:
 * - typeLabel would be like "Blah Color"
 * - keyLabel would be like "Number of papers published"
 */
public class ColorLegend extends LegendComponent {
	/* Width of each cell of the gradient in points.
	 * A lesser positive integer means more fine, or less blocky.
	 */
	public static final int GRADIENT_RESOLUTION = 1;
	public static final double EXTREMA_LABEL_GRAY = 0.0;
	public static final double EXTREMA_LABEL_FONT_SIZE = 8;
	public static final double TYPE_LABEL_GRAY = 0.0;
	public static final double TYPE_LABEL_FONT_SIZE = 10;
	public static final double SCALING_LABEL_GRAY = 0.5;
	public static final double KEY_LABEL_GRAY = 0.5;
	public static final double KEY_LABEL_FONT_SIZE = 8;
	public static final String FONT_NAME = Constants.FONT_NAME;	

	private Range<Double> rawRange;
	private String scaling;
	private double rawMidColorQuantity;
	private Range<Color> interpolatedRange;
	/* These "lower lefts" are the lower left corners of the key text,
	 * e.g., "Population" or "Number of Works Authored" or what have you.
	 * This is an ex post facto change so that the legend components align
	 * vertically by their type and key text labels rather than by their
	 * extrema labels (as it was before).
	 */
	private double lowerLeftX;
	private double lowerLeftY;
	private double gradientWidth;
	private double gradientHeight;
	private String typeLabel;
	private String keyLabel;	
	

	public ColorLegend(Range<Double> rawRange,
						   String scaling,
						   double rawMidColorQuantity,
						   Range<Color> interpolatedRange,
						   String typeLabel,
						   String keyLabel,
						   double lowerLeftX,
						   double lowerLeftY,
						   double width, double
						   height) {
		this.rawRange = rawRange;
		this.scaling = scaling;
		this.rawMidColorQuantity = rawMidColorQuantity;
		this.interpolatedRange = interpolatedRange;
		this.typeLabel = typeLabel;
		this.keyLabel = keyLabel;
		this.lowerLeftX = lowerLeftX;
		this.lowerLeftY = lowerLeftY;
		this.gradientWidth = width;
		this.gradientHeight = height;
	}

	public String toPostScript() {
		String s = "";

		s += lowerLeftX + " ";
		s += lowerLeftY + " ";

		s += gradientWidth + " ";
		s += gradientHeight + " ";

		s += GRADIENT_RESOLUTION + " ";

		float[] minColorComponents = new float[3];
		interpolatedRange.getMin().getColorComponents(minColorComponents);

		s += minColorComponents[0] + " ";
		s += minColorComponents[1] + " ";
		s += minColorComponents[2] + " ";

		float[] maxColorComponents = new float[3];
		interpolatedRange.getMax().getColorComponents(maxColorComponents);

		s += maxColorComponents[0] + " ";
		s += maxColorComponents[1] + " ";
		s += maxColorComponents[2] + " ";

		s += "(" + prettyPrintDouble(rawRange.getMin()) + ")" + " ";
		s += "(" + prettyPrintDouble(rawMidColorQuantity) + ")" + " ";
		s += "(" + prettyPrintDouble(rawRange.getMax()) + ")" + " ";

		s += EXTREMA_LABEL_GRAY + " ";
		s += EXTREMA_LABEL_FONT_SIZE + " ";

		s += "(" + typeLabel + ")" + " ";
		s += TYPE_LABEL_GRAY + " ";
		s += TYPE_LABEL_FONT_SIZE + " ";
		
		s += "(" + "(" + scaling+ ")" + ")" + " ";
		s += SCALING_LABEL_GRAY + " ";
		
		s += "(" + keyLabel + ")" + " ";
		s += KEY_LABEL_GRAY + " ";
		s += KEY_LABEL_FONT_SIZE + " ";

		s += "{/" + FONT_NAME + " findfont}" + " ";

		s += "labeledgradient" + "\n";

		return s;
	}

	public Range<Double> getRawRange() {
		return rawRange;
	}
}
