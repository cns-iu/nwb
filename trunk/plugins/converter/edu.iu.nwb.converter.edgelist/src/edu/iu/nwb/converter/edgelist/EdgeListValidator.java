package edu.iu.nwb.converter.edgelist;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

public class EdgeListValidator implements Algorithm {
    
	Data[] data;
    Dictionary parameters;
    CIShellContext ciContext;
    LogService logger;
    
    public EdgeListValidator(Data[] dm, Dictionary parameters, CIShellContext context) {
    	this.data = dm;
        this.parameters = parameters;
        this.ciContext = context;
        logger = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {

		String edgeFileName = (String) data[0].getData();
		File edgeFile = new File(edgeFileName);
		
		EdgeListParser parser;
		try{ 
			parser = new EdgeListParser(edgeFile);

			Data[] dm = new Data[] {new BasicData(edgeFile, "file:text/edge")};
			dm[0].getMetadata().put(DataProperty.LABEL, "edge file: " + edgeFileName);
			dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			return dm;
		} catch (FileNotFoundException e){
			throw new AlgorithmExecutionException( 
					"Could not find the specified edge list file.",e);						
		} catch (IOException ioe){
			throw new AlgorithmExecutionException(
					"There were IO Errors while reading the specified edge list file.",ioe);
		} catch (InvalidEdgeListFormatException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);					
		}
		
		

    }
  

}