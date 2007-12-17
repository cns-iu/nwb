package edu.iu.nwb.converter.prefusexgmml.reader;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.io.XMLGraphReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseXGMMLReader implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public PrefuseXGMMLReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	LogService logger = (LogService)context.getService(LogService.class.getName());
    	File fileHandler = (File) data[0].getData();

    	try{
    		Graph graph = (new XMLGraphReader()).loadGraph(fileHandler.getAbsoluteFile());
    		Data[] dm = new Data[] {new BasicData(graph, Graph.class.getName())};
    		dm[0].getMetaData().put(DataProperty.LABEL, "Old Prefuse Graph: " + fileHandler);
            dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    		return dm;
    	}catch (IOException ioe){
       		logger.log(LogService.LOG_ERROR, "IO errors while reading the specified XGMML file.", ioe);       	 
    		return null;
    	}

    }
}