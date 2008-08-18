package edu.iu.nwb.converter.prefusegraphml.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseGraphMLValidation implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseGraphMLValidationAlg(data, parameters, context);
    }
    
    public class PrefuseGraphMLValidationAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        
        public PrefuseGraphMLValidationAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
        }

        public Data[] execute() throws AlgorithmExecutionException {
        	String fileHandler = (String) data[0].getData();
        	File inData = new File(fileHandler);
        	
        	try{
        		if (validateGraphMLHeader(inData)){
	        		(new GraphMLReaderModified()).readGraph(new FileInputStream(fileHandler));
	        		Data[] dm = new Data[] {new BasicData(inData, "file:text/graphml+xml")};
	        		dm[0].getMetadata().put(DataProperty.LABEL, "Prefuse GraphML file: " + fileHandler);
	                dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
	        		return dm;
        		}else 
            		return null;
        	}catch (DataIOException dioe){
				throw new AlgorithmExecutionException("Data IO error while validating the specified graphML file.", dioe);
        	}catch (SecurityException exception){
        		throw new AlgorithmExecutionException( "Security error while validating the specified graphML file.", exception);
        	}catch (FileNotFoundException e){
        		throw new AlgorithmExecutionException("Could not find the specified graphML file for validation.", e);
        	}catch (IOException ioe){
        		throw new AlgorithmExecutionException("IO errors while validating the specified graphML", ioe);
        	}
        	
        	
        }
        
        //TODO
        //This is a temporary fix. The problem is after we get rid of LoadDataChooser
        //It seems that prefuse library can load xgmml file(.xml) in as a graphml.
        //but all visualization algs didn't work since it is not a real graphml file.
        //Here I try to detect if there is "http://graphml.graphdrawing.org/xmlns" namespace
        //in the file, if not, it is not a gramphml file
        private boolean validateGraphMLHeader(File inData)
        		throws FileNotFoundException, IOException{
        	boolean hasGraphMLHeader = false;
    		
        	BufferedReader reader = 
    			new BufferedReader(new FileReader(inData));
    		
    		String line = reader.readLine();
    		while(line != null){
    			if(line.indexOf("http://graphml.graphdrawing.org/xmlns")!= -1){
    				hasGraphMLHeader = true;
    				break;
    			}    			
    			line = reader.readLine();	
    		}
    		return hasGraphMLHeader;
    		
        }
    }
}