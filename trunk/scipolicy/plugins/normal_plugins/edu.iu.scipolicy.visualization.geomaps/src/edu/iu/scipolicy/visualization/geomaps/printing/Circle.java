package edu.iu.scipolicy.visualization.geomaps.printing;

import java.awt.Color;

public class Circle {
	private double area;
	private Color color;

	public Circle(double area, Color color) {
		this.area = area;
		this.color = color;
	}

	public static double calculateRadiusFromArea(double area) {
		return Math.sqrt(area / Math.PI);
	}

	public double calculateRadiusFromArea() {
		return Circle.calculateRadiusFromArea(area);
	}

	public Color getColor() {
		return color;
	}

	public double getArea() {
		return area;
	}
}
