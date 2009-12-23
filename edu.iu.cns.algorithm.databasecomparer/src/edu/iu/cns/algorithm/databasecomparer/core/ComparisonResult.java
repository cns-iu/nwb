package edu.iu.cns.algorithm.databasecomparer.core;

public class ComparisonResult {
	
	private boolean wereEqual;
	private DifferenceLog differenceLog;
	
	public ComparisonResult(boolean wereEqual, DifferenceLog differenceLog) {
		this.wereEqual = wereEqual;
		this.differenceLog = differenceLog;
	}
	
	public boolean databasesWereEqual() {
		return this.wereEqual;
	}
	
	public DifferenceLog getDifferenceLog() {
		return this.differenceLog;
	}
}
