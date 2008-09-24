package edu.iu.iv.visualization.treeviz;

import java.util.Dictionary;

import javax.swing.tree.TreeModel;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

import edu.berkeley.guir.prefuse.graph.Tree;

public class TreeVizAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public TreeVizAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
        TreeConverter converter = new TreeConverter() ;
        TreeModel treeModel = converter.convert((Tree)data[0].getData()) ;        
        TreeVizGUI gui = new TreeVizGUI(treeModel,"Tree Visualization");
        
        gui.show();
        gui.setVisible(true);
        return null;
    }
}