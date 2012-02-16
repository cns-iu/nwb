package edu.iu.sci2.visualization.bipartitenet.component;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

import math.geom2d.Point2D;


public class RightAlignedLabel implements Paintable {

	private final Point2D rightBaseline;
	private final String text;
	private final Font font;

	public RightAlignedLabel(Point2D rightBaseline, String text, Font font) {
		this.rightBaseline = rightBaseline;
		this.text = text;
		this.font = font;
	}

	@Override
	public void paint(Graphics2D g) {
		TextLayout tl = new TextLayout(text, font, g.getFontRenderContext());
		Rectangle2D bounds = tl.getBounds();
		tl.draw(g, (float) (rightBaseline.getX() - bounds.getWidth()), (float) rightBaseline.getY());
	}

}
