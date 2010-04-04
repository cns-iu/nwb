package edu.iu.scipolicy.visualization.horizontalbargraph.utility;

import java.util.ArrayList;
import java.util.Collection;

import org.cishell.utilities.Pair;
import org.cishell.utilities.osgi.logging.LogMessageHandler;
import org.cishell.utilities.osgi.logging.LogMessageHandler.MessageTypeDescriptor;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.scipolicy.visualization.horizontalbargraph.Metadata;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.exception.BadDatasetException;
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
	private boolean validAmountFound = false;
	private boolean validDateFound = false;

	public PreprocessedRecordInformation(
			Table source, Metadata metadata, LogService logger) throws BadDatasetException {
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
			handleRow(source, rows, metadata);
		}

		if (!this.validAmountFound) {
			String exceptionMessage =
				"Your dataset cannot be visualized because no records contain a valid amount.";
			throw new BadDatasetException(exceptionMessage);
		}

		if (!this.validDateFound) {
			String exceptionMessage =
				"Your dataset cannot be visualized because no records contain a valid date.";
			throw new BadDatasetException(exceptionMessage);
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

	private void handleRow(Table source, IntIterator rows, Metadata metadata) {
		int rowIndex = rows.nextInt();
		Tuple row = source.getTuple(rowIndex);
		String label = handleLabel(row, rowIndex, metadata);

		try {
			Pair<Double, Boolean> handleAmountResult = handleAmount(label, row, metadata);
			double amount = handleAmountResult.getFirstObject();
			boolean amountIsInfinityOrNaN = handleAmountResult.getSecondObject();

			handleStartAndEndDates(label, amount, amountIsInfinityOrNaN, row, rowIndex, metadata);
		} catch (InvalidAmountException e) {
			String logMessage =
				"The row with label \"" + label + "\" " +
				"has an invalid amount (attribute \"" +
				metadata.getSizeByColumn() + "\").  Skipping";
			this.logMessageHandler.handleMessage(
				this.recordWithInvalidAmountType, LogService.LOG_WARNING, logMessage);
		}
	}

	private String handleLabel(Tuple row, int rowIndex, Metadata metadata) {
		String label = this.labelGenerator.generateLabel(row, rowIndex, metadata.getLabelColumn());

		if (this.labelGenerator.lastLabelExtractedHadProblem()) {
			this.labelGenerator.clearProblemStatus();

			String logMessage =
				"The label column in row " + rowIndex +
				" (attribute \"" + metadata.getLabelColumn() +
				"\") did not contain a valid value.  " +
				"Creating default label \"" + label + "\".";
			this.logMessageHandler.handleMessage(
				this.recordWithInvalidLabelType, LogService.LOG_WARNING, logMessage);
		}

		return label;
	}

	private Pair<Double, Boolean> handleAmount(String label, Tuple row, Metadata metadata)
			throws InvalidAmountException {
		double amount = Utilities.extractAmount(row, metadata.getSizeByColumn());
		boolean amountIsInfinityOrNaN = (Double.isInfinite(amount) || Double.isNaN(amount));

		if (amountIsInfinityOrNaN) {
			String logMessage =
				"The row with label \"" + label + "\" has an the amount of " + amount + ".";
			this.logMessageHandler.handleMessage(
				this.recordWithInfiniteOrNaNAmountType, LogService.LOG_WARNING, logMessage);
		} else {
			this.validAmountFound = true;
		}

		return new Pair<Double, Boolean>(amount, amountIsInfinityOrNaN);
	}

	private void handleStartAndEndDates(
			String label,
			double amount,
			boolean amountIsInfinityOrNaN,
			Tuple row,
			int rowIndex,
			Metadata metadata) {
		DateTime startDate = Utilities.extractDate(
			row, metadata.getStartDateColumn(), metadata.getDateFormat()).getDateTime();
		DateTime endDate = Utilities.extractDate(
			row, metadata.getEndDateColumn(), metadata.getDateFormat()).getDateTime();
		boolean startDateIsNull = (startDate == null);
		boolean endDateIsNull = (endDate == null);
		boolean eitherDateIsNull = (startDateIsNull || endDateIsNull);
		boolean datesAreUsable =
			(eitherDateIsNull || (!eitherDateIsNull && (startDate.compareTo(endDate) <= 0)));

		if (!startDateIsNull || !endDateIsNull) {
			this.validDateFound = true;
		}

		if (datesAreUsable) {
			this.validRows.add(rowIndex);

			if (!amountIsInfinityOrNaN && (amount > this.maximumAmountFound)) {
				this.maximumAmountFound = amount;
			}
		} else {
			String logMessage =
				"The row with label \"" + label +
				"\" (attribute \"" + metadata.getLabelColumn() + "\") " +
				"has the start date \"" + startDate +
				"\" (attribute \"" + metadata.getStartDateColumn() +
					"\"), which is later than " +
				"the end date \"" + endDate +
				"\" (attribute \"" + metadata.getEndDateColumn() + "\").  Skipping.";
			this.logMessageHandler.handleMessage(
				this.recordWithLaterStartDateThanEndDateType, LogService.LOG_WARNING, logMessage);
		}
	}
}