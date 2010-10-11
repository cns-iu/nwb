package edu.iu.epic.modeling.compartment.converters.text.in;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.log.LogService;

public class ModelValidatorAlgorithmFactory implements AlgorithmFactory {
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		LogService logger = (LogService) context.getService(LogService.class.getName());
		return new ModelValidatorAlgorithm(data, logger);
	}
}
