package edu.iu.scipolicy.visualization.horizontalbargraph.utility;

import org.cishell.utilities.osgi.logging.LogMessageHandler;
import org.cishell.utilities.osgi.logging.LogMessageHandler.MessageTypeDescriptor;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.exception.InvalidAmountException;

public class PreprocessedRecordInformation {
	public static final String RECORD_WITH_INVALID_AMOUNT = "record(s) with an invalid amount";
	public static final int RECORD_WITH_INVALID_AMOUNT_MESSAGE_COUNT = 1;

	private boolean infinityOrNaNWasFound = false;
	private double maximumAmountFound = 0.0;

	private LogMessageHandler logMessageHandler;
	private MessageTypeDescriptor recordWithInvalidAmountType;

	public PreprocessedRecordInformation(Table source, String amountKey, LogService logger) {
		this.logMessageHandler = new LogMessageHandler(logger);
		this.recordWithInvalidAmountType = logMessageHandler.addMessageType(
			RECORD_WITH_INVALID_AMOUNT,
			RECORD_WITH_INVALID_AMOUNT_MESSAGE_COUNT);

		for (IntIterator rows = source.rows(); rows.hasNext(); ) {
			int rowIndex = rows.nextInt();
			Tuple row = source.getTuple(rowIndex);

			try {
				double amount = Utilities.extractAmount(row, amountKey);

				if (!Double.isInfinite(amount) && !Double.isNaN(amount)) {
					if (amount > this.maximumAmountFound) {
						this.maximumAmountFound = amount;
					}
				} else {
					this.infinityOrNaNWasFound = true;

					throw new InvalidAmountException();
				}
			} catch (InvalidAmountException e) {
				String logMessage =
					"The row number " + rowIndex +
					" has an invalid amount " +
					"(attribute \"" + amountKey + "\").  " +
					"Skipping.";
				this.logMessageHandler.handleMessage(
					this.recordWithInvalidAmountType,
					LogService.LOG_WARNING,
					logMessage);
				
				continue;
			}
		}
	}

	public boolean infinityOrNaNWasFound() {
		return this.infinityOrNaNWasFound;
	}

	public double getMaximumAmountFound() {
		return this.maximumAmountFound;
	}
}