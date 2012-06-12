package edu.iu.sci2.visualization.geomaps.viz.model;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;

import org.cishell.utilities.NumberUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.sci2.visualization.geomaps.data.GeoDataset;
import edu.iu.sci2.visualization.geomaps.data.GeoDataset.Stage;
import edu.iu.sci2.visualization.geomaps.data.GeoDatum;
import edu.iu.sci2.visualization.geomaps.viz.AnnotationMode;
import edu.iu.sci2.visualization.geomaps.viz.Circle;
import edu.iu.sci2.visualization.geomaps.viz.CircleDimension;
import edu.iu.sci2.visualization.geomaps.viz.FeatureView;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.coding.Coding;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public class CircleAnnotationMode extends AnnotationMode<Coordinate, CircleDimension> {
	public static final Ordering<GeoDatum<Coordinate, CircleDimension>> AREA_ORDERING =
			Ordering.natural().nullsFirst().onResultOf(
					new Function<GeoDatum<Coordinate, CircleDimension>, Double>() {
						@Override
						public Double apply(GeoDatum<Coordinate, CircleDimension> input) {
							return input.valueInDimension(CircleDimension.AREA);
						}				
					});
	
	private final String longitudeColumnName;
	private final String latitudeColumnName;
	
	public CircleAnnotationMode(String longitudeColumnName, String latitudeColumnName) {
		this.longitudeColumnName = longitudeColumnName;
		this.latitudeColumnName = latitudeColumnName;
	}

	
	@Override
	protected EnumSet<CircleDimension> dimensions() {
		return EnumSet.allOf(CircleDimension.class);
	}

	@Override
	protected GeoDataset<Coordinate, CircleDimension> readTable(Table table,
			Collection<Binding<CircleDimension>> bindings) {
		return GeoDataset.fromTable(table, bindings, CircleDimension.class, new CoordinateReader(
				longitudeColumnName, latitudeColumnName));
	}
	
	public static Collection<Circle> asCirclesInDrawingOrder(
			Collection<? extends GeoDatum<Coordinate, CircleDimension>> geoData,
			final Collection<? extends Coding<CircleDimension>> codings) {
		return Collections2.transform(
				AREA_ORDERING.reverse().sortedCopy(geoData),
				new Function<GeoDatum<Coordinate, CircleDimension>, Circle>() {
					@Override
					public Circle apply(GeoDatum<Coordinate, CircleDimension> geoDatum) {
						Coordinate coordinate = geoDatum.getGeo();
	
						EnumMap<CircleDimension, Strategy> strategies =
								Maps.newEnumMap(CircleDimension.class);
						for (Coding<CircleDimension> coding : codings) {
							strategies.put(
									coding.dimension(),
									coding.strategyForValue(
											geoDatum.valueInDimension(coding.dimension())));
						}
	
						return new Circle(coordinate, strategies);
					}
				});
	}

	@Override
	protected Collection<Circle> makeCircles(
			GeoDataset<Coordinate, CircleDimension> scaledData,
			Collection<? extends Coding<CircleDimension>> codings) {
		return asCirclesInDrawingOrder(scaledData.geoData(Stage.SCALED), codings);
	}

	
	@Override
	protected Collection<FeatureView> makeFeatureViews(
			GeoDataset<Coordinate, CircleDimension> scaledData,
			Collection<? extends Coding<CircleDimension>> codings) {
		return ImmutableSet.<FeatureView>of(); // No feature views in the circles mode
	}

	private static class CoordinateReader implements GeoIdentifierReader<Coordinate> {
		private final String longitudeColumnName;
		private final String latitudeColumnName;
		
		CoordinateReader(String longitudeColumnName, String latitudeColumnName) {
			this.longitudeColumnName = longitudeColumnName;
			this.latitudeColumnName = latitudeColumnName;
		}
	
		@Override
		public Coordinate readFrom(Tuple tuple) throws GeoIdentifierException {
			Object xObject = tuple.get(longitudeColumnName);
			Object yObject = tuple.get(latitudeColumnName);

			if (xObject == null || yObject == null) {
				throw new GeoIdentifierException(
						String.format("Missing coordinate info in tuple %s.", tuple));
			}			
			Double x = NumberUtilities.interpretObjectAsDouble(xObject);
			Double y = NumberUtilities.interpretObjectAsDouble(yObject);
			
			if (x == null || y == null) {
				throw new GeoIdentifierException(
						String.format("Uninterpretable coordinate info in tuple %s.", tuple));
			}			
			return new Coordinate(x, y);
		}
	}
}