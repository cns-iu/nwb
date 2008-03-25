package edu.iu.nwb.converter.jungpajeknet.writer;

import java.io.File;
import java.util.Dictionary;
//OSGi
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;
//CIShell
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class JungPajekNetFileHandler implements AlgorithmFactory {

    protected void activate(ComponentContext ctxt) {  }
    protected void deactivate(ComponentContext ctxt) {   }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new JungPajekNetFileHandlerAlg(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
    }    

    public class JungPajekNetFileHandlerAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        LogService logger;
        
        public JungPajekNetFileHandlerAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
            logger = (LogService) context.getService(LogService.class.getName());
        }

        public Data[] execute() {
        	Object inData = data[0].getData();
        	String format = data[0].getFormat();
        	if(inData instanceof File && format.equals("file:application/pajek")){
        		return new Data[]{new BasicData(inData, "file-ext:net")};          		
        	}
        	else {
        		if (!(inData instanceof File))        				
        			logger.log(LogService.LOG_ERROR, "Expect a File, but the input data is "+inData.getClass().getName());
        		else if (!format.equals("file:application/pajek"))
        			logger.log(LogService.LOG_ERROR, "Expect file:application/pajek, but the input format is "+format);
       			return null;
        	}     	
        }
    }
}