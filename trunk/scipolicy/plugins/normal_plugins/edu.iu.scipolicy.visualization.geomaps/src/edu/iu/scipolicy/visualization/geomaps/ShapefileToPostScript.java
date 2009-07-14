package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.osgi.service.log.LogService;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import edu.iu.scipolicy.visualization.geomaps.legend.AreaLegend;
import edu.iu.scipolicy.visualization.geomaps.legend.LabeledGradient;
import edu.iu.scipolicy.visualization.geomaps.legend.Legend;
import edu.iu.scipolicy.visualization.geomaps.printing.Circle;
import edu.iu.scipolicy.visualization.geomaps.printing.CirclePrinter;
import edu.iu.scipolicy.visualization.geomaps.printing.FeaturePrinter;
import edu.iu.scipolicy.visualization.geomaps.printing.MapBoundingBox;
import edu.iu.scipolicy.visualization.geomaps.projection.GeometryProjector;
import edu.iu.scipolicy.visualization.geomaps.utility.ShapefileFeatureReader;

import org.geotools.referencing.crs.DefaultGeographicCRS;

public class ShapefileToPostScript {
	public static final String INDENT = "  ";
	
	public static final String MERCATOR_EPSG_CODE = "EPSG:3395";
	public static final String ALBERS_EPSG_CODE = "EPSG:3083";
	public static final String LAMBERT_EPSG_CODE = "EPSG:2267";	
	
	private FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection;
	private GeometryProjector geometryProjector;
	private MapBoundingBox mapBoundingBox;
	private Legend legend = new Legend();
	private Map<String, Color> featureColorMap = new HashMap<String, Color>();
	private String featureNameKey;
	private Map<Coordinate, Circle> circleMap = new HashMap<Coordinate, Circle>();

	
	public ShapefileToPostScript(URL shapefileURL, ProjectedCRS projectedCRS, String featureNameKey) throws AlgorithmExecutionException {
		ShapefileFeatureReader shapefileFeatureReader = new ShapefileFeatureReader(shapefileURL);
		featureCollection = shapefileFeatureReader.getFeatureCollection();
		geometryProjector = makeGeometryPreparer(projectedCRS);
		mapBoundingBox = calculateMapBoundingBox();
		this.featureNameKey = featureNameKey;
	}
	
	public void setFeatureColorAnnotations(Map<String, Color> featureColorMap, LabeledGradient featureColorGradient) {
		this.featureColorMap = featureColorMap;
		legend.add(featureColorGradient);
	}
	
	public void setCircleAnnotations(Map<Coordinate, Circle> circleMap, AreaLegend circleAreaLegend, LabeledGradient circleColorGradient) {
		this.circleMap = circleMap;
		legend.add(circleAreaLegend);
		legend.add(circleColorGradient);
	}

	public void printPostScript(File psFile) throws IOException, AlgorithmExecutionException {
		BufferedWriter out = new BufferedWriter(new FileWriter(psFile));

		printHeader(out);

		FeaturePrinter featurePrinter = new FeaturePrinter(featureCollection, geometryProjector, mapBoundingBox);
		featurePrinter.printFeatures(out, featureColorMap, featureNameKey);

		CirclePrinter circlePrinter = new CirclePrinter(geometryProjector, mapBoundingBox);
		circlePrinter.printCircles(out, circleMap);

		out.write("% Restore the default clipping path" + "\n");
		out.write("grestore" + "\n");
		out.write("\n");

		out.write(legend.toPostScript());
		out.write("\n");

		out.write("showpage" + "\n");

		out.close();

		GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Done.");
	}

	public static String makeSetRGBColorCommand(Color color) {
		float[] rgbColorComponents = new float[3];
		color.getRGBColorComponents(rgbColorComponents);

		float r = rgbColorComponents[0];
		float g = rgbColorComponents[1];
		float b = rgbColorComponents[2];
		return r + " " + g + " " + b + " setrgbcolor" + "\n";
	}

