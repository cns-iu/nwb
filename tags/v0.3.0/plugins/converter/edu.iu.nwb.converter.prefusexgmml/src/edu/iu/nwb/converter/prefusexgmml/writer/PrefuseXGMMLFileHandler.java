package edu.iu.nwb.converter.prefusexgmml.writer;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseXGMMLFileHandler implements AlgorithmFactory {
 
    protected void activate(ComponentContext ctxt) {
    }
    protected void deactivate(ComponentContext ctxt) {
     }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseXGMMLFileHandlerAlg(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
    }

    public class PrefuseXGMMLFileHandlerAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        LogService logger;
        
        public PrefuseXGMMLFileHandlerAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
            logger=(LogService)context.getService(LogService.class.getName());
        }

        public Data[] execute() {
        	Object inData = data[0].getData();
        	String format = data[0].getFormat();
        	if(inData instanceof File && format.equals("file:text/xgmml+xml")){
        		return new Data[]{new BasicData(inData, "file-ext:xml")};          		
        	}
        	else {
        		if (!(inData instanceof File))        				
        			logger.log(LogService.LOG_ERROR, "Expect a File, but the input data is "+inData.getClass().getName());
        		else if (!format.equals("file:text/xgmml+xml"))
        			logger.log(LogService.LOG_ERROR, "Expect file:text/xgmml+xml, but the input format is "+format);
       			return null;
        	}     	
        }

    }
}