package edu.iu.cns.inspectdata;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

public class DataInspectorAlgorithmFactory implements AlgorithmFactory {
	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.logger = (LogService) componentContext.locateService("LOG");
	}

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
        return new DataInspectorAlgorithm(data[0], this.logger);
    }
}