package edu.iu.scipolicy.visualization.horizontalbargraph.utility;

import org.cishell.utilities.TableUtilities;

import prefuse.data.Tuple;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.exception.InvalidAmountException;

public class Utilities {
	public static double extractAmount(Tuple row, String columnName)
			throws InvalidAmountException {
		try {
			double amount = TableUtilities.extractDoubleFromCell(row, columnName);

			if (amount < 0.0) {
				String exceptionMessage = "The tuple " + row + " has a negative amount.";

				throw new InvalidAmountException(exceptionMessage);
			}

			return amount;
		} catch (NumberFormatException e) {
			throw new InvalidAmountException(e);
		}
	}
}