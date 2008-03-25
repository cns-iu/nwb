package edu.iu.nwb.converter.prefusetreeml.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
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
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public PrefuseTreeMLReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
     	LogService logger = (LogService)context.getService(LogService.class.getName());
    	File fileHandler = (File) data[0].getData();

    	try{
    		Graph graph= (new TreeMLReader()).readGraph(new FileInputStream(fileHandler));
    		Data[] dm = new Data[] {new BasicData(graph, graph.getClass().getName())};
    		dm[0].getMetaData().put(DataProperty.LABEL, "Prefuse Tree: " + fileHandler);
            dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    		return dm;
    	}catch (DataIOException dioe){
    		logger.log(LogService.LOG_ERROR, "A Data IO error occured while reading the specified TreeML file.", dioe);
    		return null;
    	}catch (SecurityException exception){
    		logger.log(LogService.LOG_ERROR, "A security error occured while reading the specified TreeML file.", exception);
    		return null;
    	}catch (FileNotFoundException e){
    		logger.log(LogService.LOG_ERROR, "Could not find the specified TreeML file.", e);
    		return null;
    	}
    	
    }
}