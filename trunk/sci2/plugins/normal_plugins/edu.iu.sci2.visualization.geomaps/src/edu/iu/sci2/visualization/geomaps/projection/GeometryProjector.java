package edu.iu.sci2.visualization.geomaps.projection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Projection;
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import com.vividsolutions.jts.geom.Geometry;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.projection.custom.CustomTransformFactory;
import edu.iu.sci2.visualization.geomaps.projection.custom.CustomTransformFactory.TransformNotFoundException;
import edu.iu.sci2.visualization.geomaps.utility.Constants;

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
	public static final String WELL_KNOWN_TEXTS_PROPERTIES_FILE_PATH =
		"/edu/iu/sci2/visualization/geomaps/projection/wellKnownTexts.properties";
	
	public static final double MAX_LONGITUDE = 180.0;
	public static final double NORTH_POLE_CROP_HEIGHT_IN_DEGREES = 10.0;
	public static final double SOUTH_POLE_CROP_HEIGHT_IN_DEGREES = 12.0;

	/* "if the math transform should be created even when there is no
	 * information available for a datum shift."
	 * Suppresses errors due to unspecified Bursa-Wolf parameters when
	 * transforming between distinct datums.  Practically, this should
	 * not result in errors large enough to significantly distort drawing.
	 */
	public static final boolean REQUEST_LENIENT_TRANSFORM = true;
	
	
	private GeometryCoordinateSequenceTransformer coordinateTransformer =
		new GeometryCoordinateSequenceTransformer();

	private Set<Geometry> cropGeometries;

	
	public GeometryProjector(
			CoordinateReferenceSystem originalCRS, String projectionName)
				throws AlgorithmExecutionException {
		MathTransform transform = null;
		double centralMeridian = Double.NaN;		
		if (Constants.WKT_PROJECTIONS.contains(projectionName)) {
			if (originalCRS == null) {
				GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING,
					"Shapefile has no associated coordinate reference system.  "
					+ "Assuming the default (WGS84, a common reference system).");
				originalCRS = DefaultGeographicCRS.WGS84;
			}
			
			ProjectedCRS projectedCRS = getProjectedCRS(projectionName);
			centralMeridian = findCentralMeridian(projectedCRS);
			
			try {
				transform = CRS.findMathTransform(
						originalCRS, projectedCRS, REQUEST_LENIENT_TRANSFORM);
			} catch (FactoryException e) {
				throw new AlgorithmExecutionException(e.getMessage(), e);
			}
		} else {
			centralMeridian = CustomTransformFactory.DEFAULT_CENTRAL_MERIDIAN;
			try {
				transform =
					CustomTransformFactory.getTransform(projectionName);
			} catch (TransformNotFoundException e) {
				throw new AlgorithmExecutionException(e.getMessage(), e);
			}
		}
				
		coordinateTransformer.setMathTransform(transform);

		/* Note that cropping around the opposite meridian is generally
		 * necessary even when the desired resulting map will have less than
		 * global extent.
		 * Because the source shapefile may generally contain features which
		 * span the opposite meridian, even a map showing only features away
		 * from the opposite meridian could have glitchy map-crossing borders
		 * passing through that area when said features are drawn.
		 */	
		double oppositeMeridian = getOppositeLongitude(centralMeridian);
		cropGeometries = new HashSet<Geometry>();
		cropGeometries.add(
				CropGeometryBuilder.createMeridianGeometry(oppositeMeridian));
		cropGeometries.add(
				CropGeometryBuilder.createNorthPoleGeometry(
						NORTH_POLE_CROP_HEIGHT_IN_DEGREES));
		cropGeometries.add(
				CropGeometryBuilder.createSouthPoleGeometry(
						SOUTH_POLE_CROP_HEIGHT_IN_DEGREES));
	}

	
	public Geometry projectGeometry(Geometry originalGeometry)
			throws TransformException {
		Geometry croppedGeometry =
			cropGeometry(originalGeometry, cropGeometries);
		Geometry projectedGeometry = transformGeometry(croppedGeometry);
		
		return projectedGeometry;
	}	
	private static Geometry cropGeometry(Geometry originalGeometry,
			Set<Geometry> cropGeometries) {
		Geometry croppedGeometry = originalGeometry;

		for (Geometry cropGeometry : cropGeometries) {
			croppedGeometry = croppedGeometry.difference(cropGeometry);
		}

		return croppedGeometry;
	}
	/* Typically one will want to use projectGeometry to both crop and transform.
	 * Some callers, like CirclePrinter, must be sure that their passed Geometry
	 * is not cropped out.
	 */
	public Geometry transformGeometry(Geometry originalGeometry)
			throws TransformException {
		return coordinateTransformer.transform(originalGeometry);
	}	
	
	private static double getOppositeLongitude(double centralMeridian) {
		if (centralMeridian >= 0) {
			return centralMeridian - MAX_LONGITUDE;
		} else {
			return centralMeridian + MAX_LONGITUDE;
		}
	}
	
	private ProjectedCRS getProjectedCRS(String projectionName) throws AlgorithmExecutionException {
		final InputStream wellKnownTextInputStream =
			getClass().getResourceAsStream(WELL_KNOWN_TEXTS_PROPERTIES_FILE_PATH);

		final Properties wellKnownTexts = new Properties();
		try {
			wellKnownTexts.load(wellKnownTextInputStream);
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException(
				"Error finding the file that describes available map projections: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
				"Error accessing the file that describes available map projections: " + e.getMessage(), e);
		}

		String projectionWKTKey = Constants.PROJECTIONS.get(projectionName);
		try {
			return (ProjectedCRS) CRS.parseWKT(wellKnownTexts.getProperty(projectionWKTKey));

			/*
			 * You will need to attach the EPSG database (and maybe even its
			 * extensions) if you wish to specify the projection using an EPSG
			 * code. You may know a better way, but I would do this by creating
			 * a small GeoTools standalone project (see
			 * http://docs.codehaus.org/display/GEOTDOC/03+First+Project ) and
			 * specifying the epsg and epsg-extension dependencies in the
			 * pom.xml. Let maven acquire the necessary jars (as on the page
			 * given), then create a new geolibs plugin from those.
			 * 
			 * projectedCRS = (ProjectedCRS) CRS.decode(MERCATOR_EPSG_CODE);
			 */
		} catch (FactoryException e) {
			throw new AlgorithmExecutionException(
					"Failed to parse well-known text: " + e.getMessage(), e);
		}
	}
	
	private static double findCentralMeridian(ProjectedCRS projectedCRS)
			throws AlgorithmExecutionException {
		Projection projection = projectedCRS.getConversionFromBase();
		ParameterValueGroup parameterValues = projection.getParameterValues();
		ParameterValue<?> centralMeridianParameter =
			parameterValues.parameter(CENTRAL_MERIDIAN_PARAMETER_KEY);
		
		double centralMeridian;
		if ( centralMeridianParameter == null ) {
			throw new AlgorithmExecutionException(
					"No central meridian specified.");
		}
		else {
			centralMeridian = centralMeridianParameter.doubleValue();
		}
		
		return centralMeridian;
	}
}
