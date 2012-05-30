package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.sci2.visualization.geomaps.utility.ColorTuples;
import edu.iu.sci2.visualization.geomaps.utility.Dimension;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.NumberFormatFactory;
import edu.iu.sci2.visualization.geomaps.utility.numberformat.UnsignedZeroFormat;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.legend.ColorLegend;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;

/* Create PostScript to draw a color gradient representing the extrema (minimum,
 * midpoint, and maximum) of the interpolated range and label each end with the
 * respective value in the raw range (which is intended to be the respective
 * value before scalingLabel and before interpolation).
 * 
 * This legend component has two captions:
 * - legendDescription would be like "Blah RGB"
 * - dataColumnName would be like "Number of papers published"
 */
public class LabeledReferenceGradient implements LabeledReference {
	/* Width of each cell of the gradient in points.
	 * A lesser positive integer means more fine, or less blocky.
	 */
	public static final int GRADIENT_RESOLUTION = 1;
	/* Brightnesses correspond to PostScript's setgray command.
	 * 0 is black, 1 is white.
	 */
	public static final double EXTREMA_LABEL_BRIGHTNESS = 0.0;
	public static final double TYPE_LABEL_BRIGHTNESS = 0.0;
	public static final double SCALING_LABEL_BRIGHTNESS = 0.25;
	public static final double KEY_LABEL_BRIGHTNESS = 0.5;
	
	private final ColorLegend legend;
	/* These "lower lefts" are the lower left corners of the key text,
	 * e.g., "Population" or "Number of Works Authored" or what have you.
	 * This is an ex post facto change so that the legend components align
	 * vertically by their type and key text labels rather than by their
	 * extrema labels (as it was before).
	 */
	private final Point2D.Double lowerLeft;
	private final PageLayout pageLayout;
	private final Dimension<Double> dimension;
	
	private boolean hasPrintedDefinitions;

	public LabeledReferenceGradient(ColorLegend legend, Point2D.Double lowerLeft,
			PageLayout pageLayout, Dimension<Double> dimension) {
		this.legend = legend;
		this.lowerLeft = lowerLeft;
		this.pageLayout = pageLayout;
		this.dimension = dimension;
		
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
				GeoMapViewPS.TEMPLATE_GROUP.getInstanceOf("colorLegendDefinitions");
			
			s += definitionsTemplate.toString();
			
			this.hasPrintedDefinitions = true;
		}
		
		
		StringTemplate invocationTemplate =
			GeoMapViewPS.TEMPLATE_GROUP.getInstanceOf("colorLegend");
		
		invocationTemplate.setAttribute("x", lowerLeft.x);
		invocationTemplate.setAttribute("y", lowerLeft.y);
		
		invocationTemplate.setAttribute("gradientWidth", dimension.getWidth());
		invocationTemplate.setAttribute("gradientHeight", dimension.getHeight());
		
		invocationTemplate.setAttribute("gradientResolution", GRADIENT_RESOLUTION);
		
		Color minColor = legend.getVizContinuum().getPointA();
		double[] minColorTuple = ColorTuples.asTuple(minColor);		
		invocationTemplate.setAttribute("minColorRed", minColorTuple[0]);
		invocationTemplate.setAttribute("minColorGreen", minColorTuple[1]);
		invocationTemplate.setAttribute("minColorBlue", minColorTuple[2]);
		
		Color maxColor = legend.getVizContinuum().getPointB();
		double[] maxColorTuple = ColorTuples.asTuple(maxColor);	
		invocationTemplate.setAttribute("maxColorRed", maxColorTuple[0]);
		invocationTemplate.setAttribute("maxColorGreen", maxColorTuple[1]);
		invocationTemplate.setAttribute("maxColorBlue", maxColorTuple[2]);
		
		UnsignedZeroFormat doubleFormatter =
			NumberFormatFactory.getNumberFormat(
					legend.getNumericFormatType(),
					legend.getDataRange().lowerEndpoint(),
					legend.getDataValueForOutRangeMidpoint(),
					legend.getDataRange().upperEndpoint());
		invocationTemplate.setAttribute("minLabel", doubleFormatter.format(legend.getDataRange().lowerEndpoint()));
		invocationTemplate.setAttribute("midLabel", doubleFormatter.format(legend.getDataValueForOutRangeMidpoint()));
		invocationTemplate.setAttribute("maxLabel", doubleFormatter.format(legend.getDataRange().upperEndpoint()));
		
		invocationTemplate.setAttribute("extremaLabelBrightness", EXTREMA_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute("extremaLabelFontSize", extremaLabelFontSize);
		
		invocationTemplate.setAttribute("typeLabel", legend.getLegendDescription());
		invocationTemplate.setAttribute("typeLabelBrightness", TYPE_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute("typeLabelFontSize", typeLabelFontSize);
		
		invocationTemplate.setAttribute("scalingLabel", "(" + legend.getScalingLabel() + ")");
		invocationTemplate.setAttribute("scalingLabelBrightness", SCALING_LABEL_BRIGHTNESS);
		
		invocationTemplate.setAttribute("keyLabel", PSUtility.escapeForPostScript(legend.getColumnName()));
		invocationTemplate.setAttribute("keyLabelBrightness", KEY_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute("keyLabelFontSize", keyLabelFontSize);
		
		invocationTemplate.setAttribute("fontName", PSUtility.psFontName(pageLayout.contentFont()));
		
		s += invocationTemplate.toString();

		return s;
	}
}
