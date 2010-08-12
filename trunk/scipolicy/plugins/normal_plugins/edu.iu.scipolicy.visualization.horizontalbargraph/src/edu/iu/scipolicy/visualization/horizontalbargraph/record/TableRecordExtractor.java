package edu.iu.scipolicy.visualization.horizontalbargraph.record;

import org.cishell.utilities.osgi.logging.LogMessageHandler;
import org.cishell.utilities.osgi.logging.LogMessageHandler.MessageTypeDescriptor;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.scipolicy.visualization.horizontalbargraph.DateTimeWrapper;
import edu.iu.scipolicy.visualization.horizontalbargraph.HorizontalBarGraphAlgorithm;
import edu.iu.scipolicy.visualization.horizontalbargraph.Metadata;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.exception.InvalidAmountException;
import edu.iu.scipolicy.visualization.horizontalbargraph.utility.PreprocessedRecordInformation;
import edu.iu.scipolicy.visualization.horizontalbargraph.utility.RecordLabelGenerator;
import edu.iu.scipolicy.visualization.horizontalbargraph.utility.Utilities;

public class TableRecordExtractor {
	public static final String NO_START_DATE_MESSAGE =
		"  Using the earliest start date found.";
	public static final String NO_END_DATE_MESSAGE =
		"  Using the latest end date found.";
	public static final String NO_START_AND_END_DATES_MESSAGE =
		"  Using the earliest start date and latest end date found.";

	public static final String RECORD_WITHOUT_VALID_START_DATE =
		"record(s) without a valid start date";
	public static final int RECORD_WITHOUT_VALID_START_DATE_MESSAGE_COUNT = 1;
	
	public static final String RECORD_WITHOUT_VALID_END_DATE =
		"record(s) without a valid end date";
	public static final int RECORD_WITHOUT_VALID_END_DATE_MESSAGE_COUNT = 1;
	
	public static final String RECORD_WITHOUT_VALID_START_AND_END_DATES =
		"record(s) without valid start and end dates";
	public static final int
		RECORD_WITHOUT_VALID_START_AND_END_DATES_MESSAGE_COUNT = 1;
	
	private RecordLabelGenerator labelGenerator = new RecordLabelGenerator();
	
	private LogMessageHandler logMessageHandler;
	private MessageTypeDescriptor recordWithoutValidStartDateType;
	private MessageTypeDescriptor recordWithoutValidEndDateType;
	private MessageTypeDescriptor recordWithoutValidStartOrEndDateType;
	
	public TableRecordExtractor(LogService logger) {
		// TODO: Move this stuff to PreprocessedRecordInformation.
		this.logMessageHandler = new LogMessageHandler(logger);
		this.recordWithoutValidStartDateType = logMessageHandler.addMessageType(
			RECORD_WITHOUT_VALID_START_DATE,
			RECORD_WITHOUT_VALID_START_DATE_MESSAGE_COUNT);
		this.recordWithoutValidEndDateType = logMessageHandler.addMessageType(
			RECORD_WITHOUT_VALID_END_DATE,
			RECORD_WITHOUT_VALID_END_DATE_MESSAGE_COUNT);
		this.recordWithoutValidStartOrEndDateType = logMessageHandler.addMessageType(
			RECORD_WITHOUT_VALID_START_AND_END_DATES,
			RECORD_WITHOUT_VALID_START_AND_END_DATES_MESSAGE_COUNT);
	}
	
	public RecordCollection extractRecords(
			PreprocessedRecordInformation recordInformation,
			Table source,
			Metadata metadata,
			LogService logger) {
		RecordCollection recordCollection =
			new RecordCollection(recordInformation, metadata.getScalingFunction());

		for (Integer rowIndex : recordInformation.getValidRows()) {
			Tuple row = source.getTuple(rowIndex);

			String label =
				this.labelGenerator.generateLabel(row, rowIndex, metadata.getLabelColumn()).trim();
			String colorizedBy = getColorizedBy(row, rowIndex, metadata.getColorizedByColumn());
			DateTimeWrapper startDateWrapper = Utilities.extractDate(
				row, metadata.getStartDateColumn(), metadata.getDateFormat());
			DateTimeWrapper endDateWrapper =
				Utilities.extractDate(row, metadata.getEndDateColumn(), metadata.getDateFormat());

			try {
				double amount = Utilities.extractAmount(row, metadata.getSizeByColumn());

				addRecordToCollector(
					recordCollection,
					label,
					colorizedBy,
					startDateWrapper,
					endDateWrapper,
					amount);
			} catch (InvalidAmountException e) {
				/*
				 * Needing to catch the InvalidAmountException is an artifact of using
				 *  Utilities.extractAmount().  PreprocessedRecordInformation handles printing a
				 *  warning message to the user when this happens.
				 */ 
				continue;
			}
		}
		
		this.logMessageHandler.printOverloadedMessageTypes(LogService.LOG_WARNING);
	
		return recordCollection;
	}
		
