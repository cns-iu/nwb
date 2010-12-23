package edu.iu.sci2.visualization.geomaps.interpolation;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.iu.sci2.visualization.geomaps.utility.Averager;
import edu.iu.sci2.visualization.geomaps.utility.BinaryCondition;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.utility.RelativeDifferenceLimit;


public class ColorInterpolator implements Interpolator<Color> {
	public static final int RED = 0;
	public static final int GREEN = 1;
	public static final int BLUE = 2;
	/* Permissible pairwise relative difference of the RGB coordinates during
	 * an inversion.  1% is arbitrary.
	 */
	public static final double INVERT_TOLERANCE = 0.01;

	private LinearInterpolator redInterpolator;
	private LinearInterpolator greenInterpolator;
	private LinearInterpolator blueInterpolator;

	public ColorInterpolator(Range<Double> inputRange, Range<Color> interpolatedRange) {
		float[] minimumColorComponents = new float[3];
		interpolatedRange.getMin().getColorComponents(minimumColorComponents);
		
		float[] maximumColorComponents = new float[3];
		interpolatedRange.getMax().getColorComponents(maximumColorComponents);
		
		redInterpolator =
			new LinearInterpolator(
					inputRange,
					new Range<Double>(
							(double) minimumColorComponents[RED],
							(double) maximumColorComponents[RED]));
		greenInterpolator =
			new LinearInterpolator(
					inputRange,
					new Range<Double>(
							(double) minimumColorComponents[GREEN],
							(double) maximumColorComponents[GREEN]));
		blueInterpolator =
			new LinearInterpolator(
					inputRange,
					new Range<Double>(
							(double) minimumColorComponents[BLUE],
							(double) maximumColorComponents[BLUE]));
	}
	
	public List<Color> interpolate(List<Double> values) {
		List<Color> interpolatedValues = new ArrayList<Color>();
		
		for (Double value : values) {
			Color interpolatedValue = interpolate(value);
			interpolatedValues.add(interpolatedValue);
		}

		return interpolatedValues;
	}

	public Color interpolate(double colorQuantity) {
		float red = redInterpolator.interpolate(colorQuantity).floatValue();
		float green =  greenInterpolator.interpolate(colorQuantity).floatValue();
		float blue = blueInterpolator.interpolate(colorQuantity).floatValue();
		return new Color(red, green, blue);
	}
	
	public double invert(Color color) throws InterpolatorInversionException {
		List<Double> inversionSamples = calculateColorInversionSamples(color);
		
		BinaryCondition<Double> relativeDifferenceLimit =
			new RelativeDifferenceLimit(INVERT_TOLERANCE);
		
		if (relativeDifferenceLimit.isPairwiseSatisfiedBy(inversionSamples)) {
			/* To use the mean is really quite arbitrary.
			 * If the pairwise relative difference is small, any of the three
			 * samples should suffice.
			 * We choose to return the mean with a hand-waving claim
			 * of "numerical stability".
			 */
			return Averager.mean(inversionSamples.toArray(new Double[0]));
		} else {
			String message = 
				"Unexpected error: Inverting interpolation of the Color "
				+ color
				+ " generated intolerable differences between the pre-image "
				+ "red, green, and blue dimensions.  These values should be "
				+ "nearly equal, up to floating-point arithmetic error.  ";
			
			message += "Found these samples: ";
			for (double inversionSample : inversionSamples) {
				message += (inversionSample + " ");
			}
			message += ".";
			
			throw new InterpolatorInversionException(message);
		}
	}

	private List<Double> calculateColorInversionSamples(Color color)
			throws InterpolatorInversionException {
		float[] colorComponents = new float[3];
		color.getColorComponents(colorComponents);
		
		List<Double> inversionSamples = new ArrayList<Double>();
		List<InterpolatorInversionException> exceptions =
			new ArrayList<InterpolatorInversionException>();
		
		/* "Do we really need to check all three dimensions?"  Yes.
		 * Suppose you consider eliminating the inversion of dimension "foo".
		 * Then I add a new Color Range, where foo varies
		 * from the minimum color to the maximum color,
		 * but the other two dimensions are fixed.
		 * For example, foo is "green".
		 * Now I want to use a Range<Color> from (37, 10, 42) to (37, 251, 42).
		 * If you don't sample green, you're out of luck,
		 * because the red and blue inversions are impossible.
		 */
		
		double invertedRed =
			redInterpolator.invert((double) colorComponents[RED]);
		
		inversionSamples.add(invertedRed);
		
		double invertedGreen =
			greenInterpolator.invert((double) colorComponents[GREEN]);
		
		inversionSamples.add(invertedGreen);
		
		double invertedBlue =
			blueInterpolator.invert((double) colorComponents[BLUE]);
		
		inversionSamples.add(invertedBlue);
		
		/* If all three inversions failed, retrieving the pre-image is
		 * impossible (at this late stage).
		 * It amounts to a division by zero for the same reason that you cannot
		 * find a unique solution for x in 0*x = 0.  The information is gone.
		 */
		if (inversionSamples.isEmpty()) {
			String message =
				"Unexpected error: "
				+ "Inversion failed for all three color coordinates. "
				+ "This should only happen if " + this + " is trying "
				+ " to interpolate into a degenerate Color Range. ";
			
			message += ("Caught these exceptions in the attempt:" + "\n");
			for (Exception exception : exceptions) {
				message += ("  " + exception.getMessage() + "\n");
			}
			
			throw new InterpolatorInversionException(message);
		}
		
		return inversionSamples;
	}
}
