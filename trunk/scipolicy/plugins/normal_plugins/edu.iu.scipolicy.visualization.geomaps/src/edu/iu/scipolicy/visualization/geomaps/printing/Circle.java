package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;

import com.vividsolutions.jts.geom.Coordinate;

public class Circle {
	private double area;
	private Color color;
	private Coordinate coordinate;

	public Circle(Coordinate coordinate, double area, Color color) {
		this.coordinate = coordinate;
		this.area = area;
		this.color = color;
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

	public Color getColor() {
		return color;
	}

	
}
