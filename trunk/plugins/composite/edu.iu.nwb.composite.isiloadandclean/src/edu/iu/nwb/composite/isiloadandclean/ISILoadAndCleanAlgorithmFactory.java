package edu.iu.nwb.composite.isiloadandclean;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;


public class ISILoadAndCleanAlgorithmFactory implements AlgorithmFactory {
    private MetaTypeProvider provider;
    private BundleContext bContext;
    
    private LogService log;
    private AlgorithmFactory isiValidator;
    private AlgorithmFactory isiToPrefuseConverter;
    private AlgorithmFactory isiDupRemover;
    
    protected void activate(ComponentContext ctxt) {
    	this.log = (LogService) 
    		ctxt.locateService("LOG");
    	
        bContext = ctxt.getBundleContext();
    }
    
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
    	String filter = "";
        try {
        	filter = "(& (in_data=file-ext:isi) (out_data=file:text/isi))";
        	isiValidator = getAlgorithmFactory(filter);
        	
        	filter = "(& (in_data=file:text/isi) (out_data=prefuse.data.Table))";
        	isiToPrefuseConverter = getAlgorithmFactory(filter);
        	
        	filter = "(service.pid=edu.iu.nwb.analysis.isidupremover.ISIDupRemoverAlgorithm)";
        	isiDupRemover = getAlgorithmFactory(filter);
			
		} catch (InvalidSyntaxException e) {
			log.log(LogService.LOG_ERROR, "Invalid syntax in filter " + filter);
		}
		
        return new ISILoadAndCleanAlgorithm(data, parameters, context,
        		isiValidator, isiToPrefuseConverter, isiDupRemover);
    }
    
    private AlgorithmFactory getAlgorithmFactory (String filter) 
    throws InvalidSyntaxException {
    	ServiceReference[] algFactoryRefs = bContext
    	.getServiceReferences(AlgorithmFactory.class.getName(), filter);
    	
    	if (algFactoryRefs != null && algFactoryRefs.length != 0) {
    		ServiceReference algFactoryRef = algFactoryRefs[0];
    		
    		AlgorithmFactory algFactory = 
    			(AlgorithmFactory) bContext.getService(algFactoryRef);
    		
    		return algFactory;
    	} else {
    		this.log.log(LogService.LOG_ERROR, "ISI Load and Clean Algorithm" +
    				" was unable to find an algorithm that satisfied the " +
    				"following filter: " + filter);
    		return null;
    	}
    	
    }
}