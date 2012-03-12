package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Font;
import java.awt.Graphics2D;

import math.geom2d.Point2D;

public class TextWrappingLabel implements Paintable {
	
	private final String text;
	private final Font font;
	private final Point2D topLeft;

	public TextWrappingLabel(Point2D topLeft, String text, Font font) {
		this.topLeft = topLeft;
		this.text = text;
		this.font = font;
	}

	@Override
	public void paint(Graphics2D g) {
		// TODO Auto-generated method stub

	}

}
