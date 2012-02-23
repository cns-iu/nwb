package edu.iu.sci2.visualization.geomaps.geo.shapefiles;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.EnumSet;

import org.geotools.data.DataStore;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;

public enum Shapefile {
	UNITED_STATES(
			"United States",
			"NAME",
			KnownProjectedCRSDescriptor.LAMBERT,
			Resources.getResource(Shapefile.class, "st99_d00.shp"),
			ImmutableSet.of(
					AnchorPoint.NEAR_ALEUTIAN_ISLANDS,
					AnchorPoint.NEAR_PUERTO_RICO)),
	WORLD(
			"World",
			"NAME",
			KnownProjectedCRSDescriptor.ECKERT_IV,
			Resources.getResource(Shapefile.class, "countries.shp"),
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
	private final ImmutableCollection<AnchorPoint> anchorPoints;

	private Shapefile(
			String niceName,
			String featureAttributeName,
			KnownProjectedCRSDescriptor defaultProjectedCrs,
			URL url,
			Collection<AnchorPoint> anchorPoints) throws ShapefileException {
		this.niceName = niceName;
		this.featureAttributeName = featureAttributeName;
		this.defaultProjectedCrs = defaultProjectedCrs;
		this.anchorPoints = ImmutableSet.copyOf(anchorPoints);
		
		try {
			DataStore dataStore = new ShapefileDataStore(url);

			this.featureSource = dataStore.getFeatureSource(dataStore.getTypeNames()[0]);
		} catch (IOException e) {
			throw new ShapefileException("TODO Error accessing shapefile.", e);
		}
	}
	
	public CoordinateReferenceSystem detectCRS() {
		return Objects.firstNonNull( // TODO ?
				featureSource.getSchema().getCoordinateReferenceSystem(),
				DEFAULT_SOURCE_CRS);
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
	

	public static class AnchorPoint {
		public static final Shapefile.AnchorPoint NEAR_ALASKA =
				AnchorPoint.at(
						"Near Alaska",
						new Coordinate(-179,	89 - GeometryProjector.NORTH_POLE_CROP_HEIGHT_IN_DEGREES));
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
