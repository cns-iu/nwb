package edu.iu.nwb.preprocessing.extractnodesandedges;

import java.util.Dictionary;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

public class GraphDataFormatter {

	public static Data[] formatExtractedGraphAsData(Object extractedGraph, String label,  Data parent) {
		Data graphData = new BasicData(extractedGraph,  extractedGraph.getClass().getName());
		Dictionary metadata = graphData.getMetadata();
		metadata.put(DataProperty.PARENT, parent);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		metadata.put(DataProperty.LABEL, label);
		return new Data[]{graphData};
	}
}
