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
import org.cishell.utilities.UnicodeReader;
import org.osgi.service.log.LogService;

import prefuse.data.io.DataIOException;
import prefuse.data.io.TreeMLReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseTreeMLValidation implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseTreeMLValidationAlg(data, parameters, context);
    }
    
    public class PrefuseTreeMLValidationAlg implements Algorithm {
		public static final String TREEML_MIME_TYPE = "file:text/treeml+xml";
		
		private String inTreeMLFile;
        
		
        public PrefuseTreeMLValidationAlg(
        		Data[] data, Dictionary parameters, CIShellContext context) {
			this.inTreeMLFile = (String) data[0].getData();
        }

        
        public Data[] execute() throws AlgorithmExecutionException {
        	File inData = new File(inTreeMLFile);
        	
        	try{
        		if (validateTreeMLHeader(inData)){
	        		(new TreeMLReader()).readGraph(
	        				new FileInputStream(inTreeMLFile));
	        		return createOutData(inData);
        		} else {
        			throw new AlgorithmExecutionException(
        					"TreeML file does not contain a valid header.");
        		}
        	} catch (DataIOException e) {
				throw new AlgorithmExecutionException(e.getMessage(), e);
        	} catch (SecurityException e) {
        		throw new AlgorithmExecutionException(e.getMessage(), e);
        	} catch (FileNotFoundException e) {
        		throw new AlgorithmExecutionException(e.getMessage(), e);
        	} catch (IOException e) {
        		throw new AlgorithmExecutionException(e.getMessage(), e);
        	}
        }

		private Data[] createOutData(File inData) {
			Data[] dm = new Data[] {new BasicData(inData, TREEML_MIME_TYPE)};
			dm[0].getMetadata().put(DataProperty.LABEL,
					"Prefuse TreeML file: " + inTreeMLFile);
			dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.TREE_TYPE);
			return dm;
		}
        
        /* TODO:  This is a temporary fix. The problem is after we get rid of
         * LoadDataChooser.  It seems that prefuse library can load
         * xgmml file(.xml) in as a graphml.  But all visualization algs didn't
         * work since it is not a real graphml file.  Here I try to detect if
         * there is "<graphml" header in the file, if not, it is not a gramphml
         * file.
         */
        private boolean validateTreeMLHeader(File inData)
        		throws FileNotFoundException, IOException {
        	boolean hasTreeMLHeader = false;
    		
        	BufferedReader reader = 
    			new BufferedReader(new UnicodeReader(new FileInputStream(inData)));
    		
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