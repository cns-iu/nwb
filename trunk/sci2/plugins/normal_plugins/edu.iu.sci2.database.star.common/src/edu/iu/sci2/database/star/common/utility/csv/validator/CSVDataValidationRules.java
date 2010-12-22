package edu.iu.sci2.database.star.common.utility.csv.validator;

import edu.iu.sci2.database.star.common.utility.csv.validator.exception.CSVBodyValidationException;
import edu.iu.sci2.database.star.common.utility.csv.validator.exception.CSVHeaderValidationException;

public interface CSVDataValidationRules {
	public void validateHeader(String[] header) throws CSVHeaderValidationException;
	public void validateRow(String[] row) throws CSVBodyValidationException;
}