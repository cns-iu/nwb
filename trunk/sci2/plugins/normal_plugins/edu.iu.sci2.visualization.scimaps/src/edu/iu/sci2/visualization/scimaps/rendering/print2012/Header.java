package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import java.awt.Color;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement;

public class Header implements PageElement{
	String titleSlug;
	String generatedFromSlug;
	String exploreSlug;
	String dateSlug;
	private double leftBoundary;
	private double topBoundary;

	public Header(String title, String generatedFrom,
			MapOfScience mapOfScience, double leftBoundary, double topBoundary) {

		this.titleSlug = title;
		this.generatedFromSlug = "Generated from " + generatedFrom;

		this.exploreSlug = String
				.format("%s out of %s publications were mapped to %s subdiciplines and %s diciplines.",
						mapOfScience.prettyCountOfMappedPublications(),
						mapOfScience.prettyCountOfPublications(),
						mapOfScience.prettyCountOfMappedSubdiciplines(),
						mapOfScience.prettyCountOfDisciplinesUsed());
		DateTime dateTime = new DateTime();
		DateTimeFormatter formatter = DateTimeFormat
				.forPattern("MMMM dd, yyyy | KK:mm a zzz");
		this.dateSlug = formatter.print(dateTime);
		this.leftBoundary = leftBoundary;
		this.topBoundary = topBoundary;
	}


	public void render(GraphicsState state) {
		state.save();
		double titleFontSize = 16;
		Color titleColor = new Color(0, 0, 0);
		double slugFontSize = 12;
		Color slugColor = Color.black;
		state.setFontSize(titleFontSize);
		state.current.translate(this.leftBoundary, this.topBoundary);

		// Draw the title
		state.current.setColor(titleColor);
		state.current.drawString(this.titleSlug, 0, 0);
		state.current.translate(0, titleFontSize);

		// Draw the other slugs
		state.setFontSize(slugFontSize);
		state.current.setColor(slugColor);
		state.current.drawString(this.generatedFromSlug, 0, 0);
		state.current.translate(0, slugFontSize);
		state.current.drawString(this.exploreSlug, 0, 0);
		state.current.translate(0, slugFontSize);
		state.current.drawString(this.dateSlug, 0, 0);
		state.current.translate(0, slugFontSize);
		state.restore();
		
	}
}