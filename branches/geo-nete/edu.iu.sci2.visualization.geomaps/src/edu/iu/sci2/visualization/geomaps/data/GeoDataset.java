package edu.iu.sci2.visualization.geomaps.data;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;

import org.cishell.utilities.NumberUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.data.scaling.ScalingException;
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
	
	/**
	 * A "stage" of this immutable geo data is a view of its contents after applying zero or more
	 * filters or transformations from the below sequence.
	 */
	public enum Stage {
		/**
		 * The data in its original form.
		 */
		RAW {
			@Override
			protected <G, D extends Enum<D> & VizDimension> Collection<GeoDatum<G, D>> transform(
					Collection<GeoDatum<G, D>> geoData, Collection<Binding<D>> bindings) {
				return geoData;
			}
		},
		/**
		 * Only the above data that is scalable by the given bindings.
		 */
		SCALABLE {
			@Override
			protected <G, D extends Enum<D> & VizDimension> Collection<GeoDatum<G, D>> transform(
					Collection<GeoDatum<G, D>> geoData,
					Collection<Binding<D>> bindings) {
				return Collections2.filter(
						geoData,
						Predicates.and(
								Collections2.transform(
										bindings,
										new Function<Binding<D>, Predicate<GeoDatum<G, D>>>() {
											@Override
											public Predicate<GeoDatum<G, D>> apply(final Binding<D> binding) {
												return new Predicate<GeoDatum<G, D>>() {
													@Override
													public boolean apply(GeoDatum<G, D> geoDatum) {
														return binding.isScalable(geoDatum);
													}													
												};
											}											
										})));
			}
		},
		/**
		 * The above data scaled by the given bindings.
		 */
		SCALED {
			@Override
			protected <G, D extends Enum<D> & VizDimension> Collection<GeoDatum<G, D>> transform(
					Collection<GeoDatum<G, D>> geoData, final Collection<Binding<D>> bindings) {
				return Collections2.transform(
						geoData,
						new Function<GeoDatum<G, D>, GeoDatum<G, D>>() {
							@Override
							public GeoDatum<G, D> apply(GeoDatum<G, D> geoDatum) {
								GeoDatum<G, D> scaled = geoDatum;
								for (Binding<D> binding : bindings) {
									try {
										scaled = GeoDatum.copyOf(scaled, binding.dimension(), binding.scale(geoDatum));
									} catch (ScalingException e) {
										String message = String.format(
												"The geo datum %s is not scalable by %s.",
												geoDatum, binding);
										throw new AssertionError(message);
									}
								}
								
								return scaled;
							}										
						});
			}			
		};
		
		protected abstract <G, D extends Enum<D> & VizDimension> Collection<GeoDatum<G, D>> transform(
				Collection<GeoDatum<G, D>> geoData,
				Collection<Binding<D>> bindings);
		
		public <G, D extends Enum<D> & VizDimension> Collection<GeoDatum<G, D>> transformTo(GeoDataset<G, D> geoDataset) {
			Collection<GeoDatum<G, D>> transformed = geoDataset.geoData;
			
			for (Stage stage : EnumSet.range(Stage.values()[0], this)) {
				transformed = stage.transform(transformed, geoDataset.bindings);
			}
			
			return transformed;
		}
	}
	
	public Collection<GeoDatum<G, D>> geoData(Stage stage) {
		return stage.transformTo(this);
	}

	public ImmutableCollection<Binding<D>> getBindings() {
		return bindings;
	}
	
	public Range<Double> calculateRangeOver(final D dimension, Stage stage) {
		return Ranges.encloseAll(Collections2.transform(stage.transformTo(this), new Function<GeoDatum<G, D>, Double>() {
			@Override
			public Double apply(GeoDatum<G, D> geoDatum) {
				return geoDatum.valueInDimension(dimension);
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
							return NumberUtilities.interpretObjectAsDouble(
									tuple.get(binding.columnName()));
						}
					}));
	}
}
