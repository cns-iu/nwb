package edu.iu.scipolicy.visualization.geomaps.projection;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;


public class CropGeometryBuilder {	
	public static final double MERIDIAN_CROP_WIDTH_AT_EQUATOR = .01;
	public static final double MIN_LATITUDE = -90.0;
	public static final double EQUATOR_LATITUDE = 0.0;
	public static final double MAX_LATITUDE = 90.0;
	public static final double MIN_LONGITUDE = -180.0;
	public static final double MAX_LONGITUDE = 180.0;
	
	
	/* Draw a rhombus (in latitude & longitude space)
	 * centered on meridian from the north pole to the south pole
	 * with the specified constant width at the equator.
	 * 
	 * An approximation to the space surrounding the given meridian.
	 */
	public static Geometry createMeridianGeometry(double meridian) {
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

		Coordinate northPole = new Coordinate(meridian, MAX_LATITUDE);
		Coordinate equatorEpsilonEast = new Coordinate(meridian + MERIDIAN_CROP_WIDTH_AT_EQUATOR, EQUATOR_LATITUDE);
		Coordinate equatorEpsilonWest = new Coordinate(meridian	- MERIDIAN_CROP_WIDTH_AT_EQUATOR, EQUATOR_LATITUDE);
		Coordinate southPole = new Coordinate(meridian, MIN_LATITUDE);

		LineString easternCrop = geometryFactory.createLineString(new Coordinate[] { northPole,	equatorEpsilonEast, southPole });
		LineString westernCrop = geometryFactory.createLineString(new Coordinate[] { northPole, equatorEpsilonWest, southPole });

		Geometry meridianCropGeometry = easternCrop.union(westernCrop);

		meridianCropGeometry = meridianCropGeometry.convexHull();

		return meridianCropGeometry;
	}

	/*
	 * A spherical section around the north pole.
	 */
	public static Geometry createNorthPoleGeometry(double northPoleCropHeightDegrees) {
		return createLongitudinalGeometry(MAX_LATITUDE, -northPoleCropHeightDegrees);
	}

	/*
	 * A spherical section around the south pole.
	 */
	public static Geometry createSouthPoleGeometry(double southPoleCropHeightDegrees) {
		return createLongitudinalGeometry(MIN_LATITUDE, southPoleCropHeightDegrees);
	}

	/*
	 * Create a Geometry spanning all longitudes
	 * between latitude and (latitude+latitudeCropSize).
	 * latitudeCropSize may be meaningfully negative.
	 * 
	 * In latitude & longitude space, this is a rectangle bound by the
	 * Coordinates (latitude, 180 deg W) in the lower left
	 * and (latitude+latitudeCropSize, 180 deg E) in the upper right.
	 * 
	 * On the globe, it is the spherical section determined by two parallel
	 * planes at latitude and latitude+latitudeCropSize.
	 */
	public static Geometry createLongitudinalGeometry(double latitude, double latitudeCropSize) {
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		LinearRing border = geometryFactory.createLinearRing(new Coordinate[] {
				new Coordinate(MIN_LONGITUDE, latitude),
				new Coordinate(MIN_LONGITUDE, latitude + latitudeCropSize),
				new Coordinate(MAX_LONGITUDE, latitude + latitudeCropSize),
				new Coordinate(MAX_LONGITUDE, latitude),
				new Coordinate(MIN_LONGITUDE, latitude) });
		return border.convexHull();
	}
}
