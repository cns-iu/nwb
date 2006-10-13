package edu.iu.nwb.converter.junggraphml.writer;

//Java
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
//CIShell
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;
//Jung
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLFile;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JungGraphMLWriter implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService logger;
    
    public JungGraphMLWriter(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        logger = (LogService)context.getService("LogService.class.getName()");
    }

    public Data[] execute() {
       File tempFile;
        
    	String tempPath = System.getProperty("java.io.tmpdir");
    	File tempDir = new File(tempPath+File.separator+"temp");
    	if(!tempDir.exists())
    		tempDir.mkdir();
    	try{
    		tempFile = File.createTempFile("NWB-Session-", ".xml", tempDir);
    		
    	}catch (IOException e){
    		logger.log(LogService.LOG_ERROR, e.toString());
    		tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.nwb");

    	}
    	if (tempFile != null){
    		(new GraphMLFile()).save((Graph)(data[0].getData()), tempFile.getPath()) ;
    	}
        return new Data[]{new BasicData(tempFile, "file:text/graphml+xml") };

    }
}