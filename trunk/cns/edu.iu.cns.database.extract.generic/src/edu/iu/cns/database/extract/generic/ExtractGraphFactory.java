package edu.iu.cns.database.extract.generic;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class ExtractGraphFactory implements AlgorithmFactory {
	
    protected static final String NODE_QUERY_KEY = "node_query";
    protected static final String EDGE_QUERY_KEY = "edge_query";
    protected static final String ID_COLUMN_KEY = "id_column";
    protected static final String SOURCE_COLUMN_KEY = "source_column";
    protected static final String TARGET_COLUMN_KEY = "target_column";
    protected static final Object DIRECTED_KEY = "directed";
    protected static final Object LABEL_KEY = "label";
    
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ExtractGraph(data, parameters, context);
    }
}