package edu.iu.nwb.converter.prefusegraphml.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import prefuse.data.Graph;
import prefuse.data.io.DataIOException;

public class PrefuseGraphMLValidation implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
        return new PrefuseGraphMLValidationAlgorithm(data);
    }
    
    public class PrefuseGraphMLValidationAlgorithm implements Algorithm {
    	private Data[] inputData;
		private String inGraphMLFileName;
        
        public PrefuseGraphMLValidationAlgorithm(Data[] data) {
        	this.inputData = data;
			this.inGraphMLFileName = (String) data[0].getData();
        }

        public Data[] execute() throws AlgorithmExecutionException {
        	File inGraphMLFile = new File(inGraphMLFileName);
        	
        	try {
        		if (validateGraphMLHeader(inGraphMLFile)) {
	        		(new GraphMLReaderModified(false)).readGraph(
	        			new FileInputStream(inGraphMLFileName));

	        		return createOutData(inGraphMLFile);
        		} else {
            		throw new AlgorithmExecutionException("Unable to validate GraphML file.");
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

		private Data[] createOutData(File inGraphMLFile) {
			Data outputData = new BasicData(inGraphMLFile, "file:text/graphml+xml");
			String label = String.format("Prefuse GraphML file: %s", inGraphMLFileName);
			outputData.getMetadata().put(DataProperty.LABEL, label);
			outputData.getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);

			return new Data[] { outputData };
		}
        
        /* TODO:  This is a temporary fix. The problem is after we get rid of
         * LoadDataChooser.  It seems that prefuse library can load
         * xgmml file(.xml) in as a graphml.  But all visualization algs didn't
         * work since it is not a real graphml file.  Here I try to detect if
         * there is "http://graphml.graphdrawing.org/xmlns" namespace in the
         * file, if not, it is not a gramphml file.
         */
        private boolean validateGraphMLHeader(File inData)
        		throws FileNotFoundException, IOException {
        	boolean hasGraphMLHeader = false;
        	BufferedReader reader =
        		new BufferedReader(new UnicodeReader(new FileInputStream(inData)));
    		String line = reader.readLine();

    		while (line != null) {
    			if (line.indexOf("http://graphml.graphdrawing.org/xmlns")!= -1) {
    				hasGraphMLHeader = true;

    				break;
    			} 

    			line = reader.readLine();	
    		}
    		
    		return hasGraphMLHeader;    		
        }
    }
}