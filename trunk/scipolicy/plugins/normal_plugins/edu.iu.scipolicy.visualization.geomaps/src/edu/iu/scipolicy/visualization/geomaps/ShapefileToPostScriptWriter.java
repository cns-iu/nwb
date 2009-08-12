package edu.iu.scipolicy.visualization.geomaps;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
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
import edu.iu.scipolicy.visualization.geomaps.printing.PageTitle;
import edu.iu.scipolicy.visualization.geomaps.projection.GeometryProjector;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;
import edu.iu.scipolicy.visualization.geomaps.utility.ShapefileFeatureReader;

public class ShapefileToPostScriptWriter {
	public static final String INDENT = "  ";
	
	public static final String MERCATOR_EPSG_CODE = "EPSG:3395";
	public static final String ALBERS_EPSG_CODE = "EPSG:3083";
	public static final String LAMBERT_EPSG_CODE = "EPSG:2267";		
	
	public static final String PAGE_TITLE = "Geo Map";
	
	private String subtitle = "";
	
	private FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection;
	private GeometryProjector geometryProjector;
	private MapDisplayer mapDisplayer;
	private double pageHeightInPoints;
	private Legend legend = new Legend();
	private Map<String, Color> featureColorMap = new HashMap<String, Color>();
	private String featureNameKey;
	private List<Circle> circles = new ArrayList<Circle>();
	
	
	public ShapefileToPostScriptWriter(
			URL shapefileURL, ProjectedCRS projectedCRS, String featureNameKey)
				throws AlgorithmExecutionException {
		ShapefileFeatureReader shapefileFeatureReader =
			new ShapefileFeatureReader(shapefileURL);
		this.featureCollection = shapefileFeatureReader.getFeatureCollection();
		this.geometryProjector = makeGeometryPreparer(projectedCRS);
		this.mapDisplayer = calculateMapBoundingBox();
		
		this.pageHeightInPoints =
			Constants.calculatePageHeightInPoints(mapDisplayer.getMapHeightInPoints());
		
		
		this.featureNameKey = featureNameKey;
	}

	public void setFeatureColorAnnotations(String subtitle, Map<String, Color> featureColorMap, LegendComponent featureColorGradient) {
		this.subtitle = subtitle;
		this.featureColorMap = featureColorMap;
		legend.add(featureColorGradient);
	}
	
	public void setCircleAnnotations(String subtitle, List<Circle> circles, LegendComponent circleAreaLegend, LegendComponent colorGradient) {
		this.subtitle = subtitle;
		this.circles = circles;
		legend.add(circleAreaLegend);
		legend.add(colorGradient);
	}

	public void writePostScriptToFile(
			File psFile, String authorName, String dataLabel)
				throws IOException, AlgorithmExecutionException {
		BufferedWriter out = new BufferedWriter(new FileWriter(psFile));

		writeCodeHeader(out, psFile.getName());
		
		out.write(createPostScriptUtilityDefinitions());
		out.write("\n");
		
		out.write((new PageHeader(authorName, dataLabel, pageHeightInPoints)).toPostScript() + "\n");
		out.write((new PageFooter()).toPostScript() + "\n");		
		
		out.write("% Save the default clipping path so we can clip the map safely" + "\n");
		out.write("gsave" + "\n");
		out.write("\n");

		out.write(mapDisplayer.toPostScript());
		out.write("\n");
		
		FeaturePrinter featurePrinter =
			new FeaturePrinter(featureCollection, geometryProjector, mapDisplayer);
		featurePrinter.printFeatures(out, featureColorMap, featureNameKey);

		CirclePrinter circlePrinter = new CirclePrinter(geometryProjector, mapDisplayer);
		circlePrinter.printCircles(out, circles);

		out.write("% Restore the default clipping path" + "\n");
		out.write("grestore" + "\n");
		out.write("\n");

		out.write((new PageTitle(PAGE_TITLE, subtitle)).toPostScript());
		out.write("\n");
		
		out.write(legend.toPostScript());
		out.write("\n");
		
		out.write("showpage" + "\n");

		out.close();

		GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Done.");
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

	private GeometryProjector makeGeometryPreparer(ProjectedCRS projectedCRS)
			throws AlgorithmExecutionException {
		GeometryProjector geometryProjector = null;

		SimpleFeatureType featureSchema = featureCollection.getSchema();
		CoordinateReferenceSystem originalCRS =
			featureSchema.getCoordinateReferenceSystem();
		if ( originalCRS == null ) {
			GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING,
				"Shapefile has no associated coordinate reference system.  "
				+ "Assuming the default (WGS84, a very common reference system).");
			originalCRS = DefaultGeographicCRS.WGS84;
		}
		
		try {
			geometryProjector = new GeometryProjector(originalCRS, projectedCRS);
		} catch (FactoryException e) {
			throw new AlgorithmExecutionException(e);
		}

		return geometryProjector;
	}

	private MapDisplayer calculateMapBoundingBox() throws AlgorithmExecutionException {
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
		if (it != null) {
			// YOU MUST CLOSE THE ITERATOR!
			it.close();
		}

		return new MapDisplayer(dataMinX, dataMinY, dataMaxX, dataMaxY);
	}

	private void writeCodeHeader(
			BufferedWriter out, String outputPSFileName) throws IOException {
		GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Printing PostScript.." + "\n");

		out.write("%!PS-Adobe-3.0 EPSF-3.0" + "\n");
		out.write((new DSCProlog(outputPSFileName, pageHeightInPoints)).toPostScript());
		
		/* TODO
		 * We're using setpagedevice to force page dimensions
		 * corresponding to US Letter landscape.  This command
		 * is forbidden in Encapsulated PostScript, so if we
		 * wish to maintain that format, we'll need a different
		 * method to specify landscape-style dimensions.
		 */
		out.write("/setpagedevice where" + "\n"
				+ "{ pop 1 dict" + "\n"
				+ "dup /PageSize [ "
					+ Constants.PAGE_WIDTH_IN_POINTS + " "
					+ pageHeightInPoints + " "
				+ "] put" + "\n"
				+ "setpagedevice" + "\n"
				+ "} if" + "\n");
		out.write("\n");
	}
}