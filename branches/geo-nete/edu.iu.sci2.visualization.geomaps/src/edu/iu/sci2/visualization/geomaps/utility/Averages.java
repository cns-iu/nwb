package edu.iu.sci2.visualization.geomaps.utility;

import java.util.Arrays;
import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.ObjectArrays;
import com.google.common.primitives.Doubles;


public class Averages {
	public static double meanOfDoubles(Collection<Double> values) {
		Preconditions.checkArgument(!values.isEmpty(), "Cannot take the mean of zero numbers.");
		
		double sum = 0.0;		
		for (double value : values) {
			sum += value;
		}
		
		return (sum / (values.size()));
	}
	
	public static double meanOfDoubles(double first, double... rest) {
		return meanOfDoubles(Doubles.asList(Doubles.concat(new double[]{ first }, rest)));
	}
	
	public static double[] meanOfTuples(Collection<double[]> tuples) {
		/* Establish dimensionality. */
		Preconditions.checkArgument(!tuples.isEmpty(), "Cannot take the mean of zero tuples.");
		// Assume the dimensionality is the length of the first tuple.
		// The precondition should keep us from ever using the defaultValue given to getFirst.
		double[] first = Iterables.getFirst(tuples, null);
		final int dimensionality = first.length;
		
		/* Check that all tuples match the established dimensionality. */
		for (double[] tuple : tuples) {
			Preconditions.checkArgument(
					tuple.length == dimensionality,
					String.format("Expected dimensionality %d but found %d.",
							dimensionality,
							tuple.length));
		}
		
		/* Take the mean over each dimension. */
		double[] means = new double[dimensionality];
		for (int dd = 0; dd < dimensionality; dd++) {
			final int dimension = dd;

			means[dd] = meanOfDoubles(Collections2.transform(tuples,
					new Function<double[], Double>() {
						public Double apply(double[] tuple) {
							// The preconditions should guarantee this.
							assert (0 <= dimension && dimension < tuple.length);
							
							return tuple[dimension];
						}			
					}));
		}
		
		return means;
	}
	
	public static double[] meanOfTuples(double[] first, double[]... rest) {
		return meanOfTuples(Arrays.asList(ObjectArrays.concat(first, rest)));
	}
}
