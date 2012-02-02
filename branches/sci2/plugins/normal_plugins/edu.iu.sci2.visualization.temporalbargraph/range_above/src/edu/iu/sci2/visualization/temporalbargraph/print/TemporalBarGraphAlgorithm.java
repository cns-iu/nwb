package edu.iu.sci2.visualization.temporalbargraph.print;
import java.awt.Color;
import java.util.List;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.utilities.color.ColorRegistry;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithm;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithmFactory;
import edu.iu.sci2.visualization.temporalbargraph.common.DoubleDimension;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;
import edu.iu.sci2.visualization.temporalbargraph.common.TemporalBarGraphColorSchema;

public class TemporalBarGraphAlgorithm extends
		AbstractTemporalBarGraphAlgorithm implements Algorithm {

	private Data inputData;
	private LogService logger;

	private String labelColumn;
	private String startDateColumn;
	private String endDateColumn;
	private String sizeByColumn;
	private String startDateFormat;
	private String endDateFormat;
	private double pageWidth;
	private double pageHeight;
	private boolean shouldScaleOutput;
	private String categoryColumn;
	private ColorRegistry<String> colorRegistry;
	private List<Record> records;
	private String query;

	public TemporalBarGraphAlgorithm(Data inputData, Table inputTable,
			LogService logger, String labelColumn, String startDateColumn,
			String endDateColumn, String sizeByColumn, String startDateFormat,
			String endDateFormat, String query, double pageWidth,
			double pageHeight, boolean shouldScaleOutput, String categoryColumn) {
		this.inputData = inputData;
		this.logger = logger;

		this.labelColumn = labelColumn;
		this.startDateColumn = startDateColumn;
		this.endDateColumn = endDateColumn;
		this.sizeByColumn = sizeByColumn;
		this.startDateFormat = startDateFormat;
		this.endDateFormat = endDateFormat;
		this.query = query;
		this.pageWidth = pageWidth;
		this.pageHeight = pageHeight;
		this.shouldScaleOutput = shouldScaleOutput;
		this.categoryColumn = categoryColumn;

		if (this.categoryColumn
				.equals(AbstractTemporalBarGraphAlgorithmFactory.DO_NOT_PROCESS_CATEGORY_VALUE)) {
			colorRegistry = new ColorRegistry<String>(
					new TemporalBarGraphColorSchema(
							new Color[] { TemporalBarGraphColorSchema.DEFAULT_COLOR },
							TemporalBarGraphColorSchema.DEFAULT_COLOR));
		} else {
			colorRegistry = new ColorRegistry<String>(
					TemporalBarGraphColorSchema.DEFAULT_COLOR_SCHEMA);

		}

		this.records = readRecordsFromTable(inputTable, logger,
				this.labelColumn, this.startDateColumn, this.endDateColumn,
				this.sizeByColumn, this.startDateFormat, this.endDateFormat,
				this.categoryColumn);
		for (Record record : records) {
			colorRegistry.getColorOf(record.getCategory());
		}
	}

	protected String createPostScriptCode(CSVWriter csvWriter)
			throws PostScriptCreationException {

		String legendText = "Area size equals \"" + this.sizeByColumn + "\"";

		PostscriptDocument postscriptDocument = new PostscriptDocument(
				csvWriter, this.records, shouldScaleOutput, legendText,
				colorRegistry, query,
				new DoubleDimension(pageWidth, pageHeight));

		String documentPostScript = postscriptDocument.renderPostscript();

		return documentPostScript;

	}

	@Override
	protected LogService getLogger() {
		return this.logger;
	}

	@Override
	protected Data getInputData() {
		return this.inputData;
	}
}
