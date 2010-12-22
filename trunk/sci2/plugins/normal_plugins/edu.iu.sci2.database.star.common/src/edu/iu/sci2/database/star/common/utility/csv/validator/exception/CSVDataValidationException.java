package edu.iu.sci2.database.star.common.utility.csv.validator.exception;

public class CSVDataValidationException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public CSVDataValidationException() {
		super();
	}

	public CSVDataValidationException(String arg0) {
		super(arg0);
	}

	public CSVDataValidationException(Throwable arg0) {
		super(arg0);
	}

	public CSVDataValidationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
