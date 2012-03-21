package edu.iu.sci2.visualization.geomaps.data;

import java.util.EnumMap;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import edu.iu.sci2.visualization.geomaps.viz.VizDimension;

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
	public static <G, D extends Enum<D> & VizDimension> GeoDatum<G, D> copyOf(GeoDatum<G, D> geoDatum, D dimension, Double newValue) {
		return copyOf(geoDatum, Maps.newEnumMap(ImmutableMap.of(dimension, newValue)));
	}
	public static <G, D extends Enum<D> & VizDimension> GeoDatum<G, D> copyOf(GeoDatum<G, D> geoDatum, EnumMap<D, Double> overridingValues) {
		EnumMap<D, Double> updatedValues = Maps.newEnumMap(geoDatum.values);
		updatedValues.putAll(overridingValues);
		
		return of(geoDatum.geo, updatedValues);
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
}
