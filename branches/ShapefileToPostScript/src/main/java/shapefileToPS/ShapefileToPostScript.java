package shapefileToPS;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.referencing.CRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

public class ShapefileToPostScript {
	public static final String DEFAULT_NAME_COLOR_MAP_KEY = "NAME";
	public static final double BORDER_BRIGHTNESS = 0.0;
	public static final double BORDER_LINE_WIDTH = 0.01;
	public static final String INDENT = "  ";

	public static final String GOOGLE_MERCATOR_WKT = "PROJCS[\"Google Mercator\",  GEOGCS[\"WGS 84\",    DATUM[\"World Geodetic System 1984\",      SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],      AUTHORITY[\"EPSG\",\"6326\"]],    PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],    UNIT[\"degree\", 0.017453292519943295],    AXIS[\"Geodetic latitude\", NORTH],    AXIS[\"Geodetic longitude\", EAST],    AUTHORITY[\"EPSG\",\"4326\"]],  PROJECTION[\"Mercator_1SP\"],  PARAMETER[\"semi_minor\", 6378137.0],  PARAMETER[\"latitude_of_origin\", 0.0],  PARAMETER[\"central_meridian\", 0.0],  PARAMETER[\"scale_factor\", 1.0],  PARAMETER[\"false_easting\", 0.0],  PARAMETER[\"false_northing\", 0.0],  UNIT[\"m\", 1.0],  AXIS[\"Easting\", EAST],  AXIS[\"Northing\", NORTH],  AUTHORITY[\"EPSG\",\"900913\"]]";

	public static final String MERCATOR_EPSG_CODE = "EPSG:3395";
	public static final String MERCATOR_WKT = "PROJCS[\"WGS 84 / World Mercator\","
			+ "    GEOGCS[\"WGS 84\","
			+ "        DATUM[\"WGS_1984\","
			+ "            SPHEROID[\"WGS 84\",6378137,298.257223563,"
			+ "                AUTHORITY[\"EPSG\",\"7030\"]],"
			+ "            AUTHORITY[\"EPSG\",\"6326\"]],"
			+ "        PRIMEM[\"Greenwich\",0,"
			+ "            AUTHORITY[\"EPSG\",\"8901\"]],"
			+ "        UNIT[\"degree\",0.01745329251994328,"
			+ "            AUTHORITY[\"EPSG\",\"9122\"]],"
			+ "        AUTHORITY[\"EPSG\",\"4326\"]],"
			+ "    UNIT[\"metre\",1,"
			+ "        AUTHORITY[\"EPSG\",\"9001\"]],"
			+ "    PROJECTION[\"Mercator_1SP\"],"
			+ "    PARAMETER[\"central_meridian\",0],"
			+ "    PARAMETER[\"scale_factor\",1],"
			+ "    PARAMETER[\"false_easting\",0],"
			+ "    PARAMETER[\"false_northing\",0],"
			+ "    AUTHORITY[\"EPSG\",\"3395\"],"
			+ "    AXIS[\"Easting\",EAST]," + "    AXIS[\"Northing\",NORTH]]";

	public static final String ALBERS_EPSG_CODE = "EPSG:3083";
	public static final String ALBERS_WKT = "PROJCS[\"NAD83 / Texas Centric Albers Equal Area\","
			+ "    GEOGCS[\"NAD83\","
			+ "        DATUM[\"North_American_Datum_1983\","
			+ "            SPHEROID[\"GRS 1980\",6378137,298.257222101,"
			+ "                AUTHORITY[\"EPSG\",\"7019\"]],"
			+ "            AUTHORITY[\"EPSG\",\"6269\"]],"
			+ "        PRIMEM[\"Greenwich\",0,"
			+ "            AUTHORITY[\"EPSG\",\"8901\"]],"
			+ "        UNIT[\"degree\",0.01745329251994328,"
			+ "            AUTHORITY[\"EPSG\",\"9122\"]],"
			+ "        AUTHORITY[\"EPSG\",\"4269\"]],"
			+ "    UNIT[\"metre\",1,"
			+ "        AUTHORITY[\"EPSG\",\"9001\"]],"
			+ "    PROJECTION[\"Albers_Conic_Equal_Area\"],"
			+ "    PARAMETER[\"standard_parallel_1\",27.5],"
			+ "    PARAMETER[\"standard_parallel_2\",35],"
			+ "    PARAMETER[\"latitude_of_center\",18],"
			+ "    PARAMETER[\"longitude_of_center\",-98],"
			+ "    PARAMETER[\"false_easting\",1500000],"
			+ "    PARAMETER[\"false_northing\",6000000],"
			+ "    AUTHORITY[\"EPSG\",\"3083\"],"
			+ "    AXIS[\"X\",EAST],"
			+ "    AXIS[\"Y\",NORTH]]";

