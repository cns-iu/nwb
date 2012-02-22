package edu.iu.sci2.visualization.temporalbargraph.web;

import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.POINTS_PER_INCH;

import java.util.List;

import org.cishell.utilities.color.ColorRegistry;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractPages;
import edu.iu.sci2.visualization.temporalbargraph.common.DoubleDimension;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;

public class PostscriptDocument extends
		edu.iu.sci2.visualization.temporalbargraph.common.AbstractPostscriptDocument {

	private static final double WEB_HEIGHT = 960; // in pixels
	private static final double WEB_WIDTH = 1280; // in pixels
	private static final double PIXELS_PER_INCH = POINTS_PER_INCH;
	
	private DoubleDimension size;
	private WebTemporalBarGraphPages webTemporalBarGraphPages;
	
	public PostscriptDocument(CSVWriter csvWriter, List<Record> records,
			boolean scaleToOnePage, String legendText, String categoryText,
			ColorRegistry<String> colorRegistry) throws PostScriptCreationException {
		
		double pageHeight = WEB_HEIGHT / PIXELS_PER_INCH * POINTS_PER_INCH;
		double pageWidth = WEB_WIDTH / PIXELS_PER_INCH * POINTS_PER_INCH;
		
		this.size = new DoubleDimension(pageWidth, pageHeight);
		this.webTemporalBarGraphPages = new WebTemporalBarGraphPages(csvWriter, records, scaleToOnePage,
				colorRegistry, getPageSize(), legendText, categoryText);

	}

	@Override
	protected AbstractPages getPages() {
		return this.webTemporalBarGraphPages;
	}

	@Override
	protected DoubleDimension getPageSize() {
		return this.size;
	}


}
