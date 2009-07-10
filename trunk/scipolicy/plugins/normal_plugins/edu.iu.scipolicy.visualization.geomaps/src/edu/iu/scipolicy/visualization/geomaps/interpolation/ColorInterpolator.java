package edu.iu.scipolicy.visualization.geomaps.interpolation;
import java.awt.Color;
import java.util.Collection;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.scipolicy.visualization.geomaps.utility.Range;


public class ColorInterpolator implements DoubleInterpolator<Color> {
	public static final Range<Color> DEFAULT_INTERPOLATED_COLOR_RANGE = new Range<Color>(Color.BLUE, Color.GREEN);
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;

	private LinearInterpolator redInterpolator;
	private LinearInterpolator greenInterpolator;
	private LinearInterpolator blueInterpolator;

	public ColorInterpolator(Collection<Double> colorQuantities) {
		this(colorQuantities, DEFAULT_INTERPOLATED_COLOR_RANGE);
	}

	public ColorInterpolator(Collection<Double> colorQuantities, Range<Color> interpolatedRange) {
		float[] minimumColorComponents = new float[3];
		interpolatedRange.getMin().getColorComponents(minimumColorComponents);

		float[] maximumColorComponents = new float[3];
		interpolatedRange.getMax().getColorComponents(maximumColorComponents);

		redInterpolator = new LinearInterpolator(colorQuantities, new Range<Double>((double) minimumColorComponents[RED], (double) maximumColorComponents[RED]));
		greenInterpolator = new LinearInterpolator(colorQuantities, new Range<Double>((double) minimumColorComponents[GREEN], (double) maximumColorComponents[GREEN]));
		blueInterpolator = new LinearInterpolator(colorQuantities, new Range<Double>((double) minimumColorComponents[BLUE], (double) maximumColorComponents[BLUE]));
	}

	public Color interpolate(double colorQuantity) throws AlgorithmExecutionException {
		float red = redInterpolator.interpolate(colorQuantity).floatValue();
		float green =  greenInterpolator.interpolate(colorQuantity).floatValue();
		float blue = blueInterpolator.interpolate(colorQuantity).floatValue();
		return new Color(red, green, blue);
	}
}
