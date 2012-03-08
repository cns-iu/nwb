package edu.iu.sci2.visualization.scimaps.rendering.web2012;

import java.awt.Dimension;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class Web2012 {
	private RenderableVisualization visualization;


	public Web2012(MapOfScience mapOfScience, Dimension size, float scalingFactor) {
		this(createVisualization(mapOfScience,size, scalingFactor));
	}
	
	/**
	 * @param visualization
	 */
	private Web2012(RenderableVisualization visualization){
		this.visualization = visualization;
	}
	
	private static RenderableVisualization createVisualization(MapOfScience mapOfScience, Dimension size, float scalingFactor){
		RenderableVisualization visualization = new DocumentRenderer(mapOfScience, size, scalingFactor);
		
		return visualization;
	}
	
	public RenderableVisualization getVisualization() {
		return this.visualization;
	}

}
