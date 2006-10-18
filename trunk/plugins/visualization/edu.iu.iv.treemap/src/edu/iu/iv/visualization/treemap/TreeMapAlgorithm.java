package edu.iu.iv.visualization.treemap;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import edu.berkeley.guir.prefuse.graph.Tree;

public class TreeMapAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public TreeMapAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	 TreeMapVisualization tm = new TreeMapVisualization();
         tm.setInput((Tree) data[0].getData());
         tm.show();
         return null;
    }
}