	public static final String LAMBERT_EPSG_CODE = "EPSG:2267";
	public static final String LAMBERT_WKT = "PROJCS[\"NAD83 / Oklahoma North (ftUS)\","
			+ "    GEOGCS[\"NAD83\","
			+ "        DATUM[\"North_American_Datum_1983\","
			+ "            SPHEROID[\"GRS 1980\",6378137,298.257222101,"
			+ "                AUTHORITY[\"EPSG\",\"7019\"]],"
			+ "            AUTHORITY[\"EPSG\",\"6269\"]],"
			+ "        PRIMEM[\"Greenwich\",0,"
			+ "            AUTHORITY[\"EPSG\",\"8901\"]],"
			+ "        UNIT[\"degree\",0.01745329251994328,"
			+ "            AUTHORITY[\"EPSG\",\"9122\"]],"
			+ "        AUTHORITY[\"EPSG\",\"4269\"]],"
			+ "    UNIT[\"US survey foot\",0.3048006096012192,"
			+ "        AUTHORITY[\"EPSG\",\"9003\"]],"
			+ "    PROJECTION[\"Lambert_Conformal_Conic_2SP\"],"
			+ "    PARAMETER[\"standard_parallel_1\",36.76666666666667],"
			+ "    PARAMETER[\"standard_parallel_2\",35.56666666666667],"
			+ "    PARAMETER[\"latitude_of_origin\",35],"
			+ "    PARAMETER[\"central_meridian\",-98],"
			+ "    PARAMETER[\"false_easting\",1968500],"
			+ "    PARAMETER[\"false_northing\",0],"
			+ "    AUTHORITY[\"EPSG\",\"2267\"],"
			+ "    AXIS[\"X\",EAST],"
			+ "    AXIS[\"Y\",NORTH]]";

	public static void main(String[] args) throws FactoryException,
			TransformException, IOException {
		//TODO: A little confusing that there's no distinction between input and output paths
		String shapefilePath = "C:\\Documents and Settings\\jrbibers\\Desktop\\shapefiles\\countries.shp";
		String psFilePath = "C:\\Documents and Settings\\jrbibers\\Desktop\\test.ps";

		ShapefileToPostScript shapefileToPostScript = new ShapefileToPostScript();
		shapefileToPostScript.printPostScript(shapefilePath, psFilePath);
	}

	public void printPostScript(String shapefilePath, String psFilePath)
			throws FactoryException, IOException, TransformException {
		// TODO For example
		Map<String, Color> nameColorMap = new HashMap<String, Color>();
		nameColorMap.put("Kodiak Island", Color.RED);
		nameColorMap.put("Maui", Color.GREEN);
		nameColorMap.put("Los Angeles", Color.BLUE);
		nameColorMap.put("Hancock", Color.ORANGE);
		nameColorMap.put("United States", Color.BLUE);
		nameColorMap.put("Russia", Color.RED);

		ProjectedCRS projectedCRS = (ProjectedCRS) CRS.parseWKT(MERCATOR_WKT);
		// ProjectedCRS projectedCRS = (ProjectedCRS)
		// CRS.decode(MERCATOR_EPSG_CODE);

		printPostScript(shapefilePath, projectedCRS,
				DEFAULT_NAME_COLOR_MAP_KEY, nameColorMap, psFilePath);
	}

	public void printPostScript(String shapefilePath,
			ProjectedCRS projectedCRS, String nameColorMapKey,
			Map<String, Color> nameColorMap, String psFilePath)
			throws FactoryException, IOException, TransformException {
		File shapefile = new File(shapefilePath);
		FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = getFeatureSource(shapefile);
		FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource
				.getFeatures();

		SimpleFeatureType simpleFeatureType = featureSource.getSchema();
		CoordinateReferenceSystem originalCRS = simpleFeatureType
				.getCoordinateReferenceSystem();
		ProjectingGeometryPreparer projectingGeometryPreparer = new ProjectingGeometryPreparer(
				originalCRS, projectedCRS);

		//TODO: Add some kind of small comment here
		
		BufferedWriter out = new BufferedWriter(new FileWriter(psFilePath));

		printHeader(collection, projectingGeometryPreparer, out);

		printFeatures(collection, projectingGeometryPreparer, nameColorMap, out);

		out.write("showpage" + "\n");

		out.close();

		//TODO: Change all these to use the LogService (later on)
		System.out.println("Done.");
	}

