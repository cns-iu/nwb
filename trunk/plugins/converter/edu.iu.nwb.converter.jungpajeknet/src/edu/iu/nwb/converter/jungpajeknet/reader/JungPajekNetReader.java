package edu.iu.nwb.converter.jungpajeknet.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Dictionary;
import java.util.Iterator;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.UserDatumNumberEdgeValue;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.utils.UserData;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JungPajekNetReader implements Algorithm {
    public static final String EDGE_WEIGHT_ATTRIBUTE = "weight";
    
    private File inNetFileName;
	
    
    public JungPajekNetReader(Data[] data,
    						  Dictionary parameters,
    						  CIShellContext context) {
		inNetFileName = (File) data[0].getData();
    }

    
    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		Reader reader =
    			new BufferedReader(new InputStreamReader
    				(new FileInputStream(inNetFileName)));
    		
    		UserDatumNumberEdgeValue weightValues =
    			new UserDatumNumberEdgeValue(EDGE_WEIGHT_ATTRIBUTE);
    		weightValues.setCopyAction(UserData.SHARED);
			Graph graph = (new PajekNetReader()).load(reader, weightValues);
            fixLabels(graph);
            
    		return createOutData(inNetFileName, graph);
    	} catch (FileNotFoundException e) {
    		String message =
    			"Couldn't find the Pajek .net file: " + e.getMessage();
    		throw new AlgorithmExecutionException(message, e);
    	} catch (IOException e) {
    		String message = "File access error: " + e.getMessage();
    		throw new AlgorithmExecutionException(message, e);
    	}
    }

	private Data[] createOutData(File fileHandler, Graph graph) {
		Data[] dm =
			new Data[]{ new BasicData(graph, Graph.class.getName()) };
		dm[0].getMetadata().put(
				DataProperty.LABEL,
				"Jung Graph: " + fileHandler.getAbsolutePath());
		dm[0].getMetadata().put(
				DataProperty.TYPE,
				DataProperty.NETWORK_TYPE);
		return dm;
	}
    
    private void fixLabels(Graph g) {
        Iterator vertices = g.getVertices().iterator();
        while (vertices.hasNext()) {
            Vertex v = (Vertex) vertices.next();
            Object label = v.getUserDatum(PajekNetReader.LABEL);
            if (label != null) {
                v.addUserDatum("label", label, UserData.SHARED);
            }
        }
    }
}