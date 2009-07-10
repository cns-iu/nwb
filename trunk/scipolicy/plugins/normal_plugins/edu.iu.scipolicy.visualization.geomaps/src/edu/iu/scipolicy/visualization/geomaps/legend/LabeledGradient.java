package edu.iu.scipolicy.visualization.geomaps.legend;

import java.awt.Color;

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
public class LabeledGradient implements LegendComponent {
	public static final int DEFAULT_LOWER_LEFT_X_IN_POINTS = 100;
	public static final int DEFAULT_LOWER_LEFT_Y_IN_POINTS = 500;
	public static final int DEFAULT_WIDTH_IN_POINTS = 400;
	public static final int DEFAULT_HEIGHT_IN_POINTS = 10;
	public static final int RESOLUTION = 1;
	public static final double EXTREMA_LABEL_GRAY = 0.0;
	public static final double TYPE_GRAY = 0.0;
	public static final double KEY_GRAY = 0.5;
	public static final double FONT_SIZE = 20.0;
	public static final String FONT_NAME = "Garamond";

	private Range<Double> rawRange;
	private Range<Color> interpolatedRange;
	private double lowerLeftX;
	private double lowerLeftY;
	private double gradientWidth;
	private double gradientHeight;
	private String typeLabel;
	private String keyLabel;

	public LabeledGradient(Range<Double> rawRange, Range<Color> interpolatedRange, String typeLabel, String keyLabel) {
		this(rawRange, interpolatedRange, typeLabel, keyLabel, DEFAULT_LOWER_LEFT_X_IN_POINTS, DEFAULT_LOWER_LEFT_Y_IN_POINTS, DEFAULT_WIDTH_IN_POINTS, DEFAULT_HEIGHT_IN_POINTS);
	}

	public LabeledGradient(Range<Double> rawRange,
			Range<Color> interpolatedRange,
			String typeLabel, String keyLabel, double lowerLeftX, double lowerLeftY, double width, double height) {
		this.rawRange = rawRange;
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

		s += RESOLUTION + " ";

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

		s += "(" + Legend.prettyPrintDouble(rawRange.getMin()) + ")" + " ";
		s += "(" + Legend.prettyPrintDouble(rawRange.getMax()) + ")" + " ";

		s += EXTREMA_LABEL_GRAY + " ";

		s += "(" + typeLabel + ")" + " ";
		s += TYPE_GRAY + " ";
		s += "(" + keyLabel + ")" + " ";
		s += KEY_GRAY + " ";

		s += "{/" + FONT_NAME + " findfont}" + " ";
		s += FONT_SIZE + " ";

		s += "labeledgradient" + "\n";

		return s;
	}
}
