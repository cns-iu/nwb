package edu.iu.nwb.visualization.balloongraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

import edu.berkeley.guir.prefuse.graph.Tree;

public class BalloongraphAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public BalloongraphAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	BalloongraphVisualization bg = new BalloongraphVisualization();
    	bg.setInput((Tree) data[0].getData());
    	bg.show();
        return null;
    }
}