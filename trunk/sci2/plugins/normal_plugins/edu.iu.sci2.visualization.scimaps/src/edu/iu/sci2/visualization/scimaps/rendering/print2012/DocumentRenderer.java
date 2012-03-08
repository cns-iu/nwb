package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.common.CircleSizeLegend;
import edu.iu.sci2.visualization.scimaps.rendering.common.CopyrightInfo;
import edu.iu.sci2.visualization.scimaps.rendering.common.Footer;
import edu.iu.sci2.visualization.scimaps.rendering.common.HowToArea;
import edu.iu.sci2.visualization.scimaps.rendering.common.PageLegend;
import edu.iu.sci2.visualization.scimaps.rendering.common.scimaps.MapOfScienceRenderer;
import edu.iu.sci2.visualization.scimaps.rendering.common.scimaps.MapOfScienceRenderer.MapOfScienceRenderingException;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class DocumentRenderer implements RenderableVisualization {

	private MapOfScience mapOfScience;
	private String generatedFrom;
	private Dimension dimensions;
	private float scalingFactor;

	public DocumentRenderer(MapOfScience mapOfScience, String generatedFrom,
			Dimension size, float scalingFactor) {
		this.mapOfScience = mapOfScience;
		this.generatedFrom = generatedFrom;
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
		double mapOfScienceBottom = inch(5.0f);
		double mapOfScienceLeft = inch(0.0f);
		state.current.translate(mapOfScienceLeft, mapOfScienceBottom);
		state.current.scale(1.3, 1.3);
		try {
			MapOfScienceRenderer.render(state, this.mapOfScience, this.scalingFactor);
		} catch (MapOfScienceRenderingException e) {
			// SOMEDAY change the interface to allow an exception to be thrown.
			e.printStackTrace();
		}
		state.restore();
		
		CopyrightInfo.renderAbout(state, inch(5.5f), (float)size.getWidth() / 2);
		
		Header header = new Header(title(), this.generatedFrom, this.mapOfScience);
		header.render(state, inch(0.5f), inch(0.5f));
		
		HowToArea.render(state, inch(5.5f), inch(6.3f));

		Footer.renderAbout(state, (float) size.getWidth() / 2, inch(8.0f));
		
		CircleSizeLegend circleSizeLegend = new CircleSizeLegend();
		circleSizeLegend.render(state, inch(3.25f), inch(6.3f));
		
		PageLegend pageLegend = new PageLegend((int) this.mapOfScience.countOfUnmappedPublications(), 0.0, 2838847273884834.0);
		pageLegend.render(state, inch(0.5f), inch(6.3f));
	}

	public Dimension getDimension() {
		return this.dimensions;
	}
	
}