	private String getColorizedBy(Tuple row, int rowIndex, String labelColumn) {
		
		/* return null if NO_COLORIZED_BY is chosen because a special string might appear in the data */
		if(labelColumn.equals(HorizontalBarGraphAlgorithm.NO_COLORIZED_BY)) {
			return null;
		}
		
		return this.labelGenerator.generateLabel(
			 	row, rowIndex, labelColumn.trim());
	}
	
	private void addRecordToCollector(
			RecordCollection recordCollector,
			String label,
			String colorizedBy,
			DateTimeWrapper startDateWrapper,
			DateTimeWrapper endDateWrapper,
			double amount) {
		if (!startDateWrapper.isSpecified()) {
			handleUnspecifiedStartDateCases(
				recordCollector, label, colorizedBy, startDateWrapper, endDateWrapper, amount);
		} else if (!startDateWrapper.isValid()) {
			handleInvalidStartDateCases(
				recordCollector, label, colorizedBy, startDateWrapper, endDateWrapper, amount);
		} else {
			handleValidStartDateCases(
				recordCollector, label, colorizedBy, startDateWrapper, endDateWrapper, amount);
		}
	}

	private void handleUnspecifiedStartDateCases(
			RecordCollection recordCollector,
			String label,
			String colorizedBy,
			DateTimeWrapper startDateWrapper,
			DateTimeWrapper endDateWrapper,
			double amount) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (!endDateWrapper.isSpecified()) {
			String logMessage =
				logPrefix + "unspecified start and end dates." + NO_START_AND_END_DATES_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartOrEndDateType, LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoDates(label, colorizedBy, amount);
		} else if (!endDateWrapper.isValid()) {
			String logMessage =
				logPrefix +
				"an unspecified start date and an invalid end date." +
				NO_START_AND_END_DATES_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartOrEndDateType, LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoDates(label, colorizedBy, amount);
		} else {
			String logMessage = logPrefix + "an unspecified start date." + NO_START_DATE_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartDateType, LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoStartDate(label, colorizedBy, endDateWrapper.getDateTime(), amount);
		}
	}
	
	private void handleInvalidStartDateCases(
			RecordCollection recordCollector,
			String label,
			String colorizedBy,
			DateTimeWrapper startDateWrapper,
			DateTimeWrapper endDateWrapper,
			double amount) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (!endDateWrapper.isSpecified()) {
			String logMessage =
				logPrefix +
				"an invalid start date and an unspecified end date." +
				NO_START_AND_END_DATES_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartOrEndDateType, LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoDates(label, colorizedBy, amount);
		} else if (!endDateWrapper.isValid()) {
			String logMessage =
				logPrefix + "invalid start date and end dates." + NO_START_AND_END_DATES_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartOrEndDateType, LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoDates(label, colorizedBy, amount);
		} else {
			String logMessage = logPrefix + "an invalid start date." + NO_START_DATE_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartDateType, LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoStartDate(label, colorizedBy, endDateWrapper.getDateTime(), amount);
		}
	}
	
	private void handleValidStartDateCases(
			RecordCollection recordCollector,
			String label,
			String colorizedBy,
			DateTimeWrapper startDateWrapper,
			DateTimeWrapper endDateWrapper,
			double amount) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (!endDateWrapper.isSpecified()) {
			String logMessage = logPrefix + "an unspecified end date." + NO_END_DATE_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidEndDateType, LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoEndDate(
				label, colorizedBy, startDateWrapper.getDateTime(), amount);
		} else if (!endDateWrapper.isValid()) {
			String logMessage =
				logPrefix + "an invalid end date." + NO_END_DATE_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidEndDateType, LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoEndDate(label, colorizedBy, startDateWrapper.getDateTime(), amount);
		} else {
			recordCollector.addNormalRecord(
				label, colorizedBy, startDateWrapper.getDateTime(), endDateWrapper.getDateTime(), amount);
		}
	}
}