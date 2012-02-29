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
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;

public enum Shapefile {
	UNITED_STATES(
			Resources.getResource(Shapefile.class, "st99_d00.shp"),
			"United States",
			"NAME",
			KnownProjectedCRSDescriptor.LAMBERT,
			ImmutableSet.of(Inset.ALASKA),
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
							return shapefile.niceName(); }}));
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
	private final ImmutableSet<Inset> insets;
	private final transient ImmutableMap<String, Inset> insetForFeatureName;
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
		this.insets = ImmutableSet.copyOf(insets);
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
	
	public Geometry translateForInset(String rawFeatureName, Geometry geometry) throws MismatchedDimensionException, TransformException {
		String featureName = rawFeatureName.toLowerCase(); // TODO fix normalization at both ends
		
		if (!insetForFeatureName.containsKey(featureName)) {
			return geometry;
		}
		
		return insetForFeatureName.get(featureName).translate(geometry);
	}
	
	public String extractFeatureName(SimpleFeature feature) {
		return String.valueOf(feature.getAttribute(featureAttributeName));
	}

	@Override
	public String toString() {
		return niceName;
	}

	public String niceName() {
		return niceName;
	}
	
	public String featureAttributeName() {
		return featureAttributeName;
	}
	
	public KnownProjectedCRSDescriptor defaultProjectedCrs() {
		return defaultProjectedCrs;
	}
	
	public ImmutableCollection<AnchorPoint> anchorPoints() {
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
	
	public static void main(String[] args) { // TODO
		try {
			Shapefile.WORLD.getBounds();
			System.out.println();
			Shapefile.UNITED_STATES.getBounds();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FeatureCollection<SimpleFeatureType, SimpleFeature> viewOfFeatureCollection()
			throws ShapefileFeatureRetrievalException {
		try {
			return featureSource.getFeatures();
		} catch (IOException e) {
			throw new ShapefileFeatureRetrievalException("Error accessing shapefile: " + e.getMessage(), e);
		}
	}
	
//	public FeatureIterator<SimpleFeature> features() {
//		try {
//			return Iterators.transform(
//					featureSource.getFeatures().features(),
//					new Function<SimpleFeature, SimpleFeature>() {
//						
//					});
//		} catch (IOException e) {
//			throw new ShapefileFeatureRetrievalException("Error accessing shapefile: " + e.getMessage(), e);
//		}
//	}
	
	public static class Inset {
		public static final Inset ALASKA = Inset.translating("Alaska", +20.0, -40.0); // TODO test these values

		private final String featureName;
		private final double longitudeTranslation;
		private final double latitudeTranslation;

		private Inset(String featureName, double longitudeTranslation, double latitudeTranslation) {
			this.featureName = featureName;
			this.longitudeTranslation = longitudeTranslation;
			this.latitudeTranslation = latitudeTranslation;
		}
		public static Inset translating(
				String featureName, double longitudeTranslation, double latitudeTranslation) {
			return new Inset(featureName, longitudeTranslation, latitudeTranslation);
		}
		
		
		public Coordinate translate(Coordinate coordinate) {
			return new Coordinate(
					coordinate.x + longitudeTranslation,
					coordinate.y + latitudeTranslation);
		}
		
		public Geometry translate(Geometry geometry) throws MismatchedDimensionException, TransformException {
			return JTS.transform(
					geometry,
					new AffineTransform2D(AffineTransform.getTranslateInstance(
							longitudeTranslation, latitudeTranslation)));
		}
		
		public String featureName() {
			return featureName;
		}
		
		public double longitudeTranslation() {
			return longitudeTranslation;
		}
		
		public double latitudeTranslation() {
			return latitudeTranslation;
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
