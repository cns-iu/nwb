package edu.iu.nwb.converter.junggraphml.reader;

//Java
import java.util.Dictionary;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
//OSGi
import org.osgi.service.log.LogService;
//CIShell 
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.framework.data.BasicData;
//Jung lib
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
    	LogService logger = (LogService)context.getService(LogService.class.getName());
    	File fileHandler = (File) data[0].getData();
    	try {
    		Graph graph = (new GraphMLFile()).load(new FileReader(fileHandler));
    		Data[] dm = new Data[] {new BasicData(graph, Graph.class.getName())};
    		dm[0].getMetadata().put(DataProperty.LABEL, "Jung Graph: " + fileHandler.getAbsolutePath());
            dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
            
    		return dm;
    	}catch (FileNotFoundException exception){
    		logger.log(LogService.LOG_ERROR, "The specified GraphML file could not be found.", exception);
    		return null;
    	}
    	
    }
}