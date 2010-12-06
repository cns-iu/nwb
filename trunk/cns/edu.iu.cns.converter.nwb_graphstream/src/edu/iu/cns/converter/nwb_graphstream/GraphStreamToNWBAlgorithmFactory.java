package edu.iu.cns.converter.nwb_graphstream;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;

import edu.iu.cns.graphstream.common.AnnotatedGraph;

public class GraphStreamToNWBAlgorithmFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	Data inputData = data[0];
    	AnnotatedGraph inputGraph = (AnnotatedGraph) inputData.getData();

        return new GraphStreamToNWBAlgorithm(inputData, inputGraph);
    }
}