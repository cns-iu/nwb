package edu.iu.sci2.visualization.scimaps.rendering;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.AbstractPageManager;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public abstract class AbstractRenderablePageManager extends AbstractPageManager implements
		RenderableVisualization {
	protected final MapOfScience mapOfScience;
	protected final float scalingFactor;

	protected AbstractRenderablePageManager(Layout layout, Dimension dimensions, MapOfScience mapOfScience,
			float scalingFactor) {
		super(layout, dimensions);
		
		this.mapOfScience = mapOfScience;
		this.scalingFactor = scalingFactor;
	}
	
	protected abstract void addPageDependentElements();
	protected abstract void addMapOfSciencePage(int pageNumber);
	protected abstract void addPageIndependentElements();

	@Override
	public String title() {
		return "Topical Visualization";
	}

	@Override
	public GraphicsState preRender(Graphics2D graphics, Dimension size) {
		graphics.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		GraphicsState graphicsState = new GraphicsState(graphics);

		graphicsState.setFont("Arial", 12);

		return graphicsState;
	}

	@Override
	public void render(GraphicsState state, Dimension size) {
		try {
			this.render(0, state);
		} catch (PageManagerRenderingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Dimension getDimension() {
		return this.dimensions;
	}
}
