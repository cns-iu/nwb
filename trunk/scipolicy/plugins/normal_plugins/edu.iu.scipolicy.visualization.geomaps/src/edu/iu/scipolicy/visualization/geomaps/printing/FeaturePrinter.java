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
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import edu.iu.scipolicy.visualization.geomaps.printing.colorstrategy.ColorStrategy;
import edu.iu.scipolicy.visualization.geomaps.printing.colorstrategy.NullColorStrategy;
import edu.iu.scipolicy.visualization.geomaps.projection.GeometryProjector;
import edu.iu.scipolicy.visualization.geomaps.scaling.LinearScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.Scaler;

public class FeaturePrinter {
	public static final Color DEFAULT_FEATURE_COLOR = Color.WHITE;	
	public static final double BORDER_BRIGHTNESS = 0.6;
	public static final double BORDER_LINE_WIDTH = 0.001;
	public static final String INDENT = "  ";
	
	private FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection;
	private GeometryProjector geometryProjector;
	private MapDisplayer mapDisplayer;
	public static final Scaler DEFAULT_FEATURE_COLOR_QUANTITY_SCALER = new LinearScaler();
	
	
	public FeaturePrinter(FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection, GeometryProjector geometryProjector, MapDisplayer mapDisplayer) {
		this.featureCollection = featureCollection;
		this.geometryProjector = geometryProjector;
		this.mapDisplayer = mapDisplayer;
	}
	
	
	public void printFeatures(
			BufferedWriter out,
			Map<String, ColorStrategy> featureColorMap,
			String featureColorMapKey)
				throws IOException, AlgorithmExecutionException, TransformException {
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

	private void printFeature(
			BufferedWriter out,
			SimpleFeature feature,
			Map<String, ColorStrategy> featureColorMap,
			String featureColorMapKey)
				throws IOException, TransformException, AlgorithmExecutionException {
		Geometry rawGeometry = (Geometry) feature.getDefaultGeometry();
		Geometry geometry = geometryProjector.projectGeometry(rawGeometry);

		String name = getFeatureName(feature, featureColorMapKey);

		out.write(INDENT + "% Feature, " + featureColorMapKey + ": " + name + "\n");

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
			Coordinate firstCoordinate = mapDisplayer.getDisplayCoordinate(coordinates[0]);

			out.write(INDENT + "newpath" + "\n");
			out.write(INDENT + INDENT + (firstCoordinate.x) + " " + (firstCoordinate.y) + " moveto\n");

			for (int cc = 1; cc < coordinates.length; cc++) {
				Coordinate coordinate = mapDisplayer.getDisplayCoordinate(coordinates[cc]);

				out.write(INDENT + INDENT + (coordinate.x) + " " + (coordinate.y) + " lineto\n");
			}

			out.write(INDENT + "closepath" + "\n");

			// Fill the interior
			ColorStrategy colorStrategy = new NullColorStrategy();
			if (featureColorMap.containsKey(name)) {
				colorStrategy = featureColorMap.get(name);
			}
			out.write(colorStrategy.toPostScript());
			
			// Stroke the border
			out.write("stroke" + "\n");
		}		
	}

	private String getFeatureName(
			SimpleFeature feature, String featureColorMapKey)
				throws AlgorithmExecutionException {
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
		
		return name;
	}
}
