package edu.iu.cns.database.extract.generic;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

public class ExtractTableFactory implements AlgorithmFactory {
	
    protected static final String QUERY_KEY = "query";
	protected static final Object LABEL_KEY = "label";
    
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ExtractTable(data, parameters, context);
    }
}