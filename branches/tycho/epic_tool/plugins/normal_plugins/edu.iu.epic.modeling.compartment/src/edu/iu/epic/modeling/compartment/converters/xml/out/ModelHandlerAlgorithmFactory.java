package edu.iu.epic.modeling.compartment.converters.xml.out;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

public class ModelHandlerAlgorithmFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		Data inputData = data[0];
		File modelFile = (File) data[0].getData();
		LogService logger = (LogService) context.getService(LogService.class.getName());
		
		return new ModelHandlerAlgorithm(inputData, modelFile, logger);
	}
}
