package edu.iu.nwb.converter.prefusexgmml.reader;

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

import edu.berkeley.guir.prefuse.graph.io.XMLGraphReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseXGMMLValidation implements AlgorithmFactory { 
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseXGMMLValidationAlg(data, parameters, context);
    }
    
    public class PrefuseXGMMLValidationAlg implements Algorithm {
		public static final String XGMML_MIME_TYPE = "file:text/xgmml+xml";
		
		private String inXGMMLFileName;
        
		
        public PrefuseXGMMLValidationAlg(
        		Data[] data, Dictionary parameters, CIShellContext context) {
            this.inXGMMLFileName = (String) data[0].getData();
        }

        
        public Data[] execute() throws AlgorithmExecutionException {
        	File inData = new File(inXGMMLFileName);
        	
        	try{
        		if (validateXGMMLHeader(inData)) {
	        		(new XMLGraphReader()).loadGraph(inXGMMLFileName);
	        		
	        		return createOutData(inData);
        		} else {
        			throw new AlgorithmExecutionException(
        				"Couldn't validate " + inXGMMLFileName
        				+ " as an XGMML file.");
        		}
        	} catch (FileNotFoundException e) {
        		throw new AlgorithmExecutionException(
        			"Could not find XGMML file to validate: "
        				+ e.getMessage(), e);
        	} catch (IOException e) {
        		throw new AlgorithmExecutionException(e.getMessage(), e);
        	}
        }

		private Data[] createOutData(File inData) {
			Data[] dm = new Data[]{ new BasicData(inData, XGMML_MIME_TYPE) };
			dm[0].getMetadata().put(
					DataProperty.LABEL,
					"Prefuse XGMML .xml file: " + inXGMMLFileName);
			dm[0].getMetadata().put(
					DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			return dm;
		}
        
        /* TODO Bonnie
         * This is a temporary fix. The problem is after we get rid of
         * LoadDataChooser.  It seems that prefuse library can load xgmml
         * file(.xml) in as a graphml but all visualization algs didn't work
         * since it is not a real graphml file.  It also possible that sometimes
         * prefuse can load graphml file in as an xgmml.  Although the reverse
         * path never happened in my testing env.  Here I try to detect if there
         * is "<graphml" header in the file, if not, it is not a gramphml file.
         */
        private boolean validateXGMMLHeader(File inData)
        		throws FileNotFoundException, IOException {
        	boolean hasXGMMLHeader = false;
    		
        	BufferedReader reader = 
    			new BufferedReader(new UnicodeReader(new FileInputStream(inData)));
    		
    		String line = reader.readLine();
    		while(line != null){
    			if(line.startsWith("<graphml")) {
    				hasXGMMLHeader = false;
    				break;
    			}
    			if(line.startsWith("<graph ") || line.startsWith("<graph>")) {
    				hasXGMMLHeader = true;
    				break;
    			}    			
    			line = reader.readLine();	
    		}
    		
    		return hasXGMMLHeader;    		
        }
    }
}