package edu.iu.nwb.converter.prefusexgmml.reader;

import java.io.File;
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
        
        public PrefuseXGMMLValidationAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
        }

        public Data[] execute() {
	    	LogService logger = (LogService)context.getService(LogService.class.getName());

			String fileHandler = (String) data[0].getData();
			File inData = new File(fileHandler);
        	try{
        		(new XMLGraphReader()).loadGraph(fileHandler);
        		Data[] dm = new Data[] {new BasicData(inData, "file:text/xgmml+xml")};
        		dm[0].getMetaData().put(DataProperty.LABEL, "Prefuse XGMML .xml file: " + fileHandler);
                dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
        		return dm;
        	}catch (IOException ioe){
        		logger.log(LogService.LOG_ERROR, "IOException", ioe);
        		return null;
        	}

        }
    }
}