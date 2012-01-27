package edu.iu.sci2.visualization.temporalbargraph;

import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.POINTS_PER_INCH;

import java.util.List;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.color.ColorRegistry;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractPostscriptDocument;
import edu.iu.sci2.visualization.temporalbargraph.common.DoubleDimension;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;

public class PostscriptDocument
		extends
		edu.iu.sci2.visualization.temporalbargraph.common.AbstractPostscriptDocument {

	private DoubleDimension postscriptPageSize;
	private TemporalBarGraphPages temporalBarGraphPages;

	public static StringTemplateGroup documentGroup = AbstractPostscriptDocument.documentGroup;

	public PostscriptDocument(CSVWriter csvWriter, List<Record> records,
			boolean scaleToOnePage, String legendText,
			ColorRegistry<String> colorRegistry, String query,
			DoubleDimension pageSize) throws PostScriptCreationException {

		this.postscriptPageSize = new DoubleDimension(pageSize.getWidth() * POINTS_PER_INCH,
				pageSize.getHeight() * POINTS_PER_INCH);
		this.temporalBarGraphPages = new TemporalBarGraphPages(csvWriter, records, scaleToOnePage,
				colorRegistry, postscriptPageSize, legendText, query);
	}

	@Override
	protected TemporalBarGraphPages getPages() {
		return this.temporalBarGraphPages;
	}

	@Override
	protected DoubleDimension getPageSize() {
		return this.postscriptPageSize;
	}

}