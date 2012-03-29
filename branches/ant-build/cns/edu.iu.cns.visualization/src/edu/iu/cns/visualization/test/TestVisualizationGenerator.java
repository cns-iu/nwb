package edu.iu.cns.visualization.test;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import edu.iu.cns.visualization.generator.VisualizationGenerator;
import edu.iu.cns.visualization.gui.awt.AWTVisualizationGUIBuilder;
import edu.iu.cns.visualization.gui.awt.AWTVisualizationRunner;
import edu.iu.cns.visualization.parameter.VisualizationParameter;

public class TestVisualizationGenerator implements VisualizationGenerator<TestVisualization> {
	private Map<String, VisualizationParameter<?>> parameters =
		new HashMap<String, VisualizationParameter<?>>();

	public Map<String, VisualizationParameter<?>> getParameters() {
		return this.parameters;
	}

	public TestVisualization generateVisualization() {
		return new TestVisualization();
	}

	public static void main(String[] arguments) {
		TestVisualizationGenerator generator = new TestVisualizationGenerator();
		AWTVisualizationGUIBuilder guiBuilder = new AWTVisualizationGUIBuilder();
		AWTVisualizationRunner runner =
			new AWTVisualizationRunner(generator, guiBuilder, new Dimension(800, 600));
		runner.run();
	}
}