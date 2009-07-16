package edu.iu.scipolicy.visualization.geomaps.legend;

import edu.iu.scipolicy.visualization.geomaps.printing.Circle;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

/* Create PostScript to draw two Circles representing the extrema of the
 * interpolated range and label each with the respective value in the raw
 * range (which is intended to be the respective value before scaling and
 * before interpolation).
 * 
 * This legend component has two captions:
 * - typeLabel would be like "Circle Size"
 * - keyLabel would be like "Number of papers published"
 */
public class AreaLegend implements LegendComponent {
	public static final int DEFAULT_LOWER_LEFT_X_IN_POINTS = 100;
	public static final int DEFAULT_LOWER_LEFT_Y_IN_POINTS = 500;
	public static final double CIRCLE_GRAY = 0.5;
	public static final double EXTREMA_LABEL_GRAY = 0.0;
	public static final double TYPE_LABEL_GRAY = 0.0;
	public static final double KEY_LABEL_GRAY = 0.5;
	public static final String FONT_NAME = "Garamond";
	public static final double FONT_SIZE = 20.0;

	private Range<Double> rawRange;
	private Range<Double> interpolatedRange;
	private String typeLabel;
	private String keyLabel;
	private double lowerLeftX;
	private double lowerLeftY;


	public AreaLegend(Range<Double> rawRange, Range<Double> interpolatedRange, String typeLabel, String keyLabel) {
		this(rawRange, interpolatedRange, typeLabel, keyLabel, DEFAULT_LOWER_LEFT_X_IN_POINTS, DEFAULT_LOWER_LEFT_Y_IN_POINTS);
	}

	public AreaLegend(Range<Double> areaRange, Range<Double> interpolatedAreaRange, String typeLabel, String keyLabel, double lowerLeftX, double lowerLeftY) {
		this.rawRange = areaRange;
		this.interpolatedRange = interpolatedAreaRange;
		this.typeLabel = typeLabel;
		this.keyLabel = keyLabel;
		this.lowerLeftX = lowerLeftX;
		this.lowerLeftY = lowerLeftY;
	}

	// TODO? Draw circles using the line width set in CirclePrinter.CIRCLE_LINE_WIDTH
	public String toPostScript() {
		String s = "";

		s += lowerLeftX + " ";
		s += lowerLeftY + " ";

		s += Circle.calculateRadiusFromArea(interpolatedRange.getMin()) + " ";
		s += Circle.calculateRadiusFromArea(interpolatedRange.getMax()) + " ";

		s += CIRCLE_GRAY + " ";

		s += "(" + Legend.prettyPrintDouble(rawRange.getMin()) + ")" + " ";
		s += "(" + Legend.prettyPrintDouble(rawRange.getMax()) + ")" + " ";
		s += EXTREMA_LABEL_GRAY + " ";

		s += "(" + typeLabel + ")" + " ";
		s += TYPE_LABEL_GRAY + " ";
		s += "(" + keyLabel + ")" + " ";
		s += KEY_LABEL_GRAY + " ";

		s += "{/" + FONT_NAME + " findfont}" + " ";
		s += FONT_SIZE + " ";

		s += "arealegend" + "\n";

		return s;
	}
}
