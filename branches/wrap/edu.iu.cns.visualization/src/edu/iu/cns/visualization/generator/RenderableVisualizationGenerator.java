package edu.iu.cns.visualization.generator;

import edu.iu.cns.visualization.RenderableVisualization;
import edu.iu.cns.visualization.gui.VisualizationGUIBuilder;

public interface RenderableVisualizationGenerator<
			A extends RenderableVisualization,
			C,
			P extends C,
			G extends VisualizationGUIBuilder<C, P>>
		extends VisualizationGenerator<A> {
	/* Alias */
}