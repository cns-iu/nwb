package edu.iu.sci2.visualization.geomaps.geo.shapefiles;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.utility.NicelyNamedEnums.NicelyNamed;
public enum Shapefile implements NicelyNamed {
	UNITED_STATES(
			Resources.getResource(Shapefile.class, "st99_d00.shp"),
			"United States",
			"%d U.S. states and other jurisdictions",
			"U.S. State",
			"U.S. state",
			"NAME",
			KnownProjectedCRSDescriptor.LAMBERT,
			ImmutableSet.of(InsetRequest.ALASKA, InsetRequest.HAWAII, InsetRequest.PUERTO_RICO),
			ImmutableSet.of(
					AnchorPoint.NEAR_ALEUTIAN_ISLANDS,
					AnchorPoint.NEAR_PUERTO_RICO)),
	WORLD(
			Resources.getResource(Shapefile.class, "countries.shp"),
			"World",
			"%d countries of the world",
			"Country",
			"country",
			"NAME",
			KnownProjectedCRSDescriptor.ECKERT_IV,
			ImmutableSet.<InsetRequest>of(),
			ImmutableSet.of(
					AnchorPoint.NEAR_ALASKA,
					AnchorPoint.NEAR_ANTARCTICA));
	
	public static final DefaultGeographicCRS FALLBACK_SOURCE_CRS = DefaultGeographicCRS.WGS84;
	
	private final String niceName;
	private final String mapDescriptionFormat;
	private final String componentDescriptionTitleCase;
	private final String componentDescriptionPlain;
	private final String featureAttributeName;
	private final KnownProjectedCRSDescriptor defaultProjectedCrs;
	private final SimpleFeatureSource featureSource;
	private final Map<ReferencedEnvelope, Inset> boundsToInset;
	private final ImmutableCollection<AnchorPoint> anchorPoints;


	private Shapefile(
			URL url,
			String niceName,
			String mapDescriptionFormat,
			String componentDescriptionTitleCase,
			String componentDescriptionPlain,
			String featureAttributeName,
			KnownProjectedCRSDescriptor defaultProjectedCrs,
			Collection<InsetRequest> insetRequests,
			Collection<AnchorPoint> anchorPoints) throws ShapefileException {
		this.niceName = niceName;
		this.mapDescriptionFormat = mapDescriptionFormat;
		this.componentDescriptionTitleCase = componentDescriptionTitleCase;
		this.componentDescriptionPlain = componentDescriptionPlain;
		this.featureAttributeName = featureAttributeName;
		this.defaultProjectedCrs = defaultProjectedCrs;
		this.anchorPoints = ImmutableSet.copyOf(anchorPoints);
		
		try {
			DataStore dataStore = new ShapefileDataStore(url);
			
			String typeName = dataStore.getTypeNames()[0];
			this.featureSource = dataStore.getFeatureSource(typeName);
			
			boundsToInset = Maps.newHashMap();
			for (InsetRequest insetRequest : insetRequests) {
				Query query = new Query(typeName, CQL.toFilter(String.format("NAME = '%s'", insetRequest.getName())));
				SimpleFeatureCollection featuresToInset = featureSource.getFeatures(query);				
				ReferencedEnvelope bounds = featuresToInset.getBounds();
				Inset inset = new Inset(insetRequest, bounds);
				
				boundsToInset.put(bounds, inset);
			}
		} catch (IOException e) {
			throw new ShapefileException("The shapefile data store could not be opened.", e);
		} catch (CQLException e) {
			throw new ShapefileException("Error detecting boundaries of map insets.", e);
		}
		
		
	}
	
	/**
	 * If this feature source schema has no associated coordinate reference system,
	 * {@link #FALLBACK_SOURCE_CRS} is used instead.
	 */
	public CoordinateReferenceSystem detectNativeCRS() {
		return Objects.firstNonNull(
				featureSource.getSchema().getCoordinateReferenceSystem(),
				FALLBACK_SOURCE_CRS);
	}
	
	public Inset inset(Geometry geometry) {
		// TODO Terrible, order-sensitive, behavior not clear when any two bounds overlap
		for (Entry<ReferencedEnvelope, Inset> entry : boundsToInset.entrySet()) {
			if (entry.getKey().contains(geometry.getEnvelopeInternal())) {
				return entry.getValue();
			}
		}
				
		return null; // TODO Just for now
	}
	
	public String extractFeatureName(SimpleFeature feature) {
		Object featureName = feature.getAttribute(featureAttributeName);

		if (featureName != null) {
			return String.valueOf(featureName);
		}
		
		// Error
		String message = String.format(
				"Feature %s has no \"%s\" property.  ", feature, featureAttributeName);
		
		message += "Consider using one of these properties: ";		
		List<String> attributeDescriptorNames = Lists.newArrayList();
		for (AttributeDescriptor attributeDescriptor
				: viewOfFeatureCollection().getSchema().getAttributeDescriptors()) {
			attributeDescriptorNames.add(attributeDescriptor.getName().toString());
		}		
		message += Joiner.on(",").join(attributeDescriptorNames);
		
		throw new FeatureAttributeAbsentException(message);
	}
	
