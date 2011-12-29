package edu.iu.cns.converter.nwb_graphstream;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.cns.graphstream.common.AnnotatedGraph;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class NWBToGraphStreamAlgorithm implements Algorithm {
	public static final String OUT_LABEL = "Converted to GraphStream";

    private Data inputData;
    private File nwbFile;
    
    public NWBToGraphStreamAlgorithm(Data inputData, File nwbFile) {
        this.inputData = inputData;
        this.nwbFile = nwbFile;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		NWBIntoAnnotatedGraphFileParser converter = new NWBIntoAnnotatedGraphFileParser();
    		NWBFileParser nwbParser = new NWBFileParser(this.nwbFile);
    		nwbParser.parse(converter);
    		AnnotatedGraph graph = converter.getGraph();

    		return wrapGraphAsOutputData(graph);
    	} catch (IOException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	} catch (ParsingException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    }

    private Data[] wrapGraphAsOutputData(AnnotatedGraph graph) {
    	Data outputGraphData = new BasicData(graph, graph.getClass().getName());
    	Dictionary<String, Object> outputGraphMetadata = outputGraphData.getMetadata();
    	outputGraphMetadata.put(DataProperty.LABEL, OUT_LABEL);
    	outputGraphMetadata.put(DataProperty.PARENT, inputData);
    	outputGraphMetadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    	
    	return new Data[] { outputGraphData };
    }
}