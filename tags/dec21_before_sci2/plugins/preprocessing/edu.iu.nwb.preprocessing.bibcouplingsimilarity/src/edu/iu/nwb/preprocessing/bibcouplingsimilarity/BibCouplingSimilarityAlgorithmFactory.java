package edu.iu.nwb.preprocessing.bibcouplingsimilarity;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;


public class BibCouplingSimilarityAlgorithmFactory implements AlgorithmFactory {
    private BundleContext bContext;

    protected void activate(ComponentContext ctxt) {
        bContext = ctxt.getBundleContext();
    }
    protected void deactivate(ComponentContext ctxt) {
    }
    
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new BibCouplingSimilarityAlgorithm(data, parameters, context, bContext);
    }
}