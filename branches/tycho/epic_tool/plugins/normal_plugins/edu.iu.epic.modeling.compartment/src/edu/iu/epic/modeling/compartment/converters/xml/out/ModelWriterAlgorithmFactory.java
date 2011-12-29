package edu.iu.epic.modeling.compartment.converters.xml.out;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

import edu.iu.epic.modeling.compartment.model.Model;

public class ModelWriterAlgorithmFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		Data inputData = data[0];
		Model inputModel = (Model) data[0].getData();
		LogService logger = (LogService) context.getService(LogService.class.getName());
		
		return new ModelWriterAlgorithm(inputData, inputModel, logger);
	}
}
