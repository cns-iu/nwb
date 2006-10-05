package edu.iu.nwb.converter.junggraphml.reader;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.framework.data.BasicData;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLFile; 

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JungGraphMLReader implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public JungGraphMLReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	String fileHandler = (String) data[0].getData();
    	Graph graph = (new GraphMLFile()).load(fileHandler);
    	Data[] dm = new Data[] {new BasicData(graph, Graph.class.getName())};
    	dm[0].getMetaData().put(DataProperty.LABEL, "Jung GraphML Data Model: " + fileHandler);
    	return dm;
    }
}