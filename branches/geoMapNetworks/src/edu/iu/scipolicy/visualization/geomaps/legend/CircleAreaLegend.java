package edu.iu.scipolicy.visualization.geomaps.legend;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.scipolicy.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.scipolicy.visualization.geomaps.printing.Circle;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

/* Create PostScript to draw two Circles representing the extrema of the
 * interpolated range and label each with the respective value in the raw
 * range (which is intended to be the respective value before scalingLabel and
 * before interpolation).
 * 
 * This legend component has two captions:
 * - typeLabel would be like "Circle Area"
 * - keyLabel would be like "Number of papers published"
 */
public class CircleAreaLegend implements LegendComponent {
	public static final double CIRCLE_BRIGHTNESS = 0.5;
	public static final double EXTREMA_LABEL_BRIGHTNESS = 0.0;
	public static final double EXTREMA_LABEL_FONT_SIZE = 8;
	public static final double TYPE_LABEL_BRIGHTNESS = 0.0;
	public static final double TYPE_LABEL_FONT_SIZE = 10;
	public static final double SCALING_LABEL_BRIGHTNESS = 0.5;
	public static final double KEY_LABEL_BRIGHTNESS = 0.5;
	public static final double KEY_LABEL_FONT_SIZE = 8;
	public static final String FONT_NAME = Constants.FONT_NAME;

	private Range<Double> rawRange;
	private String scalingLabel;
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
	
	private boolean hasPrintedDefinitions;


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
		this.scalingLabel = scaling;
		this.rawMidArea = rawMidArea;
		this.areaMidrange = areaMidrange;
		this.interpolatedRange = interpolatedAreaRange;
		this.typeLabel = typeLabel;
		this.keyLabel = keyLabel;
		this.lowerLeftX = lowerLeftX;
		this.lowerLeftY = lowerLeftY;
		
		this.hasPrintedDefinitions = false;
	}

	
	/* TODO? Draw circles using the line width set
	 * in CirclePrinter.CIRCLE_LINE_WIDTH?
	 */
	public String toPostScript() {
		String s = "";
		
		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapsAlgorithm.group.getInstanceOf(
						"circleAreaLegendDefinitions");
			
			s += definitionsTemplate.toString();
			
			this.hasPrintedDefinitions = true;
		}
		
		StringTemplate invocationTemplate =
			GeoMapsAlgorithm.group.getInstanceOf("circleAreaLegend");
		
		invocationTemplate.setAttribute("x", lowerLeftX);
		invocationTemplate.setAttribute("y", lowerLeftY);
		
		invocationTemplate.setAttribute(
				"minRadius",
				Circle.calculateRadiusFromArea(interpolatedRange.getMin()));
		invocationTemplate.setAttribute(
				"midRadius",
				Circle.calculateRadiusFromArea(areaMidrange));
		invocationTemplate.setAttribute(
				"maxRadius",
				Circle.calculateRadiusFromArea(interpolatedRange.getMax()));
		
		invocationTemplate.setAttribute("circleBrightness", CIRCLE_BRIGHTNESS);
		
		UnsignedZeroDecimalFormat doubleFormatter =
			UnsignedZeroDecimalFormat.createDecimalFormatOver(
					rawRange.getMin(), rawMidArea, rawRange.getMax());
		invocationTemplate.setAttribute(
				"minLabel", doubleFormatter.format(rawRange.getMin()));
		invocationTemplate.setAttribute(
				"midLabel", doubleFormatter.format(rawMidArea));
		invocationTemplate.setAttribute(
				"maxLabel", doubleFormatter.format(rawRange.getMax()));
		
		invocationTemplate.setAttribute(
				"extremaLabelBrightness", EXTREMA_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute(
				"extremaLabelFontSize", EXTREMA_LABEL_FONT_SIZE);
		
		invocationTemplate.setAttribute("typeLabel", typeLabel);
		invocationTemplate.setAttribute(
				"typeLabelBrightness", TYPE_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute(
				"typeLabelFontSize", TYPE_LABEL_FONT_SIZE);
		
		invocationTemplate.setAttribute(
				"scalingLabel", "(" + scalingLabel + ")");
		invocationTemplate.setAttribute(
				"scalingLabelBrightness", SCALING_LABEL_BRIGHTNESS);
		
		invocationTemplate.setAttribute("keyLabel", keyLabel);
		invocationTemplate.setAttribute(
				"keyLabelBrightness", KEY_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute(
				"keyLabelFontSize", KEY_LABEL_FONT_SIZE);
		
		invocationTemplate.setAttribute("fontName", FONT_NAME);

		s += invocationTemplate.toString();

		return s;
	}
	
	public Range<Double> getRawRange() {
		return rawRange;
	}
}
