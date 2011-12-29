package edu.iu.nwb.converter.prefuseTreeBetaAlpha;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.berkeley.guir.prefuse.graph.Tree;


public class PrefuseTreeBetaAlphaAlgorithm implements Algorithm {
    private Data[] data;
    
    public PrefuseTreeBetaAlphaAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
    }

    public Data[] execute() {
    	prefuse.data.Tree inTree = (prefuse.data.Tree) data[0].getData();
    
        Tree AlphaTree =
        	new PrefuseTreeBetaAlphaConverter().getPrefuseBetaTree(inTree);
        
        Data dm = new BasicData(
        		data[0].getMetadata(), AlphaTree, Tree.class.getName());
        
        return new Data[]{ dm };
    }
}