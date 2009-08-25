package edu.iu.scipolicy.visualization.geomaps.printing;

import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.scipolicy.visualization.geomaps.printing.colorstrategy.ColorStrategy;

public class Circle {
	private Coordinate coordinate;
	private double area;
	private ColorStrategy innerColorStrategy;
	private ColorStrategy outerColorStrategy;
	

	public Circle(
			Coordinate coordinate,
			double area,
			ColorStrategy innerColorStrategy,
			ColorStrategy outerColorStrategy) {
		this.coordinate = coordinate;
		this.area = area;
		this.innerColorStrategy = innerColorStrategy;
		this.outerColorStrategy = outerColorStrategy;
	}

	
	public static double calculateAreaFromRadius(double radius) {
		return (Math.PI * radius * radius);
	}
	
	public static double calculateRadiusFromArea(double area) {
		return Math.sqrt(area / Math.PI);
	}

	public double calculateRadiusFromArea() {
		return Circle.calculateRadiusFromArea(area);
	}
	
	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	public double getArea() {
		return area;
	}
	
	public ColorStrategy getInnerColorStrategy() {
		return innerColorStrategy;
	}

	public ColorStrategy getOuterColorStrategy() {
		return outerColorStrategy;
	}
}
