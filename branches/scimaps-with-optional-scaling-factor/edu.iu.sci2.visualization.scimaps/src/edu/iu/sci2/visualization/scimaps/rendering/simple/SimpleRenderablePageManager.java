package edu.iu.sci2.visualization.scimaps.rendering.simple;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.util.Collections;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.AbstractRenderablePageManager;
import edu.iu.sci2.visualization.scimaps.rendering.CenteredCopyrightInfo;
import edu.iu.sci2.visualization.scimaps.rendering.CircleSizeLegend;
import edu.iu.sci2.visualization.scimaps.rendering.ItalicCenteredFooter;
import edu.iu.sci2.visualization.scimaps.rendering.Layout;
import edu.iu.sci2.visualization.scimaps.rendering.PageLegend;
import edu.iu.sci2.visualization.scimaps.rendering.discipline_breakdown.DisciplineBreakdownArea;
import edu.iu.sci2.visualization.scimaps.rendering.discipline_breakdown.DisciplineBreakdownAreas;
import edu.iu.sci2.visualization.scimaps.rendering.scimaps.MapOfScienceRenderer;

public class SimpleRenderablePageManager extends AbstractRenderablePageManager {
	public SimpleRenderablePageManager(Layout layout, Dimension size, MapOfScience mapOfScience,
			float scalingFactor) {
		super(layout, size, mapOfScience, scalingFactor);
		
		addPageIndependentElements();
		addPageDependentElements();
	}
	
	@Override
	protected void addPageIndependentElements() {
		addToAllPages(new ItalicCenteredFooter(this.dimensions.getWidth() / 2,
				this.dimensions.getHeight() - inch(0.25f)));
	}

	@Override
	protected void addPageDependentElements() {
		int currentPage = 0;
		addMapOfSciencePage(currentPage);
		currentPage++;

		Dimension breakdownAreaSize = new Dimension(1100, 800);
		for (DisciplineBreakdownArea breakdownArea : DisciplineBreakdownAreas
				.getDisciplineBreakdownAreas(breakdownAreaSize, 2, this.mapOfScience, inch(0.25f),
						100.0f)) {
			addToPage(currentPage, breakdownArea);
			currentPage++;
		}
	}

	@Override
	protected void addMapOfSciencePage(int pageNumber) {
		addToPage(pageNumber, new CenteredCopyrightInfo((float) this.dimensions.getWidth() / 2,
				inch(8.66f), 16));
		addToPage(pageNumber, new CircleSizeLegend(this.scalingFactor, inch(4.96f), inch(11.0f),
				16, 20, 5, layout.getAreaForLegendMax()));
		addToPage(
				pageNumber,
				new PageLegend(
						mapOfScience.getDataColumnName(),
						(int) this.mapOfScience.countOfUnmappedPublications(),
						Collections.min(this.mapOfScience.getMappedWeights()),
						Collections.max(this.mapOfScience.getMappedWeights()),
						scalingFactor,
						inch(0.25f),
						inch(11.0f),
						20,
						16));
		addToPage(pageNumber, new MapOfScienceRenderer(this.mapOfScience, this.scalingFactor,
				layout.getMapSpecificScalingFactor(), 25, 500));
	}
}
