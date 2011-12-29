package edu.iu.epic.visualization.linegraph;

public interface ActiveAlgorithmHook {
	void nowActive(LineGraphAlgorithm algorithm);
	void nowInactive(LineGraphAlgorithm algorithm);
}