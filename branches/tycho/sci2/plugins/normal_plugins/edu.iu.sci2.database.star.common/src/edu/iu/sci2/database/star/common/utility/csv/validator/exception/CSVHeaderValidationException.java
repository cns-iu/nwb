package edu.iu.sci2.database.star.common.utility.csv.validator.exception;


public class CSVHeaderValidationException extends CSVDataValidationException {
	private static final long serialVersionUID = 1L;
	
	public CSVHeaderValidationException() {
		super();
	}

	public CSVHeaderValidationException(String arg0) {
		super(arg0);
	}

	public CSVHeaderValidationException(Throwable arg0) {
		super(arg0);
	}

	public CSVHeaderValidationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
