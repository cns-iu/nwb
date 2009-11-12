package edu.iu.epic.modeling.compartment.converters.text.out;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;

import edu.iu.epic.modeling.compartment.converters.text.Constants;
import edu.iu.epic.modeling.compartment.model.Model;

public class ModelWriterAlgorithm implements Algorithm {
	private Model inputModel;


	public ModelWriterAlgorithm(Data[] data) {
		this.inputModel = (Model) data[0].getData();
	}


	public Data[] execute() throws AlgorithmExecutionException {
		try {
			File outputModelFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
					"CompartmentModelFileWriter-", "mdl");

			Writer writer = new FileWriter(outputModelFile);
			writer.write(inputModel.toString());
			writer.close();

			return new Data[]{ new BasicData(outputModelFile, Constants.MODEL_MIME_TYPE) };
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Error writing model file: " + e.getMessage(), e);
		}
	}
}
