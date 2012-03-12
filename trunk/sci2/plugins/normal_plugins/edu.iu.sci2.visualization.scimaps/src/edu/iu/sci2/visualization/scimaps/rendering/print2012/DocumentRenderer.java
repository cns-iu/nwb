package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

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
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement;
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement.PageElementRenderingException;
import edu.iu.sci2.visualization.scimaps.tempvis.PageManager;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class DocumentRenderer implements RenderableVisualization, PageManager {

	private MapOfScience mapOfScience;
	private String generatedFrom;
	private Dimension dimensions;
	private float scalingFactor;
	private Multimap<Integer, PageElement> pageSpecificElements;
	private Set<PageElement> pageIndependentElements;

	public DocumentRenderer(MapOfScience mapOfScience, String generatedFrom,
			Dimension size, float scalingFactor) {
		this.mapOfScience = mapOfScience;
		this.generatedFrom = generatedFrom;
		this.dimensions = size;
		this.scalingFactor = scalingFactor;

		this.pageSpecificElements = HashMultimap.create();
		this.pageIndependentElements = new HashSet<PageElement>();

		addPageIndependentElements();
		addPageDependentElements();
	}

	private void addPageIndependentElements() {
		addToAllPages(new Header(title(), this.generatedFrom,
				this.mapOfScience, inch(0.5f), inch(0.5f)));
		addToAllPages(new ItalicCenteredFooter(this.dimensions.getWidth() / 2,
				inch(8.0f)));
	}

	private void addPageDependentElements() {

		int currentPage = 0;
		addMapOfSciencePage(0);
		currentPage++;

		Dimension breakdownAreaSize = new Dimension((int) inch(9.0f),
				(int) inch(6.0f));
		for (DisciplineBreakdownArea breakdownArea : DisciplineBreakdownAreas
				.getDisciplineBreakdownAreas(breakdownAreaSize, 2,
						this.mapOfScience, 35, 100)) {
			addToPage(currentPage, breakdownArea);
			currentPage++;
		}
	}

	private void addMapOfSciencePage(int pageNumber) {
		addToPage(pageNumber, new HowToArea(inch(5.5f), inch(6.3f)));
		addToPage(pageNumber, new CenteredCopyrightInfo((float) this.dimensions.getWidth() / 2, inch(5.72f) , 10));
		addToPage(pageNumber, new CircleSizeLegend(this.scalingFactor,
				inch(3.25f), inch(6.3f)));
		addToPage(
				pageNumber,
				new PageLegend((int) this.mapOfScience
						.countOfUnmappedPublications(), Collections
						.min(this.mapOfScience.getMappedWeights()), Collections
						.max(this.mapOfScience.getMappedWeights()), inch(0.5f),
						inch(6.3f)));
		addToPage(pageNumber, new MapOfScienceRenderer(this.mapOfScience,
				this.scalingFactor, 1.3, inch(0.0f), inch(5.0f)));

	}

	private void addToPage(int pageNumber, PageElement pageElement) {
		this.pageSpecificElements.put(pageNumber, pageElement);
	}

	private void addToAllPages(PageElement pageElement) {
		this.pageIndependentElements.add(pageElement);
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

	public void render(int pageNumber, GraphicsState state)
			throws PageManagerRenderingException {
		if (!this.pageSpecificElements.isEmpty()
				&& this.pageSpecificElements.get(pageNumber).isEmpty()) {
			return;
		}

		if (pageNumber != 1 && this.pageSpecificElements.isEmpty()) {
			throw new PageManagerRenderingException("Page number '"
					+ pageNumber + "' does not exist");
		}

		List<PageElementRenderingException> exceptions = new ArrayList<PageElementRenderingException>();

		for (PageElement element : this.pageIndependentElements) {
			try {
				element.render(state);
			} catch (PageElementRenderingException e) {
				exceptions.add(e);
			}
		}

		for (PageElement element : this.pageSpecificElements.get(pageNumber)) {
			try {
				element.render(state);
			} catch (PageElementRenderingException e) {
				exceptions.add(e);
			}
		}

		if (!exceptions.isEmpty()) {
			String newline = System.getProperty("line.separator");
			String message = "The following exceptions occured when rendering.  The cause of the first is also passed up."
					+ newline;

			for (PageElementRenderingException e : exceptions) {
				message += e.getMessage() + newline;
			}

			throw new PageManagerRenderingException(message, exceptions.get(0));
		}

		return;
	}

	public int numberOfPages() {
		if (this.pageIndependentElements.isEmpty()
				&& this.pageSpecificElements.isEmpty()) {
			return 0;
		}

		if (this.pageSpecificElements.isEmpty()
				&& !this.pageIndependentElements.isEmpty()) {
			return 1;
		}

		return this.pageSpecificElements.size();
	}

	public Dimension getDimension() {
		return this.dimensions;
	}

	public Dimension pageDimensions() {
		return this.dimensions;
	}

}
