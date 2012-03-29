package edu.iu.nwb.analysis.nodebetweennesscentrality;

import java.util.Dictionary;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.graphstream.algorithm.BetweennessCentrality;

import edu.iu.cns.graphstream.common.AnnotatedGraph;
import edu.iu.cns.graphstream.common.Utilities;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class NodeBetweennessCentralityAlgorithm implements Algorithm {
    private Data inputData;
    private AnnotatedGraph graph;
    private String weightAttributeName;
    private boolean isWeighted;
    private String centralityAttributeName;
    
    public NodeBetweennessCentralityAlgorithm(
    		Data inputData,
    		AnnotatedGraph graph,
    		String weightAttributeName,
    		boolean isWeighted,
    		String centralityAttributeName) {
        this.inputData = inputData;
        this.graph = graph;
        this.weightAttributeName = weightAttributeName;
        this.isWeighted = isWeighted;
        this.centralityAttributeName = centralityAttributeName;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	BetweennessCentrality betweennessCentralityCalculator =
    		createBetweennessCentralityCalculator();
    	AnnotatedGraph outputGraph = createOutputGraph();
    	betweennessCentralityCalculator.betweennessCentrality(outputGraph);

        return wrapAsData(outputGraph);
    }

    private AnnotatedGraph createOutputGraph() {
    	AnnotatedGraph outputGraph = new AnnotatedGraph(this.graph);
    	Utilities.addNodeAttribute(
    		outputGraph, this.centralityAttributeName, NWBFileProperty.TYPE_FLOAT);

    	return outputGraph;
    }

    private BetweennessCentrality createBetweennessCentralityCalculator() {
    	if (this.isWeighted) {
    		return new BetweennessCentrality(
    			this.centralityAttributeName, this.weightAttributeName);
    	} else {
    		return new BetweennessCentrality(this.centralityAttributeName);
    	}
    }

    private Data[] wrapAsData(AnnotatedGraph outputGraph) {
    	Data outputData = new BasicData(outputGraph, outputGraph.getClass().getName());
    	Dictionary<String, Object> metadata = outputData.getMetadata();
    	metadata.put(DataProperty.PARENT, this.inputData);
    	String label =
    		String.format("With '%s' as the Betweenness Centrality", this.centralityAttributeName);
    	metadata.put(DataProperty.LABEL, label);
    	metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);

    	return new Data[] { outputData };
    }
}