package edu.iu.sci2.visualization.scimaps.rendering.web2012;

import java.awt.Dimension;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.PageManager;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class Web2012 {
	private DocumentRenderer document;


	public Web2012(MapOfScience mapOfScience, Dimension size, float scalingFactor) {
		this.document = new DocumentRenderer(mapOfScience, size, scalingFactor);
	}
	
	public RenderableVisualization getVisualization() {
		return this.document;
	}
	
	public PageManager getPageManager() {
		return this.document;
	}

}