	private GeometryProjector makeGeometryPreparer(ProjectedCRS projectedCRS) throws AlgorithmExecutionException {
		GeometryProjector geometryProjector = null;

		SimpleFeatureType featureSchema = featureCollection.getSchema();
		CoordinateReferenceSystem originalCRS = featureSchema.getCoordinateReferenceSystem();
		if ( originalCRS == null ) {
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, "Shapefile has no associated coordinate reference system.  Assuming WGS84.");
			originalCRS = DefaultGeographicCRS.WGS84;
		}
		
		try {
			geometryProjector = new GeometryProjector(originalCRS, projectedCRS);
		} catch (FactoryException e) {
			throw new AlgorithmExecutionException(e);
		}

		return geometryProjector;
	}

	private MapBoundingBox calculateMapBoundingBox() throws AlgorithmExecutionException {
		/* Identify extreme values for the X and Y dimensions
		 * among the Geometries in our featureCollection.
		 * Note that this is *after* Geometry preparation (cropping and projecting).
		 */
		double dataMinX = Double.POSITIVE_INFINITY;
		double dataMinY = Double.POSITIVE_INFINITY;
		double dataMaxX = Double.NEGATIVE_INFINITY;
		double dataMaxY = Double.NEGATIVE_INFINITY;
		FeatureIterator<SimpleFeature> it = featureCollection.features();
		while (it.hasNext()) {
			SimpleFeature feature = it.next();
			Geometry rawGeometry = (Geometry) feature.getDefaultGeometry();
			Geometry geometry = geometryProjector.projectGeometry(rawGeometry);

			/* TODO: How deeply can geometries be nested (if infinitely, we need
			 * to handle it)
			 */
			for (int gg = 0; gg < geometry.getNumGeometries(); gg++) {
				Geometry subgeometry = geometry.getGeometryN(gg);

				Coordinate[] coordinates = subgeometry.getCoordinates();

				for (Coordinate coordinate : coordinates) {
					if (coordinate.x < dataMinX) {
						dataMinX = coordinate.x;
					}
					if (coordinate.y < dataMinY) {
						dataMinY = coordinate.y;
					}
					if (coordinate.x > dataMaxX) {
						dataMaxX = coordinate.x;
					}
					if (coordinate.y > dataMaxY) {
						dataMaxY = coordinate.y;
					}
				}
			}
		}
		if (it != null) {
			// YOU MUST CLOSE THE ITERATOR!
			it.close();
		}

		return new MapBoundingBox(dataMinX, dataMinY, dataMaxX, dataMaxY);
	}

	private void printHeader(BufferedWriter out) throws IOException {
		GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Printing PostScript.." + "\n");

		out.write("%!PS-Adobe-3.0 EPSF-3.0\n");
		out.write(createBoundingBoxComment());
		out.write("%%Pages: 1" + "\n");
		out.write("\n");

		out.write("% Save the default clipping path so we can clip the map safely" + "\n");
		out.write("gsave" + "\n");

		out.write(mapBoundingBox.toPostScript());

		out.write("\n");
	}
	
	/* Heuristic only.
	 * This calculation will likely be incorrect if the map and legend are
	 * repositioned.
	 * Should work when the legend is below the map.
	 */
	private String createBoundingBoxComment() throws IOException {
		double lowerLeftX = Math.min(mapBoundingBox.getLowerLeftX(), legend.getLowerLeftX());
		double lowerLeftY = Math.min(mapBoundingBox.getLowerLeftY(), legend.getLowerLeftY());
		double upperRightX = mapBoundingBox.getUpperRightX();
		double upperRightY = mapBoundingBox.getUpperRightY();		
		
		return "%%BoundingBox: " + lowerLeftX + " " + lowerLeftY + " " + upperRightX + " " + upperRightY + "\n";
	}
}