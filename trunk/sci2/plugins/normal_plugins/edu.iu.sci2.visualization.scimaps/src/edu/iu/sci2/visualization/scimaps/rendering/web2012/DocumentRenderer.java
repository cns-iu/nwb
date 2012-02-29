package edu.iu.sci2.visualization.scimaps.rendering.web2012;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.print2012.HowToArea;
import edu.iu.sci2.visualization.scimaps.rendering.scimaps.MapOfScienceRenderer;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class DocumentRenderer implements RenderableVisualization {

	private MapOfScience mapOfScience;
	private Dimension size;
	private float scalingFactor;
	
	public DocumentRenderer(MapOfScience mapOfScience, Dimension size, float scalingFactor) {
		this.mapOfScience = mapOfScience;
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
		// FIXME why is translation weird for the map of science?
		double mapOfScienceBottom = 500;
		double mapOfScienceLeft = 50;
		state.current.translate(mapOfScienceLeft, mapOfScienceBottom);
		state.current.scale(2.0, 2.0);
		MapOfScienceRenderer.render(state, mapOfScience, scalingFactor);
		state.restore();

		CopyrightInfo copyrightInfo = new CopyrightInfo();
		copyrightInfo.render(state, 550.0f, (float)getDimension().getWidth());
		
		Footer footer = new Footer();
		footer.render(state, 400.0f, 910.0f);

		String legendTitle = "Circle Area: Fractional Journal Count";
		String legendSubtitle = "Unclasified: "
				+ mapOfScience.prettyCountOfUnmappedPublications();

		HowToArea howto = new HowToArea();
		howto.render(state, 600.0f, 760.0f);
		
		CircleSizeLegend legend = new CircleSizeLegend(
				mapOfScience.getMappedWeights(), scalingFactor, legendTitle,
				legendSubtitle);
		legend.render(state, 100.0f, 760.0f);

	}


	
	public Dimension getDimension() {
		return size;
	}

}
