package edu.iu.nwb.converter.jungprefuse;


import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.berkeley.guir.prefuse.graph.Graph;

public class PrefuseJungConverterAlgorithm
		implements Algorithm, AlgorithmProperty {
	private Data[] data;

	public PrefuseJungConverterAlgorithm(
			Data[] data) {
		this.data = data;
	}

	public Data[] execute() {
		Graph prefuseGraph = (Graph) data[0].getData();
		edu.uci.ics.jung.graph.Graph jungGraph =
			PrefuseJungConverter.getJungGraph(prefuseGraph);
		// TODO Is the metadata still accurate?
		Data dm = new BasicData(data[0].getMetadata(),
								jungGraph,
								edu.uci.ics.jung.graph.Graph.class.getName());
        
        return new Data[]{ dm };
	}
}
