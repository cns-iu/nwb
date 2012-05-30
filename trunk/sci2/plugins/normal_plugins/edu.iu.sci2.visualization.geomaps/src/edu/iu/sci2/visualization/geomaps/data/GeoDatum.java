package edu.iu.sci2.visualization.geomaps.data;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import org.cishell.utilities.NumberUtilities;

import prefuse.data.Tuple;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;

public class GeoDatum<G, D extends Enum<D> & VizDimension> {
	private final G geo;	
	private final EnumMap<D, Double> values;		
	
	private GeoDatum(G geo, EnumMap<D, Double> values) {
		this.geo = geo;
		this.values = Maps.newEnumMap(values);
	}
	public static <G, D extends Enum<D> & VizDimension> GeoDatum<G, D> of(G geo, EnumMap<D, Double> values) {
		return new GeoDatum<G, D>(geo, values);		
	}
	public static <G, D extends Enum<D> & VizDimension> GeoDatum<G, D> modifiedCopyOf(
			GeoDatum<G, D> geoDatum, D dimension, Double newValue) {
		return modifiedCopyOf(geoDatum, Maps.newEnumMap(ImmutableMap.of(dimension, newValue)));
	}
	public static <G, D extends Enum<D> & VizDimension> GeoDatum<G, D> modifiedCopyOf(
			GeoDatum<G, D> geoDatum, EnumMap<D, Double> overridingValues) {
		EnumMap<D, Double> updatedValues = Maps.newEnumMap(geoDatum.values);
		updatedValues.putAll(overridingValues);
		
		return of(geoDatum.geo, updatedValues);
	}

	/**
	 * @throws GeoDatumValueInterpretationException
	 *             If the value in any dimension is absent or non-numeric
	 */
	public static <G, D extends Enum<D> & VizDimension> GeoDatum<G, D> forTuple(Tuple tuple,
			Map<D, ? extends Binding<D>> dimensionToBinding, Class<D> dimensionClass,
			G geoIdentifier) throws GeoDatumValueInterpretationException {
		EnumMap<D, Double> dimensionToValue = Maps.newEnumMap(dimensionClass);
		
		for (Entry<D, ? extends Binding<D>> dimensionAndBinding : dimensionToBinding.entrySet()) {
			D dimension = dimensionAndBinding.getKey();
			Binding<D> binding = dimensionAndBinding.getValue();
			
			Object valueObject = tuple.get(binding.columnName());

			if (valueObject == null) {
				throw new GeoDatumValueInterpretationException(String.format(
						"No value specified in dimension %s for tuple %s.", dimension, tuple));
			}

			try {
				double value = NumberUtilities.interpretObjectAsDouble(valueObject);

				dimensionToValue.put(dimension, value);
			} catch (NumberFormatException e) {
				throw new GeoDatumValueInterpretationException(
						String.format(
								"Value \"%s\" specified in dimension %s for tuple %s couldn't be " +
								"interpreted as a number.",
								String.valueOf(valueObject), dimension, tuple),
						e);
			}
		}
		
		return GeoDatum.of(geoIdentifier, dimensionToValue);
	}
	
	
	public G getGeo() {
		return geo;
	}

	public Double valueInDimension(D dimension) {
		return values.get(dimension);
	}

	@Override
	public boolean equals(Object thatObject) {
		if (this == thatObject) { return true; }
		if (thatObject == null) { return false;	}
		if (!(thatObject instanceof GeoDatum<?, ?>)) { return false; }			
		GeoDatum<?, ?> that = (GeoDatum<?, ?>) thatObject;
		
		return Objects.equal(this.geo,
							 that.geo)
			&& Objects.equal(this.values,
							 that.values);
	}
		
	@Override
	public int hashCode() {
		return Objects.hashCode(geo, values);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("geo", geo)
				.add("values", values)
				.toString();
	}
	
	
	public static class GeoDatumValueInterpretationException extends Exception {
		private static final long serialVersionUID = 8535808482844659436L;

		private GeoDatumValueInterpretationException(String message, Throwable cause) {
			super(message, cause);
		}

		private GeoDatumValueInterpretationException(String message) {
			super(message);
		}
	}
}
