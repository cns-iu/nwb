package edu.iu.scipolicy.visualization.horizontalbargraph.utility;

import java.util.ArrayList;
import java.util.Collection;

import org.cishell.utilities.osgi.logging.LogMessageHandler;
import org.cishell.utilities.osgi.logging.LogMessageHandler.MessageTypeDescriptor;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.exception.InvalidAmountException;

public class PreprocessedRecordInformation {
	public static final String RECORD_WITH_INVALID_LABEL = "record(s) with an invalid label";
	public static final int RECORD_WITH_INVALID_LABEL_MESSAGE_COUNT = 1;

	public static final String RECORD_WITH_INVALID_AMOUNT = "record(s) with an invalid amount";
	public static final int RECORD_WITH_INVALID_AMOUNT_MESSAGE_COUNT = 1;

	public static final String RECORD_WITH_INFINITE_OR_NAN_AMOUNT =
		"record(s) with an amount that is infinity or not a number";
	public static final int RECORD_WITH_INFINITE_OR_NAN_AMOUNT_MESSAGE_COUNT = 1;

	public static final String RECORD_WITH_LATER_START_DATE_THAN_END_DATE =
		"record(s) with a later start date than end date";
	public static final int RECORD_WITH_LATER_START_DATE_THAN_END_DATE_MESSAGE_COUNT = 1;

	private Collection<Integer> validRows = new ArrayList<Integer>();
	private boolean infinityOrNaNWasFound = false;
	private double maximumAmountFound = 0.0;

	private RecordLabelGenerator labelGenerator = new RecordLabelGenerator();

	private LogMessageHandler logMessageHandler;
	private MessageTypeDescriptor recordWithInvalidLabelType;
	private MessageTypeDescriptor recordWithInvalidAmountType;
	private MessageTypeDescriptor recordWithInfiniteOrNaNAmountType;
	private MessageTypeDescriptor recordWithLaterStartDateThanEndDateType;

	public PreprocessedRecordInformation(
			Table source,
			String labelKey,
			String amountKey,
			String startDateKey,
			String endDateKey,
			String startDateFormat,
			String endDateFormat,
			LogService logger) {
		this.logMessageHandler = new LogMessageHandler(logger);
		this.recordWithInvalidLabelType = logMessageHandler.addMessageType(
			RECORD_WITH_INVALID_LABEL,
			RECORD_WITH_INVALID_LABEL_MESSAGE_COUNT);
		this.recordWithInvalidAmountType = logMessageHandler.addMessageType(
			RECORD_WITH_INVALID_AMOUNT,
			RECORD_WITH_INVALID_AMOUNT_MESSAGE_COUNT);
		this.recordWithInfiniteOrNaNAmountType = logMessageHandler.addMessageType(
			RECORD_WITH_INFINITE_OR_NAN_AMOUNT,
			RECORD_WITH_INFINITE_OR_NAN_AMOUNT_MESSAGE_COUNT);
		this.recordWithLaterStartDateThanEndDateType = logMessageHandler.addMessageType(
			RECORD_WITH_LATER_START_DATE_THAN_END_DATE,
			RECORD_WITH_LATER_START_DATE_THAN_END_DATE_MESSAGE_COUNT);

		for (IntIterator rows = source.rows(); rows.hasNext(); ) {
			int rowIndex = rows.nextInt();
			Tuple row = source.getTuple(rowIndex);

			String label = this.labelGenerator.generateLabel(row, rowIndex, labelKey);

			if (this.labelGenerator.lastLabelExtractedHadProblem()) {
				this.labelGenerator.clearProblemStatus();

				String logMessage =
					"The label column in row " + rowIndex +
					" (attribute \"" + labelKey +
					"\") did not contain a valid value.  " +
					"Creating default label \"" + label + "\".";
				this.logMessageHandler.handleMessage(
					this.recordWithInvalidLabelType, LogService.LOG_WARNING, logMessage);
			}

			try {
				double amount = Utilities.extractAmount(row, amountKey);
				boolean amountIsInfinityOrNaN =
					(Double.isInfinite(amount) || Double.isNaN(amount));

				if (amountIsInfinityOrNaN) {
					String logMessage =
						"The row with label \"" + label +
						"\" has an the amount of " + amount + ".";
					this.logMessageHandler.handleMessage(
						this.recordWithInfiniteOrNaNAmountType,
						LogService.LOG_WARNING,
						logMessage);
				}

				DateTime startDate =
					Utilities.extractDate(row, startDateKey, startDateFormat).getDateTime();
				DateTime endDate =
					Utilities.extractDate(row, endDateKey, endDateFormat).getDateTime();
				boolean eitherDateIsNull = ((startDate == null) || (endDate == null));
				boolean datesAreUsable = (
					eitherDateIsNull ||
					(!eitherDateIsNull && (startDate.compareTo(endDate) <= 0)));

				if (datesAreUsable) {
					this.validRows.add(rowIndex);

					if (!amountIsInfinityOrNaN && (amount > this.maximumAmountFound)) {
						this.maximumAmountFound = amount;
					}
				} else {
					String logMessage =
						"The row with label \"" + label +
						"\" (attribute \"" + labelKey + "\") " +
						"has the start date \"" + startDate +
						"\" (attribute \"" + startDateKey + "\"), which is later than " +
						"the end date \"" + endDate +
						"\" (attribute \"" + endDateKey + "\").  Skipping.";
					this.logMessageHandler.handleMessage(
						this.recordWithLaterStartDateThanEndDateType, LogService.LOG_WARNING, logMessage);
				}
			} catch (InvalidAmountException e) {
				String logMessage =
					"The row with label \"" + label + "\" " +
					"has an invalid amount (attribute \"" + amountKey + "\").  Skipping";
				this.logMessageHandler.handleMessage(
					this.recordWithInvalidAmountType,
					LogService.LOG_WARNING,
					logMessage);
				
				continue;
			}
		}
	}

	public Collection<Integer> getValidRows() {
		return this.validRows;
	}

	public boolean infinityOrNaNWasFound() {
		return this.infinityOrNaNWasFound;
	}

	public double getMaximumAmountFound() {
		return this.maximumAmountFound;
	}
}