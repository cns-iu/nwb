package edu.iu.sci2.visualization.scimaps.rendering.full;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.util.Collections;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.AbstractRenderablePageManager;
import edu.iu.sci2.visualization.scimaps.rendering.CenteredCopyrightInfo;
import edu.iu.sci2.visualization.scimaps.rendering.CircleSizeLegend;
import edu.iu.sci2.visualization.scimaps.rendering.HowToArea;
import edu.iu.sci2.visualization.scimaps.rendering.ItalicCenteredFooter;
import edu.iu.sci2.visualization.scimaps.rendering.PageLegend;
import edu.iu.sci2.visualization.scimaps.rendering.discipline_breakdown.DisciplineBreakdownArea;
import edu.iu.sci2.visualization.scimaps.rendering.discipline_breakdown.DisciplineBreakdownAreas;
import edu.iu.sci2.visualization.scimaps.rendering.scimaps.MapOfScienceRenderer;

public class FullRenderablePageManager extends AbstractRenderablePageManager {
	private final String generatedFrom;

	public FullRenderablePageManager(Dimension size, MapOfScience mapOfScience,
			float scalingFactor, String generatedFrom) {
		super(size, mapOfScience, scalingFactor);
		
		this.generatedFrom = generatedFrom;

		addPageIndependentElements();
		addPageDependentElements();
	}

	@Override
	protected void addPageIndependentElements() {
		addToAllPages(new Header(title(), this.generatedFrom, this.mapOfScience, inch(0.25f),
				inch(0.25f)));
		addToAllPages(new ItalicCenteredFooter(this.dimensions.getWidth() / 2, inch(8.5f - 0.25f)));
	}

	@Override
	protected void addPageDependentElements() {
		int currentPage = 0;
		addMapOfSciencePage(0);
		currentPage++;

		Dimension breakdownAreaSize = new Dimension((int) inch(9.0f), (int) inch(6.5f));
		for (DisciplineBreakdownArea breakdownArea :
				DisciplineBreakdownAreas.getDisciplineBreakdownAreas(
						breakdownAreaSize, 2, this.mapOfScience, inch(0.25f), inch(1.5f))) {
			addToPage(currentPage, breakdownArea);
			currentPage++;
		}
	}

	@Override
	protected void addMapOfSciencePage(int pageNumber) {
		float pageScalingFactor = 1.3f;
		int fontSize = 10;
		int titleFontSize = 14;

		addToPage(pageNumber, new HowToArea(inch(5.5f), inch(7.0f)));
		addToPage(pageNumber,
				new CenteredCopyrightInfo(
						(float) this.dimensions.getWidth() / 2, inch(5.72f),
						fontSize));
		addToPage(pageNumber, new CircleSizeLegend(this.scalingFactor,
				pageScalingFactor, inch(3.47f), inch(7.0f), fontSize,
				titleFontSize, 5, 50));
		addToPage(
				pageNumber,
				new PageLegend(mapOfScience.getDataColumnName(), (int) this.mapOfScience
						.countOfUnmappedPublications(), Collections.min(this.mapOfScience
						.getMappedWeights()),
						Collections.max(this.mapOfScience.getMappedWeights()), inch(0.25f),
						inch(7.0f), titleFontSize, fontSize));
		addToPage(pageNumber, new MapOfScienceRenderer(this.mapOfScience,
				this.scalingFactor, pageScalingFactor, inch(0.0f), inch(5.0f)));
	}
}
