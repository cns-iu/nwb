package edu.iu.sci2.visualization.horizontalbargraph.utility;

import java.text.ParseException;
import java.util.Date;

import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.TableUtilities;
import org.joda.time.DateTime;

import prefuse.data.Tuple;
import edu.iu.sci2.visualization.horizontalbargraph.DateTimeWrapper;
import edu.iu.sci2.visualization.horizontalbargraph.record.exception.InvalidAmountException;

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

	public static DateTimeWrapper extractDate(Tuple row, String dateKey, String dateFormat) {
		Object potentialDate = row.get(dateKey);

		if ((potentialDate == null) ||
				StringUtilities.isEmptyOrWhitespace(potentialDate.toString())) {
			return DateTimeWrapper.createUnspecifiedDateTimeWrapper();
		}
		
		try {
			Date parsedDate =
				DateUtilities.interpretObjectAsDate(potentialDate, dateFormat, false);
			
			return DateTimeWrapper.createValidDateTimeWrapper(new DateTime(parsedDate));
		} catch (ParseException unparsableDateException) {
			return DateTimeWrapper.createInvalidDateTimeWrapper();
		} catch (IllegalArgumentException invalidDateException) {
			return DateTimeWrapper.createInvalidDateTimeWrapper();
		}
	}

	public static String postscriptEscape(String rawReference) {
		return rawReference.replace("\\", "\\\\").replace(")","\\)").replace("(", "\\(");
	}
}