package edu.iu.nwb.analysis.extractdirectednetfromtable.extractdirectednetwork;

import edu.iu.nwb.analysis.extractdirectednetfromtable.ExtractNetworkAlgorithmFactory;

public class ExtractDirectedNetworkAlgorithmFactory extends ExtractNetworkAlgorithmFactory {
	public boolean isBipartite() {
		return false;
	}

	public String createOutDataLabel(String sourceColumnName, String targetColumnName) {
		return "Network with directed edges from " + sourceColumnName + " to " + targetColumnName;
	}
}
