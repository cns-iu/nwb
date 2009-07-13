package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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

import edu.iu.scipolicy.visualization.geomaps.interpolation.ColorInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.DoubleInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.DoubleMapInterpolator;
import edu.iu.scipolicy.visualization.geomaps.interpolation.LinearInterpolator;
import edu.iu.scipolicy.visualization.geomaps.legend.AreaLegend;
import edu.iu.scipolicy.visualization.geomaps.legend.LabeledGradient;
import edu.iu.scipolicy.visualization.geomaps.legend.Legend;
import edu.iu.scipolicy.visualization.geomaps.printing.Circle;
import edu.iu.scipolicy.visualization.geomaps.printing.CirclePrinter;
import edu.iu.scipolicy.visualization.geomaps.printing.FeaturePrinter;
import edu.iu.scipolicy.visualization.geomaps.printing.PostScriptBoundingBox;
import edu.iu.scipolicy.visualization.geomaps.projection.GeometryProjector;
import edu.iu.scipolicy.visualization.geomaps.scaling.DoubleMapScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.DoubleScaler;
import edu.iu.scipolicy.visualization.geomaps.utility.Range;
import edu.iu.scipolicy.visualization.geomaps.utility.ShapefileFeatureReader;

public class ShapefileToPostScript {
	
	// PostScript is not Encapsulated if this is set to false!
	public static final boolean CLIP_TO_BOUNDING_BOX = true;
	public static final boolean DRAW_BOUNDING_BOX = true;
	public static final boolean BACKGROUND_TRANSPARENT = true;
	public static final Color BACKGROUND_COLOR = Color.CYAN;
	public static final String DEFAULT_FEATURE_COLOR_MAP_KEY = "NAME";
	public static final String INDENT = "  ";
	
	public static final String MERCATOR_EPSG_CODE = "EPSG:3395";
	public static final String ALBERS_EPSG_CODE = "EPSG:3083";
	public static final String LAMBERT_EPSG_CODE = "EPSG:2267";
	
	private FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection;
	private GeometryProjector geometryProjector;
	private PostScriptBoundingBox postScriptBoundingBox;

//	public static void main(String[] args) throws FactoryException, TransformException, IOException {
//		String shapefileInputPath = "C:\\Documents and Settings\\jrbibers\\Desktop\\shapefiles\\countries.shp";
//		String postscriptFileOutputPath = "C:\\Documents and Settings\\jrbibers\\Desktop\\test.ps";
//
//		ProjectedCRS projectedCRS = (ProjectedCRS) CRS.parseWKT(WKTLibrary.MERCATOR_WKT);
//		// ProjectedCRS projectedCRS = (ProjectedCRS) CRS.decode(MERCATOR_EPSG_CODE);
//
//		ShapefileToPostScript shapefileToPostScript = new ShapefileToPostScript(shapefileInputPath, projectedCRS);
//		shapefileToPostScript.printPostScript(postscriptFileOutputPath);
//	}

	public ShapefileToPostScript(URL shapefileURL, ProjectedCRS projectedCRS) throws AlgorithmExecutionException {
		ShapefileFeatureReader shapefileFeatureReader = new ShapefileFeatureReader(shapefileURL);
		featureCollection = shapefileFeatureReader.getFeatureCollection();
		geometryProjector = makeGeometryPreparer(projectedCRS);
		postScriptBoundingBox = calculatePostScriptBoundingBox();
	}

