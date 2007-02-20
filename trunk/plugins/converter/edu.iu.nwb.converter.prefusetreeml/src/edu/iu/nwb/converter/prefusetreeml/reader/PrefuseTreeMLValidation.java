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
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.component.ComponentContext;
//import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;

import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;
import prefuse.data.io.TreeMLReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseTreeMLValidation implements AlgorithmFactory {
    protected void activate(ComponentContext ctxt) {
    }
    protected void deactivate(ComponentContext ctxt) {
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseGraphMLValidationAlg(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
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

        public Data[] execute() {
//	    	LogService logger = (LogService)context.getService(LogService.class.getName());
			
        	String fileHandler = (String) data[0].getData();
        	File inData = new File(fileHandler);
        	
        	try{
        		if (validateTreeMLHeader(inData)){
	        		(new TreeMLReader()).readGraph(new FileInputStream(fileHandler));
	        		Data[] dm = new Data[] {new BasicData(inData, "file:text/treeml+xml")};
	        		dm[0].getMetaData().put(DataProperty.LABEL, "Prefuse TreeML file: " + fileHandler);
	                dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.TREE_TYPE);
	        		return dm;
        		}else {
            		return null;
        		}
        	}catch (DataIOException dioe){
//				logger.log(LogService.LOG_ERROR, "Might not be a GraphML file. Got the following exception");
//        		logger.log(LogService.LOG_ERROR, "DataIOException", dioe);
        		return null;
        	}catch (SecurityException exception){
//				logger.log(LogService.LOG_ERROR, "Might not be a GraphML file. Got the following exception");
//        		logger.log(LogService.LOG_ERROR, "SecurityException", exception);
        		return null;
        	}catch (FileNotFoundException e){
//       		logger.log(LogService.LOG_ERROR, "FileNotFoundException", e);
        		return null;
        	}catch (IOException ioe){
//       		logger.log(LogService.LOG_ERROR, "IOException", ioe);
        		return null;
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