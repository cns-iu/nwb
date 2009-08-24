package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;

import com.vividsolutions.jts.geom.Coordinate;

public class Circle {
	private Coordinate coordinate;
	private double area;
	private Color innerColor;
	private Color outerColor;
	

	public Circle(Coordinate coordinate, double area, Color innerColor, Color outerColor) {
		this.coordinate = coordinate;
		this.area = area;
		this.innerColor = innerColor;
		this.outerColor = outerColor;
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
	
	public Color getInnerColor() {
		return innerColor;
	}

	public Color getOuterColor() {
		return outerColor;
	}
}
