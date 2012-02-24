package edu.iu.sci2.visualization.geomaps.viz;

import java.util.Dictionary;
import java.util.List;

import org.cishell.utilities.NumberUtilities;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;

import prefuse.data.Tuple;

import com.google.common.base.Objects;

import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.coding.Coding;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public interface VizDimension {
	String getColumnNameParameterId();
	String getColumnNameParameterDisablingToken();
	String getScalingParameterId();
	String getRangeParameterId();
	
	abstract Strategy defaultStrategy();
	abstract Binding<? extends VizDimension> bindingFor(Dictionary<String, Object> parameters);
	
	void addOptionsToAlgorithmParameters(DropdownMutator mutator, List<String> numericColumnNames);
	
	
	public static abstract class Binding<D extends VizDimension> {		
		private final D dimension;
		private final String columnName;
		private final Scaling scaling;
		
		public Binding(D dimension, Dictionary<String, Object> parameters) {
			this.dimension = dimension;
			this.columnName = (String) parameters.get(dimension.getColumnNameParameterId());
			this.scaling = Scaling.valueOf((String) parameters.get(dimension.getScalingParameterId()));
		}
		
		
		public abstract Coding<D> codingForDataRange(Range<Double> usableRange, Range<Double> scaledRange);
		
		public D dimension() {
			return dimension;
		} 
		
		public String columnName() {
			return columnName;
		}
		
		public Scaling scaling() {
			return scaling;
		}
		
		public boolean isEnabled() {
			return (!Objects.equal(columnName, dimension.getColumnNameParameterDisablingToken()));
		}
		
		public double readValueFromTuple(Tuple tuple) { // TODO maybe this is a bad fit
			return NumberUtilities.interpretObjectAsDouble(tuple.get(columnName()));
		}		
	}
}