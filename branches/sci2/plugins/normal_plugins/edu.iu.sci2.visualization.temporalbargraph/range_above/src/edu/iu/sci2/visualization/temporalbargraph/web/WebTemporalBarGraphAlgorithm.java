package edu.iu.sci2.visualization.temporalbargraph.web;

import java.awt.Color;
import java.util.List;

import org.cishell.framework.data.Data;
import org.cishell.utilities.color.ColorRegistry;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithm;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithmFactory;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;
import edu.iu.sci2.visualization.temporalbargraph.common.TemporalBarGraphColorSchema;

public class WebTemporalBarGraphAlgorithm extends
		AbstractTemporalBarGraphAlgorithm {

	private LogService logger;
	private Data inputData;

	private String areaColumn;
	
	private boolean shouldScaleOutput;
	private ColorRegistry<String> colorRegistry;
	private List<Record> records;
	private String categoryColumn;

	public WebTemporalBarGraphAlgorithm(Data inputData, Table inputTable,
			LogService logger, String labelColumn, String startDateColumn,
			String endDateColumn, String sizeByColumn, String startDateFormat,
			String endDateFormat, boolean shouldScaleOutput,
			String categoryColumn) {

		this.logger = logger;
		this.inputData = inputData;

		/*
		 * SOMEDAY I don't want to pass the column label down all the way, but I
		 * really shouldn't care what the legend text is at this point. If you
		 * find a good solution, please apply it to the colorRegistry and
		 * categoryText too.
		 */
		this.areaColumn = sizeByColumn;

		this.shouldScaleOutput = shouldScaleOutput;
		this.categoryColumn = categoryColumn;
		
		if (AbstractTemporalBarGraphAlgorithmFactory.DO_NOT_PROCESS_CATEGORY_VALUE
				.equals(this.categoryColumn)) {
			this.colorRegistry = new ColorRegistry<String>(
					new TemporalBarGraphColorSchema(
							new Color[] { TemporalBarGraphColorSchema.DEFAULT_COLOR },
							TemporalBarGraphColorSchema.DEFAULT_COLOR));
		} else {
			this.colorRegistry = new ColorRegistry<String>(
					TemporalBarGraphColorSchema.DEFAULT_COLOR_SCHEMA);

		}

		this.records = readRecordsFromTable(inputTable, logger, labelColumn,
				startDateColumn, endDateColumn, sizeByColumn, startDateFormat,
				endDateFormat, categoryColumn);

		for (Record record : this.records) {
			this.colorRegistry.getColorOf(record.getCategory());
		}
	}

	@Override
	protected LogService getLogger() {
		return this.logger;
	}

	@Override
	protected Data getInputData() {
		return this.inputData;
	}

	@Override
	protected String createPostScriptCode(CSVWriter csvWriter)
			throws PostScriptCreationException {

		PostscriptDocument postscriptDocument = new PostscriptDocument(csvWriter, this.records,
				this.shouldScaleOutput, this.areaColumn, this.categoryColumn, this.colorRegistry);

		return postscriptDocument.renderPostscript();
	}

}
