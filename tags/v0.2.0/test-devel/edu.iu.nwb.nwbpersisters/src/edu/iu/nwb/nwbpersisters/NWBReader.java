package edu.iu.nwb.nwbpersisters;

import java.io.File;
import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.nwbpersisters.FileBackedNWBModel;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBReader implements AlgorithmFactory {
	   private MetaTypeProvider provider;

	   protected void activate(ComponentContext ctxt) {
	    	MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
	        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());  	        
	    }
	   protected void deactivate(ComponentContext ctxt) {
	        provider = null;
	    }

	   /**
	    * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	    */
	   public MetaTypeProvider createParameters(Data[] dm) {
	        return provider;
	    }
	    
	    /**
	     * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	     */
	    public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
	            CIShellContext context) {
	        return new NWBReaderPersister(dm, parameters);
	    }
	    
	    private class NWBReaderPersister implements Algorithm {
	    	Data[] data;
	        Dictionary parameters;
	        
	        public NWBReaderPersister(Data[] dm, Dictionary parameters) {
	        	this.data = dm;
	            this.parameters = parameters;
	        }

	        public Data[] execute() {
//	            String fileHandler = (String) parameters.get("edu.iu.nwb.nwbpersisters.NWBReader.fileInput");
	        	String fileHandler = (String) data[0].getData();
	        	File frd = new File(fileHandler);
	            FileBackedNWBModel nwbData = new FileBackedNWBModel(frd);
	            Data[] dm = new Data[]{new BasicData(nwbData, NWBModel.class.getName()) };
	            dm[0].getMetaData().put(DataProperty.LABEL, "NWB Data Model: " + fileHandler);
	            
	            return dm;
	        }
	    }
}






  

    
    
    

