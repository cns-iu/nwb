package edu.iu.sci2.visualization.geomaps.printing;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.printing.colorstrategy.ColorStrategy;
import edu.iu.sci2.visualization.geomaps.printing.colorstrategy.StrokeColorStrategy;
import edu.iu.sci2.visualization.geomaps.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.scaling.LinearScaler;
import edu.iu.sci2.visualization.geomaps.scaling.Scaler;
import edu.iu.sci2.visualization.geomaps.utility.Constants;

public class CirclePrinter {
	public static final double OUTLINE_ADDITIONAL_RADIUS = 0.1;
	public static final Color OUTLINE_COLOR = Color.BLACK;
	public static final String INDENT = "  ";
	public static final double DEFAULT_CIRCLE_LINE_WIDTH = 1.5;
	
	private GeometryProjector geometryProjector;
	private MapDisplayer mapDisplayer;
	public static final Scaler DEFAULT_CIRCLE_COLOR_QUANTITY_SCALER = new LinearScaler();
	public static final Scaler DEFAULT_CIRCLE_AREA_SCALER = new LinearScaler();
	
	public static final double DEFAULT_CIRCLE_RADIUS_MINIMUM =
		0.00135 * Constants.MAP_PAGE_AREA_WIDTH_IN_POINTS;
	public static final double DEFAULT_CIRCLE_AREA_MINIMUM =
		Circle.calculateAreaFromRadius(DEFAULT_CIRCLE_RADIUS_MINIMUM);
	
	public static final double DEFAULT_CIRCLE_RADIUS_MAXIMUM =
		0.03 * Constants.MAP_PAGE_AREA_WIDTH_IN_POINTS;
	public static final double DEFAULT_CIRCLE_AREA_MAXIMUM =
		Circle.calculateAreaFromRadius(DEFAULT_CIRCLE_RADIUS_MAXIMUM);
	
	private boolean hasPrintedDefinitions;

	public CirclePrinter(GeometryProjector geometryProjector,
			MapDisplayer mapDisplayer) {
		this.geometryProjector = geometryProjector;
		this.mapDisplayer = mapDisplayer;
		
		this.hasPrintedDefinitions = false;
	}

	public void printCircles(BufferedWriter out, List<Circle> circles)
			throws IOException, TransformException {
		/* Sort descending by area.  We want to draw starting with the biggest
		 * circles to try to avoid drawing over previously drawn circles.
		 */
		Collections.sort(
			circles,
			Collections.reverseOrder(
				new Comparator<Circle>() {
					public int compare(Circle circle1, Circle circle2) {
						return Double.compare(circle1.getArea(),
											  circle2.getArea());
					}
				}));
		
		out.write("% Circle annotations" + "\n");
		
		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapsAlgorithm.group.getInstanceOf("circlePrinterDefinitions");
			
			out.write(definitionsTemplate.toString());
			
			this.hasPrintedDefinitions = true;
		}

		out.write("gsave" + "\n");
		out.write("\n");

		// TODO See TODO below.
		double circleLineWidth = DEFAULT_CIRCLE_LINE_WIDTH;
		if (!circles.isEmpty() && circles.get(0).getOuterColorStrategy().getColor() == OUTLINE_COLOR) {
			circleLineWidth = 0.1;
		}
		out.write(INDENT + circleLineWidth + " setlinewidth" + "\n");
		out.write("\n");

		for (Circle circle : circles) {
			printCircle(out, circle);
		}

		out.write("grestore" + "\n");
		out.write("\n");
	}

	private void printCircle(BufferedWriter out, Circle circle)
			throws IOException, TransformException {
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
		ColorStrategy innerColorStrategy = circle.getInnerColorStrategy();
		ColorStrategy outerColorStrategy = circle.getOuterColorStrategy();

		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		Point rawPoint = geometryFactory.createPoint(coordinate);
		/* Note we transform here, not project, because our "projection" involves
		 * cropping and in cropping we might subtract out rawPoint.
		 * Then we wouldn't be able to draw this Circle.
		 */
		Geometry point = geometryProjector.transformGeometry(rawPoint);
		Coordinate displayCoordinate = mapDisplayer.getDisplayCoordinate(point.getCoordinate());

		/* TODO This is a hack to guess whether the user selected exterior circle coloring or not.
		 * In the case that they did, we use a thicker line width for the stroke.
		 * This is less than ideal; if the user chooses an exterior color range with pure black
		 * as one of the endpoints, then the min or max (accordingly) circle will have a
		 * thick line width.
		 */
		if (outerColorStrategy.getColor() != OUTLINE_COLOR) {
			// Create and paint the circle outline (by stroking a slightly larger circle).
			ColorStrategy outlineStrategy = new StrokeColorStrategy(OUTLINE_COLOR);
			double outlineRadius = radius + OUTLINE_ADDITIONAL_RADIUS;
			out.write(INDENT + displayCoordinate.x + " " + displayCoordinate.y + " " + outlineRadius + " circle" + "\n");
			out.write(outlineStrategy.toPostScript());
		}
		
		// Create and paint the circle path
		out.write(INDENT + displayCoordinate.x + " " + displayCoordinate.y + " " + radius + " circle" + "\n");
		out.write(innerColorStrategy.toPostScript());
		
		out.write(outerColorStrategy.toPostScript());	
		out.write("\n");
	}
}
