package edu.iu.nwb.converter.prefusegraphml.reader;

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

import prefuse.data.Graph;
import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseGraphMLReader implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public PrefuseGraphMLReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	File fileHandler = (File) data[0].getData();

    	try{
    		Graph graph= (new GraphMLReaderModified()).readGraph(new FileInputStream(fileHandler));
    		Data[] dm = new Data[] {new BasicData(graph, Graph.class.getName())};
    		dm[0].getMetadata().put(DataProperty.LABEL, "Prefuse Graph: " + fileHandler);
            dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    		return dm;
    	}catch (DataIOException dioe){
    		throw new AlgorithmExecutionException("A Data IO error occurred while reading the specified file into prefuse.data.Graph.", dioe);
    	}catch (SecurityException exception){
    		throw new AlgorithmExecutionException("A security violation occured while reading the specified file into prefuse.data.Graph.", exception);
    	}catch (FileNotFoundException e){
    		throw new AlgorithmExecutionException("Could not find the specified file to convert to prefuse.data.Graph.", e);
    	}
    }
}