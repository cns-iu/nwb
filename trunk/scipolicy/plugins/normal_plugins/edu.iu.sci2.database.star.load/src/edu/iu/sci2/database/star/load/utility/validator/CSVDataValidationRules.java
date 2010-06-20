package edu.iu.sci2.database.star.load.utility.validator;

import edu.iu.sci2.database.star.load.utility.validator.exception.CSVBodyValidationException;
import edu.iu.sci2.database.star.load.utility.validator.exception.CSVHeaderValidationException;

public interface CSVDataValidationRules {
	public void validateHeader(String[] header) throws CSVHeaderValidationException;
	public void validateRow(String[] row) throws CSVBodyValidationException;
}