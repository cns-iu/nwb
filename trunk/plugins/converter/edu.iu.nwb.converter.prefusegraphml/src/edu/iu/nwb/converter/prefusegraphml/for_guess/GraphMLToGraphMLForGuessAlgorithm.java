package edu.iu.nwb.converter.prefusegraphml.for_guess;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.converter.prefusegraphml.reader.PrefuseGraphMLReader;
import edu.iu.nwb.converter.prefusegraphml.writer.PrefuseGraphMLWriter;


public class GraphMLToGraphMLForGuessAlgorithm implements Algorithm {
	public static final String GRAPH_ML_FOR_GUESS_MIME_TYPE = "file:text/graphml_for_guess+xml";
	public static final String DEFAULT_LABEL = "Untitled";

	private Data[] inputData;
    private CIShellContext context;
	
    public GraphMLToGraphMLForGuessAlgorithm(Data[] inputData, CIShellContext context) {
     	this.inputData = inputData;
    	this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	try {
    		Data[] graphData =
				(new PrefuseGraphMLReader(this.inputData, true, this.context)).execute();
			Data[] outputData = (new PrefuseGraphMLWriter(graphData)).execute();
    		
    		return createOutData(outputData);
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }

	private Data[] createOutData(Data[] originalOutputData) {
		Data outputData =
			new BasicData((File) originalOutputData[0].getData(), GRAPH_ML_FOR_GUESS_MIME_TYPE);
		Dictionary<String, Object> inputMetadata = this.inputData[0].getMetadata();
		Dictionary<String, Object> outputMetadata = outputData.getMetadata();
		outputMetadata.put(DataProperty.PARENT, this.inputData[0]);
		outputMetadata.put(DataProperty.LABEL, determineLabel(inputMetadata));
		outputMetadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);

		return new Data[] { outputData };
	}

	private String determineLabel(Dictionary<String, Object> inputMetadata) {
		Object label = inputMetadata.get(DataProperty.LABEL);

		if (label != null) {
			return label.toString();
		} else {
			return DEFAULT_LABEL;
		}
	}
}