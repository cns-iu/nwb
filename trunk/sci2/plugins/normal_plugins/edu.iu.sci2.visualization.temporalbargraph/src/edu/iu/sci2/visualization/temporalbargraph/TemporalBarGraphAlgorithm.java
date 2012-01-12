package edu.iu.sci2.visualization.temporalbargraph;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithm;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;

import prefuse.data.Table;

import au.com.bytecode.opencsv.CSVWriter;

public class TemporalBarGraphAlgorithm extends
		AbstractTemporalBarGraphAlgorithm implements Algorithm {
    
	private Data inputData;
    private Table inputTable;
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
	private String query;
	
	public TemporalBarGraphAlgorithm(
    		Data inputData,
    		Table inputTable,
    		LogService logger,
    		String labelColumn,
    		String startDateColumn,
    		String endDateColumn,
    		String sizeByColumn,
    		String startDateFormat,
    		String endDateFormat,
    		String query,
    		double pageWidth,
    		double pageHeight,
    		boolean shouldScaleOutput) {
        this.inputData = inputData;
        this.inputTable = inputTable;
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
    }
	
	//TODO I'd like this to be private, but this is the best I can do.  Is it enough?
	protected String createPostScriptCode(CSVWriter csvWriter)
			throws PostScriptCreationException {
		
		DocumentPostScriptCreator postScriptCreator = new DocumentPostScriptCreator(
				labelColumn, startDateColumn, endDateColumn, sizeByColumn,
				startDateFormat, endDateFormat, query, pageWidth, pageHeight,
				shouldScaleOutput);

		String postScriptCode = postScriptCreator.createPostScript(this.inputTable, this.logger, csvWriter);

		return postScriptCode;
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
