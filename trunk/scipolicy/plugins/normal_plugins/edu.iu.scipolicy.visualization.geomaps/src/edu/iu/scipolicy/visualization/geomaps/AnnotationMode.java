package edu.iu.scipolicy.visualization.geomaps;

import java.util.Dictionary;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.NumberUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.TableIterator;
import edu.iu.scipolicy.visualization.geomaps.scaling.Scaler;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;

public abstract class AnnotationMode {
	public abstract void applyAnnotations(
			ShapefileToPostScriptWriter postScriptWriter,
			Table inTable,
			Dictionary<String, Object> parameters)
				throws AlgorithmExecutionException;

	public static Range<Double> calculateScalableRangeOverColumn(
			Table inTable, String columnName, Scaler scaler)
				throws AlgorithmExecutionException {
		Range<Double> range =
			new Range<Double>(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
		
		int unreadableValueCount = 0;
		boolean someRowWasReadable = false;
		boolean someValueWasScalable = false;
		
		for (TableIterator tableIt = inTable.iterator(); tableIt.hasNext();) {
			Tuple row = inTable.getTuple(tableIt.nextInt());
			
			try {
				double value =
					NumberUtilities.interpretObjectAsDouble(row.get(columnName));
				
				someRowWasReadable = true;
				
				if (scaler.canScale(value)) {
					someValueWasScalable = true;
					
					if (value < range.getMin()) {
						range.setMin(value);
					}
	
					if (value > range.getMax()) {
						range.setMax(value);
					}
				}
				
			} catch (NumberFormatException e) {
				unreadableValueCount++;
			}
	
		}
		
		if (!someRowWasReadable) {
			throw new AlgorithmExecutionException(
				"Couldn't interpret any of the table's values in the "
					+ columnName
					+ " column as numbers.");
		}
		
		if (!someValueWasScalable) {
			throw new AlgorithmExecutionException(
					"Couldn't scale any of the table's values in the "
					+ columnName
					+ " column.");
		}
		
		if (unreadableValueCount > 0) {
			GeoMapsAlgorithm.logger.log(
				LogService.LOG_WARNING,
				"Failed to read the values of " + unreadableValueCount + " rows.");
		}
		
		return range;
	}
}
