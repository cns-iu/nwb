package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import java.awt.Dimension;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class Print2012 {
	private RenderableVisualization visualization;


	public Print2012(MapOfScience mapOfScience, String generatedFrom, Dimension size, float scalingFactor) {
		this(createVisualization(mapOfScience, generatedFrom, size, scalingFactor));
	}
	
	/**
	 * @param visualization
	 */
	private Print2012(RenderableVisualization visualization){
		this.visualization = visualization;
	}
	
	private static RenderableVisualization createVisualization(MapOfScience mapOfScience, String generatedFrom, Dimension size, float scalingFactor){
		RenderableVisualization visualization = new DocumentRenderer(mapOfScience, generatedFrom, size, scalingFactor);
		
		return visualization;
	}
	
	public RenderableVisualization getVisualization() {
		return visualization;
	}

}
