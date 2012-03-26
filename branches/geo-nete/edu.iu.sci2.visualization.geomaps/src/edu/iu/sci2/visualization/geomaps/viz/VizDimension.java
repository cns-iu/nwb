package edu.iu.sci2.visualization.geomaps.viz;

import java.util.Dictionary;
import java.util.List;

import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;

import com.google.common.base.Objects;
import com.google.common.collect.Range;

import edu.iu.sci2.visualization.geomaps.data.GeoDatum;
import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.data.scaling.ScalingException;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.utility.Continuum;
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
	
	
	public static abstract class Binding<D extends Enum<D> & VizDimension> {		
		private final D dimension;
		private final String columnName;
		private final Scaling scaling;
		
		public Binding(D dimension, Dictionary<String, Object> parameters) {
			this.dimension = dimension;
			this.columnName = (String) parameters.get(dimension.getColumnNameParameterId());
			this.scaling = Scaling.valueOf((String) parameters.get(dimension.getScalingParameterId()));
		}
		
		
		public abstract Coding<D> codingForDataRange(Continuum<Double> usableContinuum, Range<Double> scaledRange, Shapefile shapefile);
		
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
		
		public <G> boolean isScalable(GeoDatum<G, D> geoDatum) {
			return scaling().isScalable(geoDatum.valueInDimension(dimension()));
		}
		
		public <G> double scale(GeoDatum<G, D> geoDatum) throws ScalingException {
			return scaling().scale(geoDatum.valueInDimension(dimension()));
		}
	}
}