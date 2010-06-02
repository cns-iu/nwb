package edu.iu.cns.visualization.generator;

import java.util.Map;

import edu.iu.cns.visualization.Visualization;
import edu.iu.cns.visualization.parameter.VisualizationParameter;


public interface VisualizationGenerator<A extends Visualization> {
	public Map<String, VisualizationParameter<?>> getParameters();

	public A generateVisualization();
}