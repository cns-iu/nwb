package edu.iu.nwb.converter.jungpajeknet.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Dictionary;
import java.util.Iterator;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.UserDatumNumberEdgeValue;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.utils.UserDataContainer;
import edu.uci.ics.jung.utils.UserDataContainer.CopyAction;

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
    		Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileHandler), "UTF8"));
    		
    		UserDatumNumberEdgeValue weightValues = new UserDatumNumberEdgeValue("weight");
    		weightValues.setCopyAction(UserData.SHARED);
			Graph graph = (new PajekNetReader()).load(reader, weightValues);
            fixLabels(graph);
            
    		Data[] dm = new Data[] {new BasicData(graph, Graph.class.getName())};
    		dm[0].getMetaData().put(DataProperty.LABEL, "Jung Graph: " + fileHandler.getAbsolutePath());
            dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    		return dm;
    	}catch (FileNotFoundException exception){
    		logger.log(LogService.LOG_ERROR, "FileNotFoundException", exception);
    		return null;
    	}catch (IOException ioe){
    		logger.log(LogService.LOG_ERROR, "IOException", ioe);
    		return null;
    	}
    }
    
    private void fixLabels(Graph g) {
        Iterator vertices = g.getVertices().iterator();
        while (vertices.hasNext()) {
            Vertex v = (Vertex) vertices.next();
            Object label = v.getUserDatum(PajekNetReader.LABEL);
            if (label != null) {
                v.addUserDatum("label", label, new UserDataContainer.CopyAction.Clone());
            }
        }
        
    }
}