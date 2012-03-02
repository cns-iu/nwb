package edu.iu.sci2.visualization.geomaps.geo.shapefiles;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.EnumSet;

import org.geotools.data.DataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;

public enum Shapefile {
	UNITED_STATES(
			Resources.getResource(Shapefile.class, "st99_d00.shp"),
			"United States",
			"NAME",
			KnownProjectedCRSDescriptor.LAMBERT,
			ImmutableSet.of(Inset.ALASKA, Inset.HAWAII, Inset.PUERTO_RICO),
			ImmutableSet.of(
					AnchorPoint.NEAR_ALEUTIAN_ISLANDS,
					AnchorPoint.NEAR_PUERTO_RICO)),
	WORLD(
			Resources.getResource(Shapefile.class, "countries.shp"),
			"World",
			"NAME",
			KnownProjectedCRSDescriptor.ECKERT_IV,
			ImmutableSet.<Inset>of(),
			ImmutableSet.of(
					AnchorPoint.NEAR_ALASKA,
					AnchorPoint.NEAR_ANTARCTICA));

	public static final DefaultGeographicCRS DEFAULT_SOURCE_CRS = DefaultGeographicCRS.WGS84;
	private static final ImmutableBiMap<String, Shapefile> FOR_NICE_NAME =
			ImmutableBiMap.copyOf(Maps.uniqueIndex(
					EnumSet.allOf(Shapefile.class),
					new Function<Shapefile, String>() {
						@Override
						public String apply(Shapefile shapefile) {
							return shapefile.getNiceName(); }}));
	public static ImmutableSet<String> byNiceNames() {
		return FOR_NICE_NAME.keySet();
	}
	public static Shapefile forNiceName(String niceName) {
		// TODO Null?
		return FOR_NICE_NAME.get(niceName);
	}


	private final String niceName;
	private final String featureAttributeName;
	private final KnownProjectedCRSDescriptor defaultProjectedCrs;
	private final SimpleFeatureSource featureSource;
	private final ImmutableMap<String, Inset> insetForFeatureName;
	private final ImmutableCollection<AnchorPoint> anchorPoints;

	private Shapefile(
			URL url,
			String niceName,
			String featureAttributeName,
			KnownProjectedCRSDescriptor defaultProjectedCrs,
			Collection<Inset> insets,
			Collection<AnchorPoint> anchorPoints) throws ShapefileException {
		this.niceName = niceName;
		this.featureAttributeName = featureAttributeName;
		this.defaultProjectedCrs = defaultProjectedCrs;
		this.anchorPoints = ImmutableSet.copyOf(anchorPoints);
		
		this.insetForFeatureName = Maps.uniqueIndex(insets, new Function<Inset, String>() {
			@Override
			public String apply(Inset inset) {
				return inset.featureName().toLowerCase(); // TODO fix normalization at both ends
			}			
		});
		
		try {
			DataStore dataStore = new ShapefileDataStore(url);

			this.featureSource = dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
		} catch (IOException e) {
			throw new ShapefileException("TODO Error accessing shapefile.", e);
		}
	}
	
	public CoordinateReferenceSystem detectNativeCRS() {
		return Objects.firstNonNull( // TODO ?
				featureSource.getSchema().getCoordinateReferenceSystem(),
				DEFAULT_SOURCE_CRS);
	}
	
	public Geometry inset(String rawFeatureName, Geometry geometry) throws MismatchedDimensionException, TransformException {
		String featureName = rawFeatureName.toLowerCase(); // TODO fix normalization at both ends
		
		if (!insetForFeatureName.containsKey(featureName)) {
			return geometry;
		}
		
		return insetForFeatureName.get(featureName).inset(geometry);
	}
	
	public String extractFeatureName(SimpleFeature feature) {
		Object featureName = feature.getAttribute(featureAttributeName);
		
		if (featureName == null) {
			String message =
					String.format(
							"Feature %s has no \"%s\" property.  " +
							"Consider using one of these properties: " +
							Joiner.on(",").join(Lists.transform(
									viewOfFeatureCollection().getSchema().getAttributeDescriptors(),
									new Function<AttributeDescriptor, String>() {
										@Override
										public String apply(AttributeDescriptor input) {
											return input.getName().toString();
										}									
									}),
							"\n",
							feature,
							featureAttributeName));
			
			throw new RuntimeException(message); // TODO !
		}
		return String.valueOf(featureName);
	}

	@Override
	public String toString() {
		return niceName;
	}

	public String getNiceName() {
		return niceName;
	}
	
	public String getFeatureAttributeName() {
		return featureAttributeName;
	}
	
