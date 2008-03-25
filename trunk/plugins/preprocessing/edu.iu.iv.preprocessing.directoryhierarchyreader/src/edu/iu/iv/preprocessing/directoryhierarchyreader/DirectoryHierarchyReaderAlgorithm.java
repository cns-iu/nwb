package edu.iu.iv.preprocessing.directoryhierarchyreader;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.berkeley.guir.prefuse.graph.Graph;

public class DirectoryHierarchyReaderAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public DirectoryHierarchyReaderAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	File rootFile = new File((String)parameters.get("rootDirectory"));
	    int numLevels = ((Integer) parameters.get("level")).intValue();
	    boolean doRecurseTree = ((Boolean) parameters.get("doRecurse")).booleanValue();
	    boolean doSkipFiles = ((Boolean) parameters.get("readDirectories")).booleanValue();
	    
	    if (doRecurseTree) {
	        numLevels = DirectoryHierarchyReader.INDEFINITE;
	    }
	    
	    Graph g = DirectoryHierarchyReader.readDirectory(rootFile,numLevels,!doSkipFiles);
	    
	    Data dm = new BasicData(g, Graph.class.getName());
	    dm.getMetadata().put(DataProperty.LABEL,"Directory Tree - Prefuse Graph");
	    dm.getMetadata().put(DataProperty.TYPE,DataProperty.TREE_TYPE);
	    
	    return new Data[]{dm};
    }
}