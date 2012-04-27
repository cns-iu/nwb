package edu.iu.cns.visualization.test;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import edu.iu.cns.visualization.generator.VisualizationGenerator;
import edu.iu.cns.visualization.gui.awt.AWTVisualizationRunner;
import edu.iu.cns.visualization.parameter.VisualizationParameter;

public class TestVisualizationGenerator implements VisualizationGenerator<TestVisualization> {
	private Map<String, VisualizationParameter<?>> parameters =
		new HashMap<String, VisualizationParameter<?>>();

	@Override
	public Map<String, VisualizationParameter<?>> getParameters() {
		return this.parameters;
	}

	@Override
	public TestVisualization generateVisualization() {
		return new TestVisualization();
	}

	public static void main(String[] arguments) {
		TestVisualizationGenerator generator = new TestVisualizationGenerator();
		AWTVisualizationRunner runner =
				new AWTVisualizationRunner(generator, new Dimension(800, 600));
		runner.run();
	}
}