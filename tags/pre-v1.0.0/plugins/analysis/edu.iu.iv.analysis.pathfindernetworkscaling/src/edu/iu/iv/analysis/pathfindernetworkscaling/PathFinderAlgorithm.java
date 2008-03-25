package edu.iu.iv.analysis.pathfindernetworkscaling;

import java.util.Dictionary;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import cern.colt.matrix.DoubleMatrix2D;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;


public class PathFinderAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public PathFinderAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	int q = ((Integer)parameters.get("q")).intValue();
	    int r = ((Integer)parameters.get("r")).intValue();
	    PathFinder pathFinder = new PathFinder(q, r,(DoubleMatrix2D)data[0].getData());
        pathFinder.applyScaling();
        
		    BasicData dm = new BasicData(pathFinder.getResultMatrix(),PathFinder.class.getName());
		    Dictionary map = dm.getMetaData();
		    map.put(DataProperty.LABEL,"Path Finder Network Scaled Model");
		    map.put(DataProperty.TYPE,DataProperty.MATRIX_TYPE);
		    map.put(DataProperty.PARENT,DataProperty.PARENT);
		    
        
        return data;
    }
}