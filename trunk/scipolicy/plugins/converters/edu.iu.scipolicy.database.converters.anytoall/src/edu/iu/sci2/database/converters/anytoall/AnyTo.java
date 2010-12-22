package edu.iu.sci2.database.converters.anytoall;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

public class AnyTo implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    public AnyTo(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
        return data;
    }
}