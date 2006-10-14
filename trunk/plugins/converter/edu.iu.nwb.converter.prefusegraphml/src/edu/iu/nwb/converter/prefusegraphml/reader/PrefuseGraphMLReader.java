package edu.iu.nwb.converter.prefusegraphml.reader;

import java.util.Dictionary;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.SecurityException;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.io.GraphMLReader;
import prefuse.data.io.DataIOException;

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

    public Data[] execute() {
     	LogService logger = (LogService)context.getService(LogService.class.getName());
    	File fileHandler = (File) data[0].getData();

    	try{
    		Graph graph= (new GraphMLReader()).readGraph(new FileInputStream(fileHandler));
    		Data[] dm = new Data[] {new BasicData(graph, Graph.class.getName())};
    		dm[0].getMetaData().put(DataProperty.LABEL, "Prefuse Graph: " + fileHandler);
    		return dm;
    	}catch (DataIOException dioe){
    		logger.log(LogService.LOG_ERROR, "DataIOException", dioe);
    		return null;
    	}catch (SecurityException exception){
    		logger.log(LogService.LOG_ERROR, "SecurityException", exception);
    		return null;
    	}catch (FileNotFoundException e){
    		logger.log(LogService.LOG_ERROR, "FileNotFoundException", e);
    		return null;
    	}
    	
    }
}