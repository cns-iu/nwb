package edu.iu.sci2.visualization.scimaps.tempvis;

import java.awt.Dimension;
import java.awt.Graphics2D;

public interface RenderableVisualization {
	public String title();
	public GraphicsState preRender(
			Graphics2D graphics,
			Dimension size);
	public void render(
			GraphicsState graphicsState,
			Dimension size);
	public Dimension getDimension();
}