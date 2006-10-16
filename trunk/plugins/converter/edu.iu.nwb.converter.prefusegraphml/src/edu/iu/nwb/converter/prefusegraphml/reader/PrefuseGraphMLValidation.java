package edu.iu.nwb.converter.prefusegraphml.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.osgi.service.metatype.MetaTypeService;

import prefuse.data.io.DataIOException;
import prefuse.data.io.GraphMLReader;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseGraphMLValidation implements AlgorithmFactory {
    private MetaTypeProvider provider;

    protected void activate(ComponentContext ctxt) {
        //You may delete all references to metatype service if 
        //your algorithm does not require parameters and return
        //null in the createParameters() method
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());       
    }
    protected void deactivate(ComponentContext ctxt) {
        provider = null;
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PrefuseGraphMLValidationAlg(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return provider;
    }
    
    public class PrefuseGraphMLValidationAlg implements Algorithm {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        
        public PrefuseGraphMLValidationAlg(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
        }

        public Data[] execute() {
	    	LogService logger = (LogService)context.getService(LogService.class.getName());
			
        	String fileHandler = (String) data[0].getData();
        	File inData = new File(fileHandler);
        	try{
        		(new GraphMLReader()).readGraph(new FileInputStream(fileHandler));
        		Data[] dm = new Data[] {new BasicData(inData, "file:text/graphml+xml")};
        		dm[0].getMetaData().put(DataProperty.LABEL, "Prefuse GraphML file: " + fileHandler);
                dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
        		return dm;
        	}catch (DataIOException dioe){
        		logger.log(LogService.LOG_ERROR, "DataIOException", dioe);
        		return null;
        	}catch (SecurityException exception){
        		logger.log(LogService.LOG_ERROR, "SecurityException", exception);
        		return null;
        	}catch (FileNotFoundException e){
        		logger.log(LogService.LOG_ERROR, "FileNotFoundException", e);
        		return null;
        	}
        	
        }
    }
}