	/* TODO Name is no longer accurate.. not this class' responsibility.  Needs to move
	 */
	public void printPostScript(File psFile, Map<String, Double> featureColorQuantityMap, String featureColorQuantityAttribute, DoubleScaler featureColorQuantityScaler, Range<Color> featureColorRange, Map<Coordinate, Double> circleAreaMap, String circleAreaAttribute, DoubleScaler circleAreaScaler, Map<Coordinate, Double> circleColorQuantityMap, String circleColorQuantityAttribute, DoubleScaler circleColorQuantityScaler, Range<Color> circleColorRange)
	throws AlgorithmExecutionException, IOException {
		// Create and fill legend
		Legend legend = new Legend();

		Map<String, Color> interpolatedFeatureColorMap = new HashMap<String, Color>();
		if ( !featureColorQuantityMap.isEmpty() ) {
			// Scale and interpolate feature colors
			DoubleMapScaler<String> featureColorQuantityMapScaler = new DoubleMapScaler<String>(featureColorQuantityScaler);
			Map<String, Double> scaledFeatureColorQuantityMap = featureColorQuantityMapScaler.scale(featureColorQuantityMap);
			Range<Double> featureColorQuantityScalableRange = featureColorQuantityMapScaler.getScalableRange();
			DoubleInterpolator<Color> featureColorQuantityInterpolator = new ColorInterpolator(scaledFeatureColorQuantityMap.values(), featureColorRange);
			interpolatedFeatureColorMap = (new DoubleMapInterpolator<String, Color>(featureColorQuantityInterpolator)).getInterpolatedMap(scaledFeatureColorQuantityMap );
			
			// Add feature color legend
			double featureColorGradientLowerLeftX = .05*legend.getWidth();
			double featureColorGradientLowerLeftY = legend.getLowerLeftY();
			double featureColorGradientWidth = legend.getWidth()/3 * .90;
			double featureColorGradientHeight = 15;
			String featureColorTypeLabel = "Feature Color";
			legend.add(new LabeledGradient(featureColorQuantityScalableRange, featureColorRange, featureColorTypeLabel, featureColorQuantityAttribute, featureColorGradientLowerLeftX, featureColorGradientLowerLeftY, featureColorGradientWidth, featureColorGradientHeight));
		}

		Map<Coordinate, Circle> interpolatedCircleMap = new HashMap<Coordinate, Circle>();		
		if ( circleAreaMap.keySet().equals(circleColorQuantityMap.keySet()) ) {
			if ( !circleAreaMap.isEmpty() ) {
				// Scale and interpolate circle areas
				DoubleMapScaler<Coordinate> circleAreaDoubleMapScaler = new DoubleMapScaler<Coordinate>(circleAreaScaler);
				Map<Coordinate, Double> scaledCircleAreaMap = circleAreaDoubleMapScaler.scale(circleAreaMap);
				Range<Double> circleAreaScalableRange = circleAreaDoubleMapScaler.getScalableRange();
				Range<Double> interpolatedCircleAreaRange = new Range<Double>(CirclePrinter.DEFAULT_CIRCLE_AREA_MINIMUM, CirclePrinter.DEFAULT_CIRCLE_AREA_MAXIMUM);
				DoubleInterpolator<Double> circleAreaInterpolator = new LinearInterpolator(scaledCircleAreaMap.values(), interpolatedCircleAreaRange);
				Map<Coordinate, Double> interpolatedCircleAreaMap = (new DoubleMapInterpolator<Coordinate, Double>(circleAreaInterpolator)).getInterpolatedMap(scaledCircleAreaMap);
		
				// Add circle area legend
				double areaLegendLowerLeftX = .38*legend.getWidth();
				double areaLegendLowerLeftY = legend.getLowerLeftY();
				String areaTypeLabel = "Circle Size";
				legend.add(new AreaLegend(circleAreaScalableRange, interpolatedCircleAreaRange, areaTypeLabel, circleAreaAttribute, areaLegendLowerLeftX, areaLegendLowerLeftY));
				
				// Scale and interpolate circle colors
				DoubleMapScaler<Coordinate> circleColorQuantityMapScaler = new DoubleMapScaler<Coordinate>(circleColorQuantityScaler);
				Map<Coordinate, Double> scaledCircleColorQuantityMap = circleColorQuantityMapScaler.scale(circleColorQuantityMap);
				Range<Double> circleColorQuantityScalableRange = circleColorQuantityMapScaler.getScalableRange();
				DoubleInterpolator<Color> circleColorInterpolator = new ColorInterpolator(scaledCircleColorQuantityMap.values(), circleColorRange);
				Map<Coordinate, Color> interpolatedCircleColorMap = (new DoubleMapInterpolator<Coordinate, Color>(circleColorInterpolator)).getInterpolatedMap(scaledCircleColorQuantityMap);
	
				// Add circle color legend
				double circleColorGradientLowerLeftX = 2*legend.getWidth()/3;
				double circleColorGradientLowerLeftY = legend.getLowerLeftY();
				double circleColorGradientWidth = legend.getWidth()/3 * .90;
				double circleColorGradientHeight = 15;
				String circleColorTypeLabel = "Circle Color";
				legend.add(new LabeledGradient(circleColorQuantityScalableRange, circleColorRange, circleColorTypeLabel, circleColorQuantityAttribute, circleColorGradientLowerLeftX, circleColorGradientLowerLeftY, circleColorGradientWidth, circleColorGradientHeight));
				
				/* Construct the Circle map from the specified areas and colors.
				 * Note we require that every Coordinate with an area specified has a color specified
				 * and that every Coordinate with a color specified has an area specified.
				 */
				interpolatedCircleMap = new HashMap<Coordinate, Circle>();
				assert( interpolatedCircleAreaMap.keySet().equals(interpolatedCircleColorMap.keySet()) ); // TODO
				for ( Entry<Coordinate, Double> circleAreaMapEntry : interpolatedCircleAreaMap.entrySet() ) {
					Coordinate coordinate = circleAreaMapEntry.getKey();
					double area = circleAreaMapEntry.getValue();
					Color color = interpolatedCircleColorMap.get(coordinate);
	
					interpolatedCircleMap.put(coordinate, new Circle(area, color));
				}
			}
		}
		else {
			throw new AlgorithmExecutionException("Every circle annotation must have both a size and a color specified.");
		}
		
		printPostScript(DEFAULT_FEATURE_COLOR_MAP_KEY, interpolatedFeatureColorMap, interpolatedCircleMap, legend, psFile);
	}

