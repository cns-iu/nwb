package edu.iu.nwb.analysis.pathfindergraphnetworkscaling;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;

import edu.iu.iv.analysis.pathfindernetworkscaling.PathFinderAlgorithmFactory;


public class PathfinderGraphAlgorithmFactory implements AlgorithmFactory {
    private MetaTypeProvider provider;
	private PathFinderAlgorithmFactory pathfinderFactory;

    protected void activate(ComponentContext ctxt) {
        //You may delete all references to metatype service if 
        //your algorithm does not require parameters and return
        //null in the createParameters() method
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());
        
        LogService log = ((LogService) ctxt.locateService("LOG"));
        
        try {
			ServiceReference[] references = ctxt.getBundleContext().getServiceReferences(AlgorithmFactory.class.getName(), "(&(service.pid=edu.iu.iv.analysis.pathfindernetworkscaling.PathFinderAlgorithm))");
			if(references.length == 0) {
				log.log(LogService.LOG_ERROR, "The PathFinderAlgorithm service is not installed");
			} else {
				ServiceReference pathfinderReference = references[0];
				pathfinderFactory = (PathFinderAlgorithmFactory) ctxt.getBundleContext().getService(pathfinderReference);
			}
			
        } catch (InvalidSyntaxException e) {
			
			log.log(LogService.LOG_ERROR, "Invalid syntax when retrieving service reference for PathFinderAlgorithm", e);
		}
    }
    protected void deactivate(ComponentContext ctxt) {
        provider = null;
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new PathfinderGraphAlgorithm(data, parameters, context, pathfinderFactory);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return provider;
    }
}