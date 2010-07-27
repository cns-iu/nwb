package edu.iu.scipolicy.visualization.horizontalbargraph.bar;

import java.awt.Color;

/**
 * The legend that represents categories of the colors. It contains
 * the title (colorized column name), (x,y) position in the graph and
 * a list of color labels.
 * @author kongch
 *
 */
public class ColorLegendLabel {
	private String label;
	private double labelX;
	private double labelY;
	private double boxX;
	private double boxY;
	private double width;
	private double height;
	private Color color;
	
	public ColorLegendLabel(String label,
							double labelX,
							double labelY,
							double boxX,
							double boxY,
							double width,
							double height,
							Color color){
		this.label = label;
		this.labelX = labelX;
		this.labelY = labelY;
		this.boxX = boxX;
		this.boxY = boxY;
		this.width = width;
		this.height = height;
		this.color = color;
	}

	public String getLabel() {
		return label;
	}

	public double getLabelX() {
		return labelX;
	}

	public double getLabelY() {
		return labelY;
	}

	public double getBoxX() {
		return boxX;
	}

	public double getBoxY() {
		return boxY;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public Color getColor() {
		return color;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setLabelX(double labelX) {
		this.labelX = labelX;
	}

	public void setLabelY(double labelY) {
		this.labelY = labelY;
	}

	public void setBoxX(double boxX) {
		this.boxX = boxX;
	}

	public void setBoxY(double boxY) {
		this.boxY = boxY;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
