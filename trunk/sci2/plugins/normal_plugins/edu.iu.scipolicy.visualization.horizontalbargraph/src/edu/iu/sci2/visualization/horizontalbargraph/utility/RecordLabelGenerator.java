package edu.iu.sci2.visualization.horizontalbargraph.utility;

import org.cishell.utilities.StringUtilities;

import prefuse.data.Tuple;
import edu.iu.sci2.visualization.horizontalbargraph.record.exception.InvalidLabelException;

public class RecordLabelGenerator {
	public static final String UNKNOWN_LABEL_PREFIX = "Unknown Label ";

	private boolean lastLabelExtractedHadProblem = false;
	private int unknownLabelCount = 0;

	public boolean lastLabelExtractedHadProblem() {
		return this.lastLabelExtractedHadProblem;
	}

	public void clearProblemStatus() {
		this.lastLabelExtractedHadProblem = false;
	}

	public String generateLabel(Tuple tableRow, int rowIndex, String labelKey) {
		this.lastLabelExtractedHadProblem = false;

		try {
			return extractLabel(tableRow, rowIndex, labelKey);
		} catch (InvalidLabelException e) {
			String label =
				Utilities.postscriptEscape(UNKNOWN_LABEL_PREFIX + this.unknownLabelCount);
			this.lastLabelExtractedHadProblem = true;
			this.unknownLabelCount++;

			return label;
		}
	}

	private static String extractLabel(
			Tuple row, int rowIndexInCaseOfError, String labelKey) throws InvalidLabelException {
		String potentialLabel =
			StringUtilities.interpretObjectAsString(row.get(labelKey));

		if ((potentialLabel != null) &&
				!StringUtilities.isNull_Empty_OrWhitespace(potentialLabel)) {
			return Utilities.postscriptEscape(potentialLabel);
		} else {
			String exceptionMessage =
				"A valid could not be extracted out of row " + rowIndexInCaseOfError + ".";
			throw new InvalidLabelException(exceptionMessage);
		}
	}
}