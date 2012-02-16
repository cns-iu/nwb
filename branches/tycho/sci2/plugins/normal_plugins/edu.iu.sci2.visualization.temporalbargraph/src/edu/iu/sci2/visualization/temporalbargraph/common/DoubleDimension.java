package edu.iu.sci2.visualization.temporalbargraph.common;

import java.awt.geom.Dimension2D;

public class DoubleDimension extends Dimension2D {
	double height;
	double width;
	
	public DoubleDimension(double width, double height){
		this.setSize(width, height);
	}
	
	@Override
	public double getHeight() {
		return this.height;
	}

	@Override
	public double getWidth() {
		return this.width;
	}

	@Override
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {
		return "DoubleDimension [height=" + height + ", width=" + width + "]";
	}
	
}
