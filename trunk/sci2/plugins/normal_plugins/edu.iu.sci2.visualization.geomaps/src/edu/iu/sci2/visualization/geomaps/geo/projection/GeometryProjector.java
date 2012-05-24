package edu.iu.sci2.visualization.geomaps.geo.projection;

import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.Projection;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/* Here, to "project" a Geometry is to both crop it and transform it. Cropping is necessary
 * (according to specifics of the projection) for aesthetic and logical reasons.
 * 
 * - When a projection cuts the Coordinate space (opposite the central meridian or longitude of
 * origin, for example), we must crop around the cut so that when drawing paths of features in the
 * shapefile with Coordinates on either side of the cut, we do not draw lines away either side of
 * the cut to connect them. See createMeridianCropGeometry.
 * 
 * - In a typical Mercator projection, Antarctica will take up a great deal of map space, and so
 * cropping around the south pole can have aesthetic value.
 * 
 * - Some projections (such as Mercator) are not well-defined (at least by GeoTools) in particular
 * Coordinate (latitude & longitude) spaces. For example, the Mercator transform will throw up if it
 * is asked to process a point sufficiently close to a pole (such as 90' S). In this case it is also
 * desirable to crop near the poles.
 * 
 * This class is designed only for projections with exactly one cut, that being along the meridian
 * opposite the central meridian (which is presumed specified). */
public class GeometryProjector {
	private static final GeometryFactory GEOMETRY_FACTORY = JTSFactoryFinder.getGeometryFactory(null);
	public static final String CENTRAL_MERIDIAN_PARAMETER_KEY = "central_meridian";
	public static final double MAX_LONGITUDE = 180.0;
	public static final double NORTH_POLE_CROP_HEIGHT_IN_DEGREES = 10.0;
	public static final double SOUTH_POLE_CROP_HEIGHT_IN_DEGREES = 12.0;

	private final GeometryCoordinateSequenceTransformer transformer =
			new GeometryCoordinateSequenceTransformer();
	private final Geometry cropGeometry;

	
	public GeometryProjector(
			CoordinateReferenceSystem sourceCrs,
			KnownProjectedCRSDescriptor knownProjectedCRSDescriptor)
					throws GeometryProjectorException {
		try {
			transformer.setMathTransform(knownProjectedCRSDescriptor.getTransformFrom(sourceCrs));
	
			cropGeometry = createCropGeometry(knownProjectedCRSDescriptor);
		} catch (FactoryException e) {
			throw new GeometryProjectorException("Failed to create projection", e);
		}
	}


	/* Cropping around the opposite meridian is generally necessary even when the desired resulting
	 * map will have less than global extent. Because the source shapefile may generally contain
	 * features which span the opposite meridian, even a map showing only features away from the
	 * opposite meridian could have glitchy map-crossing borders passing through that area when
	 * said features are drawn. */
	private static Geometry createCropGeometry(
			KnownProjectedCRSDescriptor knownProjectedCRSDescriptor)
			throws GeometryProjectorException, NoSuchAuthorityCodeException, FactoryException {
		double oppositeOfCentralMeridian = getOppositeLongitude(centralMeridianOf(
						knownProjectedCRSDescriptor.asProjectedCRS()));
		
		return union(
				CropGeometries.aroundMeridian(oppositeOfCentralMeridian),
				CropGeometries.aroundNorthPole(NORTH_POLE_CROP_HEIGHT_IN_DEGREES),
				CropGeometries.aroundSouthPole(SOUTH_POLE_CROP_HEIGHT_IN_DEGREES));
	}
	
	private static Geometry union(Geometry first, Geometry... rest) {
		Geometry union = (Geometry) first.clone();
		
		for (Geometry g : rest) {
			union = union.union(g);
		}
		
		return union;
	}

	
	public Geometry cropAndTransformGeometry(Geometry originalGeometry) throws TransformException {
		return transformer.transform(originalGeometry.difference(cropGeometry));
	}
	
	/**
	 * The projection of this (longitude, latitude) coordinate.
	 */
	public Point transformCoordinate(Coordinate coordinate) throws TransformException {
		Point point = GEOMETRY_FACTORY.createPoint(coordinate);
		
		return transformer.transformPoint(point, GEOMETRY_FACTORY);
	}
	
	private static double getOppositeLongitude(double longitude) {
		return (longitude >= 0) ? (longitude - MAX_LONGITUDE) : (longitude + MAX_LONGITUDE);
	}

	private static double centralMeridianOf(ProjectedCRS projectedCRS)
			throws GeometryProjectorException {
		Projection projection = projectedCRS.getConversionFromBase();
		ParameterValueGroup parameterValues = projection.getParameterValues();
		ParameterValue<?> centralMeridianParameter =
				parameterValues.parameter(CENTRAL_MERIDIAN_PARAMETER_KEY);
		
		if (centralMeridianParameter == null) {
			throw new GeometryProjectorException("No central meridian specified.");
		}
		
		return centralMeridianParameter.doubleValue();
	}

	
	public static class GeometryProjectorException extends Exception {
		private static final long serialVersionUID = -2240465725004904181L;

		public GeometryProjectorException(String message, Throwable cause) {
			super(message, cause);
		}

		public GeometryProjectorException(String message) {
			super(message);
		}
	}
}
