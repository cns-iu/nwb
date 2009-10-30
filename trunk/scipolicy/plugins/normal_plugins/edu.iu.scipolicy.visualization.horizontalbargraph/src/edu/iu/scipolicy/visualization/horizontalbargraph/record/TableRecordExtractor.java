package edu.iu.scipolicy.visualization.horizontalbargraph.record;

import java.text.ParseException;
import java.util.Date;

import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.NumberUtilities;
import org.cishell.utilities.StringUtilities;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;

public class TableRecordExtractor {
	public static final String UNKNOWN_LABEL_PREFIX = "Unknown Label ";
	public static final DateTime UNSPECIFIED_DATE = new DateTime();
	public static final DateTime INVALID_DATE = new DateTime();
	
	public static final String NO_START_DATE_MESSAGE =
		"  Using the earliest start date found.";
	public static final String NO_END_DATE_MESSAGE =
		"  Using the latest end date found.";
	public static final String NO_START_AND_END_DATES_MESSAGE =
		"  Using the earliest start date and latest end date found.";
	
	private int unknownLabelCount = 0;
	
	public RecordCollection extractRecords(
			Table source,
			String labelKey,
			String startDateKey,
			String endDateKey,
			String amountKey,
			String startDateFormat,
			String endDateFormat,
			LogService logger) {
		RecordCollection recordCollection = new RecordCollection();
		
		/*
		 * I'm not using an iterator here so I can report the row number if a
		 *  problem occurs.
		 */
		//TODO: .rows() for an IntIterator
		for (int ii = 0; ii < source.getRowCount(); ii++) {
			Tuple row = source.getTuple(ii);
			
			String label = extractLabel(row, labelKey);
			DateTime startDate =
				extractDate(row, startDateKey, startDateFormat);
			DateTime endDate = extractDate(row, endDateKey, endDateFormat);
			double amount;
			
			try {
				amount = extractAmount(row, amountKey);
			} catch (NumberFormatException invalidAmountException) {
				String logMessage =
				// TODO (FIXED?): Row number ii
					"The row number " + ii +
					" has an invalid amount " +
					"(attribute \"" + amountKey + "\").  " +
					"Skipping.";
				logger.log(LogService.LOG_WARNING, logMessage);
				
				continue;
			}
			
			addRecordToCollector(
				recordCollection, label, startDate, endDate, amount, logger);
		}
	
		return recordCollection;
	}
	
	private String extractLabel(Tuple row, String labelKey) {
		String potentialLabel =
			StringUtilities.interpretObjectAsString(row.get(labelKey));
		
		//TODO: check for empty/whitespace string
		if (potentialLabel != null) {
			return potentialLabel;
		} else {
			String label = UNKNOWN_LABEL_PREFIX + this.unknownLabelCount;
			this.unknownLabelCount++;
			
			return label;
		}
	}
	
	private DateTime extractDate(
			Tuple row, String dateKey, String dateFormat) {
		Object potentialDate = row.get(dateKey);
		
		if ((potentialDate == null) ||
				StringUtilities.isEmptyOrWhiteSpace(
					potentialDate.toString())) {
			return UNSPECIFIED_DATE;
		}
		
		try {
			Date parsedDate = DateUtilities.interpretObjectAsDate(
				potentialDate, dateFormat, false);
			
			return new DateTime(parsedDate);
		} catch (ParseException unparsableDateException) {
			return INVALID_DATE;
		} catch (IllegalArgumentException invalidDateException) {
			return INVALID_DATE;
		}
	}
	
	private double extractAmount(Tuple row, String amountKey)
			throws NumberFormatException {
		//TODO: negative numbers? They'd probably mess everything up. Check the double parsing exception and that here, and throw custom exceptions with understandable messages, then just log those messages outside
		return NumberUtilities.interpretObjectAsDouble(
			row.get(amountKey)).doubleValue();
	}
	
	private void addRecordToCollector(
			RecordCollection recordCollector,
			String label,
			DateTime startDate,
			DateTime endDate,
			double amount,
			LogService logger) {
	
		//TODO: .equals()! magic is bad
		//TODO: handle by wrapping
		//TODO: just make the logger a member, don't pass it everywhere
		if (startDate == UNSPECIFIED_DATE) {
			handleUnspecifiedStartDateCases(
				recordCollector, label, startDate, endDate, amount, logger);
		} else if (startDate == INVALID_DATE) {
			handleInvalidStartDateCases(
				recordCollector, label, startDate, endDate, amount, logger);
		} else {
			handleValidStartDateCases(
				recordCollector, label, startDate, endDate, amount, logger);
		}
	}

	// TODO: Consider summarizing warnings or just giving one.
	//TODO: if just giving one, also say if there are more. Never be totally silent about further errors, just treat them as a group.
	private void handleUnspecifiedStartDateCases(
			RecordCollection recordCollector,
			String label,
			DateTime startDate,
			DateTime endDate,
			double amount,
			LogService logger) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (endDate == UNSPECIFIED_DATE) {
			String logMessage =
				logPrefix +
				"unspecified start and end dates." +
				NO_START_AND_END_DATES_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		//TODO: include what the invalid end date is (and similarly elsewhere)
		} else if (endDate == INVALID_DATE) {
			String logMessage =
				logPrefix +
				"an unspecified start date and an invalid end date." +
				NO_START_AND_END_DATES_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else {
			String logMessage =
				logPrefix + "an unspecified start date." +
				NO_START_DATE_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoStartDate(
				label, endDate, amount);
		}
	}
	
	private void handleInvalidStartDateCases(
			RecordCollection recordCollector,
			String label,
			DateTime startDate,
			DateTime endDate,
			double amount,
			LogService logger) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (endDate == UNSPECIFIED_DATE) {
			String logMessage =
				logPrefix +
				"an invalid start date and an unspecified end date." +
				NO_START_AND_END_DATES_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else if (endDate == INVALID_DATE) {
			String logMessage =
				logPrefix + "invalid start date and end dates." +
				NO_START_AND_END_DATES_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else {
			String logMessage =
				logPrefix + "an invalid start date." + NO_START_DATE_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoStartDate(
				label, endDate, amount);
		}
	}
	
	private void handleValidStartDateCases(
			RecordCollection recordCollector,
			String label,
			DateTime startDate,
			DateTime endDate,
			double amount,
			LogService logger) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (endDate == UNSPECIFIED_DATE) {
			String logMessage =
				logPrefix + "an unspecified end date." + NO_END_DATE_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoEndDate(
				label, startDate, amount);
		} else if (endDate == INVALID_DATE) {
			String logMessage =
				logPrefix + "an invalid end date." + NO_END_DATE_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoEndDate(
				label, startDate, amount);
		} else {
			recordCollector.addNormalRecord(
				label, startDate, endDate, amount);
		}
	}
}