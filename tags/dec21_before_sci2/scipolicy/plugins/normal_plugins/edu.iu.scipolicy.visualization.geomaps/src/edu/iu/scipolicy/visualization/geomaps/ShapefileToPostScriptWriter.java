package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.FileUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import edu.iu.scipolicy.visualization.geomaps.legend.Legend;
import edu.iu.scipolicy.visualization.geomaps.legend.LegendComponent;
import edu.iu.scipolicy.visualization.geomaps.printing.Circle;
import edu.iu.scipolicy.visualization.geomaps.printing.CirclePrinter;
import edu.iu.scipolicy.visualization.geomaps.printing.DSCProlog;
import edu.iu.scipolicy.visualization.geomaps.printing.FeaturePrinter;
import edu.iu.scipolicy.visualization.geomaps.printing.MapDisplayer;
import edu.iu.scipolicy.visualization.geomaps.printing.PageFooter;
import edu.iu.scipolicy.visualization.geomaps.printing.PageHeader;
import edu.iu.scipolicy.visualization.geomaps.printing.PageMetadata;
import edu.iu.scipolicy.visualization.geomaps.printing.colorstrategy.ColorStrategy;
import edu.iu.scipolicy.visualization.geomaps.projection.GeometryProjector;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;
import edu.iu.scipolicy.visualization.geomaps.utility.ShapefileFeatureReader;

public class ShapefileToPostScriptWriter {
	/* Percentage of the data range to add to each side of the map as a buffer.
	 * Between 0 and 1.
	 */
	public static final double MAP_BOUNDING_BOX_BUFFER_RATIO = 0.1;

	public static final String OUTPUT_FILE_EXTENSION = "ps";
	
	public static final String TITLE = "Geo Map";
	public static final String INDENT = "  ";
	
	
	private String subtitle = "";	
	private FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection;
	private GeometryProjector geometryProjector;
	private MapDisplayer mapDisplayer;
	private double pageHeightInPoints;
	private Legend legend = new Legend();
	private Map<String, ColorStrategy> featureColorMap =
		new HashMap<String, ColorStrategy>();
	private String shapefileFeatureNameKey;
	private List<Circle> circles = new ArrayList<Circle>();
	
	
	public ShapefileToPostScriptWriter(
			URL shapefileURL, String projectionName, String featureNameKey)
				throws AlgorithmExecutionException, TransformException {
		ShapefileFeatureReader shapefileFeatureReader =
			new ShapefileFeatureReader(shapefileURL);
		this.featureCollection = shapefileFeatureReader.getFeatureCollection();
		this.geometryProjector = makeGeometryProjecter(projectionName);
		this.mapDisplayer = calculateMapBoundingBox();
		
		this.pageHeightInPoints =
			Constants.calculatePageHeightInPoints(mapDisplayer.getMapHeightInPoints());
		
		
		this.shapefileFeatureNameKey = featureNameKey;
	}

	public void setFeatureColorAnnotations(
			String subtitle,
			Map<String, ColorStrategy> featureColorMap,
			LegendComponent featureColorGradient) {
		this.subtitle = subtitle;
		this.featureColorMap = featureColorMap;
		legend.add(featureColorGradient);
	}
	
	public void setCircleAnnotations(String subtitle, List<Circle> circles, LegendComponent circleAreaLegend, LegendComponent innerColorGradient, LegendComponent outerColorGradient) {
		this.subtitle = subtitle;
		this.circles = circles;
		
		legend.add(innerColorGradient);
		legend.add(outerColorGradient);
		legend.add(circleAreaLegend);
	}