	public void printPostScript(String featureColorMapKey,
			Map<String, Color> featureColorMap, Map<Coordinate, Circle> circleMap,
			Legend legend, File psFile) throws IOException, AlgorithmExecutionException {
		BufferedWriter out = new BufferedWriter(new FileWriter(psFile));

		printHeader(out);

		FeaturePrinter featurePrinter = new FeaturePrinter(featureCollection, geometryProjector, postScriptBoundingBox);
		featurePrinter.printFeatures(out, featureColorMap, featureColorMapKey);

		CirclePrinter circlePrinter = new CirclePrinter(geometryProjector, postScriptBoundingBox);
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
		try {
			geometryProjector = new GeometryProjector(originalCRS, projectedCRS);
		} catch (FactoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return geometryProjector;
	}

	private PostScriptBoundingBox calculatePostScriptBoundingBox() throws AlgorithmExecutionException {
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

			// TODO: How deeply can geometries be nested (if infinitely, we need
			// to handle it)
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

		return new PostScriptBoundingBox(dataMinX, dataMinY, dataMaxX, dataMaxY);
	}

	private void printHeader(BufferedWriter out) throws IOException {
		GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Printing PostScript.." + "\n");

//		out.write("%!PS-Adobe-3.0 EPSF-3.0\n");
//		out.write("%%BoundingBox: " + postScriptBoundingBox.getCoordinatesString() + "\n");
//		out.write("%%Pages: 1" + "\n");
//		out.write("\n");
		out.write("%!" + "\n");
		out.write("\n");
		

		out.write("% Save the default clipping path so we can clip the map safely" + "\n");
		out.write("gsave" + "\n");

		out.write(postScriptBoundingBox.toPostScript(DRAW_BOUNDING_BOX, BACKGROUND_TRANSPARENT, BACKGROUND_COLOR, CLIP_TO_BOUNDING_BOX));

		out.write("\n");
	}
}