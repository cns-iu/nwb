package edu.iu.epic.modeling.compartment.converters.text.out;

import java.io.File;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.iu.epic.modeling.compartment.converters.text.Constants;

public class ModelHandlerAlgorithm implements Algorithm {
	private File inputModelFile;
	private String inputFormat;


	public ModelHandlerAlgorithm(Data[] data) {
		this.inputModelFile = (File) data[0].getData();
		this.inputFormat = data[0].getFormat();
	}


	public Data[] execute() throws AlgorithmExecutionException {
		if (Constants.MODEL_MIME_TYPE.equals(inputFormat)) {
			return new Data[]{ new BasicData(inputModelFile, Constants.MODEL_FILE_EXTENSION) };
		} else {
			String message = String.format(
				"Expected %s, but the input format is %s.",
				Constants.MODEL_MIME_TYPE,
				inputFormat);
			throw new AlgorithmExecutionException(message);
		}
    }
}
