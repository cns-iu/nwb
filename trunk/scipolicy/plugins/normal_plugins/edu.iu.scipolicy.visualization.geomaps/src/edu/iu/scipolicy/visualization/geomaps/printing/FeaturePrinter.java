package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.ArrayListUtilities;
import org.cishell.utilities.StringUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import edu.iu.scipolicy.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.scipolicy.visualization.geomaps.printing.colorstrategy.ColorStrategy;
import edu.iu.scipolicy.visualization.geomaps.printing.colorstrategy.NullColorStrategy;
import edu.iu.scipolicy.visualization.geomaps.projection.GeometryProjector;

public class FeaturePrinter {
	public static final Color DEFAULT_FEATURE_COLOR = Color.WHITE;
	public static final double BORDER_BRIGHTNESS = 0.7;
	public static final double BORDER_LINE_WIDTH = 0.8;
	public static final String INDENT = "  ";
	
	private FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection;
	private GeometryProjector geometryProjector;
	private MapDisplayer mapDisplayer;
	private String shapefileFeatureNameKey;
	
	private Map<String, Boolean> featureWasColoredMap;	
	
	public FeaturePrinter(
			FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection,
			GeometryProjector geometryProjector,
			MapDisplayer mapDisplayer,
			String shapefileFeatureNameKey) {
		this.featureCollection = featureCollection;
		this.geometryProjector = geometryProjector;
		this.mapDisplayer = mapDisplayer;
		this.shapefileFeatureNameKey = shapefileFeatureNameKey;
	}
	
	
	public void printFeatures(
			BufferedWriter out,
			Map<String, ColorStrategy> featureColorMap)
				throws IOException, AlgorithmExecutionException, TransformException {
		/* Record that all features have not been colored.
		 * This Map is updated in printFeature and read in reportUnfoundFeatures.
		 */
		this.featureWasColoredMap = new HashMap<String, Boolean>(); 
		for (String featureName : featureColorMap.keySet()) {
			featureWasColoredMap.put(featureName, false);
		}
		
		// Write PostScript to draw and color the features
		out.write("% Features" + "\n");
		out.write("gsave" + "\n");
		out.write(INDENT + BORDER_LINE_WIDTH + " setlinewidth" + "\n");
		out.write(INDENT + BORDER_BRIGHTNESS + " setgray" + "\n");
		out.write("\n");

		FeatureIterator<SimpleFeature> iterator = featureCollection.features();
		while (iterator.hasNext()) {
			SimpleFeature feature = iterator.next();

			printFeature(out, feature, featureColorMap);
		}
		// Quoth GeoTools: "YOU MUST CLOSE THE ITERATOR!"
		iterator.close();

		out.write("grestore" + "\n");

		out.write("\n");
		
		reportUnfoundFeatures();
	}

	/* Find all feature names in featureColorMap that were never actually
	 * used to color a feature.  These represent rows (regions) in the table
	 * that had valid names and data, but that were not found in the
	 * shapefile during printFeatures.
	 * This is all to warn the user in the case that some rows aren't
	 * represented in the output, so that they can tweak their table and
	 * correct the region names (maybe the row is name="Canana" and now they
	 * can see that they wanted name="Canada" and correct it).
	 * It's important to do this because the accidental omission could be
	 * subtle (for example, the region is tiny and they don't notice that
	 * that one's color is missing).
	 */
	private void reportUnfoundFeatures() {
		final List<String> unfoundFeatureNames = new ArrayList<String>();
		
		for (Entry<String, Boolean> entry : featureWasColoredMap.entrySet()) {
			final String featureName = entry.getKey();
			final boolean featureWasColored = entry.getValue();
			
			if (!featureWasColored) {
				unfoundFeatureNames.add(featureName);
			}
		}
		
		if (unfoundFeatureNames.size() > 0) {
			final int prefixSize = 3;
			final int suffixSize = 1;
			final int previewSize = prefixSize + suffixSize;
			
			final String unfoundFeatureNamesPreview =
				ArrayListUtilities.makePreview(
					unfoundFeatureNames, prefixSize, suffixSize, ", ", "...");
			
			String warning = "Warning: "
				+ unfoundFeatureNames.size()
				+ " regions had "
				+ "data associated in the table but could not be found in "
				+ "the shapefile (using region name key \""
				+ shapefileFeatureNameKey
				+ "\") and so could not be colored: "
				+ unfoundFeatureNamesPreview
				+ ".";
			
			if (unfoundFeatureNames.size() > previewSize) {
				warning += "  The full list is in the log file."; 
			}
			
			GeoMapsAlgorithm.logger.log(
				LogService.LOG_WARNING,
				warning,
				new Exception(
					"These regions could not be found in the shapefile (using "
					+ "region name key \""
					+ shapefileFeatureNameKey
					+ "\"): "
					+ StringUtilities.implodeList(unfoundFeatureNames, ", ")
					+ "."));					
		}
	}

	private void printFeature(
			BufferedWriter out,
			SimpleFeature feature,
			Map<String, ColorStrategy> featureColorMap)
				throws IOException, TransformException, AlgorithmExecutionException {
		Geometry rawGeometry = (Geometry) feature.getDefaultGeometry();
		Geometry geometry = geometryProjector.projectGeometry(rawGeometry);

		String name = getFeatureName(feature);

		out.write(INDENT + "% Feature, " + shapefileFeatureNameKey + ": " + name + "\n");

		for (int gg = 0; gg < geometry.getNumGeometries(); gg++) {
			Geometry subgeometry = geometry.getGeometryN(gg);
			printGeometry(subgeometry, out, featureColorMap, name);
		}

		out.write("\n");
	}

	private void printGeometry(
			Geometry subgeometry,
			BufferedWriter out,
			Map<String, ColorStrategy> featureColorMap,
			String name)
				throws IOException {
		Coordinate[] coordinates = subgeometry.getCoordinates();
		if (coordinates.length > 0) {
			Coordinate firstCoordinate =
				mapDisplayer.getDisplayCoordinate(coordinates[0]);

			out.write(INDENT + "newpath" + "\n");
			out.write(INDENT + INDENT + (firstCoordinate.x) + " " + (firstCoordinate.y) + " moveto\n");

			for (int cc = 1; cc < coordinates.length; cc++) {
				Coordinate coordinate =
					mapDisplayer.getDisplayCoordinate(coordinates[cc]);

				out.write(INDENT + INDENT + (coordinate.x) + " " + (coordinate.y) + " lineto\n");
			}

			out.write(INDENT + "closepath" + "\n");

			// Fill the interior
			ColorStrategy colorStrategy = new NullColorStrategy();
			if (featureColorMap.containsKey(name)) {
				featureWasColoredMap.put(name, true);
				colorStrategy = featureColorMap.get(name);
			}
			out.write(colorStrategy.toPostScript());
			
			// Stroke the border
			out.write("stroke" + "\n");
		}		
	}

	private String getFeatureName(SimpleFeature feature)
				throws AlgorithmExecutionException {
		Property nameProperty = feature.getProperty(shapefileFeatureNameKey);		
		String name;
		if (nameProperty != null) {
			name = (String) nameProperty.getValue();
		} else {
			String message =
				"Feature " + feature + " has no " + shapefileFeatureNameKey
				+ " property.  Consider using one of the following: " + "\n";
			for (AttributeDescriptor ad : featureCollection.getSchema().getAttributeDescriptors()) {
				message += ("  " + ad.getName() + "\n");
			}
			throw new AlgorithmExecutionException(message);
		}
		
		return name;
	}
}
