package edu.iu.sci2.visualization.temporalbargraph.print;

import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.POINTS_PER_INCH;

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

	private DoubleDimension postscriptPageSize;
	private AbstractPages temporalBarGraphPages;

	public PostscriptDocument(CSVWriter csvWriter, List<Record> records,
			boolean scaleToOnePage, String legendText,
			String categoryText, ColorRegistry<String> colorRegistry, String query,
			DoubleDimension pageSize) throws PostScriptCreationException {

		this.postscriptPageSize = new DoubleDimension(pageSize.getWidth()
				* POINTS_PER_INCH, pageSize.getHeight() * POINTS_PER_INCH);
		
		if (this.postscriptPageSize.getWidth() > this.postscriptPageSize.getHeight()){
		this.temporalBarGraphPages = new TemporalBarGraphLandscapePages(csvWriter,
				records, scaleToOnePage, colorRegistry, this.postscriptPageSize,
				legendText, categoryText, query);
		} else {
			this.temporalBarGraphPages = new TemporalBarGraphPortraitPages(csvWriter,
					records, scaleToOnePage, colorRegistry, this.postscriptPageSize,
					legendText, categoryText, query);
		}
	}

	@Override
	protected AbstractPages getPages() {
		return this.temporalBarGraphPages;
	}

	@Override
	protected DoubleDimension getPageSize() {
		return this.postscriptPageSize;
	}

}