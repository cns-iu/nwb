package edu.iu.cns.visualization.test;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import org.freehep.graphics2d.VectorGraphics;

import edu.iu.cns.visualization.RenderableVisualization;
import edu.iu.cns.visualization.parameter.VisualizationParameter;
import edu.iu.cns.visualization.utility.GraphicsState;
import edu.iu.cns.visualization.utility.VisualizationMessages;

public class TestVisualization implements RenderableVisualization {
	public String title() {
		return "Test Visualization";
	}

	public Map<String, VisualizationParameter<?>> parameters() {
		Map<String, VisualizationParameter<?>> parameters =
			new HashMap<String, VisualizationParameter<?>>();
		parameters.put("test", new VisualizationParameter<String>(
			"test", "Test", "This is the first test parameter", null));

		return parameters;
	}

	public void renderBody(
			VectorGraphics graphics,
			GraphicsState graphicsState,
			VisualizationMessages messages,
			Dimension size) {
	}
}