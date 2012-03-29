package edu.iu.nwb.preprocessing.cocitationsimilarity;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;


public class CoCitationSimilarityAlgorithmFactory implements AlgorithmFactory {
    private BundleContext bContext;

    protected void activate(ComponentContext ctxt) {
        bContext = ctxt.getBundleContext();
    }
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new CoCitationSimilarityAlgorithm(data, parameters, context, bContext);
    }
}