	public static class FeatureAttributeAbsentException extends RuntimeException {
		private static final long serialVersionUID = -6714673548077059632L;

		public FeatureAttributeAbsentException(String message) {
			super(message);
		}		
	}

	@Override
	public String toString() {
		return niceName;
	}

	public boolean hasInsets() {
		return !boundsToInset.isEmpty();
	}
	
	@Override
	public String getNiceName() {
		return niceName;
	}
	
	public String makeMapDescription() {
		Set<String> uniqueFeatureNames = Sets.newHashSet();
		
		FeatureIterator<SimpleFeature> it = viewOfFeatureCollection().features();
		while (it.hasNext()) {
			uniqueFeatureNames.add(extractFeatureName(it.next()));
		}
		it.close();
		
		return String.format(mapDescriptionFormat, uniqueFeatureNames.size());
	}
	
	public String getComponentDescriptionTitleCase() {
		return componentDescriptionTitleCase;
	}
	
	public String getComponentDescriptionPlain() {
		return componentDescriptionPlain;
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
	
	public FeatureCollection<SimpleFeatureType, SimpleFeature> viewOfFeatureCollection()
			throws ShapefileFeatureRetrievalException {
		try {
			return featureSource.getFeatures();
		} catch (IOException e) {
			throw new ShapefileFeatureRetrievalException("Error accessing shapefile: " + e.getMessage(), e);
		}
	}
	
	
	public static class Inset {
		private InsetRequest request;
		private ReferencedEnvelope bounds;
		
		Inset(InsetRequest request, ReferencedEnvelope bounds) {
			this.request = request;
			this.bounds = bounds;
		}

		public Geometry inset(Geometry geometry, GeometryProjector geometryProjector) {
			GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
			Point srcGeoPoint = geometryFactory.createPoint(request.getSrc());
			Point destGeoPoint = geometryFactory.createPoint(request.getDest());
			
			try {
				Point srcDisplayPoint = geometryProjector.transformGeometry(srcGeoPoint).getCentroid(); // TODO ?
				Point destDisplayPoint = geometryProjector.transformGeometry(destGeoPoint).getCentroid(); // TODO ?
				
				Coordinate srcDisplayCoordinate = new Coordinate(srcDisplayPoint.getX(), srcDisplayPoint.getY());
				Coordinate destDisplayCoordinate = new Coordinate(destDisplayPoint.getX(), destDisplayPoint.getY());
				
				AffineTransform preScale = AffineTransform.getTranslateInstance(-srcDisplayCoordinate.x, -srcDisplayCoordinate.y);
				AffineTransform scale = AffineTransform.getScaleInstance(request.getScaling(), request.getScaling());
				AffineTransform postScale = AffineTransform.getTranslateInstance(destDisplayCoordinate.x, destDisplayCoordinate.y);			

				AffineTransform2D transform = new AffineTransform2D(preConcatenate(preScale, scale, postScale));
				
				return JTS.transform(geometry, transform);
			} catch (TransformException e) {
				throw new RuntimeException("Inset transform failed."); // TODO
			}
		}

		private static AffineTransform preConcatenate(
				AffineTransform first, AffineTransform... rest) {
			AffineTransform concat = (AffineTransform) first.clone();
			for (AffineTransform subsequent : rest) {
				concat.preConcatenate(subsequent);
			}
			
			return concat;
		}
	}
	

	public static class InsetRequest {
		public static final InsetRequest ALASKA = InsetRequest.of(
				"Alaska", 0.3, new Coordinate(-141.0, 69.7), new Coordinate(-109.5, 28.1));
		public static final InsetRequest HAWAII = InsetRequest.of(
				"Hawaii", 0.5, new Coordinate(-155.7, 18.9), new Coordinate(-102.7, 25.0));
		public static final InsetRequest PUERTO_RICO = InsetRequest.of(
				"Puerto Rico", 1, new Coordinate(-67.3, 18.3), new Coordinate(-88.9, 24.2));

		private final String name;
		private final double scaling;
		private final Coordinate src;
		private final Coordinate dest;

		public InsetRequest(String name, double scaling, Coordinate src, Coordinate dest) {
			this.name = name;
			this.scaling = scaling;
			this.src = src;
			this.dest = dest;
		}
		
		static InsetRequest of(String name, double scaling, Coordinate src, Coordinate dest) {
			return new InsetRequest(name, scaling, src, dest);
		}
				

		public String getName() {
			return name;
		}
		
		public double getScaling() {
			return scaling;
		}
		
		public Coordinate getSrc() {
			return src;
		}
		
		public Coordinate getDest() {
			return dest;
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

	
	public static class ShapefileFeatureRetrievalException extends RuntimeException {
		private static final long serialVersionUID = -5812550122559595790L;

		public ShapefileFeatureRetrievalException(String message, Throwable cause) {
			super(message, cause);
		}		
	}
	
	
	public static class ShapefileException extends RuntimeException {
		private static final long serialVersionUID = -9175935612884445370L;
		
		public ShapefileException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
