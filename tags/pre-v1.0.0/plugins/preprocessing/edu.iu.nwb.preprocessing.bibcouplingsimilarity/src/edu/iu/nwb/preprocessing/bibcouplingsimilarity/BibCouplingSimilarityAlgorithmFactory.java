package edu.iu.nwb.preprocessing.bibcouplingsimilarity;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;


public class BibCouplingSimilarityAlgorithmFactory implements AlgorithmFactory {
    private MetaTypeInformation provider;
    private BundleContext bContext;

    protected void activate(ComponentContext ctxt) {
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());
        bContext = ctxt.getBundleContext();
    }
    protected void deactivate(ComponentContext ctxt) {
        provider = null;
    }
    
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new BibCouplingSimilarityAlgorithm(data, parameters, context, bContext);
    }
	public MetaTypeProvider createParameters(Data[] dm) {
		return provider;
	}
}