	public KnownProjectedCRSDescriptor getDefaultProjectedCrs() {
		return defaultProjectedCrs;
	}
	
	public ImmutableCollection<AnchorPoint> getAnchorPoints() {
		return anchorPoints;
	}
	
	public ReferencedEnvelope getBounds() throws IOException { // TODO
		ReferencedEnvelope bounds = featureSource.getBounds();
		System.out.println(bounds.getMinX());
		System.out.println(bounds.getMinY());
		System.out.println(bounds.getMaxX());
		System.out.println(bounds.getMaxY());
		return bounds;
	}


	public FeatureCollection<SimpleFeatureType, SimpleFeature> viewOfFeatureCollection()
			throws ShapefileFeatureRetrievalException {
		try {
			return featureSource.getFeatures();
		} catch (IOException e) {
			throw new ShapefileFeatureRetrievalException("Error accessing shapefile: " + e.getMessage(), e);
		}
	}

	
	public static class Inset {
		public static final Inset ALASKA = Inset.inset(
				"Alaska", new Coordinate(-129.7, 52.3), new Coordinate(-125.7, 48.5));
		public static final Inset HAWAII = Inset.inset(
				"Hawaii", new Coordinate(-155.7, 18.9), new Coordinate(-102.7, 25.0));
		public static final Inset PUERTO_RICO = Inset.inset(
				"Puerto Rico", new Coordinate(-67.3, 18.3), new Coordinate(-88.9, 24.2));
		
		private final String featureName;
		private final MathTransform transform;

		private Inset(String featureName, MathTransform transform) {
			this.featureName = featureName;			
			this.transform = transform;
			
		}
		public static Inset inset(
				String featureName, Coordinate anchor, Coordinate dest) {
			return new Inset(
					featureName,
					new AffineTransform2D(
							AffineTransform.getTranslateInstance(
									dest.x - anchor.x, dest.y - anchor.y)));
		}

		
		public String featureName() {
			return featureName;
		}
		
		public Geometry inset(Geometry geometry) throws MismatchedDimensionException, TransformException {
			return JTS.transform(geometry, transform);
		}
	}

	public static class AnchorPoint {
		public static final Shapefile.AnchorPoint NEAR_ALASKA =
				AnchorPoint.at(
						"Near Alaska",
						new Coordinate(-179, 89 - GeometryProjector.NORTH_POLE_CROP_HEIGHT_IN_DEGREES));
		public static final Shapefile.AnchorPoint NEAR_ALEUTIAN_ISLANDS =
				AnchorPoint.at(
						"Near Aleutian Islands",
						new Coordinate(-179, 50));
		public static final Shapefile.AnchorPoint NEAR_ANTARCTICA =
				AnchorPoint.at(
						"Near Antarctica",
						new Coordinate(179, -89 + GeometryProjector.SOUTH_POLE_CROP_HEIGHT_IN_DEGREES));
		public static final Shapefile.AnchorPoint NEAR_PUERTO_RICO =
				AnchorPoint.at(
						"Near Puerto Rico",
						new Coordinate(-64, 16));

		private final String displayName;
		private final Coordinate coordinate;

		private AnchorPoint(String displayName, Coordinate coordinate) {
			this.displayName = displayName;
			this.coordinate = coordinate;
		}
		public static AnchorPoint at(String displayName, Coordinate coordinate) {
			return new AnchorPoint(displayName, coordinate);
		}

		public String getDisplayName() {
			return displayName;
		}

		public Coordinate getCoordinate() {
			return coordinate;
		}
		
		@Override
		public boolean equals(Object thatObject) {
			if (this == thatObject) {
				return true;
			}
			if (thatObject == null) {
				return false;
			}
			if (!(thatObject instanceof AnchorPoint)) {
				return false;
			}
			AnchorPoint that = (AnchorPoint) thatObject;
		
			return Objects.equal(this.displayName, that.displayName) &&
				   Objects.equal(this.coordinate, that.coordinate);
		}
		
		@Override
		public int hashCode() {
			return Objects.hashCode(displayName, coordinate);
		}		
	}

	
	public static class ShapefileFeatureRetrievalException extends RuntimeException { // TODO Unchecked is ok?
		private static final long serialVersionUID = -5812550122559595790L;

		public ShapefileFeatureRetrievalException(String message, Throwable cause) {
			super(message, cause);
		}		
	}
	
	
	public static class ShapefileException extends RuntimeException { // TODO Unchecked is ok?
		private static final long serialVersionUID = -9175935612884445370L;
		
		public ShapefileException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
