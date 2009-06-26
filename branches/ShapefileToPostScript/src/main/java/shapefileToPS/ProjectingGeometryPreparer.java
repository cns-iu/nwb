package shapefileToPS;

import java.util.HashSet;
import java.util.Set;

import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.Projection;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;

public class ProjectingGeometryPreparer {
	public static final String CENTRAL_MERIDIAN_PARAMETER_KEY = "central_meridian";
	public static final double MERIDIAN_CROP_WIDTH_AT_EQUATOR = .01;
	public static final double MIN_LATITUDE = -90.0;
	public static final double EQUATOR_LATITUDE = 0.0;
	public static final double MAX_LATITUDE = 90.0;
	public static final double MIN_LONGITUDE = -180.0;
	public static final double MAX_LONGITUDE = 180.0;
	public static final double NORTH_POLE_CROP_HEIGHT_DEGREES = 5.0;
	public static final double SOUTH_POLE_CROP_HEIGHT_DEGREES = 20.0;

	private GeometryCoordinateSequenceTransformer coordinateTransformer = 
		new GeometryCoordinateSequenceTransformer();
	
	private Set<Geometry> cropGeometries = new HashSet<Geometry>();

	public ProjectingGeometryPreparer(CoordinateReferenceSystem originalCRS,
			ProjectedCRS projectedCRS) throws FactoryException {
		coordinateTransformer.setMathTransform(CRS.findMathTransform(
				originalCRS, projectedCRS, true));
		//TODO: Add small explanation
		Projection projection = projectedCRS.getConversionFromBase();
		ParameterValue<?> centralMeridianParameter = projection
				.getParameterValues().parameter(CENTRAL_MERIDIAN_PARAMETER_KEY);
		double centralMeridian;
		if ( centralMeridianParameter == null ) {
			throw new RuntimeException("No central meridian specified."); // TODO
		}
		else {
			centralMeridian = centralMeridianParameter.doubleValue();
		}

		//TODO: Think about how we want to handle this (what if it's not a world map?)
		double oppositeMeridian = getOppositeLongitude(centralMeridian);
		cropGeometries.add(createMeridianCropGeometry(oppositeMeridian));
		cropGeometries.add(getNorthPoleCropGeometry());
		cropGeometries.add(getSouthPoleCropGeometry());
	}

	//TODO: Ask Russell if there's a better name for this?
	public Geometry prepareGeometry(Geometry originalGeometry) {
		Geometry croppedGeometry = cropGeometry(originalGeometry, cropGeometries);

		Geometry geometry;
		try {
			geometry = coordinateTransformer.transform(croppedGeometry);
		} catch (TransformException e) {
			throw new RuntimeException(e); // TODO
		}

		return geometry;
	}

	private Geometry cropGeometry(Geometry originalGeometry,
			Set<Geometry> cropGeometries) {
		Geometry croppedGeometry = originalGeometry;

		for (Geometry cropGeometry : cropGeometries) {
			croppedGeometry = croppedGeometry.difference(cropGeometry);
		}

		return croppedGeometry;
	}

	private double getOppositeLongitude(double centralMeridian) {
		if (centralMeridian >= 0) {
			return centralMeridian - MAX_LONGITUDE;
		} else {
			return centralMeridian + MAX_LONGITUDE;
		}
	}

	private Geometry createMeridianCropGeometry(double meridian) {
		GeometryFactory geometryFactory = JTSFactoryFinder
				.getGeometryFactory(null);

		Coordinate northPole = new Coordinate(meridian, MAX_LATITUDE);
		Coordinate equatorEpsilonEast = new Coordinate(meridian
				+ MERIDIAN_CROP_WIDTH_AT_EQUATOR, EQUATOR_LATITUDE);
		Coordinate equatorEpsilonWest = new Coordinate(meridian
				- MERIDIAN_CROP_WIDTH_AT_EQUATOR, EQUATOR_LATITUDE);
		Coordinate southPole = new Coordinate(meridian, MIN_LATITUDE);

		LineString easternCrop = geometryFactory
				.createLineString(new Coordinate[] { northPole,
						equatorEpsilonEast, southPole });
		LineString westernCrop = geometryFactory
				.createLineString(new Coordinate[] { northPole,
						equatorEpsilonWest, southPole });

		Geometry meridianCropGeometry = easternCrop.union(westernCrop);
		
//		Coordinate northPoleW = new Coordinate(meridian - MERIDIAN_CROP_WIDTH_AT_EQUATOR, MAX_LATITUDE);
//		Coordinate northPoleE = new Coordinate(meridian + MERIDIAN_CROP_WIDTH_AT_EQUATOR, MAX_LATITUDE);
//		Coordinate southPoleE = new Coordinate(meridian + MERIDIAN_CROP_WIDTH_AT_EQUATOR, MIN_LATITUDE);
//		Coordinate southPoleW = new Coordinate(meridian - MERIDIAN_CROP_WIDTH_AT_EQUATOR, MIN_LATITUDE);
//		
//		LineString cropLineString = geometryFactory.createLineString(new Coordinate[] { northPoleW, northPoleE, southPoleE, southPoleW, northPoleW });
		
		//TODO: Explain a bit
		meridianCropGeometry = meridianCropGeometry.convexHull(); //cropLineString.convexHull();

		return meridianCropGeometry;
	}

	private Geometry getSouthPoleCropGeometry() {
		return getPoleCropGeometry(MIN_LATITUDE, SOUTH_POLE_CROP_HEIGHT_DEGREES);
	}

	private Geometry getNorthPoleCropGeometry() {
		return getPoleCropGeometry(MAX_LATITUDE, -NORTH_POLE_CROP_HEIGHT_DEGREES);
	}

	private Geometry getPoleCropGeometry(double latitude,
			double latitudeCropSize) {
		GeometryFactory geometryFactory = JTSFactoryFinder
				.getGeometryFactory(null);
		LinearRing southPoleCropBorder = geometryFactory
				.createLinearRing(new Coordinate[] {
						new Coordinate(MIN_LONGITUDE, latitude),
						new Coordinate(MIN_LONGITUDE, latitude
								+ latitudeCropSize),
						new Coordinate(MAX_LONGITUDE, latitude
								+ latitudeCropSize),
						new Coordinate(MAX_LONGITUDE, latitude),
						new Coordinate(MIN_LONGITUDE, latitude) });
		return southPoleCropBorder.convexHull();
	}
}
