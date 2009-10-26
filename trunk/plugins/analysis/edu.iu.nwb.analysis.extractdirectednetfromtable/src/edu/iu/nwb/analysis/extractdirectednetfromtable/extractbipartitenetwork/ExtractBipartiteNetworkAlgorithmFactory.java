package edu.iu.nwb.analysis.extractdirectednetfromtable.extractbipartitenetwork;

import edu.iu.nwb.analysis.extractdirectednetfromtable.ExtractNetworkAlgorithmFactory;

public class ExtractBipartiteNetworkAlgorithmFactory extends ExtractNetworkAlgorithmFactory {
	public boolean isBipartite() {
		return true;
	}

	public String createOutDataLabel(String sourceColumnName, String targetColumnName) {
		return "Bipartite network from " + sourceColumnName + " and " + targetColumnName;
	}
}
