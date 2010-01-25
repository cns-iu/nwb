package edu.iu.scipolicy.visualization.horizontalbargraph.record;

import java.text.ParseException;
import java.util.Date;

import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.NumberUtilities;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.osgi.logging.LogMessageHandler;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.scipolicy.visualization.horizontalbargraph.DateTimeWrapper;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.exception.InvalidAmountException;

public class TableRecordExtractor {
	public static final String UNKNOWN_LABEL_PREFIX = "Unknown Label ";
	
	public static final String NO_START_DATE_MESSAGE =
		"  Using the earliest start date found.";
	public static final String NO_END_DATE_MESSAGE =
		"  Using the latest end date found.";
	public static final String NO_START_AND_END_DATES_MESSAGE =
		"  Using the earliest start date and latest end date found.";
	
	public static final String RECORD_WITH_INVALID_AMOUNT =
		"record(s) with an invalid amount";
	public static final int RECORD_WITH_INVALID_AMOUNT_MESSAGE_COUNT = 1;
	
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
	
	private int unknownLabelCount = 0;
	
	LogMessageHandler logMessageHandler;
	LogMessageHandler.MessageTypeIndicator recordWithInvalidAmountType;
	LogMessageHandler.MessageTypeIndicator recordWithoutValidStartDateType;
	LogMessageHandler.MessageTypeIndicator recordWithoutValidEndDateType;
	LogMessageHandler.MessageTypeIndicator
		recordWithoutValidStartAndEndDateType;
	
	public TableRecordExtractor(LogService logger) {
		this.logMessageHandler = new LogMessageHandler(logger);
		this.recordWithInvalidAmountType = logMessageHandler.addMessageType(
			RECORD_WITH_INVALID_AMOUNT,
			RECORD_WITH_INVALID_AMOUNT_MESSAGE_COUNT);
		this.recordWithoutValidStartDateType =
			logMessageHandler.addMessageType(
				RECORD_WITHOUT_VALID_START_DATE,
				RECORD_WITHOUT_VALID_START_DATE_MESSAGE_COUNT);
		this.recordWithoutValidEndDateType =
			logMessageHandler.addMessageType(
				RECORD_WITHOUT_VALID_END_DATE,
				RECORD_WITHOUT_VALID_END_DATE_MESSAGE_COUNT);
		this.recordWithoutValidStartAndEndDateType =
			logMessageHandler.addMessageType(
				RECORD_WITHOUT_VALID_START_AND_END_DATES,
				RECORD_WITHOUT_VALID_START_AND_END_DATES_MESSAGE_COUNT);
	}
	
	public RecordCollection extractRecords(
			Table source,
			String labelKey,
			String startDateKey,
			String endDateKey,
			String amountKey,
			String startDateFormat,
			String endDateFormat) {
		RecordCollection recordCollection = new RecordCollection();

		for (IntIterator rows = source.rows(); rows.hasNext(); ) {
			int rowIndex = rows.nextInt();
			Tuple row = source.getTuple(rowIndex);
			
			String label = extractLabel(row, labelKey);
			DateTimeWrapper startDateWrapper =
				extractDate(row, startDateKey, startDateFormat);
			DateTimeWrapper endDateWrapper =
				extractDate(row, endDateKey, endDateFormat);
			double amount;
			
			try {
				amount = extractAmount(row, amountKey);
			} catch (InvalidAmountException invalidAmountException) {
				String logMessage =
					"The row number " + rowIndex +
					" has an invalid amount " +
					"(attribute \"" + amountKey + "\").  " +
					"Skipping.";
				this.logMessageHandler.logMessage(
					this.recordWithInvalidAmountType,
					LogService.LOG_WARNING,
					logMessage);
				
				continue;
			}
			
			addRecordToCollector(
				recordCollection,
				label,
				startDateWrapper,
				endDateWrapper,
				amount);
		}
		
		this.logMessageHandler.printOverloadedMessageTypes(
			LogService.LOG_WARNING);
	
		return recordCollection;
	}
	
	private String extractLabel(Tuple row, String labelKey) {
		String potentialLabel =
			StringUtilities.interpretObjectAsString(row.get(labelKey));

		if (
				(potentialLabel != null) &&
				!StringUtilities.isEmptyOrWhitespace(potentialLabel)) {
			return potentialLabel;
		} else {
			String label = UNKNOWN_LABEL_PREFIX + this.unknownLabelCount;
			this.unknownLabelCount++;
			
			return label;
		}
	}
	
	private DateTimeWrapper extractDate(
			Tuple row, String dateKey, String dateFormat) {
		Object potentialDate = row.get(dateKey);
		
		if ((potentialDate == null) ||
				StringUtilities.isEmptyOrWhitespace(
					potentialDate.toString())) {

			return DateTimeWrapper.createUnspecifiedDateTimeWrapper();
		}
		
		try {
			Date parsedDate = DateUtilities.interpretObjectAsDate(
				potentialDate, dateFormat, false);
			
			return DateTimeWrapper.createValidDateTimeWrapper(
				new DateTime(parsedDate));
		} catch (ParseException unparsableDateException) {
			return DateTimeWrapper.createInvalidDateTimeWrapper();
		} catch (IllegalArgumentException invalidDateException) {
			return DateTimeWrapper.createInvalidDateTimeWrapper();
		}
	}
	
	private double extractAmount(Tuple row, String amountKey)
			throws InvalidAmountException {
		try {
			double amount = NumberUtilities.interpretObjectAsDouble(
				row.get(amountKey)).doubleValue();
			
			if (amount < 0.0) {
				String exceptionMessage =
					"The tuple " + row + " has a negative amount.";

				throw new InvalidAmountException(exceptionMessage);
			}
			
			return amount;
		} catch (NumberFormatException numberFormatException) {
			throw new InvalidAmountException(numberFormatException);
		}
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

	// TODO: Consider summarizing warnings or just giving one.
	/* TODO: If just giving one, also say if there are more.
	 * Never be totally silent about further errors, just treat them as
	 *  a group.
	 * (Not done yet.)
	 */
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
			this.logMessageHandler.logMessage(
				this.recordWithoutValidStartAndEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else if (!endDateWrapper.isValid()) {
			String logMessage =
				logPrefix +
				"an unspecified start date and an invalid end date." +
				NO_START_AND_END_DATES_MESSAGE;
			this.logMessageHandler.logMessage(
				this.recordWithoutValidStartAndEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else {
			String logMessage =
				logPrefix + "an unspecified start date." +
				NO_START_DATE_MESSAGE;
			this.logMessageHandler.logMessage(
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
			this.logMessageHandler.logMessage(
				this.recordWithoutValidStartAndEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else if (!endDateWrapper.isValid()) {
			String logMessage =
				logPrefix + "invalid start date and end dates." +
				NO_START_AND_END_DATES_MESSAGE;
			this.logMessageHandler.logMessage(
				this.recordWithoutValidStartAndEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else {
			String logMessage =
				logPrefix + "an invalid start date." + NO_START_DATE_MESSAGE;
			this.logMessageHandler.logMessage(
				this.recordWithoutValidStartDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoStartDate(
				label, endDateWrapper.getDateTime(), amount);
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
			this.logMessageHandler.logMessage(
				this.recordWithoutValidEndDateType,
				LogService.LOG_WARNING,
				logMessage);
		
			recordCollector.addRecordWithNoEndDate(
				label, startDateWrapper.getDateTime(), amount);
		} else if (!endDateWrapper.isValid()) {
			String logMessage =
				logPrefix + "an invalid end date." + NO_END_DATE_MESSAGE;
			this.logMessageHandler.logMessage(
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