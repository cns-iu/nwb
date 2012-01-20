package edu.iu.sci2.visualization.geomaps.legend;

import java.awt.Color;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.numberformat.NumberFormatFactory;
import edu.iu.sci2.visualization.geomaps.numberformat.UnsignedZeroDecimalFormat;
import edu.iu.sci2.visualization.geomaps.utility.Constants;
import edu.iu.sci2.visualization.geomaps.utility.Range;

/* Create PostScript to draw a color gradient representing the extrema (minimum,
 * midpoint, and maximum) of the interpolated range and label each end with the
 * respective value in the raw range (which is intended to be the respective
 * value before scalingLabel and before interpolation).
 * 
 * This legend component has two captions:
 * - typeLabel would be like "Blah Color"
 * - keyLabel would be like "Number of papers published"
 */
public class ColorLegend implements LegendComponent {
	/* Width of each cell of the gradient in points.
	 * A lesser positive integer means more fine, or less blocky.
	 */
	public static final int GRADIENT_RESOLUTION = 1;
	/* Brightnesses correspond to PostScript's setgray command.
	 * 0 is black, 1 is white.
	 */
	public static final double EXTREMA_LABEL_BRIGHTNESS = 0.0;
	public static final double EXTREMA_LABEL_FONT_SIZE = 8;
	public static final double TYPE_LABEL_BRIGHTNESS = 0.0;
	public static final double TYPE_LABEL_FONT_SIZE = 10;
	public static final double SCALING_LABEL_BRIGHTNESS = 0.25;
	public static final double KEY_LABEL_BRIGHTNESS = 0.5;
	public static final double KEY_LABEL_FONT_SIZE = 8;
	public static final String FONT_NAME = Constants.FONT_NAME;
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;	

	private Range<Double> rawRange;
	private String scalingLabel;
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
	private boolean hasPrintedDefinitions;
	private String numberFormatName;
	

	public ColorLegend(Range<Double> rawRange,
						   String scaling,
						   double rawMidColorQuantity,
						   Range<Color> interpolatedRange,
						   String typeLabel,
						   String keyLabel,
						   double lowerLeftX,
						   double lowerLeftY,
						   double width,
						   double height,
						   String numberFormatName) {
		this.rawRange = rawRange;
		this.scalingLabel = scaling;
		this.rawMidColorQuantity = rawMidColorQuantity;
		this.interpolatedRange = interpolatedRange;
		this.typeLabel = typeLabel;
		this.keyLabel = keyLabel;
		this.lowerLeftX = lowerLeftX;
		this.lowerLeftY = lowerLeftY;
		this.gradientWidth = width;
		this.gradientHeight = height;
		this.numberFormatName = numberFormatName;
		
		this.hasPrintedDefinitions = false;
	}

	@Override
	public String toPostScript() {
		String s = "";
		
		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapsAlgorithm.group.getInstanceOf("colorLegendDefinitions");
			
			s += definitionsTemplate.toString();
			
			this.hasPrintedDefinitions = true;
		}
		
		
		StringTemplate invocationTemplate =
			GeoMapsAlgorithm.group.getInstanceOf("colorLegend");
		
		invocationTemplate.setAttribute("x", lowerLeftX);
		invocationTemplate.setAttribute("y", lowerLeftY);
		
		invocationTemplate.setAttribute("gradientWidth", gradientWidth);
		invocationTemplate.setAttribute("gradientHeight", gradientHeight);
		
		invocationTemplate.setAttribute("gradientResolution", GRADIENT_RESOLUTION);		
		
		float[] minColorComponents = new float[3];
		interpolatedRange.getMin().getColorComponents(minColorComponents);
		invocationTemplate.setAttribute("minColorRed", minColorComponents[RED]);
		invocationTemplate.setAttribute("minColorGreen", minColorComponents[GREEN]);
		invocationTemplate.setAttribute("minColorBlue", minColorComponents[BLUE]);
		
		float[] maxColorComponents = new float[3];
		interpolatedRange.getMax().getColorComponents(maxColorComponents);
		invocationTemplate.setAttribute("maxColorRed", maxColorComponents[RED]);
		invocationTemplate.setAttribute("maxColorGreen", maxColorComponents[GREEN]);
		invocationTemplate.setAttribute("maxColorBlue", maxColorComponents[BLUE]);
		
		UnsignedZeroDecimalFormat doubleFormatter =
			NumberFormatFactory.getNumberFormat(
					numberFormatName, 
					rawRange.getMin(), rawMidColorQuantity, rawRange.getMax());
		invocationTemplate.setAttribute("minLabel", doubleFormatter.format(rawRange.getMin()));
		invocationTemplate.setAttribute("midLabel", doubleFormatter.format(rawMidColorQuantity));
		invocationTemplate.setAttribute("maxLabel", doubleFormatter.format(rawRange.getMax()));
		
		invocationTemplate.setAttribute("extremaLabelBrightness", EXTREMA_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute("extremaLabelFontSize", EXTREMA_LABEL_FONT_SIZE);
		
		invocationTemplate.setAttribute("typeLabel", typeLabel);
		invocationTemplate.setAttribute("typeLabelBrightness", TYPE_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute("typeLabelFontSize", TYPE_LABEL_FONT_SIZE);
		
		invocationTemplate.setAttribute("scalingLabel", "(" + scalingLabel + ")");
		invocationTemplate.setAttribute("scalingLabelBrightness", SCALING_LABEL_BRIGHTNESS);
		
		invocationTemplate.setAttribute("keyLabel", keyLabel);
		invocationTemplate.setAttribute("keyLabelBrightness", KEY_LABEL_BRIGHTNESS);
		invocationTemplate.setAttribute("keyLabelFontSize", KEY_LABEL_FONT_SIZE);
		
		invocationTemplate.setAttribute("fontName", FONT_NAME);
		
		s += invocationTemplate.toString();

		return s;
	}
}
