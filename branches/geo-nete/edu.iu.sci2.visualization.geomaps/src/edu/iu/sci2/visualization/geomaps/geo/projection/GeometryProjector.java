package edu.iu.sci2.visualization.geomaps.geo.projection;

import java.util.Set;

import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.Projection;
import org.opengis.referencing.operation.TransformException;

import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Geometry;

/*
 * Here, to "project" a Geometry is to both crop it and transform it.
 * Cropping is necessary (according to specifics of the projection)
 * for aesthetic and logical reasons.
 * 
 *   - When a projection cuts the Coordinate space (opposite the
 * central meridian or longitude of origin, for example), we must crop
 * around the cut so that when drawing paths of features in the shapefile
 * with Coordinates on either side of the cut, we do not draw lines away
 * either side of the cut to connect them.
 *     See createMeridianCropGeometry.
 * 
 *   - In a typical Mercator projection, Antarctica will take up a great deal of
 * map space, and so cropping around the south pole can have aesthetic value.
 * 
 *   - Some projections (such as Mercator) are not well-defined
 * (at least by GeoTools) in particular Coordinate (latitude & longitude)
 * spaces.  For example, the Mercator transform will throw up if it is
 * asked to process a point sufficiently close to a pole
 * (such as 90' S).  In this case it is also desirable to crop near the poles.
 * 
 * This class is designed only for projections with exactly one cut, that being
 * along the meridian opposite the central meridian (which is presumed specified).
 */

public class GeometryProjector {
	public static final String CENTRAL_MERIDIAN_PARAMETER_KEY = "central_meridian";
	
	public static final double MAX_LONGITUDE = 180.0;
	public static final double NORTH_POLE_CROP_HEIGHT_IN_DEGREES = 10.0;
	public static final double SOUTH_POLE_CROP_HEIGHT_IN_DEGREES = 12.0;

	private final GeometryCoordinateSequenceTransformer coordinateTransformer =
			new GeometryCoordinateSequenceTransformer();
	private final Set<Geometry> cropGeometries;

	
	public GeometryProjector(CoordinateReferenceSystem sourceCrs, KnownProjectedCRSDescriptor knownProjectedCRSDescriptor)
				throws GeometryProjectorException {

		try {
			coordinateTransformer.setMathTransform(
					knownProjectedCRSDescriptor.getTransformFrom(sourceCrs));
	
			/* Note that cropping around the opposite meridian is generally
			 * necessary even when the desired resulting map will have less than
			 * global extent.
			 * Because the source shapefile may generally contain features which
			 * span the opposite meridian, even a map showing only features away
			 * from the opposite meridian could have glitchy map-crossing borders
			 * passing through that area when said features are drawn.
			 */
			cropGeometries = Sets.newHashSet();
			cropGeometries.add(
					CropGeometryBuilder.createMeridianCropGeometry(
							getOppositeLongitude(
									centralMeridianOf(
											knownProjectedCRSDescriptor.asProjectedCRS()))));
			cropGeometries.add(
					CropGeometryBuilder.createNorthPoleGeometry(NORTH_POLE_CROP_HEIGHT_IN_DEGREES));
			cropGeometries.add(
					CropGeometryBuilder.createSouthPoleGeometry(SOUTH_POLE_CROP_HEIGHT_IN_DEGREES));
		} catch (FactoryException e) {
			throw new GeometryProjectorException(e);
		}
	}

	
	public Geometry projectGeometry(Geometry originalGeometry) throws TransformException {
		Geometry croppedGeometry = cropGeometry(originalGeometry, cropGeometries);
		Geometry projectedGeometry = transformGeometry(croppedGeometry);
		
		return projectedGeometry;
	}
	
	/* Typically one will want to use projectGeometry to both crop and transform.
	 * Some callers, like CirclePrinter, must be sure that their passed Geometry
	 * is not cropped out.
	 */
	public Geometry transformGeometry(Geometry originalGeometry) throws TransformException {
		return coordinateTransformer.transform(originalGeometry);
	}

	private static Geometry cropGeometry(Geometry originalGeometry,	Set<Geometry> cropGeometries) {
		Geometry croppedGeometry = originalGeometry;

		for (Geometry cropGeometry : cropGeometries) {
			croppedGeometry = croppedGeometry.difference(cropGeometry);
		}

		return croppedGeometry;
	}
	
	private static double getOppositeLongitude(double longitude) {
		if (longitude >= 0) {
			return longitude - MAX_LONGITUDE;
		} else {
			return longitude + MAX_LONGITUDE;
		}
	}

	private static double centralMeridianOf(ProjectedCRS projectedCRS) throws GeometryProjectorException {
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

		public GeometryProjectorException(Throwable cause) {
			super(cause);
		}
	}
}
