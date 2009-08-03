package edu.iu.nwb.converter.jungpajeknet.writer;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JungPajekNetFileHandler implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext context) {
        return new JungPajekNetFileHandlerAlgorithm(data, parameters, context);
    }

    
    public class JungPajekNetFileHandlerAlgorithm implements Algorithm {
        private Data[] data;
        
        public JungPajekNetFileHandlerAlgorithm(Data[] data,
        								  Dictionary parameters,
        								  CIShellContext context) {
            this.data = data;
        }

        public Data[] execute() {
        	return AlgorithmUtilities.cloneSingletonData(data);        
        }		
    }
}