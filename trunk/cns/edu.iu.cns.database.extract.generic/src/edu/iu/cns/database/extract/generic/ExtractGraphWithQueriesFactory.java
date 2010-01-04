package edu.iu.cns.database.extract.generic;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

public class ExtractGraphWithQueriesFactory implements AlgorithmFactory {
	
	protected static final String NODE_QUERY_PROPERTY = "node_query";
    protected static final String EDGE_QUERY_PROPERTY = "edge_query";
    protected static final String ID_COLUMN_PROPERTY = "id_column";
    protected static final String SOURCE_COLUMN_PROPERTY = "source_column";
    protected static final String TARGET_COLUMN_PROPERTY = "target_column";
    protected static final Object DIRECTED_PROPERTY = "directed";
	private static final String LABEL_PROPERTY = "output_label";

	private static AlgorithmFactory extractFactory = new ExtractGraphFactory();
	
	private Dictionary constructedParameters = new Hashtable();
	
	
	protected void activate(ComponentContext ctxt) {
		Dictionary properties = ctxt.getProperties();

		constructedParameters.put(ExtractGraphFactory.NODE_QUERY_KEY, properties.get(NODE_QUERY_PROPERTY));
		constructedParameters.put(ExtractGraphFactory.EDGE_QUERY_KEY, properties.get(EDGE_QUERY_PROPERTY));
		constructedParameters.put(ExtractGraphFactory.ID_COLUMN_KEY, properties.get(ID_COLUMN_PROPERTY));
		constructedParameters.put(ExtractGraphFactory.SOURCE_COLUMN_KEY, properties.get(SOURCE_COLUMN_PROPERTY));
		constructedParameters.put(ExtractGraphFactory.TARGET_COLUMN_KEY, properties.get(TARGET_COLUMN_PROPERTY));
		constructedParameters.put(ExtractGraphFactory.DIRECTED_KEY, new Boolean((String) properties.get(DIRECTED_PROPERTY)));
		constructedParameters.put(ExtractGraphFactory.LABEL_KEY, properties.get(LABEL_PROPERTY));
	}
	
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return extractFactory.createAlgorithm(data, constructedParameters, context);
    }
}