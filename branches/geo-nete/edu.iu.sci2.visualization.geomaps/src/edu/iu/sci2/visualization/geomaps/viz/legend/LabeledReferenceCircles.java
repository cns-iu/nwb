package edu.iu.sci2.visualization.geomaps.viz.legend;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.viz.Circle;
import edu.iu.sci2.visualization.geomaps.viz.Constants;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.NumberFormatFactory;
import edu.iu.sci2.visualization.geomaps.viz.legend.numberformat.UnsignedZeroFormat;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;

/* Create PostScript to draw three Circles representing the extrema (minimum,
 * midpoint, and maximum) of the interpolated range and label each with the
 * respective value in the raw range (which is intended to be the respective
 * value before scalingLabel and before interpolation).
 * 
 * This legend component has two captions:
 * - legendDescription would be like "Circle Area"
 * - columnName would be like "Number of papers published"
 */
public class LabeledReferenceCircles implements PostScriptable {
	// Brightness for PostScript's setgray command. 0 is black, 1 is white.
	public static final double CIRCLE_BRIGHTNESS = 0.5;
	
	public static final double EXTREMA_LABEL_BRIGHTNESS = 0.0;
	public static final double EXTREMA_LABEL_FONT_SIZE = 8;
	public static final double TYPE_LABEL_BRIGHTNESS = 0.0;
	public static final double TYPE_LABEL_FONT_SIZE = 10;
	public static final double SCALING_LABEL_BRIGHTNESS = 0.25;
	public static final double KEY_LABEL_BRIGHTNESS = 0.5;
	public static final double KEY_LABEL_FONT_SIZE = 8;
	public static final String FONT_NAME = Constants.FONT_NAME;

	private AreaLegend areaLegend;
	/* These "lower lefts" are the lower left corners of the key text,
	 * e.g., "Population" or "Number of Works Authored" or what have you.
	 * This is an ex post facto change so that the legend components align
	 * vertically by their type and key text labels rather than by their
	 * extrema labels (as it was before).
	 */
	private final double lowerLeftX;
	private final double lowerLeftY;
	
	private boolean hasPrintedDefinitions;


	public LabeledReferenceCircles(
			AreaLegend areaLegend, double lowerLeftX,  double lowerLeftY) {
		this.areaLegend = areaLegend;
		this.lowerLeftX = lowerLeftX;
		this.lowerLeftY = lowerLeftY;
		
		this.hasPrintedDefinitions = false;
	}

	
	/* TODO? Draw circles using the line width set
	 * in CirclePrinter.CIRCLE_LINE_WIDTH?
	 */
	@Override
	public String toPostScript() {
		String s = "";
		
		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapsAlgorithm.TEMPLATE_GROUP.getInstanceOf(
						"circleAreaLegendDefinitions");
			
			s += definitionsTemplate.toString();
			
			this.hasPrintedDefinitions = true;
		}
		
		StringTemplate invocationTemplate =
			GeoMapsAlgorithm.TEMPLATE_GROUP.getInstanceOf("circleAreaLegend");
		
		invocationTemplate.setAttribute("x", lowerLeftX);
		invocationTemplate.setAttribute("y", lowerLeftY);
		
		invocationTemplate.setAttribute(
				"minRadius",
				Circle.calculateRadiusFromArea(areaLegend.getVizRange().getPointA()));
		invocationTemplate.setAttribute(
				"midRadius",
				Circle.calculateRadiusFromArea(areaLegend.getMidpointArea()));
		invocationTemplate.setAttribute(
				"maxRadius",
				Circle.calculateRadiusFromArea(areaLegend.getVizRange().getPointB()));
		
		invocationTemplate.setAttribute("circleBrightness", CIRCLE_BRIGHTNESS);
		
		UnsignedZeroFormat doubleFormatter =
			NumberFormatFactory.getNumberFormat(
					areaLegend.getNumericFormatType(),
					areaLegend.getDataRange().getPointA(), areaLegend.dataValueForOutputMidpoint(), areaLegend.getDataRange().getPointB());
		invocationTemplate.setAttribute(
				"minLabel", doubleFormatter.format(areaLegend.getDataRange().getPointA()));
		invocationTemplate.setAttribute(
				"midLabel", doubleFormatter.format(areaLegend.dataValueForOutputMidpoint()));
		invocationTemplate.setAttribute(
				"maxLabel", doubleFormatter.format(areaLegend.getDataRange().getPointB()));
		
		invocationTemplate.setAttribute(
				"extremaLabelBrightness", EXTREMA_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute(
				"extremaLabelFontSize", EXTREMA_LABEL_FONT_SIZE);
		
		invocationTemplate.setAttribute("typeLabel", areaLegend.getLegendDescription());
		invocationTemplate.setAttribute(
				"typeLabelBrightness", TYPE_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute(
				"typeLabelFontSize", TYPE_LABEL_FONT_SIZE);
		
		invocationTemplate.setAttribute(
				"scalingLabel", "(" + areaLegend.getScalingLabel() + ")");
		invocationTemplate.setAttribute(
				"scalingLabelBrightness", SCALING_LABEL_BRIGHTNESS);
		
		invocationTemplate.setAttribute("keyLabel", areaLegend.getColumnName());
		invocationTemplate.setAttribute(
				"keyLabelBrightness", KEY_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute(
				"keyLabelFontSize", KEY_LABEL_FONT_SIZE);
		
		invocationTemplate.setAttribute("fontName", FONT_NAME);

		s += invocationTemplate.toString();

		return s;
	}
}
