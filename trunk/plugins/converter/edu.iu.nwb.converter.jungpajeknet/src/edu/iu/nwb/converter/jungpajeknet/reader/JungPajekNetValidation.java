package edu.iu.nwb.converter.jungpajeknet.reader;

//Java
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import org.cishell.framework.data.DataProperty;

//Jung
import edu.uci.ics.jung.io.PajekNetReader;


public class JungPajekNetValidation implements AlgorithmFactory {

    protected void activate(ComponentContext ctxt) {  }
    protected void deactivate(ComponentContext ctxt) {   }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new JungPajekNetValidationAlg(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
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

        public Data[] execute() {
	    	LogService logger = (LogService)context.getService(LogService.class.getName());
		
        	String fileHandler = (String) data[0].getData();
        	File inData = new File(fileHandler);
        	try{
        		(new PajekNetReader()).load(new FileReader(inData));
        		Data[] dm = new Data[] {new BasicData(inData, "file:application/pajek")};
        		dm[0].getMetaData().put(DataProperty.LABEL, "Pajek .net file: " + fileHandler);
                dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
        		return dm;
			}catch (FileNotFoundException exception){
				logger.log(LogService.LOG_ERROR,"FileNotFoundException",exception);
				return null;
			}catch (Exception e){
				logger.log(LogService.LOG_ERROR, "Exception", e);
				return null;	
			}

        }
    }
}