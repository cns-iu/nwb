package edu.iu.nwb.converter.prefusegraphml.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Graph;
import prefuse.data.io.DataIOException;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseGraphMLReader implements Algorithm {
	private File inGraphMLFile;
    
    public PrefuseGraphMLReader(Data[] data) {
		this.inGraphMLFile = (File) data[0].getData();
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		Graph outGraph =
    			(new GraphMLReaderModified()).readGraph(
    					new FileInputStream(inGraphMLFile));
    		
    		return createOutData(inGraphMLFile, outGraph);
    	} catch (DataIOException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} catch (SecurityException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} catch (FileNotFoundException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }

	private Data[] createOutData(File inGraphMLFile, Graph outGraph) {
		Data[] dm = new Data[]{ new BasicData(
				outGraph, Graph.class.getName()) };
		dm[0].getMetadata().put(
				DataProperty.LABEL, "Prefuse Graph: " + inGraphMLFile);
		dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		return dm;
	}
}