package edu.iu.cns.algorithm.databasecomparer.core;

public class DatabaseComparisonException extends Exception {
	public DatabaseComparisonException(Exception e) {
		super(e);
	}
	
	public DatabaseComparisonException(String message, Exception e) {
		super(message, e);
	}

}
