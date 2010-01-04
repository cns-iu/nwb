package edu.iu.cns.database.extract.generic;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

public class ExtractTableWithQueryFactory implements AlgorithmFactory {
	
	private static final String QUERY_PROPERTY = "query";
	private static final String LABEL_PROPERTY = "output_label";

	private static AlgorithmFactory extractFactory = new ExtractTableFactory();
	
	private Dictionary constructedParameters = new Hashtable();
	
	
	protected void activate(ComponentContext ctxt) {
		Dictionary properties = ctxt.getProperties();
		constructedParameters.put(ExtractTableFactory.QUERY_KEY, properties.get(QUERY_PROPERTY));
		constructedParameters.put(ExtractTableFactory.LABEL_KEY, properties.get(LABEL_PROPERTY));
	}
	
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return extractFactory.createAlgorithm(data, constructedParameters, context);
    }
}