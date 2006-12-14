package edu.iu.nwb.converter.jungprefusebeta;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.uci.ics.jung.graph.Graph;

public class PrefuseBetaJungConverterAlgorithm implements Algorithm, AlgorithmProperty {

	private Data[] data;
	private Dictionary parameters;
	private CIShellContext context;

	public PrefuseBetaJungConverterAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
	}

	public Data[] execute() {
		prefuse.data.Graph prefuseGraph = (prefuse.data.Graph) data[0].getData();
		Graph jungGraph = PrefuseBetaJungConverter.getJungGraph(prefuseGraph);
		Data dm = new BasicData(data[0].getMetaData(), jungGraph, Graph.class.getName());
        
        return new Data[]{dm};
	}

}