	public File writePostScriptToFile(
			 String projectionName, String authorName, String dataLabel)
				throws IOException, AlgorithmExecutionException, TransformException {
		
		File psFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("geoMaps", OUTPUT_FILE_EXTENSION);
		
		BufferedWriter out = new BufferedWriter(new FileWriter(psFile));

		writeCodeHeader(out, psFile.getName());
		
		out.write(createPostScriptUtilityDefinitions());
		out.write("\n");
		
		out.write((new PageHeader(dataLabel, pageHeightInPoints)).toPostScript() + "\n");
		out.write((new PageFooter()).toPostScript() + "\n");		
		
		out.write("% Save the default clipping path so we can clip the map safely" + "\n");
		out.write("gsave" + "\n");
		out.write("\n");

		out.write(mapDisplayer.toPostScript());
		out.write("\n");
		
		FeaturePrinter featurePrinter =
			new FeaturePrinter(
					featureCollection,
					geometryProjector,
					mapDisplayer,
					shapefileFeatureNameKey);
		featurePrinter.printFeatures(out, featureColorMap);

		CirclePrinter circlePrinter = new CirclePrinter(geometryProjector, mapDisplayer);
		circlePrinter.printCircles(out, circles);

		out.write("% Restore the default clipping path" + "\n");
		out.write("grestore" + "\n");
		out.write("\n");
		
		PageMetadata pageMetadata = new PageMetadata(TITLE, subtitle);
		pageMetadata.add(projectionName + " Projection");		
		pageMetadata.add(timestamp());
		pageMetadata.add(authorName);
		out.write(pageMetadata.toPostScript());
		out.write("\n");
		
		out.write(legend.toPostScript());
		out.write("\n");
		
		out.write("showpage" + "\n");

		out.close();

		GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Done.");
		
		return psFile;
	}
	
	private static String timestamp() {
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf =
	    	new SimpleDateFormat("MMM dd, yyyy | hh:mm:ss aa");
	    return sdf.format(cal.getTime());
	}

	private String createPostScriptUtilityDefinitions() {
		StringTemplate definitionsTemplate =
			GeoMapsAlgorithm.group.getInstanceOf("utilityDefinitions");
		
		return definitionsTemplate.toString();
	}

	public static String makeSetRGBColorCommand(Color color) {
		float[] rgbColorComponents = new float[3];
		color.getRGBColorComponents(rgbColorComponents);

		float r = rgbColorComponents[0];
		float g = rgbColorComponents[1];
		float b = rgbColorComponents[2];
		return r + " " + g + " " + b + " setrgbcolor" + "\n";
	}

	private GeometryProjector makeGeometryProjecter(String projectionName)
			throws AlgorithmExecutionException {
		SimpleFeatureType featureSchema = featureCollection.getSchema();
		CoordinateReferenceSystem originalCRS =
			featureSchema.getCoordinateReferenceSystem();

		return new GeometryProjector(originalCRS, projectionName);
	}
	
	

	private MapDisplayer calculateMapBoundingBox() throws TransformException {
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
		// YOU MUST CLOSE THE ITERATOR!
		it.close();
		
		// Exaggerate the data range a bit to provide a buffer around it in the map.
		double xRange = dataMaxX - dataMinX;
		double xBufferSize = MAP_BOUNDING_BOX_BUFFER_RATIO * xRange;
		double bufferedDataMinX = dataMinX - xBufferSize;
		double bufferedDataMaxX = dataMaxX + xBufferSize;
		
		double yRange = dataMaxY - dataMinY;
		double yBufferSize = MAP_BOUNDING_BOX_BUFFER_RATIO * yRange;
		double bufferedDataMinY = dataMinY - yBufferSize;
		double bufferedDataMaxY = dataMaxY + yBufferSize;

		return new MapDisplayer(
				bufferedDataMinX, 
				bufferedDataMinY, 
				bufferedDataMaxX, 
				bufferedDataMaxY);
	}

	private void writeCodeHeader(
			BufferedWriter out, String outputPSFileName) throws IOException {
		GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Printing PostScript.." + "\n");

		out.write((new DSCProlog(outputPSFileName, pageHeightInPoints)).toPostScript());
		
//		/* TODO We're using setpagedevice to force page dimensions
//		 * corresponding to US Letter landscape.  This command
//		 * is forbidden in Encapsulated PostScript, so if we
//		 * wish to maintain that format, we'll need a different
//		 * method to specify landscape-style dimensions (that popular
//		 * PostScript-to-PDF renderers will all respect).
//		 */
//		out.write("/setpagedevice where" + "\n"
//				+ "{ pop 1 dict" + "\n"
//				+ "dup /PageSize [ "
//					+ Constants.PAGE_WIDTH_IN_POINTS + " "
//					+ pageHeightInPoints + " "
//				+ "] put" + "\n"
//				+ "setpagedevice" + "\n"
//				+ "} if" + "\n");
//		out.write("\n");
	}
}