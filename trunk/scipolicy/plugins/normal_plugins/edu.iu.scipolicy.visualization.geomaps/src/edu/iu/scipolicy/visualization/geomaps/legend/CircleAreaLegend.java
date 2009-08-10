package edu.iu.scipolicy.visualization.geomaps.legend;

import edu.iu.scipolicy.visualization.geomaps.printing.Circle;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

/* Create PostScript to draw two Circles representing the extrema of the
 * interpolated range and label each with the respective value in the raw
 * range (which is intended to be the respective value before scaling and
 * before interpolation).
 * 
 * This legend component has two captions:
 * - typeLabel would be like "Circle Area"
 * - keyLabel would be like "Number of papers published"
 */
public class CircleAreaLegend extends LegendComponent {
	public static final double CIRCLE_GRAY = 0.5;
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
	private double rawMidArea;
	private double areaMidrange;
	private Range<Double> interpolatedRange;
	private String typeLabel;
	private String keyLabel;
	/* These "lower lefts" are the lower left corners of the key text,
	 * e.g., "Population" or "Number of Works Authored" or what have you.
	 * This is an ex post facto change so that the legend components align
	 * vertically by their type and key text labels rather than by their
	 * extrema labels (as it was before).
	 */
	private double lowerLeftX;
	private double lowerLeftY;		


	public CircleAreaLegend(Range<Double> areaRange,
					  String scaling,
					  double rawMidArea,
					  double areaMidrange,
					  Range<Double> interpolatedAreaRange,
					  String typeLabel,
					  String keyLabel,
					  double lowerLeftX,
					  double lowerLeftY) {
		this.rawRange = areaRange;
		this.scaling = scaling;
		this.rawMidArea = rawMidArea;
		this.areaMidrange = areaMidrange;
		this.interpolatedRange = interpolatedAreaRange;
		this.typeLabel = typeLabel;
		this.keyLabel = keyLabel;
		this.lowerLeftX = lowerLeftX;
		this.lowerLeftY = lowerLeftY;
	}

	
	/* TODO? Draw circles using the line width set
	 * in CirclePrinter.CIRCLE_LINE_WIDTH?
	 */
	public String toPostScript() {
		String s = "";

		s += lowerLeftX + " ";
		s += lowerLeftY + " ";
		
		s += Circle.calculateRadiusFromArea(interpolatedRange.getMin()) + " ";
		s += Circle.calculateRadiusFromArea(areaMidrange) + " ";
		s += Circle.calculateRadiusFromArea(interpolatedRange.getMax()) + " ";

		s += CIRCLE_GRAY + " ";

		s += "(" + prettyPrintDouble(rawRange.getMin()) + ")" + " ";
		s += "(" + prettyPrintDouble(rawMidArea) + ")" + " ";
		s += "(" + prettyPrintDouble(rawRange.getMax()) + ")" + " ";
		s += EXTREMA_LABEL_GRAY + " ";
		s += EXTREMA_LABEL_FONT_SIZE + " ";

		s += "(" + typeLabel + ")" + " ";
		s += TYPE_LABEL_GRAY + " ";
		s += TYPE_LABEL_FONT_SIZE + " ";
		
		s += "(" + "(" + scaling + ")" + ")" + " ";
		s += SCALING_LABEL_GRAY + " ";
		
		s += "(" + keyLabel + ")" + " ";
		s += KEY_LABEL_GRAY + " ";
		s += KEY_LABEL_FONT_SIZE + " ";

		s += "{/" + FONT_NAME + " findfont}" + " ";

		s += "arealegend" + "\n";

		return s;
	}
	
	public Range<Double> getRawRange() {
		return rawRange;
	}
}
