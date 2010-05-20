package edu.iu.cns.visualization;

import java.awt.Dimension;

import org.freehep.graphics2d.VectorGraphics;

import edu.iu.cns.visualization.utility.GraphicsState;
import edu.iu.cns.visualization.utility.VisualizationMessages;

public interface RenderableVisualization {
	public String title();
	// TODO: renderHeader?  renderHeaderAndFooter?
	public GraphicsState preRender(
			VectorGraphics graphics,
			VisualizationMessages messages,
			Dimension size);
	public void renderBody(
			VectorGraphics graphics,
			GraphicsState graphicsState,
			VisualizationMessages messages,
			Dimension size);
}