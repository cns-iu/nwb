package edu.iu.sci2.visualization.scimaps.rendering.web2012;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.common.CircleSizeLegend;
import edu.iu.sci2.visualization.scimaps.rendering.common.CopyrightInfo;
import edu.iu.sci2.visualization.scimaps.rendering.common.Footer;
import edu.iu.sci2.visualization.scimaps.rendering.common.PageLegend;
import edu.iu.sci2.visualization.scimaps.rendering.common.scimaps.MapOfScienceRenderer;
import edu.iu.sci2.visualization.scimaps.rendering.common.scimaps.MapOfScienceRenderer.MapOfScienceRenderingException;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class DocumentRenderer implements RenderableVisualization {

	private MapOfScience mapOfScience;
	private Dimension dimensions;
	private float scalingFactor;
	
	public DocumentRenderer(MapOfScience mapOfScience, Dimension size, float scalingFactor) {
		this.mapOfScience = mapOfScience;
		this.dimensions = size;
		this.scalingFactor = scalingFactor;
	}

	public String title() {
		String title = "Topic Analysis - Map of Science";
		return title;
	}

	public GraphicsState preRender(Graphics2D graphics, Dimension size) {

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		GraphicsState graphicsState = new GraphicsState(graphics);

		graphicsState.setFont("Arial", 12);

		return graphicsState;
	}

	public void render(GraphicsState state, Dimension size) {

		state.save();
		// FIXME why is translation weird for the map of science?
		double mapOfScienceBottom = 500;
		double mapOfScienceLeft = 25;
		state.current.translate(mapOfScienceLeft, mapOfScienceBottom);
		state.current.scale(2.1, 2.1);
		
		try {
			MapOfScienceRenderer.render(state, this.mapOfScience, this.scalingFactor);
		} catch (MapOfScienceRenderingException e) {
			// SOMEDAY reimplement the interface to allow an exception to be thrown.
			e.printStackTrace();
		}
		state.restore();

		CopyrightInfo.renderAbout(state, 550.0f, (float) getDimension().getWidth() / 2);
		
		Footer.render(state, 400.0f, 940.0f);
		
		CircleSizeLegend circleSizeLegend = new CircleSizeLegend(2.0f);
		circleSizeLegend.render(state, 275.0f, 770.0f);
		
		PageLegend pageLegend = new PageLegend((int) this.mapOfScience.countOfUnmappedPublications(), 0.0, 2838847273884834.0);
		pageLegend.render(state, 50.0f, 770.0f);

	}


	
	public Dimension getDimension() {
		return this.dimensions;
	}

}
