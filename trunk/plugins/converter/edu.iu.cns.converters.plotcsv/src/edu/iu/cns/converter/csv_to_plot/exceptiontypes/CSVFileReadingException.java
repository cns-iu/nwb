package edu.iu.cns.converter.csv_to_plot.exceptiontypes;

public class CSVFileReadingException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public CSVFileReadingException() {
		super();
	}

	public CSVFileReadingException(String arg0) {
		super(arg0);
	}

	public CSVFileReadingException(Throwable arg0) {
		super(arg0);
	}

	public CSVFileReadingException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}