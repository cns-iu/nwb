package edu.iu.cns.algorithm.databasecomparer.core;

import java.util.ArrayList;
import java.util.List;

public class DifferenceLog {

	private List differencesFound = new ArrayList();
	
	public void addDifference(String differenceReport) {
		this.differencesFound.add(differenceReport);
	}
	
	public boolean differencesHaveBeenFound() {
		return ! differencesFound.isEmpty();
	}
	
	public List getDifferencesFound() {
		return this.differencesFound;
	}
}
