package edu.iu.nwb.converter.prefusetreeml.reader;

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
import prefuse.data.io.TreeMLReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseTreeMLValidation implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseTreeMLValidationAlg(data, parameters, context);
    }
    
    public class PrefuseTreeMLValidationAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        LogService logger;
        
        public PrefuseTreeMLValidationAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
            logger = (LogService)context.getService(LogService.class.getName());
        }

        public Data[] execute() throws AlgorithmExecutionException {
        	String fileHandler = (String) data[0].getData();
        	File inData = new File(fileHandler);
        	
        	try{
        		if (validateTreeMLHeader(inData)){
	        		(new TreeMLReader()).readGraph(new FileInputStream(fileHandler));
	        		Data[] dm = new Data[] {new BasicData(inData, "file:text/treeml+xml")};
	        		dm[0].getMetadata().put(DataProperty.LABEL, "Prefuse TreeML file: " + fileHandler);
	                dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.TREE_TYPE);
	        		return dm;
        		}else {
        			logger.log(LogService.LOG_ERROR, "TreeML file does not contain a valid header.");
            		return null;
        		}
        	}catch (DataIOException dioe){
				throw new AlgorithmExecutionException("Data IO error while validating the specified TreeML file.", dioe);
        	}catch (SecurityException exception){
        		throw new AlgorithmExecutionException("Security error while validating the specified TreeML file.", exception);
        	}catch (FileNotFoundException e){
        		throw new AlgorithmExecutionException("Could not find the specified TreeML file.", e);
        	}catch (IOException ioe){
        		throw new AlgorithmExecutionException("TreeML validator experienced an unexplained IO Exception", ioe);
        	}
        }
        
        //TODO
        //This is a temporary fix. The problem is after we get rid of LoadDataChooser
        //It seems that prefuse library can load xgmml file(.xml) in as a graphml.
        //but all visualization algs didn't work since it is not a real graphml file.
        //Here I try to detect if there is "<graphml" header in the file, if not, it 
        //is not a gramphml file
        private boolean validateTreeMLHeader(File inData)
        		throws FileNotFoundException, IOException{
        	boolean hasTreeMLHeader = false;
    		
        	BufferedReader reader = 
    			new BufferedReader(new FileReader(inData));
    		
    		String line = reader.readLine();
    		while(line != null){
    			if(line.startsWith("<tree")){
    				hasTreeMLHeader = true;
    				break;
    			}    			
    			line = reader.readLine();	
    		}
    		return hasTreeMLHeader;
        }
    }
}