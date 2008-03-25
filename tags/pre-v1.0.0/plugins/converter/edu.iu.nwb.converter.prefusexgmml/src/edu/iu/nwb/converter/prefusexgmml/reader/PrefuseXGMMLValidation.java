package edu.iu.nwb.converter.prefusexgmml.reader;

import java.io.BufferedReader;
import java.io.File;
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
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;

import edu.berkeley.guir.prefuse.graph.io.XMLGraphReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseXGMMLValidation implements AlgorithmFactory {
 
    protected void activate(ComponentContext ctxt) {
    }
    protected void deactivate(ComponentContext ctxt) {
     }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseXGMMLValidationAlg(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
    }
    
    public class PrefuseXGMMLValidationAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        LogService logger;
        
        public PrefuseXGMMLValidationAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
           	logger = (LogService)context.getService(LogService.class.getName());
        }

        public Data[] execute() {
//	 

			String fileHandler = (String) data[0].getData();
			File inData = new File(fileHandler);
        	try{
        		if (validateXGMMLHeader(inData)){
	        		(new XMLGraphReader()).loadGraph(fileHandler);
	        		Data[] dm = new Data[] {new BasicData(inData, "file:text/xgmml+xml")};
	        		dm[0].getMetaData().put(DataProperty.LABEL, "Prefuse XGMML .xml file: " + fileHandler);
	                dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
	        		return dm;
        		}else
        			return null;   
        	}catch (FileNotFoundException e){
        		logger.log(LogService.LOG_ERROR, "Could not find the specified XGMML file for validation.",e );
        		return null;
        	}catch (IOException ioe){
        		logger.log(LogService.LOG_ERROR, "IO error while validating the specified XGMML file.", ioe);
//        		logger.log(LogService.LOG_ERROR, "IOException", ioe);
        		return null;
        	}

        }
        
        //TODO Bonnie
        //This is a temporary fix. The problem is after we get rid of LoadDataChooser
        //It seems that prefuse library can load xgmml file(.xml) in as a graphml.
        //but all visualization algs didn't work since it is not a real graphml file.
        //It also possible that sometimes prefuse can load gramphml file in as an xgmml.
        //although the reverse path never happened in my testing env. 
        //Here I try to detect if there is "<graphml" header in the file, if not, it 
        //is not a gramphml file
        private boolean validateXGMMLHeader(File inData)
        		throws FileNotFoundException, IOException{
        	boolean hasXGMMLHeader = false;
    		
        	BufferedReader reader = 
    			new BufferedReader(new FileReader(inData));
    		
    		String line = reader.readLine();
    		while(line != null){
    			if(line.startsWith("<graphml")) {
    				hasXGMMLHeader = false;
    				break;
    			}
    			if(line.startsWith("<graph ") || line.startsWith("<graph>")){
    				hasXGMMLHeader = true;
    				break;
    			}    			
    			line = reader.readLine();	
    		}
    		return hasXGMMLHeader;
    		
        }

    }
}