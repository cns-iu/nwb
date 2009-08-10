package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.geotools.geometry.jts.JTSFactoryFinder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import edu.iu.scipolicy.visualization.geomaps.ShapefileToPostScriptWriter;
import edu.iu.scipolicy.visualization.geomaps.projection.GeometryProjector;
import edu.iu.scipolicy.visualization.geomaps.scaling.Scaler;
import edu.iu.scipolicy.visualization.geomaps.scaling.LinearScaler;

public class CirclePrinter {
	public static final String INDENT = "  ";
	public static final double CIRCLE_LINE_WIDTH = 1;	
	public static final String CIRCLE_DEF = "/circle {" + "\n"
											+ INDENT + "newpath" + "\n"
											+ INDENT + INDENT + "0 360 arc" + "\n"
											+ INDENT + "closepath" + "\n"
											+ "} def" + "\n";
	
	private GeometryProjector geometryProjector;
	private DisplayedMapBounds displayedMapBounds;
	public static final Scaler DEFAULT_CIRCLE_COLOR_QUANTITY_SCALER = new LinearScaler();
	public static final Scaler DEFAULT_CIRCLE_AREA_SCALER = new LinearScaler();
	public static final Color DEFAULT_CIRCLE_COLOR_MAXIMUM = Color.MAGENTA;
	public static final double DEFAULT_CIRCLE_AREA_MAXIMUM = 750.0;
	public static final double DEFAULT_CIRCLE_AREA_MINIMUM = 80.0;
	public static final Color DEFAULT_CIRCLE_COLOR_MINIMUM = Color.YELLOW;

	public CirclePrinter(GeometryProjector geometryProjector,
			DisplayedMapBounds displayedMapBounds) {
		this.geometryProjector = geometryProjector;
		this.displayedMapBounds = displayedMapBounds;
	}

	public void printCircles(BufferedWriter out, List<Circle> circles) throws IOException, AlgorithmExecutionException {
		out.write("% Circle annotations" + "\n");
		out.write("gsave" + "\n");
		out.write("\n");

		out.write(CIRCLE_DEF);
		out.write("\n");

		out.write(INDENT + CIRCLE_LINE_WIDTH + " setlinewidth" + "\n");
		out.write("\n");

		for ( Circle circle : circles ) {
			printCircle(out, circle);
		}

		out.write("grestore" + "\n");
		out.write("\n");
	}

	private void printCircle(BufferedWriter out, Circle circle) throws IOException, AlgorithmExecutionException {
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

		Coordinate coordinate = circle.getCoordinate();
		double radius = circle.calculateRadiusFromArea();
		Color color = circle.getColor();

		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		Point rawPoint = geometryFactory.createPoint(coordinate);
		/* Note we transform here, not project, because our "projection" involves
		 * cropping and in cropping we might subtract out rawPoint.
		 * Then we wouldn't be able to draw this Circle.
		 */
		Geometry point = geometryProjector.transformGeometry(rawPoint);
		Coordinate displayCoordinate = displayedMapBounds.getDisplayCoordinate(point.getCoordinate());

		out.write(INDENT + displayCoordinate.x + " " + displayCoordinate.y + " " + radius + " circle" + "\n");
		out.write(INDENT + "gsave" + "\n");
		out.write(INDENT + INDENT + ShapefileToPostScriptWriter.makeSetRGBColorCommand(color));
		out.write(INDENT + INDENT + "stroke" + "\n");
		out.write(INDENT + "grestore" + "\n");
		out.write("\n");
	}
}
