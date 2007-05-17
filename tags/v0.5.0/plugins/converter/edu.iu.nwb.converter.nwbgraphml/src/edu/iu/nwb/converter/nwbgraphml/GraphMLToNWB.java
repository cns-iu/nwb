package edu.iu.nwb.converter.nwbgraphml;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.log.LogService;


/**
 * Converts from GraphML to NWB file format
 * @author Ben Markines 
 */
public class GraphMLToNWB implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext ciContext;
    LogService logger;
    GUIBuilderService guiBuilder;
    
    Map vertexToIdMap;
	private Transformer stylesheet;
    
    /**
     * Intializes the algorithm
     * @param data List of Data objects to convert
     * @param parameters Parameters passed to the converter
     * @param context Provides access to CIShell services
     * @param transformer 
     */
    public GraphMLToNWB(Data[] data, Dictionary parameters, CIShellContext context, Transformer transformer) {
        this.data = data;
        this.parameters = parameters;
        this.ciContext = context;
        this.logger = (LogService)ciContext.getService(LogService.class.getName());
        this.stylesheet = transformer;
    }

    /**
     * Executes the conversion
     * 
     * @return A single java file object
     */
    public Data[] execute() {
		Object inFile = data[0].getData();
    	
		if (stylesheet != null && inFile instanceof File){
			
			
			
			Source graphml = new StreamSource((File) inFile);
			
			File nwbFile = getTempFile();
			Result nwb = new StreamResult(nwbFile);
			
			try {
				stylesheet.transform(graphml, nwb);
				return new Data[] {new BasicData(nwbFile, "file:text/nwb")};
			} catch(TransformerException exception) {
				logger.log(LogService.LOG_ERROR, "Problem executing transformation from GraphML to NWB");
				exception.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
    }
    
	
    /**
     * Creates a temporary file for the NWB file
     * @return The temporary file
     */
	private File getTempFile(){
		File tempFile;
    
		String tempPath = System.getProperty("java.io.tmpdir");
		File tempDir = new File(tempPath+File.separator+"temp");
		if(!tempDir.exists())
			tempDir.mkdir();
		try{
			tempFile = File.createTempFile("NWB-Session-", ".nwb", tempDir);
		
		}catch (IOException e){
			logger.log(LogService.LOG_ERROR, e.toString());
			tempFile = new File (tempPath+File.separator+"nwbTemp"+File.separator+"temp.nwb");

		}
		return tempFile;
	}
}