package edu.iu.nwb.preprocessing.cocitationsimilarity;


import java.util.Map;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;

public class NWBToEdgeFileHandler extends NWBFileParserAdapter {
	public int count = 0;
	
	public void addDirectedEdge(int node1, int node2, Map attributes) {
		this.count += 1;
	}
}
