package edu.iu.cns.converter.plot_to_csv.exceptiontypes;

public class PlotFileReadingException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public PlotFileReadingException() {
		super();
	}

	public PlotFileReadingException(String arg0) {
		super(arg0);
	}

	public PlotFileReadingException(Throwable arg0) {
		super(arg0);
	}

	public PlotFileReadingException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}