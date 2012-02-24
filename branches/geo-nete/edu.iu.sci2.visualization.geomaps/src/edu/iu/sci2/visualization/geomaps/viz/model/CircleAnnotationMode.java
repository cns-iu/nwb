package edu.iu.sci2.visualization.geomaps.viz.model;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;

import org.cishell.utilities.NumberUtilities;
import org.geotools.factory.FactoryRegistryException;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.sci2.visualization.geomaps.GeoMapsCirclesFactory;
import edu.iu.sci2.visualization.geomaps.data.GeoDataset;
import edu.iu.sci2.visualization.geomaps.data.GeoDataset.Stage;
import edu.iu.sci2.visualization.geomaps.data.GeoDatum;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.viz.AnnotationMode;
import edu.iu.sci2.visualization.geomaps.viz.Circle;
import edu.iu.sci2.visualization.geomaps.viz.CircleDimension;
import edu.iu.sci2.visualization.geomaps.viz.FeatureView;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension.Binding;
import edu.iu.sci2.visualization.geomaps.viz.coding.Coding;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS.ShapefilePostScriptWriterException;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public class CircleAnnotationMode extends AnnotationMode<Coordinate, CircleDimension> {
	// TODO This doesn't seem like a great way to handle the possible nullity?  Maybe it's good to keep defensively in any case, but this shouldn't be the only solution/band-aid.
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
		return GeoDataset.fromTable(table, bindings, CircleDimension.class,
				new Function<Tuple, Coordinate>() {
					@Override
					public Coordinate apply(Tuple input) {
						return new Coordinate(
								NumberUtilities.interpretObjectAsDouble(input.get(longitudeColumnName)),
								NumberUtilities.interpretObjectAsDouble(input.get(latitudeColumnName)));
					}					
				});
	}
	
	@Override
	protected GeoMap createGeoMap(Shapefile shapefile,
			KnownProjectedCRSDescriptor projectedCrs,
			GeoDataset<Coordinate, CircleDimension> scaledData,
			Collection<? extends Coding<CircleDimension>> codings,
			Collection<PostScriptable> legends) throws ShapefilePostScriptWriterException, FactoryRegistryException, GeoMapException {
		Collection<Circle> circles = asCirclesInDrawingOrder(scaledData.geoData(Stage.SCALED), codings);
		
		return new GeoMap(
				GeoMapsCirclesFactory.SUBTITLE,
				shapefile,
				projectedCrs,
				ImmutableSet.<FeatureView>of(),
				circles,
				legends);
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
}