package edu.iu.scipolicy.visualization.geomaps.utility;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import prefuse.data.Tuple;

/*
 * In trying to get a value from a Prefuse Table (or Tuple within the table)
 * one may get any of several types.
 * For example, I'm led to believe that an empty array of the column's type
 * is returned when a row has a null value there.
 * 
 * This class attempts to box up the ugliness by providing methods to safely
 * read a double value.
 * 
 * Always check that specified() is true before trying to get().
 * 
 * TODO Could this class be replaced by org.cishell.utilities.NumberUtilities?
 */
public class PrefuseDoubleReader {
	private enum TupleDoubleSpecification { NONE, NUMBER, DOUBLE_ARRAY, FLOAT_ARRAY, INTEGER_ARRAY }

	public static double get(Tuple row, String attributeKey) throws AlgorithmExecutionException {
		if ( isSpecified(row, attributeKey) ) {
			TupleDoubleSpecification spec = findSpecification(row, attributeKey);
			Object value = row.get(attributeKey);
			
			if ( spec.equals(TupleDoubleSpecification.NUMBER)) {
				return ((Number) value).doubleValue();
			}
			else if ( spec.equals(TupleDoubleSpecification.DOUBLE_ARRAY) ) {
				double[] values = (double[]) value;				
				return values[0];
			}
			else if ( spec.equals(TupleDoubleSpecification.FLOAT_ARRAY) ) {
				float[] values = (float[]) value;				
				return (double) values[0];
			}
			else if ( spec.equals(TupleDoubleSpecification.INTEGER_ARRAY) ) {
				int[] values = (int[]) value;				
				return (double) values[0];
			}
			else {
				throw new AlgorithmExecutionException("Unrecognized TupleDoubleSpecification for row " + row + " and attribute " + attributeKey);
			}
		}
		else {
			throw new AlgorithmExecutionException("Check specified() before calling get().");
		}
	}

	public static boolean isSpecified(Tuple row, String attributeKey) {
		return ( ! ( findSpecification(row, attributeKey).equals(TupleDoubleSpecification.NONE) ) );
	}
	
	private static TupleDoubleSpecification findSpecification(Tuple row, String attributeKey) {
		Object value = row.get(attributeKey);		
		
		if ( value instanceof Number ) {
			return TupleDoubleSpecification.NUMBER;
		}
		else if ( value instanceof double[]) {
			if ( ((double[]) value).length > 0 ) {
				return TupleDoubleSpecification.DOUBLE_ARRAY;
			}
			else {
				return TupleDoubleSpecification.NONE;
			}
		}
		else if ( value instanceof float[] ) {
			if ( ((float[]) value).length > 0 ) {
				return TupleDoubleSpecification.FLOAT_ARRAY;
			}
			else {
				return TupleDoubleSpecification.NONE;
			}
		}
		else if ( value instanceof int[] ) {
			if ( ((int[]) value).length > 0 ) {
				return TupleDoubleSpecification.INTEGER_ARRAY;
			}
			else {
				return TupleDoubleSpecification.NONE;
			}
		}
		else {
			return TupleDoubleSpecification.NONE;
		}
	}
}
