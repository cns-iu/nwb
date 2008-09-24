package edu.iu.nwb.converter.prefusetreeml.writer;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseTreeMLFileHandler implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseTreeMLFileHandlerAlg(data, parameters, context);
    }
    public class PrefuseTreeMLFileHandlerAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        LogService logger;
        
        public PrefuseTreeMLFileHandlerAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
            logger=(LogService)context.getService(LogService.class.getName());
        }

        public Data[] execute() {
        	return new Data[]{new BasicData(data[0].getMetadata(),data[0].getData(),data[0].getFormat())};
        }
    }
}