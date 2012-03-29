package edu.iu.nwb.converter.jungprefuse;


import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.berkeley.guir.prefuse.graph.Graph;

public class JungPrefuseConverterAlgorithm implements Algorithm, AlgorithmProperty {
    private Data[] data;
    
    public JungPrefuseConverterAlgorithm(Data[] data) {
        this.data = data;
    }

    public Data[] execute() {
        edu.uci.ics.jung.graph.Graph g = 
            (edu.uci.ics.jung.graph.Graph) data[0].getData();
        
        Graph prefuseGraph = JungPrefuseConverter.getPrefuseGraph(g);
        Data dm = new BasicData(data[0].getMetadata(),
        						prefuseGraph,
        						Graph.class.getName());
        
        return new Data[]{ dm };
    }
}