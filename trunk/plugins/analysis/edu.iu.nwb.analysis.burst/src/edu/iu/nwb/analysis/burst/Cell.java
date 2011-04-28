package edu.iu.nwb.analysis.burst;

public class Cell {
	private double[] costs;
	private double[] totals;
	private int[] previousPaths;
	private int path = 0;
	private int minRateClass = 0;
	private boolean[] candidates;
	private int[] endCandidates;
	private boolean[] marks;
	private int[] breakpoints;
	private boolean[] subordinates;
	private double[] powers;
	private double[] totalPowers;
	
	public Cell(int levels) {
		costs = new double[levels];
		totals = new double[levels];
		powers = new double[levels];
		totalPowers = new double[levels];
		previousPaths = new int[levels];
		endCandidates = new int[levels];
		breakpoints = new int[levels];
		subordinates = new boolean[levels];
		candidates = new boolean[levels];
		marks = new boolean[levels];
	}
	
	public double[] getCosts() {
		return costs;
	}

	public double[] getTotals() {
		return totals;
	}

	public int[] getPreviousPaths() {
		return previousPaths;
	}

	public int getPath() {
		return path;
	}

	public int getMinRateClass() {
		return minRateClass;
	}

	public boolean[] getCandidates() {
		return candidates;
	}

	public int[] getEndCandidates() {
		return endCandidates;
	}

	public boolean[] getMarks() {
		return marks;
	}

	public int[] getBreakpoints() {
		return breakpoints;
	}

	public boolean[] getSubordinates() {
		return subordinates;
	}

	public double[] getPowers() {
		return powers;
	}

	public double[] getTotalPowers() {
		return totalPowers;
	}

	public void setMinRateClass(int minRateClass) {
		this.minRateClass = minRateClass;
	}

	public void setPath(int path) {
		this.path = path;
	}
}
