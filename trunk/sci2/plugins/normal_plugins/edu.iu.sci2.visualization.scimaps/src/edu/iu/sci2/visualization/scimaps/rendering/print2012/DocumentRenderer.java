package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.scimaps.MapOfScienceRenderer;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class DocumentRenderer implements RenderableVisualization {

	private MapOfScience mapOfScience;
	private String generatedFrom;
	private Dimension size;
	private float scalingFactor;

	public DocumentRenderer(MapOfScience mapOfScience, String generatedFrom,
			Dimension size, float scalingFactor) {
		this.mapOfScience = mapOfScience;
		this.generatedFrom = generatedFrom;
		this.size = size;
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
		MapOfScienceRenderer.render(state, mapOfScience, scalingFactor);
		state.restore();
		
		CopyrightInfo copyrightInfo = new CopyrightInfo();
		copyrightInfo.render(state, inch(5.5f), (float)size.getWidth());
		
		Header header = new Header(title(), generatedFrom, mapOfScience);
		header.render(state, inch(0.5f), inch(0.5f));
		
		HowToArea howto = new HowToArea();
		howto.render(state, inch(4.5f), inch(6.5f));
		
		Footer footer = new Footer((float) size.getWidth(), inch(8.0f));
		footer.render(state);
		
		String legendTitle = "Circle Area: Fractional Journal Count";
		String legendSubtitle = "Unclasified: " + mapOfScience.prettyCountOfUnmappedPublications();
		
		CircleSizeLegend legend = new CircleSizeLegend(mapOfScience.getMappedWeights(), scalingFactor, legendTitle, legendSubtitle);
		legend.render(state, inch(1.0f), inch(6.5f));		
	}

	public Dimension getDimension() {
		return size;
	}

}
