package edu.iu.nwb.nwbpersisters;

import java.util.Dictionary;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBWriter implements AlgorithmFactory {
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
	        return new NWBWriterPersister(dm, parameters, context);
	    }
	    
	    private class NWBWriterPersister implements Algorithm {
	        Data[] dm;
	        Dictionary parameters;
	        CIShellContext ciContext;
	        
	        public NWBWriterPersister(Data[] dm, Dictionary parameters,
	        		CIShellContext ciContext) {
	        	this.dm = dm;
	        	this.parameters = parameters;
	            this.ciContext = ciContext;
	        }

	        public Data[] execute() {
	            String fileHandler = (String) parameters.get("edu.iu.nwb.nwbpersisters.NWBReader.fileInput");
	            System.out.println("in NWBWriterPersister execute method, input fileHandler= "+fileHandler);
	    		(new NWBFile()).save((NWBModel)(dm[0].getData()), fileHandler) ;

	            return null;
	        }
	    }
}






  

    
    
    

