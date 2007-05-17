package edu.iu.nwb.converter.treegraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.osgi.service.metatype.MetaTypeProvider;

import prefuse.data.Graph;


public class ConverterAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new ConverterAlgorithm(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return null;
    }
    
    public class ConverterAlgorithm implements Algorithm, AlgorithmProperty {
        Data[] data;
        Dictionary parameters;
        CIShellContext context;
        
        public ConverterAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
            this.parameters = parameters;
            this.context = context;
        }

        public Data[] execute() {
            return data;
        }
    }
}