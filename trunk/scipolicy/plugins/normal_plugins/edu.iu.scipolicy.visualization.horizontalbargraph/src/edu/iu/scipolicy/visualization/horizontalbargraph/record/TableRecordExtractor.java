package edu.iu.scipolicy.visualization.horizontalbargraph.record;

import org.cishell.utilities.osgi.logging.LogMessageHandler;
import org.cishell.utilities.osgi.logging.LogMessageHandler.MessageTypeDescriptor;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.scipolicy.visualization.horizontalbargraph.DateTimeWrapper;
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
		this.recordWithoutValidStartDateType =
			logMessageHandler.addMessageType(
				RECORD_WITHOUT_VALID_START_DATE,
				RECORD_WITHOUT_VALID_START_DATE_MESSAGE_COUNT);
		this.recordWithoutValidEndDateType =
			logMessageHandler.addMessageType(
				RECORD_WITHOUT_VALID_END_DATE,
				RECORD_WITHOUT_VALID_END_DATE_MESSAGE_COUNT);
		this.recordWithoutValidStartOrEndDateType =
			logMessageHandler.addMessageType(
				RECORD_WITHOUT_VALID_START_AND_END_DATES,
				RECORD_WITHOUT_VALID_START_AND_END_DATES_MESSAGE_COUNT);
	}
	
	public RecordCollection extractRecords(
			Table source,
			Metadata metadata,
//			String labelKey,
//			String startDateKey,
//			String endDateKey,
//			String amountKey,
//			String startDateFormat,
//			String endDateFormat,
			LogService logger) {
		PreprocessedRecordInformation recordInformation = new PreprocessedRecordInformation(
			source,
			metadata,
//			labelKey,
//			amountKey,
//			startDateKey,
//			endDateKey,
//			startDateFormat,
//			endDateFormat,
			logger);
		RecordCollection recordCollection = new RecordCollection(recordInformation);

//		for (Integer rowIndex : recordInformation.getValidRows()) {
//			System.err.println("Row: " + rowIndex);
//		}
		//IntIterator rows = source.rows();
		for (Integer rowIndex : recordInformation.getValidRows()) {
//		for (IntIterator rows = source.rows(); rows.hasNext(); ) {
//			System.err.println("IntIterator row " + rows.nextInt());
//			System.err.println("Row " + rowIndex);
//			int rowIndex = rows.nextInt();
			Tuple row = source.getTuple(rowIndex);

			String label = this.labelGenerator.generateLabel(
				row, rowIndex, metadata.getLabelColumn());
//			String label = this.labelGenerator.generateLabel(row, rowIndex, labelKey);
			
			DateTimeWrapper startDateWrapper = Utilities.extractDate(
				row, metadata.getStartDateColumn(), metadata.getDateFormat());
//				Utilities.extractDate(row, startDateKey, startDateFormat);
			DateTimeWrapper endDateWrapper =
				Utilities.extractDate(row, metadata.getEndDateColumn(), metadata.getDateFormat());
//				Utilities.extractDate(row, endDateKey, endDateFormat);

			try {
				double amount = Utilities.extractAmount(row, metadata.getAmountColumn());
//				double amount = Utilities.extractAmount(row, amountKey);
				addRecordToCollector(
					recordCollection, label, startDateWrapper, endDateWrapper, amount);
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
	
	private void addRecordToCollector(
			RecordCollection recordCollector,
			String label,
			DateTimeWrapper startDateWrapper,
			DateTimeWrapper endDateWrapper,
			double amount) {
		if (!startDateWrapper.isSpecified()) {
			handleUnspecifiedStartDateCases(
				recordCollector,
				label,
				startDateWrapper,
				endDateWrapper,
				amount);
		} else if (!startDateWrapper.isValid()) {
			handleInvalidStartDateCases(
				recordCollector,
				label,
				startDateWrapper,
				endDateWrapper,
				amount);
		} else {
			handleValidStartDateCases(
				recordCollector,
				label,
				startDateWrapper,
				endDateWrapper,
				amount);
		}
	}

	private void handleUnspecifiedStartDateCases(
			RecordCollection recordCollector,
			String label,
			DateTimeWrapper startDateWrapper,
			DateTimeWrapper endDateWrapper,
			double amount) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (!endDateWrapper.isSpecified()) {
			String logMessage =
				logPrefix +
				"unspecified start and end dates." +
				NO_START_AND_END_DATES_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartOrEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else if (!endDateWrapper.isValid()) {
			String logMessage =
				logPrefix +
				"an unspecified start date and an invalid end date." +
				NO_START_AND_END_DATES_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartOrEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else {
			String logMessage =
				logPrefix + "an unspecified start date." +
				NO_START_DATE_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoStartDate(
				label, endDateWrapper.getDateTime(), amount);
		}
	}
	
	private void handleInvalidStartDateCases(
			RecordCollection recordCollector,
			String label,
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
				this.recordWithoutValidStartOrEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else if (!endDateWrapper.isValid()) {
			String logMessage =
				logPrefix + "invalid start date and end dates." +
				NO_START_AND_END_DATES_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartOrEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else {
			String logMessage =
				logPrefix + "an invalid start date." + NO_START_DATE_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidStartDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoStartDate(label, endDateWrapper.getDateTime(), amount);
		}
	}
	
	private void handleValidStartDateCases(
			RecordCollection recordCollector,
			String label,
			DateTimeWrapper startDateWrapper,
			DateTimeWrapper endDateWrapper,
			double amount) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (!endDateWrapper.isSpecified()) {
			String logMessage =
				logPrefix + "an unspecified end date." + NO_END_DATE_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoEndDate(
				label, startDateWrapper.getDateTime(), amount);
		} else if (!endDateWrapper.isValid()) {
			String logMessage =
				logPrefix + "an invalid end date." + NO_END_DATE_MESSAGE;
			this.logMessageHandler.handleMessage(
				this.recordWithoutValidEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoEndDate(
				label, startDateWrapper.getDateTime(), amount);
		} else {
			recordCollector.addNormalRecord(
				label,
				startDateWrapper.getDateTime(),
				endDateWrapper.getDateTime(),
				amount);
		}
	}
}