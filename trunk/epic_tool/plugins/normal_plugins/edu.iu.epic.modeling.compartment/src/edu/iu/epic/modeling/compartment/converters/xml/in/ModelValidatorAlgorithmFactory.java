package edu.iu.epic.modeling.compartment.converters.xml.in;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

public class ModelValidatorAlgorithmFactory implements AlgorithmFactory {
	@SuppressWarnings("unchecked") // Raw Dictionary
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		Data inputData = data[0];
		String inputModelFilePath = (String) data[0].getData();
		LogService logger = (LogService) context.getService(LogService.class.getName());
		
		return new ModelValidatorAlgorithm(inputData, inputModelFilePath, logger);
	}
}
