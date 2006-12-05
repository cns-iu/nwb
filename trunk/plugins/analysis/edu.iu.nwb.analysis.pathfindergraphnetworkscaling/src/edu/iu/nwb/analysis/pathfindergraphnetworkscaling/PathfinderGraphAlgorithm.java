package edu.iu.nwb.analysis.pathfindergraphnetworkscaling;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import cern.colt.matrix.DoubleMatrix2D;

import edu.iu.nwb.analysis.pathfindergraphnetworkscaling.old.PathFinderAlgorithm;
import edu.uci.ics.jung.graph.Graph;

public class PathfinderGraphAlgorithm implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public PathfinderGraphAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	
    	LogService log = (LogService) context.getService(LogService.class.getName());
    	
    	Graph inputGraph = (Graph) data[0].getData();
    	
    	GraphMetadataMemory memory = new GraphMetadataMemory(inputGraph);
    	
    	Data inputData = new BasicData(memory.getMatrix(), DoubleMatrix2D.class.getName());
    	
		PathFinderAlgorithm pathfinder = new PathFinderAlgorithm(new Data[]{ inputData },
				parameters, context);
    	
    	Data[] pathfinderData = pathfinder.execute();
    	Data outputMatrix = pathfinderData[0];
    	
    	Data outputData = null;
    	
		if(outputMatrix.getFormat().equals(DoubleMatrix2D.class.getName())) {
    		Graph outputGraph = memory.reconstructMetadata((DoubleMatrix2D) outputMatrix.getData());
    		outputData = new BasicData(outputGraph, Graph.class.getName());
    		
    		Dictionary map = outputData.getMetaData();
    		map.put(DataProperty.MODIFIED,
                    new Boolean(true));
            map.put(DataProperty.PARENT, data[0]);
            map.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
            map.put(DataProperty.LABEL, "Pathfinder Network Scaling");
    		
    	} else {
    		log.log(LogService.LOG_ERROR, "PathFinderAlgorithm did not return correct "
    				+ DoubleMatrix2D.class.getName() + " data format, returns " + outputMatrix.getFormat() + " instead");
    	}
    	
		
		
        return new Data[]{ outputData };
    }
}