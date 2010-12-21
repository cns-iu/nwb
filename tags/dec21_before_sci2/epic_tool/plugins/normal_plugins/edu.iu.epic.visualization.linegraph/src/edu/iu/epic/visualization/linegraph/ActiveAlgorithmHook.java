package edu.iu.epic.visualization.linegraph;

public interface ActiveAlgorithmHook {
	public void nowActive(LineGraphAlgorithm algorithm);
	public void nowInactive(LineGraphAlgorithm algorithm);
}