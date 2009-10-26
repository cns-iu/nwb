package edu.iu.nwb.converter.jungprefusebeta;


import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.uci.ics.jung.graph.Graph;

public class PrefuseBetaJungConverterAlgorithm implements Algorithm, AlgorithmProperty {
	private Data[] data;

	public PrefuseBetaJungConverterAlgorithm(Data[] data) {
		this.data = data;
	}

	public Data[] execute() {
		prefuse.data.Graph prefuseGraph = (prefuse.data.Graph) data[0].getData();
		Graph jungGraph = new PrefuseBetaJungConverter().getJungGraph(prefuseGraph);
		Data dm = new BasicData(data[0].getMetadata(), jungGraph, Graph.class.getName());
        
        return new Data[]{ dm };
	}
}
