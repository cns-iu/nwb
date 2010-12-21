package edu.iu.nwb.converter.prefusexgmml.reader;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.io.XMLGraphReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseXGMMLReader implements Algorithm {
	private File inGraphFile;
    
	
    public PrefuseXGMMLReader(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        this.inGraphFile = (File) data[0].getData();
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		Graph graph =
    			(new XMLGraphReader()).loadGraph(inGraphFile.getAbsoluteFile());
    		
    		return createOutData(graph);
    	} catch (IOException e) {
       		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }

	private Data[] createOutData(Graph graph) {
		Data[] dm = new Data[] {new BasicData(graph, Graph.class.getName())};
		dm[0].getMetadata().put(
				DataProperty.LABEL, "Old Prefuse Graph: " + inGraphFile);
		dm[0].getMetadata().put(
				DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		return dm;
	}
}