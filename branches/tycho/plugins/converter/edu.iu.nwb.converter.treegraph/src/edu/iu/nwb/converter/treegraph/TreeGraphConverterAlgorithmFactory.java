package edu.iu.nwb.converter.treegraph;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.AlgorithmProperty;
import org.cishell.framework.data.Data;


public class TreeGraphConverterAlgorithmFactory implements AlgorithmFactory {
    public Algorithm createAlgorithm(
    		Data[] data, Dictionary parameters, CIShellContext context) {
        return new TreeGraphConverterAlgorithm(data, parameters, context);
    }
    
    public class TreeGraphConverterAlgorithm
    		implements Algorithm, AlgorithmProperty {
        private Data[] data;
        
        public TreeGraphConverterAlgorithm(
        		Data[] data, Dictionary parameters, CIShellContext context) {
            this.data = data;
        }

        public Data[] execute() {
            return data;
        }
    }
}