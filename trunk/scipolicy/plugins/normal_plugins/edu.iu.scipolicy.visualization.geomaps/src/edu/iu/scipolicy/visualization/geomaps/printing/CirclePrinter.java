package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.geotools.geometry.jts.JTSFactoryFinder;

import edu.iu.scipolicy.visualization.geomaps.ShapefileToPostScript;
import edu.iu.scipolicy.visualization.geomaps.projection.GeometryProjector;
import edu.iu.scipolicy.visualization.geomaps.scaling.DoubleScaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.LinearScaler;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class CirclePrinter {
	public static final String INDENT = "  ";
	public static final double CIRCLE_LINE_WIDTH = 1.2;	
	public static final String CIRCLE_DEF = "/circle {" + "\n"
											+ INDENT + "newpath" + "\n"
											+ INDENT + INDENT + "0 360 arc" + "\n"
											+ INDENT + "closepath" + "\n"
											+ "} def" + "\n";
	
	private GeometryProjector geometryProjector;
	private MapBoundingBox mapBoundingBox;
	public static final DoubleScaler DEFAULT_CIRCLE_COLOR_QUANTITY_SCALER = new LinearScaler();
	public static final DoubleScaler DEFAULT_CIRCLE_AREA_SCALER = new LinearScaler();
	public static final Color DEFAULT_CIRCLE_COLOR_MAXIMUM = Color.MAGENTA;
	public static final double DEFAULT_CIRCLE_AREA_MAXIMUM = 750.0;
	public static final double DEFAULT_CIRCLE_AREA_MINIMUM = 80.0;
	public static final Color DEFAULT_CIRCLE_COLOR_MINIMUM = Color.YELLOW;

	public CirclePrinter(GeometryProjector geometryProjector,
			MapBoundingBox mapBoundingBox) {
		this.geometryProjector = geometryProjector;
		this.mapBoundingBox = mapBoundingBox;
	}

	public void printCircles(BufferedWriter out, Map<Coordinate, Circle> circleMap) throws IOException, AlgorithmExecutionException {
		out.write("% Circle annotations" + "\n");
		out.write("gsave" + "\n");
		out.write("\n");

		out.write(CIRCLE_DEF);
		out.write("\n");

		out.write(INDENT + CIRCLE_LINE_WIDTH + " setlinewidth" + "\n");
		out.write("\n");

		for ( Entry<Coordinate, Circle> circleMapEntry : circleMap.entrySet() ) {
			Coordinate circleCoordinate = circleMapEntry.getKey();
			Circle circle = circleMapEntry.getValue();

			printCircle(out, circleCoordinate, circle);
		}

		out.write("grestore" + "\n");
		out.write("\n");
	}

	private void printCircle(BufferedWriter out, Coordinate circleCoordinate, Circle circle) throws IOException, AlgorithmExecutionException {
		/* If in the future we would like to project the circles around these points,
		 * rather than just projecting the central point and drawing a perfect circle,
		 * I believe that this could be done by, rather than:
		 * (1) Create a Point
		 * (2) Project it
		 * (3) Draw a circle around it
		 * 
		 * instead doing
		 * 
		 * (1) Create an appropriate Geometry (perhaps a Polygon closely approximating
		 * 		a ring, which could be formed by creating two linear rings approximating
		 * 		circles and then calling geometryFactory.createPolygon(larger, smaller).
		 * (2) Then crop, project, and draw it just as we would a Feature in the shapefile.
		 */

		double radius = circle.calculateRadiusFromArea();
		Color color = circle.getColor();

		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		Point rawPoint = geometryFactory.createPoint(circleCoordinate);
		/* Note we transform here, not project, because our "projection" involves
		 * cropping and in cropping we might subtract out rawPoint.
		 * Then we wouldn't be able to draw this Circle.
		 */
		Geometry point = geometryProjector.transformGeometry(rawPoint);
		Coordinate coordinate = mapBoundingBox.getDisplayCoordinate(point.getCoordinate());

		out.write(INDENT + coordinate.x + " " + coordinate.y + " " + radius + " circle" + "\n");
		out.write(INDENT + "gsave" + "\n");
		out.write(INDENT + INDENT + ShapefileToPostScript.makeSetRGBColorCommand(color));
		out.write(INDENT + INDENT + "stroke" + "\n");
		out.write(INDENT + "grestore" + "\n");
		out.write("\n");
	}
}
