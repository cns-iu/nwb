package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import edu.iu.scipolicy.visualization.geomaps.ShapefileToPostScript;
import edu.iu.scipolicy.visualization.geomaps.projection.GeometryProjector;
import edu.iu.scipolicy.visualization.geomaps.scaling.DoubleScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.LinearScaler;

public class FeaturePrinter {
	public static final Color DEFAULT_FEATURE_COLOR = Color.WHITE;	
	public static final double BORDER_BRIGHTNESS = 0.0;
	public static final double BORDER_LINE_WIDTH = 0.001;
	public static final String INDENT = "  ";
	
	private FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection;
	private GeometryProjector geometryProjector;
	private MapBoundingBox mapBoundingBox;
	public static final DoubleScaler DEFAULT_FEATURE_COLOR_QUANTITY_SCALER = new LinearScaler();
	public static final Color DEFAULT_FEATURE_COLOR_MAXIMUM = Color.GREEN;
	public static final Color DEFAULT_FEATURE_COLOR_MINIMUM = Color.BLUE;
	
	public FeaturePrinter(FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection, GeometryProjector geometryProjector, MapBoundingBox mapBoundingBox) {
		this.featureCollection = featureCollection;
		this.geometryProjector = geometryProjector;
		this.mapBoundingBox = mapBoundingBox;
	}
	
	public void printFeatures(
			BufferedWriter out,
			Map<String, Color> featureColorMap,
			String featureColorMapKey)
	throws IOException, AlgorithmExecutionException {
		out.write("% Features" + "\n");
		out.write("gsave" + "\n");
		out.write(INDENT + BORDER_LINE_WIDTH + " setlinewidth" + "\n");
		out.write(INDENT + BORDER_BRIGHTNESS + " setgray" + "\n");
		out.write("\n");

		FeatureIterator<SimpleFeature> iterator = featureCollection.features();
		while (iterator.hasNext()) {
			SimpleFeature feature = iterator.next();

			printFeature(out, feature, featureColorMap, featureColorMapKey);
		}
		if (iterator != null) {
			// YOU MUST CLOSE THE ITERATOR!
			iterator.close();
		}

		out.write("grestore" + "\n");

		out.write("\n");
	}

	private void printFeature(BufferedWriter out,
			SimpleFeature feature,
			Map<String, Color> featureColorMap, String featureColorMapKey) throws IOException, AlgorithmExecutionException {
		// TODO: Add template comments
		Geometry rawGeometry = (Geometry) feature.getDefaultGeometry();
		Geometry geometry = geometryProjector.projectGeometry(rawGeometry);

		Property nameProperty = feature.getProperty(featureColorMapKey);
		String name;
		if (nameProperty != null) {
			name = (String) nameProperty.getValue();
		} else {
			String message = ("Feature " + feature + " has no " + featureColorMapKey + " property.  Consider using one of the following: " + "\n");
			for ( AttributeDescriptor ad : featureCollection.getSchema().getAttributeDescriptors() ) {
				message += ("  " + ad.getName() + "\n");
			}
			throw new AlgorithmExecutionException(message);
		}

		out.write(INDENT + "% Feature, " + featureColorMapKey + ": " + name + "\n");

		for (int gg = 0; gg < geometry.getNumGeometries(); gg++) {
			Geometry subgeometry = geometry.getGeometryN(gg);

			out.write(INDENT + "newpath" + "\n");

			Coordinate[] coordinates = subgeometry.getCoordinates();

			if (coordinates.length == 0) {
				continue;
			}
			Coordinate firstCoordinate = mapBoundingBox.getDisplayCoordinate(coordinates[0]);

			out.write(INDENT + INDENT + (firstCoordinate.x) + " "
					+ (firstCoordinate.y) + " moveto\n");

			for (int cc = 1; cc < coordinates.length; cc++) {
				Coordinate coordinate = mapBoundingBox.getDisplayCoordinate(coordinates[cc]);

				out.write(INDENT + INDENT + (coordinate.x) + " "
						+ (coordinate.y) + " lineto\n");
			}

			out.write(INDENT + "closepath" + "\n");

			Color color = DEFAULT_FEATURE_COLOR;
			if (featureColorMap.containsKey(name)) {
				color = featureColorMap.get(name);
			}

			out.write(INDENT + "gsave" + "\n");
			out.write(INDENT + INDENT + ShapefileToPostScript.makeSetRGBColorCommand(color));
			out.write(INDENT + INDENT + "fill" + "\n");
			out.write(INDENT + "grestore" + "\n");
			out.write(INDENT + "stroke" + "\n");
		}

		out.write("\n");
	}
}
