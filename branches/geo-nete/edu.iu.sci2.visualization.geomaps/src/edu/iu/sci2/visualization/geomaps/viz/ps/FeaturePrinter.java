package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.viz.FeatureDimension;
import edu.iu.sci2.visualization.geomaps.viz.FeatureView;
import edu.iu.sci2.visualization.geomaps.viz.strategy.NullColorStrategy;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public class FeaturePrinter {
	// TODO Comment.
	public static final double INTERRUPTION_CROSSING_GLITCH_DETECTION_THRESHOLD = 150;
	public static final double BORDER_BRIGHTNESS = 0.7;
	public static final double BORDER_LINE_WIDTH = 0.4;
	public static final String INDENT = "  ";
	
	private final FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection;
	private final GeometryProjector geometryProjector;
	private final GeoMapViewPageArea geoMapViewPageArea;
	private final String shapefileFeatureNameKey;
	
	private Map<String, Boolean> featureWasColoredMap;
	
	public FeaturePrinter(
			FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection,
			GeometryProjector geometryProjector,
			GeoMapViewPageArea geoMapViewPageArea,
			String shapefileFeatureNameKey) {
		this.featureCollection = featureCollection;
		this.geometryProjector = geometryProjector;
		this.geoMapViewPageArea = geoMapViewPageArea;
		this.shapefileFeatureNameKey = shapefileFeatureNameKey;
	}
	
	
	public void printFeatures(
			BufferedWriter out,
			Collection<FeatureView> featureViews)
				throws IOException, AlgorithmExecutionException, TransformException {
		/* Record that all features have not been colored.
		 * This Map is updated in printFeature and read in reportUnfoundFeatures.
		 */
		this.featureWasColoredMap = new HashMap<String, Boolean>();
		for (FeatureView featureView : featureViews) {
			String featureName = featureView.getFeatureName();
			featureWasColoredMap.put(featureName, false);
		}
		
		ImmutableMap<String, FeatureView> featureColorMap = Maps.uniqueIndex(
				featureViews,
				new Function<FeatureView, String>() {
					public String apply(FeatureView featureView) {
						return featureView.getFeatureName();
					}			
				});
		
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
			final int prefixSize = 6 ;
			final int suffixSize = 3;
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
					+ StringUtilities.implodeItems(unfoundFeatureNames, ", ")
					+ "."));
		}
	}

	private void printFeature(
			BufferedWriter out,
			SimpleFeature feature,
			ImmutableMap<String, FeatureView> featureColorMap)
				throws IOException, TransformException, AlgorithmExecutionException {
		Geometry rawGeometry = (Geometry) feature.getDefaultGeometry();
		Geometry geometry = geometryProjector.projectGeometry(rawGeometry);

		String name = extractNameFromFeature(feature);

		for (int gg = 0; gg < geometry.getNumGeometries(); gg++) {
			out.write(INDENT + "% Feature, " + shapefileFeatureNameKey + " = " + name + ", subgeometry " + gg + "\n");
			Geometry subgeometry = geometry.getGeometryN(gg);
			printGeometry(subgeometry, out, featureColorMap, name);
		}

		out.write("\n");
	}

	private void printGeometry(
			Geometry subgeometry,
			BufferedWriter out,
			Map<String, FeatureView> featureColorMap,
			String name)
				throws IOException {
		Coordinate[] coordinates = subgeometry.getCoordinates();
		if (coordinates.length > 0) {
			Coordinate firstCoordinate =
				geoMapViewPageArea.getDisplayCoordinate(coordinates[0]);

			out.write(INDENT + "newpath" + "\n");
			out.write(INDENT + INDENT + (firstCoordinate.x) + " " + (firstCoordinate.y) + " moveto\n");

			/* Many projections involve interruptions.
			 * Practically, this may mean that one coordinate of a geometry is on "one side"
			 * of the interruption and the next is on the "other".
			 * In GeometryProjecter, we account for interruptions at the meridian opposite that
			 * which will be central in the visualization.  That check does NOT account for
			 * non-opposite-meridianial interruptions, such as polar interruptions in a conic
			 * projection of the whole world.
			 * Without a special check, this code would naively draw an unwanted and ugly line
			 * between two points which could projected at opposite ends of the world.
			 * Therefore, we must check the distance between each coordinate that we process
			 * and the previous.
			 * If their distance is greater than INTERRUPTION_CROSSING_GLITCH_DETECTION_THRESHOLD,
			 * we assume that that line isn't really meant to be drawn, we stroke the path up
			 * to the last "good" coordinate, and start a new path.
			 */
			for (int cc = 1; cc < coordinates.length; cc++) {
				Coordinate coordinate =
					geoMapViewPageArea.getDisplayCoordinate(coordinates[cc]);
				Coordinate previousCoordinate =
					geoMapViewPageArea.getDisplayCoordinate(coordinates[cc - 1]);
				
				/* A closed path consisting of two or more points at the same location is a
				 * degenerate path. A degenerate path will be drawn only if you have set the line
				 * caps to round caps. If your line caps are not round caps, or if the path is not
				 * closed, the path will not be drawn. If the path is drawn, it will appear as a
				 * filled circle center at the point."
				 * 
				 * In an attempt to avoid some subtleties with degenerate paths,
				 * we skip any coordinate coincident to the previous.
				 */
				if (coordinate.equals2D(previousCoordinate)) {
					continue;
				}
				
				if (isAProbableInterruptionGlitch(coordinate, previousCoordinate)) {
					System.err.println(name + ":\n\t" + coordinate.x + ", " + coordinate.y);
					System.err.println("\tdistance = " + distance(coordinate, previousCoordinate));
					
					out.write("% Probable line glitch across polar cuts." + "\n");
					out.write("% Inserting a mid-Geometry stroke and " +
								"starting new path at next coordinate." + "\n");
					out.write(INDENT + "stroke" + "\n");
					out.write(INDENT + "newpath" + "\n");
					out.write(INDENT + INDENT + (coordinate.x) + " " + (coordinate.y) + " moveto\n");
				} else {
					out.write(INDENT + INDENT + (coordinate.x) + " " + (coordinate.y) + " lineto\n");
				}
			}

			out.write(INDENT + "closepath" + "\n");

			writeInkingCommands(out, featureColorMap, name);
		}
	}

	private static boolean isAProbableInterruptionGlitch(Coordinate coordinate,
			Coordinate previousCoordinate) {
		return (distance(coordinate, previousCoordinate) > INTERRUPTION_CROSSING_GLITCH_DETECTION_THRESHOLD);
	}

	private void writeInkingCommands(
			BufferedWriter out,	Map<String, FeatureView> featureColorMap, String rawName)
				throws IOException {
		// Fill the interior
		Strategy colorStrategy = new NullColorStrategy();
		
		String normalName = FeatureView.normalizeFeatureName(rawName);
		
		if (featureColorMap.containsKey(normalName)) {
			featureWasColoredMap.put(normalName, true);
			colorStrategy = featureColorMap.get(normalName).strategyFor(FeatureDimension.REGION_COLOR);
		}
		out.write(colorStrategy.toPostScript());
		
		// Stroke the border
		out.write("stroke" + "\n");
	}
	
	private static double distance(Coordinate coordinate1, Coordinate coordinate2) {
		return Math.hypot((coordinate1.x - coordinate2.x),
						  (coordinate1.y - coordinate2.y));
	}

	// TODO This should probably throw an unchecked exception
	private String extractNameFromFeature(SimpleFeature feature) throws AlgorithmExecutionException {
		Property nameProperty = feature.getProperty(shapefileFeatureNameKey);

		if (nameProperty == null) {
			String message =
					String.format(
							"Feature %s has no \"%s\" property.  " +
							"Consider using one of these properties: " +
							Joiner.on(",").join(Lists.transform(
									featureCollection.getSchema().getAttributeDescriptors(),
									new Function<AttributeDescriptor, String>() {
										public String apply(AttributeDescriptor input) {
											return input.getName().toString();
										}									
									}),
							"\n",
							feature,
							shapefileFeatureNameKey));
			
			throw new AlgorithmExecutionException(message);
		}

		return (String) nameProperty.getValue();
	}
}
