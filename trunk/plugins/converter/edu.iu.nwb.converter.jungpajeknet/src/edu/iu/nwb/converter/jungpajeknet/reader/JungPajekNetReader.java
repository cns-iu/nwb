package edu.iu.nwb.converter.jungpajeknet.reader;

import java.util.Dictionary;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.PajekNetReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JungPajekNetReader implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public JungPajekNetReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	LogService logger = (LogService)context.getService(LogService.class.getName());
    	File fileHandler = (File) data[0].getData();
    	try{
    		Graph graph = (new PajekNetReader()).load(new FileReader(fileHandler));
    		Data[] dm = new Data[] {new BasicData(graph, Graph.class.getName())};
    		dm[0].getMetaData().put(DataProperty.LABEL, "Jung Graph: " + fileHandler.getAbsolutePath());
    		return dm;
    	}catch (FileNotFoundException exception){
    		logger.log(LogService.LOG_ERROR, "FileNotFoundException", exception);
    		return null;
    	}catch (IOException ioe){
    		logger.log(LogService.LOG_ERROR, "IOException", ioe);
    		return null;
    	}

    }
}