package edu.iu.nwb.converter.prefusetreeml.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.io.DataIOException;
import prefuse.data.io.TreeMLReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseTreeMLReader implements Algorithm {
	private File inTreeMLFile;
    
    public PrefuseTreeMLReader(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        this.inTreeMLFile = (File) data[0].getData();
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		Graph graph =
    			(new TreeMLReader()).readGraph(
    					new FileInputStream(inTreeMLFile));
    		return createOutData(graph);
    	} catch (DataIOException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} catch (SecurityException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} catch (FileNotFoundException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}    	
    }

	private Data[] createOutData(Graph graph) {
		Data[] dm = new Data[] {new BasicData(
				graph, graph.getClass().getName())};
		dm[0].getMetadata().put(
				DataProperty.LABEL, "Prefuse Tree: " + inTreeMLFile);
		dm[0].getMetadata().put(
				DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		return dm;
	}
}