package edu.iu.sci2.visualization.scimaps.rendering.print2008;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Date;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.scimaps.MapOfScienceRenderer;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

/**
 * This class renders the print version of the map of science.
 *
 */
public class MapOfScienceDocumentRenderer implements RenderableVisualization {
	private String dataDisplayName;
	private String inDataLabel;
	private Date date;
	private double listFontSize;
	private String titleSlug;
	private String locatedSlug;
	private String unmatchedSlug;
	private MapOfScience mapOfScience;
	private float scalingFactor;
	private String circleSizeMeaning;
	private Dimension size;

	public MapOfScienceDocumentRenderer(String dataDisplayName, String inDataLabel,
			MapOfScience mapOfScience, double listFontSize, String titleSlug,
			String locatedSlug, String unmatchedSlug, Date date,
			float scalingFactor, String circleSizeMeaning, Dimension size) {
		this.dataDisplayName = dataDisplayName;
		this.inDataLabel = inDataLabel;
		this.listFontSize = listFontSize;
		this.mapOfScience = mapOfScience;

		this.titleSlug = titleSlug;
		this.locatedSlug = locatedSlug;
		this.unmatchedSlug = unmatchedSlug;

		this.scalingFactor = scalingFactor;
		this.circleSizeMeaning = circleSizeMeaning;

		this.date = date;
		this.size = size;
	}

	public GraphicsState preRender(Graphics2D graphics, Dimension size) {
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		GraphicsState graphicsState = new GraphicsState(graphics);

		graphicsState.setFont("Garamond", 12);

		return graphicsState;
	}

	public void render(GraphicsState state, Dimension size) {
		double scale = 1;

		state.current.translate(0, size.height / scale);
		state.current.scale(scale, scale);

		state.save();
		state.current.translate(0, -size.height);
		render(state);
		state.restore();

	}

	private void render(GraphicsState state) {
		state.save();
		HeaderFooterRenderer.render(state, new HeaderFooter(inDataLabel, date));
		state.restore();

		// Overview
		state.setFontSize(14);
		state.current.drawString(title(), inch(.5f), inch(.8f));

		state.setFontSize(8);
		String shortReport = mapOfScience.prettyCountOfMappedPublications()
				+ " out of " + mapOfScience.prettyCountOfPublications() + " "
				+ locatedSlug + " located.";
		String longReport = "These " + mapOfScience.prettyCountOfMappedPublications()
				+ " " + locatedSlug + " are associated with "
				+ mapOfScience.prettyCountOfCategoriesUsed()
				+ " of 13 disciplines of science and "
				+ mapOfScience.prettyCountOfMappedSubdiciplines()
				+ " of 554 research specialties in"
				+ " the UCSD Map of Science.";

		state.current.drawString(shortReport, inch(.5f), inch(1.1f));
		state.current.drawString(longReport, inch(.5f), inch(1.38f));

		state.current.translate(0, inch(4.2f));

		// Map
		state.current
		.drawString(
				"Copyright (c) 2008 The Regents of the University of California",
				inch(4.8f), inch(.45f));
		MapOfScienceRenderer.render(state, mapOfScience, scalingFactor);
		
		// Legend
		CircleSizeLegend circleSizeLegend = new CircleSizeLegend(
				mapOfScience.getMappedWeights(), scalingFactor,
				circleSizeMeaning);
		CircleSizeLegendRenderer.render(state, circleSizeLegend);
		
		state.current.translate(inch(1), inch(.5f));

		state.setFontSize(Math.max(1.0, listFontSize));
		double lineHeight = 1.5 * listFontSize;

		CategoryRenderer
				.renderMappedCategories(state, mapOfScience, lineHeight);

		state.save();
		state.scaleFont(1.5);
		if (mapOfScience.getUnmappedResults().isEmpty()) {
			state.current.drawString(
					"All entries were successfully identified.", 0, 0);
		} else {
			state.current.drawString(unmatchedSlug, 0, 0);
		}
		state.restore();

		state.setGray(.4);

		CategoryRenderer.renderUnmappedCategories(state, mapOfScience,
				lineHeight);
	}

	public String title() {
		return "Science Map " + titleSlug + " for " + dataDisplayName;
	}

	public Dimension getDimension() {
		return this.size;
	}

}