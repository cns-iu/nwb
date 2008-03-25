package edu.iu.nwb.analysis.burst;

public class Cell {
	public double[] cost;
	public double[] total;
	public int[] previous;
	public int path = 0;
	public int minRateClass = 0;
	public boolean[] candidate;
	public int[] endCandidate;
	public boolean[] mark;
	public int[] breakpoint;
	public boolean[] subordinate;
	public double[] power;
	public double[] totalPower;
	
	public Cell(int levels) {
		cost = new double[levels];
		total = new double[levels];
		power = new double[levels];
		totalPower = new double[levels];
		previous = new int[levels];
		endCandidate = new int[levels];
		breakpoint = new int[levels];
		subordinate = new boolean[levels];
		candidate = new boolean[levels];
		mark = new boolean[levels];
	}
}
