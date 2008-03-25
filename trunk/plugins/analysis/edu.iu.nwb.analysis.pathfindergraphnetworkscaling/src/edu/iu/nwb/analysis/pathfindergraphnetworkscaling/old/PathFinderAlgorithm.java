package edu.iu.nwb.analysis.pathfindergraphnetworkscaling.old;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import cern.colt.matrix.DoubleMatrix2D;


/**
 * @author Russell Duhon
 *
 */
public class PathFinderAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    /**
     * @param data
     * @param parameters
     * @param context
     */
    public PathFinderAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	int q = ((Integer)parameters.get("q")).intValue();
	    int r = ((Integer)parameters.get("r")).intValue();
	    DoubleMatrix2D inputMatrix = (DoubleMatrix2D)data[0].getData();
		PathFinder pathFinder = new PathFinder(q, r,inputMatrix);
		
        DoubleMatrix2D outputMatrix = pathFinder.getResultMatrix();
		BasicData dm = new BasicData(outputMatrix,DoubleMatrix2D.class.getName());
        Dictionary map = dm.getMetadata();
		map.put(DataProperty.LABEL,"Path Finder Network Scaled Model");
		map.put(DataProperty.TYPE,DataProperty.MATRIX_TYPE);
		map.put(DataProperty.PARENT,DataProperty.PARENT);
		
        
        return new Data[]{ dm };
    }
}