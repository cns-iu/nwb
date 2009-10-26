package edu.iu.nwb.converter.jungprefusebeta;


import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import prefuse.data.Graph;

public class JungPrefuseBetaConverterAlgorithm implements Algorithm, AlgorithmProperty {
    private Data[] data;
    
    public JungPrefuseBetaConverterAlgorithm(Data[] data) {
        this.data = data;
    }

    public Data[] execute() {
        edu.uci.ics.jung.graph.Graph g = 
            (edu.uci.ics.jung.graph.Graph) data[0].getData();
        
        Graph prefuseGraph = new JungPrefuseBetaConverter().getPrefuseGraph(g);
        Data dm = new BasicData(data[0].getMetadata(), prefuseGraph, Graph.class.getName());
        
        return new Data[]{ dm };
    }
}