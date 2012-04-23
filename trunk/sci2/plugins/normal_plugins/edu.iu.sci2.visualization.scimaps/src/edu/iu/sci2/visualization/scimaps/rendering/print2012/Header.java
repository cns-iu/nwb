package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import java.awt.Color;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement;

public class Header implements PageElement{
	String title;
	String generatedFrom;
	String publicationMapping;
	String date;
	private double leftBoundary;
	private double topBoundary;

	public Header(String title, String generatedFrom,
			MapOfScience mapOfScience, double leftBoundary, double topBoundary) {

		this.title = title;
		this.generatedFrom = "Generated from " + generatedFrom;

		this.publicationMapping = String
				.format("%s out of %s publications were mapped to %s subdisciplines and %s disciplines.",
						mapOfScience.prettyCountOfMappedPublications(),
						mapOfScience.prettyCountOfPublications(),
						mapOfScience.prettyCountOfMappedSubdisciplines(),
						mapOfScience.prettyCountOfDisciplinesUsed());
		DateTime dateTime = new DateTime();
		DateTimeFormatter formatter = DateTimeFormat
				.forPattern("MMMM dd, yyyy | KK:mm a zzz");
		this.date = formatter.print(dateTime);
		this.leftBoundary = leftBoundary;
		this.topBoundary = topBoundary;
	}


	public void render(GraphicsState state) {
		int titleFontSize = 16;
		int otherFontSize = 10;
		
		state.save();
		state.current.setColor(Color.BLACK);
		state.current.translate(this.leftBoundary, this.topBoundary);
		state.current.translate(0, titleFontSize);

		// Draw the title
		state.setBoldFont("Arial", titleFontSize);
		state.drawStringAndTranslate(this.title, 0, 0);

		// Draw the other
		state.setFont("Arial", otherFontSize);
		state.drawStringAndTranslate(this.generatedFrom, 0, 0);
		state.drawStringAndTranslate(this.publicationMapping, 0, 0);
		state.drawStringAndTranslate(this.date, 0, 0);

		state.restore();
		
	}
}