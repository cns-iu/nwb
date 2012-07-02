package edu.iu.sci2.visualization.scimaps.rendering.web2012;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.common.CenteredCopyrightInfo;
import edu.iu.sci2.visualization.scimaps.rendering.common.CircleSizeLegend;
import edu.iu.sci2.visualization.scimaps.rendering.common.ItalicCenteredFooter;
import edu.iu.sci2.visualization.scimaps.rendering.common.PageLegend;
import edu.iu.sci2.visualization.scimaps.rendering.common.discipline_breakdown.DisciplineBreakdownArea;
import edu.iu.sci2.visualization.scimaps.rendering.common.discipline_breakdown.DisciplineBreakdownAreas;
import edu.iu.sci2.visualization.scimaps.rendering.common.scimaps.MapOfScienceRenderer;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageManager;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class DocumentRenderer extends PageManager implements
		RenderableVisualization {
	private MapOfScience mapOfScience;
	private float scalingFactor;

	public DocumentRenderer(MapOfScience mapOfScience, Dimension size,
			float scalingFactor) {
		this.mapOfScience = mapOfScience;
		this.dimensions = size;
		this.scalingFactor = scalingFactor;

		addPageIndependentElements();
		addPageDependentElements();
	}

	private void addPageDependentElements() {
		int currentPage = 0;
		addMapOfSciencePage(currentPage);
		currentPage++;

		Dimension breakdownAreaSize = new Dimension(1100, 800);
		for (DisciplineBreakdownArea breakdownArea : DisciplineBreakdownAreas
				.getDisciplineBreakdownAreas(breakdownAreaSize, 2,
						this.mapOfScience, inch(0.25f), 100.0f)) {
			addToPage(currentPage, breakdownArea);
			currentPage++;
		}

	}

	private void addMapOfSciencePage(int pageNumber) {
		float pageScalingFactor = 2.1f;
		addToPage(
				pageNumber,
				new CenteredCopyrightInfo(
						(float) this.dimensions.getWidth() / 2, inch(8.66f), 16));
		addToPage(pageNumber, new CircleSizeLegend(this.scalingFactor,
				pageScalingFactor, inch(4.96f), inch(11.0f), 16, 20, 5, 100));
		addToPage(
				pageNumber,
				new PageLegend(mapOfScience.getDataColumnName(), (int) this.mapOfScience
						.countOfUnmappedPublications(), Collections.min(this.mapOfScience
						.getMappedWeights()),
						Collections.max(this.mapOfScience.getMappedWeights()), inch(0.25f),
						inch(11.0f), 20, 16));
		addToPage(pageNumber, new MapOfScienceRenderer(this.mapOfScience,
				this.scalingFactor, pageScalingFactor, 25, 500));
	}

	private void addPageIndependentElements() {
		addToAllPages(new ItalicCenteredFooter(this.dimensions.getWidth() / 2,
				this.dimensions.getHeight() - inch(0.25f)));
	}

	@Override
	public String title() {
		String title = "Topical Visualization";
		return title;
	}

	@Override
	public GraphicsState preRender(Graphics2D graphics, Dimension size) {

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
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
