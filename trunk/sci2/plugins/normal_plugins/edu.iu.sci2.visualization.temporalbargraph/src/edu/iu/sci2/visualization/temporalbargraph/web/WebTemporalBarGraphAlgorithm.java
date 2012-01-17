package edu.iu.sci2.visualization.temporalbargraph.web;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithm;
import edu.iu.sci2.visualization.temporalbargraph.common.InvalidRecordException;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;

public class WebTemporalBarGraphAlgorithm extends
		AbstractTemporalBarGraphAlgorithm {

	private LogService logger;
	private Data inputData;
	private String labelColumn;
	private String startDateColumn;
	private String endDateColumn;
	private String sizeByColumn;
	private String startDateFormat;
	private String endDateFormat;
	private Boolean shouldScaleOutput;

	private List<Record> records;
	
	public WebTemporalBarGraphAlgorithm(Data inputData, Table inputTable,
			LogService logger, String labelColumn, String startDateColumn,
			String endDateColumn, String sizeByColumn, String startDateFormat,
			String endDateFormat, Boolean shouldScaleOutput) {
		
		this.logger = logger;
		this.inputData = inputData;
		this.labelColumn = labelColumn;
		this.startDateColumn = startDateColumn;
		this.endDateColumn = endDateColumn;
		this.sizeByColumn = sizeByColumn;
		this.startDateFormat = startDateFormat;
		this.endDateFormat = endDateFormat;
		this.shouldScaleOutput = shouldScaleOutput;
		
		
		this.records = readRecordsFromTable(inputTable, logger);
		
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
		String legendText = "Area size equals \"" + this.sizeByColumn + "\"";
		String footerText = "NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)";
		WebDocumentPostScriptCreator documentPostScriptCreator = new WebDocumentPostScriptCreator(csvWriter, this.records, shouldScaleOutput, legendText, footerText);
		
		String documentPostScript = documentPostScriptCreator.createPostScript();
		
		return documentPostScript;
	}
	
	private List<Record> readRecordsFromTable(Table table, LogService logger) {
		List<Record> workingRecordSet = new ArrayList<Record>();
		
		for(Iterator<?> rows = table.tuples(); rows.hasNext();) {
			Tuple row = (Tuple) rows.next();
			
			try {
				Record newRecord = new Record(
					row,
					this.labelColumn,
					this.startDateColumn,
					this.endDateColumn,
					this.sizeByColumn,
					this.startDateFormat,
					this.endDateFormat);
				
				workingRecordSet.add(newRecord);
			} catch (InvalidRecordException e) {
				logger.log(LogService.LOG_WARNING, e.getMessage(), e);
			}
		}

		return workingRecordSet;
	}

}
