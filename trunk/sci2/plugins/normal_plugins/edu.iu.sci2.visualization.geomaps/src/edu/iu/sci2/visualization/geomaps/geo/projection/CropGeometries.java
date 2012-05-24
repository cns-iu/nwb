package edu.iu.sci2.visualization.geomaps.geo.projection;

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;


class CropGeometries {
	static final double MERIDIAN_CROP_WIDTH_AT_EQUATOR = .01;
	static final double MIN_LATITUDE = -90.0;
	static final double EQUATOR_LATITUDE = 0.0;
	static final double MAX_LATITUDE = 90.0;
	static final double MIN_LONGITUDE = -180.0;
	static final double MAX_LONGITUDE = 180.0;
	
	/**
	 * An approximation to the space surrounding the given meridian.
	 * 
	 * Draws a rhombus (in latitude & longitude space) centered on {@code meridian} from the north
	 * pole to the south pole with a small constant width at the equator.
	 */
	static Geometry aroundMeridian(double meridian) {
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);

		Coordinate northPole = new Coordinate(meridian, MAX_LATITUDE);
		Coordinate equatorEpsilonEast =
				new Coordinate(meridian + MERIDIAN_CROP_WIDTH_AT_EQUATOR, EQUATOR_LATITUDE);
		Coordinate equatorEpsilonWest =
				new Coordinate(meridian	- MERIDIAN_CROP_WIDTH_AT_EQUATOR, EQUATOR_LATITUDE);
		Coordinate southPole = new Coordinate(meridian, MIN_LATITUDE);

		LineString easternLine = geometryFactory.createLineString(
				new Coordinate[] { northPole, equatorEpsilonEast, southPole });
		LineString westernLine = geometryFactory.createLineString(
				new Coordinate[] { northPole, equatorEpsilonWest, southPole });

		return easternLine.union(westernLine).convexHull();
	}

	/**
	 * A spherical section around the north pole.
	 */
	static Geometry aroundNorthPole(double cropHeightInDegreesLatitude) {
		return betweenTwoLatitudes(MAX_LATITUDE, MAX_LATITUDE - cropHeightInDegreesLatitude);
	}

	/**
	 * A spherical section around the south pole.
	 */
	static Geometry aroundSouthPole(double cropHeightInDegreesLatitude) {
		return betweenTwoLatitudes(MIN_LATITUDE, MIN_LATITUDE + cropHeightInDegreesLatitude);
	}

	/**
	 * A geometry spanning all longitudes between latitude1 and latitude2. In latlong space this is
	 * a rectangle. On the globe it is the spherical section between two parallel planes (both at
	 * right angles to the axis).
	 */
	static Geometry betweenTwoLatitudes(double latitude1, double latitude2) {
		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		LinearRing border = geometryFactory.createLinearRing(new Coordinate[] {
				new Coordinate(MIN_LONGITUDE, latitude1),
				new Coordinate(MIN_LONGITUDE, latitude2),
				new Coordinate(MAX_LONGITUDE, latitude2),
				new Coordinate(MAX_LONGITUDE, latitude1),
				new Coordinate(MIN_LONGITUDE, latitude1) });
		
		return border.convexHull();
	}
}
