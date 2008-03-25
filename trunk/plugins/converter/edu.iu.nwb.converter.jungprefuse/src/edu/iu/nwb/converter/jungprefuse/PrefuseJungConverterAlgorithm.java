package edu.iu.nwb.converter.jungprefuse;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.berkeley.guir.prefuse.graph.Graph;

public class PrefuseJungConverterAlgorithm  implements Algorithm, AlgorithmProperty {

	private Data[] data;
	private Dictionary parameters;
	private CIShellContext context;

	public PrefuseJungConverterAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
	}

	public Data[] execute() {
		Graph prefuseGraph = (Graph) data[0].getData();
		edu.uci.ics.jung.graph.Graph jungGraph = PrefuseJungConverter.getJungGraph(prefuseGraph);
		Data dm = new BasicData(data[0].getMetadata(), //might this include now-incorrect information?
				jungGraph, edu.uci.ics.jung.graph.Graph.class.getName());
        
        return new Data[]{dm};
	}

}
