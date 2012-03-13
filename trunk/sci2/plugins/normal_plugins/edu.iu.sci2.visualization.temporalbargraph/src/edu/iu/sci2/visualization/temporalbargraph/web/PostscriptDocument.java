package edu.iu.sci2.visualization.temporalbargraph.web;

import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.pixelToInch;
import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.inchToPoint;

import java.util.List;

import org.cishell.utilities.color.ColorRegistry;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractPages;
import edu.iu.sci2.visualization.temporalbargraph.common.DoubleDimension;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;

public class PostscriptDocument
		extends
		edu.iu.sci2.visualization.temporalbargraph.common.AbstractPostscriptDocument {

	private static final double WEB_HEIGHT = 960; // in pixels
	private static final double WEB_WIDTH = 1280; // in pixels

	private DoubleDimension size;
	private WebTemporalBarGraphPages webTemporalBarGraphPages;

	public PostscriptDocument(CSVWriter csvWriter, List<Record> records,
			boolean scaleToOnePage, String areaColumn, String categoryColumn,
			String labelColumn, ColorRegistry<String> colorRegistry)
			throws PostScriptCreationException {

		double pageWidth = inchToPoint(pixelToInch(WEB_WIDTH));
		double pageHeight = inchToPoint(pixelToInch(WEB_HEIGHT));

		this.size = new DoubleDimension(pageWidth, pageHeight);
		this.webTemporalBarGraphPages = new WebTemporalBarGraphPages(csvWriter,
				records, scaleToOnePage, colorRegistry, getPageSize(),
				areaColumn, categoryColumn, labelColumn);

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
