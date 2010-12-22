package edu.iu.sci2.visualization.horizontalbargraph.bar;

import java.awt.Color;

public class Bar {
	private String label;
	private Color color;
	private boolean continuesLeft;
	private boolean continuesRight;
	private double x;
	private double y;
	private double width;
	private double height;
	private double originalAmount;
	private boolean hasInfiniteAmount;

	public Bar(
			String label,
			Color color,
			boolean continuesLeft,
			boolean continuesRight,
			double x,
			double y,
			double width,
			double height,
			double originalAmount,
			boolean hasInfiniteAmount) {
		this.label = label;
		this.color = color;
		this.continuesLeft = continuesLeft;
		this.continuesRight = continuesRight;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.originalAmount = originalAmount;
		this.hasInfiniteAmount = hasInfiniteAmount;
	}

	public String getLabel() {
		return this.label;
	}
	
	public Color getColor() {
		return this.color;
	}

	public boolean continuesLeft() {
		return this.continuesLeft;
	}

	public boolean continuesRight() {
		return this.continuesRight;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getWidth() {
		return this.width;
	}

	public double getHeight() {
		return this.height;
	}

	public double getOriginalAmount() {
		return this.originalAmount;
	}

	public boolean hasInfiniteAmount() {
		return this.hasInfiniteAmount;
	}
}