	private void printHeader(
			FeatureCollection<SimpleFeatureType, SimpleFeature> collection,
			ProjectingGeometryPreparer projectingGeometryPreparer,
			BufferedWriter out) throws IOException {
		//TODO: What are we doing here?
		double dataMinX = Double.POSITIVE_INFINITY;
		double dataMinY = Double.POSITIVE_INFINITY;
		double dataMaxX = Double.NEGATIVE_INFINITY;
		double dataMaxY = Double.NEGATIVE_INFINITY;
		FeatureIterator<SimpleFeature> it = collection.features();
		while (it.hasNext()) {
			SimpleFeature feature = it.next();
			Geometry originalGeometry = (Geometry) feature.getDefaultGeometry();
			Geometry geometry = projectingGeometryPreparer
					.prepareGeometry(originalGeometry);

			//TODO: How deeply can geometries be nested (if infinitely, we need to handle it)
			for (int gg = 0; gg < geometry.getNumGeometries(); gg++) {
				Geometry subgeometry = geometry.getGeometryN(gg);

				Coordinate[] coordinates = subgeometry.getCoordinates();

				for (int cc = 0; cc < coordinates.length; cc++) {
					Coordinate coordinate = coordinates[cc];

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

		System.out.print("Printing PostScript.." + "\n");

		out.write("%!PS-Adobe-3.0 EPSF-3.0\n");

		PostScriptBoundingBox postScriptBoundingBox = new PostScriptBoundingBox(
				dataMinX, dataMinY, dataMaxX, dataMaxY);

		out.write("%%BoundingBox: " + postScriptBoundingBox + "\n");
		out.write("%%Pages: 1" + "\n");
		out.write("\n");

		out.write(postScriptBoundingBox.createDrawingCode());
		out.write("\n");

		out.write(postScriptBoundingBox.getDisplayTranslateString());
		out.write(postScriptBoundingBox.getScaleString());
		out.write(postScriptBoundingBox.getDataTranslateString());
		out.write("\n");
	}

	private void printFeatures(
			FeatureCollection<SimpleFeatureType, SimpleFeature> collection,
			ProjectingGeometryPreparer projectingGeometryPreparer,
			Map<String, Color> nameColorMap, BufferedWriter out)
			throws IOException {

		out.write(BORDER_LINE_WIDTH + " setlinewidth" + "\n");
		out.write("\n");

		FeatureIterator<SimpleFeature> iterator = collection.features();
		while (iterator.hasNext()) {
			SimpleFeature feature = iterator.next();

			printFeature(feature, projectingGeometryPreparer, out, nameColorMap);
		}
		if (iterator != null) {
			// YOU MUST CLOSE THE ITERATOR!
			iterator.close();
		}
	}

	private void printFeature(SimpleFeature feature,
			ProjectingGeometryPreparer projectingGeometryPreparer,
			BufferedWriter out, Map<String, Color> nameColorMap)
			throws IOException {
		//TODO: Add template comments
		Geometry originalGeometry = (Geometry) feature.getDefaultGeometry();
		Geometry geometry = projectingGeometryPreparer
				.prepareGeometry(originalGeometry);

		out.write("gsave\n");

		Property nameProperty = feature.getProperty("NAME");
		String name;
		if (nameProperty != null) {
			name = (String) nameProperty.getValue();
		} else {
			throw new RuntimeException("Feature " + feature
					+ " has no NAME property.");
		}

		Color color = Color.WHITE; //TODO: Abstract out as "DEFAULT_COLOR"
		if (nameColorMap.containsKey(name)) {
			color = nameColorMap.get(name);
		}
		// Color color = new Color(random.nextFloat(), random.nextFloat(),
		// random.nextFloat());

		float[] rgbColorComponents = new float[3];
		color.getRGBColorComponents(rgbColorComponents);

		float r = rgbColorComponents[0];
		float g = rgbColorComponents[1];
		float b = rgbColorComponents[2];
		out.write(INDENT + r + " " + g + " " + b + " setrgbcolor" + "\n");

		for (int gg = 0; gg < geometry.getNumGeometries(); gg++) {
			Geometry subgeometry = geometry.getGeometryN(gg);

			out.write(INDENT + "newpath" + "\n");

			Coordinate[] coordinates = subgeometry.getCoordinates();

			if (coordinates.length == 0) {
				continue;
			}

			Coordinate firstCoordinate = coordinates[0];

			out.write(INDENT + INDENT + (firstCoordinate.x) + " "
					+ (firstCoordinate.y) + " moveto\n");

			for (int cc = 1; cc < coordinates.length; cc++) {
				Coordinate coordinate = coordinates[cc];
				out.write(INDENT + INDENT + (coordinate.x) + " "
						+ (coordinate.y) + " lineto\n");
			}

			out.write(INDENT + "closepath" + "\n");

			out.write(INDENT + "gsave" + "\n");
			out.write(INDENT + INDENT + BORDER_BRIGHTNESS + " setgray" + "\n");
			out.write(INDENT + INDENT + "stroke" + "\n");
			out.write(INDENT + "grestore" + "\n");

			out.write(INDENT + "fill" + "\n");
		}

		out.write("grestore" + "\n");

		out.write("\n");
	}

	private FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(
			File shapefile) {
		Map<String, Serializable> connectParameters = new HashMap<String, Serializable>();
		//TODO: add two template comments (or maybe make them separate functions).
		try {
			connectParameters.put("url", shapefile.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e); // TODO
		}
		connectParameters.put("create spatial index", true);

		DataStore dataStore;
		try {
			dataStore = DataStoreFinder.getDataStore(connectParameters);
		} catch (IOException e) {
			throw new RuntimeException(e); // TODO
		}

		String[] typeNames;
		String typeName;
		FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
		try {
			typeNames = dataStore.getTypeNames();
			typeName = typeNames[0]; //TODO: Give readers a hint about what is going on with this
			System.out.println("Reading content: " + typeName);
			featureSource = dataStore.getFeatureSource(typeName);
		} catch (IOException e) {
			throw new RuntimeException(); // TODO
		}

		return featureSource;
	}
}