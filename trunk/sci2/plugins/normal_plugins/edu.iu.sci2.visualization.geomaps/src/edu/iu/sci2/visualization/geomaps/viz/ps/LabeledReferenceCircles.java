package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.geom.Point2D;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.UnsignedZeroFormat;
import edu.iu.sci2.visualization.geomaps.viz.Circle;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.legend.AreaLegend;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;

/* Create PostScript to draw three Circles representing the extrema (minimum,
 * midpoint, and maximum) of the interpolated range and label each with the
 * respective value in the raw range (which is intended to be the respective
 * value before scalingLabel and before interpolation).
 * 
 * This legend component has two captions:
 * - legendDescription would be like "Circle Area"
 * - columnName would be like "Number of papers published"
 */
public class LabeledReferenceCircles implements LabeledReference {
	// Brightness for PostScript's setgray command. 0 is black, 1 is white.
	public static final double CIRCLE_BRIGHTNESS = 0.0;
	
	public static final double EXTREMA_LABEL_BRIGHTNESS = 0.0;
	public static final double TYPE_LABEL_BRIGHTNESS = 0.0;
	public static final double SCALING_LABEL_BRIGHTNESS = 0.25;
	
	public static final double KEY_LABEL_BRIGHTNESS = 0.5;
	private final AreaLegend areaLegend;
	private final Point2D.Double keyTextLowerLeft;
	private final PageLayout pageLayout;
	
	private boolean hasPrintedDefinitions;

	public LabeledReferenceCircles(AreaLegend areaLegend, Point2D.Double keyTextLowerLeft, PageLayout pageLayout) {
		this.areaLegend = areaLegend;
		this.keyTextLowerLeft = keyTextLowerLeft;
		this.pageLayout = pageLayout;
		
		this.hasPrintedDefinitions = false;
	}

	
	@Override
	public String toPostScript() {
		double extremaLabelFontSize = 0.8 * pageLayout.contentFont().getSize();
		double typeLabelFontSize = pageLayout.contentFont().getSize();
		double keyLabelFontSize = 0.8 * pageLayout.contentFont().getSize();
		
		String s = "";
		
		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapViewPS.TEMPLATE_GROUP.getInstanceOf(
						"circleAreaLegendDefinitions");
			
			s += definitionsTemplate.toString();
			
			this.hasPrintedDefinitions = true;
		}
		
		StringTemplate invocationTemplate =
			GeoMapViewPS.TEMPLATE_GROUP.getInstanceOf("circleAreaLegend");
		
		invocationTemplate.setAttribute("x", keyTextLowerLeft.x);
		invocationTemplate.setAttribute("y", keyTextLowerLeft.y);
		
		invocationTemplate.setAttribute(
				"minRadius",
				Circle.calculateRadiusFromArea(areaLegend.getVizContinuum().getPointA()));
		invocationTemplate.setAttribute(
				"midRadius",
				Circle.calculateRadiusFromArea(areaLegend.getMidpointArea()));
		invocationTemplate.setAttribute(
				"maxRadius",
				Circle.calculateRadiusFromArea(areaLegend.getVizContinuum().getPointB()));
		
		invocationTemplate.setAttribute("circleBrightness", CIRCLE_BRIGHTNESS);
		
		UnsignedZeroFormat doubleFormatter =
			NumberFormatFactory.getNumberFormat(
					areaLegend.numericFormatType(),
					areaLegend.getDataRange().lowerEndpoint(),
					areaLegend.getDataValueForOutputMidpoint(),
					areaLegend.getDataRange().upperEndpoint());
		invocationTemplate.setAttribute(
				"minLabel", doubleFormatter.format(areaLegend.getDataRange().lowerEndpoint()));
		invocationTemplate.setAttribute(
				"midLabel", doubleFormatter.format(areaLegend.getDataValueForOutputMidpoint()));
		invocationTemplate.setAttribute(
				"maxLabel", doubleFormatter.format(areaLegend.getDataRange().upperEndpoint()));

		invocationTemplate.setAttribute(
				"extremaLabelBrightness", EXTREMA_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute(
				"extremaLabelFontSize", extremaLabelFontSize);
		
		invocationTemplate.setAttribute("typeLabel", areaLegend.legendDescription());
		invocationTemplate.setAttribute(
				"typeLabelBrightness", TYPE_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute(
				"typeLabelFontSize", typeLabelFontSize);
		
		invocationTemplate.setAttribute(
				"scalingLabel", "(" + areaLegend.scalingLabel() + ")");
		invocationTemplate.setAttribute(
				"scalingLabelBrightness", SCALING_LABEL_BRIGHTNESS);
		
		invocationTemplate.setAttribute("keyLabel", PSUtility.escapeForPostScript(areaLegend.columnName()));
		invocationTemplate.setAttribute(
				"keyLabelBrightness", KEY_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute(
				"keyLabelFontSize", keyLabelFontSize);
		
		invocationTemplate.setAttribute("fontName", PSUtility.psFontName(pageLayout.contentFont()));

		s += invocationTemplate.toString();

		return s;
	}
}
