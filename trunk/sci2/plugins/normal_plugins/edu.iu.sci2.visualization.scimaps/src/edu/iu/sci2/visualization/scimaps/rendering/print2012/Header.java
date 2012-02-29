package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import java.awt.Color;
import java.text.ChoiceFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

public class Header {
	String titleSlug;
	String generatedFromSlug;
	String exploreSlug;
	String dateSlug;

	public Header(String title, String generatedFrom,
			MapOfScience mapOfScience) {

		titleSlug = title;
		generatedFromSlug = "Generated from " + generatedFrom;

		// All to make the language match for the last sentence...
		// TODO extend this functionality for the other text in the slug.
		MessageFormat messageForm = new MessageFormat("{0}");
		double[] pubLimits = { 0, 1, 2 };
		String[] pubStrings = { "{1} publications are", "1 publication is",
				"{1} publications are" };
		ChoiceFormat choiceForm = new ChoiceFormat(pubLimits, pubStrings);
		Format[] formats = { choiceForm, NumberFormat.getInstance() };
		messageForm.setFormats(formats);
		Object[] messageArguments = {
				mapOfScience.countOfUnmappedPublications(),
				mapOfScience.countOfUnmappedPublications() };

		exploreSlug = String
				.format("Explore publication activity: %s out of %s publications were mapped to %s subdiciplines and %s diciplines.  Exactly %s unclassified.",
						mapOfScience.prettyCountOfMappedPublications(),
						mapOfScience.prettyCountOfPublications(),
						mapOfScience.prettyCountOfMappedSubdiciplines(),
						mapOfScience.prettyCountOfCategoriesUsed(),
						messageForm.format(messageArguments));
		DateTime dateTime = new DateTime();
		DateTimeFormatter formatter = DateTimeFormat
				.forPattern("MMMM dd, yyyy | KK:mm:ss a zzz");
		dateSlug = formatter.print(dateTime);
	}

	public void render(GraphicsState state, float leftMargin, float topMargin) {
		state.save();
		double titleFontSize = 16;
		Color titleColor = new Color(0, 0, 0);
		double slugFontSize = 12;
		Color slugColor = Color.black;
		state.setFontSize(titleFontSize);
		state.current.translate(leftMargin, topMargin);

		// Draw the title
		state.current.setColor(titleColor);
		state.current.drawString(titleSlug, 0, 0);
		state.current.translate(0, titleFontSize);

		// Draw the other slugs
		state.setFontSize(slugFontSize);
		state.current.setColor(slugColor);
		state.current.drawString(generatedFromSlug, 0, 0);
		state.current.translate(0, slugFontSize);
		state.current.drawString(exploreSlug, 0, 0);
		state.current.translate(0, slugFontSize);
		state.current.drawString(dateSlug, 0, 0);
		state.current.translate(0, slugFontSize);
		state.restore();
	}
}