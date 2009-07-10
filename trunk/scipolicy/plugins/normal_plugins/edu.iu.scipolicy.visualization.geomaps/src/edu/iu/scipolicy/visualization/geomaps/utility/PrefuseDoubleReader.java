package edu.iu.scipolicy.visualization.geomaps.utility;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import prefuse.data.Tuple;

/*
 * In trying to get a value from a Prefuse Table (or Tuple within the table)
 * one may get any of several types.  This class attempts to box up the ugliness
 * by providing methods to safely read a double value.
 * 
 * Always check that specified() is true before trying to get().
 */
public class PrefuseDoubleReader {
	private enum TupleDoubleSpecification { NONE, DOUBLE, FLOAT, INTEGER, DOUBLE_ARRAY, FLOAT_ARRAY, INTEGER_ARRAY }

	public static double get(Tuple row, String attributeKey) throws AlgorithmExecutionException {
		TupleDoubleSpecification spec = findSpecification(row, attributeKey);
		
		if ( specified(row, attributeKey) ) {
			System.out.println(row.get(attributeKey).getClass() + ": " + row.get(attributeKey));
			
			
			if ( spec.equals(TupleDoubleSpecification.DOUBLE) ) {
				System.out.println("  Trying to get double on row " + row + " at attribute " + attributeKey);
//				if ( row.canGetDouble(attributeKey) ) {
					return row.getDouble(attributeKey);
//				}
//				else {
//					throw new AlgorithmExecutionException("Couldn't get double");
//				}
			}
			if ( spec.equals(TupleDoubleSpecification.FLOAT) ) {
				return (double) row.getFloat(attributeKey);
			}
			else if ( spec.equals(TupleDoubleSpecification.INTEGER) ) {
				return (double) row.getInt(attributeKey);
			}
			else if ( spec.equals(TupleDoubleSpecification.DOUBLE_ARRAY) ) {
				double[] values = (double[]) row.get(attributeKey);
				
				return values[0];
			}
			else if ( spec.equals(TupleDoubleSpecification.FLOAT_ARRAY) ) {
				float[] values = (float[]) row.get(attributeKey);
				
				return (double) values[0];
			}
			else if ( spec.equals(TupleDoubleSpecification.INTEGER_ARRAY) ) {
				int[] values = (int[]) row.get(attributeKey);
				
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

	public static boolean specified(Tuple row, String attributeKey) {
		return ( ! ( findSpecification(row, attributeKey).equals(TupleDoubleSpecification.NONE) ) );
	}
	
	private static TupleDoubleSpecification findSpecification(Tuple row, String attributeKey) {
//		System.out.println("row = " + row + ", attributeKey = " + attributeKey);
		Object value = row.get(attributeKey);
//		System.out.println("  value = " + value);
		
		if ( value instanceof Double && row.canGetDouble(attributeKey) ) {
			return TupleDoubleSpecification.DOUBLE;
		}
		if ( value instanceof Float ) {
			return TupleDoubleSpecification.FLOAT;
		}
		else if ( value instanceof Integer ) {
			return TupleDoubleSpecification.INTEGER;
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
