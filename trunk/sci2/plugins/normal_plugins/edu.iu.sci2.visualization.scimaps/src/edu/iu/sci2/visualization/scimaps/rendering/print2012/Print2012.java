package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import java.awt.Dimension;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.PageManager;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class Print2012 {
	private DocumentRenderer document;

	public Print2012(MapOfScience mapOfScience, String generatedFrom,
			Dimension size, float scalingFactor) {
		this.document = new DocumentRenderer(
				mapOfScience, generatedFrom, size, scalingFactor);
	}

	public RenderableVisualization getVisualization() {
		return this.document;
	}

	public PageManager getPageManger() {
		return this.document;
	}

}
