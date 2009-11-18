package edu.iu.epic.visualization.linegraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class LineGraphAlgorithmFactory
	implements AlgorithmFactory, ParameterMutator {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext ciShellContext) {
        return new LineGraphAlgorithm(data, parameters, ciShellContext);
    }
    
    public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
    	Table inputTable = (Table) data[0];
    
    	/*
    	 * In the input table, 
    	 * we must choose one numeric or date column to be the 'time step' column,
    	 * and we must decide which of the other numeric columns should be graphed as lines.
    	 * All other columns will be ignored.
    	 */
    	
    	//create a new empty parameters
    	
    	//TODO: Stub. Fill this out
    	return oldParameters;
    }
		
}