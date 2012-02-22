package edu.iu.sci2.visualization.geomaps.viz;

import java.awt.Color;
import java.util.EnumMap;

import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.viz.ps.MapDisplayer;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;
import edu.iu.sci2.visualization.geomaps.viz.strategy.CircleAreaStrategy;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;

public class Circle {
	private final Coordinate coordinate;
	private final EnumMap<CircleDimension, Strategy> strategies;
	public static final double DEFAULT_CIRCLE_RADIUS_MINIMUM = 0.00135 * Constants.MAP_PAGE_AREA_WIDTH_IN_POINTS;
	public static final double DEFAULT_CIRCLE_RADIUS_MAXIMUM = 0.03 * Constants.MAP_PAGE_AREA_WIDTH_IN_POINTS;
	public static final double DEFAULT_CIRCLE_AREA_MINIMUM = calculateAreaFromRadius(DEFAULT_CIRCLE_RADIUS_MINIMUM);
	public static final double DEFAULT_CIRCLE_AREA_MAXIMUM = calculateAreaFromRadius(DEFAULT_CIRCLE_RADIUS_MAXIMUM);
	public static final double DEFAULT_CIRCLE_AREA = 0.1 * DEFAULT_CIRCLE_AREA_MAXIMUM;
	public static final double DEFAULT_CIRCLE_LINE_WIDTH = 1.5;
	public static final double OUTLINE_ADDITIONAL_RADIUS = 0.1;
	public static final Color DEFAULT_OUTLINE_COLOR = Color.BLACK;

	public Circle(
			Coordinate coordinate,
			EnumMap<CircleDimension, Strategy> strategies) {
		this.coordinate = coordinate;
		this.strategies = strategies;
	}
	
	
	public String toPostScript(GeometryProjector geometryProjector, MapDisplayer mapDisplayer) throws TransformException {
		// TODO ugly cast
		double radius = calculateRadiusFromArea(((CircleAreaStrategy) strategyFor(CircleDimension.AREA)).area());
		Strategy innerColorStrategy = strategyFor(CircleDimension.INNER_COLOR);
		Strategy outerColorStrategy = strategyFor(CircleDimension.OUTER_COLOR);

		GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
		Point rawPoint = geometryFactory.createPoint(coordinate);
		/* Note we transform here, not project, because our "projection" involves
		 * cropping and in cropping we might subtract out rawPoint.
		 * Then we wouldn't be able to draw this Circle.
		 */
		Geometry point = geometryProjector.transformGeometry(rawPoint);
		Coordinate displayCoordinate = mapDisplayer.getDisplayCoordinate(point.getCoordinate());

		StringBuilder builder = new StringBuilder();
		
		double outlineRadius = radius + Circle.OUTLINE_ADDITIONAL_RADIUS;
		builder.append(PostScriptable.INDENT + displayCoordinate.x + " " + displayCoordinate.y + " " + outlineRadius + " circle" + "\n");
		builder.append(outerColorStrategy.toPostScript());
		
		// Create and paint the circle path
		builder.append(PostScriptable.INDENT + displayCoordinate.x + " " + displayCoordinate.y + " " + radius + " circle" + "\n");
		builder.append(innerColorStrategy.toPostScript());
		
		builder.append("\n");
		
		return builder.toString();
	}

	public Strategy strategyFor(CircleDimension dimension) {
		if (strategies.containsKey(dimension)) {
			return strategies.get(dimension);
		} else {
			return dimension.defaultStrategy();
		}
	}

	public static double calculateAreaFromRadius(double radius) {
		return (Math.PI * radius * radius);
	}
	
	public static double calculateRadiusFromArea(double area) {
		return Math.sqrt(area / Math.PI);
	}
}
