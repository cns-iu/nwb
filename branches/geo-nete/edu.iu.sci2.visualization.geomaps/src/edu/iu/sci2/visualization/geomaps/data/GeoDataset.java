package edu.iu.sci2.visualization.geomaps.data;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;

import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.data.scaling.ScalingException;
import edu.iu.sci2.visualization.geomaps.utility.Range;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;

public class GeoDataset<G, D extends Enum<D> & VizDimension> {
	private final ImmutableCollection<GeoDatum<G, D>> geoData;
	private final ImmutableCollection<Binding<D>> bindings;
	
	private GeoDataset(
			Collection<? extends GeoDatum<G, D>> geoData,
			Collection<? extends Binding<D>> bindings) {
		this.geoData = ImmutableSet.copyOf(geoData);
		this.bindings = ImmutableSet.copyOf(bindings);
	}
	public static <G, D extends Enum<D> & VizDimension> GeoDataset<G, D> fromTable(
			final Table table,
			final Collection<? extends Binding<D>> bindings,
			final Class<D> dimensionClass,
			final Function<Tuple, G> geoMaker) {		
		final ImmutableMap<D, ? extends Binding<D>> dimensionToBinding = mapDimensionToBinding(bindings);
		
		ImmutableSet<Tuple> tuples = ImmutableSet.copyOf((Iterator<Tuple>) table.tuples());

		ImmutableSet<GeoDatum<G, D>> geoData = ImmutableSet.copyOf(Collections2.transform(
				tuples,
				new Function<Tuple, GeoDatum<G, D>>() {
					@Override
					public GeoDatum<G, D> apply(final Tuple tuple) {						
						EnumMap<D, Double> dimensionToValue =
								readValues(tuple, dimensionClass, dimensionToBinding);
						
						return GeoDatum.of(geoMaker.apply(tuple), dimensionToValue);				
					}					
				}));
		
		if (geoData.size() < tuples.size()) {
			int incompleteSpecificationCount = tuples.size() - geoData.size();		
			String warning = String.format(
					"%d rows of the table did not specify all required values and were skipped.",
					incompleteSpecificationCount);
			
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING,	warning);
		}

		return new GeoDataset<G, D>(ImmutableSet.copyOf(geoData), ImmutableSet.copyOf(bindings));
	}
	
	
	public ImmutableCollection<GeoDatum<G, D>> geoData() {
		return geoData;
	}

	public ImmutableCollection<Binding<D>> bindings() {
		return bindings;
	}
	
	public GeoDataset<G, D> viewScalableOnly() throws ScalingException {
		ImmutableCollection<GeoDatum<G, D>> rawData = geoData; // TODO
		
		ImmutableCollection<GeoDatum<G, D>> scalableData = rawData;
		for (Binding<D> binding : bindings) {
			scalableData = scalableOnDimension(scalableData, binding);
		}
		
		if (scalableData.isEmpty()) {
			throw new ScalingException("TODO None of the data is scalable.");
		}
		
		if (scalableData.size() < rawData.size()) {
			int unusableCount = rawData.size() - scalableData.size();
			
			GeoMapsAlgorithm.logger.log(
					LogService.LOG_WARNING,
					String.format(
							"TODO %d of the %d records contain values that are invalid for " +
							"the selecting scaling and will be ignored.",
							unusableCount,
							rawData.size()));
		}
		
		return new GeoDataset<G, D>(scalableData, bindings);
	}

	public GeoDataset<G, D> viewScaled() {
		ImmutableCollection<GeoDatum<G, D>> scaledData = geoData;
		for (Binding<D> binding : bindings) {			
			scaledData = scaledOnDimension(scaledData, binding);
		}
		
		return new GeoDataset<G, D>(scaledData, bindings);
	}

	public Range<Double> calculateRangeOver(final D dimension) {
		return Range.over(Collections2.transform(geoData, new Function<GeoDatum<G, D>, Double>() {
			@Override
			public Double apply(GeoDatum<G, D> geoDatum) {
				return geoDatum.valueInDimension(dimension);
			}			
		}));
	}
	private static <G, D extends Enum<D> & VizDimension> ImmutableCollection<GeoDatum<G, D>> scalableOnDimension(
			final Collection<GeoDatum<G, D>> geoData,
			final Binding<D> binding) {
		return ImmutableSet.copyOf(Collections2.filter(
				geoData,
				new Predicate<GeoDatum<G, D>>() {
					@Override
					public boolean apply(GeoDatum<G, D> geoDatum) {
						return binding.scaling().isScalable(geoDatum.valueInDimension(binding.dimension()));
					}					
				}));
	}
	
	private static <G, D extends Enum<D> & VizDimension> ImmutableCollection<GeoDatum<G, D>> scaledOnDimension(
			final Collection<GeoDatum<G, D>> geoData,
			final Binding<D> binding) {
		return ImmutableSet.copyOf(Collections2.transform(
				geoData,
				new Function<GeoDatum<G, D>, GeoDatum<G, D>>() {
					@Override
					public GeoDatum<G, D> apply(GeoDatum<G, D> geoDatum) {
						assert (binding.scaling().isScalable(geoDatum.valueInDimension(binding.dimension())));
						
						try {
							return GeoDatum.copyOf(geoDatum, binding.dimension(), binding.scaling().scale(geoDatum.valueInDimension(binding.dimension())));
						} catch (ScalingException e) {
							throw new RuntimeException("TODO", e);
						}
					}										
				}));
	}
	
	private static <D extends Enum<D> & VizDimension> ImmutableMap<D, ? extends Binding<D>> mapDimensionToBinding(
			Collection<? extends Binding<D>> bindings) {
		return Maps.uniqueIndex(
				bindings,
				new Function<Binding<D>, D>() {
					@Override
					public D apply(Binding<D> binding) {
						return binding.dimension();
					}
				});
	}
	
	private static <D extends Enum<D> & VizDimension> EnumMap<D, Double> readValues(
			final Tuple tuple,
			final Class<D> dimensionClass,
			final ImmutableMap<D, ? extends Binding<D>> dimensionToBinding) {			
		if (dimensionToBinding.isEmpty()) {
			return Maps.newEnumMap(dimensionClass);
		}
		
		return Maps.newEnumMap(Maps.transformValues(
					dimensionToBinding,
					new Function<Binding<D>, Double>() {
						@Override
						public Double apply(Binding<D> binding) {
							return binding.readValueFromTuple(tuple);
						}
					}));
	}
}
