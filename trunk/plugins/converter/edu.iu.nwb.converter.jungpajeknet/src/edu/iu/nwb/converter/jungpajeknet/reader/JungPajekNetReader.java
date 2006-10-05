package edu.iu.nwb.converter.jungpajeknet.reader;

import java.util.Dictionary;
import java.io.IOException;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

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
    	String fileHandler = (String) data[0].getData();
    	try{
    		Graph graph = (new PajekNetReader()).load(fileHandler);
    		Data[] dm = new Data[] {new BasicData(graph, Graph.class.getName())};
    		dm[0].getMetaData().put(DataProperty.LABEL, "Jung GraphML Data Model: " + fileHandler);
    		return dm;
    	}catch (IOException ioe){
    		//use guibuilder to display the exception
    		return null;
    	}

    }
}