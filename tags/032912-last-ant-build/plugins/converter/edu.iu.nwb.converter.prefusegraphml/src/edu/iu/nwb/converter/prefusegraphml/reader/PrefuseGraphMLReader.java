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

public class PrefuseGraphMLReader implements Algorithm {
	private File inGraphMLFile;
	private boolean cleanForGUESS;
    
    public PrefuseGraphMLReader(Data[] data, boolean cleanForGUESS) {
		this.inGraphMLFile = (File) data[0].getData();
		this.cleanForGUESS = cleanForGUESS;
    }

    // cleanForGUESS is a major hack.
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		Graph outGraph =
    			(new GraphMLReaderModified(this.cleanForGUESS)).readGraph(
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
		Data outputData = new BasicData(outGraph, Graph.class.getName());
		outputData.getMetadata().put(DataProperty.LABEL, "Prefuse Graph: " + inGraphMLFile);
		outputData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);

		return new Data[] { outputData };
	}
}