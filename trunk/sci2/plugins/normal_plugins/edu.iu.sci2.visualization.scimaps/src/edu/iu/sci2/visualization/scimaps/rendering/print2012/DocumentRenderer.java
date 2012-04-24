package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Collections;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.common.CenteredCopyrightInfo;
import edu.iu.sci2.visualization.scimaps.rendering.common.CircleSizeLegend;
import edu.iu.sci2.visualization.scimaps.rendering.common.HowToArea;
import edu.iu.sci2.visualization.scimaps.rendering.common.ItalicCenteredFooter;
import edu.iu.sci2.visualization.scimaps.rendering.common.PageLegend;
import edu.iu.sci2.visualization.scimaps.rendering.common.discipline_breakdown.DisciplineBreakdownArea;
import edu.iu.sci2.visualization.scimaps.rendering.common.discipline_breakdown.DisciplineBreakdownAreas;
import edu.iu.sci2.visualization.scimaps.rendering.common.scimaps.MapOfScienceRenderer;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageManager;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class DocumentRenderer extends PageManager implements RenderableVisualization {

	private MapOfScience mapOfScience;
	private String generatedFrom;
	private float scalingFactor;

	public DocumentRenderer(MapOfScience mapOfScience, String generatedFrom,
			Dimension size, float scalingFactor) {
		this.mapOfScience = mapOfScience;
		this.generatedFrom = generatedFrom;
		this.dimensions = size;
		this.scalingFactor = scalingFactor;

		addPageIndependentElements();
		addPageDependentElements();
	}

	private void addPageIndependentElements() {
		addToAllPages(new Header(title(), this.generatedFrom,
				this.mapOfScience, inch(0.25f), inch(0.25f)));
		addToAllPages(new ItalicCenteredFooter(this.dimensions.getWidth() / 2,
				inch(8.5f - 0.25f)));
	}

	private void addPageDependentElements() {

		int currentPage = 0;
		addMapOfSciencePage(0);
		currentPage++;

		Dimension breakdownAreaSize = new Dimension((int) inch(9.0f),
				(int) inch(6.5f));
		for (DisciplineBreakdownArea breakdownArea : DisciplineBreakdownAreas
				.getDisciplineBreakdownAreas(breakdownAreaSize, 2,
						this.mapOfScience, inch(0.25f), inch(1.5f))) {
			addToPage(currentPage, breakdownArea);
			currentPage++;
		}
	}

	private void addMapOfSciencePage(int pageNumber) {
		float pageScalingFactor = 1.3f;
		int fontSize = 10;
		int titleFontSize = 14;
		
		addToPage(pageNumber, new HowToArea(inch(5.5f), inch(7.0f)));
		addToPage(
				pageNumber,
				new CenteredCopyrightInfo(
						(float) this.dimensions.getWidth() / 2, inch(5.72f), fontSize));
		addToPage(pageNumber, new CircleSizeLegend(this.scalingFactor, pageScalingFactor,
				inch(3.47f), inch(7.0f), fontSize, titleFontSize, 5, 50));
		addToPage(
				pageNumber,
				new PageLegend((int) this.mapOfScience
						.countOfUnmappedPublications(), Collections
						.min(this.mapOfScience.getMappedWeights()), Collections
						.max(this.mapOfScience.getMappedWeights()), inch(0.25f),
						inch(7.0f), titleFontSize, fontSize));
		addToPage(pageNumber, new MapOfScienceRenderer(this.mapOfScience,
				this.scalingFactor, pageScalingFactor, inch(0.0f), inch(5.0f)));

	}

	public String title() {
		String title = "Topical Visualization";
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
		try {
			this.render(0, state);
		} catch (PageManagerRenderingException e) {
			e.printStackTrace();
		}
	}

	public Dimension getDimension() {
		return this.dimensions;
	}

}
