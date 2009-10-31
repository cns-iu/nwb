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
				logger.log(LogService.LOG_WARNING, logMessage);
				
				continue;
			}
			
			addRecordToCollector(
				recordCollection,
				label,
				startDateWrapper,
				endDateWrapper,
				amount,
				logger);
		}
	
		return recordCollection;
	}
	
	private String extractLabel(Tuple row, String labelKey) {
		String potentialLabel =
			StringUtilities.interpretObjectAsString(row.get(labelKey));

		if (
				(potentialLabel != null) &&
				!StringUtilities.isEmptyOrWhiteSpace(potentialLabel)) {
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
				StringUtilities.isEmptyOrWhiteSpace(
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
			double amount,
			LogService logger) {

		// TODO: Just make the logger a member, don't pass it everywhere.
		// (All of these not done yet.)
		if (!startDateWrapper.isSpecified()) {
			handleUnspecifiedStartDateCases(
				recordCollector,
				label,
				startDateWrapper,
				endDateWrapper,
				amount,
				logger);
		} else if (!startDateWrapper.isValid()) {
			handleInvalidStartDateCases(
				recordCollector,
				label,
				startDateWrapper,
				endDateWrapper,
				amount,
				logger);
		} else {
			handleValidStartDateCases(
				recordCollector,
				label,
				startDateWrapper,
				endDateWrapper,
				amount,
				logger);
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
			double amount,
			LogService logger) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (!endDateWrapper.isSpecified()) {
			String logMessage =
				logPrefix +
				"unspecified start and end dates." +
				NO_START_AND_END_DATES_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		/* TODO: Include what the invalid end date is
		 *  (and similarly elsewhere).
		 * (Not done yet).
		 */
		} else if (!endDateWrapper.isValid()) {
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
				label, endDateWrapper.getDateTime(), amount);
		}
	}
	
	private void handleInvalidStartDateCases(
			RecordCollection recordCollector,
			String label,
			DateTimeWrapper startDateWrapper,
			DateTimeWrapper endDateWrapper,
			double amount,
			LogService logger) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (!endDateWrapper.isSpecified()) {
			String logMessage =
				logPrefix +
				"an invalid start date and an unspecified end date." +
				NO_START_AND_END_DATES_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoDates(label, amount);
		} else if (!endDateWrapper.isValid()) {
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
				label, endDateWrapper.getDateTime(), amount);
		}
	}
	
	private void handleValidStartDateCases(
			RecordCollection recordCollector,
			String label,
			DateTimeWrapper startDateWrapper,
			DateTimeWrapper endDateWrapper,
			double amount,
			LogService logger) {
		String logPrefix = "The record \"" + label + "\" has ";
		
		if (!endDateWrapper.isSpecified()) {
			String logMessage =
				logPrefix + "an unspecified end date." + NO_END_DATE_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
			recordCollector.addRecordWithNoEndDate(
				label, startDateWrapper.getDateTime(), amount);
		} else if (!endDateWrapper.isValid()) {
			String logMessage =
				logPrefix + "an invalid end date." + NO_END_DATE_MESSAGE;
			logger.log(LogService.LOG_WARNING, logMessage);
		
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