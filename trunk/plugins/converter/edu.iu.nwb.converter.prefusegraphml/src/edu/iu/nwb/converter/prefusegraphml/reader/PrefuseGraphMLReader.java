package edu.iu.nwb.converter.prefusegraphml.reader;

import java.util.Dictionary;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.SecurityException;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

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
    	String fileHandler = (String) data[0].getData();
//    	Graph graph = (new GraphMLFile()).load(fileHandler);
    	try{
    		Graph graph= (new GraphMLReader()).readGraph(new FileInputStream(fileHandler));
    		Data[] dm = new Data[] {new BasicData(graph, Graph.class.getName())};
    		dm[0].getMetaData().put(DataProperty.LABEL, "Prefuse Graph Data Model: " + fileHandler);
    		return dm;
    	}catch (DataIOException dioe){
    		return null;
    	}catch (SecurityException exception){
    		
    		return null;
    	}catch (FileNotFoundException e){
    		return null;
    	}
    	
    }
}