package edu.iu.nwb.converter.jungpajeknet.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.uci.ics.jung.io.PajekNetReader;


public class JungPajekNetValidation implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new JungPajekNetValidationAlg(data, parameters, context);
    }
    
    public class JungPajekNetValidationAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        
        public JungPajekNetValidationAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
        }

        public Data[] execute() throws AlgorithmExecutionException {		
        	String fileHandler = (String) data[0].getData();
        	File inData = new File(fileHandler);
        	try{
        		(new PajekNetReader()).load(new FileReader(inData));
        		Data[] dm = new Data[] {new BasicData(inData, "file:application/pajek")};
        		dm[0].getMetadata().put(DataProperty.LABEL, "Pajek .net file: " + fileHandler);
                dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
        		return dm;
			}catch (FileNotFoundException exception){
				throw new AlgorithmExecutionException(exception.getMessage(),exception);
			}catch (Exception e){
				throw new AlgorithmExecutionException(e.getMessage(),e);	
			}
        }
    }
}